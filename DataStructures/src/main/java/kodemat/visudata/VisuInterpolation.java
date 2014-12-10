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
public class VisuInterpolation implements DataSerializable {

    public boolean translation;
    public boolean rotation;
    public boolean scale;

    public VisuInterpolation() {
    }

    public VisuInterpolation(boolean translation, boolean rotation, boolean scale) {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeBoolean(translation);
        out.writeBoolean(rotation);
        out.writeBoolean(scale);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        translation = in.readBoolean();
        rotation = in.readBoolean();
        scale = in.readBoolean();
    }

    @Override
    public String toString() {
        return "(" + translation + ", " + rotation + ", " + scale + ")";
    }
}
