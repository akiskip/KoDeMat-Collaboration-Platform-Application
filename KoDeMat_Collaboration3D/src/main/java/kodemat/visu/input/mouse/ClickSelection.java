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
package kodemat.visu.input.mouse;

import com.jme3.collision.CollisionResult;
import com.jme3.scene.Spatial;
import kodemat.visudata.visuComponents.VisuComponent;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class ClickSelection<T> {
    private CollisionResult collisionResult = null;
    private Spatial spatial;
    private int clickCount = 1;
    private boolean special = false;
    private boolean right = false;
    private T component = null;

    /**
     * @return the collisionResult
     */
    public CollisionResult getCollisionResult() {
        return collisionResult;
    }

    /**
     * @param collisionResult the collisionResult to set
     */
    public void setCollisionResult(CollisionResult collisionResult) {
        this.collisionResult = collisionResult;
    }

    /**
     * @return the component
     */
    public T getComponent() {
        return component;
    }

    /**
     * @param component the component to set
     */
    public void setComponent(T component) {
        this.component = component;
    }

    /**
     * @return the clickCount
     */
    public int getClickCount() {
        return clickCount;
    }

    /**
     * @param clickCount the clickCount to set
     */
    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    /**
     * @return the special
     */
    public boolean isSpecial() {
        return special;
    }
    
    public boolean isRight(){
        return right;
    }

    /**
     * @param special the special to set
     */
    public void setSpecial(boolean special) {
        this.special = special;
    }
    
    public void setRight(boolean right){
        this.right = right;
    }

    /**
     * @return the spatial
     */
    public Spatial getSpatial() {
        return spatial;
    }

    /**
     * @param spatial the spatial to set
     */
    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }
    
    
}
