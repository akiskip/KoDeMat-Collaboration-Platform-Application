/* 
 * Copyright 2014 Orthodoxos Kipouridis and Moritz Roidl.
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
package kodemat.visudata;

import kodemat.visudata.visuComponents.VisuComponent;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import java.util.ArrayList;
import kodemat.visudata.change.VisuAddChangeHistory;
import kodemat.visudata.change.VisuChatMessageChange;
import kodemat.visudata.change.VisuCreateComponent;
import kodemat.visudata.change.VisuCreateEdge;
import kodemat.visudata.change.VisuDeleteComponent;
import kodemat.visudata.change.VisuDeleteEdge;
import kodemat.visudata.change.VisuEdgeSourceChange;
import kodemat.visudata.change.VisuEdgeTargetChange;
import kodemat.visudata.change.VisuInterpolationChange;
import kodemat.visudata.change.VisuLabelChange;
import kodemat.visudata.change.VisuMarkingChange;
import kodemat.visudata.change.VisuParentChange;
import kodemat.visudata.change.VisuReferenceToEdgeChange;
import kodemat.visudata.change.VisuRotationChange;
import kodemat.visudata.change.VisuScaleChange;
import kodemat.visudata.change.VisuTranslationChange;
import kodemat.visudata.change.VisuTypeChange;

/**
 *In this we add class entry listeners for the IMap of the Hazelcast cluster
 * Hazelcast allows you to register for entry events to get notified when entries added, updated or removed. 
 * Listeners are cluster-wide. When a member adds a listener, it is actually registering for events originated in any member in the cluster.
 * When a new member joins, events originated at the new member will also be delivered.
All events are ordered, meaning, listeners will receive and process the events in the order they are actually occurred.
* see http://code.google.com/p/hazelcast/wiki/Events
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuEventListenerSupport {

    final VisuHelper helper;
    final VisuEventListenerSupport.IdListener idListener = new VisuEventListenerSupport.IdListener();
    final VisuEventListenerSupport.NameListener nameListener = new VisuEventListenerSupport.NameListener();
    final VisuEventListenerSupport.TranslationListener translationListener = new VisuEventListenerSupport.TranslationListener();
    final VisuEventListenerSupport.RotationListener rotationListener = new VisuEventListenerSupport.RotationListener();
    final VisuEventListenerSupport.ScaleListener scaleListener = new VisuEventListenerSupport.ScaleListener();
    final VisuEventListenerSupport.LabelListener labelListener = new VisuEventListenerSupport.LabelListener();
    final VisuEventListenerSupport.ParentListener parentListener = new VisuEventListenerSupport.ParentListener();
    final VisuEventListenerSupport.TypeListener typeListener = new VisuEventListenerSupport.TypeListener();
    final VisuEventListenerSupport.InterpolationListener interpolationListener = new VisuEventListenerSupport.InterpolationListener();
//    final VisuEventListenerSupport.HistoryListener historyListener = new VisuEventListenerSupport.HistoryListener();
    final VisuEventListenerSupport.MarkingChangeListener markingChangeListener = new VisuEventListenerSupport.MarkingChangeListener();
    final VisuEventListenerSupport.EdgeSourceChangeListener edgeSourceChangeListener = new VisuEventListenerSupport.EdgeSourceChangeListener();
    final VisuEventListenerSupport.EdgeTargetChangeListener edgeTargetChangeListener = new VisuEventListenerSupport.EdgeTargetChangeListener();
    final VisuEventListenerSupport.EdgeIdListener edgeIdListener = new VisuEventListenerSupport.EdgeIdListener();
    final VisuEventListenerSupport.ReferenceToEdgeListener referenceToEdgeListener = new VisuEventListenerSupport.ReferenceToEdgeListener();
    
    final String nameListenerId;
    final String translationListenerId;
    final String rotationListenerId;
    final String scaleListenerId;
    final String labelListenerId;
    final String parentListenerId;
    final String typeListenerId;
    final String interpolationListenerId;
    final String markingListenerId;
    final String edgeSourceListenerId;
    final String edgeTargetListenerId;
    final String edgeIdListenerId;
    final String edgeReferenceListenerId;
    
    //interface that will be implemented by the classes that will hadle the events e.g OpenGL thread
    private VisuEventListener listener;

    public VisuEventListenerSupport(VisuHelper helper) {
        this.helper = helper;
        //helper.ids.addEntryListener(idListener, true); //since hazelcast 3.1 events missing, using namelistener instead
        nameListenerId = helper.names.addEntryListener(nameListener, true);
        translationListenerId = helper.translations.addEntryListener(translationListener, true);
        rotationListenerId = helper.rotations.addEntryListener(rotationListener, true);
        scaleListenerId = helper.scales.addEntryListener(scaleListener, true);
        labelListenerId = helper.labels.addEntryListener(labelListener, true);
        parentListenerId = helper.parents.addEntryListener(parentListener, true);
        typeListenerId = helper.types.addEntryListener(typeListener, true);
        interpolationListenerId = helper.interpolations.addEntryListener(interpolationListener, true);
        markingListenerId = helper.markings.addEntryListener(markingChangeListener, true);
//        helper.componentHistories.addEntryListener(historyListener, true);
        edgeSourceListenerId = helper.edgeSources.addEntryListener(edgeSourceChangeListener, true);
        edgeTargetListenerId = helper.edgeTargets.addEntryListener(edgeTargetChangeListener, true);
        edgeIdListenerId = helper.edgeIds.addEntryListener(edgeIdListener, true);
        edgeReferenceListenerId = helper.referencesToEdges.addEntryListener(referenceToEdgeListener, true);
        
    }
    
    public void dispose(){
        helper.names.removeEntryListener(nameListenerId);
        helper.translations.removeEntryListener(translationListenerId);
        helper.rotations.removeEntryListener(rotationListenerId);
        helper.scales.removeEntryListener(scaleListenerId);
        helper.labels.removeEntryListener(labelListenerId);
        helper.parents.removeEntryListener(parentListenerId);
        helper.types.removeEntryListener(typeListenerId);
        helper.interpolations.removeEntryListener(interpolationListenerId);
        helper.note.removeEntryListener(markingListenerId);
        helper.edgeSources.removeEntryListener(edgeSourceListenerId);
        helper.edgeTargets.removeEntryListener(edgeTargetListenerId);
        helper.edgeIds.removeEntryListener(edgeIdListenerId);
        helper.referencesToEdges.removeEntryListener(edgeReferenceListenerId);
    }

    /**
     * @return the listener
     */
    public VisuEventListener getListener() {
        return listener;
    }

    /**
     * @param listener the listener to set
     */
    public void setListener(VisuEventListener listener) {
        this.listener = listener;
    }

    private void publishEvent(VisuChange vc) {
        if (listener != null) {
            listener.handleEvent(vc);
        }
    }
    /*Methode die aufgerufen wenn ein neue Client kommt
     * Methods that are called when a new client member joins the cluster
     */
    public void generateEventsForAllExistingComponents() {
        //for every visuComponent in the hazelcast cluster generate events by sending to the listener class
        //visuChanges that correspond to the attributes contained by the components
        for (VisuComponent v : helper.getAllComponents()) {
            publishEvent(new VisuCreateComponent(v.getId(), v.getName()));
            publishEvent(new VisuTypeChange(v.getType(), v.getId()));
            if (v.getTranslation() != null) {
                publishEvent(new VisuTranslationChange(v.getTranslation(), v.getId()));
            }
            if (v.getRotation() != null) {
                publishEvent(new VisuRotationChange(v.getRotation(), v.getId()));
            }
            if (v.getScale() != null) {
                publishEvent(new VisuScaleChange(v.getScale(), v.getId()));
            }
            if (v.getLabel() != null) {
                publishEvent(new VisuLabelChange(v.getLabel(), v.getId()));
            }
            if (v.getParent() != null) {
                publishEvent(new VisuParentChange(v.getParent(), v.getId()));
            }
            if (v.getInterpolation() != null) {
                publishEvent(new VisuInterpolationChange(v.getInterpolation(), v.getId()));
            }
//            if (v.getLastHistoryChange() != null) {
////                check if its needed
////               publishEvent(new VisuAddChangeHistory(v.getLastHistoryChange(), v.getId()));
//            }
            if (v.getReferenceToEdge() != null){
                publishEvent(new VisuReferenceToEdgeChange(v.getReferenceToEdge(), v.getId()));
            }
        }
           //for every visuComponent in the hazelcast cluster
        for (VisuEdge ve : helper.getAllEdges()){
            publishEvent(new VisuCreateEdge(ve.getId(), ve.getName()));
            if(ve.getSource() != null){
                publishEvent(new VisuEdgeSourceChange(ve.getSource(), ve.getId()));
            }
            if(ve.getTarget() != null){
                publishEvent(new VisuEdgeTargetChange(ve.getTarget(), ve.getId()));
            }
        }
    }

    //do not use this listener, it seems as if since hazelcast 3.1 this does not work reliable (events missing, key as a string??). use namelistener instead
    class IdListener implements EntryListener<String, Long> {

        @Override
        public void entryAdded(EntryEvent<String, Long> ee) {
            System.out.println("IDLISTENER entry added " + ee.getValue());
            publishEvent(new VisuCreateComponent(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<String, Long> ee) {
            publishEvent(new VisuDeleteComponent(ee.getValue()));
        }

        @Override
        public void entryUpdated(EntryEvent<String, Long> ee) {
        }

        @Override
        public void entryEvicted(EntryEvent<String, Long> ee) {
        }
    }
    
    class NameListener implements EntryListener<Long, String> {

        @Override
        public void entryAdded(EntryEvent<Long, String> ee) {
            System.out.println("NAMELISTENER entry added " + ee.getValue());
            publishEvent(new VisuCreateComponent(ee.getKey(), ee.getValue()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, String> ee) {
            publishEvent(new VisuDeleteComponent(ee.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, String> ee) {
        }

        @Override
        public void entryEvicted(EntryEvent<Long, String> ee) {
        }
        
    }

    class TranslationListener implements EntryListener<Long, VisuVector3f> {

        @Override
        public void entryAdded(EntryEvent<Long, VisuVector3f> ee) {
            publishEvent(new VisuTranslationChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, VisuVector3f> ee) {
            publishEvent(new VisuTranslationChange(null, ee.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, VisuVector3f> ee) {
            publishEvent(new VisuTranslationChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryEvicted(EntryEvent<Long, VisuVector3f> ee) {
        }
    }

    class RotationListener implements EntryListener<Long, VisuRotation> {

        @Override
        public void entryAdded(EntryEvent<Long, VisuRotation> ee) {
            publishEvent(new VisuRotationChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, VisuRotation> ee) {
            publishEvent(new VisuRotationChange(null, ee.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, VisuRotation> ee) {
            publishEvent(new VisuRotationChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryEvicted(EntryEvent<Long, VisuRotation> ee) {
        }
    }

    class ScaleListener implements EntryListener<Long, VisuVector3f> {

        @Override
        public void entryAdded(EntryEvent<Long, VisuVector3f> ee) {
            publishEvent(new VisuScaleChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, VisuVector3f> ee) {
            publishEvent(new VisuScaleChange(null, ee.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, VisuVector3f> ee) {
            publishEvent(new VisuScaleChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryEvicted(EntryEvent<Long, VisuVector3f> ee) {
        }
    }

    class LabelListener implements EntryListener<Long, String> {

        @Override
        public void entryAdded(EntryEvent<Long, String> ee) {
            publishEvent(new VisuLabelChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, String> ee) {
            publishEvent(new VisuLabelChange(null, ee.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, String> ee) {
            publishEvent(new VisuLabelChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryEvicted(EntryEvent<Long, String> ee) {
        }
    }
     
    class MarkingChangeListener implements EntryListener<Long, VisuMarking> {

        @Override
        public void entryAdded(EntryEvent<Long, VisuMarking> event) {
 publishEvent(new VisuMarkingChange(event.getValue(), event.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, VisuMarking> event) {
             publishEvent(new VisuMarkingChange(null, event.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, VisuMarking> event) {
   publishEvent(new VisuMarkingChange(event.getValue(), event.getKey()));
        }

        @Override
        public void entryEvicted(EntryEvent<Long, VisuMarking> event) {
        }

     
    }

    class ParentListener implements EntryListener<Long, Long> {

        @Override
        public void entryAdded(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuParentChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuParentChange(null, ee.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuParentChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryEvicted(EntryEvent<Long, Long> ee) {
        }
    }

    class TypeListener implements EntryListener<Long, VisuType> {

        @Override
        public void entryAdded(EntryEvent<Long, VisuType> ee) {
            publishEvent(new VisuTypeChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, VisuType> ee) {
            publishEvent(new VisuTypeChange(null, ee.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, VisuType> ee) {
            publishEvent(new VisuTypeChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryEvicted(EntryEvent<Long, VisuType> ee) {
        }
    }

    class InterpolationListener implements EntryListener<Long, VisuInterpolation> {

        @Override
        public void entryAdded(EntryEvent<Long, VisuInterpolation> ee) {
            publishEvent(new VisuInterpolationChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, VisuInterpolation> ee) {
            publishEvent(new VisuInterpolationChange(null, ee.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, VisuInterpolation> ee) {
            publishEvent(new VisuInterpolationChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryEvicted(EntryEvent<Long, VisuInterpolation> ee) {
        }
    }

    
    class EdgeSourceChangeListener implements EntryListener<Long, Long> {

        @Override
        public void entryAdded(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuEdgeSourceChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuEdgeSourceChange(null, ee.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuEdgeSourceChange(ee.getValue() , ee.getKey()));
        }

        @Override
        public void entryEvicted(EntryEvent<Long, Long> ee) {
        }
    }
    
    class EdgeTargetChangeListener implements EntryListener<Long, Long> {

        @Override
        public void entryAdded(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuEdgeTargetChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuEdgeTargetChange(null, ee.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuEdgeTargetChange(ee.getValue() , ee.getKey()));
        }

        @Override
        public void entryEvicted(EntryEvent<Long, Long> ee) {
        }
    }
    
    class EdgeIdListener implements EntryListener<String, Long> {

        @Override
        public void entryAdded(EntryEvent<String, Long> ee) {
            publishEvent(new VisuCreateEdge(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<String, Long> ee) {
            publishEvent(new VisuDeleteEdge(ee.getValue()));
        }

        @Override
        public void entryUpdated(EntryEvent<String, Long> ee) {
        }

        @Override
        public void entryEvicted(EntryEvent<String, Long> ee) {
        }
    }
    
    class ReferenceToEdgeListener implements EntryListener<Long, Long> {

        @Override
        public void entryAdded(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuReferenceToEdgeChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryRemoved(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuReferenceToEdgeChange(null, ee.getKey()));
        }

        @Override
        public void entryUpdated(EntryEvent<Long, Long> ee) {
            publishEvent(new VisuReferenceToEdgeChange(ee.getValue(), ee.getKey()));
        }

        @Override
        public void entryEvicted(EntryEvent<Long, Long> ee) {
        }
    }
}

