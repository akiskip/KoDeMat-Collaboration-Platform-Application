/* 
 * Copyright 2014 Institute fml (TU Munich) and Institute FLW (TU Dortmund).
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
package flw.energieeffizienz;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.jme3.math.Vector3f;
import java.io.IOException;
import kodemat.visudata.VisuVector3f;

/**
 *
 * @author Orthodoxos Kipouridis. Moritz Roidl
 */
public class PhotoSensor implements DataSerializable{
    
    long id;
    
    boolean isActive;
    
    long parentModelId;
    
    VisuVector3f relativePosition;
    
    
    public PhotoSensor(long id, boolean active, long parentModelId, VisuVector3f relativePosition) {
        this.id = id;
        this.isActive = active;
        this.parentModelId = parentModelId;
        this.relativePosition = relativePosition;
    }


    @Override
    public void writeData(ObjectDataOutput out) throws IOException {

        out.writeLong(id);
        out.writeBoolean(isActive);
        out.writeLong(parentModelId);
        relativePosition.writeData(out);
        
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {

        id=in.readLong();
        isActive= in.readBoolean();
        parentModelId = in.readLong();
        relativePosition.readData(in);
    }
    
}
