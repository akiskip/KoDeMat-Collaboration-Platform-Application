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
package kodemat.visu.editor.userActions;

import com.jme3.math.Vector3f;
import kodemat.visu.editor.IEditor;
import kodemat.visu.input.mouse.ClickSelection;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuInterpolation;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.visuComponents.VisuComponent;

/**
 *
 * @author Kipouridis
 */
public class EdgeUIActionsFacade  extends IUIActionsImpl implements IUIActions{

    
    
    public EdgeUIActionsFacade(IEditor visuController) {
        super(visuController);
     
         }

   
        public void createEdge() {

      ClickSelection<VisuComponent> lastSelection = this.getVisuController().getMarkerSelection();
            if (lastSelection != null) {
                VisuComponent parent = lastSelection.getComponent();
                System.out.println("Parent: " + parent);

                Long id = this.getVisuHelper().getIdGenerator().newId();

                VisuComponent node = getVisuHelper().getComponent("Node for Edge " + id);
                if (node == null) {
                    node = getVisuHelper().createComponent(id, "Node for Edge " + id);
                    node.setType(new VisuType(VisuType.NODE, null));
                }

                Vector3f contactPoint = lastSelection.getCollisionResult().getContactPoint();
                System.out.println(contactPoint);
                System.out.println(lastSelection.getSpatial());
                Vector3f v = lastSelection.getSpatial().worldToLocal(contactPoint, new Vector3f());

                node.setTranslation(new VisuVector3f(v.x, v.y + 0.15f, v.z));
                node.setRotation(new VisuRotation(0, 0, 0));
                node.setScale(new VisuVector3f(1f, 1f, 1f));
                node.setInterpolation(new VisuInterpolation(true, true, true));
                node.setParent(parent.getId());

                id = getVisuHelper().getIdGenerator().newId();

                VisuComponent box = getVisuHelper().getComponent("Edge " + id);
                if (box == null) {
                    box = getVisuHelper().createComponent(id, "Edge " + id);
                    box.setType(new VisuType(VisuType.BOX, null));
                }

                box.setRotation(new VisuRotation(0, 0, 0));
                box.setScale(new VisuVector3f(0.3f, 0.3f, 0.3f));
                box.setInterpolation(new VisuInterpolation(true, true, true));
                box.setLabel("Waypoint " + id);

                box.setTranslation(new VisuVector3f(0, 0, 0));

                box.setParent(node.getId());

            
        }
    }
        //TODO: uncomment this for setting edge point by drag and drop
    /*public void beginSetEdgePoint() {
     Spatial clone = visuClient.getAssetManager().loadModel("Models/box_1.j3o");
     clone.setName("Ghost_EdgePoint");
     clone.scale(0.1f);
     visuClient.getRootNode().attachChild(clone);
     cameraControllerAppState.getCameraController().setCollisionMarker(clone);
     waitForClick_EdgePoint = true;
     }
    
     public void setEdgePoint(ClickSelection<VisuComponent> lastSelection) {
    
    
     if (hazelcastAppState.getClient() != null) {
    
     if (lastSelection == null) {
     lastSelection = getLastSelection();
     }
     if (lastSelection != null) {
     VisuComponent parent = visuHelper.getComponent(lastSelection.getComponent().getParent());
     Long id = visuHelper.getIdGenerator().newId();
    
     VisuComponent edgePoint = visuHelper.getComponent("EdgePoint " + id);
     if (edgePoint == null) {
     edgePoint = visuHelper.createComponent(id, "EdgePoint " + id);
     edgePoint.setType(new VisuType(VisuType.MODEL, "Models/box_1.j3o"));
     }
    
     edgePoint.setRotation(new VisuRotation(0, 0, 0));
     edgePoint.setInterpolation(new VisuInterpolation(true, true, true));
     Vector3f contactPoint = lastSelection.getCollisionResult().getContactPoint();
    
     Vector3f v = lastSelection.getSpatial().getParent().worldToLocal(contactPoint, new Vector3f());
     edgePoint.setScale(new VisuVector3f(.1f, .1f, .1f));
     edgePoint.setParent(parent.getId());
     edgePoint.setTranslation(new VisuVector3f(v.x, v.y, v.z));
    
     }
     }
     }*/
    

    
}
