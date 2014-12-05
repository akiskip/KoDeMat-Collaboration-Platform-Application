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

/**
 *
 * @author Kipouridis
 */


import Interfaces.LayoutClientImpl;
import XMLUtils.Jme2GEXF;
import XMLUtils.JmeFromGEXFBuilder;
import java.io.FileNotFoundException;
import kodemat.visudata.VisuEdge;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.visuComponents.VisuComponent;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.HierarchicalGraph;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.NodeData;
import org.openide.util.Exceptions;



public class ImportGEXF extends LayoutClientImpl {
    private final VisuHelper helper;

    private HierarchicalGraph importedGraph;
    
    public ImportGEXF(VisuHelper helper){
       super(helper);
        this.helper = helper;
        this.helper.setServerMode(false);
       
        try {
            script();
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    public ImportGEXF(VisuHelper helper,String gexf_name){
       super(helper);
        this.helper = helper;
        this.helper.setServerMode(false);
       
        try {
            script(gexf_name);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public void script() throws FileNotFoundException {
        
    JmeFromGEXFBuilder importedGraph = new JmeFromGEXFBuilder();
   
    this.addComponentsListFromGraph(importedGraph.generateHierarchicalGraph());
    this.addEdgesFromGraph();
          
    }
    
    public void script(String gexf_name) throws FileNotFoundException {
        
    JmeFromGEXFBuilder importedGraph = new JmeFromGEXFBuilder(gexf_name);
   
    this.addComponentsListFromGraph(importedGraph.generateHierarchicalGraph());
    this.addEdgesFromGraph();
          
    }
 
       /**
     *
     * @param importedGraph
     */
    public void addComponentsListFromGraph(HierarchicalGraph importedGraph) {

        this.importedGraph = importedGraph;
//        ArrayList<VisuComponent> componentsList = new ArrayList();
        //Iterate over nodes
        //Attention for hierarchical graphs use getNodesTree to get every node in the graph
        for (Node n : importedGraph.getNodesTree()) {

            NodeData nodeData = n.getNodeData();

            int type = 4;
            String componentType = (String) nodeData.getAttributes().getValue("type");
            System.out.println("type is " + componentType);

            if (componentType.equalsIgnoreCase("Node")) {
                System.out.println("entered node ");
                this.createNode(nodeData.getLabel());
                VisuComponent newNode = helper.getComponent(nodeData.getLabel());
                System.out.println("entered node with ID " + newNode.getId());


                this.setNodeTranslation(helper.getComponent(nodeData.getLabel()), new VisuVector3f(0, 0, 0));
                this.setNodeTranslation(newNode, Jme2GEXF.vector3fFromString((String) nodeData.getAttributes().getValue("translation")));
                this.setNodeRotation(helper.getComponent(nodeData.getLabel()), Jme2GEXF.rotationFromString((String) nodeData.getAttributes().getValue("rotation")));

                //Caution to the SCALE!!! Default value when not explicitly set is 0 and will result in invisible objects!!!
                VisuVector3f scale = Jme2GEXF.vector3fFromString((String) nodeData.getAttributes().getValue("scale"));
                if (scale.x != 0) {
                    this.setNodeScale(helper.getComponent(nodeData.getLabel()), Jme2GEXF.vector3fFromString((String) nodeData.getAttributes().getValue("scale")));
                }

            } else if (componentType.equalsIgnoreCase("Model")) {
                this.createModel(nodeData.getLabel(), (String) nodeData.getAttributes().getValue("model"));
                VisuComponent newModel = helper.getComponent(nodeData.getLabel());

                if (importedGraph.getParent(n) != null && newModel != null) {

                    Long parentId = this.helper.getComponent(importedGraph.getParent(n).getNodeData().getLabel()).getId();
                    helper.getComponent(newModel.getId()).setParent(parentId);
//                    this.addChildNode(newModel.getId(), parentId);
                    System.out.println("addded parent with id " + parentId + " to model " + newModel.getName());

                }
                this.setNodeTranslation(newModel, Jme2GEXF.vector3fFromString((String) nodeData.getAttributes().getValue("translation")));
                this.setNodeRotation(newModel, Jme2GEXF.rotationFromString((String) nodeData.getAttributes().getValue("rotation")));
                //Caution to the SCALE!!! Default value when not explicitly set is 0 and will result in invisible objects!!!
                VisuVector3f scale = Jme2GEXF.vector3fFromString((String) nodeData.getAttributes().getValue("scale"));
                if (scale.x != 0) {
                    this.setNodeScale(newModel, Jme2GEXF.vector3fFromString((String) nodeData.getAttributes().getValue("scale")));
                }


            } else if (componentType.equalsIgnoreCase("BOX")) {
                this.createBox(nodeData.getLabel(), null);
                VisuComponent newBox = helper.getComponent(nodeData.getLabel());

                if (importedGraph.getParent(n) != null && newBox != null) {

                    Long parentId = this.helper.getComponent(importedGraph.getParent(n).getNodeData().getLabel()).getId();
                    helper.getComponent(newBox.getId()).setParent(parentId);
//                    this.addChildNode(newModel.getId(), parentId);
                    System.out.println("addded parent with id " + parentId + " to model " + newBox.getName());

                }
                this.setNodeTranslation(newBox, Jme2GEXF.vector3fFromString((String) nodeData.getAttributes().getValue("translation")));
                this.setNodeRotation(newBox, Jme2GEXF.rotationFromString((String) nodeData.getAttributes().getValue("rotation")));
                //Caution to the SCALE!!! Default value when not explicitly set is 0 and will result in invisible objects!!!
                VisuVector3f scale = Jme2GEXF.vector3fFromString((String) nodeData.getAttributes().getValue("scale"));
                if (scale.x != 0) {
                    this.setNodeScale(newBox, Jme2GEXF.vector3fFromString((String) nodeData.getAttributes().getValue("scale")));
                }


            } else if (componentType.equalsIgnoreCase("Texture")) {
                this.addImageAsFloor((String) nodeData.getAttributes().getValue("model"));
            }

        }

    }

//        Caution to the execution order!! The nodes should be there before adding the edges
    public void addEdgesFromGraph() {

//        ArrayList<VisuComponent> componentsList = new ArrayList();
        //Iterate over nodes
        //Attention for hierarchical graphs use getNodesTree to get every node in the graph
        for (Edge edge : importedGraph.getEdgesTree()) {

            String sourceNodeName = edge.getSource().getNodeData().getLabel();
            String targetNodeName = edge.getTarget().getNodeData().getLabel();

            Long edgeId = (long) edge.getId();
            String edgeLabel = edge.getEdgeData().getLabel();

            //add here the edges generation code
            VisuEdge visuEdge = helper.createEdge(edgeId, edgeLabel);

            visuEdge.setSource(helper.getComponent(sourceNodeName).getId());
            visuEdge.setTarget(helper.getComponent(targetNodeName).getId());

        }
    }

    
    
    
    
}
