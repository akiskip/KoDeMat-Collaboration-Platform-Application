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
package kodemat.visu.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import kodemat.visu.editor.VisuEditor.EditorPanelController;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class ComponentInformationPanelFactory {
    public static Element createInformationPanel(String prefix, Nifty nifty, Screen screen, Element parent){
        Element panel = null;
        

        panel = new PanelBuilder(prefix + "-information-panel") {

            {
                childLayoutVertical();
                controller(EditorPanelController.class.getName());
                padding("10px");

            }
        }.build(nifty, screen, parent);
//        editorPanelController = editorPanel.getControl(EditorPanelController.class);
//        editorPanelController.setBasicEditor(this);


        new ButtonBuilder("test-button", "Test") {

            {
                
                height("20px");
                alignLeft();
                interactOnClick("test()");
            }
        }.build(nifty, screen, panel);
        
        new ButtonBuilder("create-box-button", "Create Box") {

            {
                
                height("20px");
                alignLeft();
                interactOnClick("createBox()");
            }
        }.build(nifty, screen, panel);


        parent.layoutElements();
        
        return panel;
    }
}
