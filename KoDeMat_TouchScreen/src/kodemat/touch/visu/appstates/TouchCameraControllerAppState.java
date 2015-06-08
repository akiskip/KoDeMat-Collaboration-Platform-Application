/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.touch.visu.appstates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import java.util.ArrayList;
import kodemat.visu.gui.ClickHandlerCameraController;
import kodemat.visu.input.mouse.ClickMapping;
import kodemat.visu.input.mouse.ClickSelection;
import kodemat.visu.editor.EditorManager;
import kodemat.touch.visu.input.camera.controller.AbstractTouchHandlerCameraController;
import kodemat.visu.gui.JmeCamInputMapping;
import kodemat.touch.visu.input.multitouch.MultiTouchAdapter;
import kodemat.visudata.visuComponents.VisuComponent;
import org.openide.util.Exceptions;

/**
 *
 * @author moro
 */
public class TouchCameraControllerAppState extends AbstractAppState {

    SimpleApplication visuClient;
    private ClickHandlerCameraController cameraController;
    private AbstractTouchHandlerCameraController cameraControllerT;
    ArrayList<ClickMapping> clickMappings;
    Node modelNode;
    Node rootNode;
    private Geometry cursorGeo;
    private EditorManager editorManager = new EditorManager(this);
    private JmeCamInputMapping keyMappings;
    public MultiTouchAdapter mt_adapter;

    public TouchCameraControllerAppState(Node modelNode) {
        this.modelNode = modelNode;

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.visuClient = (SimpleApplication) app;
        this.rootNode = visuClient.getRootNode();
        editorManager.initialize(visuClient);
//!!!!sync
        //createCursor();

//        this.rootNode = ap
        clickMappings = new ArrayList<ClickMapping>();
        keyMappings = new JmeCamInputMapping();
        
   

        clickMappings.add(
                new ClickMapping<VisuComponent>(VisuComponent.class) {
            @Override
            public void handleClick(final ClickSelection<VisuComponent> selection) {
                editorManager.handleClick(selection);
            }
        });

        cameraController = new ClickHandlerCameraController(app, modelNode, clickMappings, keyMappings);
        /*
         */ 
        cameraControllerT = new AbstractTouchHandlerCameraController(app, modelNode, clickMappings, keyMappings);
         
      //!!!!  
             // converted to touch
//        try {
//            mt_adapter = new MultiTouchAdapter(cameraControllerT);
//        } catch (Exception ex) {
//            Exceptions.printStackTrace(ex);
//        }
        
        getCameraController().initialize();
        getCameraController().setCollisionMarker(null);
        getCameraController().setDragOriginMarker(null);
        getCameraController().enable();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        getCameraController().update(tpf);
    }

    public void createCursor() {
        Mesh mesh = new com.jme3.scene.shape.Box(0.07f, 0.07f, 0.07f);
        setCursorGeo(new Geometry("collisionMarker", mesh));
        ColorRGBA color = ColorRGBA.Cyan;
        Material mat;
        mat = new Material(visuClient.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

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
        getCursorGeo().setMaterial(mat);
        getCursorGeo().setCullHint(CullHint.Always);

        rootNode.attachChild(getCursorGeo());
    }

    /**
     * @return the cursorGeo
     */
    public Geometry getCursorGeo() {
        return cursorGeo;
    }

    /**
     * @param cursorGeo the cursorGeo to set
     */
    public void setCursorGeo(Geometry cursorGeo) {
        this.cursorGeo = cursorGeo;
    }

    /**
     * @return the cameraController
     */
    public ClickHandlerCameraController getCameraController() {
        return cameraController;
    }
  /**
     * @return the cameraController
     */
    public AbstractTouchHandlerCameraController getCameraControllerT() {
        return cameraControllerT;
    }
    /**
     * @return the editorManager
     */
    public EditorManager getEditorManager() {
        return editorManager;
    }
}
