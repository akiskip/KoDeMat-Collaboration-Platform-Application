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
import kodemat.visu.input.mouse.ClickSelection;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.visuComponents.VisuComponent;

/**
 *
 * @author Kipouridis
 */
public class NotesUIActionsFacade extends IUIActionsImpl {
    
    
    
        public NotesUIActionsFacade(IEditor visuController) {
        super(visuController);
     
         }
        
       
 

    public void addNote() {


            ClickSelection<VisuComponent> lastSelection = this.getVisuController().getMarkerSelection();
            if (lastSelection != null) {
                VisuComponent parent = lastSelection.getComponent();
  
                Long id = this.getVisuHelper().getIdGenerator().newId();

                    VisuComponent newNote = this.getVisuHelper().getComponent("Note " + id);
                    if (newNote == null) {
                        newNote = this.getVisuHelper().createComponent(id, "Note " + id);
                        newNote.setType(new VisuType(VisuType.MODEL, "Others/note.png"));
                    }
                    Vector3f normal = lastSelection.getCollisionResult().getContactNormal();
                    Vector3f contactPoint = lastSelection.getCollisionResult().getContactPoint();
                    Vector3f coordToPlace = contactPoint.subtract(normal.mult(0.1f));
                    Vector3f v = lastSelection.getSpatial().worldToLocal(coordToPlace, new Vector3f());
                    newNote.setScale(new VisuVector3f(.3f, .3f, .3f));
                    newNote.setParent(parent.getId());
//                    newNote.setParent(unSelectedObject.getModel().getId());
                    newNote.setTranslation(new VisuVector3f(v.x, v.y + 0.1f, v.z));

            
        }

    }       
    
    
    /*
    
    public void modifyNote() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        Element e2 = screen.findElementByName("note-field");
        if (currentNote.setObjectModel(selectedObject.getModel())) {
            showNoteHistory();
            e2.enable();
            e2.setFocus();
        }
    }
          public void showOrHideNote() {
        closeNoteHistory();
        hazelcastAppState.threadPool.execute(new Runnable() {
            @Override
            public void run() {
                selectedObject.showOrHideNote();
            }
        });
    }
        
        
        
    public void submitNote() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");

        String noteStr = screen.findNiftyControl("note-field", TextField.class).getText();
        if (!noteStr.equals("")) {

            String clientName = visuHelper.getUsers().get(visuHelper.getClientUUID()).getLast_name();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String noteText = clientName + ", " + dateFormat.format(date) + " - " + noteStr;
            currentNote.getModel().addNote(noteText);

            if (unSelectedObject.getModel() != null) {
                unSelectedObject.getModel().addNote(noteText);
            } else if (selectedObject.getModel() != null && selectedObject.getModel().getName().contains("Note")) {
                unSelectedObject.setObjectModel(selectedObject.getModelParent());
                unSelectedObject.getModel().addNote(noteText);
                selectedObject.showBigNote();
                unSelectedObject.setObject(null, null);
            }
            currentNote.showBigNote();



            screen.findNiftyControl("note-field", TextField.class).setText("");
            if (openHistory) {
                showNoteHistory();
                unSelectedObject.setObject(null, null);
            }
        } else {
            if (!selectedObject.getModel().getName().contains("Note")) {
                visuHelper.deleteComponent(currentNote.getModel().getName());
            }
        }
    }

    public void showNoteHistory() {
        openHistory = true;
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        VisuComponent noteHolder = null;
        if (unSelectedObject != null && unSelectedObject.getModel() != null) {
            noteHolder = unSelectedObject.getModel();

        } else if (selectedObject != null && selectedObject.getModel() != null && selectedObject.getModel().getName().contains("Note")) {
            noteHolder = selectedObject.getModel();
        }
        ArrayList<String> notes = noteHolder.getNotes();
        if (notes != null) {
            ListBox control = screen.findNiftyControl("notes-list", ListBox.class);
            control.clear();
            for (int i = 0; i < notes.size(); i++) {
                control.addItem(notes.get(i));
            }
            control.showItemByIndex(control.getItems().size() - 1);

            Element e = screen.findElementByName("show-history-panel");
            e.setFocusable(true);
            e.setVisible(true);
            Element e2 = screen.findElementByName("note-field");
            if (!selectedObject.isEmpty() && !selectedObject.getModel().getName().contains("Note")) {
                e2.disable();
            }
        }

    }

    public void closeNoteHistory() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");

        Element e = screen.findElementByName("show-history-panel");
        e.setFocusable(false);
        e.setVisible(false);
        openHistory = false;

        if (selectedObject != null && selectedObject.getModel() != null) {
            if (selectedObject.getModel().getName().contains("Note")) {
                Node n = selectedObject.getSpatial().getParent();
                n.detachChildNamed("Note");
            }
        }
        if (currentNote != null && currentNote.getModel() != null && currentNote.getModel().getNotes().isEmpty()) {
            visuHelper.deleteComponent(currentNote.getModel().getName());
            currentNote.setObject(null, null);
        }

    }
    
    */

}
