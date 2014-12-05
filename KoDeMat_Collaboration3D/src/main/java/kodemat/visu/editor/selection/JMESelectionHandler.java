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
package kodemat.visu.editor.selection;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapEvent;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import kodemat.visudata.VisuColor;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuMarking;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuVector3f;

/**
 *
 * @author Koshkabb, Kipouridis
 */
public   class  JMESelectionHandler implements IVisuSelectable {

    private  VisuComponent selectedModel;
    private Spatial selectedSpatial;
    private static  String username;
    
    public static String getUsername(){
        return username;
    } 

    public  VisuComponent getSelectedModel() {
        
        return selectedModel;
    }

    public  void setSelectedModel(VisuComponent selectedModel) {
        this.selectedModel = selectedModel;
    }

    public Spatial getSelectedSpatial() {
        return selectedSpatial;
    }

    public void setSelectedSpatial(Spatial selectedSpatial) {
        this.selectedSpatial = selectedSpatial;
    }
    private VisuHelper visuHelper;
    private SimpleApplication visuClient;
    
    private ArrayList<Long> selectedObjectIds;
    private boolean showSelection;
    private final IMap<String, ArrayList<Long>> allSelectedObjectsMap;

    public JMESelectionHandler(Application visuClient, VisuHelper visuHelper, boolean showSelection) {
        this.visuHelper = visuHelper;
        username = visuHelper.getUsername();
        selectedObjectIds = new ArrayList<>();
        this.visuClient = (SimpleApplication) visuClient;
        this.showSelection = showSelection;
        
        allSelectedObjectsMap = visuHelper.getSelectedComponents();
        
//        add an entry listener to get the events when a user selects an object
        EntryListener listener = new UserSelectionEntryListener();
            
            allSelectedObjectsMap.addEntryListener(listener, true);
            
    }

    public ArrayList<Long> getSelectedObjectIds() {
        return selectedObjectIds;
    }

    public void setSelectedObjectIds(ArrayList<Long> selectedObjectIds) {
        this.selectedObjectIds = selectedObjectIds;
 
    }

//    only for one component for the moment, should be extended to enable multiple object selection
       public void setSelectedModel(ArrayList<Long> selectedObjectIds) {
      
        long selectedModelId = this.getSelectedObjectIds().get(0);
        this.selectedModel = visuHelper.getComponent(selectedModelId);
        
        findSpatial(selectedModel.getName());
           
       
       
    }

    public void findSpatial(final String name) {
        SceneGraphVisitor visitor = new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
                if (spatial.getName() != null) {
                    if (spatial.getName().equals(name)) {
                        selectedSpatial = spatial;
                    }
                }
            }
        };
        visuClient.getRootNode().breadthFirstTraversal(visitor);
    }
//The method is a bit erroneous, should be replaced with a clear interface
    @Override
    public void setObjectSpatial(Spatial spatial) {
        this.selectedSpatial = spatial;
        
    }

    
    public void rotate() {
//            VisuComponent parent = getModelParent();
            VisuComponent component = getSelectedModel();

            float old = 0;
            if (component.getRotation() != null) {
                old = component.getRotation().y;
            }
            if (old < -360) {
                old = old + 360;
            }
            component.setRotation(new VisuRotation(0, old - 5, 0));
        
    }

    @Override
    public void rotate(float deg) {
            VisuComponent component = getSelectedModel();
            component.setRotation(new VisuRotation(0, deg, 0));
    }

    @Override
    public void scale(float scale) {
 
            /*  VisuVector3f originalScale = selectedModel.getOriginalScale();
             if (originalScale != null) {
             selectedModel.setScale(new VisuVector3f(originalScale.x * scaleNum, originalScale.y * scaleNum, originalScale.z * scaleNum));
             }else{*/
            removeBoundingBox();
            selectedModel.setScale(new VisuVector3f(scale, scale, scale));
            createBoundingBox();
            //}
    }

    
      @Override
              public void delete(){
         ArrayList<String> toDelete = new ArrayList<String>();
          
         toDelete.add(getSelectedModel().getName());

                visuHelper.deleteComponents(toDelete);
    }

    @Override
    public void showOrHideNote() {

        Node selectedNode = (Node) selectedSpatial;
        List<Spatial> childrenList = selectedNode.getChildren();
        for (Spatial child : childrenList) {
            if (child.getName().contains("Note")) {
                if (child.getCullHint() != Spatial.CullHint.Always) {
                    child.setCullHint(Spatial.CullHint.Always);
                } else {
                    child.setCullHint(Spatial.CullHint.Never);
                }
            }
        }
    }

