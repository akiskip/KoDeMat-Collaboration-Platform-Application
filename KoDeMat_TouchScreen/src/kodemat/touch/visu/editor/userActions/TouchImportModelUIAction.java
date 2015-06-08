/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.touch.visu.editor.userActions;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import kodemat.touch.visu.appstates.TouchCameraControllerAppState;
import kodemat.visu.editor.IEditor;
import kodemat.visu.editor.userActions.IUIActionsImpl;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.visuComponents.VisuComponent;

/**
 *
 * @author Kipouridis
 */
public class TouchImportModelUIAction extends IUIActionsImpl {

    JFrame frame = new JFrame("Test Preview");
    private String modelName;
    private VisuVector3f position;

    public TouchImportModelUIAction(IEditor visuController) {
        super(visuController);

    }

    public void addModelToSelectedComponent(VisuVector3f inputPosition) throws AssetNotFoundException, IOException {

        position = inputPosition;
        if (position == null) {
            position = new VisuVector3f(0f, 0f, 0f);
        }
        File File = this.openFile();
        this.doAddModelToSelectedComponent(File);
    }

        public void addModelToSelectedComponentDirect(File fileName, VisuVector3f inputPosition) throws AssetNotFoundException, IOException {

        position = inputPosition;
        if (position == null) {
            position = new VisuVector3f(0f, 0f, 0f);
        }
       // File File = this.openFile();
        this.doAddModelToSelectedComponent(fileName);
        
    }
    /**
     * Call the file chooser to select a binary .j3o file
     *
     * @return File, the file that what selecteed
     */
    public File openFile() {
        JFileChooser chooser;
        try {
            String test = this.getClass().getClassLoader().getResource(".").getPath();
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("D:\\KoDeMat_working_GoogleCode\\trunk\\JMEProjects\\HazelCast\\lib\\assets\\Models"));
//            chooser.setCurrentDirectory(new File(test).getParentFile().getParentFile().getParentFile());

            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "j3o Files", "j3o");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                System.out.println("You chose to open this file: " + modelName);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return chooser.getSelectedFile();
    }
/**
 * 
 * @param inputModel The user selected model from the filechooser
 * @throws AssetNotFoundException 
 */
    public void doAddModelToSelectedComponent(final File inputModel) throws AssetNotFoundException, IOException {

        //TODO: Heavy platform dependency. Should be made indepentend from the file system structure -> need for a project structure
 
        final String modelDirectory = inputModel.getAbsolutePath();
        final String modelRelativPath = inputModel.getPath();
        final String modelRelativPath1 = inputModel.getCanonicalPath();
  
     String[] tempPath = modelDirectory.split("assets");
     if(tempPath[1].startsWith("/")){
     tempPath[1]=  this.removeFirstChar(tempPath[1]);
  }
     
 final    String modelPath = (tempPath[1]).replaceAll(Matcher.quoteReplacement(File.separator), "/");

  
  
  
        modelName = inputModel.getName();

        //register the current directory as classpath for the asset manager
//      assetManager.registerLocator(modelDirectory, FileLocator.class);

        final VisuHelper helper = this.getVisuHelper();
        getHazelcastAppState().threadPool.execute(new Runnable() {
            @Override
            public void run() {
                //create a node component at the position of the marker
//                VisuComponent addedNodeComponent = createNode((modelName + "_Node "), position);
                //create a model component 
                VisuComponent addedModelComponent = createModel(modelName + "_Model ", position,null,null, modelPath);
//                    addedModelComponent.setParent(selectedObject.getModel().getId());
//                addedModelComponent.setParent(addedNodeComponent.getId());
//                    assetManager.unregisterLocator(modelDirectory, FileLocator.class);
                
                
            }
        });

    }
    


