/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.touch.visu.editor.nifty.panels;

import com.jme3.app.Application;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;
import kodemat.touch.visu.editor.TouchVisuEditor;
import kodemat.visu.appstates.CameraControllerAppState;
import kodemat.visu.appstates.NiftyGuiAppState;
import kodemat.visu.editor.EditorManager;
import kodemat.visu.editor.IEditor;
import kodemat.visu.editor.VisuEditor;
import kodemat.visu.editor.nifty.panels.IEditorPanel;
import kodemat.visu.editor.selection.IVisuSelectable;
import kodemat.visu.input.mouse.ClickSelection;
import kodemat.visudata.visuComponents.VisuComponent;

/**
 *
 * @author moro
 */
public class UndoRedoEditor implements IEditorPanel {

    NiftyGuiAppState guiAppState;

    public UndoRedoEditor(Application visuClient) {
        this.guiAppState = visuClient.getStateManager().getState(NiftyGuiAppState.class);
    }


    @Override
    public void layout(IEditor basicEditor) {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");


        Element toolbarUR = screen.findElementByName("UR-panel");

        PanelBuilder pUR = new PanelBuilder("UR-buttons-panel") {
            {
                childLayoutVertical();
                controller(URPanelController.class.getName());
                padding("10px");

            }
        };
        Element URPanel = pUR.build(nifty, screen, toolbarUR);
        URPanelController uRPanelController = URPanel.getControl(URPanelController.class);
        uRPanelController.setBasicEditor((TouchVisuEditor)basicEditor);

        new ImageBuilder("global-undo-button") {
            {

                height("20px");
                filename("Interface/Images/undo.png");
                width("20px");
                alignRight();
                interactOnClick("globalUndo()");
            }
        }.build(nifty, screen, URPanel);

        new ImageBuilder("global-redo-button") {
            {

                height("20px");
                filename("Interface/Images/redo.png");
                width("20px");
                alignRight();
                interactOnClick("globalRedo()");
            }
        }.build(nifty, screen, URPanel);

        toolbarUR.layoutElements();
        toolbarUR.setFocusable(false);
        toolbarUR.setVisible(false);
    }


    public static class URPanelController extends AbstractController {

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

        public void globalUndo() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.globalUndo();
                }
            });
        }

        public void globalRedo() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.globalRedo();
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
