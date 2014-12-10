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
package kodemat.visudata.visuComponents.metaComponents;

import java.util.List;
import kodemat.visudata.VisuChange;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuInterpolation;
import kodemat.visudata.VisuMarking;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.visuComponents.AbstractCompositeComponent;
import kodemat.visudata.visuComponents.IComponent;
import kodemat.visudata.visuComponents.VisuComponent;

/**
 *
 * @author Kipouridis
 */
public class VisuNoteComponent extends AbstractCompositeComponent{

      public VisuNoteComponent(String name, VisuHelper helper, boolean serverMode) {
        super(name, helper, serverMode);
    }

    public VisuNoteComponent(Long id, VisuHelper helper, boolean serverMode) {
        super(id, helper, serverMode);
    }
    
    
    @Override
    public void setTranslation(VisuVector3f vec, Boolean... param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VisuVector3f getTranslation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRotation(VisuRotation rot, Boolean... param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VisuRotation getRotation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setScale(VisuVector3f vec, Boolean... param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VisuVector3f getScale() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setInterpolation(VisuInterpolation vi, Boolean... param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VisuInterpolation getInterpolation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void applyChange(VisuChange change, Boolean... param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


 
    
}
