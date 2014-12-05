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
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
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
public class ModelEditWindowPanel implements IEditorPanel {

    static NiftyGuiAppState guiAppState;
    Nifty nifty;
    
    public ModelEditWindowPanel(Application visuClient) {
        this.guiAppState = visuClient.getStateManager().getState(NiftyGuiAppState.class);
        nifty = guiAppState.getNifty();
    }

    @Override
    public void layout(IEditor basicEditor) {
      
        final Screen screen = nifty.getScreen("hidden");

    Element toolbar = screen.findElementByName("toolbar-modification-panel");

        PanelBuilder pb = new PanelBuilder("model-editor-panel") {
            {
                height("50%");
                valignBottom();
                childLayoutHorizontal();
                controller(ModelEditWindowPanel.ModelEditPanelController.class.getName());
              
            }
            };
        Element editorPanel = pb.build(nifty, screen, toolbar);
       ModelEditWindowPanel.ModelEditPanelController editorPanelController = editorPanel.getControl(ModelEditWindowPanel.ModelEditPanelController.class);
        editorPanelController.setBasicEditor((VisuEditor)basicEditor);

             PanelBuilder smallIconsPanel = new PanelBuilder("small-icons-panel") {
            {
                height("20%");
                valignBottom();
                childLayoutHorizontal();
                controller(ModelEditWindowPanel.ModelEditPanelController.class.getName());
               
            }
            };
        Element iconsPanel = smallIconsPanel.build(nifty, screen, toolbar);
       ModelEditWindowPanel.ModelEditPanelController iconsPanelController = iconsPanel.getControl(ModelEditWindowPanel.ModelEditPanelController.class);
        iconsPanelController.setBasicEditor((VisuEditor)basicEditor);
        
        
        new ImageBuilder("moveModelImage") {
            {
              paddingRight("5px");
              
                filename("Interface/Buttons/button_move.png");
                visibleToMouse(true);
                interactOnClick("moveTo()");
                onClickEffect(new EffectBuilder("changeImage"){
                 {
                        getAttributes().setAttribute("active", "Interface/Buttons/button_move_highlight.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/button_move_highlight.png");
                    }
            });
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/button_move_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/button_move.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Move Object");
                    }
                });
            }
        }.build(nifty, screen, editorPanel);


     new ImageBuilder("rotateModelImage") {
            {
                  marginRight("5px");
              
                filename("Interface/Buttons/button_rotate.png");
                visibleToMouse(true);
                interactOnClick("rotate()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/button_rotate_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/button_rotate.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Rotate Object");
                    }
                });
            }
        }.build(nifty, screen, editorPanel);

     new ImageBuilder("scaleModelImage") {
            {
                marginRight("5px");
                filename("Interface/Buttons/button_scale.png");
                visibleToMouse(true);
                interactOnClick("scale()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/button_scale_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/button_scale.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Scale Object");
                    }
                });
            }
        }.build(nifty, screen, editorPanel);

     
     
//     /--------------------------Icons Panel ------------------------------------
          new ImageBuilder("undoImage") {
            {
                  marginRight("5px");
                align(Align.Right);
                filename("Interface/Buttons/button_undo.png");
                visibleToMouse(true);
                interactOnClick("localUndo()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/button_undo_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/button_undo.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Undo last action");
                    }
                });
            }
        }.build(nifty, screen, iconsPanel);
            new ImageBuilder("redoImage") {
            {
                  marginRight("5px");
                 align(Align.Right);
                filename("Interface/Buttons/button_redo.png");
                visibleToMouse(true);
                interactOnClick("localRedo()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/button_redo_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/button_redo.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Redo last action");
                    }
                });
            }
        }.build(nifty, screen, iconsPanel);
            
            
              new ImageBuilder("showHistoryImage") {
            {
                  marginRight("5px");
                 align(Align.Right);
                filename("Interface/Buttons/button_history.png");
                visibleToMouse(true);
                interactOnClick("showHistory()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/button_history.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/button_history.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Show history");
                    }
                });
            }
        }.build(nifty, screen, iconsPanel);
              
              
                new ImageBuilder("setNoteImage") {
            {
                  marginRight("5px");
                filename("Interface/Buttons/button_add_note.png");
                visibleToMouse(true);
                interactOnClick("editInfo()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/button_add_note_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/button_add_note.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Add/Edit Information");
                    }
                });
            }
        }.build(nifty, screen, iconsPanel);
              
                
                new ImageBuilder("deleteComponentImage") {
            {
                  marginRight("5px");
                filename("Interface/Buttons/button_delete.png");
                visibleToMouse(true);
                interactOnClick("delete()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/button_delete_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/button_delete.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Delete Component");
                    }
                });
            }
        }.build(nifty, screen, iconsPanel);
//     screen.findElementByName("model-edit-panel").layoutElements();

    }

    public static class ModelEditPanelController extends AbstractController {

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
        public void editInfo() {

            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.showComponentInfo();
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
        
        public void localUndo() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.localUndo();
                }
            });
        }

        public void localRedo() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.localRedo();
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
