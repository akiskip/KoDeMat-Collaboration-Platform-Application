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

import com.hazelcast.core.IMap;
import com.hazelcast.query.EntryObject;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;
import kodemat.versioning.visuHistory.VisuHistoryEvent;
import kodemat.versioning.visuHistory.VisuHistoryRecordUtil;
import kodemat.visudata.VisuChange;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuInterpolation;
import kodemat.visudata.VisuMarking;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.change.VisuLabelChange;
import kodemat.visudata.change.VisuMarkingChange;
import kodemat.visudata.change.VisuParentChange;
import kodemat.visudata.change.VisuTypeChange;
import kodemat.visudata.command.VisuChangeCommand;

/**
 *
 * @author Kipouridis
 */
public abstract class AbstractCompositeComponent implements IComponent {

    /**
     * The global id of the component, used in the Components IMAP
     */
    public final long id;
    /**
     * Variable denoting if the component belongs to a server hazelcast instance
     * and should not be versioned
     */
    public final boolean serverMode;
    private ArrayList<AbstractCompositeComponent> childrenComponents = new ArrayList<AbstractCompositeComponent>(); //Childen Components added as meta data
    /**
     * The VisuHelper class belonging to the respective Hazelcast client
     */
    public final VisuHelper helper;
    public final String name;
    //TODO: remove with notes revision
    public ArrayList<String> notes = new ArrayList<String>(); //history of notes for this model
    final IMap<Integer, VisuHistoryEvent> visuHistoryMap;
    final IMap<Integer, VisuComponentInfoEntry> visuInfoMap;

    public AbstractCompositeComponent(String name, VisuHelper helper, boolean serverMode) {
        this.name = name;
        this.helper = helper;
        this.serverMode = serverMode;
        this.id = (long) helper.getIds().get(name);


        visuHistoryMap = helper.getClient().getMap("history_" + id);
        visuInfoMap = helper.getClient().getMap("info_" + id);

        if (helper.getNotes().get(id) != null) {
            notes = (ArrayList<String>) helper.getNotes().get(id);
        }
        //check if there is already a history of this object in the map

    }

    public IMap<Integer, VisuComponentInfoEntry> getVisuInfoMap() {
        return visuInfoMap;
    }

    public IMap<Integer, VisuHistoryEvent> getHistoryMap() {
        return visuHistoryMap;
    }

    public AbstractCompositeComponent(Long id, VisuHelper helper, boolean serverMode) {
        this.name = (String) helper.getNames().get(id);
        this.helper = helper;
        this.serverMode = serverMode;
        this.id = id;
        visuHistoryMap = helper.getClient().getMap("history_" + id);
        visuInfoMap = helper.getClient().getMap("info_" + id);

        if (helper.getNotes().get(id) != null) {
            notes = (ArrayList<String>) helper.getNotes().get(id);
        }
    }

    @Override
    public VisuHelper getHelper() {
        return helper;
    }

