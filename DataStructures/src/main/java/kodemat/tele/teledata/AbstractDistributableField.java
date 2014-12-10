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
package kodemat.tele.teledata;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import kodemat.tele.teledata.IObservableData;

/**
 *
 * @author Orthodoxos Kipouridis, Moritz Roidl
 */
public abstract class AbstractDistributableField implements DataSerializable,IObservableData<Integer, String>   {
    public int id;    
    public String name;
        public String value;
        public String userSelected;

        private final PropertyChangeSupport support = new PropertyChangeSupport(this);

        public AbstractDistributableField(int id, String value) {
            this.name = "";
            this.id = id;
            this.value = value;
            userSelected = "";
        }
        public AbstractDistributableField(int id, String name, String value) {
            this.name = name;
            this.id = id;
            this.value = value;
            userSelected = "";

        }
        
        public AbstractDistributableField(){
            
        }
        
        public void addPropertyChangeListener(PropertyChangeListener l) {
            support.addPropertyChangeListener(l);
        }

        public void removePropertyChangeListener(PropertyChangeListener l) {
            support.removePropertyChangeListener(l);
        }

        public String getName() {
            return name;
        }
    
        
        public Integer getId() {
            return id;
        }
        
     public void setId(int id) {
            final int oldId = this.id;
            this.id = id;
            support.firePropertyChange("id", oldId, id);
        }
     
        public void setName(String name) {
            final String oldName = this.name;
            this.name = name;
            support.firePropertyChange("name", oldName, name);
        }
    @Override
        public void setValue(String value) {
            final String oldValue = this.value;
            this.value = value;
            support.firePropertyChange("value", oldValue, value);
        }

    @Override
        public String getValue() {
            return value;
        }

    public String getUserSelected() {
        return userSelected;
    }

    public void setUserSelected(String userSelected) { 
           final String oldValue = this.userSelected;
                this.userSelected = userSelected;
            support.firePropertyChange("userSelected", oldValue, userSelected);
    }

    
    /**
     * The Following methods are required for hazelcast serialisation
     * @param odo
     * @throws IOException 
     */
    @Override
    public void writeData(ObjectDataOutput odo) throws IOException {
      odo.writeInt(id);
      odo.writeUTF(name);
      odo.writeUTF(value);
      odo.writeUTF(userSelected);
    }

    @Override
    public void readData(ObjectDataInput odi) throws IOException {
       id= odi.readInt();
       name = odi.readUTF();
       value = odi.readUTF();
       userSelected = odi.readUTF();
    }   

  
}
