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
package kodemat.visudata.command;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import java.io.IOException;
import kodemat.visudata.VisuChange;
import kodemat.visudata.change.CompositeVisuChange;
import kodemat.visudata.change.VisuAddChangeHistory;
import kodemat.visudata.change.VisuAddToGlobalUndoCreate;
import kodemat.visudata.change.VisuAddToGlobalUndoDelete;
import kodemat.visudata.change.VisuCreateComponent;
import kodemat.visudata.change.VisuCreateEdge;
import kodemat.visudata.change.VisuDeleteComponent;
import kodemat.visudata.change.VisuDeleteEdge;
import kodemat.visudata.change.VisuEdgeSourceChange;
import kodemat.visudata.change.VisuEdgeTargetChange;
import kodemat.visudata.change.VisuInterpolationChange;
import kodemat.visudata.change.VisuLabelChange;
import kodemat.visudata.change.VisuMarkingChange;
import kodemat.visudata.change.VisuParentChange;
import kodemat.visudata.change.VisuRedoChange;
import kodemat.visudata.change.VisuReferenceToEdgeChange;
import kodemat.visudata.change.VisuRotationChange;
import kodemat.visudata.change.VisuScaleChange;
import kodemat.visudata.change.VisuTranslationChange;
import kodemat.visudata.change.VisuTypeChange;
import kodemat.visudata.change.VisuUndoChange;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuChangeCommand implements VisuCommand, DataSerializable {

    private VisuChange change;
    private String clientUUID ;

    public VisuChangeCommand() {
    }

    public String getClientUUID() {
        return clientUUID;
    }

    public void setClientUUID(String clientUUID) {
        this.clientUUID = clientUUID;
    }


    
    public VisuChangeCommand(VisuChange change, String clientUUID) {
        this.change = change;
        this.clientUUID = clientUUID;
    }



    public VisuChange getChange() {
        return change;
    }

    @Override
    public void writeData(ObjectDataOutput d) throws IOException {
        d.writeUTF(clientUUID);
        d.writeShort(change.getType());
        change.writeData(d);
    }

    @Override
    public void readData(ObjectDataInput di) throws IOException {
        clientUUID = di.readUTF();
        short type = di.readShort();
        change = readVisuChange(type, di);
    }

    public static VisuChange readVisuChange(short type, ObjectDataInput di) throws IOException {
        VisuChange ret = null;
        if (type == VisuChange.CREATE) {
            VisuCreateComponent vcc = new VisuCreateComponent();
            vcc.readData(di);
            ret = vcc;
        }
        if (type == VisuChange.DELETE) {
            VisuDeleteComponent vcc = new VisuDeleteComponent();
            vcc.readData(di);
            ret = vcc;
        }
        if (type == VisuChange.INTERPOLATION) {
            VisuInterpolationChange vcc = new VisuInterpolationChange();
            vcc.readData(di);
            ret = vcc;
        }
        if (type == VisuChange.LABEL) {
            VisuLabelChange vcc = new VisuLabelChange();
            vcc.readData(di);
            ret = vcc;
        }
        if (type == VisuChange.MARKING) {
            VisuMarkingChange vcc = new VisuMarkingChange();
            vcc.readData(di);
            ret = vcc;
        }
        if (type == VisuChange.PARENT) {
            VisuParentChange vcc = new VisuParentChange();
            vcc.readData(di);
            ret = vcc;
        }
        if (type == VisuChange.ROTATION) {
            VisuRotationChange vcc = new VisuRotationChange();
            vcc.readData(di);
            ret = vcc;
        }
        if (type == VisuChange.SCALE) {
            VisuScaleChange vcc = new VisuScaleChange();
            vcc.readData(di);
            ret = vcc;
        }
        if (type == VisuChange.TRANSLATION) {
            VisuTranslationChange vcc = new VisuTranslationChange();
            vcc.readData(di);
            ret = vcc;
        }
        if (type == VisuChange.TYPE) {
            VisuTypeChange vcc = new VisuTypeChange();
            vcc.readData(di);
            ret = vcc;
        }
        if (type == VisuChange.UNDO) {
            VisuUndoChange vuc = new VisuUndoChange();
            vuc.readData(di);
            ret = vuc;
        }
        if (type == VisuChange.REDO) {
            VisuRedoChange vrc = new VisuRedoChange();
            vrc.readData(di);
            ret = vrc;
        }
        if (type == VisuChange.NEW_CHANGE) {
            VisuAddChangeHistory vach = new VisuAddChangeHistory();
            vach.readData(di);
            ret = vach;
        }
        if (type == VisuChange.ADD_TO_GLOBAL_UNDO_DELETE) {
            VisuAddToGlobalUndoDelete vatgud = new VisuAddToGlobalUndoDelete();
            vatgud.readData(di);
            ret = vatgud;
        }
        if (type == VisuChange.ADD_TO_GLOBAL_UNDO_CREATE) {
            VisuAddToGlobalUndoCreate vatguc = new VisuAddToGlobalUndoCreate();
            vatguc.readData(di);
            ret = vatguc;
        }
        if(type == VisuChange.EDGE_SOURCE){
            VisuEdgeSourceChange vesc = new VisuEdgeSourceChange();
            vesc.readData(di);
            ret = vesc;
        }
        if(type == VisuChange.EDGE_TARGET){
            VisuEdgeTargetChange vetc = new VisuEdgeTargetChange();
            vetc.readData(di);
            ret = vetc;
        }
        if(type == VisuChange.CREATE_EDGE){
            VisuCreateEdge vce = new VisuCreateEdge();
            vce.readData(di);
            ret = vce;
        }
        if(type == VisuChange.DELETE_EDGE){
            VisuDeleteEdge vde = new VisuDeleteEdge();
            vde.readData(di);
            ret = vde;
        }
        if(type == VisuChange.REFERENCE_TO_EDGE){
            VisuReferenceToEdgeChange vrtec = new VisuReferenceToEdgeChange();
            vrtec.readData(di);
            ret = vrtec;
        }
        if(type == VisuChange.COMPOSITE_COMPONENT_CHANGE){
            CompositeVisuChange cvc = new CompositeVisuChange();
            cvc.readData(di);
            ret = cvc;
        }
        return ret;
    }
}
