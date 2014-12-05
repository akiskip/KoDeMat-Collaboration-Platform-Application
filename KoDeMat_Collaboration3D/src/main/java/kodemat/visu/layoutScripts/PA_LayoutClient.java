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
import com.jme3.scene.Spatial;
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
public class PA_LayoutClient extends LayoutClientImpl {

    /*
     * Haselcast Client
     */

    VisuComponent kurve;
    VisuComponent gurtetrei;
    VisuComponent gurtetrei1;
    private VisuComponent halleNode;
    private VisuComponent halleModel;
    private VisuComponent ehbModel;
 
    
    public PA_LayoutClient() {
        //conf file for haselcast

        super();

     
        
    }

    /**
     * 2
     *
     * @param count
     */
    public void startTest(int count) {

           super.addImageAsFloor("Textures/fml_versuchshalle_1.jpg");
        
      halleNode = this.createNode("Halle",false);
        setNodeTranslation(halleNode, new VisuVector3f(0f, 0.0f, 0f));
    
        halleModel = this.createModel("HalleModel", "Models/halle_fml_imvirell/halle_fml_DSA.j3o");
        this.addChildNode(halleModel.getId(), halleNode.getId());
        this.setNodeTranslation(halleModel, new VisuVector3f(0f, 0f, -.1f));
        this.setNodeRotation(halleModel, new VisuRotation(0, 180f, 0));
        this.setNodeScale(halleModel, new VisuVector3f(1f, 1f, 1f)); 
        
        ehbModel = this.createModel("EHBModel", "Models/EHB/ehb.j3o");
      
        this.addChildNode(ehbModel.getId(), halleNode.getId());
        this.setNodeTranslation(ehbModel, new VisuVector3f(0f, 0f, -.1f));
        this.setNodeRotation(ehbModel, new VisuRotation(0, 180f, 0));
        this.setNodeScale(ehbModel, new VisuVector3f(1f, 1f, 1f));


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
        PA_LayoutClient tc = new PA_LayoutClient();

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



    }
}
