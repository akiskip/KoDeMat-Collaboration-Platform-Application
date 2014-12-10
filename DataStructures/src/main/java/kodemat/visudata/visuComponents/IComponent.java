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
package kodemat.visudata.visuComponents;

import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuMarking;
import kodemat.visudata.VisuType;

/**
 * Public interface for the VisuComponents
 *
 * @author Kipouridis
 */
public interface IComponent {

    /**
     * The name of the component
     *
     * @return The component name
     */
    public String getName();

    /**
     * The Component unique Hazelcastid
     *
     * @return the id of the component
     */
    public long getId();

    public VisuHelper getHelper();
    public boolean isServerMode();
   

    public void setLabel(String lbl, Boolean... param);

    public String getLabel();

    public void setParent(Long pid, Boolean... param);

    public Long getParent();

//    public List<IComponent> getChildren();

    public void setType(VisuType t, Boolean... param);

    public VisuType getType();
    
    public void setMarking(VisuMarking vm) ;
    
    public VisuMarking getMarking();
   
}
