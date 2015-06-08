/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.touch.visu.editor.nifty.panels;

import com.jme3.app.Application;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;
import kodemat.touch.visu.editor.TouchVisuEditor;
import kodemat.visu.appstates.NiftyGuiAppState;
import kodemat.visu.editor.IEditor;
import kodemat.visu.editor.nifty.panels.IEditorPanel;

/**
 *
 * @author OzgeA
 */
public class TouchModelEditWindowPanel implements IEditorPanel{

    static NiftyGuiAppState guiAppState;
    private CommandController commandPanelController;
    private ModelEditPanelController editorPanelController;
    private Nifty nifty;
    
    public TouchModelEditWindowPanel(Application visuClient) {
        this.guiAppState = visuClient.getStateManager().getState(NiftyGuiAppState.class);
        nifty = guiAppState.getNifty();
    }
    
    public void layout(IEditor basicEditor) {
        final Screen screen = nifty.getScreen("hidden");

    Element dragPanel = screen.findElementByName("drag-modification-panel");

        PanelBuilder commandpb = new PanelBuilder("drag-panel") {
            {
     
                align(Align.Right);
                valign(VAlign.Bottom);
                childLayoutVertical();
                controller(CommandController.class.getName());
 
  //here
                                     
        
//                 here
                                 

                    /* 
                control(new SliderBuilder("rotation-slider", false) {
                                    {
                                        visible(false);
                                        valignBottom();
                                        max(360);
                                        stepSize(5);
                                        buttonStepSize(30);
                                    }
                                });
                     */  
                
            }
        };

        Element commandPanel = commandpb.build(nifty, screen, dragPanel);
        commandPanelController = commandPanel.getControl(CommandController.class);
        commandPanelController.setBasicEditor((TouchVisuEditor)basicEditor);

    //    dragPanel.layoutElements();

//        next two lines could cause crash
        //        commandPanel.setFocusable(false);
        Element toolbar = screen.findElementByName("toolbar-modification-panel");

        PanelBuilder pb = new PanelBuilder("model-editor-panel") {
            {
                height("30%");
                valignBottom();
                childLayoutHorizontal();
                controller(ModelEditPanelController.class.getName());
              
            }
            };
        Element editorPanel = pb.build(nifty, screen, toolbar);
       editorPanelController = editorPanel.getControl(ModelEditPanelController.class);
        editorPanelController.setBasicEditor((TouchVisuEditor)basicEditor);

             PanelBuilder smallIconsPanel = new PanelBuilder("small-icons-panel") {
            {
                height("30%");
                valignBottom();
                childLayoutHorizontal();
                controller(ModelEditPanelController.class.getName());
               
            }
            };
        Element iconsPanel = smallIconsPanel.build(nifty, screen, toolbar);
       ModelEditPanelController iconsPanelController = iconsPanel.getControl(ModelEditPanelController.class);
        iconsPanelController.setBasicEditor((TouchVisuEditor)basicEditor);
        
        PanelBuilder smallIconsPanel2 = new PanelBuilder("small-icons-panel") {
            {
                height("30%");
                valignBottom();
                childLayoutHorizontal();
                controller(ModelEditPanelController.class.getName());
               
            }
            };
        Element iconsPanel2 = smallIconsPanel2.build(nifty, screen, toolbar);
       ModelEditPanelController iconsPanelController2 = iconsPanel2.getControl(ModelEditPanelController.class);
        iconsPanelController2.setBasicEditor((TouchVisuEditor)basicEditor);
        
           new ImageBuilder("empty") {
            {
                 marginRight("5px");
                 
                 align(ElementBuilder.Align.Right);
                
               
              
            }
        }.build(nifty, screen, editorPanel);
        new ImageBuilder("moveModelImage") {
            {
                paddingRight("5px");
                width("30px");
                height("30px");
                filename("Interface/Buttons/buttont_move.png");
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
                        getAttributes().setAttribute("active", "Interface/Buttons/buttont_move_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/buttont_move.png");
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
                width("30px");
                height("30px");
                filename("Interface/Buttons/buttont_rotate.png");
                visibleToMouse(true);
                interactOnClick("rotate()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/buttont_rotate_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/buttont_rotate.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Rotate Object");
                    }
                });
            }
        }.build(nifty, screen, editorPanel);
     
     new ImageBuilder("empty") {
            {
                 marginRight("5px");
                 
                 align(ElementBuilder.Align.Right);
                
               
              
            }
        }.build(nifty, screen, editorPanel);
     
     new ImageBuilder("closeModificator") {
            {
              marginRight("5px");             
                filename("Interface/Buttons/button_close.png");
                visibleToMouse(true);
                interactOnClick("closeModificator()");
                onClickEffect(new EffectBuilder("changeImage"){
                 {
                        getAttributes().setAttribute("active", "Interface/Buttons/button_close_highlight.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/button_close_highlight.png");
                    }
            });
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/button_close_highlight.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/button_close.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Move Object");
                    }
                });
            }
        }.build(nifty, screen, editorPanel);

        
    

     
     
