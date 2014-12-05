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
package jade_layout;

import com.hazelcast.core.IMap;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial.CullHint;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;
import java.awt.EventQueue;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JOptionPane;
import kodemat.visu.appstates.CameraControllerAppState;
import kodemat.visu.appstates.NiftyGuiAppState;
import kodemat.visu.VisuClient;
import kodemat.visu.appstates.VisuHazelcastAppState;
import kodemat.visu.input.mouse.ClickSelection;
import kodemat.visu.editor.IEditor;
import kodemat.visu.editor.EditorManager;
import kodemat.visu.editor.selection.IVisuSelectable;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;

/**
 * Class that handles the clicks made on the screen.
 *
 * @author Koshkabb
 */
public class AuftragEditor implements IEditor {

    SimpleApplication visuClient;
    EditorManager editorManager;
    CameraControllerAppState cameraControllerAppState;
    NiftyGuiAppState guiAppState;
    private InputManager inputManager;
    Element editorPanel;
    Element modificationPanel;
    private ClickSelection<VisuComponent> lastCursorSelection = null;
    VisuHelper visuHelper;
    VisuHazelcastAppState hazelcastAppState;
    int idBox = 0;
    public static final String AUFTRAG_MENU_BUTTON = "AuftragEditor_AUFTRAG_MENU_BUTTON";
    private static final int EMPTY = 1;
    private static final int TRANSPORT = 2;
    private static final int MANUAL = 3;
    private int auftragType;
    private KeyListener keyListener = new KeyListener();
    public IMap<String, SocketAddress> status;
    private ArrayList<VisuVector3f> auftragCoord;

    /**
     * The nifty GUI is created here.
     *
     * @param visuClient
     * @param editorManager
     * @param cameraControllerAppState
     */
    @Override
    public void initialize(Application visuClient) {

        System.out.println("INITIALIZE BasicEditor START");

        this.visuClient = (SimpleApplication) visuClient;
        this.editorManager = editorManager;
        this.cameraControllerAppState = cameraControllerAppState;
        this.guiAppState = visuClient.getStateManager().getState(NiftyGuiAppState.class);
        this.hazelcastAppState = visuClient.getStateManager().getState(VisuHazelcastAppState.class);
       
        this.initExtraInputMapping();
        this.initUI();

        //TODO: maybe move the following into the initUI method
        cancelManualAuftrag();


        System.out.println("INITIALIZE BasicEditor END");


    }

    /**
     * Sets the nifty text label for informative purposes.
     *
     * @param text The text which must be displayed
     */
    private void setLabel(String text) {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        Element e = nifty.getCurrentScreen().findElementByName("auftrag-label");


        if (e != null) {
            e.getRenderer(TextRenderer.class).setText(text);
            e.setConstraintWidth(new SizeValue(e.getRenderer(TextRenderer.class).getTextWidth() + ""));
            screen.layoutLayers();
        }
    }

    /**
     * Start process of creating a new empty task.
     */
    public void newEmptyAuftrag() {
        String text = "Double-click on desired initial point";
        setLabel(text);
        auftragCoord = new ArrayList<VisuVector3f>();
        auftragType = EMPTY;
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        Element e = nifty.getCurrentScreen().findElementByName("empty-auftrag-button");
        e.setFocusable(false);
    }

    /**
     * Start process of creating a new transport task.
     */
    public void newTransportAuftrag() {
        String text = "Double-click on desired initial point";
        setLabel(text);
        auftragCoord = new ArrayList<VisuVector3f>();
        auftragType = TRANSPORT;
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        Element e = nifty.getCurrentScreen().findElementByName("transport-auftrag-button");
        e.setFocusable(false);

    }

    /**
     * Open nifty GUI menu for creating tasks.
     */
    public void openAuftragMenu() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");

