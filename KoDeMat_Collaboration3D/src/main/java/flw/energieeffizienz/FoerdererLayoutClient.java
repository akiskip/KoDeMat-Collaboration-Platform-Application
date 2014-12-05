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
package flw.energieeffizienz;

import jade_layout.*;
import Interfaces.LayoutClientImpl;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.MapEvent;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import com.jme3.network.Server;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuVector3f;

/**
 * Class that makes possible the 3D visualization of tasks, and responsible for
 * obtaining the data of visually created tasks.
 *
 * @author Koshkabb
 */
public class FoerdererLayoutClient extends LayoutClientImpl implements IFacilityCommunicator {

    /**
     * Hashmap that links an model name with its parent node.
     */
    HashMap<String, String> parents;
    /**
     * Hashmap that indicates the parent node of an electric jack (katze)
     * depending on the waypoint.
     */
    HashMap<String, String> wpParents;
    /**
     * ArrayList which holds the name of all the implemented agents.
     */
    ArrayList<String> implementedNames;
    /**
     * Instance of VisuCoordinateTranslation, which performs the translation
     * [0,1] <--> 3D coordinates.
     */
    private VisuCoordinateTranslator vct;
    int boxId;
    private boolean isTransport;

    VisuVector3f finalCoord;
    private ITopic<String> flwAnlageTopic;
    public static String FINALIZED_STRING = "finalized";
    public static String FINALIZED_STRING_VALUE = "true";
    private IMap<Integer, VisuComponentInfoEntry> eeaTelegramsMap;

    /**
     * In the constructor all the Hashmaps are filled in.
     */
    public FoerdererLayoutClient() {
        //starts the hazelcast client based on the prop.properties file
        super();
        vct = new VisuCoordinateTranslator();
        isTransport = false;

        initialize();
    }

    /**
     * The scenario is initialized (the floor is created and the electric
     * monorail system is placed).
     */
    private void initialize() {
//        super.addImageAsFloor("Textures/fml_versuchshalle_1.jpg");
        
        //create (or get) a topic for the messages 
      flwAnlageTopic = client.getTopic("kodemat-energie-effizienz\"");


        //Create the 3D Anlage model 
        VisuComponent ehbNode = this.createNode("ehbNode");

        VisuComponent ehbModel = this.createModel("ehbModel", "Models/ehb_jade/ehb.j3o");
        this.addChildNode(ehbModel.getId(), ehbNode.getId());

        
        //init a map that will hold telegramField objects
        eeaTelegramsMap =  client.getMap("EEATelegramsMap");
        this.initializeOrdersMap();
        
    }

    /**
     * Creates a component in the Hazelcast structures, and this in the 3D
     * world.
     *
     * @param modelName The name of the component
     * @param translation The position of the component [0,1] ranged
     * @param wp The waypoint of the component
     */

    public void createComponent(String modelName, float translation, String wp) {

    }

    /**
     * Moves a component in the 3D world. If such component does not exist, it
     * is created.
     *
     * @param modelName The name of the component
     * @param translation The position of the component [0,1] ranged
     * @param wp The waypoint of the component
     */
 
    public void moveContainer(String modelName, float coordinate, String wp) {
        VisuComponent model = this.getHelper().getComponent(modelName + "_Node");

     
    }

 

    /**
     * Erases the boxes that were created when the task was created. If the task
     * is a transport task, the initial box will remain. It also erases the Task
     * component and its children.
     *
     * @param transport The type of task (0 for empty travel, 1 for transport, 2
     * for manual).
     */
    private void prepareForAuftrag(int transport) {
      
    }

        public void initializeOrdersMap(){

        eeaTelegramsMap.put(0, new VisuComponentInfoEntry(0,"id","0") );
        eeaTelegramsMap.put(1, new VisuComponentInfoEntry(0,"startGW","0") );
        eeaTelegramsMap.put(2, new VisuComponentInfoEntry(0,"endGW","0") );
        eeaTelegramsMap.put(3, new VisuComponentInfoEntry(0,"module","0") );
        eeaTelegramsMap.put(4, new VisuComponentInfoEntry(0,"finalised","false") );

        
    }
           

    public static void main(String[] args) {
        FoerdererLayoutClient tc = new FoerdererLayoutClient();


        tc.shutdown();
    }

    @Override
    public void startCommunicationService() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stopCommunicationService() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void startReceiveMessage(String msg) {

        flwAnlageTopic.publish("test from Orthodoxos Kipouridis. Moritz Roidl");
        flwAnlageTopic.addMessageListener(new MessageListener<String>() {

            @Override
            public void onMessage(Message<String> msg) {
                Logger.getLogger(Server.class.getName()).log(Level.INFO, "{0} {1}: {2}", new Object[]{formatTime(msg.getPublishTime()), msg.getPublishingMember().getUuid(), msg.getMessageObject()});
           
            //do something
            }
        });
        
    
    }
    
  

    @Override
    public void sendMessage(String msg) {

    flwAnlageTopic.publish(msg);
    }
    
    
      public static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss.SSS");
        public static String formatTime(long millis){
        return time.format(new Date(millis));
    }
    
             private void handleFinalizedOrder() {
                 
       Iterator it = eeaTelegramsMap.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pairs = (Map.Entry)it.next();
    String attributeName =   ((VisuComponentInfoEntry)pairs.getValue()).getName();
    String attributeValue =   ((VisuComponentInfoEntry)pairs.getValue()).getName();
    
    //send a message through the topic based on the values above
    this.sendMessage("commando");
        it.remove(); // avoids a ConcurrentModificationException
    }
                 
        }
        
    public class OrdersEntryListener implements EntryListener<Integer, VisuComponentInfoEntry> {

        @Override
        public void entryAdded(EntryEvent<Integer, VisuComponentInfoEntry> ee) {
            if (ee.getValue().getName().equals(FINALIZED_STRING)) {

                if (ee.getValue().getValue().equals(FINALIZED_STRING_VALUE)) {
                    // create order out of it and then xml and send
                    System.out.println("OrderMap is finalized");
                    handleFinalizedOrder();
                }
            }
        }

        @Override
        public void entryRemoved(EntryEvent<Integer, VisuComponentInfoEntry> ee) {
        }

        @Override
        public void entryUpdated(EntryEvent<Integer, VisuComponentInfoEntry> ee) {
            //Check if glazedlists get only Integers as keys

            if (ee.getValue().getName().equals(FINALIZED_STRING)) {

                if (ee.getValue().getValue().equals(FINALIZED_STRING_VALUE)) {

                    // create order out of it and then xml and send
                    System.out.println("OrderMap is finalized");
                    handleFinalizedOrder();
                    eeaTelegramsMap.put(ee.getKey(), new VisuComponentInfoEntry(ee.getKey(),FINALIZED_STRING,"false"));
             
                    
                }
            }


        }

        @Override
        public void entryEvicted(EntryEvent<Integer, VisuComponentInfoEntry> ee) {
        }

        @Override
        public void mapEvicted(MapEvent me) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mapCleared(MapEvent me) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

   
    }
        
        
}
