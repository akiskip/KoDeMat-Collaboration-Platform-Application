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
package flw.energieeffizienz;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kodemat.visu.VisuScenegraphComponent;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class ReadOutAnchorPoints extends SimpleApplication {

    public static final String INPUT_MAPPING_NEXT = "ReadOutAnchorPoints_NEXT";
    Node container;
    Map<String, Node> id2node = new HashMap<>();

    public static void main(String[] args) {
        ReadOutAnchorPoints app = new ReadOutAnchorPoints();
        app.start();
    }

    @Override
    public void simpleInitApp() {


        if (getInputManager() != null) {

            inputManager.addMapping(INPUT_MAPPING_NEXT, new KeyTrigger(KeyInput.KEY_SPACE));

            inputManager.addListener(new KeyListener(),
                    INPUT_MAPPING_NEXT);

        }

        Node tank = (Node) assetManager.loadModel("CeMAT_Assets/EEA_FLW/EEA_FLW.j3o");
        container = (Node) assetManager.loadModel("CeMAT_Assets/Container/container.j3o");

        cam.setLocation(new Vector3f(0f, 8f, 15f));
        flyCam.setMoveSpeed(50f);

//        flyCam.setEnabled(true);
//        ChaseCamera chaseCam = new ChaseCamera(cam, tank, inputManager);
//        chaseCam.setSmoothMotion(true);
//        chaseCam.setMaxDistance(100000);
//        chaseCam.setMinVerticalRotation(-FastMath.PI / 2);
//        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
//
////        Geometry tankGeom = (Geometry) tank;
////        LodControl control = new LodControl();
////        tankGeom.addControl(control);
//        rootNode.attachChild(tank);
//
        Vector3f lightDir = new Vector3f(-0.8719428f, -0.46824604f, 0.14304268f);
        DirectionalLight dl = new DirectionalLight();
        dl.setColor(new ColorRGBA(1.0f, 0.92f, 0.75f, 1f));
        dl.setDirection(lightDir);

        Vector3f lightDir2 = new Vector3f(0.70518064f, 0.5902297f, -0.39287305f);
        DirectionalLight dl2 = new DirectionalLight();
        dl2.setColor(new ColorRGBA(0.7f, 0.85f, 1.0f, 1f));
        dl2.setDirection(lightDir2);

        rootNode.addLight(dl);
        rootNode.addLight(dl2);
//        rootNode.attachChild(tank);

        // create the geometry and attach it
//        Geometry teaGeom = (Geometry) assetManager.loadModel("Models/Teapot/Teapot.obj");

        // show normals as material
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
//        tank.setMaterial(mat);

        rootNode.attachChild(tank);


        System.out.println("EEA_FLW");
        System.out.println("Class " + tank.getClass().getSimpleName());


        System.out.println("Model position " + tank.getLocalTranslation() + " rot " + tank.getLocalRotation());


        System.out.println("Parent name " + tank.getName());
        Node eea = (Node) tank.getChild("EEA");

        System.out.println("EEA position " + eea.getLocalTranslation() + " rot " + eea.getLocalRotation());

        List<Spatial> children = eea.getChildren();



        for (Spatial spatial : children) {
            if (spatial instanceof Node) {
                Node module = (Node) spatial;
                String name = module.getName();
                System.out.print("Module " + name.toLowerCase());
                Node anchor = (Node) module.getChild("Node");
                if (anchor != null) {
                    id2node.put(name.toLowerCase().trim(), anchor);
                    System.out.println(" pos " + anchor.getLocalTranslation() + " rot " + anchor.getLocalRotation());
                } else {
                    System.out.println("");
                }
            }
        }

        container.setLocalTranslation(id2node.get("c01").getWorldTranslation());
        container.setLocalRotation(id2node.get("c01").getLocalRotation());

        rootNode.attachChild(container);

        cam.lookAt(container.getWorldTranslation(), Vector3f.UNIT_Y);
//        cam.lookAt(globalAnchorTest.getWorldTranslation(), Vector3f.UNIT_Y);
    }

    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);





    }

    class KeyListener implements ActionListener {

        DecimalFormat nf = new DecimalFormat("00");
        int counter = 1;

        @Override
        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }

            if (name.equals(INPUT_MAPPING_NEXT)) {

                String target = "c" + nf.format(counter++);

                System.out.println(target);

                Node n = id2node.get(target);

                if (n != null) {
                    container.addControl(new TranslationInterpolationControl(n.getWorldTranslation(), 1f));
                    container.addControl(new RotationInterpolationControl(n.getLocalRotation(), 1f));
                    
//                    container.setLocalTranslation(n.getWorldTranslation());
//                    container.setLocalRotation(n.getLocalRotation());
                }
                
                if (counter > 112){
                    counter = 1;
                }

            }

        }
    }
    
     class TranslationInterpolationControl extends AbstractControl {

        Vector3f source;
        Vector3f target;
        final float duration;
        float current = 0f;
        boolean started = false;

        public TranslationInterpolationControl(Vector3f target, float duration) {
            super();
            this.target = target;
            this.duration = duration;

        }

        public void setTarget(Vector3f target) {
            this.target = target;
            source = spatial.getLocalTranslation().clone();
            current = 0f;
        }

        @Override
        protected void controlUpdate(float tpf) {
            if (!started) {
                source = spatial.getLocalTranslation().clone();
                started = true;
            }
            if (current < duration) {
                float percentage = current / duration;
                Vector3f interpolation = new Vector3f();//spatial.getLocalTranslation().clone();
                interpolation.interpolate(source, target, percentage);
                spatial.setLocalTranslation(interpolation);
                current += tpf;
            } else {
                this.spatial.removeControl(this);
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }

        @Override
        public Control cloneForSpatial(Spatial spatial) {
            return new ReadOutAnchorPoints.TranslationInterpolationControl(target.clone(), duration);
        }
    }

    class RotationInterpolationControl extends AbstractControl {

        Quaternion source;
        Quaternion target;
        final float duration;
        float current = 0f;
        boolean started = false;

        public RotationInterpolationControl(Quaternion target, float duration) {
            super();
            this.target = target;
            this.duration = duration;

        }

        public void setTarget(Quaternion target) {
            this.target = target;
            source = spatial.getLocalRotation().clone();
            current = 0f;
        }

        @Override
        protected void controlUpdate(float tpf) {
            if (!started) {
                source = spatial.getLocalRotation().clone();
                started = true;
            }
            if (current < duration) {
                float percentage = current / duration;
                spatial.setLocalRotation(spatial.getLocalRotation().slerp(source, target, percentage));
                current += tpf;
            } else {
                this.spatial.removeControl(this);
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }

        @Override
        public Control cloneForSpatial(Spatial spatial) {
            return new ReadOutAnchorPoints.RotationInterpolationControl(target.clone(), duration);
        }
    }
}