public String removeFirstChar(String s){
   return s.substring(1);
}


    public void addComponentsFromBlenderScene(Node scene){
           
//        List<Spatial> nodesList = scene.getChildren();
//        
//        for(Spatial node : nodesList){
            List<Spatial> modelList = ((Node)scene).getChildren();
        
            
           int i=0;
            for(Spatial model: modelList )
            {
              System.out.println(model.getName());  
              
                  Vector3f nodePos3f = model.getLocalTranslation();
            Quaternion nodeRotation3f = model.getLocalRotation();
   
            Vector3f nodeScale3f = model.getWorldScale();
            VisuVector3f nodeVisuPos = new VisuVector3f(nodePos3f.x, nodePos3f.y,  nodePos3f.z);
     
 float[] angles =nodeRotation3f.toAngles(null) ; 
         nodeRotation3f.toAngles(angles);
            VisuRotation nodeVisuRotation = new VisuRotation(angles[0]*180/FastMath.PI,angles[1]*180/FastMath.PI,angles[2]*180/FastMath.PI);
            VisuVector3f nodeVisuScale = new VisuVector3f(nodeScale3f.x, nodeScale3f.y,  nodeScale3f.z);
      
            String[] modelName = model.getName().split("/");
            VisuComponent addedModelComponent=  this.createModel(modelName[modelName.length-1].concat("_"+i), nodeVisuPos,nodeVisuRotation,nodeVisuScale, model.getName());
     
            i++;
            
//            }
        }
    }

    public VisuComponent createNode(String nodeName, VisuVector3f position) {
        VisuComponent node = this.getVisuHelper().getComponent(nodeName);

        if (node == null) {
            node = this.getVisuHelper().createComponent(nodeName);
            node.setType(new VisuType(VisuType.NODE, null));
            node.setTranslation(position);
            System.out.println("created node with name " + nodeName + "and id" + node.getId());

        } else {
            JOptionPane.showMessageDialog(new Frame(), "There is already an node with that name " + node.getName());
        }

        return node;
    }

    public VisuComponent createModel(String modelName, VisuVector3f position, VisuRotation rotation,VisuVector3f scale, String modelAssetPath) {
        VisuComponent childNode = this.getVisuHelper().getComponent(modelName);
        if (childNode == null) {
            childNode = this.getVisuHelper().createComponent(modelName);
            childNode.setType(new VisuType(VisuType.MODEL, modelAssetPath));
            childNode.setTranslation(position);
            if(rotation!= null)
            childNode.setRotation(rotation);
            if(scale!=null)
            childNode.setScale(scale);

            System.out.println("created model with name " + modelName + " and id " + childNode.getId());

        } else {
            long randomNum = this.getVisuHelper().getIdGenerator().newId();
            childNode = this.getVisuHelper().createComponent(modelName.concat("_"+randomNum));
            childNode.setType(new VisuType(VisuType.MODEL, modelAssetPath));
            childNode.setTranslation(position);
            if(rotation!= null)
            childNode.setRotation(rotation);
            if(scale!=null)
            childNode.setScale(scale);

            System.out.println("created model with name " + modelName + " and id " + childNode.getId());

        }

        return childNode;
    }

    public void addModelToSelectedComponentDirect(File fileName, TouchCameraControllerAppState c, VisuVector3f inputPosition) throws AssetNotFoundException, IOException {
        position = inputPosition;
        if (position == null) {
            position = new VisuVector3f(0f, 0f, 0f);
        }
       // File File = this.openFile();
        this.doAddModelToSelectedComponent(fileName, c);
    }

    private void doAddModelToSelectedComponent(File inputModel, final TouchCameraControllerAppState c) throws IOException {
             final String modelDirectory = inputModel.getAbsolutePath();
        final String modelRelativPath = inputModel.getPath();
        final String modelRelativPath1 = inputModel.getCanonicalPath();
  
     String[] tempPath = modelDirectory.split("assets");
     if(tempPath[1].startsWith("/")){
     tempPath[1]=  this.removeFirstChar(tempPath[1]);
  }
     
 final    String modelPath = (tempPath[1]).replaceAll(Matcher.quoteReplacement(File.separator), "/");

  
  
  
        modelName = inputModel.getName();

        //register the current directory as classpath for the asset manager
//      assetManager.registerLocator(modelDirectory, FileLocator.class);

        final VisuHelper helper = this.getVisuHelper();
        getHazelcastAppState().threadPool.execute(new Runnable() {
            @Override
            public void run() {
                //create a node component at the position of the marker
//                VisuComponent addedNodeComponent = createNode((modelName + "_Node "), position);
                //create a model component 
                VisuComponent addedModelComponent = createModel(modelName + "_Model ", position,null,null, modelPath);
//                    addedModelComponent.setParent(selectedObject.getModel().getId());
//                addedModelComponent.setParent(addedNodeComponent.getId());
//                    assetManager.unregisterLocator(modelDirectory, FileLocator.class);
              //  c.getCameraController().setCollisionMarker(addedModelComponent, true);
                
            }
        });
    }
}
