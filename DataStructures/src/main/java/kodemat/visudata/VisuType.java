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
 * Class that defines the type of a VisuComponent
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuType implements DataSerializable {

    public static final String MODEL = "Model";
    public static final String NODE = "Node";
    public static final String TEXTURE = "Texture";
    public static final String BOX = "Box";
    public static final String WAYPOINT = "Waypoint";

    public VisuType() {
    }

    public VisuType(String type, String path) {
        this.type = type;
        this.path = path;
    }
    public String type;
    /*
     * the path for the model asset data  
     */
    public String path;

    public String getString() {
        return this.type;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(type);
        out.writeUTF(path);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        type = in.readUTF();
        path = in.readUTF();
    }

    @Override
    public String toString() {
        return "(" + type + ", " + path + ")";
    }
}
