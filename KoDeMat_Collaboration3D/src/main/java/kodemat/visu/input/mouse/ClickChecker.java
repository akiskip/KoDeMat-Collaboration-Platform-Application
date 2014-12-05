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

import kodemat.visu.gui.ClickHandlerCameraController;
import com.jme3.collision.CollisionResult;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import kodemat.visu.input.camera.controller.AbstractClickHandlerCameraController;
import kodemat.visu.input.camera.SavableUserDataWrapper;
import kodemat.visu.input.camera.controller.AbstractCameraController;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class ClickChecker implements Runnable {

    AbstractClickHandlerCameraController controller;
    CollisionResult result;
    private CollisionResult dragOrigin;
    protected List<ClickMapping> clickMappings = new ArrayList<ClickMapping>();
    int clickCount = 1;
    boolean special = false;
    boolean right = false;

    public ClickChecker(AbstractClickHandlerCameraController controller, CollisionResult result, CollisionResult dragOrigin, List<ClickMapping> clickMappings, int clickCount, boolean special) {
        this.controller = controller;
        this.result = result;
        this.dragOrigin = dragOrigin;
        this.clickMappings = clickMappings;
        this.clickCount = clickCount;
        this.special = special;
    }
        public ClickChecker(AbstractClickHandlerCameraController controller, CollisionResult result, CollisionResult dragOrigin, List<ClickMapping> clickMappings, int clickCount, boolean special, boolean right) {
        this.controller = controller;
        this.result = result;
        this.dragOrigin = dragOrigin;
        this.clickMappings = clickMappings;
        this.clickCount = clickCount;
        this.special = special;
        this.right = right;
    }

    @Override
    public void run() {
        ClickSelection dragOriginSelection = new ClickSelection();
        if (dragOrigin != null && dragOrigin.getGeometry() != null) {
            dragOriginSelection.setCollisionResult(dragOrigin);
            Spatial geometry = dragOrigin.getGeometry();
            Object userData = null;
            ClickMapping usedMapping = null;
            outer:
            while (geometry != null && userData == null && clickMappings.size() > 0) {
                for (ClickMapping clickMapping : clickMappings) {
                    usedMapping = clickMapping;
//                    Logger.getLogger(ClickChecker.class.getName()).log(Level.INFO, "geo {0} clickMapping {1} userDataKey {2}", new Object[]{geometry, clickMapping, (clickMapping != null ? clickMapping.userDataKey : null)});
                    userData = geometry.getUserData(clickMapping.userDataKey.getName());

//                    Logger.getLogger(ClickChecker.class.getName()).log(Level.INFO, "userdata dragorigin {0} : all keys {1}", new Object[]{userData, geometry.getUserDataKeys()});
                    if (userData != null) {
                        break outer;
                    }
                }
                geometry = geometry.getParent();
                usedMapping = null;
            }

            if (userData != null) {
                SavableUserDataWrapper wrapper = (SavableUserDataWrapper) userData;
                dragOriginSelection.setComponent(wrapper.getObject());
                dragOriginSelection.setSpatial(geometry);
            }
        }
    
        controller.setDragOriginSelection(dragOriginSelection);    
        



        ClickSelection selection = new ClickSelection();
        selection.setClickCount(clickCount);
        selection.setSpecial(special);
        selection.setRight(right);

        if (result != null && result.getGeometry() != null) {
            selection.setCollisionResult(result);

            Spatial geometry = result.getGeometry();
            Object userData = null;
            ClickMapping usedMapping = null;
            outer:
            while (geometry != null && userData == null && clickMappings.size() > 0) {
                for (ClickMapping clickMapping : clickMappings) {
                    usedMapping = clickMapping;
//                    Logger.getLogger(ClickChecker.clas s.getName()).log(Level.INFO, "geo {0} clickMapping {1} userDataKey {2}", new Object[]{geometry, clickMapping, (clickMapping != null ? clickMapping.userDataKey : null)});
                    userData = geometry.getUserData(clickMapping.userDataKey.getName());
                    if (userData != null) {
                        break outer;
                    }
                }
                geometry = geometry.getParent();
                usedMapping = null;
            }

            if (userData != null) {
                SavableUserDataWrapper wrapper = (SavableUserDataWrapper) userData;
                selection.setComponent(wrapper.getObject());
                selection.setSpatial(geometry);
                usedMapping.handleClick(selection);
            } else {
                if (controller.getDefaultHandler() != null) {
                    controller.getDefaultHandler().handleClick(selection);
                }
            }
        } else {
            selection.setCollisionResult(result);
            if (controller.getDefaultHandler() != null) {
                controller.getDefaultHandler().handleClick(selection);
            }
        }
        controller.setCurrentSelection(selection);


    }
}
