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
import kodemat.visudata.VisuVector3f;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuTranslationChange extends AbstractVisuAttributeChange<VisuVector3f> {
    
    private VisuVector3f vec;

    public VisuTranslationChange() {
    }

    public VisuTranslationChange(VisuVector3f vec, long id) {
        super(id);
        this.vec = vec;
    }

    @Override
    public VisuVector3f getValue() {
        return vec;
    }

    @Override
    public void setValue(VisuVector3f t) {
        vec = t;
    }
    
    @Override
    public void writeData(ObjectDataOutput d) throws IOException {
        super.writeData(d);
        vec.writeData(d);
    }

    @Override
    public void readData(ObjectDataInput di) throws IOException {
        super.readData(di);
        vec = new VisuVector3f();
        vec.readData(di);
    }
    
    @Override
    public short getType() {
        return VisuChange.TRANSLATION;
    }
    
}
