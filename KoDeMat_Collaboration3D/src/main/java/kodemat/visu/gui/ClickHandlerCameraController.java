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
package kodemat.visu.gui;

import kodemat.visu.input.camera.controller.AbstractClickHandlerCameraController;
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
public class ClickHandlerCameraController extends AbstractClickHandlerCameraController {

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
   

    public ClickHandlerCameraController(Application application, Node rootNode, List<ClickMapping> clickMappings, KeyInputMapping keyMappings) {
        super(application,rootNode,clickMappings, keyMappings);
        
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




}
