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
public class VisuVector3f implements DataSerializable {

    public VisuVector3f() {
    }

    public VisuVector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public float x;
    public float y;
    public float z;

    /*
     * Getter to return an array of coordinates scaled according to the scalingfactor 
     * given as param. Did not use vector due to class serialisation contstraints
     */
    public float[] getWithScale(float scalingFactor) {

        float[] scaledVisuVector = new float[3];
        scaledVisuVector[0] = this.x * scalingFactor;
        scaledVisuVector[1] = this.y * scalingFactor;
        scaledVisuVector[2] = this.z * scalingFactor;
        return scaledVisuVector;
    }
    
    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
             out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(z);
    }
    
    @Override
    public void readData(ObjectDataInput in) throws IOException {
            x = in.readFloat();
        y = in.readFloat();
        z = in.readFloat();
    }
    
    @Override
    public String toString(){
        return "("+x+", "+y+", "+z+")";
    }
}
