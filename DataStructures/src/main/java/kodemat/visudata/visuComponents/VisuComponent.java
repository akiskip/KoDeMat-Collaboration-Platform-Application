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

import com.hazelcast.query.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import kodemat.tele.teledata.AbstractDistributableField;
import kodemat.versioning.visuHistory.VisuHistoryEvent;
import kodemat.visudata.VisuChange;
import kodemat.visudata.VisuEdge;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuInterpolation;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.change.VisuAddChangeHistory;
import kodemat.visudata.change.VisuInterpolationChange;
import kodemat.visudata.change.VisuLabelChange;
import kodemat.visudata.change.VisuMarkingChange;
import kodemat.visudata.change.VisuParentChange;
import kodemat.visudata.change.VisuReferenceToEdgeChange;
import kodemat.visudata.change.VisuRotationChange;
import kodemat.visudata.change.VisuScaleChange;
import kodemat.visudata.change.VisuTranslationChange;
import kodemat.visudata.change.VisuTypeChange;
import kodemat.visudata.command.VisuChangeCommand;
import kodemat.visudata.command.VisuChangeToWorldPosition;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuComponent extends AbstractCompositeComponent {

    //Set if the component can be selected as a result of user clicking 
    private boolean isSelectable = true;

    public VisuComponent(String name, VisuHelper helper, boolean serverMode) {
        super(name, helper, serverMode);
    }

    public VisuComponent(Long id, VisuHelper helper, boolean serverMode) {
        super(id, helper, serverMode);
    }

    @Override
    public void setTranslation(VisuVector3f vec, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;


        if (serverMode) {
            if (vec == null) {
                helper.getTranslations().remove(id);
            } else {
                helper.getTranslations().put(id, vec);
            }
        } else {
            addChangeHistory(new VisuTranslationChange(vec, id));

            VisuChangeCommand currentTrans = new VisuChangeCommand(new VisuTranslationChange(getTranslation(), id), helper.getClientUUID());
            helper.addToStack(currentTrans, isUndo);
            helper.getCommands().offer(new VisuChangeCommand(new VisuTranslationChange(vec, id), helper.getClientUUID()));
        }
    }

    @Override
    public VisuVector3f getTranslation() {
        return (VisuVector3f) helper.getTranslations().get(id);
    }

    @Override
    public void setRotation(VisuRotation rot, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;


        if (serverMode) {
            if (rot == null) {
                helper.getRotations().remove(id);
            } else {
                helper.getRotations().put(id, rot);
            }
        } else {
            addChangeHistory(new VisuRotationChange(rot, id));

            VisuChangeCommand currentRot;
            if (getRotation() != null) {
                currentRot = new VisuChangeCommand(new VisuRotationChange(getRotation(), id), helper.getClientUUID());
                helper.addToStack(currentRot, isUndo);
            }
            helper.getCommands().offer(new VisuChangeCommand(new VisuRotationChange(rot, id), helper.getClientUUID()));
        }
    }

    @Override
    public VisuRotation getRotation() {
        return (VisuRotation) helper.getRotations().get(id);
    }

    @Override
    public void setScale(VisuVector3f vec, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;

        if (serverMode) {
            if (vec == null) {
                helper.getScales().remove(id);
            } else {
                helper.getScales().put(id, vec);
            }
        } else {
           addChangeHistory(new VisuScaleChange(vec, id));

            VisuChangeCommand currentScale;
            if (getScale() != null) {
                currentScale = new VisuChangeCommand(new VisuScaleChange(getScale(), id), helper.getClientUUID());
                helper.addToStack(currentScale, isUndo);
            }
            helper.getCommands().offer(new VisuChangeCommand(new VisuScaleChange(vec, id), helper.getClientUUID()));
        }
    }

    @Override
    public VisuVector3f getScale() {
        return (VisuVector3f) helper.getScales().get(id);
    }

    @Override
    public void setInterpolation(VisuInterpolation vi, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;


        if (serverMode) {
            if (vi == null) {
                helper.getInterpolations().remove(id);
            } else {
                helper.getInterpolations().put(id, vi);
            }
        } else {
            VisuChangeCommand currentInterpolation = new VisuChangeCommand(new VisuInterpolationChange(getInterpolation(), id), helper.getClientUUID());
            helper.addToStack(currentInterpolation, isUndo);
            addChangeHistory(new VisuInterpolationChange(vi, id));
            helper.getCommands().offer(new VisuChangeCommand(new VisuInterpolationChange(vi, id), helper.getClientUUID()));
        }
    }

    @Override
    public VisuInterpolation getInterpolation() {
        return (VisuInterpolation) helper.getInterpolations().get(id);
    }

    /**
     * This attribute will control weather an scenegraph object will be
     * positioned on an edge. If this attribute is set then the parent attribute
     * will be ignored and the spatial of the component will be connected to the
     * root node. Its position should then be controlled over the x value of the
     * translation attribute. A value of 0 means position at the source of the
     * edge, a value of 1 means a position on the target of the edge, adn
     * anything in between will be an interpolation between these two
     * positions.F
     *
     * @param edgeId
     * @param param
     */
    public void setReferenceToEdge(Long edgeId, Boolean... param) {

        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        //VisuChangeCommand currentParent = new VisuChangeCommand(id, new VisuParentChange(getParent(), id));
        //addToStack(currentParent, isUndo);
        //addChangeHistory(visuHistory.getString("user", new VisuParentChange(pid, id)));

        if (serverMode) {
            if (edgeId == null) {
                helper.getReferencesToEdges().remove(id);
            } else {
                helper.getReferencesToEdges().put(id, edgeId);
            }
        } else {
            helper.getCommands().offer(new VisuChangeCommand(new VisuReferenceToEdgeChange(edgeId, id), helper.getClientUUID()));
        }
    }

    /**
     * This attribute will control weather an scenegraph object will be
     * positioned on an edge. If this attribute is set then the parent attribute
     * will be ignored and the spatial of the component will be connected to the
     * root node. Its position should then be controlled over the x value of the
     * translation attribute. A value of 0 means position at the source of the
     * edge, a value of 1 means a position on the target of the edge, adn
     * anything in between will be an interpolation between these two positions.
     */
    public Long getReferenceToEdge() {
        return (Long) helper.getReferencesToEdges().get(id);
    }

    public void changeToWorldPosition() {
        helper.getCommands().offer(new VisuChangeToWorldPosition(id));
    }



    public String getLastNote() {
        if (notes.size() > 0) {
            return notes.get(notes.size() - 1);
        }
        return null;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }
    public String getNote() {
        return (String) helper.getNote().get(id);
    }

    public List<VisuEdge> getIncomingEdges() {
        List<VisuEdge> list = new ArrayList<VisuEdge>();
        Set<Long> incomingEdges = helper.getEdgeTargets().keySet(new Predicate<Long, Long>() {
            @Override
            public boolean apply(Entry<Long, Long> entry) {
                return entry.getValue().equals(id);
            }
        });

        for (Long id : incomingEdges) {
            list.add(helper.getEdge(id));
        }
        return list;
    }

    public List<VisuEdge> getOutgoingEdges() {
        List<VisuEdge> list = new ArrayList<VisuEdge>();
        Set<Long> outgoingEdges = helper.getEdgeSources().keySet(new Predicate<Long, Long>() {
            @Override
            public boolean apply(Entry<Long, Long> entry) {
                return entry.getValue().equals(id);
            }
        });

        for (Long id : outgoingEdges) {
            list.add(helper.getEdge(id));
        }
        return list;
    }

    @Override
    public void applyChange(VisuChange change, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        //if (serverMode) {
        if (change instanceof VisuTranslationChange) {
            VisuTranslationChange vtc = (VisuTranslationChange) change;
            if (vtc.getValue() != null) {
                setTranslation(vtc.getValue(), isUndo);
            }
        }
        if (change instanceof VisuRotationChange) {
            VisuRotationChange vrc = (VisuRotationChange) change;
            if (vrc.getValue() != null) {
                setRotation(vrc.getValue(), isUndo);
            }
        }
        if (change instanceof VisuScaleChange) {
            VisuScaleChange vsc = (VisuScaleChange) change;
            setScale(vsc.getValue(), isUndo);
        }
        if (change instanceof VisuLabelChange) {
            VisuLabelChange vlc = (VisuLabelChange) change;
            if (vlc.getValue() != null) {
                setLabel(vlc.getValue(), isUndo);
            }
        }
        if (change instanceof VisuParentChange) {
            VisuParentChange vpc = (VisuParentChange) change;
            if (vpc.getValue() != null) {
                setParent(vpc.getValue(), isUndo);
            }
        }
        if (change instanceof VisuTypeChange) {
            VisuTypeChange vtc = (VisuTypeChange) change;
            if (vtc.getValue() != null) {

                setType(vtc.getValue());
            }
        }
        if (change instanceof VisuInterpolationChange) {
            VisuInterpolationChange vic = (VisuInterpolationChange) change;
            if (vic.getValue() != null) {
                setInterpolation(vic.getValue(), isUndo);
            }
        }
        if (change instanceof VisuMarkingChange) {
            VisuMarkingChange vmc = (VisuMarkingChange) change;
            if (vmc.getValue() != null) {
                setMarking(vmc.getValue());
            }
        }
        if (change instanceof VisuAddChangeHistory) {
            VisuAddChangeHistory vach = (VisuAddChangeHistory) change;
            if (vach.getValue() != null) {
             //TODO: check if we need to save changes of visuhistory?
                addChangeHistory(vach);
            }
        }


        if (change instanceof VisuReferenceToEdgeChange) {
            VisuReferenceToEdgeChange vrtec = (VisuReferenceToEdgeChange) change;
            if (vrtec.getValue() != null) {
                setReferenceToEdge(vrtec.getValue());
            }
        }
    }

  
    public boolean isIsSelectable() {
        return isSelectable;
    }

    public void setIsSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
    }

   
}
