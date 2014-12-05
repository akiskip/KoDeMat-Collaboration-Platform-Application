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
package kodemat.visu.input.camera.controller;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.io.IOException;

public class KeepSizeControl extends AbstractControl {

    private float factor;

    public KeepSizeControl() {
        super();
        factor = 10f;
    }

    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        Camera cam = vp.getCamera();
        fixSize(cam);
    }

    private void fixRefreshFlags() {
// force transforms to update below this node
        spatial.updateGeometricState();

// force world bound to update
        Spatial rootNode = spatial;
        while (rootNode.getParent() != null) {
            rootNode = rootNode.getParent();
        }

        rootNode.getWorldBound();
    }

    private void fixSize(Camera cam) {
        Vector3f camPosition = cam.getLocation();
        Vector3f nodePosition = spatial.getLocalTranslation();

        float scale = camPosition.subtract(nodePosition).length() / 100f
                * factor;

        spatial.setLocalScale(scale);
        fixRefreshFlags();
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public float getFactor() {
        return factor;
    }

    @Override
    public void write(JmeExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(factor, "factor", 0f);
    }

    @Override
    public void read(JmeImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        factor = capsule.readFloat("factor", 0f);
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}