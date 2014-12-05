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
package kodemat.visu.editor.userActions;

import kodemat.visu.appstates.VisuHazelcastAppState;
import kodemat.visu.editor.IEditor;
import kodemat.visudata.VisuHelper;


public abstract class IUIActionsImpl implements IUIActions {
    
    private  IEditor visuController;
    private VisuHelper visuHelper;
    
    public IUIActionsImpl(IEditor visuController){
    this.visuController = visuController;
    this.visuHelper= visuController.getVisuHelper();
    }

    @Override
    public IEditor getVisuController() {
        return this.visuController;
    }

    @Override
    public void setVisuController(IEditor visuController) {
    this.visuController = visuController;
    }

    @Override
    public VisuHelper getVisuHelper() {
    return this.visuHelper;
    }
    
    public VisuHazelcastAppState getHazelcastAppState(){
        return visuController.getHazelcastAppState();
    }

   
    
}
