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
package jade_layout.interfaces;

/**
 * Interface that should be implemented by those who wish to visualize the 
 * movements of agents.
 * @author Koshkabb
 */
public interface IBlackboardVisualizer {
    /**
     * Creates a component in the world.
     * @param modelName     The name of the component
     * @param translation   The position of the component
     * @param wp            The waypoint of the component
     */
    public void createComponent(String modelName, float translation, String wp);
     /**
     * Moves a component in the world.
     * @param modelName     The name of the component
     * @param translation   The position of the component
     * @param wp            The waypoint of the component
     */
    public void moveComponent(String modelName, float translation, String wp);
}
