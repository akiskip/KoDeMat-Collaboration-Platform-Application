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
package XMLUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import kodemat.visudata.visuComponents.VisuComponent;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.HierarchicalGraph;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.NodeData;

import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

/**
 *
 * @author  Orthodoxos Kipouridis}
 */
public class JmeFromGEXFBuilder {

    JFrame frame = new JFrame("Test Preview");
    ProjectController pc;
    Workspace workspace;
    ImportController importController;
    Container container;
    private GraphModel graphModel;
    private DirectedGraph directedGraph;
    
    private ArrayList<VisuComponent> componentsList;
    
    
public JmeFromGEXFBuilder() throws FileNotFoundException{
    this.initialiseImport();
        container = this.getContainerFromFile();
}
public JmeFromGEXFBuilder(String gexf_name) throws FileNotFoundException{
    this.initialiseImport();
        container = this.getContainerFromFile(gexf_name);
}
public void initialiseImport(){
    //Init a project - and therefore a workspace
    pc = Lookup.getDefault().lookup(ProjectController.class);
    pc.newProject();
    workspace = pc.getCurrentWorkspace();

    //Import file by choosing a file from disk
    importController = Lookup.getDefault().lookup(ImportController.class);

    //Get a graph model - it exists because we have a workspace
    graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
    
    componentsList = new ArrayList<VisuComponent>();

}
    
public File openFile(){
    JFileChooser chooser;
        try {
         chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "GEXF Files", "gexf");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(frame);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
       System.out.println("You chose to open this file: " +
            chooser.getSelectedFile().getName());
    } 
            
//            File file = new File(getClass().getResource(chooser.getSelectedFile().getPath()).toURI());
           
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return chooser.getSelectedFile();
}


public HierarchicalGraph generateHierarchicalGraph() throws FileNotFoundException{
    

     //Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);
        
  HierarchicalGraph   hierarchicalGraph = graphModel.getHierarchicalGraph() ;
  
     directedGraph = graphModel.getDirectedGraph();
    
    //Count nodes and edges
System.out.println("Nodes: "+hierarchicalGraph.getNodesTree().toArray().length+" Edges: "+hierarchicalGraph.getEdgeCount());
 
//Get a UndirectedGraph now and count edges
UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();
System.out.println("Edges: "+undirectedGraph.getEdgeCount());   //The mutual edge is automatically merged
 
//Iterate over nodes
for(Node n : directedGraph.getNodes()) {
    Node[] neighbors = directedGraph.getNeighbors(n).toArray();
    System.out.println(n.getNodeData().getLabel()+" has "+neighbors.length+" neighbors");
}
 
//Iterate over edges
for(Edge e : directedGraph.getEdges()) {
    System.out.println(e.getSource().getNodeData().getId()+" -> "+e.getTarget().getNodeData().getId());
}
    

return hierarchicalGraph;
}



    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public ImportController getImportController() {
        return importController;
    }

    public void setImportController(ImportController importController) {
        this.importController = importController;
    }

    public DirectedGraph getDirectedGraph() {
        return directedGraph;
    }

    public void setDirectedGraph(DirectedGraph directedGraph) {
        this.directedGraph = directedGraph;
    }




public Container getContainerFromFile() throws FileNotFoundException{
    File gexfFile = this.openFile();
    container = importController.importFile(gexfFile);
    return container;
}

public Container getContainerFromFile(String gexf_file) throws FileNotFoundException{
         
    File gexfFile = new File(gexf_file);
    container = importController.importFile(gexfFile);
    return container;
}
 


 
public  Node findNodeById(String nodeId){
    //Find node by id
   
Node node = directedGraph.getNode(nodeId);
 return node;
}
        

}
