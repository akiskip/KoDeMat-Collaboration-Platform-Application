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
package kodemat.visu.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.Properties;
import kodemat.admin.VisuKodematUser;
import kodemat.visu.IKoDeMatClient;
import kodemat.visu.VisuClient;
import kodemat.visu.VisuClientConnectionInfo;

/**
 *
 * @author Koshkabb
 */
public class NiftyConnectAppState extends AbstractAppState implements ScreenController {

    public static final String CANCEL_BUTTON = "NiftyConnectAppState_CANCEL_BUTTON";
    private Nifty nifty;
    private Screen screen;
    private InputManager inputManager;
    private NiftyConnectAppState.KeyListener keyListener = new NiftyConnectAppState.KeyListener();

    private Properties globalProperties;
    private IKoDeMatClient visuClient;

    public NiftyConnectAppState() {
    }

    public NiftyConnectAppState(Nifty nifty, VisuClientKeyMappingAppState vckmas, Properties globalProps, IKoDeMatClient visuClient) {
        
        this.nifty = nifty;
        this.globalProperties = globalProps;
        this.visuClient = visuClient;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

//        nifty.setDebugOptionPanelColors(true);
        this.inputManager = app.getInputManager();

        nifty.fromXml("Interface/Nifty/AllScreens.xml", "connection-screen", this);
        nifty.registerScreenController(this);
        screen = nifty.getScreen("connection-screen");
        screen.findElementByName("panel-user-data").setVisible(true);
        screen.findElementByName("panel-user-data").setFocusable(true);
        nifty.gotoScreen("connection-screen");
        screen.layoutLayers();

        if (app.getInputManager() != null) {

            inputManager.addMapping(CANCEL_BUTTON, new KeyTrigger(KeyInput.KEY_F5));

            inputManager.addListener(keyListener, CANCEL_BUTTON);
        }
        try{
        screen.findNiftyControl("username_text", TextField.class).setText(globalProperties.getProperty("kodemat.user.lastName"));
        screen.findNiftyControl("password_text", TextField.class).setText(globalProperties.getProperty("hazelcast.client.group.pass"));
        screen.findNiftyControl("server_addr_text", TextField.class).setText(globalProperties.getProperty("hazelcast.client.addresses"));
        screen.findNiftyControl("company_text", TextField.class).setText(globalProperties.getProperty("kodemat.user.company"));
        screen.findNiftyControl("position_text", TextField.class).setText(globalProperties.getProperty("kodemat.user.position"));
        }
        catch(Exception ex){
            System.out.println("could not find the gloabal prop.properties file");
             screen.findNiftyControl("username_text", TextField.class).setText("dev");
        screen.findNiftyControl("password_text", TextField.class).setText("dev-pass");
        screen.findNiftyControl("server_addr_text", TextField.class).setText("129.187.174.164:5802");
        screen.findNiftyControl("company_text", TextField.class).setText("TUM");
        screen.findNiftyControl("position_text", TextField.class).setText("Wiss. Mitarbeiter");
        }
    }

    public void connect() {

//        screen.findElementByName("panel-user-data").setVisible(false);
//        screen.findElementByName("panel-user-data").setFocusable(false);
//        String username = screen.findNiftyControl("username_text", TextField.class).getText();
        String username = globalProperties.getProperty("hazelcast.client.group.name");
        String lastName = screen.findNiftyControl("username_text", TextField.class).getText();
        String password = screen.findNiftyControl("password_text", TextField.class).getText();
        if(password.equals("")){
            password = null;
        }
        String serverAddr = screen.findNiftyControl("server_addr_text", TextField.class).getText();
        String company = screen.findNiftyControl("company_text", TextField.class).getText();
        String position = screen.findNiftyControl("position_text", TextField.class).getText();

        nifty.gotoScreen("hidden");
        screen = nifty.getScreen("hidden");
        screen.findElementByName("connection-panel-background").setVisible(true);
        screen.findElementByName("connection-panel-background").setFocusable(true);


        //caution the user name ("dev") is used for connecting to the hazelcast while the last name is used for creating
        //the kodemauser which will be displayed
        VisuClientConnectionInfo vcci = new VisuClientConnectionInfo(serverAddr, username, password, null);
        VisuKodematUser vku = new VisuKodematUser(lastName, company, position, serverAddr);
        
        //TODO: Show pop up dialog if credentials do not match
        visuClient.createConnectionInfo(vcci, vku);
        visuClient.clientConnect();

    }
    
   
    

    @Override
    public void bind(Nifty nifty, Screen screen) {
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    private class KeyListener implements ActionListener {

        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }

            if (name.equals(CANCEL_BUTTON)) {
                visuClient.createConnectionInfo(null, null);
              visuClient.clientConnect();
                nifty.gotoScreen("hidden");
            }

        }
    }
}
