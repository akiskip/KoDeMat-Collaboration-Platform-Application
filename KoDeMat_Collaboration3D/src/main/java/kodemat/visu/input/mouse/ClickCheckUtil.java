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

import com.jme3.scene.Spatial;
import kodemat.visu.input.camera.SavableUserDataWrapper;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class ClickCheckUtil {
    public static <T> T checkClick(ClickSelection selection, Class<T> clazz){
        if(selection != null && clazz != null){
            if(selection.getCollisionResult() != null && selection.getCollisionResult().getGeometry() != null){
                Spatial geo = selection.getCollisionResult().getGeometry();
                SavableUserDataWrapper<T> userData = null;
                while (geo != null){
                    userData = (SavableUserDataWrapper<T>) geo.getUserData(clazz.getName());
                    if (userData != null) {
                        return userData.getObject();
                    }
                    geo = geo.getParent();
                }
            }
        } 
        return null;
    }
}
