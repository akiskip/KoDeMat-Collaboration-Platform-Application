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
package kodemat.visu;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.*;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;
import kodemat.admin.VisuKodematUser;
import static kodemat.visu.ScenegraphEventAssembler.*;
import kodemat.visudata.VisuChange;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuInterpolation;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.command.VisuChangeCommand;
import kodemat.visudata.command.VisuChangeToWorldPosition;
import kodemat.visudata.command.VisuCommand;
import kodemat.visudata.visuComponents.IComponent;

/**
 *
 * @author mari
 */
public class VisuServer {

    VisuHelper helper;
    IQueue<VisuCommand> commands;
    CommandListener commandListener = new CommandListener();
    HazelcastInstance hz;
//   private TransactionContext transactionalContext;

    public static void main(String args[]) {
        VisuServer server = new VisuServer();
        server.server_Start();

    }
    
    public void server_stop(){
        if(hz != null){
            hz.shutdown();
        }
    }

    public void server_Start() {
        Config cfg = xmlFileBuild("./hazelcastServer.xml");
        hz = Hazelcast.newHazelcastInstance(cfg);

        //start a transactionalContext 
//        transactionalContext = hz.newTransactionContext();

        //IMap<Integer,Vector3f> ClientStatus = hz.getMap("ClientStatus");

//        usefull for usermanager
        final IMap<String, VisuKodematUser> users = hz.getMap("users");
        commands = hz.getQueue("kodemat.visu.commands");

        commands.addItemListener(commandListener, true);
        helper = new VisuHelper(hz, true, false);

        //ClientService clientService = hz.getClientService();
        //ClientListener connect = null;
        //clientService.addClientListener(connect);
        
        IMap<String, Long> ids = hz.getMap("kodemat.visu.ids");
        ids.addEntryListener(new IdListener(), true);

        //Implement a listener for getting the client status changes eg connection
//        of new clients or disconnection of existing

        //Clientlistener extends eventlistener
        ClientListener CL;
        CL = new ClientListener() {

            @Override
            public void clientConnected(Client client) {
                SocketAddress socketAddress = client.getSocketAddress();
                
                System.out.println(" Current thread is  " + Thread.currentThread().getName());
                String clientstring = client.getUuid();

//                VisuKodematUser newUser = (VisuKodematUser) users.get(clientstring);

//            System.out.println("New Client "+newUser.getLast_name()+" Connected at" + socketAddress);

            }

            @Override
            public void clientDisconnected(Client client) {
              
                SocketAddress socketAddress = client.getSocketAddress();
                String clientUserName = helper.getClientUsername(client.getUuid());
                if(clientUserName != null)
                {
                    users.remove(clientUserName);            //free the selected object that was lastly beeing edited by the user who left
                if(helper.getSelectedComponents().get(clientUserName) != null )
                    for(long compId:helper.getSelectedComponents().get(clientUserName) ){
               removeBoundingBoxFromComponent(compId);
                        
                    }
                }
                
                System.out.println("commands size: " + commands.size());
                System.out.println("Client left " + socketAddress);
    
            }
        };
        hz.getClientService().addClientListener(CL);
    }

    public Config xmlFileBuild(String FileName) {

        Config cfg_test = new Config();
        try {
//            cfg_test = new ClasspathXmlConfig(FileName);
            cfg_test = new XmlConfigBuilder(FileName).build();
        } catch (Exception e) {
            System.err.print("Error: " + e.getMessage());

        }
        return cfg_test;
    }

    class CommandListener implements ItemListener<VisuCommand> {

        @Override
        public void itemAdded(ItemEvent<VisuCommand> ie) {
            if (ie.getItem() instanceof VisuChangeToWorldPosition) {
                long id = ((VisuChangeToWorldPosition)ie.getItem()).getId();
                VisuComponent comp = helper.getComponent(id);
                Node n = null;
                if (comp != null) {
                    n = buildSpatialTree(comp, new HashSet<Long>());
                }
                if (n != null) {
                    comp.setInterpolation(new VisuInterpolation(false, false, false));
                    comp.setParent(null);
                    comp.setTranslation(new VisuVector3f(n.getWorldTranslation().x, n.getWorldTranslation().y, n.getWorldTranslation().z));
                    Quaternion q = n.getWorldRotation();
                    float[] angles = q.toAngles(null);
                    angles[0] = FastMath.RAD_TO_DEG * angles[0];
                    angles[1] = FastMath.RAD_TO_DEG * angles[1];
                    angles[2] = FastMath.RAD_TO_DEG * angles[2];
                    comp.setRotation(new VisuRotation(angles[0], angles[1], angles[2]));
                    comp.setScale(new VisuVector3f(n.getWorldScale().x, n.getWorldScale().y, n.getWorldScale().z));
                    comp.setInterpolation(new VisuInterpolation(true, true, true));
                }
            } 
            else if (ie.getItem() instanceof VisuChangeCommand) {
                VisuChangeCommand vcc = (VisuChangeCommand) ie.getItem();
                VisuChange change = vcc.getChange();
                helper.applyChange(change);
            }
        }

        @Override
        public void itemRemoved(ItemEvent<VisuCommand> ie) {
        }
    }

         void removeBoundingBoxFromComponent(long selectedId) {
      VisuComponent previousSelected = helper.getComponent(selectedId);
            if (previousSelected != null) {
                
                previousSelected.setLabel("");
            }
        

    }
    
    
    public Node buildSpatialTree(VisuComponent comp, Set<Long> visitedIds) {
        visitedIds.add(comp.getId());
        Node n = new Node(comp.getId() + "");
        if (comp.getTranslation() != null) {
            n.setLocalTranslation(convert(comp.getTranslation()));
        }
        if (comp.getRotation() != null) {
            n.setLocalRotation(convert(comp.getRotation()));
        }
        if (comp.getScale() != null) {
            n.setLocalScale(convert(comp.getScale()));
        }
        if (comp.getParent() != null && !visitedIds.contains(comp.getParent())) {
            VisuComponent parent = helper.getComponent(comp.getParent());
            if (parent != null) {
                Node parentNode = buildSpatialTree(parent, visitedIds);
                if (parentNode != null) {
                    parentNode.attachChild(n);
                    parentNode.updateGeometricState();
                }
            }
        }

        return n;
    }
    
    class IdListener implements EntryListener<String, Long> {

        @Override
        public void entryAdded(EntryEvent<String, Long> ee) {
            System.out.println("IDLISTENER entry added " + ee.getValue());
        }

        @Override
        public void entryRemoved(EntryEvent<String, Long> ee) {
        }

        @Override
        public void entryUpdated(EntryEvent<String, Long> ee) {
        }

        @Override
        public void entryEvicted(EntryEvent<String, Long> ee) {
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
