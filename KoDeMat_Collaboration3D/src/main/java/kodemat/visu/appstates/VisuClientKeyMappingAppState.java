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

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuClientKeyMappingAppState extends AbstractAppState {

    public static final String INPUT_MAPPING_START_CLIENT = "VisuClientKeyMappingAppState_START_CLIENT";
    private SimpleApplication visuClient;
    private KeyListener keyListener = new KeyListener();
    private InputManager inputManager;
    AppStateManager stateManager;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = app.getStateManager();

        this.visuClient = (SimpleApplication) app;
        this.inputManager = app.getInputManager();

        if (app.getInputManager() != null) {

            inputManager.addMapping(INPUT_MAPPING_START_CLIENT, new KeyTrigger(KeyInput.KEY_G));

            inputManager.addListener(keyListener,
                    INPUT_MAPPING_START_CLIENT);


            //Change application exit key from standard ESC to F10. The ESC key should be used to cancel editor states to mimic standard application behaviour.
            inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
            inputManager.addMapping(SimpleApplication.INPUT_MAPPING_EXIT, new KeyTrigger(KeyInput.KEY_F10));
            inputManager.addListener(keyListener, SimpleApplication.INPUT_MAPPING_EXIT);

        }



    }

    @Override
    public void cleanup() {
        super.cleanup();

        if (inputManager.hasMapping(INPUT_MAPPING_START_CLIENT)) {
            inputManager.deleteMapping(INPUT_MAPPING_START_CLIENT);
        }

        inputManager.removeListener(keyListener);
    }

    private class KeyListener implements ActionListener {

        @Override
        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }

            if (name.equals(SimpleApplication.INPUT_MAPPING_EXIT)) {
                VisuClientKeyMappingAppState.this.visuClient.stop();
            }

            if (name.equals(INPUT_MAPPING_START_CLIENT)) {
                connect();
            }
        }
    }

    public void connect() {
        final VisuHazelcastAppState hazelcastAppState = stateManager.getState(VisuHazelcastAppState.class);
        if (hazelcastAppState != null) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    hazelcastAppState.client_start();
                }
            };
            hazelcastAppState.threadPool.submit(r);
        }
    }
}
