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
package kodemat.visudata.change;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import kodemat.visudata.VisuUndoDeleteData;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import kodemat.visudata.VisuChange;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuAddToGlobalUndoDelete extends AbstractVisuAttributeChange<VisuUndoDeleteData>{
    
    public VisuUndoDelete vud;
    public boolean isUndo;

    public VisuAddToGlobalUndoDelete() {
    }

    public VisuAddToGlobalUndoDelete(long id) {
        super(id);
    }

    @Override
    public void writeData(ObjectDataOutput d) throws IOException {
        super.writeData(d);
        vud.writeData(d);
    }

    @Override
    public void readData(ObjectDataInput di) throws IOException {
        super.readData(di);
        vud = new VisuUndoDelete();
        vud.readData(di);
    }

    @Override
    public short getType() {
        return VisuChange.ADD_TO_GLOBAL_UNDO_DELETE;
    }

    @Override
    public long getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setId(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public VisuUndoDeleteData getValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(VisuUndoDeleteData t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
