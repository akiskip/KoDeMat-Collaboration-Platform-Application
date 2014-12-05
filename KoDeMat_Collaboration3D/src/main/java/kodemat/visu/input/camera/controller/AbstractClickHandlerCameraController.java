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
package kodemat.visu.input.camera.controller;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Line;
import java.awt.Toolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import kodemat.visu.input.camera.InputMappingPair;
import kodemat.visu.input.camera.KeyInputMapping;
import kodemat.visu.input.camera.controller.AbstractCameraController;
import kodemat.visu.input.mouse.ClickChecker;
import kodemat.visu.input.mouse.ClickMapping;
import kodemat.visu.input.mouse.ClickSelection;

/**
 * Class that implements
 *
 * @author normenhansen
 */
public  class AbstractClickHandlerCameraController extends AbstractCameraController {

    public static final String CONNECTIONARROW = "connectionArrow";
    public static final String CONNECTIONLINE = "connectionLine";
    public static final String CONNECTIONNODE = "connectionNode";
    protected Node rootNode;
    private Spatial collisionMarker = null;
    private Spatial dragOriginMarker = null;
    protected List<ClickMapping> clickMappings = new ArrayList<ClickMapping>();
    CollisionResults results = null;
    private CollisionResult dragOrigin = null;
    Material arrowMat;
    private boolean showDragLine = true;
    private Line dragLine = null;
    private Geometry dragGeometry = null;
    private Node dragNode;
    private Vector3f startLine, endLine;
    private boolean collideOnlyFloor = false;
    private KeyInputMapping keyMappings;
    Application app;
   

