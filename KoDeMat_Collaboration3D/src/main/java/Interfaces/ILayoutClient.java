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


import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuVector3f;

/**
 *
 * @author Orthodoxos Kipouridis. Moritz Roidl
 */
public interface ILayoutClient {

//    String propertiesFile = "./prop.properties";
    String propertiesFile = "./kodemat_hazelcast_prop.xml";
    
    HazelcastInstance getHaselcastClient();
    
    HazelcastInstance  getClientInst();
    VisuHelper getHelper();
    VisuHelper createHelper(boolean isServer, boolean isClient);
    
    Config  getClientConfig();
            
    Config  fileBuild(String FileName);
    
    
    public void addImageAsFloor(String assetPath);
    
    public VisuComponent createNode(String nodeName);
    
    /**
     *
     * @return
     */
    public VisuComponent createModel(String modelName, String modelAssetPath);
    public void addChildNode(long childId, long parentId);
    
    //TODO: specify the data model of connenctions in visuDataStructures taking into 
//    account the GEXF Edges structure
    public  void addConnection();
    
    public void setNodeTranslation(VisuComponent node, VisuVector3f nodePosition);
    public void setNodeScale(VisuComponent node, VisuVector3f nodePosition);
    public void setNodeRotation(VisuComponent node, VisuRotation nodePosition);

    public void shutdown();

    public void sleep(long millis);
}
