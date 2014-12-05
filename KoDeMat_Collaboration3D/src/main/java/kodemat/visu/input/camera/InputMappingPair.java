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
package kodemat.visu.input.camera;

import com.jme3.input.controls.Trigger;

/**
 *Datatype to hold a pair of keyInput - trigger that is passed to the inputManager
 * @author Kipouridis
 */
public class InputMappingPair {
    
    String mappingName = null;
    Trigger mappingTrigger = null;

       public InputMappingPair(String mappingName, Trigger mappingTrigger)
    {
        this.mappingName = mappingName;
        this.mappingTrigger = mappingTrigger;

    }
    
    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public Trigger getMappingTrigger() {
        return mappingTrigger;
    }

    public void setMappingTrigger(Trigger mappingTrigger) {
        this.mappingTrigger = mappingTrigger;
    }

  
 
    
}