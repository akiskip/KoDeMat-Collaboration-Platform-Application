/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.touch.visu.input.camera.controller;

import com.jme3.app.Application;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Line;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import static kodemat.touch.visu.input.camera.controller.AbstractTouchHandlerCameraController.CONNECTIONLINE;
import static kodemat.touch.visu.input.camera.controller.AbstractTouchHandlerCameraController.CONNECTIONNODE;
import static kodemat.touch.visu.input.camera.controller.AbstractTouchHandlerCameraController.PROP_CURRENTSELECTION;
import kodemat.visu.input.camera.InputMappingPair;
import kodemat.visu.input.camera.KeyInputMapping;
import kodemat.visu.input.camera.controller.AbstractCameraController;
import kodemat.visu.input.camera.controller.AbstractCameraController;
import kodemat.visu.input.camera.controller.AbstractClickHandlerCameraController;
import kodemat.visu.input.mouse.ClickChecker;
import kodemat.visu.input.mouse.ClickMapping;
import kodemat.visu.input.mouse.ClickSelection;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author OzgeA
 */
public class AbstractTouchHandlerCameraController extends AbstractCameraController {

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
    private boolean rotateclockwise;
    private static float revX, revY, revZ;
    private boolean reverseY = false;
    private boolean reverseZ = false;
    private boolean reverseX = false;

    public AbstractTouchHandlerCameraController(Application application, Node rootNode, List<ClickMapping> clickMappings, KeyInputMapping keyMappings) {
        super(application, application.getCamera(), application.getInputManager());
        this.rootNode = rootNode;
        this.clickMappings = clickMappings;
        this.keyMappings = keyMappings;

        /* touchmappings?? */
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
        dragNode.setCullHint(Spatial.CullHint.Always);

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
                    collisionMarker.setCullHint(Spatial.CullHint.Never);
//                    }
                }

