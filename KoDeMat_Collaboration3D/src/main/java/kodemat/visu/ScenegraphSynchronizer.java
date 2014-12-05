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

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import kodemat.visudata.VisuChange;
import kodemat.visudata.VisuEventListener;
import kodemat.visudata.VisuEventListenerSupport;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.change.VisuCreateComponent;
import kodemat.visudata.change.VisuCreateEdge;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class ScenegraphSynchronizer {

    private final VisuEventListenerSupport eventListenerSupport;
    private final ScenegraphSynchronizer.EventListener eventListener = new ScenegraphSynchronizer.EventListener();
    private final ScenegraphEventAssembler scenegraphEventAssembler;

    public ScenegraphSynchronizer(Node rootNode, AssetManager assetManager, VisuHelper helper) {

        eventListenerSupport = new VisuEventListenerSupport(helper);
        eventListenerSupport.setListener(eventListener);

        scenegraphEventAssembler = new ScenegraphEventAssembler(assetManager, rootNode, helper);
        eventListenerSupport.generateEventsForAllExistingComponents();
    }

    public void connectionReopened() {
        eventListenerSupport.generateEventsForAllExistingComponents();
    }

    public void connectionLost() {
        scenegraphEventAssembler.reset();
    }

    public void processEventsWithinOpenGL() {
        scenegraphEventAssembler.processChangesWithinOpenGL();
    }

    private class EventListener implements VisuEventListener {

        @Override
        public void handleEvent(VisuChange event) {
            if (event instanceof VisuCreateComponent) {
//                System.out.println("EVENT " + event.getClass().getSimpleName() + " FOR COMP ID " + event.getId() + " NAME " + helper.getComponent(event.getId()).getName());
            } else if (event instanceof VisuCreateEdge) {
//                System.out.println("EVENT " + event.getClass().getSimpleName() + " FOR EDGE ID " + event.getId() + " NAME " + helper.getEdge(event.getId()).getName());
            } else {
//                System.out.println("EVENT " + event.getClass().getSimpleName() + " FOR ID " + event.getId());
            }

            //handle the events (VisuChanges) that originate from other hazelcast clients
            scenegraphEventAssembler.addChangeFromOutsideOpenGL(event);
        }
    }
}
