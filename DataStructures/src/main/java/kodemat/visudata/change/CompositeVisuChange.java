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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kodemat.visudata.VisuChange;
import kodemat.visudata.command.VisuChangeCommand;

/**
 * This class encapsulates one or more VisuChanges to one component
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class CompositeVisuChange implements VisuChange {

    List<VisuChange> list = new ArrayList<>();

    public int size() {
        return list.size();
    }

    public boolean add(VisuChange e) {
        return list.add(e);
    }

    public void clear() {
        list.clear();
    }

    public CompositeVisuChange() {
    }

    @Override
    public short getType() {
        return VisuChange.COMPOSITE_COMPONENT_CHANGE;
    }

    @Override
    public void writeData(ObjectDataOutput odo) throws IOException {
        odo.writeInt(list.size());
        for (VisuChange visuChange : list) {
            odo.writeShort(visuChange.getType());
        }
        for (VisuChange visuChange : list) {
            visuChange.writeData(odo);
        }
    }

    @Override
    public void readData(ObjectDataInput odi) throws IOException {
        int size = odi.readInt();
        short[] types = new short[size];
        for (int i = 0; i < types.length; i++) {
            types[i] = odi.readShort();
        }
        for (short s : types) {
            list.add(VisuChangeCommand.readVisuChange(s, odi));
        }
    }
}
