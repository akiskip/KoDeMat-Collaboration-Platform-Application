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
package kodemat.visudata;

import kodemat.visudata.change.VisuEdgeSourceChange;
import kodemat.visudata.change.VisuEdgeTargetChange;
import kodemat.visudata.command.VisuChangeCommand;

/**
 * 
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuEdge {

    private final boolean serverMode;

    VisuEdge(String name, VisuHelper helper, boolean serverMode) {
        this.name = name;
        this.helper = helper;
        this.serverMode = serverMode;
        this.id = (long) helper.edgeIds.get(name);
    }

    VisuEdge(Long id, VisuHelper helper, boolean serverMode) {
        this.name = (String) helper.edgeNames.get(id);
        this.helper = helper;
        this.serverMode = serverMode;
        this.id = id;
    }
    private final VisuHelper helper;
    private final String name;
    private final long id;

    public VisuHelper getHelper() {
        return helper;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setSource(Long pid, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        //VisuChangeCommand currentParent = new VisuChangeCommand(id, new VisuParentChange(getParent(), id));
        //addToStack(currentParent, isUndo);
        if (serverMode) {
            if (pid == null) {
                helper.edgeSources.remove(id);
            } else {
                helper.edgeSources.put(id, pid);
            }
        } else {
            helper.commands.offer(new VisuChangeCommand(new VisuEdgeSourceChange(pid, id),helper.getClientUUID()));
        }
    }

    public Long getSource() {
        return (Long) helper.edgeSources.get(id);
    }

    public void setTarget(Long pid, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        //VisuChangeCommand currentParent = new VisuChangeCommand(id, new VisuParentChange(getParent(), id));
        //addToStack(currentParent, isUndo);
        if (serverMode) {
            if (pid == null) {
                helper.edgeTargets.remove(id);
            } else {
                helper.edgeTargets.put(id, pid);
            }
        } else {
            helper.commands.offer(new VisuChangeCommand(new VisuEdgeTargetChange(pid, id),helper.getClientUUID()));
        }
    }

    public Long getTarget() {
        return (Long) helper.edgeTargets.get(id);
    }

    public void applyChange(VisuChange change, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;

        if (change instanceof VisuEdgeSourceChange) {
            VisuEdgeSourceChange vesc = (VisuEdgeSourceChange) change;
            if (vesc.getValue() != null) {
                setSource(vesc.getValue(), isUndo);
            }
        }
        if (change instanceof VisuEdgeTargetChange) {
            VisuEdgeTargetChange vetc = (VisuEdgeTargetChange) change;
            if (vetc.getValue() != null) {
                setTarget(vetc.getValue(), isUndo);
            }
        }
    }
}
