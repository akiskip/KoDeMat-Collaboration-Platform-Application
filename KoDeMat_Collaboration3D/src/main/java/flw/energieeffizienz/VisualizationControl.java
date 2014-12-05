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

import com.hazelcast.core.HazelcastInstance;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.JmeSystem;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.visuComponents.VisuComponent;
import org.openide.util.Exceptions;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisualizationControl {

    TestTransportOrderWindow window;
    VisuHelper helper;
    HazelcastInstance hz;
    AssetManager assetManager = JmeSystem.newAssetManager(Thread.currentThread().getContextClassLoader().getResource("com/jme3/asset/Desktop.cfg"));
    VisuComponent eea = null;
    VisuComponent container = null;
    final Timer timer = new Timer();
    TimerTask deleteContainerIfTakenAwayTooLong = null;
    Map<String, Node> id2node = new HashMap<>();
    Map<String, String> franzke2feldhorst = new HashMap<>();
    Node eea_jme = (Node) assetManager.loadModel("CeMAT_Assets/EEA_FLW/EEA_FLW.j3o");
    boolean isInTransport = true;

    public VisualizationControl(VisuHelper helper, HazelcastInstance hz, TestTransportOrderWindow window) {
        this.helper = helper;
        this.hz = hz;
        this.window = window;
        window.setVisualizationControl(this);

        System.out.println("VisualizationControl created");

        List<VisuComponent> allComponents = helper.getAllComponents();

        for (VisuComponent vc : allComponents) {
            if (vc.getName().contains("EEA_FLW")) {
                eea = vc;
            }
        }

        if (eea != null) {
            System.out.println("Found EEA: " + eea.getName());
        } else {
            System.out.println("WARNING: Did not find EEA!!");
        }


        //System.out.println("EEA_FLW");
        //System.out.println("Class " + eea_jme.getClass().getSimpleName());


        //System.out.println("Model position " + eea_jme.getLocalTranslation() + " rot " + eea_jme.getLocalRotation());


        //System.out.println("Parent name " + eea_jme.getName());
        Node eea_node = (Node) eea_jme.getChild("EEA");

        //System.out.println("EEA position " + eea_node.getLocalTranslation() + " rot " + eea_node.getLocalRotation());

        List<Spatial> children = eea_node.getChildren();



        for (Spatial spatial : children) {
            if (spatial instanceof Node) {
                Node module = (Node) spatial;
                String name = module.getName();
                //System.out.print("Module " + name.toLowerCase());
                Node anchor = (Node) module.getChild("Node");
                if (anchor != null) {
                    id2node.put(name.toLowerCase().trim(), anchor);
                    //System.out.println(" pos " + anchor.getLocalTranslation() + " rot " + anchor.getLocalRotation());
                } else {
                    System.out.println("");
                }
            }
        }

//        container = this.createModel("EEA_Container", "CeMAT_Assets/Container/container.j3o");
//
//        container.setParent(eea.getId());
        //container.setTranslation(convert(id2node.get("c99").getWorldTranslation()));

        //setPositionEEAFeldhorst("c99");
        
        System.out.println("JME EEA Model read and positions mapped");

        Path file = Paths.get("./flw_eea_id_list.txt");
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

            for (String string : lines) {
                String[] split = string.split(" ");
                if (split.length >= 2) {
                    franzke2feldhorst.put(split[1].trim(), split[0].trim());
                }
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        System.out.println("Franzke and Feldhorst position names mapped");

    }

    private VisuComponent createModel(String modelName, String modelAssetPath) {
        VisuComponent childNode = helper.getComponent(modelName);
        if (childNode == null) {
            childNode = helper.createComponent(modelName);
            childNode.setIsSelectable(false);
            childNode.setType(new VisuType(VisuType.MODEL, modelAssetPath));
        }

        return childNode;
    }

    public void processEvent(String msg) {


//ipc14.04_11.Quer_Heber_3_B_Flag true
//ipc14.04_11.Quer_Heber_3_A_Flag false
//ipc14.04_11.LS_11 true
//ipc14.04_11.LS_10 false
//ipc14.04_11.Quer_Heber_3_A_Flag true
//ipc12.04_12.LS_03 true
//ipc14.04_11.Quer_Heber_3_B_Flag false
//Reader: 5152 Antenne:4 ID:e2003412dc03011931213794 Data:
//ipc14.04_11.LS_11 false
//ipc12.04_13.LS_04 true

        msg = msg.trim();

        if (msg.startsWith("ipc")) {
            String[] split = msg.split(" ");

            String idstring = split[0].trim();
            String value = split[1].trim();
            String[] split2 = idstring.split("\\.");

            System.out.println("idstring #" + idstring + "#");

            String primaryId = split2[1].trim();
            String secondaryId = split2[2].trim();

            if (secondaryId.startsWith("LS") && "true".equals(value)) {
                if (deleteContainerIfTakenAwayTooLong != null) {
                    deleteContainerIfTakenAwayTooLong.cancel();
                    deleteContainerIfTakenAwayTooLong = null;
                }


                String feldhorstId = franzke2feldhorst.get(primaryId);

                if (feldhorstId != null) {
                    setPositionEEAFeldhorst(feldhorstId);
                } else {
                    System.out.println("WARNING: franzkeId " + primaryId + " has no corresponding feldhorstId");
                }

            }

//            if ("04_05".equals(primaryId) && secondaryId.startsWith("LS") && "false".equals(value)) {
//                deleteContainer();
//            }
//
//            if ("04_10".equals(primaryId) && secondaryId.startsWith("LS") && "false".equals(value)) {
//                deleteContainer();
//            }

//            if (secondaryId.startsWith("LS") && "false".equals(value)) {
//                if (deleteContainerIfTakenAwayTooLong == null) {
//                    deleteContainerIfTakenAwayTooLong = new TimerTask() {
//                        @Override
//                        public void run() {
//                            deleteContainer();
//                            deleteContainerIfTakenAwayTooLong = null;
//                        }
//                    };
//                    timer.schedule(deleteContainerIfTakenAwayTooLong, 2000);
//                }
//            }

        }



    }

    public void setPositionEEAFeldhorst(String name) {
        Node n = id2node.get(name);
        if (container == null && eea != null) {
            container = this.createModel("EEA_Container", "CeMAT_Assets/Container/container.j3o");
            System.out.println("container id " + container.getId());
            System.out.println("eea id " + eea.getId());
            container.setParent(eea.getId());
        }

        if (n != null && container != null) {
            container.setTranslation(convert(n.getWorldTranslation()));
            container.setRotation(convert(n.getLocalRotation()));
            window.setContainerFeldhorstPos(name);
        }

    }

    public void shutdown() {
        deleteContainer();
        timer.cancel();
    }

    public void deleteContainer() {
        if (container != null) {
            helper.deleteComponent(container);
            container = null;
        }
    }

    public void setInTransport(boolean t) {
        isInTransport = t;
    }

    private VisuVector3f convert(Vector3f vec) {
        return new VisuVector3f(vec.x, vec.y, vec.z);
    }

    private VisuRotation convert(Quaternion q) {
        float[] a = q.toAngles(null);
        return new VisuRotation(FastMath.RAD_TO_DEG * a[0], FastMath.RAD_TO_DEG * a[1], FastMath.RAD_TO_DEG * a[2]);
    }
}
