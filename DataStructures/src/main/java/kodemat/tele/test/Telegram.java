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
package kodemat.tele.test;

import com.hazelcast.core.IMap;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import kodemat.admin.VisuKodematUser;

/**
 *
 * @author Orthodoxos Kipouridis, Moritz Roidl
 */
public class Telegram implements Serializable{
    
    private ArrayList<TelegramField> teleFieldArray;
    private long id;    
    private int value;
    private String editor;

        private final PropertyChangeSupport support = new PropertyChangeSupport(this);

        public Telegram(long id) {
      this.id = id;
      teleFieldArray = new ArrayList<TelegramField>();
        }
 
        public void addPropertyChangeListener(PropertyChangeListener l) {
            support.addPropertyChangeListener(l);
        }

        public void removePropertyChangeListener(PropertyChangeListener l) {
            support.removePropertyChangeListener(l);
        }

    
        public long getId() {
            return id;
        }
        
     public void setId(long id) {
            final long oldId = this.id;
            this.id = id;
            support.firePropertyChange("id", oldId, id);
        }
        
     public void setCreatorName(String editor) {
            final String oldEditor = this.editor;
            this.editor = editor;
            support.firePropertyChange("editor", oldEditor, editor);
        }
     
        public void setField(TelegramField teleField) {
            final TelegramField oldField = this.teleFieldArray.get(teleField.getId());
            teleFieldArray.set(teleField.getId(), teleField);
            support.firePropertyChange("name", oldField, teleField);
        }

        public void addField(TelegramField newField){
            this.teleFieldArray.add(newField);
        }

        public void removeField(TelegramField field){
            this.teleFieldArray.remove(field);
        }
  
}