                if (dragOrigin != null && dragOrigin.getContactPoint() != null) {
                    Vector3f dragPoint = dragOrigin.getContactPoint();
                    if (dragPoint.distance(contactPoint) > 0.2f) {
                        if (dragOriginMarker != null) {
                            dragOriginMarker.setLocalTranslation(dragPoint);
                            dragOriginMarker.setCullHint(Spatial.CullHint.Never);
                        }
                        if (showDragLine) {
                            dragLine.updatePoints(dragPoint, contactPoint);
                            startLine = dragPoint;
                            endLine = contactPoint;
                            dragNode.setCullHint(Spatial.CullHint.Never);
                        } else {
                            dragNode.setCullHint(Spatial.CullHint.Always);
                        }
                    } else {
                        if (dragOriginMarker != null) {
                            dragOriginMarker.setCullHint(Spatial.CullHint.Always);
                        }
                        dragNode.setCullHint(Spatial.CullHint.Always);
                    }
                } else {
                    if (dragOriginMarker != null) {
                        dragOriginMarker.setCullHint(Spatial.CullHint.Always);
                    }
                    dragNode.setCullHint(Spatial.CullHint.Always);
                }



            } else {
                if (collisionMarker != null) {
                    collisionMarker.setCullHint(Spatial.CullHint.Always);
                }
                dragNode.setCullHint(Spatial.CullHint.Always);
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
            ///// converted to touch
            //!!!!
            // new TouchChecker(this, result, dragOrigin, clickMappings, clickCount, special).run();

            checkClick = false;
        } else if (button == 1) {
            ///// converted to touch
            //!!!!
            // new TouchChecker(this, result, dragOrigin, clickMappings, clickCountR, special, true).run();

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
            dragOriginMarker.setCullHint(Spatial.CullHint.Always);
        }
        dragNode.setCullHint(Spatial.CullHint.Always);
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
                if (tempResult.getGeometry().getCullHint() != Spatial.CullHint.Always) {
                    break;
                }
            } else {
                break;
            }
        }
        return tempResult;
    }

    private CollisionResults computeCollision() throws UnsupportedCollisionException {
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

    public void setTouchInput2Mouse(int xAxis, int yAxis) {
        mouseX = xAxis;
        mouseY = yAxis;
        System.out.println("inside setTouchInput2Mouse source");
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

    public Spatial initiateCollisionMarker() {

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

    public Spatial initiateDragMarker() {
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
    public void setCollisionMarker(Spatial collisionMarker) {

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

    public Node getRootNode() {
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

        doSetCamFocus(new Vector3f(0f, 0f, 0f));
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

    public AbstractTouchHandlerCameraController getAbstractTouchHandlerCamC() {
        return this;

    }

    public void setRotateclockwise(boolean rotateclockwise) {
        this.rotateclockwise = rotateclockwise;
    }

    @Override
    public void rotateCamera(Vector3f axis, float amount) {
        System.out.println("rotates camera ");
        if (rotateclockwise) {
            super.rotateCamera(cam.getLeft(), amount);
            setRotateclockwise(false);
        } else {
            super.rotateCamera(axis, amount);
        }
//        rot.fromAngleAxis(amount, axis);
//        cam.getLocation().subtract(focus, vector);
//        rot.mult(vector, vector);
//        focus.add(vector, cam.getLocation());
//
//        Quaternion curRot = cam.getRotation().clone();
//        cam.setRotation(rot.mult(curRot));
    }

    public void viewOriginal() {
        reverseX = false;
        reverseY = false;
        reverseZ = false;
        cam.setLocation(new Vector3f(0.0f, 35.0f, 10.0f));
        cam.setRotation(new Quaternion(0.0f, -0.87330467f, 0.4871745f, 0.0f));
    }

    public void viewTop() {
        getReverse();
        // float x = cam.getLocation().x;
         float z = cam.getLocation().z -10.0f;
        cam.setLocation(new Vector3f(cam.getLocation().x, 35.0f, z));
        cam.setRotation(new Quaternion(0, 0.72f, -0.69f, 0.004f));
        System.out.println("cam.getlocation: " + cam.getLocation());
        setReverseZ(-10.0f);
        //rotateCamera(cam.getLeft(), 120);
        //cam.setLocation(new Vector3f(x, y, z));

    }

    public void viewLeft() {
        getReverse();
        float x = cam.getLocation().x - 60.0f;
        cam.setLocation(new Vector3f(x, 10.0f, cam.getLocation().z));
        cam.setRotation(new Quaternion(0.02356979f, 0.74957186f, 0.0f, 0.66096294f));
        setReverseX(-60.0f);
    }

    public void viewRight() {
        getReverse();
        float x = cam.getLocation().x + 60.0f;
        cam.setLocation(new Vector3f(x, 10.0f, cam.getLocation().z));
        cam.setRotation(new Quaternion(0.02356979f, -0.74957186f, 0.026729556f, 0.66096294f));
        setReverseX(60.0f);
    }

    public void viewBack() {
        getReverse();
        float z = cam.getLocation().z - 70.0f;
        cam.setLocation(new Vector3f(cam.getLocation().x, 10.0f, z));
        cam.setRotation(new Quaternion(0, 0, 0, 1));
        setReverseZ(-70.0f);
    }

    public void setReverseX(float revX) {        
        //revY = cam.getLocation().y;
        //revZ = cam.getLocation().z;
        reverseX = true;
        reverseY = false;
        reverseZ = false;
        
        this.revX = revX;
    }

    public void setReverseY(float revY) {
        //revX = cam.getLocation().x;
        //revZ = cam.getLocation().z;
        
        reverseX = false;
        reverseY = true;
        reverseZ = false;
        
        this.revY = revY;
    }

    public void setReverseZ(float revZ) {
        //revX = cam.getLocation().x;
        //revY = cam.getLocation().y;
        
        reverseX = false;
        reverseY = false;
        reverseZ = true;
        
        this.revZ = revZ;
    }

    public void getReverse() {
        if(reverseX == true)
            cam.setLocation(new Vector3f(cam.getLocation().x-revX, cam.getLocation().y, cam.getLocation().z));
        else if(reverseY == true)
            cam.setLocation(new Vector3f(cam.getLocation().x, cam.getLocation().y-revY, cam.getLocation().z));
        else if(reverseZ==true)
            cam.setLocation(new Vector3f(cam.getLocation().x, cam.getLocation().y, cam.getLocation().z-revZ));

    }

    public void moveCameraAxisX(float deltaX) {
        moveCameraInPlane(deltaX * 25f, 0);
    }

    public void moveCameraAxisY(float deltaX) {
        moveCameraInPlane(0, -deltaX * 25f);
    }
    
    public Vector3f getCam(){
        return cam.getLocation();
        
    }
}
