/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.touch.visu.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import java.util.List;
import java.util.concurrent.Executors;
import kodemat.visu.ClientStateChangeInterface;

/**
 *
 * @author moro
 */
public class NiftyTouchGuiAppState extends AbstractAppState implements ScreenController {

    public static final String HELP_SCREEN_TOGGLE_BUTTON = "NiftyGuiAppState_HELP_TOGGLE_BUTTON";
    public static final String TOOLBAR_TOGGLE_BUTTON = "NiftyGuiAppState_TOOLBAR_TOGGLE_BUTTON";
    public static final String CONNECTION_TOGGLE_BUTTON = "NiftyGuiAppState_CONNECTION_TOGGLE_BUTTON";
    private Nifty nifty;
    private Screen screen;
    private Element textArea;
    private Application app;
    private KeyListener keyListener = new KeyListener();
    private InputManager inputManager;
    AppStateManager stateManager;
    HudController hudController = new HudController();
    private boolean atStart;

    public NiftyTouchGuiAppState(Nifty nifty, boolean atStart) {
        this.nifty = nifty;
        this.atStart = atStart;
    }

    public NiftyTouchGuiAppState(Nifty nifty) {
        this.nifty = nifty;
        this.atStart = true;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        this.stateManager = stateManager;

        this.app = app;
        this.inputManager = app.getInputManager();

        if (app.getInputManager() != null) {

            inputManager.addMapping(HELP_SCREEN_TOGGLE_BUTTON, new KeyTrigger(KeyInput.KEY_F1));
            inputManager.addMapping(TOOLBAR_TOGGLE_BUTTON, new KeyTrigger(KeyInput.KEY_F3));
            inputManager.addMapping(CONNECTION_TOGGLE_BUTTON, new KeyTrigger(KeyInput.KEY_F2));

            inputManager.addListener(keyListener, HELP_SCREEN_TOGGLE_BUTTON);
            inputManager.addListener(keyListener, TOOLBAR_TOGGLE_BUTTON);
            inputManager.addListener(keyListener, CONNECTION_TOGGLE_BUTTON);
        }



//        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(),
//                app.getInputManager(),
//                app.getAudioRenderer(),
//                app.getGuiViewPort());
//        nifty = niftyDisplay.getNifty();
//        nifty.setDebugOptionPanelColors(true);   
        hudController.changeLocalClientState(null, null);
        if (atStart) {
            getNifty().fromXml("Interface/Nifty/AllScreens_Touch.xml", "hidden", this);
            getNifty().gotoScreen("hidden");
        }

        // attach the nifty display to the gui view port as a processor
        //app.getGuiViewPort().addProcessor(niftyDisplay);
        //NiftyConnect nc = new NiftyConnect(nifty, vckmas);



    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        this.textArea = screen.findElementByName("textarea");
    }

    public void setText(String str) {
        if (textArea != null && screen != null && screen.getScreenId().equals("help")) {
            textArea.getRenderer(TextRenderer.class).setText(str);
            textArea.layoutElements();
            textArea.setConstraintWidth(new SizeValue(textArea.getRenderer(TextRenderer.class).getTextWidth() + "px"));
            screen.layoutLayers();
        }
    }

    public void onStartScreen() {
        System.out.println("onStartScreen");
//        screen.layoutLayers();
    }

    public void onEndScreen() {
        System.out.println("onEndScreen");
    }

    public void quit() {
        getNifty().gotoScreen("hidden");
    }

    public HudController getHudController() {
        return hudController;
    }
    private int counter = 0;

    @Override
    public void cleanup() {
        super.cleanup();

        if (inputManager.hasMapping(HELP_SCREEN_TOGGLE_BUTTON)) {
            inputManager.deleteMapping(HELP_SCREEN_TOGGLE_BUTTON);
        }

        inputManager.removeListener(keyListener);
    }

