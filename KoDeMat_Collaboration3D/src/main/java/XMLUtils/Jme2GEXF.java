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
package XMLUtils;

import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuVector3f;

/**
 *Class that offers functions for the translation of jme attributes to GEXF XML data format
 * @author  Orthodoxos Kipouridis}
 */
public class Jme2GEXF {

    public Jme2GEXF(){
        
    }
    
    public static String translationAttribute(VisuVector3f translation){
     
        String[] translationAttArray = new String[3];
        
        translationAttArray[0] = Float.toString(translation.x);
        translationAttArray[1] = Float.toString(translation.y);
        translationAttArray[2] = Float.toString(translation.z);
    
        String translationAtt = new String(""+translationAttArray[0]+" , "+translationAttArray[1]+" , "+translationAttArray[2]);
       
        
        return translationAtt;
        
    }
    public static String typeAttribute(String componentType){
     
        String typenAtt = componentType;
       
        return typenAtt;
        
    }
    public static String rotationAttribute(VisuRotation rotation){
     
        String[] rotationAttArray = new String[3];
        
        rotationAttArray[0] = Float.toString(rotation.x);
        rotationAttArray[1] = Float.toString(rotation.y);
        rotationAttArray[2] = Float.toString(rotation.z);
    
        String rotationAtt = new String(""+rotationAttArray[0]+" , "+rotationAttArray[1]+" , "+rotationAttArray[2]);
       
        
        return rotationAtt;
        
    }
    
    public static VisuVector3f vector3fFromString(String translationString){
        
        VisuVector3f translation = new VisuVector3f();
       String[] temp = translationString.split(",");
        
       translation.x = Float.parseFloat(temp[0]);
       translation.y = Float.parseFloat(temp[1]);
       translation.z = Float.parseFloat(temp[2]);
        
        return translation;
    }
    public static VisuRotation rotationFromString(String rotationString){
        
        VisuRotation rotation = new VisuRotation();
       String[] temp = rotationString.split(",");
        
       rotation.x = Float.parseFloat(temp[0]);
       rotation.y = Float.parseFloat(temp[1]);
       rotation.z = Float.parseFloat(temp[2]);
        
        return rotation;
    }
    
    
}
