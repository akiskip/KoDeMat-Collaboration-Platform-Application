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

import kodemat.visu.gui.JmeCamInputMapping;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import java.util.concurrent.Callable;
import kodemat.visu.input.camera.CameraViewSetting;
import kodemat.visu.input.camera.KeyInputMapping;

/**
 *Class that contains halders for controlling the camera movements
 * @author normenhansen
 */
public abstract class AbstractCameraController implements ActionListener, AnalogListener, RawInputListener {

    protected boolean leftMouse, rightMouse, middleMouse;
    protected boolean leftControl;
    protected float deltaX, deltaY, deltaZ, deltaWheel;
    protected int mouseX = 0;
    protected int mouseY = 0;
    protected Quaternion rot = new Quaternion();
    protected Vector3f vector = new Vector3f(0, 0, 0);
    protected Vector3f focus = new Vector3f();
    protected Application application;
    protected Camera cam;
    protected InputManager inputManager;
    protected Object master;
    protected boolean moved = false;
    protected boolean movedR = false;
    protected boolean checkClick = false;
    protected boolean checkClickR = false;
    protected boolean checkPressed = false;
    protected boolean checkPressedR = false;
    protected boolean enabled = false;
    protected boolean focusMarkerEnabled = false;
    protected Geometry focusMarker;
    protected CameraViewSetting savedCameraViewSetting = null;
    protected float doubleClickValue = 0f;
    protected int clickCount = 0;
    protected int clickCountR = 0;
    private boolean pan;
    private boolean centering;
    private JmeCamInputMapping camInputs;

    public AbstractCameraController(Application application, Camera cam, InputManager inputManager) {
        this.application = application;
        this.cam = cam;
        this.inputManager = inputManager;
        this.pan = true;
        this.centering = true;
    }

    public void setMaster(Object component) {
        this.master = component;
    }

    public void enable() {
        application.getContext().getMouseInput().setCursorVisible(true);
        inputManager.addRawInputListener(this);
        enabled = true;
    }

    public void disable() {
        if (enabled) {
            inputManager.removeRawInputListener(this);
            inputManager.removeListener(this);
            try {
                inputManager.deleteMapping("LeftControl");
            } catch (IllegalArgumentException e) {
                //ignore if mapping is null
            }
            enabled = false;
        }
    }

    public void setCamFocus(final Vector3f focus) {
        if (centering) {
            application.enqueue(new Callable<Object>() {
                public Object call() throws Exception {
                    doSetCamFocus(focus);
                    return null;
                }
            });
        }
    }

    public void doSetCamFocus(Vector3f focus) {
        cam.setLocation(cam.getLocation().add(focus.subtract(this.focus)));
        this.focus.set(focus);
    }

    /*
     * methods to move camera
     */
    protected void rotateCamera(Vector3f axis, float amount) {
//        if (axis.equals(cam.getLeft())) {
//            float elevation = -FastMath.asin(cam.getDirection().y);
//            amount = Math.min(Math.max(elevation + amount,
//                    -FastMath.HALF_PI), FastMath.HALF_PI)
//                    - elevation;
//        }
        rot.fromAngleAxis(amount, axis);
        cam.getLocation().subtract(focus, vector);
        rot.mult(vector, vector);
        focus.add(vector, cam.getLocation());

        Quaternion curRot = cam.getRotation().clone();
        cam.setRotation(rot.mult(curRot));
    }

    protected void panCamera(float left, float up) {
        cam.getLeft().mult(left, vector);
        vector.scaleAdd(up, cam.getUp(), vector);
        vector.multLocal(cam.getLocation().distance(focus));
        cam.setLocation(cam.getLocation().add(vector));
        focus.addLocal(vector);
    }

    protected void moveCamera(float forward) {
        cam.getDirection().mult(forward, vector);
        cam.setLocation(cam.getLocation().add(vector));
    }

