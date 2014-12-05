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
package Interfaces;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import java.awt.Frame;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JOptionPane;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;


/**
 *
 * @author Kipouridis
 */
public class LayoutClientImpl implements ILayoutClient {

    public HazelcastInstance client;
    public VisuHelper helper;

    public LayoutClientImpl(boolean isServer, boolean isClient) {
        this.getHaselcastClient();
        this.createHelper(isServer,isClient);
    }
    
    //Default constractor refers to a isServer=true, isClient=false
    public LayoutClientImpl() {
        this.getHaselcastClient();
        this.createHelper(true,false);
    }
    
    public LayoutClientImpl(VisuHelper helper){
        this.helper = helper;
    }

    @Override
    public HazelcastInstance getHaselcastClient() {

        Properties globalProps = new Properties();
        try {
            globalProps.load(new FileReader("./prop.properties"));
        } catch (FileNotFoundException ex) {
      
        } catch (IOException ex) {
     
        }
        String serverAddress = globalProps.getProperty("hazelcast.client.addresses");
        if (serverAddress == null || serverAddress.equals("")) {
            throw new IllegalStateException("Could not find server adress");
        } else {
            System.out.println("CONNECTING TO SERVER " + serverAddress);
        }

        ClientConfig clientConfig = new ClientConfig();

        clientConfig.addAddress(serverAddress);

//        if(name != null )
//        clientConfig.getGroupConfig().setName(name);
//          if(passwd!=null )
//        clientConfig.getGroupConfig().setPassword(passwd);
//            if(connTimeout != null )
//        clientConfig.setConnectionTimeout(connTimeout.intValue());


        clientConfig.setSmartRouting(true);
        clientConfig.setRedoOperation(true);
        client = HazelcastClient.newHazelcastClient(clientConfig);
//        client = Hazelcast.newHazelcastInstance(this.fileBuild(propertiesFile));

        return client;
    }


    public VisuHelper getHelper() {
        if(this.helper ==null){
        helper = createHelper(true,false);
        }
       
        return (helper);
    }
    public VisuHelper createHelper(boolean isServer, boolean isClient) {
        helper = new VisuHelper(this.client,isServer,isClient);
        return (helper);
    }

    public void addImageAsFloor(String assetPath) {
        VisuComponent floor = helper.getComponent("Floor");
        if (floor == null) {
            floor = helper.createComponent("Floor");

            floor.setType(new VisuType(VisuType.TEXTURE, assetPath));
            floor.setIsSelectable(false); //not selectable by clicking
            System.out.println("created floor");

        } else {
            //JOptionPane.showMessageDialog(new Frame(), "There is already an image defined as floor " + floor.getName());
        }

    }

    @Override
    public VisuComponent createNode(String nodeName) {
        VisuComponent node = helper.getComponent(nodeName);

        if (node == null) {
            node = helper.createComponent(nodeName);

            node.setType(new VisuType(VisuType.NODE, null));

            System.out.println("created node with name " + nodeName + "and id " + node.getId());

        } else {
            //JOptionPane.showMessageDialog(new Frame(), "There is already an node with that name " + node.getName());
        }

        return node;
    }
        public VisuComponent createNode(String nodeName,boolean selectable) {
        VisuComponent node = helper.getComponent(nodeName);

        if (node == null) {
            node = helper.createComponent(nodeName);
 node.setIsSelectable(selectable);
            node.setType(new VisuType(VisuType.NODE, null));

            System.out.println("created node with name " + nodeName + "and id " + node.getId());

        } else {
            //JOptionPane.showMessageDialog(new Frame(), "There is already an node with that name " + node.getName());
        }

        return node;
    }

    @Override
    public VisuComponent createModel(String modelName, String modelAssetPath) {
        
        VisuComponent childNode = helper.getComponent(modelName);
        if (childNode == null) {
            childNode = helper.createComponent(modelName);
            childNode.setType(new VisuType(VisuType.MODEL, modelAssetPath));
//            System.out.println("created MODEL (not node) with name " + modelName + "type "+childNode.getType().type+ "and id " + childNode.getId());

        } else {
            //JOptionPane.showMessageDialog(new Frame(), "There is already an model with that name " + childNode.getName());
        }

        return childNode;
    }
    public VisuComponent createModel(String modelName, String modelAssetPath, boolean selectable) {
        VisuComponent childNode = helper.getComponent(modelName);
        if (childNode == null) {
            childNode = helper.createComponent(modelName);
            childNode.setIsSelectable(selectable);
            childNode.setType(new VisuType(VisuType.MODEL, modelAssetPath));
//            System.out.println("created MODEL (not node) with name " + modelName + "type "+childNode.getType().type+ "and id " + childNode.getId());

        } else {
            //JOptionPane.showMessageDialog(new Frame(), "There is already an model with that name " + childNode.getName());
        }

        return childNode;
    }

    public VisuComponent createBox(String boxName, String modelAssetPath) {
        VisuComponent childNode = helper.getComponent(boxName);
        if (childNode == null) {
            childNode = helper.createComponent(boxName);
            childNode.setType(new VisuType(VisuType.BOX, null));
            System.out.println("created MODEL (not node) with name " + boxName + "and id " + childNode.getId());

        } else {
            //This message dialog is interrupting the import process, it shouldn't be displaced
           // JOptionPane.showMessageDialog(new Frame(), "There is already an model with that name " + childNode.getName());
        }

        return childNode;
    }

    @Override
    public void addChildNode(long childId, long parentId) {

        VisuComponent childNode = helper.getComponent(childId);
        if (childNode != null) {

            childNode.setParent(parentId);
        } else {
            JOptionPane.showMessageDialog(new Frame(), "Node with id " + childId + " cannot be found ");
        }
    }

 
    /*
     * Main method that includes the 
     */
    public void execute() {
    }

    ;

    @Override
    public Config getClientConfig() {
        return (new Config());

    }

    @Override
    public Config fileBuild(String FileName) {
        Config cfg_test = this.getClientConfig();
        try {
            cfg_test = new XmlConfigBuilder(FileName).build();
        } catch (Exception e) {
            System.err.print("Error: " + e.getMessage());
        }
        return cfg_test;

    }

    @Override
    public void shutdown() {
        client.shutdown();
    }

    @Override
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }

    @Override
    public void addConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setNodeTranslation(VisuComponent node, VisuVector3f nodePosition) {
        node.setTranslation(nodePosition);
    }

    @Override
    public void setNodeScale(VisuComponent node, VisuVector3f nodeScale) {
        node.setScale(nodeScale);
    }

    @Override
    public void setNodeRotation(VisuComponent node, VisuRotation nodeRotation) {
        node.setRotation(nodeRotation);
    }

    @Override
    public HazelcastInstance getClientInst() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
