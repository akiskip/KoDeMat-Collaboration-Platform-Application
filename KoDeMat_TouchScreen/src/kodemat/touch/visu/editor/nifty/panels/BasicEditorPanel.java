/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.touch.visu.editor.nifty.panels;

import com.jme3.app.Application;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import kodemat.touch.visu.editor.TouchVisuEditor;
import kodemat.visu.appstates.NiftyGuiAppState;
import kodemat.visu.editor.IEditor;
import kodemat.visu.editor.VisuEditor;
import kodemat.visu.editor.nifty.panels.IEditorPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author Kipouridis
 */
    public class BasicEditorPanel  implements IEditorPanel{
    static NiftyGuiAppState guiAppState;
    private Element editorPanel;
    private EditorPanelController editorPanelController;
    final Nifty nifty;

    public BasicEditorPanel(Application visuClient) {
        this.guiAppState = visuClient.getStateManager().getState(NiftyGuiAppState.class); 
        nifty = guiAppState.getNifty();
    }

    @Override
    public void layout(IEditor basicEditor) {
   
        final Screen screen = nifty.getScreen("hidden");
        Element toolbar = screen.findElementByName("toolbar-panel");

        PanelBuilder pb = new PanelBuilder("basic-editor-panel") {
            {
                childLayoutHorizontal();
                controller(EditorPanelController.class.getName());
                padding("10px,10px");
                
            }
};
        editorPanel = pb.build(nifty, screen, toolbar);
        editorPanelController = editorPanel.getControl(EditorPanelController.class);
        editorPanelController.setBasicEditor((TouchVisuEditor)basicEditor);
        
                 new ImageBuilder("cursorImage") {
                       {         
                          marginRight("5px");
                                                valignCenter();
                                                childLayoutVertical();
                                                filename("Interface/Buttons/button_cursor.png");
                                                 visibleToMouse(true);
                                      interactOnClick("toggleClickToSelect()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
        
            getAttributes().setAttribute("active", "Interface/Buttons/button_cursor_mouseover.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/button_cursor.png");
                                  
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Select Object");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
             
                 new ImageBuilder("WaypointButton") {
                       {         
                             marginRight("5px");
                                                valignCenter();
                                                childLayoutVertical();
                                                filename("Interface/Buttons/button_create_box.png");
                                                 visibleToMouse(true);
                                      interactOnClick("createBox()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
           
            getAttributes().setAttribute("active", "Interface/Buttons/button_create_box_mouseover.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/button_create_box.png");
                                  
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Place Edge Point");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
            
                 new ImageBuilder("importModelImage") {
                       {         
                            marginRight("5px");
                                                valignCenter();
                                                childLayoutVertical();
                                                filename("Interface/Buttons/button_import_model.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModelFromFile()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/button_import_model_mouseover.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/button_import_model.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Import Model");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
                 
                 
                    new ImageBuilder("importXMLImage") {
                       {         
                            marginRight("5px");
                                                valignCenter();
                                                childLayoutVertical();
                                                filename("Interface/Buttons/button_import_layout.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importGEXF()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/button_import_layout_mouseover.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/button_import_layout.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Import Layout");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
                    
                    
                       new ImageBuilder("exportXMLImage") {
                       {         
                            marginRight("5px");
                                                valignCenter();
                                                childLayoutVertical();
                                                filename("Interface/Buttons/button_export_layout.png");
                                                 visibleToMouse(true);
                                                 
                                      interactOnClick("exportGEXF()");
                                      
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/button_export_layout_mouseover.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/button_export_layout.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Export Layout");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
                       
                                         new ImageBuilder("addNoteImage") {
                       {                
                             marginRight("5px");
                                                valignCenter();
                                                childLayoutVertical();
                                                filename("Interface/Buttons/button_show_hide_notes.png");
                                                 visibleToMouse(true);
                                      interactOnClick("addMarking()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/button_show_hide_notes_mouseover.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/button_show_hide_notes.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Add Comment");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
                                         
                                         
                new ImageBuilder("testImage") {
                       {         
                            marginRight("5px");
                                                valignCenter();
                                                childLayoutVertical();
                                                filename("Interface/Buttons/button_test.png");
                                                 visibleToMouse(true);
                                      interactOnClick("teleEditorTestStart()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/button_test_mouseover.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/button_test.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Test Execute");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);

        toolbar.layoutElements();
    }


   

  
     public static class EditorPanelController extends AbstractController {

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

        public void test() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.test();
                }
            });
        }
        private boolean clickToSelectEnabled=false;
   public void toggleClickToSelect() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //!!!!
                    basicEditor.getCammeraControllAppState().getCameraControllerT().viewTop();
//                    if(clickToSelectEnabled){
                    basicEditor.toogleClickToSelect(true);
//                    clickToSelectEnabled = false;
//                    }
//                   else
//                    {
//                    basicEditor.toogleClickToSelect(false);
//                    clickToSelectEnabled = true;
//                    }
                }
            });
        }
          public void importModelFromFile() {
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
          public void teleEditorTestStart() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                   
                        basicEditor.teleEditorTest();
                  
                }
            });
        }
        
        public void createBox() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.createEdge();
                }
            });
        }
        public void importGEXF() {
            //TODO: We have to revise the basicEditor class so that we dont have to call smt like this
            basicEditor.createEssentials();
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.importGEXF();
                }
            });
        }
        public void exportGEXF() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.exportGEXF();
                }
            });
        }
        
          public void addMarking() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
//                    basicEditor.showOrHideNote();
                    basicEditor.addFocusArrow();
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
    
    
    }
    
    
    
     