    protected void moveCameraInPlane(float sideway, float forward) {
        vector = cam.getDirection().clone();
        vector.multLocal(1, 0, 1);
        vector.normalizeLocal();
        vector.multLocal(forward, 0, forward);
        cam.setLocation(cam.getLocation().add(vector));
        focus.addLocal(vector);
        vector = cam.getLeft().clone();
        vector.multLocal(1, 0, 1);
        vector.normalizeLocal();
        vector.multLocal(sideway, 0, sideway);
        cam.setLocation(cam.getLocation().add(vector));
        focus.addLocal(vector);
    }

    protected void zoomCamera(float amount) {
        amount = cam.getLocation().distance(focus) * amount;
        float dist = cam.getLocation().distance(focus);
        amount = dist - Math.max(0f, dist - amount);
        Vector3f loc = cam.getLocation().clone();
        loc.scaleAdd(amount, cam.getDirection(), loc);
        cam.setLocation(loc);
    }

    protected void riseCamera(float value) {
        Vector3f vel = new Vector3f(0, value, 0);
        Vector3f pos = cam.getLocation().clone();

        pos.addLocal(vel);
        focus.addLocal(vel);

        cam.setLocation(pos);
    }

    @Override
    public void onAction(String string, boolean bln, float f) {
        if ("MouseButtonLeft".equals(string)) {
            if (bln) {
                leftMouse = true;
                moved = false;
                checkPressed = true;
            } else {
                leftMouse = false;
//                if (!moved) {
                checkClick = true;
//                }
            }
        }
        if ("MouseButtonRight".equals(string)) {
            if (bln) {
                rightMouse = true;
                movedR = false;
                checkPressedR = true;
            } else {
                rightMouse = false;
//                if (!movedR) {
                checkClickR = true;
//                }
            }
        }
        if ("LeftControl".equals(string)) {
            if (bln) {
                leftControl = true;
            } else {
                leftControl = false;
            }
        }
        if ("FocusMarker".equals(string)) {
            if (bln) {
                focusMarkerEnabled = !focusMarkerEnabled;
            }
        }
        if ("SaveCameraViewSetting".equals(string)) {
            if (bln) {
                saveCameraView();
            }
        }
        if ("RestoreCameraViewSetting".equals(string)) {
            if (bln) {
                loadCameraView();
            }
        }
    }

    public void onAnalog(String string, float value, float tpf) {
        if ("MouseAxisX".equals(string)) {
            moved = true;
            movedR = true;
            if (rightMouse && leftControl) {
                moveCameraInPlane(value * 25f, 0);
            }
            if (rightMouse && !leftControl) {
                if (pan) {
                    rotateCamera(Vector3f.UNIT_Y, -value * 2.5f);
                }
            }
        } else if ("MouseAxisY".equals(string)) {
            moved = true;
            movedR = true;
            if (rightMouse && leftControl) {
                moveCameraInPlane(0, -value * 25f);
            }
            if (rightMouse && !leftControl) {
                if(pan){
                    rotateCamera(cam.getLeft(), -value * 2.5f);
                }
            }
        } else if ("MouseAxisX-".equals(string)) {
            moved = true;
            movedR = true;
            if (rightMouse && leftControl) {
                moveCameraInPlane(-value * 25f, 0);
            }
            if (rightMouse && !leftControl) {
                if(pan){
                    rotateCamera(Vector3f.UNIT_Y, value * 2.5f);
                }
            }
        } else if ("MouseAxisY-".equals(string)) {
            moved = true;
            movedR = true;
            if (rightMouse && leftControl) {
                moveCameraInPlane(0, value * 25f);
            }
            if (rightMouse && !leftControl) {
                if(pan){
                    rotateCamera(cam.getLeft(), value * 2.5f);
                }
            }
        } else if ("MouseWheel".equals(string)) {
            zoomCamera(.1f);
        } else if ("MouseWheel-".equals(string)) {
            zoomCamera(-.1f);
        } else if ("StrafeLeft".equals(string)) {
            moveCameraInPlane(value * 50f, 0);
        } else if ("StrafeRight".equals(string)) {
            moveCameraInPlane(-value * 50f, 0);
        } else if ("Forward".equals(string)) {
            moveCameraInPlane(0, value * 50f);
        } else if ("Backward".equals(string)) {
            moveCameraInPlane(0, -value * 50f);
        } else if ("Rise".equals(string)) {
            riseCamera(.1f);
        } else if ("Lower".equals(string)) {
            riseCamera(-.1f);
        }


    }

