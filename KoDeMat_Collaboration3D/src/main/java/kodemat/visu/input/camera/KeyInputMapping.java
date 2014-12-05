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
import java.util.ArrayList;

/**
 *Abstract class for assigning key inputs. The classes that extend it should consume the 
 * assignKeyMappings mathod
 * @author Kipouridis
 */
public  abstract class KeyInputMapping implements IKeyInputMapping {
    protected  ArrayList<InputMappingPair> mappingsMap;
    
    
    public KeyInputMapping(){
        mappingsMap = new ArrayList<InputMappingPair>();

    }
    

    
    /**
     * Setup the key mapping for the key inputs
     */
 abstract public void assignKeyMappings();


    @Override
    public ArrayList<InputMappingPair> getInputKeyMappings() {
        return this.mappingsMap;
    }

    @Override
    public ArrayList<InputMappingPair> addKeyMapping(InputMappingPair inputMapping) {

        this.mappingsMap.add(inputMapping);
    
        return this.getInputKeyMappings();
    }


    @Override
    public void clearMappings() {
       this.mappingsMap = new ArrayList<InputMappingPair>();;
    }


    @Override
    public  String[] getMappingstoString() {

       String[] mappingNames = new String[this.mappingsMap.size()];
       
       for(int i= 0; i<mappingsMap.size();i++){
       mappingNames[i] = mappingsMap.get(i).getMappingName();
               }
           
       return mappingNames;
        
    }

 
    
}
