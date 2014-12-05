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
package kodemat.visu.editor.nifty.panels;

import com.jme3.app.Application;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;
import kodemat.visu.appstates.NiftyGuiAppState;
import kodemat.visu.editor.IEditor;
import kodemat.visu.editor.VisuEditor;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class NoteHistoryEditor implements IEditorPanel {

    NiftyGuiAppState guiAppState;

    public NoteHistoryEditor(Application visuClient) {
        this.guiAppState = visuClient.getStateManager().getState(NiftyGuiAppState.class);
    }

   

    @Override
    public void layout(IEditor basicEditor) {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");



        Element toolbarHistory = screen.findElementByName("history-panel");
        new TextFieldBuilder("note-field") {
            {
                alignCenter();
                textHAlignLeft();
            }
        }.build(nifty, screen, toolbarHistory);
        PanelBuilder histpb = new PanelBuilder("hist-button-panel") {
            {
                childLayoutHorizontal();
                alignCenter();
                controller(NotePanelController.class.getName());
                control(new ButtonBuilder("submit-button", "Submit") {
                    {

                        height("20px");
                        alignLeft();
                        interactOnClick("submitNote()");
                    }
                });
                control(new ButtonBuilder("close-history-button", "Close") {
                    {
                        height("20px");
                        alignRight();
                        interactOnClick("closeHistory()");
                    }
                });

            }
        };

        Element histPanel = histpb.build(nifty, screen, toolbarHistory);
        NotePanelController notePanelController = histPanel.getControl(NotePanelController.class);
        notePanelController.setBasicEditor((VisuEditor)basicEditor);

        toolbarHistory.layoutElements();
//        ((VisuEditor)basicEditor).closeNoteHistory();
    }



    public static class NotePanelController extends AbstractController {

        private VisuEditor basicEditor;

        @Override
        public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        }

        @Override
        public void onStartScreen() {
        }

        @Override
        public boolean inputEvent(NiftyInputEvent inputEvent) {
            return false;
        }

        public void submitNote() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
//                    basicEditor.submitNote();
                }
            });
        }

        public void closeHistory() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
//                    basicEditor.closeNoteHistory();
                }
            });
        }

        /**
         * @return the basicEditor
         */
        public VisuEditor getBasicEditor() {
            return basicEditor;
        }

        /**
         * @param basicEditor the basicEditor to set
         */
        public void setBasicEditor(VisuEditor basicEditor) {
            this.basicEditor = basicEditor;
        }
    }
}
