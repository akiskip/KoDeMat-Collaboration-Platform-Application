/* 
 * Copyright 2014 Orthodoxos Kipouridis and Moritz Roidl.
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
package kodemat.visudata.visuComponents;


import java.util.ArrayList;
import java.util.List;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuInterpolation;
import kodemat.visudata.VisuMarking;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuUndoDeleteData;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.change.VisuCreateComponent;
import kodemat.visudata.change.VisuDeleteComponent;
import kodemat.visudata.change.VisuUndoDelete;
import kodemat.visudata.command.VisuChangeCommand;

/**
 *Helper class for creating and deleting visucomponenten and updating the respective IMaps
 * @author Kipouridis
 */
public class VisuComponentHelper {
    private final VisuHelper helper;
    
    public VisuComponentHelper(VisuHelper visuHelper){
        this.helper = visuHelper;
    }
    
    
    //already copied for edge
    public VisuComponent createComponent(String name, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        VisuComponent vc = null;
        if (helper.getIds().get(name) != null) {
            deleteComponent(name);
        }
        try {
            Long newId = helper.getIdGenerator().newId();
            helper.getIds().put(name, newId);
            helper.getNames().put(newId, name);
            vc = new VisuComponent(name, helper, helper.getServerMode());
//            String changestr = helper.getVisuHistory().getString(helper.getUserLastName(), new VisuCreateComponent(newId, name), isUndo);
//            if (changestr != null) {
//                helper.getGlobalHistory().add(changestr);
//            }
     
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        return vc;
    }

    //already copied for edge
    public VisuComponent createComponent(Long id, String name, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        VisuComponent vc = null;
         if (helper.getIds().get(name) != null) {
            deleteComponent(name);
        }
        try {
            helper.getIds().put(name, id);
            helper.getNames().put(id, name);
            vc = new VisuComponent(name, helper, helper.getServerMode());
//            String changestr = helper.getVisuHistory().getString(helper.getUserLastName(), new VisuCreateComponent(id, name), isUndo);
//            if (changestr != null) {
//                helper.getGlobalHistory().add(changestr);
//            }
  
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        return vc;
    }

    //already copied for edge
    public void deleteComponent(String name, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        VisuUndoDeleteData vudd = deleteComponent(name, -1L);

        //TODO change the BB if to something more general
//        if (!vudd.name.contains("BoundingBox")) {
            VisuChangeCommand deleteCommand = new VisuChangeCommand(new VisuUndoDelete(vudd, vudd.id),helper.getClientUUID());
            helper.addToStack(deleteCommand, isUndo);
//            String changestr = helper.getVisuHistory().getString(helper.getUserLastName(), new VisuDeleteComponent(vudd.id), isUndo);
//            if (changestr != null) {
//               helper.getGlobalHistory().add(changestr);
//            }
//        }
    }

    public VisuUndoDeleteData deleteComponent(String name, Long next) {
        try {
            Long id = (Long) helper.getIds().get(name);

            helper.getNames().remove(id);
            VisuVector3f trans = (VisuVector3f) helper.getTranslations().remove(id);
            if (trans == null) {
                trans = new VisuVector3f(0, 0, 0);
            }
            VisuRotation rot = (VisuRotation) helper.getRotations().remove(id);
            if (rot == null) {
                rot = new VisuRotation(0, 0, 0);
            }
            VisuVector3f scale = (VisuVector3f) helper.getScales().remove(id);
            if (scale == null) {
                scale = new VisuVector3f(1, 1, 1);
            }
            String label = (String) helper.getLabels().remove(id);
            Long parent = (Long) helper.getParents().remove(id);
            if (parent == null) {
                parent = -1L;
            }
            VisuType vt = (VisuType) helper.getTypes().remove(id);
            if (vt == null) {
                vt = new VisuType();
            }
            VisuInterpolation vi = (VisuInterpolation) helper.getInterpolations().remove(id);
            if (vi == null) {
                vi = new VisuInterpolation(false, false, false);
            }
            VisuMarking vm = (VisuMarking) helper.getMarkings().remove(id);
            if (vm == null) {
                vm = new VisuMarking();
            }

            helper.getIds().remove(name);
            VisuUndoDeleteData vudd = new VisuUndoDeleteData(id, name, trans, rot, scale,
                    label, parent, vt, vi, vm, next);

            helper.getUndoDeletes().put(vudd.id, vudd);


            return vudd;
        } catch (Throwable t) {

            throw new RuntimeException(t);
        }
    }
    
        public VisuComponent getComponent(String name) {
        if (helper.getIds().get(name) != null) {
            return new VisuComponent(name, helper, helper.getServerMode());
        } else {
            return null;
        }
    }

    public VisuComponent getComponent(Long id) {
        if (helper.getNames().get(id) != null) {
            return new VisuComponent(id, helper, helper.getServerMode());
        } 
        else {
            return null;
        }
    }

    public List<VisuComponent> getAllComponents() {
        List<VisuComponent> list = new ArrayList<VisuComponent>(helper.getIds().size());
        List<Long> values = new ArrayList(helper.getIds().values());
        for (Long id : values) {
            list.add(getComponent(id));
        }
        return list;
    }
    
}
