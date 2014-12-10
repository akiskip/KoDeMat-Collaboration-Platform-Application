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
package kodemat.admin;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import java.io.IOException;
import kodemat.tele.teledata.IObservableData;

/**
 *
 * @author Kipouridis
 */
public class KodematUserEntry  implements DataSerializable, IObservableData<Long, VisuKodematUser> {

    private Long id = null;
    private VisuKodematUser user = null;
    private String name;
    
    public KodematUserEntry(Long id, VisuKodematUser user){
        this.id = id;
        this.user = user;
        this.setName(user.getUsername());
    }
    
    
    
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public VisuKodematUser getValue() {
return user;
    }

    @Override
    public void setValue(VisuKodematUser value) {

    this.user = value;
    }

    @Override
    public String getName() {
return this.name;
    }

    @Override
    public void setName(String name) {
this.name = name;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