//     /--------------------------Icons Panel ------------------------------------
            new ImageBuilder("empty") {
            {
                 marginRight("1px");
                 
                 align(ElementBuilder.Align.Right);
                
               
              
            }
        }.build(nifty, screen, iconsPanel);
            new ImageBuilder("undoImage") {
            {
                  marginRight("5px");
                align(ElementBuilder.Align.Right);
                filename("Interface/Buttons/buttont_undo.png");
                visibleToMouse(true);
                interactOnClick("localUndo()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/buttont_undo_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/buttont_undo.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Undo last action");
                    }
                });
            }
        }.build(nifty, screen, iconsPanel);
           new ImageBuilder("empty") {
            {
                 marginRight("1px");
                 
                 align(ElementBuilder.Align.Right);
                
               
              
            }
        }.build(nifty, screen, iconsPanel);
          
            new ImageBuilder("redoImage") {
            {
                  //marginRight("15px");
                  paddingRight("15px");
                 align(ElementBuilder.Align.Right);
                filename("Interface/Buttons/buttont_redo.png");
                visibleToMouse(true);
                interactOnClick("localRedo()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/buttont_redo_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/buttont_redo.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Redo last action");
                    }
                });
            }
        }.build(nifty, screen, iconsPanel);
            
            new ImageBuilder("empty") {
            {
                 marginRight("1px");
                 
                 align(ElementBuilder.Align.Right);
                
               
              
            }
        }.build(nifty, screen, iconsPanel);
            
            new ImageBuilder("empty") {
            {
                 marginRight("5px");
                 
                 align(ElementBuilder.Align.Right);
                
               
              
            }
        }.build(nifty, screen, iconsPanel2);   
              new ImageBuilder("showHistoryImage") {
            {
                  marginRight("5px");
                 align(ElementBuilder.Align.Right);
                filename("Interface/Buttons/buttont_history.png");
                visibleToMouse(true);
                interactOnClick("showHistory()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/buttont_history.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/buttont_history.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Show history");
                    }
                });
            }
        }.build(nifty, screen, iconsPanel2);
              
              
                new ImageBuilder("setNoteImage") {
            {
                  marginRight("5px");
                filename("Interface/Buttons/buttont_add_note.png");
                visibleToMouse(true);
                interactOnClick("editInfo()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/buttont_add_note_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/buttont_add_note.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Add/Edit Information");
                    }
                });
            }
        }.build(nifty, screen, iconsPanel2);
              
                
                new ImageBuilder("deleteComponentImage") {
            {
                  marginRight("5px");
                filename("Interface/Buttons/buttont_delete.png");
                visibleToMouse(true);
                interactOnClick("delete()");
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/buttont_delete_mouseover.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/buttont_delete.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Delete Component");
                    }
                });
            }
        }.build(nifty, screen, iconsPanel2);
                
          new ImageBuilder("empty") {
            {
                 marginRight("5px");
                 
                 align(ElementBuilder.Align.Right);
                
               
              
            }
        }.build(nifty, screen, iconsPanel2);         
