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

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuReferenceToEdgeChange extends AbstractVisuAttributeChange<Long> {
    
    private Long edgeId;

    public VisuReferenceToEdgeChange() {
    }

    public VisuReferenceToEdgeChange(Long edgeId, long id) {
        super(id);
        this.edgeId = edgeId;
    }
    
    

    @Override
    public Long getValue() {
        return edgeId;
    }

    @Override
    public void setValue(Long t) {
        edgeId = t;
    }
    
    @Override
    public void writeData(ObjectDataOutput d) throws IOException {
        super.writeData(d);
        d.writeLong(edgeId);
    }

    @Override
    public void readData(ObjectDataInput di) throws IOException {
        super.readData(di);
        edgeId = di.readLong();
    }
    
    @Override
    public short getType() {
        return VisuChange.REFERENCE_TO_EDGE;
    }
    
}
