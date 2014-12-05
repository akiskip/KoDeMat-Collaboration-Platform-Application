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

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import java.util.LinkedList;
import java.util.List;
import kodemat.visudata.VisuChange;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuScenegraphEdge {

    List<VisuChange> pendingChanges = new LinkedList<VisuChange>();
    Spatial spatial;
    Line line;
    private final ScenegraphEventAssembler eventAssembler;
    private Long sourceId = null;
    private Long targetId = null;
    private VisuScenegraphComponent source = null;
    private VisuScenegraphComponent target = null;
    public final String idInfo;
    public final long id;

    VisuScenegraphEdge(long id, String idInfo, final ScenegraphEventAssembler eventAssembler) {
        this.idInfo = idInfo;
        this.eventAssembler = eventAssembler;
        this.id = id;
    }

    public void delete() {
        if (spatial != null) {
            Node parent = spatial.getParent();
            if (parent != null) {
                parent.detachChild(spatial);
            }
            spatial = null;
        }
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long id) {
        this.sourceId = id;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long id) {
        this.targetId = id;
    }

    public void setSource(VisuScenegraphComponent source) {
        this.source = source;
        updateEdge();
    }
    
    public VisuScenegraphComponent getSource(){
        return this.source;
    }

    public void setTarget(VisuScenegraphComponent target) {
        this.target = target;
        updateEdge();
    }
    
     public VisuScenegraphComponent getTarget(){
        return this.target;
    }

    public void addChange(VisuChange c) {
        if (spatial != null) {
            applyAttributeChangeToSpatial(c);
        } else {
            pendingChanges.add(c);
        }
    }

    private void applyPendingChanges() {
        for (VisuChange visuChange : pendingChanges) {
            addChange(visuChange);
        }
        pendingChanges.clear();
    }

    private void applyAttributeChangeToSpatial(VisuChange c) {
    }

    public void updateEdge() {

        if (source != null && target != null && source.spatial != null && target.spatial != null) {
            Vector3f beginning = source.spatial.getWorldTranslation();
            Vector3f endPoint = target.spatial.getWorldTranslation();
            if (spatial == null) {
                Material arrowMat = new Material(eventAssembler.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                arrowMat.getAdditionalRenderState().setWireframe(true);
                arrowMat.setColor("Color", ColorRGBA.Red);
                line = new Line(beginning, endPoint);
                line.setLineWidth(2);
                Geometry geometry = new Geometry("Edge ", line);
                geometry.setMaterial(arrowMat);
                spatial = geometry;
                eventAssembler.rootNode.attachChild(spatial);
            } else {
                line.updatePoints(beginning, endPoint);
            }
        } else {
            if (spatial != null) {
                eventAssembler.rootNode.detachChild(spatial);
                spatial = null;
                line = null;
            }
        }


//        Node lineChild = (Node) eventAssembler.rootNode.getChild(msg);
//        if (lineChild != null) {
//            eventAssembler.rootNode.detachChild(lineChild);
//        }
//        String initialSP = msg.split("-")[1];
//        String finalSP = msg.split("-")[2];
//        Vector3f beginning = getWorldTranslation(initialSP);
//        Vector3f endPoint = getWorldTranslation(finalSP);
//        Material arrowMat = new Material(eventAssembler.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        arrowMat.getAdditionalRenderState().setWireframe(true);
//        arrowMat.setColor("Color", ColorRGBA.Red);
//        Line line = new Line(beginning, endPoint);
//        line.setLineWidth(2);
//        Node lineNode = new Node(msg);
//        Geometry geometry = new Geometry("Geometry " + msg, line);
//        geometry.setMaterial(arrowMat);
//        lineNode.attachChild(geometry);
//
//        eventAssembler.rootNode.attachChild(lineNode);
//
//        String initialParent = getParentName(initialSP);
//        ArrayList<String> list = spatialEdges.get(initialParent);
//        if (list == null) {
//            list = new ArrayList<String>();
//        }
//        list.add(msg);
//        spatialEdges.put(initialParent, list);
//
//        String finalParent = getParentName(finalSP);
//        list = spatialEdges.get(finalParent);
//        if (list == null) {
//            list = new ArrayList<String>();
//        }
//        if (!list.contains(msg)) {
//            list.add(msg);
//        }
//        spatialEdges.put(finalParent, list);
    }

    public String toString() {
        return "Edge{" + "id=" + idInfo + '}';
    }
}
