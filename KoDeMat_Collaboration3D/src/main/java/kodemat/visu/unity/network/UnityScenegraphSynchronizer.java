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
package kodemat.visu.unity.network;

import java.nio.channels.SelectionKey;
import kodemat.visudata.VisuChange;
import kodemat.visudata.VisuEventListener;
import kodemat.visudata.VisuEventListenerSupport;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.change.VisuCreateComponent;
import kodemat.visudata.change.VisuDeleteComponent;
import kodemat.visudata.change.VisuMarkingChange;
import kodemat.visudata.change.VisuParentChange;
import kodemat.visudata.change.VisuRotationChange;
import kodemat.visudata.change.VisuScaleChange;
import kodemat.visudata.change.VisuTranslationChange;
import kodemat.visudata.change.VisuTypeChange;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class UnityScenegraphSynchronizer {

    private final VisuEventListenerSupport eventListenerSupport;
    private final UnityScenegraphSynchronizer.EventListener eventListener = new UnityScenegraphSynchronizer.EventListener();
    private final SelectionKey key;
    KodematSocketServer server;

    public UnityScenegraphSynchronizer(VisuHelper helper, SelectionKey key, KodematSocketServer server) {

        this.key = key;
        this.server = server;
        eventListenerSupport = new VisuEventListenerSupport(helper);
        eventListenerSupport.setListener(eventListener);
        eventListenerSupport.generateEventsForAllExistingComponents();

    }

    public void connectionReopened() {
        eventListenerSupport.generateEventsForAllExistingComponents();
    }

    public void dispose() {
        eventListenerSupport.dispose();
    }

    private class EventListener implements VisuEventListener {

        @Override
        public void handleEvent(VisuChange event) {
            if (event instanceof VisuCreateComponent) {
                String msg = "ID " + ((VisuCreateComponent) event).getId() + " CREATE NAME " + ((VisuCreateComponent) event).getName() + " <EOF>";
                server.write(key, msg.getBytes());
            } else if (event instanceof VisuDeleteComponent) {
                String msg = "ID " + ((VisuDeleteComponent) event).getId() + " DELETE <EOF>";
                server.write(key, msg.getBytes());
            } else if (event instanceof VisuTypeChange) {
                VisuTypeChange vtc = ((VisuTypeChange) event);
                VisuType t = vtc.getValue();
                if (t != null) {
                    String msg = "ID " + vtc.getId() + " TYPE VALUE " + t.type + " PATH " + t.path + " <EOF>";
                    server.write(key, msg.getBytes());
                }
            } else if (event instanceof VisuParentChange) {
                VisuParentChange vpc = ((VisuParentChange) event);
                Long pid = vpc.getValue();
                if (pid != null) {
                    String msg = "ID " + vpc.getId() + " PARENT ID " + pid + " <EOF>";
                    server.write(key, msg.getBytes());
                }
            } else if (event instanceof VisuTranslationChange) {
                VisuTranslationChange vtc = ((VisuTranslationChange) event);
                VisuVector3f v = vtc.getValue();
                if (v != null) {
                    String msg = "ID " + vtc.getId() + " TRANSLATION X " + v.x + " Y " + v.y + " Z " + v.z + " <EOF>";
                    server.write(key, msg.getBytes());
                }
            } else if (event instanceof VisuRotationChange) {
                VisuRotationChange vrc = ((VisuRotationChange) event);
                VisuRotation r = vrc.getValue();
                if (r != null) {
                    String msg = "ID " + vrc.getId() + " ROTATION X " + r.x + " Y " + r.y + " Z " + r.z + " <EOF>";
                    server.write(key, msg.getBytes());            
                }
            } else if (event instanceof VisuScaleChange) {
                VisuScaleChange vsc = ((VisuScaleChange) event);
                VisuVector3f v = vsc.getValue();
                if (v != null) {
                    String msg = "ID " + vsc.getId() + " SCALE X " + v.x + " Y " + v.y + " Z " + v.z + " <EOF>";
                    server.write(key, msg.getBytes());
                }
            }else if (event instanceof VisuMarkingChange) {
                VisuMarkingChange vsc = ((VisuMarkingChange) event);
                String label = vsc.getValue().getLabel();
                int boundingBox = vsc.getValue().getBoundingBoxEnabled();
                if (label != null) {
                    String msg = "ID " + vsc.getId() + " MARKING "+ boundingBox + " LABEL " + label + " <EOF>";
                    System.out.println("label changed to "+label);
                    server.write(key, msg.getBytes());
                }
            }
            else {
                //nothing
            }

        }
    }
}
