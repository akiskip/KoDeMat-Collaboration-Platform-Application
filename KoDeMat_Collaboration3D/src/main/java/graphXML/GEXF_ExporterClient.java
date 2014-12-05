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
import XMLUtils.Jme2GEXF;
import it.uniroma1.dis.wsngroup.gexf4j.core.Edge;
import it.uniroma1.dis.wsngroup.gexf4j.core.EdgeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.Graph;
import it.uniroma1.dis.wsngroup.gexf4j.core.Mode;
import it.uniroma1.dis.wsngroup.gexf4j.core.Node;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.viz.PositionImpl;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuEdge;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;

/**
 *
 * @author Orthodoxos Kipouridis. Moritz Roidl
 */
public class GEXF_ExporterClient extends LayoutClientImpl {
    private List<VisuComponent> ComponentsList;
    private HashMap<String,Node> gexfNodeMap;
    private List<Edge> gexfEdgeList;
    private GexfImpl gexf;
    private Graph graph;
    private Attribute translation;
    private Attribute rotation;
    private Attribute scale;
    private Attribute type;
    private Attribute modelPath;
    private List<VisuEdge> EdgesList;
    
    public GEXF_ExporterClient(){
        super();
    }

    @Override
    public void execute() {
       ComponentsList =  getHelper().getAllComponents();
       EdgesList =  getHelper().getAllEdges();
       gexfNodeMap  = new HashMap<String,Node>();
       gexfEdgeList = new ArrayList<Edge>();
       this.initiateGraph();
       this.buildGraphNodes();
       this.buildGraphEdges();
       this.exportGraph_XML("PA_Save_20140614.gexf");
       
  
    }
    
    
    public void initiateGraph(){
         gexf = new GexfImpl();
		Calendar date = Calendar.getInstance();
		
                gexf.setVisualization(true);
		gexf.getMetadata()
			.setLastModified(date.getTime())
			.setCreator("KoDeMat.edu")
			.setDescription("A test network for KoDeMat ");

		 graph = gexf.getGraph();
		graph.setDefaultEdgeType(EdgeType.UNDIRECTED).setMode(Mode.STATIC);
		
		AttributeList attrList = new AttributeListImpl(AttributeClass.NODE);
		graph.getAttributeLists().add(attrList);
		
		 translation = attrList.createAttribute("0", AttributeType.STRING, "translation");
		 rotation = attrList.createAttribute("1", AttributeType.STRING, "rotation");
		 scale = attrList.createAttribute("2", AttributeType.STRING, "scale");
		 type = attrList.createAttribute("3", AttributeType.STRING, "type");
		 modelPath = attrList.createAttribute("4", AttributeType.STRING, "model");
                 
    }
    
    
    public void buildGraphNodes(){
            System.out.println("Starting building the XML Graph");

        for(int i=0; i< ComponentsList.size();i++) {
           
            VisuComponent visuComponent = ComponentsList.get(i);
            Node gephiNode = graph.createNode(Long.toString(visuComponent.getId()));
            //TODO: Carefully define which types of components will be exported into gexf graph
            if(visuComponent.getType().type.equals(VisuType.NODE)|| visuComponent.getType().type.equals(VisuType.MODEL) || visuComponent.getType().type.equals(VisuType.BOX))
            {  
//get the position of the node in the jme scenegraph
            VisuVector3f NodePosition = visuComponent.getTranslation();
            if( visuComponent.getTranslation() == null){
                NodePosition = new VisuVector3f(0,0,0);
            }
            VisuRotation NodeRotation = visuComponent.getRotation();
            if(NodeRotation == null) {
                NodeRotation = new VisuRotation(0,0,0);
            }
            
            VisuVector3f NodeScale = visuComponent.getScale();
             if(NodeScale == null) {
                NodeScale = new VisuVector3f(0,0,0);
            }
            /*
             * It's possible to import list attributes in Gephi, for all types (string, int, etc...) but unfortunately these data are not fully used in Gephi yet,
             * and for instance Partition won't be able to do what you want. 
             * Similarly, we also miss a set of filters for dealing with list or adapt existing filters to support lists. 
             * That wouldn't be too hard to implement, but it has to be done. I added this to our Ideas not to forget.

Only the GEXF and GraphML importers support lists, define your attribute as 'liststring" type and it will be recognized. Default separator are ",|;".

             */
        gephiNode
			.setLabel(visuComponent.getName())
                        
                        .setPosition( new PositionImpl(NodePosition.getWithScale(100)[0],NodePosition.getWithScale(100)[1],NodePosition.getWithScale(100)[2]))
			.getAttributeValues()
				.addValue(translation, Jme2GEXF.translationAttribute(NodePosition))
				.addValue(rotation, Jme2GEXF.rotationAttribute(NodeRotation))
				.addValue(scale, Jme2GEXF.translationAttribute(NodeScale))
				.addValue(type,Jme2GEXF.typeAttribute(visuComponent.getType().getString()));
             
//             check if node has a parent node
             if(visuComponent.getParent() != null) {
                gephiNode.setPID(Long.toString(visuComponent.getParent()));
            }
             
             if(visuComponent.getType().type.equals(VisuType.MODEL)||visuComponent.getType().type.equals(VisuType.TEXTURE))
                 gephiNode.getAttributeValues().addValue(modelPath,visuComponent.getType().path );
                 
             
            System.out.println("Added new node with ID " +visuComponent.getId()+"that has a parent node "+visuComponent.getParent());
        gexfNodeMap.put(gephiNode.getId(),gephiNode);
            }
            else if(visuComponent.getType().type.equals(VisuType.TEXTURE) ){
                 gephiNode
			.setLabel(visuComponent.getName())
                         .getAttributeValues()
                        .addValue(type,Jme2GEXF.typeAttribute(visuComponent.getType().getString()))
                        .addValue(modelPath,Jme2GEXF.typeAttribute(visuComponent.getType().path));
            }
        }
    }
    
    public void buildGraphEdges(){
 System.out.println("Starting building the XML Edges");

        for(int i=0; i< EdgesList.size();i++) {
           
            VisuEdge visuEdge = EdgesList.get(i);
           
//            for every edge in the list create the respective gexf edge by connecting the nodes
        String sourceNodeId = Long.toString(visuEdge.getSource());
        String targetNodeId = Long.toString(visuEdge.getTarget());
        
        
        
        gexfNodeMap.get(sourceNodeId).connectTo(Long.toString(visuEdge.getId()),visuEdge.getName(),gexfNodeMap.get(targetNodeId));

    System.out.println("Edge Name : "+visuEdge.getName());
        }
    
    }
    
 public void exportGraph_XML(String filename){
     	StaxGraphWriter graphWriter = new StaxGraphWriter();
		File f = new File(filename);
		Writer out;
		try {
			out =  new FileWriter(f, false);
			graphWriter.writeToStream(gexf, out, "UTF-8");
			System.out.println(f.getAbsolutePath());
                         System.out.println("Successfully extracted "+filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
 }
    
      public static void main(String[] args) {
        
        GEXF_ExporterClient tc = new GEXF_ExporterClient();

            tc.execute();
        
        tc.shutdown();
    }

 
}