    /**
     * @return the nifty
     */
    public Nifty getNifty() {
        return nifty;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    private class KeyListener implements ActionListener {

        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }

            if (name.equals(HELP_SCREEN_TOGGLE_BUTTON)) {
                if (getNifty().getCurrentScreen().getScreenId().equals("hidden")) {
                    getNifty().gotoScreen("help");
                } else {
                    getNifty().gotoScreen("hidden");
                }
            }
            if (name.equals(TOOLBAR_TOGGLE_BUTTON)) {
                if (getNifty().getCurrentScreen().getScreenId().equals("hidden")) {
                    if (getNifty().getCurrentScreen().getScreenId().equals("hidden")) {
                        Element e = getNifty().getCurrentScreen().findElementByName("toolbar-panel");
                        if (e.isVisible()) {
                            e.setFocusable(false);
                            e.setVisible(false);
                        } else {
                            e.setFocusable(true);
                            e.setVisible(true);
                        }
                    }
                }
            }
            if (name.equals(CONNECTION_TOGGLE_BUTTON)) {
                if (getNifty().getCurrentScreen().getScreenId().equals("hidden")) {
                    if (getNifty().getCurrentScreen().getScreenId().equals("hidden")) {
                        Element e = getNifty().getCurrentScreen().findElementByName("connection-panel");
                        Element e2 = getNifty().getCurrentScreen().findElementByName("connection-panel-background");
                        if (e.isVisible()) {
                            e.setVisible(false);
                            e2.setVisible(false);
                        } else {
                            e.setVisible(true);
                            e2.setVisible(true);
                        }
                    }
                }
            }
        }
    }

    public class HudController implements ClientStateChangeInterface {

        @Override
        public void changeLocalClientState(final String status, final List<String> InetAddrCollection) {
            app.enqueue(Executors.callable(new Runnable() {
                @Override
                public void run() {
                    if (getNifty() == null) {
                        return;
                    }
                    String msg;
                    if (status == null) {
                        msg = "Client Status = No Connection : Press G to Connect to Server";
                    } else {
                        msg = "Client Status = " + status + " with Server at " + InetAddrCollection;
                    }
                    if ("hidden".equals(getNifty().getCurrentScreen().getScreenId())) {
                        Element e = getNifty().getCurrentScreen().findElementByName("client-server-status");
                        if (e != null) {
                            e.getRenderer(TextRenderer.class).setText(msg);
//                            e.layoutElements();
                            e.setConstraintWidth(new SizeValue(e.getRenderer(TextRenderer.class).getTextWidth() + ""));
                            Element hud = getNifty().getCurrentScreen().findElementByName("connection-panel-background");
                            hud.setConstraintWidth(new SizeValue(e.getConstraintWidth().getValueAsInt(100f) + 10 + "px"));
                            hud.setConstraintHeight(e.getParent().getConstraintHeight());
                            e.getParent().getParent().layoutElements();
                            hud.getParent().layoutElements();
//                            screen.layoutLayers();

                        }
                    }
                }
            }));

        }

        @Override
        public void changeRemoteState(final String client, final String add, final String behavior) {
            app.enqueue(Executors.callable(new Runnable() {
                @Override
                public void run() {
                    if (getNifty() == null) {
                        return;
                    }
                    String msg = "";
                    String[] splittedMessage = add.split(",");                  
                    String ipaddress = splittedMessage[splittedMessage.length-1];
                    String name = splittedMessage[1];
                    if (behavior.equals("connect")) {
                        msg = ("Client " + name + " connected from " + ipaddress);
                    } else if (behavior.equals("disconnect")) {
                        msg = ("Client " + name + " from " + ipaddress + " disconnected ");
                    }
                    if ("hidden".equals(getNifty().getCurrentScreen().getScreenId())) {
                        Element e = getNifty().getCurrentScreen().findElementByName("client-events");
                        if (e != null) {
                            e.getRenderer(TextRenderer.class).setText(msg);
//                            e.layoutElements();
                            e.setConstraintWidth(new SizeValue(e.getRenderer(TextRenderer.class).getTextWidth() + ""));
//                            e.getParent().layoutElements();
                            getNifty().getCurrentScreen().layoutLayers();
                            //screen.layoutLayers();
                        }
                    }
                }
            }));

        }

        @Override
        public void setNumberOfClients(final int numberofclients) {
            app.enqueue(Executors.callable(new Runnable() {
                @Override
                public void run() {
                    if (getNifty() == null) {
                        return;
                    }
                    String msg = "Total Connected Clients =  " + numberofclients;
                    if ("hidden".equals(getNifty().getCurrentScreen().getScreenId())) {
                        Element e = getNifty().getCurrentScreen().findElementByName("client-number");
                        if (e != null) {
                            e.getRenderer(TextRenderer.class).setText(msg);
//                            e.layoutElements();
                            e.setConstraintWidth(new SizeValue(e.getRenderer(TextRenderer.class).getTextWidth() + ""));
//                            if (screen != null) {
                            e.getParent().layoutElements();
//                                getNifty().getCurrentScreen().layoutLayers();
//                            }
                        }
                    }
                }
            }));

        }

        @Override
        public void setCommandQueueInfo(final int size) {
            app.enqueue(Executors.callable(new Runnable() {
                @Override
                public void run() {
                    if (getNifty() == null) {
                        return;
                    }
                    String msg = "Press F1 for help  / Commands on Server =  " + size;
                    if ("hidden".equals(getNifty().getCurrentScreen().getScreenId())) {
                        Element e = getNifty().getCurrentScreen().findElementByName("client-help-and-command-queue-info");
                        if (e != null) {
                            e.getRenderer(TextRenderer.class).setText(msg);
//                            e.layoutElements();
                            e.setConstraintWidth(new SizeValue(e.getRenderer(TextRenderer.class).getTextWidth() + ""));
//                            if (screen != null) {
                            e.getParent().layoutElements();
//                                getNifty().getCurrentScreen().layoutLayers();
//                            }
                        }
                    }
                }
            }));
        }
    }
}
