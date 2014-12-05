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
package graphXML;




import Interfaces.LayoutClientImpl;
import XMLUtils.JmeFromGEXFBuilder;
import java.io.FileNotFoundException;

/**
 *
 * @author Mathieu Bastian
 */
public class GEXF_ImporterClient extends LayoutClientImpl {
    
    public GEXF_ImporterClient(){
        super();
        
    }
    
    public void script() throws FileNotFoundException {
        
    JmeFromGEXFBuilder importedGraph = new JmeFromGEXFBuilder();
//   
//    this.addComponentsListFromGraph(importedGraph.generateHierarchicalGraph());
//    this.addEdgesFromGraph();
          
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        GEXF_ImporterClient previewJFrame = new GEXF_ImporterClient();
        previewJFrame.script();
    }
}
