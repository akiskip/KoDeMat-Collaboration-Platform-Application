/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.touch.visu.editor.nifty.panels;

import com.jme3.app.Application;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ElementBuilder;
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
import static kodemat.touch.visu.editor.nifty.panels.TouchImportModelPanel.guiAppState;
import kodemat.visu.editor.VisuEditor;
import kodemat.visu.editor.nifty.panels.IEditorPanel;
import org.openide.util.Exceptions;

/**
 *
 * @author OzgeA
 */
public class ViewCube implements IEditorPanel {

    static NiftyGuiAppState guiAppState;
    private Element editorPanel;
    private ViewPanelController viewPanelController;
    final Nifty nifty;

    public ViewCube(Application visuClient) {
        this.guiAppState = visuClient.getStateManager().getState(NiftyGuiAppState.class);
        nifty = guiAppState.getNifty();
    }

    @Override
    public void layout(IEditor basicEditor) {

        final Screen screen = nifty.getScreen("hidden");
        Element toolbar = screen.findElementByName("view-cube-panel");

        toolbar.setConstraintX(SizeValue.percent(85));
        toolbar.setConstraintY(SizeValue.percent(0));

        PanelBuilder pb = new PanelBuilder("model-panel") {
            {
                height("30%");
                childLayoutHorizontal();
                valignTop();
                controller(ViewPanelController.class.getName());
                

            }
        };
        editorPanel = pb.build(nifty, screen, toolbar);
        viewPanelController = editorPanel.getControl(ViewPanelController.class);
        viewPanelController.setBasicEditor((TouchVisuEditor) basicEditor);

        PanelBuilder pb2 = new PanelBuilder("small-icons-panel") {
            {
                height("30%");
                childLayoutHorizontal();
                valignCenter();
                controller(ViewPanelController.class.getName());

            }
        };
        Element middlePanel = pb2.build(nifty, screen, toolbar);
        ViewPanelController middlePanelController = middlePanel.getControl(ViewPanelController.class);
        middlePanelController.setBasicEditor((TouchVisuEditor) basicEditor);

        PanelBuilder pb3 = new PanelBuilder("small-icons-panel") {
            {
                height("30%");
                childLayoutHorizontal();
                valignBottom();
                controller(ViewPanelController.class.getName());

            }
        };
        Element bottomPanel = pb3.build(nifty, screen, toolbar);
        ViewPanelController bottomPanelController = bottomPanel.getControl(ViewPanelController.class);
        bottomPanelController.setBasicEditor((TouchVisuEditor) basicEditor);

       
        new ImageBuilder("Back") {
            {
                paddingRight("5px");
                width("30");
                height("30");
                filename("Interface/Buttons/back.png");
                visibleToMouse(true);
                interactOnClick("viewBack()");
                onClickEffect(new EffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/back.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/back.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/back.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/back.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Back");
                    }
                });
            }
        }.build(nifty, screen, editorPanel);
    


        new ImageBuilder("Left") {
            {
                paddingRight("5px");
                width("30");
                height("30");
                filename("Interface/Buttons/left.png");
                visibleToMouse(true);
                interactOnClick("viewLeft()");
                onClickEffect(new EffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/left.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/left.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/left.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/left.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Left");
                    }
                });
            }
        }.build(nifty, screen, middlePanel);

        new ImageBuilder("Top") {
            {
                paddingRight("5px");
                width("30");
                height("30");
                filename("Interface/Buttons/top.png");
                visibleToMouse(true);
                interactOnClick("viewTop()");
                onClickEffect(new EffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/top.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/top.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/top.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/top.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Top");
                    }
                });
            }
        }.build(nifty, screen, middlePanel);

        new ImageBuilder("Left") {
            {
                paddingRight("5px");
                width("30");
                height("30");
                filename("Interface/Buttons/right.png");
                visibleToMouse(true);
                interactOnClick("viewRight()");
                onClickEffect(new EffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/right.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/right.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/right.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/right.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "Right");
                    }
                });
            }
        }.build(nifty, screen, middlePanel);


        new ImageBuilder("Back") {
            {
                paddingRight("5px");
                width("30");
                height("30");
                filename("Interface/Buttons/front.png");
                visibleToMouse(true);
                interactOnClick("viewOriginal()");
                onClickEffect(new EffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/front.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/front.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("changeImage") {
                    {
                        getAttributes().setAttribute("active", "Interface/Buttons/front.png");
                        getAttributes().setAttribute("inactive", "Interface/Buttons/front.png");
                    }
                });
                onHoverEffect(new HoverEffectBuilder("hint") {
                    {
                        getAttributes().setAttribute("hintText", "");
                    }
                });
            }
        }.build(nifty, screen, bottomPanel);



        toolbar.layoutElements();
    }

    public static class ViewPanelController extends AbstractController {

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

        public void viewBack() {
             basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                public void run() {
                    basicEditor.getCammeraControllAppState().getCameraControllerT().viewBack();
                }
            });
        }

        public void viewOriginal() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                public void run() {
                    basicEditor.getCammeraControllAppState().getCameraControllerT().viewOriginal();
                }
            });
        }
        
        public void viewLeft() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                public void run() {
                    basicEditor.getCammeraControllAppState().getCameraControllerT().viewLeft();
                }
            });
        }
        
        public void viewRight() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                public void run() {
                    basicEditor.getCammeraControllAppState().getCameraControllerT().viewRight();
                }
            });
        }
        
        public void viewTop() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                public void run() {
                    basicEditor.getCammeraControllAppState().getCameraControllerT().viewTop();
                }
            });
        }
    }
}
