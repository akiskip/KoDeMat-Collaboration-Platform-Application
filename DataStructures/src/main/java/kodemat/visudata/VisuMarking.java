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
import java.io.IOException;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuMarking implements DataSerializable {

    public VisuColor getColor() {
        return color;
    }

    public String getLabel() {
        return label;
    }

    public int getBoundingBoxEnabled() {
        return boundingBoxEnabled;
    }
    public VisuColor color;
    public String label;
    
//    the bounding box=1 -> enabled, 0-> disabled
    public int boundingBoxEnabled;
    
    
    public VisuMarking() {
        color = new VisuColor(0, 0, 0, 1);
        label = "";
        boundingBoxEnabled = 0; 
        
    }

    public VisuMarking(VisuColor color, String label, int enableBB) {
        if (color == null) {
            throw new IllegalArgumentException("Parameter color must not be null");
        }
        this.color = color;
        this.label = label;
        boundingBoxEnabled = enableBB;
    }
    public VisuMarking(String label, int enableBB) {
        color = new VisuColor(0, 0, 0, 1);
        this.label = label;
        boundingBoxEnabled = enableBB;
        
    }


    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(label);
        color.writeData(out);
        out.writeInt(boundingBoxEnabled);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        label = in.readUTF();
        color.readData(in);
        boundingBoxEnabled = in.readInt();
    }

    @Override
    public String toString() {
        return "(" + color + ", " + label + ")";
    }
}
