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
package kodemat.visu.editor.userActions;

import com.jme3.math.Vector3f;
import kodemat.visu.editor.IEditor;
import kodemat.visu.editor.selection.UnsupportedOpperationOnComponentException;

/**
 *Class that contains the User Actions for editing the jme scenegraph, through 
 * model manipulation
 * @author Kipouridis
 */
public class SceneUIActionsFacade extends IUIActionsImpl {

    public SceneUIActionsFacade(IEditor visuController){
        
       super(visuController);
    }
    
    

    public void rotate(float deg) {
      try{
            this.getVisuController().getObjectSelection().rotate(deg);
        }catch( UnsupportedOpperationOnComponentException ex)
        {
           System.out.println("Unsupported Operation");
        }
    }

    public void scale(float scaleNum) {
         try{
            this.getVisuController().getObjectSelection().scale(scaleNum);
        }catch( UnsupportedOpperationOnComponentException ex)
        {
           System.out.println("Unsupported Operation");
        }
        
        
    }
        
        
    public void moveUp(Vector3f direction, float factor) {
      
          try{
            this.getVisuController().getObjectSelection().move(direction, factor);
        }catch( UnsupportedOpperationOnComponentException ex)
        {
           System.out.println("Unsupported Operation");
        }

    }

    public void moveDown(Vector3f direction,float factor) {
         try{
            this.getVisuController().getObjectSelection().move(direction, -factor);
        }catch( UnsupportedOpperationOnComponentException ex)
        {
           System.out.println("Unsupported Operation");
        }
    }

    public void moveLeft(Vector3f direction,float factor) {
   try{
            getVisuController().getObjectSelection().move(direction, factor);
        }catch( UnsupportedOpperationOnComponentException ex)
        {
           System.out.println("Unsupported Operation");
        }
    }

    public void moveRight(Vector3f direction,float factor) {
            try{
            getVisuController().getObjectSelection().move(direction, -factor);
        }catch( UnsupportedOpperationOnComponentException ex)
        {
           System.out.println("Unsupported Operation");
        }
    }
    
}
