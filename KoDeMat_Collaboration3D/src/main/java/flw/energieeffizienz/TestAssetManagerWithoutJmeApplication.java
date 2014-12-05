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

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.system.JmeSystem;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class TestAssetManagerWithoutJmeApplication {
    public static void main(String[] args) {
        AssetManager assetManager = null;
        assetManager = JmeSystem.newAssetManager(Thread.currentThread().getContextClassLoader().getResource("com/jme3/asset/Desktop.cfg"));
        Node tank = (Node) assetManager.loadModel("CeMAT_Assets/EEA_FLW/EEA_FLW.j3o");
        
        System.out.println("EEA_FLW");
        System.out.println("Class " + tank.getClass().getSimpleName());


        System.out.println("Model position " + tank.getLocalTranslation() + " rot " + tank.getLocalRotation());
    }
}
