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
package kodemat.visu.layoutScripts;

import Interfaces.LayoutClientImpl;
import com.hazelcast.core.IMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import kodemat.tele.test.TelegramField;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuVector3f;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class CeMAT_StaplerScript extends LayoutClientImpl {
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    /*
     * Haselcast Client
     */
    private VisuComponent floor;
    private VisuComponent staplerNode;

    public static boolean run = true;
  private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static boolean isRun() {
        return run;
    }

    public static void setRun(boolean run) {
        CeMAT_StaplerScript.run = run;
    }
    private VisuComponent palette;
    private IMap<Integer, VisuComponentInfoEntry> staplerInfoMap;
   
    public CeMAT_StaplerScript() {
        //conf file for haselcast

        super();

    }

   long findCompByName(String name){
       for(VisuComponent c :helper.getAllComponents() )   {
          if(c.getName().contains(name)){
           return c.getId();
          }
       }
       return -1l;
   }
    
    /**
     * 2
     *
     * @param count
     */
    public void startTest(int count) {

        
             staplerNode = helper.getComponent(findCompByName("stapler"));
        palette = helper.getComponent(findCompByName("palette.j30_81"));
                 staplerInfoMap =  staplerNode.getVisuInfoMap();
 
    
     staplerInfoMap.put(staplerInfoMap.size(),new VisuComponentInfoEntry(staplerInfoMap.size(),"Vehicle ID",""));
        staplerInfoMap.put(staplerInfoMap.size(),new VisuComponentInfoEntry(staplerInfoMap.size(),"Timestamp",""));
    staplerInfoMap.put(staplerInfoMap.size(), new VisuComponentInfoEntry(staplerInfoMap.size(),"Translation",""));
    staplerInfoMap.put(staplerInfoMap.size(), new VisuComponentInfoEntry(staplerInfoMap.size(),"Rotation",""));
    
    
                final VisuVector3f startTranslation = staplerNode.getTranslation();
               final VisuRotation  startRotation = staplerNode.getRotation();
             
               executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while(true){
              turn90right(staplerNode);
            
         travelWest(staplerNode, 15f);
        turn90right(staplerNode);
        
        travelNorth(staplerNode, 26f);
         turn90right(staplerNode);
        travelEast(staplerNode, 15f);
         turn90right(staplerNode);
        travelSouth(staplerNode, 26f);


        
          staplerNode.setTranslation(startTranslation);
         staplerNode.setRotation(startRotation);
              }
                
                }
            });
        }


   public void turn90right(VisuComponent component){
          
        if(component != null){ 
            VisuRotation curentRotation = component.getRotation();
                 for(float i=0;i<=90;i+=3f){
                VisuRotation rot = new VisuRotation(curentRotation.x,curentRotation.y-i,curentRotation.z);
              component.setRotation(rot);
               staplerInfoMap.put(3,new VisuComponentInfoEntry(3,staplerInfoMap.get(3).getName(),rot.toString()));
              sleep(200);
            i++;
        }
   }
    
   }
    
   
   
   public void travelNorth(VisuComponent component,float distance){
          
        if(component != null){ 
            VisuVector3f currentTranslation = component.getTranslation();
                 for(float i=0;i<=distance;i+=1f){
          VisuVector3f pos = new VisuVector3f(currentTranslation.x,currentTranslation.y,currentTranslation.z+i);
             this.setTranslation(component, pos);
              sleep(300);
            i++;
           
        }
   }
   }
   public void travelEast(VisuComponent component,float distance){
          
        if(component != null){ 
            VisuVector3f currentTranslation = component.getTranslation();
                 for(float i=0;i<=distance;i+=1f){
                     
                      VisuVector3f pos = new VisuVector3f(currentTranslation.x-i,currentTranslation.y,currentTranslation.z);
             this.setTranslation(component, pos);
              sleep(300);
        
            i++;
           
        }
   }
   }
 
   public void travelWest(VisuComponent component,float distance){
          
        if(component != null){ 
            VisuVector3f currentTranslation = component.getTranslation();
                 for(float i=0;i<=distance;i+=1f){

                     VisuVector3f pos = new VisuVector3f(currentTranslation.x+i,currentTranslation.y,currentTranslation.z);
             this.setTranslation(component, pos);
              sleep(300);
            i++;
           
        }
   }
    
   }
      public void travelSouth(VisuComponent component,float distance){
          
        if(component != null){ 
            VisuVector3f currentTranslation = component.getTranslation();
                 for(float i=0;i<=distance;i+=1f){
                VisuVector3f pos = new VisuVector3f(currentTranslation.x,currentTranslation.y,currentTranslation.z-i);
             this.setTranslation(component, pos);
              sleep(300);
            i++;
           
        }
   }
   }
   
    public void setTranslation(VisuComponent component, VisuVector3f position){
                    component.setTranslation(position);
                    staplerInfoMap.set(1,new VisuComponentInfoEntry(1,dateFormat.format(new Date()),""));
                       staplerInfoMap.set(2,new VisuComponentInfoEntry(3,position.toString(),""));

        
    }
    
    public void executeTestScenario(int iterations) {
    }

    public static void main(String[] args) {
        CeMAT_StaplerScript tc = new CeMAT_StaplerScript();
  tc.startTest(1);
//        tc.shutdown();
    }

    @Override
    public void addConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {
 run = false;
    }
}
