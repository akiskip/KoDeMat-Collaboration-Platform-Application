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
public class VisuColor implements DataSerializable {

    public VisuColor() {
    }

    public VisuColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    public float r;
    public float g;
    public float b;
    public float a;

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeFloat(r);
        out.writeFloat(g);
        out.writeFloat(b);
        out.writeFloat(a);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        r = in.readFloat();
        g = in.readFloat();
        b = in.readFloat();
        a = in.readFloat();
    }

    @Override
    public String toString() {
        return "[" + r + ", " + g + ", " + b + ", " + a + "]";
    }
}
