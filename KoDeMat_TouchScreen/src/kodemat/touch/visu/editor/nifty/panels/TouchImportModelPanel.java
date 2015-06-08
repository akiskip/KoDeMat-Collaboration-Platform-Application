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
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import kodemat.visu.appstates.NiftyGuiAppState;
import kodemat.visu.editor.IEditor;
import kodemat.touch.visu.editor.TouchVisuEditor;
import kodemat.visu.editor.VisuEditor;
import kodemat.visu.editor.nifty.panels.IEditorPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author OzgeA
 */
    public class TouchImportModelPanel implements IEditorPanel{
    static NiftyGuiAppState guiAppState;
    private Element editorPanel;
    private EditorPanelController editorPanelController;
    final Nifty nifty;

    public TouchImportModelPanel(Application visuClient) {
        this.guiAppState = visuClient.getStateManager().getState(NiftyGuiAppState.class); 
         nifty = guiAppState.getNifty();
    }

    @Override
    public void layout(IEditor basicEditor) {
   
        final Screen screen = nifty.getScreen("hidden");
        Element toolbar = screen.findElementByName("toolbar-import-panel");

        PanelBuilder pb = new PanelBuilder("import-model-panel") {
            {
                childLayoutVertical();
                controller(EditorPanelController.class.getName());
                padding("0px,10px");
                
            }
};
        editorPanel = pb.build(nifty, screen, toolbar);
        editorPanelController = editorPanel.getControl(EditorPanelController.class);
        editorPanelController.setBasicEditor((TouchVisuEditor)basicEditor);
        
        Element EHBtoolbar = screen.findElementByName("pop-up-importEHB");
        PanelBuilder EHBpb = new PanelBuilder("importEHB-panel") {
            {
                childLayoutVertical();
                controller(EditorPanelController.class.getName());
              
            }
        };
        Element ehb = EHBpb.build(nifty, screen, EHBtoolbar);
        editorPanelController = ehb.getControl(EditorPanelController.class);
        editorPanelController.setBasicEditor((TouchVisuEditor)basicEditor);
         
        Element FLWtoolbar = screen.findElementByName("pop-up-importFLW");
        PanelBuilder FLWpb = new PanelBuilder("importFLW-panel") {
            {
                childLayoutVertical();
                controller(EditorPanelController.class.getName());
              
            }
        };
        Element flw = FLWpb.build(nifty, screen, FLWtoolbar);
        editorPanelController = flw.getControl(EditorPanelController.class);
        editorPanelController.setBasicEditor((TouchVisuEditor)basicEditor);
        
                 new ImageBuilder("boden") {
                       {         
                          marginRight("2px");  
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/boden.png");
                                                 visibleToMouse(true);
                                     // interactOnClick("toggleClickToSelect()");
                                                 interactOnClick("importModel_boden()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
        
            getAttributes().setAttribute("active", "Interface/Buttons/boden.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/boden.png");
                                  
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Boden");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
             
                 new ImageBuilder("container") {
                       {         
                             marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/container.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_container()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
           
            getAttributes().setAttribute("active", "Interface/Buttons/container.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/container.png");
                                  
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Container");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
            
                 new ImageBuilder("EHBpanel") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/EHB.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_EHB()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/EHB.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/EHB.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "EHB");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
                 
                 
                    new ImageBuilder("KukaRobot") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/kuka_robot.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_KUKARobot()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/kuka_robot.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/kuka_robot.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "KUKA Robot");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
                    
                    
                       new ImageBuilder("palette") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/palette.png");
                                                 visibleToMouse(true);
                                                 
                                      interactOnClick("importModel_palette()");
                                      
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/palette.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/palette.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Palette");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
                       
                                         new ImageBuilder("RBG") {
                       {                
                             marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/RBG.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_RBG()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/RBG.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/RBG.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "RBG");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
                                         
                                         
                new ImageBuilder("regalRBG") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/regal_RBG.png");
                                                 visibleToMouse(true);
                                      interactOnClick("ImportModel_regalRBG()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/regal_RBG.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/regal_RBG.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Regal RBG");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
                
                new ImageBuilder("Rollen Foerderer") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/rf.png");
                                                 visibleToMouse(true);
                                      interactOnClick("togglePanel_RollenFoerderer()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/rf.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/rf.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Rollen Foerderer");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
                
                new ImageBuilder("rf_palette") {
                       {     
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/rf_palette.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_rfPalette()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/rf_palette.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/rf_palette.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Rollen Foerderer Palette");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);
                
                new ImageBuilder("stapler") {
                       {     
                            marginRight("2px"); marginTop("5px"); 
                            
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/stapler.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_stapler()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/stapler.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/stapler.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Stapler");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, editorPanel);


                new ImageBuilder("ehb") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/EHB.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_EHB()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/EHB.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/EHB.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "EHB");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, ehb);
                new ImageBuilder("ehb_katze") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/button_import_model.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_EHBKatze()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/button_import_model_mouseover.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/button_import_model.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "EHB/Katze");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, ehb);
                
                new ImageBuilder("ehb_lam") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/button_import_model.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_EHBLam()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/button_import_model_mouseover.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/button_import_model.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "EHB/LAM");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, ehb);
                
                //------------FLW pop up panel ------------//
                
                new ImageBuilder("rf_kreis") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/rf_kreis.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_RFKreis()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/rf_kreis.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/rf_kreis.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Rollen Foerderer Kreis");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, flw);
                
                new ImageBuilder("rf1") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/rf_part1.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_RF1()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/rf_part1.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/rf_part1.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Rollen Foerderer No:1");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, flw);
                new ImageBuilder("rf2") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/rf_part2.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_RF2()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/rf_part2.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/rf_part2.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Rollen Foerderer No:2");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, flw);
                
                new ImageBuilder("rf2inv") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/rf_part2inv.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_RF2inv()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/rf_part2inv.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/rf_part2inv.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Rollen Foerderer No:2(inv)");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, flw);
                
                new ImageBuilder("rf3") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/rf_part3.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_RF3()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/rf_part3.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/rf_part3.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Rollen Foerderer No:3");
         
        }});
                  
                                             }
                                      
                          }.build(nifty, screen, flw);
                
                new ImageBuilder("rf3") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/rf_part3inv.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_RF3inv()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/rf_part3inv.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/rf_part3inv.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Rollen Foerderer No:3(inv)");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, flw);
                new ImageBuilder("rf4") {
                       {         
                            marginRight("2px"); marginTop("5px"); 
                                                valignCenter();
                                                childLayoutVertical();
                                                width("50");
                                                height("50");
                                                filename("Interface/Buttons/rf_part4.png");
                                                 visibleToMouse(true);
                                      interactOnClick("importModel_RF4()");
                                      onHoverEffect(new HoverEffectBuilder("changeImage"){{
            getAttributes().setAttribute("hintText", "Here is a hint!");
            getAttributes().setAttribute("active", "Interface/Buttons/rf_part4.png");
            getAttributes().setAttribute("inactive", "Interface/Buttons/rf_part4.png");
                     
        }});
                  onHoverEffect(new HoverEffectBuilder("hint"){{
            getAttributes().setAttribute("hintText", "Rollen Foerderer No:4");
         
        }});
                                             }
                                      
                          }.build(nifty, screen, flw);
                                                            


        toolbar.layoutElements();
    }


   

  
     public static class EditorPanelController extends AbstractController {

        private TouchVisuEditor basicEditor;
        private int toggleEHBpanel = 0;
        private int toggleFLWpanel = 0;

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
        
        public void createBox() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.createEdge();
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
        
        public void importModel_boden(){
            closeAll();
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\Boden\\boden.j3o");
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }

        public void importModel_container(){
            closeAll();
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\Container\\container.j3o");
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
        
        public void togglePanel_EHB(){
            if(toggleEHBpanel == 0){
                if(toggleFLWpanel == 1){
                  closeImportPanelFLW();
                  toggleFLWpanel = 0;
                }
                openImportPanelEHB(); 
                toggleEHBpanel = 1;
            }
            else {
              closeImportPanelEHB();
              toggleEHBpanel = 0;
            }
        }
        
        public void togglePanel_RollenFoerderer(){
            if(toggleFLWpanel == 0){ 
                if(toggleEHBpanel == 1){
                  closeImportPanelEHB();
                  toggleEHBpanel = 0;
                }
                openImportPanelFLW(); 
                toggleFLWpanel = 1;
            }
            else {
              closeImportPanelFLW();
              toggleFLWpanel = 0;
            }
        }

        public void importModel_EHB(){
            
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\EHB\\ehb.j3o");
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
        
        public void importModel_EHBKatze(){
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\EHB\\Katze\\Katze.j3o");
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
        
        public void importModel_EHBLam(){
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\EHB\\LAM\\LAM.j3o");
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
  
        public void importModel_KUKARobot(){
            closeAll();
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\KUKA_Robot\\kuka_robot.j3o");
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }     
        
        public void importModel_palette(){
            closeAll();
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\Pallete\\palette.j3o");
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }     
  
       public void importModel_RBG(){
            closeAll();
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\RBG\\RBG.j3o");
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }     
        
        public void importModel_regalRBG(){
            closeAll();
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\Regal_RBG\\regal_RBG.j3o");
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }   
        
        public void importModel_rfPalette(){
            closeAll();
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\Rollenfoerderer_pallete\\RF_pallete.j3o");
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
        
        public void importModel_stapler(){
            closeAll();
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\Stapler\\stapler.j3o");
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
        
        public void importModel_RF1(){
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\RollenFoerderer_FLW\\RF_part1.j3o");
        basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
        public void importModel_RF2(){
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\RollenFoerderer_FLW\\RF_part2.j3o");
        basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
        
        public void importModel_RF2inv(){
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\RollenFoerderer_FLW\\RF_part2inv.j3o");
        basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
        public void importModel_RF3(){
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\RollenFoerderer_FLW\\RF_part3.j3o");
        basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
        
        public void importModel_RF3inv(){
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\RollenFoerderer_FLW\\RF_part3inv.j3o");
        basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
        
        public void importModel_RF4(){
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\RollenFoerderer_FLW\\RF_part4.j3o");
        basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }
    
        public void importModel_RFKreis(){
            final File fileName = new File("lib\\assets\\CeMAT_Assets\\RollenFoerderer_FLW\\RF_Kreis.j3o");
        basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        basicEditor.importModelComponentDirect(fileName);
                    } catch (FileNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            });
        }

        private void openImportPanelEHB() {
                Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");

     
        Element e2 = nifty.getCurrentScreen().findElementByName("pop-up-importEHB");
        e2.setConstraintX(SizeValue.percent(90));
        e2.setConstraintY(SizeValue.percent(20));
        e2.setFocusable(true);
        e2.setVisible(true);
        screen.layoutLayers();
        }

        private void closeImportPanelEHB() {
                Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");

     
        Element e2 = nifty.getCurrentScreen().findElementByName("pop-up-importEHB");
        e2.setFocusable(false);
        e2.setVisible(false);
        
        }

        private void openImportPanelFLW() {
            Nifty nifty = guiAppState.getNifty();
            Screen screen = nifty.getScreen("hidden");

        Element e2 = screen.findElementByName("pop-up-importFLW");
        //e2.
        //e2.setConstraintY(SizeValue.percent(20));
        
        
        e2.setConstraintX(SizeValue.percent(90));
        e2.setConstraintY(SizeValue.percent(20));
        e2.setVisible(true);
        e2.setFocusable(true);
        screen.layoutLayers();
        }

        private void closeImportPanelFLW() {
            Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");

     
        Element e2 = nifty.getCurrentScreen().findElementByName("pop-up-importFLW");
        e2.setFocusable(false);
        e2.setVisible(false);        }
        
            
                   
       private void closeAll(){
           if(toggleEHBpanel == 1){
                  closeImportPanelEHB();
                  toggleEHBpanel = 0;
                }
           if(toggleFLWpanel == 1){
                  closeImportPanelFLW();
                  toggleEHBpanel = 0;
                }
       }         
            
                   
               
     
     }

 
    
    }
    
    
    
     
