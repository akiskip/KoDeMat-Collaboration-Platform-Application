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
public class TestClient extends LayoutClientImpl {

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
    private VisuComponent boxNode;
    private VisuComponent boxModel;
    private VisuComponent ehbNode;
    private VisuComponent ehbModel;
    private VisuComponent foerderModel;
    private VisuComponent foerderNode;
    VisuComponent kurve;
    VisuComponent gurtetrei;
    VisuComponent gurtetrei1;
    private VisuComponent foerderNode1;
    private VisuComponent foerderNode2;
    private VisuComponent regalNode;
    private VisuComponent regal;
    private VisuComponent kurveNode;
    private VisuComponent halleNode;
    private VisuComponent hallemodel;

    public TestClient() {
        //conf file for haselcast
        super();
    }

    /**
     * 2
     *
     * @param count
     */
    public void startTest(int count) {

//        super.addImageAsFloor("Textures/fml_versuchshalle_1.jpg");
//        helper.getComponent("Floor").setTranslation(new VisuVector3f(10f,0,10f));
//        helper.getComponent("Floor").setRotation(new VisuRotation(0f,180f,0f));
//        super.addImageAsFloor("Textures/Layout_PA-Halle.jpg");



        /*Nodes
         * 
         */
//        ehbNode = this.createNode("EHB");
//        setNodeTranslation(ehbNode, new VisuVector3f(0f, 0.5f, 0f));
        halleNode = this.createNode("Halle");
        setNodeTranslation(halleNode, new VisuVector3f(0f, 0.5f, 0f));
//        hallemodel = this.createModel("hallemodel", "Models/halle_fml/halle_fml.j3o");
        hallemodel = this.createModel("stapler", "Models/Stapler_new/stapler.j3o");
        this.addChildNode(hallemodel.getId(), halleNode.getId());
        this.setNodeTranslation(hallemodel, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(hallemodel, new VisuRotation(0, 0, 0));
        this.setNodeScale(hallemodel, new VisuVector3f(1f, 1f, 1f));


/*
        kurveNode = this.createNode("kurveNode");
        setNodeTranslation(kurveNode, new VisuVector3f(12.66f, 0.0f, 12.88f));

        foerderNode1 = this.createNode("foerderNode1");
        setNodeTranslation(foerderNode1, new VisuVector3f(10.66f, 0.0f, 7.88f));

        foerderNode2 = this.createNode("foerderNode2");
        setNodeTranslation(foerderNode2, new VisuVector3f(13.95f, 10.0f, -4f));
        regalNode = this.createNode("regalNode");
        setNodeTranslation(regalNode, new VisuVector3f(0.2f, 0.0f, 0.2f));


        ehbModel = this.createModel("EHBModel", "Models/EHB/ehb.j3o");
        this.addChildNode(ehbModel.getId(), ehbNode.getId());
        this.setNodeTranslation(ehbModel, new VisuVector3f(2f, 2f, -.1f));
        this.setNodeRotation(ehbModel, new VisuRotation(0, 0, 0));
        this.setNodeScale(ehbModel, new VisuVector3f(1f, 1f, 1f));

        kurve = this.createModel("kurveModel", "Models/flw/kurve90/kurve90.j3o");
        this.addChildNode(kurve.getId(), kurveNode.getId());
        this.setNodeTranslation(kurve, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(kurve, new VisuRotation(0, 0, 0));
        this.setNodeScale(kurve, new VisuVector3f(1f, 1f, 1f));

        gurtetrei = this.createModel("gurtetreirModel", "Models/flw/gurtetrei/gurtetrei_1.j3o");
        this.addChildNode(gurtetrei.getId(), foerderNode1.getId());
        this.setNodeTranslation(gurtetrei, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(gurtetrei, new VisuRotation(0, 0, 0));
        this.setNodeScale(gurtetrei, new VisuVector3f(1f, 1f, 1f));

        gurtetrei1 = this.createModel("gurtetreirModel1", "Models/flw/gurtetrei/gurtetrei_1.j3o");
        this.addChildNode(gurtetrei1.getId(), foerderNode2.getId());
        this.setNodeTranslation(gurtetrei1, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(gurtetrei1, new VisuRotation(0, 0, 0));
        this.setNodeScale(gurtetrei1, new VisuVector3f(1f, 1f, 1f));

        regal = this.createModel("regalModel", "Models/regal/regal_1.j3o");
        this.addChildNode(regal.getId(), regalNode.getId());
        this.setNodeTranslation(regal, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(regal, new VisuRotation(0, 0, 0));
        this.setNodeScale(regal, new VisuVector3f(1f, 1f, 1f));
        

//        caution in order to handle the move correctly, the hight of the nodes should be properly adjusted on the models

//        foerderModel = this.createModel("FoerderModel", "Models/flw/EnergieAnlage.j3o");
//        this.addChildNode(foerderModel.getId(), foerderNode.getId());
//        this.setNodeTranslation(foerderModel, new VisuVector3f(0f, 0f, -.1f));
//        this.setNodeRotation(foerderModel, new VisuRotation(0, 0, 0));

//        this.setNodeTranslation(kurve, new VisuVector3f(0f, 0f, 0f));
//        this.setNodeRotation(kurve, new VisuRotation(0f, 0f, 0f));
//         this.setNodeScale(kurve, new VisuVector3f(1f, 1ff, .2f));


//        this.setNodeTranslation(gurtetrei, new VisuVector3f(0f, 0f, 0f));
//        this.setNodeRotation(gurtetrei, new VisuRotation(0, 0, 0));
//         this.setNodeScale(gurtetrei, new VisuVector3f(.2f, .2f, .2f));

//        this.setNodeTranslation(gurtetrei1, new VisuVector3f(0f, 0f, 0f));
//        this.setNodeRotation(gurtetrei1, new VisuRotation(0, 0, 0));
//         this.setNodeScale(gurtetrei1, new VisuVector3f(.2f, .2f, .2f));


//        this.setNodeTranslation(regal, new VisuVector3f(0f, 0f, 0f));
//        this.setNodeRotation(regal, new VisuRotation(0, 0, 0));
//         this.setNodeScale(regal, new VisuVector3f(0.2f, 0.2f, 0.2f));


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

        //
        //
        //        helper.undo();
        //        sleep(1000);
        //        helper.undo();
        //        sleep(1000);
        //        helper.redo();
        //        sleep(1000);
        //        helper.redo();

*/
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
        TestClient tc = new TestClient();

        tc.startTest(1);



        tc.shutdown();
    }

    @Override
    public void addConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {

        sleep(700);



        box.setTranslation(new VisuVector3f(0, 2.2f, -1));
        sleep(700);
        box.setTranslation(new VisuVector3f(0, 2.2f, -2f));
        sleep(700);
        box.changeToWorldPosition();
        box.setInterpolation(new VisuInterpolation(false, false, false));


    }
}
