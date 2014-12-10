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

import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.Calendar;

/**
 *
 * @author Orthodoxos Kipouridis. Moritz Roidl
 */
public class VisuChatMessage implements DataSerializable {
        private String username;


        private String message;
        private String timeStamp;
        

        public VisuChatMessage(String username, String message) {
            this.username = username;
            this.message = message;
            this.timeStamp =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        }

    public VisuChatMessage() {
       
    }

        @Override
        public String toString() {
            return "["+timeStamp+"]"+username + ": " + message;
        }

        public void send(IMap<String, VisuChatMessage> chatMap) {
            chatMap.put(username, this);
        }

    @Override
    public void writeData(ObjectDataOutput odo) throws IOException {
       odo.writeUTF(username);
       odo.writeUTF(message);
       odo.writeUTF(timeStamp);
    }

    @Override
    public void readData(ObjectDataInput odi) throws IOException {
      username = odi.readUTF();
      message = odi.readUTF();
      timeStamp = odi.readUTF();
    }
    
    
    public String getUsername() {
        return username;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    
    
    
    
}