//     screen.findElementByName("model-edit-panel").layoutElements();

    }

    public CommandController getCommandPanelController() {
        return commandPanelController;
    }

    public ModelEditPanelController getModelEditPanelController() {
        return editorPanelController;
    }
    
    public static class CommandController extends AbstractController {

        private TouchVisuEditor basicEditor;
        private boolean first = true;
        long current = 0;
        float factor = 0.1f;

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

        public void moveUp() {
            refreshFactor();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.moveUp(factor);
                }
            });
        }

        public void moveDown() {
            refreshFactor();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.moveDown(factor);
                }
            });
        }

        public void moveLeft() {
            refreshFactor();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.moveLeft(factor);
                }
            });
        }

        public void moveRight() {
            refreshFactor();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.moveRight(factor);
                }
            });
        }

        private void refreshFactor() {
            long newTime = System.currentTimeMillis();
            if (newTime - current > 500) {
                first = true;
            } else {
                first = false;
            }
            current = newTime;
            if (first) {
                factor = 0.1f;
            } else {
                factor += 0.01f;
                if (factor > 1) {
                    factor = 1;
                }
            }
        }

        public void openRotationSlide() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.openRotationSlide();
                }
            });
        }

        @NiftyEventSubscriber(id = "rotation-slider")
        public void onClick(String id, final SliderChangedEvent event) {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //!!!!
                    if(basicEditor.isEnableObjTouchRotate() == true)
                    basicEditor.rotate(event.getValue());
                }
            });
        }

        public void saveCamera() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.saveCameraView();
                }
            });
        }

        public void loadCamera() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.loadCameraView();
                }
            });
        }

        public void enablePan() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.enableDisablePan();
                }
            });
        }

        public void enableCentering() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.enableDisableCentering();
                }
            });
        }

        /**
         * @return the basicEditor
         */
        public TouchVisuEditor getBasicEditor() {
            return basicEditor;
        }

        /**
         * @param basicEditor the basicEditor to set
         */
        public void setBasicEditor(TouchVisuEditor basicEditor) {
            this.basicEditor = basicEditor;
        }
    }
    
    public static class ModelEditPanelController extends AbstractController {

        private TouchVisuEditor basicEditor;
        

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
                    //!!!!
                    if(!basicEditor.isDragPanelOpen()){
                        basicEditor.setIsDragPanelOpen(true);
                        openDragPanel();
                    }
                    
                    
                    if(basicEditor.isEnableObjTouchRotate()==true)
                        basicEditor.setEnableObjTouchRotate(false);
                    basicEditor.setEnableObjTouchMove(true);
                    basicEditor.moveTo();
                }
            });
        }


        public void rotate() {
            closeAll();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //!!!!
                    if(basicEditor.isEnableObjTouchMove()==true){
                        basicEditor.setNumberClicks(0);
                        basicEditor.setWaitForClick_Move(false);
                        basicEditor.setEnableObjTouchMove(false);
                    }
                        
                    basicEditor.setEnableObjTouchRotate(true);
                    //basicEditor.rotate();
                }
            });
        }

        public void scale() {
            closeAll();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.scale();
                }
            });
        }

        public void setEdgePoint() {
            closeAll();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //TODO: for drag and drop: basicEditor.beginSetEdgePoint();
//                    basicEditor.setEdgePoint();
                    basicEditor.createEdge();  // replaced Bibianas method with Moritz
                }
            });
        }

        public void delete() {
            closeAll();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.deleteComponent();
                }
            });
        }

        public void addNote() {
            closeAll();
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
            closeAll();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.showComponentInfo();
                }
            });
        }

        public void showHistory() {
            closeAll();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.showHistory();
                }
            });
        }

        public void closeModificator() {
            closeAll();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //!!!!
                    if(basicEditor.isEnableObjTouchMove()==true){
                        basicEditor.setNumberClicks(0);
                        basicEditor.setWaitForClick_Move(false);
                        basicEditor.setEnableObjTouchMove(false);
                    }
                        
                    else if(basicEditor.isEnableObjTouchRotate()==true)
                        basicEditor.setEnableObjTouchRotate(false);
                    
                    basicEditor.closeModificator();
                }
            });
        }

        public void showOrHideNote() {
            closeAll();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.showOrHideNote();
                }
            });
        }
        
        public void localUndo() {
            closeAll();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.localUndo();
                }
            });
        }

        public void localRedo() {
            closeAll();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.localRedo();
                }
            });
        }
        
        
        private void openDragPanel(){
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {

                    Nifty nifty = guiAppState.getNifty();
                    Screen screen = nifty.getScreen("hidden");
                    Element win = nifty.getCurrentScreen().findElementByName("drag-modification-panel");
                    
                    win.setConstraintX(SizeValue.percent(85));
                    win.setConstraintY(SizeValue.percent(0));
                    win.setVisible(true);
                    screen.layoutLayers();
                }
            });
        }
        
        private void closeAll(){
            if(basicEditor.isDragPanelOpen()){
               basicEditor.setIsDragPanelOpen(false);
                basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {

                    Nifty nifty = guiAppState.getNifty();
                    Screen screen = nifty.getScreen("hidden");
                    Element win = nifty.getCurrentScreen().findElementByName("drag-modification-panel");
                    win.setVisible(false);
                    
                }
            });
            }
        }
        /**
         * @return the basicEditor
         */
        public TouchVisuEditor getBasicEditor() {
            return basicEditor;
        }

        /**
         * @param basicEditor the basicEditor to set
         */
        public void setBasicEditor(TouchVisuEditor basicEditor) {
            this.basicEditor = basicEditor;
        }

        
    }
}
