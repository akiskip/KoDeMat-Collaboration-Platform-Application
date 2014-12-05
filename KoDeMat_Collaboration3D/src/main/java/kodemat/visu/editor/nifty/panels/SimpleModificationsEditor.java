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
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import kodemat.visu.appstates.NiftyGuiAppState;
import kodemat.visu.editor.IEditor;
import kodemat.visu.editor.VisuEditor;
import org.openide.util.Exceptions;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class SimpleModificationsEditor implements IEditorPanel {

  static  NiftyGuiAppState guiAppState;

    public SimpleModificationsEditor(Application visuClient) {
        this.guiAppState = visuClient.getStateManager().getState(NiftyGuiAppState.class);
    }


    @Override
    public void layout(IEditor basicEditor) {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");


        Element toolbarEl = screen.findElementByName("toolbar-modification-panel");

        PanelBuilder pb2 = new PanelBuilder("basic-modification-panel") {
            {
                childLayoutHorizontal();
                controller(ModificationPanelController.class.getName());
                padding("10px");

            }
        };
        Element modificationPanel = pb2.build(nifty, screen, toolbarEl);
        ModificationPanelController modificationPanelController = modificationPanel.getControl(ModificationPanelController.class);
        modificationPanelController.setBasicEditor((VisuEditor)basicEditor);

        PanelBuilder pb3 = new PanelBuilder("left-panel") {
            {
                childLayoutVertical();
                alignLeft();
                controller(ModificationPanelController.class.getName());
                width("33%");

            }
        };
        Element leftPanel = pb3.build(nifty, screen, modificationPanel);

        PanelBuilder pb4 = new PanelBuilder("center-panel") {
            {
                childLayoutVertical();
                alignCenter();
                controller(ModificationPanelController.class.getName());
                width("33%");

            }
        };
        Element centerPanel = pb4.build(nifty, screen, modificationPanel);

        PanelBuilder pb5 = new PanelBuilder("right-panel") {
            {
                childLayoutVertical();
                alignRight();
                controller(ModificationPanelController.class.getName());
                width("33%");

            }
        };
        Element rightPanel = pb5.build(nifty, screen, modificationPanel);

    
        ButtonBuilder bb2 = new ButtonBuilder("translate-button", "Move to") {
            {

                height("20px");
                alignLeft();
                interactOnClick("moveTo()");
            }
        };
        bb2.build(nifty, screen, leftPanel);

        ButtonBuilder bb2a = new ButtonBuilder("rotate-button", "Rotate 90º") {
            {

                height("20px");
                alignLeft();
                interactOnClick("rotate()");
            }
        };
        bb2a.build(nifty, screen, leftPanel);

        PanelBuilder pbscale = new PanelBuilder("panel-scale") {
            {
                childLayoutHorizontal();

                control(new TextFieldBuilder("scale-field") {
                    {
                        width("30px");
                    }
                });
            }
        };
        Element scalePanel = pbscale.build(nifty, screen, leftPanel);
        
      new ButtonBuilder("import-button", "Import model") {
            {
                height("20px");
                alignCenter();
                interactOnClick("importModelComponent()");
            }
        }.build(nifty, screen, leftPanel);
        
        
        

        new ButtonBuilder("scale-button", "Scale object") {
            {

                height("20px");
                alignRight();
                interactOnClick("scale()");
            }
        }.build(nifty, screen, scalePanel);

        new ButtonBuilder("edge-point-button", "Set edge point") {
            {

                height("20px");
                alignCenter();
                interactOnClick("setEdgePoint()");
            }
        }.build(nifty, screen, centerPanel);
        new ButtonBuilder("new-note-button", "New note") {
            {

                height("20px");
                alignCenter();
                interactOnClick("addNote()");
            }
        }.build(nifty, screen, centerPanel);

        new ButtonBuilder("delete-button", "Delete object") {
            {

                height("20px");
                alignCenter();
                interactOnClick("delete()");
            }
        }.build(nifty, screen, centerPanel);


        new ButtonBuilder("show-hide-button", "Show/hide notes") {
            {

                height("20px");
                alignRight();
                interactOnClick("showOrHideNote()");
            }
        }.build(nifty, screen, rightPanel);


        new ButtonBuilder("history-button", "Show history") {
            {

                height("20px");
                alignRight();
                interactOnClick("showHistory()");
            }
        }.build(nifty, screen, rightPanel);
        
        
        new ButtonBuilder("close-button", "Close") {
            {

                height("20px");
                alignRight();
                interactOnClick("closeModificator()");
            }
        }.build(nifty, screen, rightPanel);


        toolbarEl.layoutElements();

        modificationPanel = pb2.build(nifty, screen, toolbarEl);
    }

    public static class ModificationPanelController extends AbstractController {

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

        public void moveTo() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.moveTo();
                }
            });
        }
        public void importModelComponent() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponent();
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }

        public void rotate() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.rotate();
                }
            });
        }

        public void scale() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.scale();
                }
            });
        }

        public void setEdgePoint() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //TODO: for drag and drop: basicEditor.beginSetEdgePoint();
//                    basicEditor.setEdgePoint();
                    basicEditor.createEdge();  // replaced Bibianas method with Moritz Roidl, Orthodoxos Kipouridis
                }
            });
        }

        public void delete() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.deleteComponent();
                }
            });
        }

        public void addNote() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        Element e2 = screen.findElementByName("note-field");
                        e2.enable();
                        e2.setFocus();
                 
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.addFocusArrow();
//                    basicEditor.showNoteHistory();
                }
            });
        }

        public void showHistory() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.showHistory();
                }
            });
        }

        public void closeModificator() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.closeModificator();
                }
            });
        }

        public void showOrHideNote() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.showOrHideNote();
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
