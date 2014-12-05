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
import java.util.ArrayList;
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
public class Layout_Oculus extends LayoutClientImpl {

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
    private VisuComponent rr_regal;
    private VisuComponent rr_regalNode;
    private VisuComponent rollen_EHB_beh;
    private VisuComponent kuka_regalNode;
    private VisuComponent kuka_regal;
    private VisuComponent RBG_Node;
    private VisuComponent rollenFoerdererKUKA;
    private VisuComponent rbg;
    private VisuComponent rbg_regalNode;
    private VisuComponent rbgRegalModel;
    private VisuComponent halleNode;
    private VisuComponent halleModel;
  
    
    public Layout_Oculus() {
        //conf file for haselcast

        super(true,false);

    }

    /**
     * 2
     *
     * @param count
     */
    public void startTest(int count) {

//        super.addImageAsFloor("Textures/fml_versuchshalle_1.jpg");
        //helper.getComponent("Floor").setTranslation(new VisuVector3f(10f,0,10f));
        //helper.getComponent("Floor").setRotation(new VisuRotation(0f,180f,0f));
//        super.addImageAsFloor("Textures/Layout_PA-Halle.jpg");

 
          /*Nodes
           * 
           */

            halleNode = this.createNode("Halle");
        setNodeTranslation(halleNode, new VisuVector3f(0f, 0.0f, 0f));
    
        rr_regalNode = this.createNode("RR_RegalNode");
          setNodeTranslation(rr_regalNode, new VisuVector3f(0.2f, 0.0f, 0.2f));
        /*
        ehbNode = this.createNode("EHB");
        setNodeTranslation(ehbNode, new VisuVector3f(0f, 0.5f, 0f));
    
        
        foerderNode1 = this.createNode("foerderNode1");
      setNodeTranslation(foerderNode1, new VisuVector3f(12.66f, 0.0f,-12.88f));
        
        foerderNode2 = this.createNode("foerderNode2");
        setNodeTranslation(foerderNode2, new VisuVector3f(13.95f, 0.0f, -4f));
      
          
        kuka_regalNode = this.createNode("KUKA_regalNode");
          setNodeTranslation(kuka_regalNode, new VisuVector3f(-8f, 0.0f, 6f));
          
            rbg_regalNode = this.createNode("RBG_regalNode");
          setNodeTranslation(rbg_regalNode, new VisuVector3f(12.66f, 0.0f,12.88f));
          
        RBG_Node = this.createNode("RBG_Node");
          setNodeTranslation(RBG_Node, new VisuVector3f(0f, 0.0f, 10.2f));
*/
       
        
        halleModel = this.createModel("HalleModel", "Models/halle_fml_imvirell/halle_fml_imvirell.j3o",false);
        this.addChildNode(halleModel.getId(), halleNode.getId());
        this.setNodeTranslation(halleModel, new VisuVector3f(0f, 0f, -.1f));
        this.setNodeRotation(halleModel, new VisuRotation(0, 90, 0));
        this.setNodeScale(halleModel, new VisuVector3f(1f, 1f, 1f));
        
        rr_regal = this.createModel("RR_RegalModel", "Models/RR_Regale/RR_Regaleblend.j3o");
        this.addChildNode(rr_regal.getId(), rr_regalNode.getId());
        this.setNodeTranslation(rr_regal, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(rr_regal, new VisuRotation(0, 0, 0));
this.setNodeScale(rr_regal, new VisuVector3f(1f, 1f, 1f));   
        
        /*
        ehbModel = this.createModel("EHBModel", "Models/EHB/ehb.j3o");
        this.addChildNode(ehbModel.getId(), ehbNode.getId());
        this.setNodeTranslation(ehbModel, new VisuVector3f(0f, 0f, -.1f));
        this.setNodeRotation(ehbModel, new VisuRotation(0, 0, 0));
        this.setNodeScale(ehbModel, new VisuVector3f(1f, 1f, 1f));
          
          rbgRegalModel = this.createModel("rbgRegalModel", "Models/regal_RBG/regal_RBG.j3o");
        this.addChildNode(rbgRegalModel.getId(), rbg_regalNode.getId());
        this.setNodeTranslation(rbgRegalModel, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(rbgRegalModel, new VisuRotation(0, 0, 0));
        this.setNodeScale(rbgRegalModel, new VisuVector3f(1f, 1f, 1f));
        
        rollen_EHB_beh = this.createModel("gurtetreirModel", "Models/rollenfoerdererEHB_beh/rollenfoerdererEHB_beh.j3o");
        this.addChildNode(rollen_EHB_beh.getId(), foerderNode1.getId());
        this.setNodeTranslation(rollen_EHB_beh, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(rollen_EHB_beh, new VisuRotation(0, 0, 0));
this.setNodeScale(rollen_EHB_beh, new VisuVector3f(1f, 1f, 1f));       
        
rollenFoerdererKUKA = this.createModel("rollenfoerder_KUKA", "Models/rollenfoerderKUKA/rollenfoerderKUKA.j3o");
           this.addChildNode(rollenFoerdererKUKA.getId(), foerderNode2.getId());
        this.setNodeTranslation(rollenFoerdererKUKA, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(rollenFoerdererKUKA, new VisuRotation(0, 0, 0));
this.setNodeScale(rollenFoerdererKUKA, new VisuVector3f(1f, 1f, 1f));      
     


kuka_regal = this.createModel("KUKA_RegalModel", "Models/regalKuka/regalKuka.j3o");
        this.addChildNode(kuka_regal.getId(), kuka_regalNode.getId());
        this.setNodeTranslation(kuka_regal, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(kuka_regal, new VisuRotation(0, 0, 0));
this.setNodeScale(kuka_regal, new VisuVector3f(1f, 1f, 1f));    
 
rbg = this.createModel("RBG_Model", "Models/RBG/RBG.j3o");
        this.addChildNode(rbg.getId(), RBG_Node.getId());
        this.setNodeTranslation(rbg, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(rbg, new VisuRotation(0, 0, 0));
this.setNodeScale(rbg, new VisuVector3f(1f, 1f, 1f));    

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
        Layout_Oculus tc = new Layout_Oculus();

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
