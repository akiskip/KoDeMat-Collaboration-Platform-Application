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

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import kodemat.visu.input.camera.InputMappingPair;
import kodemat.visu.input.camera.KeyInputMapping;

/**
 *
 * @author Kipouridis
 */
public class JmeCamInputMapping extends KeyInputMapping{
     
    
    public JmeCamInputMapping(){
        super();

        if(super.getInputKeyMappings().size()>0)
            super.clearMappings();
        
        this.assignKeyMappings();
    }
    
    
    @Override
    public void assignKeyMappings() {
       mappingsMap.add(new InputMappingPair("MouseAxisX", new MouseAxisTrigger(MouseInput.AXIS_X,false)));
        mappingsMap.add(new InputMappingPair(
                "MouseAxisY", new MouseAxisTrigger(MouseInput.AXIS_Y, false)));
        mappingsMap.add(new InputMappingPair(
                "MouseAxisX-", new MouseAxisTrigger(MouseInput.AXIS_X, true)));
        mappingsMap.add(new InputMappingPair(
                "MouseAxisY-", new MouseAxisTrigger(MouseInput.AXIS_Y, true)));
        mappingsMap.add(new InputMappingPair(
                "MouseWheel", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false)));
        mappingsMap.add(new InputMappingPair(
                "MouseWheel-", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true)));
       mappingsMap.add(new InputMappingPair(
                "MouseButtonLeft", new MouseButtonTrigger(0)));
       mappingsMap.add(new InputMappingPair(
                "MouseButtonMiddle", new MouseButtonTrigger(2)));
        mappingsMap.add(new InputMappingPair(
                "MouseButtonRight", new MouseButtonTrigger(1)));
        mappingsMap.add(new InputMappingPair("StrafeLeft", new KeyTrigger(KeyInput.KEY_A)));
        mappingsMap.add(new InputMappingPair("StrafeRight", new KeyTrigger(KeyInput.KEY_D)));
      mappingsMap.add(new InputMappingPair("Forward", new KeyTrigger(KeyInput.KEY_W)));
       mappingsMap.add(new InputMappingPair("Backward", new KeyTrigger(KeyInput.KEY_S)));
       mappingsMap.add(new InputMappingPair("Rise", new KeyTrigger(KeyInput.KEY_Q)));
       mappingsMap.add(new InputMappingPair("Lower", new KeyTrigger(KeyInput.KEY_Y)));
        mappingsMap.add(new InputMappingPair("FocusMarker", new KeyTrigger(KeyInput.KEY_F)));
        mappingsMap.add(new InputMappingPair("SaveCameraViewSetting", new KeyTrigger(KeyInput.KEY_E)));
        mappingsMap.add(new InputMappingPair("RestoreCameraViewSetting", new KeyTrigger(KeyInput.KEY_R)));
       mappingsMap.add(new InputMappingPair("LeftControl", new KeyTrigger(KeyInput.KEY_LCONTROL)));       
        
    }
}
