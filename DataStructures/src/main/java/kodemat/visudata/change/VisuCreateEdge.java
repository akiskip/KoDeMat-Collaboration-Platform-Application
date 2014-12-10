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
import java.io.IOException;
import kodemat.visudata.VisuChange;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuCreateEdge implements VisuChange{
    
    private long id;
    private String name;

    public VisuCreateEdge(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public VisuCreateEdge() {
        
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getName(){
        return name;
    }

    

    @Override
    public short getType() {
        return VisuChange.CREATE_EDGE;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
               out.writeLong(id);
        out.writeUTF(name);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
       id = in.readLong();
        name = in.readUTF();
    }
    
}
