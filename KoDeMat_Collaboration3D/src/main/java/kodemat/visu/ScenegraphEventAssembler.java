/* 
 * Copyright 2014 Institute fml (TU Munich) and Institute FLW (TU Dortmund).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kodemat.visu;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import kodemat.visu.test.ClientPerformanceTest2;
import kodemat.visu.test.TestResultExporter;
import kodemat.visudata.VisuAttributeChange;
import kodemat.visudata.VisuChange;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.change.VisuCreateComponent;
import kodemat.visudata.change.VisuCreateEdge;
import kodemat.visudata.change.VisuDeleteComponent;
import kodemat.visudata.change.VisuDeleteEdge;
import kodemat.visudata.change.VisuEdgeSourceChange;
import kodemat.visudata.change.VisuEdgeTargetChange;

/**
 * This class tracks and merges all change events done to visual components. It
 * maintains a list of all visual components and edges created so far and tracks
 * all references that exist. These references can be either references to an
 * edge, a child component or a parent component. This class will register all
 * references in several maps for fast access. This should make it possible that
 * the sequence in which object are created does not matter.
 *
 * For example, if a VisuCreateComponent event arrives in the processChange()
 * method then all existing components that have a reference to this object
 * registered will be updated. Vice versa, if this component has references to
 * other components that do not exist yet then it will just register their ids
 * and can rely on this ScenegraphEventAssembler to notify them when the other
 * components are created.
 * 
 * This class is strongly tied to the individual component and edge classes.
 * It is directly known by every component and manages intercomponent relationships.
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class ScenegraphEventAssembler {

    final ConcurrentLinkedQueue<VisuChange> entryQueue = new ConcurrentLinkedQueue<VisuChange>();
    final ConcurrentLinkedQueue<VisuChange> laterQueue = new ConcurrentLinkedQueue<VisuChange>();
    final Map<Long, VisuScenegraphComponent> componentTrackers = new HashMap<Long, VisuScenegraphComponent>();
    final Multimap<Long, VisuScenegraphComponent> childComponents = ArrayListMultimap.create(); //key: id of parent visucomponenet, values: child components
    final Multimap<Long, VisuScenegraphComponent> edgeReferences = ArrayListMultimap.create(); //key: id of edge that is referenced, values: components
    final Map<Long, VisuScenegraphEdge> edgeTrackers = new HashMap<Long, VisuScenegraphEdge>();
    final Multimap<Long, VisuScenegraphEdge> edgeSources = ArrayListMultimap.create(); //key: id of visucomponent, values: the edges that have this comp as source
    final Multimap<Long, VisuScenegraphEdge> edgeTargets = ArrayListMultimap.create(); //key: id of visucomponent, values: the edges that have this comp as target
    final AssetManager assetManager;
    final Node rootNode;
    final VisuHelper helper;
    private boolean shouldReset = false;
    private HashMap<String, ArrayList<String>> spatialEdges;

    public ScenegraphEventAssembler(AssetManager assetManager, Node rootNode, VisuHelper helper) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.helper = helper;
        spatialEdges = new HashMap<String, ArrayList<String>>();
    }

    /*
     * Is called when to add new events from outside the OpenGL thread. This is an important method to idscouple the network traffic from the 
     * visualization. Events can get added every time to the entryqueue even while the rendering is in progress. 
     * The rendering also will never stop because of network issues.
     */
    public void addChangeFromOutsideOpenGL(VisuChange change) {
        entryQueue.add(change);
    }

    /**
     * This method can be used to sort events. It is used, for example, to
     * postpone the execution of sttribute changes when the object is not yet
     * created. This helps in making the sequence of certain events less
     * important.
     *
     * @param change
     */
    public void addChangeToLaterQueue(VisuChange change) {
        laterQueue.add(change);
    }

    /**
     * This method is called regularly in the update phase of the OpenGL thread. It processes all events that have been added to the event queues.
     * If the reset flag has been set all events and components are deleted and the maps cleared. This can be used when the server connection is interrupted.
     */
    public void processChangesWithinOpenGL() {
        if (shouldReset) {
            entryQueue.clear();
            laterQueue.clear();
            for (VisuScenegraphComponent comp : componentTrackers.values()) {
                comp.delete();
            }
            componentTrackers.clear();
            childComponents.clear();
            for (VisuScenegraphEdge scenegraphEdge : edgeTrackers.values()) {
                scenegraphEdge.delete();
            }
            edgeTrackers.clear();
            edgeSources.clear();
            edgeTargets.clear();
            shouldReset = false;
        }
        entryQueue.addAll(laterQueue);
        laterQueue.clear();
        while (!entryQueue.isEmpty()) {
            processChange(entryQueue.poll());
        }
    }

    /**
     * Calling this method will set the shouldReset flag. It is threadsafe way of deleting all events and visual components and edges from outside OpenGL.
     * 
     */
    public void reset() {
        shouldReset = true;
    }

    
    /**
     * This is the main logic method in the class which treats all incoming events and manages the compopnents and their relationships.
     * It creates and deletes components and edges, maintains maps of interdependencies and triggers update and notifications.
     * @param c 
     */
    private void processChange(VisuChange c) {

        if (c instanceof VisuDeleteComponent) {
            long id = ((VisuDeleteComponent)c).getId();
            VisuScenegraphComponent comp = componentTrackers.remove(id);
            if (comp != null) {
                comp.delete();
            }
            notifyEdges(id, null);
            updateChildrenAndEdges(id);
        } else if (c instanceof VisuCreateComponent) {
            long id = ((VisuCreateComponent)c).getId();
            VisuScenegraphComponent comp = componentTrackers.remove(id);
            if (comp != null) {
                comp.delete();
            }
            notifyEdges(id, null);
            comp = new VisuScenegraphComponent(id, "Component " + id, this, spatialEdges);
            componentTrackers.put(id, comp);
            notifyEdges(id, comp);
            updateChildrenAndEdges(id);

        } else if (c instanceof VisuCreateEdge) {
            long id = ((VisuCreateEdge)c).getId();
            VisuScenegraphEdge edge = edgeTrackers.remove(id);
            if (edge != null) {
                edge.delete();
                edgeSources.remove(edge.getSourceId(), edge);
                edgeTargets.remove(edge.getTargetId(), edge);
            }
            edgeTrackers.put(id, new VisuScenegraphEdge(id, "Edge " + id, this));
//            debugDisplayEdgeCollections("CREATE EDGE");
            updateComponentsThatReferenceEdge(id);
        } else if (c instanceof VisuDeleteEdge) {
            long id = ((VisuDeleteEdge)c).getId();
            VisuScenegraphEdge edge = edgeTrackers.remove(id);
            if (edge != null) {
                edge.delete();
                edgeSources.remove(edge.getSourceId(), edge);
                edgeTargets.remove(edge.getTargetId(), edge);
            }
//            debugDisplayEdgeCollections("DELETE EDGE");
            updateComponentsThatReferenceEdge(id);
        } else if (c instanceof VisuEdgeSourceChange) {
            long id = ((VisuEdgeSourceChange)c).getId();
            VisuScenegraphEdge edge = edgeTrackers.get(id);
            if (edge != null) {
                edgeSources.remove(edge.getSourceId(), edge);
                VisuEdgeSourceChange vesc = (VisuEdgeSourceChange) c;
                Long sourceId = vesc.getValue();
                if (sourceId != null) {
                    edgeSources.put(sourceId, edge);
                }
                edge.setSourceId(sourceId);
                edge.setSource(componentTrackers.get(sourceId));
            } else {
                laterQueue.add(c);
            }
//            debugDisplayEdgeCollections("EDGE SOURCE CHANGE");
        } else if (c instanceof VisuEdgeTargetChange) {
            long id = ((VisuEdgeTargetChange)c).getId();
            VisuScenegraphEdge edge = edgeTrackers.get(id);
            if (edge != null) {
                edgeTargets.remove(edge.getTargetId(), edge);
                VisuEdgeTargetChange vetc = (VisuEdgeTargetChange) c;
                Long targetId = vetc.getValue();
                if (targetId != null) {
                    edgeTargets.put(targetId, edge);
                }
                edge.setTargetId(targetId);
                edge.setTarget(componentTrackers.get(targetId));
            } else {
                laterQueue.add(c);
            }
//            debugDisplayEdgeCollections("EDGE TARGET CHANGE");
        } else if (c instanceof VisuAttributeChange){
            // this the time before we started the processing, it's mainly the packet time
            long beforeProcessingTime = System.currentTimeMillis(); 
            long id = ((VisuAttributeChange)c).getId();
            VisuScenegraphComponent comp = componentTrackers.get(id);
            if (comp != null) {
                comp.processChange(c);          
                long afterProcessingTime = System.currentTimeMillis(); 
                // this is added to store measurments about the performance, if the timer is set and the its our 
                // test component then we write the duration into a file.
                if (ClientPerformanceTest2.BEFORE_MILLI_SECS != -1 && ClientPerformanceTest2.COMP_NAME.equals(helper.getComponent(id).getName())) {
//                    System.out.println("Component " + helper.getComponent(id).getName() + " set Translation took" + (afterTime - ClientPerformanceTest2.BEFORE_MILLI_SECS) + " ms."+ comp.toString());
                    TestResultExporter.writeTestResult(ClientPerformanceTest2.BEFORE_MILLI_SECS,beforeProcessingTime,afterProcessingTime);                  
                }
//                System.out.println("UPDATE " + id + " type " + c.getClass().getSimpleName());
                updateChildrenAndEdges(id);
            } else {
                laterQueue.add(c);
            }
        } else {
            //TODO: CompositeVisuChange
            throw new IllegalStateException("Received unknown change class");
        }
    }

    /**
     * private debug output method to show the edge collections. Was used while implementing edges.
     * @param txt 
     */
    private void debugDisplayEdgeCollections(String txt) {
        System.out.println(txt);
        final Joiner.MapJoiner mapJoiner = Joiner.on(';').withKeyValueSeparator("=");
        System.out.println("sources: " + mapJoiner.join(edgeSources.asMap()));
        System.out.println("targets: " + mapJoiner.join(edgeTargets.asMap()));
    }

    /**
     * Notifies all registered edges of creation or deletion of a visual component
     * @param id
     * @param comp 
     */
    private void notifyEdges(final long id, VisuScenegraphComponent comp) {
        for (VisuScenegraphEdge e : edgeSources.get(id)) {
            e.setSource(comp);
        }
        for (VisuScenegraphEdge e : edgeTargets.get(id)) {
            e.setTarget(comp);
        }
    }

    /**
     * This is a misplaced utility method for converting betwen JME vectors and Kodemat VisuVectors
     * it is only used within VisuScenegraphComponent and VisuServer right now. 
     * TODO: Should be moved to a utility class.
     * @param visurot
     * @return 
     */
    public static Vector3f convert(VisuVector3f visuvec) {
        if (visuvec == null) {
            return null;
        }
        Vector3f jmevec = new Vector3f(visuvec.x, visuvec.y, visuvec.z);
        return jmevec;
    }

    /**
     * This is a misplaced utility method for converting betwen JME quaternion and Kodemat VisuRotations
     * it is only used within VisuScenegraphComponent and VisuServer right now. 
     * TODO: Should be moved to a utility class.
     * @param visurot
     * @return 
     */
    public static Quaternion convert(VisuRotation visurot) {
        if (visurot == null) {
            return null;
        }
        Quaternion jmequat = new Quaternion().fromAngles(FastMath.DEG_TO_RAD * visurot.x, FastMath.DEG_TO_RAD * visurot.y, FastMath.DEG_TO_RAD * visurot.z);
        return jmequat;
    }

    /**
     * This method will update recursively all children and edges of a visual component
     * @param compId 
     */
    public void updateChildrenAndEdges(long compId) {
//        System.out.println("UPDATING " + compId);
        for (VisuScenegraphEdge se : edgeSources.get(compId)) {
            se.updateEdge();
            updateComponentsThatReferenceEdge(se.id);
        }
        for (VisuScenegraphEdge se : edgeTargets.get(compId)) {
            se.updateEdge();
            updateComponentsThatReferenceEdge(se.id);
        }
        for (VisuScenegraphComponent currentChild : childComponents.get(compId)) {
            currentChild.updateParentInformation();
            updateChildrenAndEdges(currentChild.id);
        }
    }

    /**
     * When a component references an edge it means that its positioned on the
     * edge. Since the component is not a direct child of the edge in the
     * scenegraph, it must be updated explicitly. This method should be called
     * all the time the position of an edge is altered somehow, so that
     * components travelling along the edge are updated. The method
     * updateRefrenceToEdge() on the component is then called and creates a new
     * translation change object for the component, so that it triggers a recomputation
     * of the actual local translation.
     *
     * @param edgeId
     */
    private void updateComponentsThatReferenceEdge(long edgeId) {
        for (VisuScenegraphComponent vsc : edgeReferences.get(edgeId)) {
//            System.out.println("UPDATING COMP  " + vsc.id + " that references EDGE " + edgeId);
            vsc.updateReferenceToEdge();
        }
    }
}
