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
package kodemat.visu.editor.selection;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import kodemat.visudata.visuComponents.VisuComponent;

/**
 *
 * @author Kipouridis
 */
public interface IVisuSelectable {

    /**
     *Type of the selected object
     */
    public short objectType = 0;
    /**
     * Set the current VisuComponent for the selected object
     * @param selectedModel
     * @param selectedSpatial
     * @return 
     */
    public boolean setVisuCompToSelected(VisuComponent selectedModel, Spatial selectedSpatial);

    /**
     * Set the current VisuComponent for the selected object, calls the findSpatial method
     * that will assign the spatial to the object
     * @param selectedModel
     * @return 
     */
    public boolean setVisuCompToSelected(VisuComponent selectedModel);

    /**
     * Transverses the scenegraph to find the spatial for the given VisuComponent
     * @param name 
     */
    void findSpatial(final String name);
//The method is a bit erroneous, should be replaced with a clear interface

    /**
     * Set the spatial to the selectedObject
     * @param spatial
     * @return 
     */
    public void setObjectSpatial(Spatial spatial);

    /**
     * Rotate the object 5 degree to the right
     */
    public void rotateRightGradual() throws UnsupportedOpperationOnComponentException;

    /**
     * Rotate the object the given amount of degrees
     * @param deg 
     */
    public void rotate(float deg) throws UnsupportedOpperationOnComponentException;

    /**
     * Scele the object to the given parameter
     * @param scale 
     */
    public void scale(float scale) throws UnsupportedOpperationOnComponentException;

    /**
     * Delete current object
     */
    public void delete() throws UnsupportedOpperationOnComponentException;

    public void showOrHideNote();

    public void showBigNote();

    public void createBoundingBox() throws UnsupportedOpperationOnComponentException;

    public void removeBoundingBox()throws UnsupportedOpperationOnComponentException;

    public void move(Vector3f direction, float factor)throws UnsupportedOpperationOnComponentException;


    public VisuComponent getModelParent()throws UnsupportedOpperationOnComponentException;

    public Spatial getSpatial();

    public boolean isEmpty();
}
