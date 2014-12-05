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
 * Interface for defining the methods that an input manager class (e.g
 * camInputManager) should implement
 *
 * @author Kipouridis
 */
public interface IKeyInputMapping {


    public ArrayList<InputMappingPair> getInputKeyMappings();

    /**
     * Add a new input to the InputMappings List, caution if it is called during
     * runntime the listeners should be updated by calling the update method
     *
     * @param inputMapping
     */
    public ArrayList<InputMappingPair> addKeyMapping(InputMappingPair inputMapping);


    public void clearMappings();
    
    public String[] getMappingstoString();
}
