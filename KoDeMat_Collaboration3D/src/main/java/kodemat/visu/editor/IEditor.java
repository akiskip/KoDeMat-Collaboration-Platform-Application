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
package kodemat.visu.editor;

import com.jme3.app.Application;
import java.util.ArrayList;
import kodemat.visu.appstates.VisuHazelcastAppState;
import kodemat.visu.editor.selection.IVisuSelectable;
import kodemat.visu.input.mouse.ClickSelection;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.visuComponents.VisuComponent;

/**
 *Interface for the Controllers of the User Interactions
 * 
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public interface IEditor {
    
     void initialize(Application visuClient);
     void initExtraInputMapping();
     void initUI();
    public void cleanup();
    public void handleClick(final ClickSelection<VisuComponent> selection);
    public IVisuSelectable getObjectSelection();
    public ClickSelection<VisuComponent> getMarkerSelection();
    public VisuHelper getVisuHelper();
    
    public void displayClientMessage(ArrayList<String> info);
    public Application getVisuClient();
    public void updateSelectedComponentsMap(ClickSelection<VisuComponent> selection);
    /**
     *
     * @return
     */
    public VisuHazelcastAppState getHazelcastAppState();

}