        Element e = screen.findElementByName("auftrag-panel");
        e.setFocusable(!e.isFocusable());
        e.setVisible(!e.isVisible());
        cancelManualAuftrag();
    }

    /**
     * Create new component to save the task information.
     *
     * @param initialLabel The initial waypoint, if it is a manual task. If not,
     * null
     * @param endLabel The end waypoint, if it is a manual task. If not, null
     */
    private void createAuftragComponent(String initialLabel, String endLabel) {
        if (hazelcastAppState.getClient() != null) {
            if (visuHelper == null) {
                this.visuHelper = new VisuHelper(hazelcastAppState.getClient());
            }
            VisuComponent initialPoint = visuHelper.getComponent("InitialPoint");
            if (initialPoint == null) {
                initialPoint = visuHelper.createComponent("InitialPoint");
                initialPoint.setType(new VisuType(VisuType.NODE, null));
                if (initialLabel != null) {
                    initialPoint.setLabel(initialLabel);
                }
            }

            VisuComponent endPoint = visuHelper.getComponent("EndPoint");
            if (endPoint == null) {
                endPoint = visuHelper.createComponent("EndPoint");
                endPoint.setType(new VisuType(VisuType.NODE, null));
                if (endLabel != null) {
                    endPoint.setLabel(endLabel);
                }
            }


            VisuComponent auftragNode = visuHelper.getComponent("Auftrag");
            if (auftragNode == null) {
                auftragNode = visuHelper.createComponent("Auftrag");
                auftragNode.setType(new VisuType(VisuType.NODE, null));
            }
            initialPoint.setParent(auftragNode.getId());
            endPoint.setParent(auftragNode.getId());
            initialPoint.setTranslation(auftragCoord.get(0));
            endPoint.setTranslation(auftragCoord.get(1));

            auftragNode.setTranslation(new VisuVector3f(0, 0, 0));
            switch (auftragType) {
                case EMPTY:
                    auftragNode.setLabel("Empty order" + " - " + idBox);

                    break;
                case TRANSPORT:
                    auftragNode.setLabel("Transport order" + " - " + idBox);

                    break;
                case MANUAL:
                    auftragNode.setLabel("Manual order" + " - " + idBox);

                    break;
            }
        }
    }

    /**
     * Create a new box to show the initial and end points.
     *
     * @param name The name of the box
     */
    public void createBox(String name) {
        if (hazelcastAppState.getClient() != null) {
            if (visuHelper == null) {
                this.visuHelper = new VisuHelper(hazelcastAppState.getClient());
            }

            VisuComponent box = visuHelper.getComponent(name + idBox);
            if (box == null) {
                box = visuHelper.createComponent(name + idBox);
                box.setType(new VisuType(VisuType.BOX, null));
            }
            box.setScale(new VisuVector3f(0.6f, 0.6f, 0.6f));
            box.setTranslation(auftragCoord.get(auftragCoord.size() - 1));
            if (name.contains("Initial")) {
                box.setLabel("Initial Point");
            } else {
                box.setLabel("End Point");
            }

        }
    }

    /**
     * Erase both the initial box and the end box.
     */
    private void eraseBoxes() {
        visuHelper.deleteComponent("InitialBox" + idBox);
        visuHelper.deleteComponent("EndBox" + idBox);
    }

    /**
     * Show a dialog asking confirmation.
     */
    private void ShowDialog() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String text = "";
                int n = JOptionPane.showConfirmDialog(null, "Place order in these coordinates?",
                        "Warning",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                switch (n) {
                    case JOptionPane.YES_OPTION:
                        createAuftragComponent(null, null);
                        auftragCoord = null;
                        break;
                    case JOptionPane.NO_OPTION:
                        eraseBoxes();
                        text = "Double-click on desired initial point";
                        auftragCoord = new ArrayList<VisuVector3f>();
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        eraseBoxes();
                        auftragCoord = null;
                        break;
                }
                setLabel(text);

            }
        });

    }

    /**
     * Check if the task coordinates are inside the set ground limit.
     * @param pos       The coordinates
     * @return          The corrected (if necessary) coordinates
     */
    public VisuVector3f checkDistance(VisuVector3f pos) {
        if (pos.x < -1.8972f) {
            pos.x = -1.8972f;
        }
        if (pos.x > 2.0785f) {
            pos.x = 2.0785f;
        }
        if (pos.z < -6.3859f) {
            pos.z = -6.3859f;
        }
        if (pos.z > -0.4683f) {
            pos.z = -0.4683f;
        }
        return pos;

    }

    @Override
    public void cleanup() {
    }

    /**
     * Handle the incoming mouse clicks.
     * @param selection         Click selection
     */
    @Override
    public void handleClick(final ClickSelection<VisuComponent> selection) {
        if (selection.getClickCount() == 2 && !selection.isSpecial()) {
            cameraControllerAppState.getCursorGeo().setLocalTranslation(selection.getCollisionResult().getContactPoint().clone());
            cameraControllerAppState.getCursorGeo().setCullHint(CullHint.Inherit);
            lastCursorSelection = selection;
            if (auftragCoord != null) {
                ClickSelection<VisuComponent> lastSelection = getLastSelection();
                Vector3f contactPnt = lastSelection.getCollisionResult().getContactPoint();
                VisuVector3f pos = new VisuVector3f(contactPnt.x, contactPnt.y, contactPnt.z);
                pos = checkDistance(pos);
                auftragCoord.add(pos);
                String text = "";
                if (auftragCoord.size() == 1) {
                    text = "Double-click on desired end point";
                    idBox++;
                    createBox("InitialBox");
                    setLabel(text);
                } else if (auftragCoord.size() == 2) {
                    createBox("EndBox");
                    ShowDialog();
                }

            }
        }


        if (selection.getClickCount()
                > 1 && selection.isSpecial()) {
            cameraControllerAppState.getCameraController().setCamFocus(selection.getCollisionResult().getContactPoint());
        }
    }

    /**
     * @return the lastSelection
     */
    public ClickSelection<VisuComponent> getLastSelection() {
        return lastCursorSelection;


    }

    public VisuHelper getVisuHelper() {
        return this.visuHelper;


    }
    
    /**
     * Start process of creating a new manual task.
     */
    private void newManualAuftrag() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        Element e = screen.findElementByName("auftrag-data-panel");
        e.enable();
        auftragType = MANUAL;

    }

    /**
     * Fetch data introduced for manual task.
     */
    private void sendManualAuftrag() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        String initialWP, endWP;
        int xIn, yIn, zIn, xEnd, yEnd, zEnd;
        initialWP = screen.findNiftyControl("wp-initial-field", TextField.class).getText();
        screen.findNiftyControl("wp-initial-field", TextField.class).setText("");
        endWP = screen.findNiftyControl("wp-end-field", TextField.class).getText();
        screen.findNiftyControl("wp-end-field", TextField.class).setText("");
        if (initialWP.equals("") || endWP.equals("")) {
            setLabel("The waypoint fields cannot be left empty!");
        } else {
            String str = screen.findNiftyControl("x-initial-field", TextField.class).getText();
            screen.findNiftyControl("x-initial-field", TextField.class).setText("");
            if (!str.equals("")) {
                yIn = Integer.parseInt(str);
            } else {
                yIn = 0;
            }
            str = screen.findNiftyControl("y-initial-field", TextField.class).getText();
            screen.findNiftyControl("y-initial-field", TextField.class).setText("");

            if (!str.equals("")) {
                xIn = Integer.parseInt(str);
            } else {
                xIn = 0;
            }
            str = screen.findNiftyControl("z-initial-field", TextField.class).getText();
            screen.findNiftyControl("z-initial-field", TextField.class).setText("");

            if (!str.equals("")) {
                zIn = Integer.parseInt(str);
            } else {
                zIn = 0;
            }
            str = screen.findNiftyControl("x-end-field", TextField.class).getText();
            screen.findNiftyControl("x-end-field", TextField.class).setText("");

            if (!str.equals("")) {
                yEnd = Integer.parseInt(str);
            } else {
                yEnd = 0;
            }
            str = screen.findNiftyControl("y-end-field", TextField.class).getText();
            screen.findNiftyControl("y-end-field", TextField.class).setText("");

            if (!str.equals("")) {
                xEnd = Integer.parseInt(str);
            } else {
                xEnd = 0;
            }
            str = screen.findNiftyControl("z-end-field", TextField.class).getText();
            screen.findNiftyControl("z-end-field", TextField.class).setText("");

            if (!str.equals("")) {
                zEnd = Integer.parseInt(str);
            } else {
                zEnd = 0;
            }
            auftragCoord = new ArrayList<VisuVector3f>();
            VisuVector3f initial = new VisuVector3f(xIn, yIn, zIn);
            VisuVector3f end = new VisuVector3f(xEnd, yEnd, zEnd);

            auftragCoord.add(initial);
            auftragCoord.add(end);

            createAuftragComponent(initialWP, endWP);

            auftragCoord = null;

            cancelManualAuftrag();
            setLabel("");
        }


    }

    /**
     * Cancels a manual task. The text fields will be locked.
     */
    private void cancelManualAuftrag() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        Element e = screen.findElementByName("auftrag-data-panel");
        e.disable();
    }


    @Override
    public IVisuSelectable getObjectSelection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ClickSelection<VisuComponent> getMarkerSelection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VisuHazelcastAppState getHazelcastAppState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Application getVisuClient() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initExtraInputMapping() {
         this.inputManager = guiAppState.getInputManager();


        inputManager.addMapping(AUFTRAG_MENU_BUTTON, new KeyTrigger(KeyInput.KEY_F4));

        inputManager.addListener(keyListener, AUFTRAG_MENU_BUTTON);
    }

    @Override
    public void initUI() {
        
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");

        Element auftragPanel = screen.findElementByName("auftrag-panel");


        PanelBuilder pbauf = new PanelBuilder("auftrag-button-panel") {
            {
                childLayoutVertical();
                controller(AuftragPanelController.class.getName());
                width("33%");
                alignLeft();

            }
        };
        Element auftragButtonPanel = pbauf.build(nifty, screen, auftragPanel);
        AuftragPanelController auftragPanelController = auftragButtonPanel.getControl(AuftragPanelController.class);
        auftragPanelController.setBasicEditor(this);

        new TextBuilder("auftrag-label") {
            {
                text("");

                font("Interface/Fonts/Default.fnt");
            }
        }.build(nifty, screen, auftragButtonPanel);

        new ButtonBuilder("empty-auftrag-button", "Empty travel") {
            {

                height("20px");
                padding("10px");
                width("120px");
                alignLeft();
                interactOnClick("newEmptyAuftrag()");
            }
        }.build(nifty, screen, auftragButtonPanel);

        new ButtonBuilder("transport-auftrag-button", "Transport") {
            {

                height("20px");
                padding("10px");
                width("120px");
                alignLeft();
                interactOnClick("newTransportAuftrag()");
            }
        }.build(nifty, screen, auftragButtonPanel);
        new ButtonBuilder("manual-auftrag-button", "Manual") {
            {

                height("20px");
                padding("10px");
                width("120px");
                alignLeft();
                interactOnClick("newManualAuftrag()");
            }
        }.build(nifty, screen, auftragButtonPanel);

        PanelBuilder pba = new PanelBuilder("auftrag-data-panel") {
            {
                childLayoutHorizontal();
                alignRight();
                controller(AuftragPanelController.class.getName());
                width("66%");
            }
        };
        Element dataPanel = pba.build(nifty, screen, auftragPanel);
        AuftragPanelController auftragPanelController2 = dataPanel.getControl(AuftragPanelController.class);
        auftragPanelController2.setBasicEditor(this);


        PanelBuilder pb4 = new PanelBuilder("initial-panel") {
            {
                childLayoutVertical();
                alignCenter();
                controller(AuftragPanelController.class.getName());
                width("50%");

            }
        };
        Element initialPanel = pb4.build(nifty, screen, dataPanel);

        PanelBuilder pb5 = new PanelBuilder("end-panel") {
            {
                childLayoutVertical();
                alignLeft();
                controller(AuftragPanelController.class.getName());
                width("33%");

            }
        };
        Element endPanel = pb5.build(nifty, screen, dataPanel);

        new TextBuilder("initial-text") {
            {
                text("Initial point (mm):");

                font("Interface/Fonts/Default.fnt");
            }
        }.build(nifty, screen, initialPanel);
        new PanelBuilder("panel-wp-initial") {
            {
                childLayoutHorizontal();
                text(new TextBuilder("wp-initial-text") {
                    {
                        text("WP: ");
                        alignLeft();
                        font("Interface/Fonts/Default.fnt");

                    }
                });

                control(new TextFieldBuilder("wp-initial-field") {
                    {
                        width("60px");
                        alignRight();

                    }
                });
            }
        }.build(nifty, screen, initialPanel);

        new PanelBuilder("panel-x-initial") {
            {
                childLayoutHorizontal();
                text(new TextBuilder("x-initial-text") {
                    {
                        text("X: ");
                        alignLeft();
                        font("Interface/Fonts/Default.fnt");

                    }
                });

                control(new TextFieldBuilder("x-initial-field") {
                    {
                        width("60px");
                        alignRight();

                    }
                });
            }
        }.build(nifty, screen, initialPanel);
        new PanelBuilder("panel-y-initial") {
            {
                childLayoutHorizontal();
                text(new TextBuilder("y-initial-text") {
                    {
                        text("Y: ");
                        font("Interface/Fonts/Default.fnt");

                    }
                });

                control(new TextFieldBuilder("y-initial-field") {
                    {
                        width("60px");
                    }
                });
            }
        }.build(nifty, screen, initialPanel);
        new PanelBuilder("panel-z-initial") {
            {
                childLayoutHorizontal();
                text(new TextBuilder("z-initial-text") {
                    {
                        text("Z: ");
                        font("Interface/Fonts/Default.fnt");

                    }
                });

                control(new TextFieldBuilder("z-initial-field") {
                    {
                        width("60px");
                    }
                });
            }
        }.build(nifty, screen, initialPanel);


        ButtonBuilder bb2 = new ButtonBuilder("Ok-button", "Send") {
            {

                height("20px");
                alignLeft();
                interactOnClick("sendManualAuftrag()");
            }
        };
        bb2.build(nifty, screen, initialPanel);

        new TextBuilder("end-text") {
            {
                text("End point (mm):");

                font("Interface/Fonts/Default.fnt");
            }
        }.build(nifty, screen, endPanel);

        new PanelBuilder("panel-wp-end") {
            {
                childLayoutHorizontal();
                text(new TextBuilder("wp-end-text") {
                    {
                        text("WP: ");
                        font("Interface/Fonts/Default.fnt");

                    }
                });

                control(new TextFieldBuilder("wp-end-field") {
                    {
                        width("60px");
                    }
                });
            }
        }.build(nifty, screen, endPanel);

        new PanelBuilder("panel-x-end") {
            {
                childLayoutHorizontal();
                text(new TextBuilder("x-end-text") {
                    {
                        text("X: ");
                        font("Interface/Fonts/Default.fnt");

                    }
                });

                control(new TextFieldBuilder("x-end-field") {
                    {
                        width("60px");
                    }
                });
            }
        }.build(nifty, screen, endPanel);
        new PanelBuilder("panel-y-end") {
            {
                childLayoutHorizontal();
                text(new TextBuilder("y-end-text") {
                    {
                        text("Y: ");
                        font("Interface/Fonts/Default.fnt");

                    }
                });

                control(new TextFieldBuilder("y-end-field") {
                    {
                        width("60px");
                    }
                });
            }
        }.build(nifty, screen, endPanel);
        new PanelBuilder("panel-z-end") {
            {
                childLayoutHorizontal();
                text(new TextBuilder("z-end-text") {
                    {
                        text("Z: ");
                        font("Interface/Fonts/Default.fnt");

                    }
                });

                control(new TextFieldBuilder("z-end-field") {
                    {
                        width("60px");
                    }
                });
            }
        }.build(nifty, screen, endPanel);

        ButtonBuilder bb2a = new ButtonBuilder("Cancel-button", "Cancel") {
            {

                height("20px");
                alignLeft();
                interactOnClick("cancelManualAuftrag()");
            }
        };
        bb2a.build(nifty, screen, endPanel);




        auftragPanel.layoutElements();
    }

    @Override
    public void displayClientMessage(ArrayList<String> info) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateSelectedComponentsMap(ClickSelection<VisuComponent> selection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Controller of the task GUI.
     */
    public static class AuftragPanelController extends AbstractController {

        private AuftragEditor auftragEditor;

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

        public void newEmptyAuftrag() {
            auftragEditor.hazelcastAppState.threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    auftragEditor.newEmptyAuftrag();
                }
            });
        }

        public void newTransportAuftrag() {
            auftragEditor.hazelcastAppState.threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    auftragEditor.newTransportAuftrag();
                }
            });
        }

        public void newManualAuftrag() {
            auftragEditor.hazelcastAppState.threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    auftragEditor.newManualAuftrag();
                }
            });
        }

        public void sendManualAuftrag() {
            auftragEditor.hazelcastAppState.threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    auftragEditor.sendManualAuftrag();
                }
            });
        }

        public void cancelManualAuftrag() {
            auftragEditor.hazelcastAppState.threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    auftragEditor.cancelManualAuftrag();
                }
            });
        }

        /**
         * @return the basicEditor
         */
        public AuftragEditor getBasicEditor() {
            return auftragEditor;
        }

        /**
         * @param basicEditor the basicEditor to set
         */
        public void setBasicEditor(AuftragEditor basicEditor) {
            this.auftragEditor = basicEditor;
        }
    }

    private class KeyListener implements ActionListener {

        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }
            if (name.equals(AUFTRAG_MENU_BUTTON)) {
                openAuftragMenu();
            }
        }
    }
}
