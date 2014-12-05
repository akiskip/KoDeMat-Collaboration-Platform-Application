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
package kodemat.visu.editor;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import kodemat.visu.input.mouse.ClickSelection;
import kodemat.visudata.visuComponents.VisuComponent;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class EditorManager {

    Application visuClient;
    AbstractAppState cameraControllerAppState;
    private boolean initialized = false;

    public EditorManager(AbstractAppState cameraControllerAppState) {
        this.cameraControllerAppState = cameraControllerAppState;
    }

    public void initialize(Application visuClient) {
        this.visuClient = visuClient;

        for (IEditor editor : editorDeque) {
            editor.initialize(visuClient);
        }
        initialized = true;
    }
    protected final Deque<IEditor> editorDeque = new ConcurrentLinkedDeque<>();

    public void push(IEditor editor) {
        if (initialized) {
            editor.initialize(visuClient);
        }
        editorDeque.push(editor);
    }

    public void pop() {
        IEditor pop = editorDeque.pop();
        pop.cleanup();
    }

    public IEditor peek() {
        return editorDeque.peek();
    }

    public boolean isEmpty() {
        return editorDeque.isEmpty();
    }

    public void handleClick(final ClickSelection<VisuComponent> selection) {
try{
   ClickSelection<VisuComponent> selected = removeNonSelectables(selection);
    if(selected != null )
        peek().handleClick(removeNonSelectables(selection));
}
catch(Exception e){
    System.out.println("There was a problem with the HadleClick method, check editor " + e);
}
}
    
    /**
     * Check if the clicked component is selectable. If not return null. 
     * //TODO: examine if this class should be implemented in the ClickSelection class or the EditorHandler class.
     * @param selection
     * @return selection, the selected component
     */
    private ClickSelection<VisuComponent> removeNonSelectables(ClickSelection<VisuComponent> selection){
         if( selection.getComponent().isIsSelectable())
             return selection;
                     else return null;
        
    }
}
