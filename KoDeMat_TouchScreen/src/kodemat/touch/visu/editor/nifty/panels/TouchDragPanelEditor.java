/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.touch.visu.editor.nifty.panels;

import com.jme3.app.Application;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.SliderChangedEvent;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.controls.dragndrop.builder.DroppableBuilder;
import de.lessvoid.nifty.controls.slider.builder.SliderBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;
import kodemat.touch.visu.editor.TouchVisuEditor;
import kodemat.visu.appstates.NiftyGuiAppState;
import kodemat.visu.editor.IEditor;
import kodemat.visu.editor.VisuEditor;
import kodemat.visu.editor.nifty.panels.IEditorPanel;

/**
 *
 * @author moro
 */
public class TouchDragPanelEditor implements IEditorPanel {

    NiftyGuiAppState guiAppState;
    private CommandController commandPanelController;

    public TouchDragPanelEditor(Application visuClient) {
        this.guiAppState = visuClient.getStateManager().getState(NiftyGuiAppState.class);
    }

    

    @Override
    public void layout(IEditor basicEditor) {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");


        Element dragPanel = screen.findElementByName("drag-modification-panel");

        PanelBuilder commandpb = new PanelBuilder("drag-panel") {
            {
     
                align(Align.Right);
                valign(VAlign.Bottom);
                childLayoutVertical();
                controller(CommandController.class.getName());
 
  //here
                                        image(new ImageBuilder() {
                                            {
                                                valignCenter();
                                                childLayoutVertical();
                                                filename("Interface/Images/commands.png");
                                                panel(new PanelBuilder("command-top-panel") {
                                                    {

                                                        height("36%");
                                                        width("100%");
                                                        valignTop();
                                                        childLayoutHorizontal();
                                                        paddingLeft("7px");
                                                        paddingRight("7px");

                                                        panel(new PanelBuilder("command-1-panel") {
                                                            {

                                                                height("80%");
                                                                width("33%");
                                                                alignLeft();
                                                                valignBottom();
                                                                interactOnClick("saveCamera()");
                                                            }
                                                        });
                                                        panel(new PanelBuilder("command-2-panel") {
                                                            {
                                                                height("100%");
                                                                width("35%");
                                                                alignCenter();
                                                                interactOnClickRepeat("moveUp()");
                                                            }
                                                        });
                                                        panel(new PanelBuilder("command-3-panel") {
                                                            {


                                                                height("80%");
                                                                width("33%");
                                                                alignRight();
                                                                valignBottom();
                                                                interactOnClick("loadCamera()");


                                                            }
                                                        });
                                                    }
                                                });
                                                panel(new PanelBuilder("command-middlev-panel") {
                                                    {

                                                        height("28%");
                                                        width("100%");
                                                        valignCenter();
                                                        childLayoutHorizontal();

                                                        panel(new PanelBuilder("command-4-panel") {
                                                            {


                                                                height("100%");
                                                                width("36%");
                                                                alignLeft();
                                                                valignBottom();
                                                                interactOnClickRepeat("moveLeft()");

                                                            }
                                                        });
                                                        panel(new PanelBuilder("command-5-panel") {
                                                            {
                                                                height("100%");
                                                                width("28%");
                                                                alignCenter();
                                                                interactOnClick("openRotationSlide()");

                                                            }
                                                        });
                                                        panel(new PanelBuilder("command-6-panel") {
                                                            {
                                                                height("100%");
                                                                width("36%");
                                                                alignRight();
                                                                valignBottom();

                                                                interactOnClickRepeat("moveRight()");
                                                            }
                                                        });
                                                    }
                                                });
                                                panel(new PanelBuilder("command-bottom-panel") {
                                                    {
                                                        height("36%");
                                                        width("100%");
                                                        valignBottom();
                                                        childLayoutHorizontal();
                                                        paddingLeft("7px");
                                                        paddingRight("7px");

                                                        panel(new PanelBuilder("command-7-panel") {
                                                            {
                                                                height("80%");
                                                                width("33%");
                                                                alignLeft();
                                                                valignTop();
                                                                interactOnClick("enablePan()");

                                                            }
                                                        });
                                                        panel(new PanelBuilder("command-8-panel") {
                                                            {
                                                                height("100%");
                                                                width("35%");
                                                                alignCenter();
                                                                interactOnClickRepeat("moveDown()");
                                                            }
                                                        });
                                                        panel(new PanelBuilder("command-9-panel") {
                                                            {
                                                                height("80%");
                                                                width("33%");
                                                                alignRight();
                                                                valignTop();
                                                                interactOnClick("enableCentering()");
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
         
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

        dragPanel.layoutElements();

//        next two lines could cause crash
        //        commandPanel.setFocusable(false);

    }

    public CommandController getCommandPanelController() {
        return commandPanelController;
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
}
