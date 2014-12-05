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
package kodemat.visu.test;

import Interfaces.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kodemat.visu.VisuServer;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuVector3f;


import kodemat.visudata.visuComponents.VisuComponent;

import org.openide.util.Exceptions;

/**
 *
 * @author Amjad
 */

public class ClientPerformanceTest2 {
    
    public static String COMP_NAME= "testBox";

    int numberOfClients;
    int numberOfObjects;
    int repeatTimes;
    List<LayoutClientImpl> clients;
    List<VisuComponent> components;
    VisuServer server;
    private List<Float> yPositions;
    private List<Float> xPositions;
    private VisuHelper helper;
    public static long BEFORE_MILLI_SECS = -1;
    public ClientPerformanceTest2(int numberOfClients, int numberOfObjects, int repeatTimes) {
       
        this.numberOfClients = numberOfClients;
        this.numberOfObjects = numberOfObjects;
        this.repeatTimes = repeatTimes;

    }
    public void create(){
    
        this.clients = new ArrayList<LayoutClientImpl>();
        this.components = new ArrayList<VisuComponent>();

        // create the users
        for (int i = 0; i < this.numberOfClients; i++) {
            LayoutClientImpl client = new LayoutClientImpl();
            this.clients.add(client);
        }

        LayoutClientImpl clientToCreateBoxes = this.clients.get(0);
        for (int i = 0; i < this.numberOfObjects; i++) {
            VisuComponent box = clientToCreateBoxes.createModel(COMP_NAME, "CeMAT_Assets/Container/container.j3o" );
            this.components.add(box);
        }

        // pre-fill the data of coordinats y, each user will have one y value
        yPositions = new ArrayList<>();

        for (int i = 0; i < this.numberOfClients; i++) {
            yPositions.add(calculateYPosition(i));
        }

        // pre-fill the data of coordinats x, each user will have multiple x value

        xPositions = new ArrayList<>();

        for (int i = 0; i < this.repeatTimes; i++) {
            xPositions.add(calculateXPosition(i));
        }
    
    }

    public static void main(String[] args) {


        ClientPerformanceTest2 test = new ClientPerformanceTest2(1, 1, 1000);
        test.testScenario();



    }

    /**
     * Test of getHaselcastClient method, of class LayoutClientImpl.
     */
    public void testScenario() {
        String return_value;
        return_value = System.setProperty("java.net.preferIPv4Stack", "true");
        System.out.println("Prefer IPV4 stack was previously :" + return_value);
        return_value = System.getProperty("java.net.preferIPv4Stack");
        System.out.println("prefer IPV4 stack has been set to :" + return_value);
        Logger.getLogger("com.jme3").setLevel(Level.WARNING);
        Logger.getLogger("com.hazelcast").setLevel(Level.WARNING);
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.WARNING);
        Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE);

        
        TestVisuClient visuClient = new TestVisuClient();
        visuClient.setSettings(visuClient.getAndSetSettings());
        visuClient.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
        
       create();

        helper = new VisuHelper(visuClient.getHazelcastAppState().getClient(), false, true);
        VisuComponent component = helper.getComponent(COMP_NAME);
        for (int i = 0; i < this.repeatTimes; i++) {
            BEFORE_MILLI_SECS = System.currentTimeMillis();
            component.setTranslation(new VisuVector3f(xPositions.get(i), 0.0f, 15.0f), true);
            addDelay(1000);
        }

    }

    private float calculateYPosition(int index) {
        float yCordinate = 2 * index - 10;
        return yCordinate;
    }

    private float calculateXPosition(int repeatTime) {
        float xCordinate = (repeatTime + 1) * 0.05f;
        return xCordinate;
    }
      public void addDelay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}