    @Override
    public boolean isServerMode() {
        return serverMode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getId() {
        return id;
    }

    /**
     * Set the label for this component. TODO: Consider making the method
     * abstract so it can be override by visu and non visu components
     *
     * @param lbl
     * @param param
     */
    @Override
    public void setLabel(String lbl, Boolean... param) {

        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;

        if (serverMode) {
            if (lbl == null) {
                helper.getLabels().remove(id);
            } else {
                helper.getLabels().put(id, lbl);
            }
        } else {
            //removed it for now, since we use it for marking the selection, should be reenabled probably
//            addChangeHistory(new VisuLabelChange(lbl, id));
            //TODO: what was the use of checking the "locked" string in the label?? I removed it for now
//            if (getLabel() != null && (!getLabel().contains("Locked") || !lbl.contains("Locked"))) {
            if (getLabel() != null) {
                VisuChangeCommand currentLabel = new VisuChangeCommand(new VisuLabelChange(getLabel(), id), helper.getClientUUID());
                helper.addToStack(currentLabel, isUndo);
            }
            helper.getCommands().offer(new VisuChangeCommand(new VisuLabelChange(lbl, id), helper.getClientUUID()));
        }
    }

    @Override
    public String getLabel() {

        return (String) helper.getLabels().get(id);
    }

    @Override
    public void setParent(Long pid, Boolean... param) {

        if (id == pid) {
            throw new IllegalArgumentException("Setting itself as a parent is not allowed, id= " + id);
        }
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        if (serverMode) {
            if (pid == null) {
                helper.getParents().remove(id);
            } else {
                helper.getParents().put(id, pid);
            }
        } else {
            //VisuChangeCommand currentParent = new VisuChangeCommand(id, new VisuParentChange(getParent(), id));
            //addToStack(currentParent, isUndo);
            addChangeHistory(new VisuParentChange(pid, id));
            //if a component has itself as a parent, there will be problems in the jme scenegraph
            //so, we throw a runtime exception
            helper.getCommands().offer(new VisuChangeCommand(new VisuParentChange(pid, id), helper.getClientUUID()));
        }
    }

    @Override
    public Long getParent() {
        return (Long) helper.getParents().get(id);
    }

    @Override
    public void setType(VisuType t, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        if (serverMode) {
            if (t == null) {
                helper.getTypes().remove(id);
            } else {
                helper.getTypes().put(id, t);
            }
        } else {
            //   VisuChangeCommand currentType = new VisuChangeCommand(id, new VisuTypeChange(getType(), id));
            //  addToStack(currentType, isUndo);
            addChangeHistory(new VisuTypeChange(t, id));
            helper.getCommands().offer(new VisuChangeCommand(new VisuTypeChange(t, id), helper.getClientUUID()));
        }
    }

    @Override
    public VisuType getType() {

        return (VisuType) helper.getTypes().get(id);
    }

    @Override
    public void setMarking(VisuMarking vm) {
        if (serverMode) {
            if (vm == null) {
                helper.getMarkings().remove(id);
            } else {
                helper.getMarkings().put(id, vm);
            }
        } else {
            //not sure if we need this in history
//            addChangeHistory(new VisuMarkingChange(vm, id));
            if (getMarking() != null) {
                VisuChangeCommand markingLabel = new VisuChangeCommand(new VisuMarkingChange(getMarking(), id), helper.getClientUUID());
                helper.addToStack(markingLabel, false);
            }
            helper.getCommands().offer(new VisuChangeCommand(new VisuMarkingChange(vm, id), helper.getClientUUID()));
        }
    }

    @Override
    public VisuMarking getMarking() {
        return (VisuMarking) helper.getMarkings().get(id);
    }

    /**
     * Add a string denoting the change event. It should be revised
     *
     * @param change
     */
    public void addChangeHistory(VisuChange change) {
        if (change != null && !serverMode) {
            //only the clients that are not in server mode should be storing the changes in the history stack
//                System.out.println("add history to selected id "+id);          
            Integer eventId = getHistoryMap().size();
            VisuHistoryEvent historyEvent = new VisuHistoryEvent(eventId, helper.getUserLastName(), VisuHistoryRecordUtil.getString(helper.getUserLastName(), this.getName(), change));
            getHistoryMap().put(eventId, historyEvent);

        }
    }

    /**
     * Add a string denoting the change event. It should be revised
     *
     * @param change
     */
    public void addInfo(String attribute, String value) {
        if (!serverMode) {
            //only the clients that are not in server mode should be storing the changes in the history stack

            Integer eventId = getVisuInfoMap().size();
            VisuComponentInfoEntry infoEntry = new VisuComponentInfoEntry(eventId, attribute, value);
            getVisuInfoMap().put(eventId, infoEntry);
//                helper.getCompoenentInfo().put(id, componentInfo);     
        }
    }

    /**
     * Set an existing attribute (name) of the info map to a specific value. It
     * should be revised
     *
     * @param change
     */
    public void setInfo(String attributeName, String value) {
        if (!serverMode) {
            //only the clients that are not in server mode should be storing the changes in the history stack

            for (Map.Entry<Integer, VisuComponentInfoEntry> entry : getVisuInfoMap().entrySet()) {
                if (entry.getValue().getName().contains(attributeName)) {
                    getVisuInfoMap().put(entry.getKey(), new VisuComponentInfoEntry(entry.getKey(), attributeName, value));
                }
            }

//                helper.getCompoenentInfo().put(id, componentInfo);     
        }
    }

    public String getLastHistoryChange() {
        return visuHistoryMap.get(visuHistoryMap.size() - 1).getValue();

    }

    /**
     * Returns a list of all child components by querying the parent attributes
     * of other components.
     *
     * @return the list of child components
     */
    public List<IComponent> getChildren() {
        EntryObject e = new PredicateBuilder().getEntryObject();
        Predicate predicate = e.get("value").equal(getId());
        Set<Map.Entry<Long, Long>> childrenIds = (Set<Map.Entry<Long, Long>>) helper.getParents().entrySet(predicate);
        List<IComponent> childComponents = new ArrayList<>(childrenIds.size());
        for (Map.Entry<Long, Long> entry : childrenIds) {
            childComponents.add(helper.getComponent(entry.getKey()));
        }
        return childComponents;
    }

    /**
     * Set the local translation of the Component. This traslation will be
     * synchronized through the distributed IMAPs in hazelcast
     *
     * @param vec the VisuVector3f coordinates of the new position
     * @param param
     */
    public abstract void setTranslation(VisuVector3f vec, Boolean... param);

    public abstract VisuVector3f getTranslation();

    public abstract void setRotation(VisuRotation rot, Boolean... param);

    public abstract VisuRotation getRotation();

    public abstract void setScale(VisuVector3f vec, Boolean... param);

    public abstract VisuVector3f getScale();

    public abstract void setInterpolation(VisuInterpolation vi, Boolean... param);

    public abstract VisuInterpolation getInterpolation();

    public abstract void applyChange(VisuChange change, Boolean... param);
    //TODO: revise the class according to the composite patern
//    public abstract List<IComponent> getChildren();
}
