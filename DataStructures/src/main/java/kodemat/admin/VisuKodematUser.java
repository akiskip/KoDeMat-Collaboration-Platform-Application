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
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import kodemat.tele.teledata.IObservableData;

/**
 *
 * @author Orthodoxos Kipouridis. Moritz Roidl
 */
public class VisuKodematUser implements DataSerializable, IObservableData {
    /**
     * Holds the set of logged in users and thier assigned colors
     */
    public static Map <String,Color> userColorDic= new HashMap<>();
    String username = "foo";
    String first_name="foo ";
    String last_name ="foo";
    String position ="Wiss. Mitarbeiter";
    String company ="TUM";
    String socketAddress="localhost";
    String userUUID = "";
    
    
  float[]  colorArray = new float[]{(float)Math.random(), (float) Math.random(), 0.5F + ((float) Math.random())/2F};
//  final Color     color = new Color(Color.HSBtoRGB(colorArray[0],colorArray[1],colorArray[2]));
  Color color ;
      private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public VisuKodematUser(String LastName) {
        this.last_name = LastName;
         color = generateRandomColor(new Color(255,255,255));
         

    }

   public VisuKodematUser() {
         color = generateRandomColor(new Color(255,255,255));
    }
   public VisuKodematUser(String username, String company, String position, String socketAddress){
       
       this.username = username;
       this.position = position;
       this.company = company;
       this.socketAddress = socketAddress;
       
       //for testing make the last name equal to the user name
       this.last_name= username;
       color = generateRandomColor(new Color(255,255,255));
       
//       create a random light color for each user, evt a user could choose his color
   
   }

   
        public void addPropertyChangeListener(PropertyChangeListener l) {
            support.addPropertyChangeListener(l);
        }

        public void removePropertyChangeListener(PropertyChangeListener l) {
            support.removePropertyChangeListener(l);
        }

   
   
    public Color getColor() {
        return color;
    }

 


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
       
               final String oldName = this.username;
               this.username = username;
            support.firePropertyChange("username", oldName, username);
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userID) {
       
                String oldId = this.userUUID;
             this.userUUID = userID;
            support.firePropertyChange("userUUID", oldId, userUUID);
       
    }
    
    

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
 
         final String oldName = this.first_name;
              this.first_name = first_name;
            support.firePropertyChange("first_name", oldName, first_name);
        
        
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
     final String oldName = this.last_name;
              this.last_name = last_name;
            support.firePropertyChange("last_name", oldName, last_name);
        
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
       
             final String oldName = this.position;
           this.position = position;
            support.firePropertyChange("position", oldName, position);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
                  final String oldName = this.company;
           this.company = company;
            support.firePropertyChange("company", oldName, company);
    }
//    public VisuUserCompany getCompany() {
//        return company;
//    }
//
//    public void setCompany(VisuUserCompany company) {
//        this.company = company;
//    }

    public String getSocketAddress() {
        return socketAddress;
    }

    public void setSocketAddress(String socketAddress) {
    
            final String oldName = this.socketAddress;
              this.socketAddress = socketAddress;
            support.firePropertyChange("socketAddress", oldName, socketAddress);
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(username);
        out.writeUTF(userUUID);
     out.writeUTF(first_name);
        out.writeUTF(last_name);
          out.writeUTF(position);
        out.writeUTF(company);
        out.writeUTF(socketAddress);
        out.writeFloatArray(colorArray);
    }
    

    @Override
    public void readData(ObjectDataInput odi) throws IOException { 
       username = odi.readUTF();
       userUUID = odi.readUTF();     
        first_name= odi.readUTF();
     last_name= odi.readUTF();
     position= odi.readUTF();
     company= odi.readUTF();
     socketAddress= odi.readUTF();
     colorArray = odi.readFloatArray();
    }
    @Override
    public String toString() {
        return "(" + first_name + ", " +last_name + ", " + position+ ", " + company+ ", " + socketAddress+", "+color+")";
    }

    @Override
    public Object getId() {
return Integer.parseInt(this.getUserUUID());
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public  Color generateRandomColor(Color mix) {
        System.out.println("calling generate color "+ last_name+ color);
        
    Random random = new Random();
    int red = random.nextInt(256);
    int green = random.nextInt(256);
    int blue = random.nextInt(256);

    // mix the color
    if (mix != null) {
        red = (red + mix.getRed()) / 2;
        green = (green + mix.getGreen()) / 2;
        blue = (blue + mix.getBlue()) / 2;
    }
    
    // a new color will be assigend for the new user, 
    // if the user was already assigned a color it will be returned
    // this is being called more than one time even for the same row
     Color color ;
    if (userColorDic.containsKey(last_name)){
         color = userColorDic.get(last_name);
    }else{
        color = new Color(red, green, blue);
        userColorDic.put(last_name, color);
    }
    return color;
}
}