    public void onJoyAxisEvent(JoyAxisEvent jae) {
    }

    public void onJoyButtonEvent(JoyButtonEvent jbe) {
    }

    public void onMouseMotionEvent(MouseMotionEvent mme) {
        mouseX = mme.getX();
        mouseY = mme.getY();
    }

    @Override
    public void onMouseButtonEvent(MouseButtonEvent mbe) {
    }

    @Override
    public void onKeyEvent(KeyInputEvent kie) {
    }

    @Override
    public void onTouchEvent(TouchEvent evt) {
    }
    /**
     * APPSTATE*
     */
    public boolean appInit = false;

    public void initialize() {

        Node rootNode = ((SimpleApplication) application).getRootNode();
        Mesh mesh = new com.jme3.scene.shape.Box(0.07f, 0.07f, 0.07f);
        focusMarker = new Geometry("focusMarker", mesh);
        ColorRGBA color = ColorRGBA.Cyan;
        Material mat;
        mat = new Material(application.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

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
        focusMarker.setMaterial(mat);

        rootNode.attachChild(focusMarker);

        focus.set(cam.getLocation());
        focus.addLocal(0, 0, -15f);

        doSetCamFocus(new Vector3f());
        rotateCamera(cam.getLeft().clone(), 45);

        appInit = true;
    }

    public boolean isInitialized() {
        return appInit;
    }

    public void stateAttached(AppStateManager asm) {
    }

    public void stateDetached(AppStateManager asm) {
    }

    public void update(float f) {
        if (doubleClickValue > 0f) {
            doubleClickValue += f;
        }
        if (doubleClickValue > 0.4f) {

            clickCount = 0;
            doubleClickValue = 0f;

        }

        if (checkClick) {
            if (doubleClickValue > 0f) {
                clickCount += 1;
            } else {
                clickCount = 1;
                doubleClickValue += f;
            }

        }
        if(checkClickR){

                clickCountR = 1;
            
        }



        if (focusMarker != null) {
            if (focusMarkerEnabled) {
                focusMarker.setCullHint(CullHint.Inherit);
            } else {
                focusMarker.setCullHint(CullHint.Always);
            }
            focusMarker.setLocalTranslation(focus.clone());
        }

        checkRay();
        if (checkPressed) {
            checkPressed(0, leftControl);
            checkPressed = false;
        }
        if (checkPressedR) {
            checkPressed(1, leftControl);
            checkPressedR = false;
        }
        if (checkClick) {
            checkReleased(0, leftControl);
            checkClick = false;
        }
        if (checkClickR) {
            checkReleased(1, leftControl);
            checkClickR = false;
        }
        endOfUpdate();
    }

    protected abstract void checkRay();

    protected abstract void checkPressed(int button, boolean special);

    protected abstract void checkReleased(int button, boolean special);

    protected abstract void endOfUpdate();
    
    protected abstract void setUpKeyInputs(KeyInputMapping keyMappigs);

    public void beginInput() {
    }

    public void endInput() {
    }

    public CameraViewSetting getCameraViewSetting() {
        return new CameraViewSetting(cam.getLocation().clone(), focus.clone());
    }

    public void applyCameraView(final CameraViewSetting setting) {
        if (setting != null) {
            application.enqueue(new Callable<Object>() {
                public Object call() throws Exception {
                    focus = setting.focus.clone();
                    cam.setLocation(setting.location.clone());
                    cam.lookAt(focus, Vector3f.UNIT_Y);
                    return null;
                }
            });
        }
    }

    public Vector3f getCameraDirection() {
        return cam.getDirection();
    }

    public Vector3f getCameraLeft() {
        return cam.getLeft();
    }

    public void saveCameraView() {
        savedCameraViewSetting = getCameraViewSetting();
    }

    public void loadCameraView() {
        applyCameraView(savedCameraViewSetting);
    }

    public void enableDisablePan() {
        pan = !pan;
    }

    public void enableDisableCentering() {
        centering = !centering;
    }


}