//    public void removeLights() {
//        if (selectedSpatial != null) {
//            selectedSpatial.removeLight(lampLight);
//            selectedSpatial.removeControl(lightControl);
//        }
//    }
    @Override
    public void showBigNote() {//Attaches the billboard to the parent
        if (selectedSpatial != null) {
            Node n = (Node) selectedSpatial;
            n.detachChildNamed("Note");
            n.attachChild(NoteBillboardCreator.createNote(visuClient.getAssetManager(), selectedModel.getLastNote()));
        }
    }

    @Override
    public void createBoundingBox() {

        if(selectedModel.getLabel() != null ){
            if(!selectedModel.getLabel().isEmpty()){
            selectedModel.setLabel(selectedModel.getLabel().concat(", "+visuHelper.getUsername())); //TODO: change this when usernames exist           
            }
            }
        String labelText = "Edited by " + visuHelper.getUsername();
            selectedModel.setLabel(labelText); //TODO: change this when usernames exist
            selectedModel.setMarking(new VisuMarking(new VisuColor(0f,0f,0f,0f),labelText,1));
    }

    @Override
    public void removeBoundingBox() {
    if(this.getSelectedModel()!=null){
                selectedModel.setLabel("");
            selectedModel.setMarking(new VisuMarking(new VisuColor(0f,0f,0f,0f),"",0));
    }
    }

    @Override
    public void move(Vector3f direction, float factor) {
//        VisuComponent parent = getModelParent();

        VisuVector3f oldPos = this.selectedModel.getTranslation();
        Vector3f currentTrans = new Vector3f(oldPos.x, oldPos.y, oldPos.z);
        Vector3f movingDirection = direction.mult(factor);
        currentTrans = currentTrans.add(movingDirection);

        oldPos = new VisuVector3f(currentTrans.x, oldPos.y, currentTrans.z);
        this.selectedModel.setTranslation(oldPos);
    }


    @Override
    public VisuComponent getModelParent() {
        if (selectedModel != null) {
            VisuComponent parent = visuHelper.getComponent(selectedModel.getParent());
            if (parent != null) {
                return parent;
            }
        }
        return null;
    }

    @Override
    public Spatial getSpatial() {
        return selectedSpatial;
    }

    @Override
    public boolean isEmpty() {
        return (selectedModel == null);
    }

    @Override
    public boolean setVisuCompToSelected(VisuComponent selectedModel, Spatial selectedSpatial) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean setVisuCompToSelected(VisuComponent selectedModel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void rotateRightGradual() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
      //The first parameter should be generalized
    public class UserSelectionEntryListener implements EntryListener<String, ArrayList<Long>> {

        @Override
        public void entryAdded(EntryEvent<String, ArrayList<Long>> event) {
//              System.out.println("Selection map entry entryAdded ");

        }

        @Override
        public void entryRemoved(EntryEvent<String, ArrayList<Long>> event) {
//              System.out.println("Selection map entry entryRemoved ");
        }

        @Override
        public void entryUpdated(EntryEvent<String, ArrayList<Long>> event) {
//             System.out.println("Selection map entry entry Updated selected ");
            if(event.getKey().contains(username)){  
            setSelectedObjectIds(event.getValue());
            if(event.getValue().size()>0){ //if there is at least one selected object
                if(getSelectedModel()!=null){  //check if there was already an object selected before
                     removeBoundingBox(); //if there was remove its marking
                }
            setSelectedModel(event.getValue());  //update the marking of the currenct selection
             createBoundingBox();
            }else
            {
            removeBoundingBox();
            }
              
        }
        }
        @Override
        public void entryEvicted(EntryEvent<String, ArrayList<Long>> event) {
            System.out.println("Selection map entry evicted ");
        }

        @Override
        public void mapEvicted(MapEvent me) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mapCleared(MapEvent me) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

 
    }
}
