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
package kodemat.visu.appstates;

import com.hazelcast.core.HazelcastInstance;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import kodemat.visu.ScenegraphSynchronizer;
import kodemat.visudata.VisuHelper;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuScenegraphAppState extends AbstractAppState {

    private Application app;
    private AppStateManager stateManager;
    private final Node rootNode;
    private final VisuHazelcastAppState hazelcastAppState;
    private VisuScenegraphAppState.ClientStateListener clientStateListener;
    private VisuHelper visuHelper;
    private ScenegraphSynchronizer scenegraphSynchronizer;

    public VisuScenegraphAppState(VisuHazelcastAppState hazelcastAppState, Node rootNode) {
        this.hazelcastAppState = hazelcastAppState;
        this.rootNode = rootNode;
    }


    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        this.app = app;
        clientStateListener = new VisuScenegraphAppState.ClientStateListener();
        hazelcastAppState.registerClientStateListener(clientStateListener);
        
        
        
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    /**
     * update phase of the OpenGL thread. It processes all events that have been added to the event queues.
     * If the reset flag has been set all events and components are deleted and the maps cleared. This can be used when the server connection is interrupted.
     */    
    public void update(float tpf) {
        super.update(tpf);
        
        if (scenegraphSynchronizer != null) {  //check if connection is opened
            scenegraphSynchronizer.processEventsWithinOpenGL();
        }
    }

    private class ClientStateListener implements VisuHazelcastAppState.ClientStateListener {

        @Override
        public void clientStarted() {
            HazelcastInstance client = hazelcastAppState.getClient();
            visuHelper = new VisuHelper(client);
            scenegraphSynchronizer = new ScenegraphSynchronizer(rootNode, app.getAssetManager(), visuHelper);
        }

        @Override
        public void connectionOpened() {
            scenegraphSynchronizer.connectionReopened();
        }

        @Override
        public void connectionLost() {
            scenegraphSynchronizer.connectionLost();
        }
    }
}
