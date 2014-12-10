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


import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuUndoDeleteData implements DataSerializable {

    public Long id;
    public String name;
    public VisuVector3f translation;
    public VisuRotation rotation;
    public VisuVector3f scale;
    public String label;
    public Long parent;
    public VisuType type;
    public VisuInterpolation interpolation;
    public VisuMarking marking;
    public Long next = null;

    
    //TODO: Check if we can implement it will transaction rollback
    public VisuUndoDeleteData() {
    }
    
    public VisuUndoDeleteData(Long id, String name, VisuVector3f translation, VisuRotation rotation,
            VisuVector3f scale, String label, Long parent, VisuType type, VisuInterpolation interpolation,
            VisuMarking marking, Long next){
        this.id=id;
        this.name=name;
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
        this.label = label;
        this.parent = parent;
        this.type = type;
        this.interpolation = interpolation;
        this.marking = marking;
        this.next = next;
    }


    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeLong(id);
        out.writeUTF(name);
        if (translation != null) {
            translation.writeData(out);
        }
        if (rotation != null) {
            rotation.writeData(out);
        }
        if (scale != null) {
            scale.writeData(out);
        }
        out.writeUTF(label);
        out.writeLong(parent);
        if (type != null) {
            type.writeData(out);
        }
        if (interpolation != null) {
            interpolation.writeData(out);
        }
        if (marking != null) {
            marking.writeData(out);
        }
        out.writeLong(next);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
                id = in.readLong();
        name = in.readUTF();
        translation = new VisuVector3f();
        translation.readData(in);
        rotation = new VisuRotation();
        rotation.readData(in);
        scale = new VisuVector3f();
        scale.readData(in);
        label = in.readUTF();
        parent = in.readLong();
        type = new VisuType();
        type.readData(in);
        interpolation = new VisuInterpolation();
        interpolation.readData(in);
        marking = new VisuMarking();
        marking.readData(in);
        next = in.readLong();
    }
}
