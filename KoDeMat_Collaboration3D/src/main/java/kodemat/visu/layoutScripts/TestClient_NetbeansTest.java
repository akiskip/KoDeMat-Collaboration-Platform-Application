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
package kodemat.visu.layoutScripts;

import Interfaces.LayoutClientImpl;
import com.hazelcast.core.IMap;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuInterpolation;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuVector3f;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class TestClient_NetbeansTest extends LayoutClientImpl {

    /*
     * Haselcast Client
     */
    private VisuComponent floor;
    private VisuComponent staplerNode;
    private VisuComponent stapler;
    private VisuComponent box;
    private VisuComponent ComponentsList;
    private List<VisuComponent> ComponentsList1;
    private VisuComponent staplerModel;

VisuComponent kurve;
VisuComponent gurtetrei;
VisuComponent gurtetrei1;
    private VisuComponent foerderNode1;
    private VisuComponent foerderNode2;
    private VisuComponent regalNode;
    private VisuComponent regal;
  
    
    public TestClient_NetbeansTest() {
        //conf file for haselcast

        super();

    }

    /**
     * 2
     *
     * @param count
     */
    public void startTest(int count) {


        staplerNode = this.createNode("StaplerNode 3");
        setNodeTranslation(staplerNode, new VisuVector3f(2, 0f, 0f));

//        box = this.createBox();
        this.addChildNode(box.getId(), staplerNode.getId());

//        this.setNodeTranslation(staplerModel, new VisuVector3f(0f, 0f, -.1f));
//        this.setNodeRotation(staplerModel, new VisuRotation(0, -90, 0));
//        this.setNodeScale(staplerModel, new VisuVector3f(.5f, .5f, .5f));


   
        

//                   boxNode = this.createNode("BoxNode");
//                    setNodeTranslation(staplerNode, new VisuVector3f(10, 0f, 0f));

        //            boxModel = this.createModel("BoxModel", "Models/box.j3o");
        //            this.addChildNode(boxModel.getId(), boxNode.getId());
        //
        //            this.setNodeTranslation(staplerModel, new VisuVector3f(0f + 15, 0f, -.1f));
        //            this.setNodeRotation(staplerModel, new VisuRotation(0, -90, 0));
        //            this.setNodeScale(staplerModel, new VisuVector3f(.5f, .5f, .5f));
        //        box.setParent(staplerNode.getId());
        //
        //        ComponentsList1 = helper.getAllComponents();
        //        ComponentsList = helper.getComponent("Box");
        //   
        //this.executeTestScenario(10);
        //        sleep(1000);
        //        box.setTranslation(new VisuVector3f(20, 0.2f, -1));
        //        sleep(1000);
        //        box.setScale(new VisuVector3f(10,10,10));
        //        sleep(1000);
        //
        //
        //
        //        helper.undo();
        //        sleep(1000);
        //        helper.undo();
        //        sleep(1000);
        //        helper.redo();
        //        sleep(1000);
        //        helper.redo();


    }


    
    
    
    //Just a small test for the query capability of hazelcast
    //if you have a map with a simple datatype, you can use the virtual attributename "value" in your query.
    public void testQuery() {
        IMap<Long, Long> map = client.getMap("testMap");

        map.put(1L, 400L);
        map.put(2L, 500L);
        map.put(3L, 400L);

        EntryObject e = new PredicateBuilder().getEntryObject();
        Predicate predicate = e.get("value").greaterEqual(500L);
        System.out.println(predicate.toString());
        Set<Map.Entry<Long, Long>> l = (Set<Map.Entry<Long, Long>>) map.entrySet(predicate);

        for (Map.Entry<Long, Long> entry : l) {
            System.out.println("key: " + entry.getKey());
        }

    }

    public void executeTestScenario(int iterations) {
    }

    public static void main(String[] args) {
        TestClient_NetbeansTest tc = new TestClient_NetbeansTest();
  tc.startTest(1);
//        tc.testQuery();

        for (int i = 0; i < 1000; i++) {
         tc.execute();
        }
        tc.shutdown();
    }

    @Override
    public void addConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {

        sleep(30);
        box.setTranslation(new VisuVector3f(0, 2.2f, -1));
        sleep(100);
        box.setTranslation(new VisuVector3f(0, 2.2f, -2f));
        sleep(100);
        box.changeToWorldPosition();
        box.setInterpolation(new VisuInterpolation(false, false, false));


    }
}
