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
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import kodemat.visudata.VisuChange;
import kodemat.visudata.VisuType;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuTypeChange extends AbstractVisuAttributeChange<VisuType> {
    
    private VisuType type;

    public VisuTypeChange() {
    }

    public VisuTypeChange(VisuType type, long id) {
        super(id);
        this.type = type;
    }
    
    

    @Override
    public VisuType getValue() {
        return type;
    }

    @Override
    public void setValue(VisuType t) {
        type = t;
    }
    
    @Override
    public void writeData(ObjectDataOutput d) throws IOException {
        super.writeData(d);
        type.writeData(d);
    }

    @Override
    public void readData(ObjectDataInput di) throws IOException {
        super.readData(di);
        type = new VisuType();
        type.readData(di);
    }
    
    @Override
    public short getType() {
        return VisuChange.TYPE;
    }
    
}
