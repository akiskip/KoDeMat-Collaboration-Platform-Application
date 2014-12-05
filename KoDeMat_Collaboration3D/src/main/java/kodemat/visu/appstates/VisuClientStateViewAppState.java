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
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import kodemat.visu.VisuClientStateView;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuClientStateViewAppState extends AbstractAppState {

    private Application app;
    protected VisuClientStateView clientStateView;
    private  boolean showClientState = true;
    
    protected Node guiNode;
    protected BitmapFont guiFont;    

    public VisuClientStateViewAppState( Node guiNode, BitmapFont guiFont ) {
        this.guiNode = guiNode;
        this.guiFont = guiFont;
    }

    
    public VisuClientStateView getClientStateView() {
        return clientStateView;
    }

    public void setDisplayClientStateView(boolean show) {
        showClientState = show;
        if (clientStateView != null ) {
            clientStateView.setEnabled(show);
            clientStateView.setCullHint(show ? Spatial.CullHint.Never : Spatial.CullHint.Always);
        }
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
               
        
        if (guiNode == null) {
            throw new RuntimeException( "No guiNode specific and cannot be automatically determined." );
        } 
        
        if (guiFont == null) {
            guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        }
         
        loadClientStateView();      
    }
            


    public void loadClientStateView() {
        clientStateView = new VisuClientStateView("Client State View", 
                                  app.getAssetManager());
        clientStateView.setLocalTranslation(0, app.getContext().getSettings().getHeight(), 0);
        clientStateView.setEnabled(showClientState);
        clientStateView.setCullHint(showClientState ? Spatial.CullHint.Never : Spatial.CullHint.Always);        
        guiNode.attachChild(clientStateView);
    }
        
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        
        if (enabled) {
            clientStateView.setEnabled(showClientState);
            clientStateView.setCullHint(showClientState ? Spatial.CullHint.Never : Spatial.CullHint.Always);        
        } else {
            clientStateView.setEnabled(false);
            clientStateView.setCullHint(Spatial.CullHint.Always);        
        }
    }
    
    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
        super.cleanup();
        
        guiNode.detachChild(clientStateView);
    }
    
}
