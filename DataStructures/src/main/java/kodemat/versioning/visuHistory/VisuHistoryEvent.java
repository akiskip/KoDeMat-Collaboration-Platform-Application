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
package kodemat.versioning.visuHistory;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import kodemat.tele.teledata.AbstractDistributableField;

/**
 *
 * @author Kipouridis
 */
public class VisuHistoryEvent extends AbstractDistributableField{
    private int id;
    private String name;
    private String value;
    private String date;
         private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
         
         
          public VisuHistoryEvent() {
super();
        }
        public VisuHistoryEvent(int id, String name, String value) {
this.id= id;
this.name= name;
this.value= value;
 Date currentDate = new Date();
 
this.date = dateFormat.format(currentDate);
            
        }

   
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
 
    public String getDate(){
       
        return date;
    }
        
          @Override
    public void writeData(ObjectDataOutput odo) throws IOException {
      odo.writeInt(id);
      odo.writeUTF(name);
      odo.writeUTF(value);
    }

    @Override
    public void readData(ObjectDataInput odi) throws IOException {
       id= odi.readInt();
       name = odi.readUTF();
       value = odi.readUTF();
    }   
}