    public AbstractClickHandlerCameraController(Application application, Node rootNode, List<ClickMapping> clickMappings, KeyInputMapping keyMappings) {
        super(application, application.getCamera(), application.getInputManager());
        this.rootNode = rootNode;
        this.clickMappings = clickMappings;
        this.keyMappings = keyMappings;
        this.app = application;
        arrowMat = new Material(application.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        arrowMat.getAdditionalRenderState().setWireframe(true);
        arrowMat.setColor("Color", ColorRGBA.Red);
      
        dragLine = new Line(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
        dragLine.setLineWidth(2);
        dragNode = new Node(CONNECTIONNODE);
        dragGeometry = new Geometry(CONNECTIONLINE, dragLine);
        dragGeometry.setMaterial(arrowMat);
        dragNode.attachChild(dragGeometry);
        dragNode.setCullHint(CullHint.Always);
    }

    @Override
    protected void checkRay() {
        if (collisionMarker != null || dragOrigin != null) {
            if (results == null) {
                results = computeCollision();
            }
            final CollisionResult result = getNearestVisibleCollision();
            if (result != null && result.getContactPoint() != null) {
                Vector3f contactPoint = result.getContactPoint();

                if (collisionMarker != null) {
//                    if (!collideOnlyFloor || (collideOnlyFloor && result.getGeometry().getName().contains("Floor"))) {

                        collisionMarker.setLocalTranslation(contactPoint);
                        collisionMarker.setCullHint(CullHint.Never);
//                    }
                }

                if (dragOrigin != null && dragOrigin.getContactPoint() != null) {
                    Vector3f dragPoint = dragOrigin.getContactPoint();
                    if (dragPoint.distance(contactPoint) > 0.2f) {
                        if (dragOriginMarker != null) {
                            dragOriginMarker.setLocalTranslation(dragPoint);
                            dragOriginMarker.setCullHint(CullHint.Never);
                        }
                        if (showDragLine) {
                            dragLine.updatePoints(dragPoint, contactPoint);
                            startLine = dragPoint;
                            endLine = contactPoint;
                            dragNode.setCullHint(CullHint.Never);
                        } else {
                            dragNode.setCullHint(CullHint.Always);
                        }
                    } else {
                        if (dragOriginMarker != null) {
                            dragOriginMarker.setCullHint(CullHint.Always);
                        }
                        dragNode.setCullHint(CullHint.Always);
                    }
                } else {
                    if (dragOriginMarker != null) {
                        dragOriginMarker.setCullHint(CullHint.Always);
                    }
                    dragNode.setCullHint(CullHint.Always);
                }



            } else {
                if (collisionMarker != null) {
                    collisionMarker.setCullHint(CullHint.Always);
                }
                dragNode.setCullHint(CullHint.Always);
            }


        }
    }

    @Override
    protected void checkPressed(int button, boolean special) {
        if (button == 0 && !special) {
            if (results == null) {
                results = computeCollision();
            }
            dragOrigin = getNearestVisibleCollision();
        }
    }

    @Override
    public void checkReleased(int button, boolean special) {


        if (results == null) {
            results = computeCollision();
        }
        final CollisionResult result = getNearestVisibleCollision();
//            java.awt.EventQueue.invokeLater(new ClickChecker(this, result, dragOrigin, clickMappings));
        if (button == 0) {
            new ClickChecker(this, result, dragOrigin, clickMappings, clickCount, special).run();

            checkClick = false;
        } else if (button == 1) {
            new ClickChecker(this, result, dragOrigin, clickMappings, clickCountR, special, true).run();

            checkClickR = false;
        }
//        if (button == 0 && special) {
//            if (results == null) {
//                results = computeCollision();
//            }
//            CollisionResult result = results.getClosestCollision();
//            if (result != null) {
////                ((TopologyDataTopComponent) master).doMoveCursor(result.getContactPoint());//getGeometry().getWorldTranslation().add(result.getGeometry().getWorldRotation().mult(result.getContactPoint())));
//            }
//            checkClickR = false;
//        }
        dragOrigin = null;
        if (dragOriginMarker != null) {
            dragOriginMarker.setCullHint(CullHint.Always);
        }
        dragNode.setCullHint(CullHint.Always);
    }

    public ClickMapping getDefaultHandler() {
        return defaultHandler;
    }

    private CollisionResult getNearestVisibleCollision() {
        CollisionResult tempResult = null;
        //Sicherstellen, dass nur sichtbare Geometries gepickt werden.
        //Allerdings nicht bei vererbtem CullHint.Always.
        for (int i = 0; i < results.size(); i++) {
            tempResult = results.getCollision(i);
            if (tempResult != null) {
                if (tempResult.getGeometry().getCullHint() != CullHint.Always) {
                    break;
                }
            } else {
                break;
            }
        }
        return tempResult;
    }
 
    
    public CollisionResults computeCollision() throws UnsupportedCollisionException {
        CollisionResults r = new CollisionResults();
        Ray ray = new Ray();
        Vector3f pos = cam.getWorldCoordinates(new Vector2f(mouseX, mouseY), 0).clone();
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(mouseX, mouseY), 0.3f).clone();
        dir.subtractLocal(pos).normalizeLocal();
        ray.setOrigin(pos);
        ray.setDirection(dir);
        rootNode.collideWith(ray, r);
        return r;
    }


    protected ClickSelection dragOriginSelection = new ClickSelection();

    public ClickSelection getDragOriginSelection() {
        return dragOriginSelection;
    }

    public void setDragOriginSelection(ClickSelection dragOriginSelection) {
        this.dragOriginSelection = dragOriginSelection;
    }
    protected ClickSelection currentSelection = new ClickSelection();
    public static final String PROP_CURRENTSELECTION = "currentSelection";

    public ClickSelection getCurrentSelection() {
        return currentSelection;
    }
    protected ClickMapping defaultHandler = null;

    public void setDefaultClickHandler(ClickMapping ha) {
        defaultHandler = ha;
    }

    public void setCurrentSelection(ClickSelection currentSelection) {
        ClickSelection oldCurrentSelection = this.currentSelection;
        this.currentSelection = currentSelection;
        propertyChangeSupport.firePropertyChange(PROP_CURRENTSELECTION, oldCurrentSelection, currentSelection);
    }
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * @return the collisionMarker
     */
    public Spatial getCollisionMarker() {
        return collisionMarker;
    }

    
    public Spatial initiateCollisionMarker(){
        
        Mesh mesh = new com.jme3.scene.shape.Box(0.07f, 0.07f, 0.07f);
        Geometry collisionMarkerGeo = new Geometry("collisionMarker", mesh);
        ColorRGBA color = ColorRGBA.Cyan;
        Material mat;
        mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

        mat.setFloat(
                "Shininess", 32f);
        mat.setBoolean(
                "UseMaterialColors", true);
        mat.setColor(
                "Ambient", color.clone());
        mat.setColor(
                "Diffuse", color.clone());
        mat.setColor(
                "Specular", color.clone());
        collisionMarkerGeo.setMaterial(mat);

        rootNode.attachChild(collisionMarkerGeo);
        return collisionMarkerGeo;
    }
    
    public Spatial initiateDragMarker(){
               Mesh mesh2 = new com.jme3.scene.shape.Box(0.07f, 0.07f, 0.07f);
        Geometry dragMarker = new Geometry("collidragMarkersionMarker", mesh2);
        ColorRGBA color2 = ColorRGBA.Red;
        Material mat2;
        mat2 = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

        mat2.setFloat(
                "Shininess", 32f);
        mat2.setBoolean(
                "UseMaterialColors", true);
        mat2.setColor(
                "Ambient", color2.clone());
        mat2.setColor(
                "Diffuse", color2.clone());
        mat2.setColor(
                "Specular", color2.clone());
        dragMarker.setMaterial(mat2);

        rootNode.attachChild(dragMarker);
        return dragMarker;
    }
    
    /**
     * @param collisionMarker the collisionMarker to set
     */
    public void setCollisionMarker(Spatial collisionMarker ) {
       
        if (collisionMarker == null) {
            collideOnlyFloor = false;
        }
        if (this.collisionMarker != null) {
            Node parent = this.collisionMarker.getParent();
            if (parent != null) {
                parent.detachChild(this.collisionMarker);
            }
        }
        this.collisionMarker = collisionMarker;
    }

    public void setCollisionMarker(Spatial collisionMarker, boolean onlyFloor) {
        this.collideOnlyFloor = onlyFloor;
        setCollisionMarker(collisionMarker);
    }

    @Override
    protected void endOfUpdate() {
        results = null;
    }

    /**
     * @return the dragOriginMarker
     */
    public Spatial getDragOriginMarker() {
        return dragOriginMarker;
    }

    /**
     * @param dragOriginMarker the dragOriginMarker to set
     */
    public void setDragOriginMarker(Spatial dragOriginMarker) {
        if (this.dragOriginMarker != null) {
            Node parent = this.dragOriginMarker.getParent();
            if (parent != null) {
                parent.detachChild(this.dragOriginMarker);
            }
        }
        this.dragOriginMarker = dragOriginMarker;
        if (dragOriginMarker != null) {
            dragOriginMarker.setCullHint(CullHint.Always);
        }
    }

    
    
    /**
     * @return the dragNode
     */
    public Node getDragNode() {
        return dragNode;
    }
    
    public Node getRootNode()  {
        return this.rootNode;
            }

    /**
     * @return the showDragLine
     */
    public boolean isShowDragLine() {
        return showDragLine;
    }

    /**
     * @param showDragLine the showDragLine to set
     */
    public void setShowDragLine(boolean showDragLine) {
        this.showDragLine = showDragLine;
    }

    @Override
    public void initialize() {
      super.initialize();
//        toggle Visible cursor
        inputManager.setCursorVisible(true);
        
        doSetCamFocus(new Vector3f(0f,0f,0f));
//        rotateCamera(cam.getLeft().clone(), 45);   
        this.setUpKeyInputs(keyMappings);
// 
        this.setCollisionMarker(this.initiateCollisionMarker());
        this.setDragOriginMarker(this.initiateDragMarker());
        rootNode.attachChild(this.getDragNode());

        super.appInit = true;
    }


    public Spatial getDragNodeCopy(String name) {
        Line line = new Line(startLine, endLine);
        line.setLineWidth(2);
        Node node = new Node(name);
        Geometry geometry = new Geometry("Geometry " + name, line);
        geometry.setMaterial(arrowMat);
        node.attachChild(geometry);
        return node;
    }

    @Override
    protected void setUpKeyInputs(KeyInputMapping keyMappigs) {

        for (InputMappingPair mapping : keyMappigs.getInputKeyMappings()) {
            inputManager.addMapping(mapping.getMappingName(), mapping.getMappingTrigger());
        }

        inputManager.addListener(this, keyMappigs.getMappingstoString());
    }
}
