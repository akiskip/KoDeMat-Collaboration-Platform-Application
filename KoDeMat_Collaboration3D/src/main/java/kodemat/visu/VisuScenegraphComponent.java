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
package kodemat.visu;

import com.jme3.asset.TextureKey;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.texture.Texture;
import com.jme3.ui.Picture;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import kodemat.visu.input.camera.SavableUserDataWrapper;
import kodemat.visudata.VisuChange;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuInterpolation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.change.VisuInterpolationChange;
import kodemat.visudata.change.VisuLabelChange;
import kodemat.visudata.change.VisuMarkingChange;
import kodemat.visudata.change.VisuParentChange;
import kodemat.visudata.change.VisuReferenceToEdgeChange;
import kodemat.visudata.change.VisuRotationChange;
import kodemat.visudata.change.VisuScaleChange;
import kodemat.visudata.change.VisuTranslationChange;
import kodemat.visudata.change.VisuTypeChange;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuScenegraphComponent {

    List<VisuChange> pendingChanges = new LinkedList<VisuChange>();
    Spatial spatial;
    private final ScenegraphEventAssembler eventAssembler;
    private boolean firstTranslation = true;
    private boolean firstRotation = true;
    private boolean interpolateTranslation = true;
    private boolean interpolateRotation = true;
    private boolean interpolateScale = true;
    private HashMap<String, ArrayList<String>> spatialEdges;
    public final String idInfo;
    public final long id;
    private Long parentId = null;
    private Long edgeReference = null;
    private Vector3f originalTranslation = null;
    private Vector3f targetTranslation = null;

    VisuScenegraphComponent(long id, String idInfo, final ScenegraphEventAssembler eventAssembler, HashMap<String, ArrayList<String>> spatialEdges) {
        this.idInfo = idInfo;
        this.eventAssembler = eventAssembler;
        this.spatialEdges = spatialEdges;
        this.id = id;
    }

    public void delete() {
        if (spatial != null) {
            Node parent = spatial.getParent();
            if (parent != null) {
                parent.detachChild(spatial);
            }
            spatial = null;
        }
    }

    public void processChange(VisuChange c) {
        if (c instanceof VisuTypeChange) {
            Node parent = null;
            Spatial old = null;
            if (spatial != null) {
                parent = spatial.getParent();
                if (parent != null) {
                    parent.detachChild(spatial);
                }
                old = spatial;
                spatial = null;

            }
            if (((VisuTypeChange) c).getValue() != null) {

                createNewSpatialFromType((VisuTypeChange) c);
                if (spatial != null && old != null) {
                    spatial.setLocalTransform(old.getLocalTransform());
                    spatial.setName(old.getName());
                }
                VisuComponent component = eventAssembler.helper.getComponent(((VisuTypeChange)c).getId());
                if ( spatial != null) {
                    System.out.println("VisuScenegraphComponent: added component \"" + component.getId() + " " + component.getName() + "\" to " + spatial.toString());
                    spatial.setName(component.getName());
                    spatial.setUserData(VisuComponent.class.getName(), new SavableUserDataWrapper(component));
                    if (parent != null) {
                        parent.attachChild(spatial);
                    } else {
                        eventAssembler.rootNode.attachChild(spatial);
                    }
                    applyPendingChanges();
                }
            } else {
                System.out.println("WARNING: Type for " + ((VisuTypeChange)c).getId() + " is null");
            }

        } else if (c instanceof VisuParentChange) {
            VisuParentChange vpc = (VisuParentChange) c;
            if (spatial != null) {
                moveToNewParent(vpc);
            } else {
                pendingChanges.add(c);
            }
        } else if (c instanceof VisuReferenceToEdgeChange) {
            if (spatial != null) {
                VisuReferenceToEdgeChange vrtec = (VisuReferenceToEdgeChange) c;
                if (edgeReference != null) {
                    eventAssembler.edgeReferences.remove(edgeReference, this);
                }
                edgeReference = vrtec.getValue();
                eventAssembler.edgeReferences.put(edgeReference, this);
                updateParentInformation();
                updateReferenceToEdge();
            } else {
                pendingChanges.add(c);
            }
        } else {
            if (spatial != null) {
                applyAttributeChangeToSpatial(c);
            } else {
                pendingChanges.add(c);
            }
        }
    }

    private void applyPendingChanges() {
        List<VisuChange> copy = new LinkedList<>(pendingChanges);
        for (VisuChange visuChange : copy) {
            processChange(visuChange);
        }
        pendingChanges.clear();
    }

    private void applyAttributeChangeToSpatial(VisuChange c) {
        if (c instanceof VisuTranslationChange) {
            VisuTranslationChange vtc = (VisuTranslationChange) c;
            if (vtc.getValue() != null) {
                Vector3f target = ScenegraphEventAssembler.convert(vtc.getValue());
                if (spatial != null && spatial.getParent() != null) {
                    //System.out.println("TRANS: " + target.toString() + ",SPATIAL: "+spatial.getName()+" PARENT: " + spatial.getParent().getName());
                } else {
                    // System.out.println("TRANS: " + target.toString() + ",SPATIAL: "+spatial.getName()+ ", no parent?");
                }
                originalTranslation = target;
                if (edgeReference != null) {
                    computeReferenceToEdge();
                    target = targetTranslation;
                }
                if (!firstTranslation && interpolateTranslation) {
                    VisuScenegraphComponent.TranslationInterpolationControl control = spatial.getControl(VisuScenegraphComponent.TranslationInterpolationControl.class);
                    if (control == null) {
                        control = new VisuScenegraphComponent.TranslationInterpolationControl(target, 0.2f);
                        spatial.addControl(control);
                    } else {
                        control.setTarget(target);
                    }
                } else {
                    spatial.setLocalTranslation(target);
                    firstTranslation = false;
                }
                ArrayList<String> list = spatialEdges.get(spatial.getName());
                if (list != null) {
                    for (String msg : list) {
                        createEdge(msg);
                    }
                }
            }
        } else if (c instanceof VisuRotationChange) {
            VisuRotationChange vrc = (VisuRotationChange) c;
            if (vrc.getValue() != null) {
                Quaternion target = ScenegraphEventAssembler.convert(vrc.getValue());
                if (!firstRotation && interpolateRotation) {
                    VisuScenegraphComponent.RotationInterpolationControl control = spatial.getControl(VisuScenegraphComponent.RotationInterpolationControl.class);
                    if (control == null) {
                        control = new VisuScenegraphComponent.RotationInterpolationControl(target, 0.1f);
                        spatial.addControl(control);
                    } else {
                        control.setTarget(target);
                    }
                } else {
                    spatial.setLocalRotation(target);
                    firstRotation = false;
                }
            }
        } else if (c instanceof VisuScaleChange) {
            VisuScaleChange vsc = (VisuScaleChange) c;
            if (vsc.getValue() != null) {
                spatial.setLocalScale(ScenegraphEventAssembler.convert(vsc.getValue()));
            }
        } else if (c instanceof VisuLabelChange) {
            VisuLabelChange vlc = (VisuLabelChange) c;
            if (vlc.getValue() != null) {
                if (spatial instanceof Node) {
                    Node n = (Node) spatial;
                    n.detachChildNamed("BMPText");
                    n.attachChild(createText(vlc.getValue()));
                }
                else {
                    eventAssembler.addChangeToLaterQueue(vlc);
                }

            } else {
                spatial.setName("" + vlc.getId());
                if (spatial instanceof Node) {
                    Node n = (Node) spatial;
                    n.detachChildNamed("BMPText");
                 
                }
            }
        } 
        //TODO: finish it
        else if (c instanceof VisuMarkingChange) {
            VisuMarkingChange vmc = (VisuMarkingChange) c;
            
            if (vmc.getValue() != null) {
                if (vmc.getValue().getBoundingBoxEnabled()==1 && spatial instanceof Node) {
                  System.out.println("visumarking label "+vmc.getValue().getLabel());
                   this.attachBoundingBox(id, spatial);
                } else if (vmc.getValue().getBoundingBoxEnabled()==0) {
                     this.removeBoundingBox(id, spatial);
                }
            } 
        } 
        else if (c instanceof VisuInterpolationChange) {
            VisuInterpolationChange vic = (VisuInterpolationChange) c;
            if (vic.getValue() != null) {
                VisuInterpolation vi = vic.getValue();
                interpolateTranslation = vi.translation;
                interpolateRotation = vi.rotation;
                interpolateScale = vi.scale;
            } else {
                interpolateTranslation = true;
                interpolateRotation = true;
                interpolateScale = true;
            }
        }
    }

    private void createEdge(String msg) {
        Node lineChild = (Node) eventAssembler.rootNode.getChild(msg);
        if (lineChild != null) {
            eventAssembler.rootNode.detachChild(lineChild);
        }
        String initialSP = msg.split("-")[1];
        String finalSP = msg.split("-")[2];

        Vector3f beginning = getWorldTranslation(initialSP);
        Vector3f endPoint = getWorldTranslation(finalSP);
        Material arrowMat = new Material(eventAssembler.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        arrowMat.getAdditionalRenderState().setWireframe(true);
        arrowMat.setColor("Color", ColorRGBA.Red);
        Line line = new Line(beginning, endPoint);
        line.setLineWidth(3);
        Node lineNode = new Node(msg);
        Geometry geometry = new Geometry("Geometry " + msg, line);
        geometry.setMaterial(arrowMat);
        lineNode.attachChild(geometry);

        eventAssembler.rootNode.attachChild(lineNode);

        String initialParent = getParentName(initialSP);
        ArrayList<String> list = spatialEdges.get(initialParent);
        if (list == null) {
            list = new ArrayList<String>();
        }
        list.add(msg);
        spatialEdges.put(initialParent, list);

        String finalParent = getParentName(finalSP);
        list = spatialEdges.get(finalParent);
        if (list == null) {
            list = new ArrayList<String>();
        }
        if (!list.contains(msg)) {
            list.add(msg);
        }
        spatialEdges.put(finalParent, list);
    }
    private Vector3f trans;

    private Vector3f getWorldTranslation(final String spatialName) {
        SceneGraphVisitor visitor = new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
                if (spatial.getName() != null) {
                    if (spatial.getName().equals(spatialName)) {
                        BoundingVolume volume = spatial.getWorldBound();

                        trans = ((BoundingBox) volume).getCenter(new Vector3f());
//                        trans = spatial.getWorldTranslation();
                    }
                }
            }
        };
        eventAssembler.rootNode.breadthFirstTraversal(visitor);
        return trans;
    }
    private String parentName;

    private String getParentName(final String spatialName) {
        SceneGraphVisitor visitor = new SceneGraphVisitor() {
            @Override
            public void visit(Spatial spatial) {
                if (spatial.getName() != null) {
                    if (spatial.getName().equals(spatialName)) {
                        parentName = spatial.getParent().getParent().getName();
                    }
                }
            }
        };
        eventAssembler.rootNode.breadthFirstTraversal(visitor);
        return parentName;
    }

    private void moveToNewParent(VisuParentChange vpc) {
        if (parentId != null) {
            eventAssembler.childComponents.remove(parentId, this);
        }
        parentId = vpc.getValue();
        eventAssembler.childComponents.put(parentId, this);
        updateParentInformation();
//        if (parentId == null) {
//            Node parent = spatial.getParent();
//            if (parent != null) {
//                parent.detachChild(spatial);
//            }
//            eventAssembler.rootNode.attachChild(spatial);
//        } else {
//            eventAssembler.childComponents.put(parentId, this);
//            VisuScenegraphComponent et = eventAssembler.componentTrackers.get(vpc.getValue());
//            if (et != null && et.spatial instanceof Node) {
//                Node parent = spatial.getParent();
//                if (parent != null) {
//                    parent.detachChild(spatial);
//                }
//                ((Node) et.spatial).attachChild(spatial);
//            } else {
//                eventAssembler.addChangeToLaterQueue(vpc);
//            }
//        }
    }

    public void updateParentInformation() {
        //if there is no parent or if there is an edge reference then the root node will be taken as JME parent
        if (parentId == null || edgeReference != null) {
            Node parent = spatial.getParent();
            if (parent != null) {
                parent.detachChild(spatial);
            }
            eventAssembler.rootNode.attachChild(spatial);
        } else {
            VisuScenegraphComponent et = eventAssembler.componentTrackers.get(parentId);
            if (et != null) {
                if (et.spatial instanceof Node) {
                    if (spatial != null) {
                        Node parent = spatial.getParent();
                        if (parent != null) {
                            parent.detachChild(spatial);
                        }
                        ((Node) et.spatial).attachChild(spatial);
                    } else {
                        System.out.println("VisuScenegraphComponent.updateParent():  spatial is nullF");
                    }
                } else {
                    System.out.println("VisuScenegraphComponent.updateParent(): Parent spatial is null or not a JME Node");
                }
            } else {
                System.out.println("VisuScenegraphComponent.updateParent(): Could not find parent scenegraph component");
            }
        }
    }

    /**
     * Creates a translation change which will trigger the new computation of
     * the actual local translation So, if the edge changed values then the new
     * actual local translation will be calculated even if the orginal
     * translation value did not change.
     */
    public void updateReferenceToEdge() {
        if (originalTranslation != null) {
            VisuTranslationChange vtc = new VisuTranslationChange(new VisuVector3f(originalTranslation.x, originalTranslation.y, originalTranslation.z), id);
            pendingChanges.add(vtc);
        }
    }

    private void computeReferenceToEdge() {
        if (edgeReference != null) {
            Node parent = spatial.getParent();
            if (parent != null) {
                parent.detachChild(spatial);
            }
            VisuScenegraphEdge edge = eventAssembler.edgeTrackers.get(edgeReference);
            if (edge != null) {
                VisuScenegraphComponent source = edge.getSource();
                VisuScenegraphComponent target = edge.getTarget();
                if (source == null) {
                    System.out.println("VisuScenegraphComponent.updateReferenceToEdge(): source == null");
                }
                if (target == null) {
                    System.out.println("VisuScenegraphComponent.updateReferenceToEdge(): target == null");
                }
                if (originalTranslation == null) {
                    System.out.println("VisuScenegraphComponent.updateReferenceToEdge(): originalTranslation == null");
                }
                if (source != null && target != null && originalTranslation != null) {
                    Vector3f beginning = source.spatial.getWorldTranslation();
                    Vector3f endPoint = target.spatial.getWorldTranslation();

                    Vector3f loc = beginning.clone();


                    loc.interpolate(endPoint, originalTranslation.x);

                    eventAssembler.rootNode.attachChild(spatial);

//                    Node parent1 = spatial.getParent();
//                    parent1.worldToLocal(loc, null);

//                    spatial.setLocalTranslation(loc);

                    targetTranslation = loc;

                } else {
                    System.out.println("VisuScenegraphComponent.updateReferenceToEdge(): source or target of edge not found OR no translation yet");
                }

            } else {
                System.out.println("VisuScenegraphComponent.updateReferenceToEdge(): Could not find edge scenegraph component");
                //There is no edge componenet currently registered, may be the edge does not exist (yet) or the events are coming
                //in such a sequence that the edge events are coming later; anyway, we detach the spatial from its parent

            }
        } else {
            //do nothing, updateParent will be called and that will handle the new position in the parent hierarchy
        }
    }

    private void createNewSpatialFromType(VisuTypeChange vtc) {
        if (vtc.getValue() == null) {
            System.out.println("WARNING VisuTypeChanged to null");
            return;
        }
        if (VisuType.MODEL.equals(vtc.getValue().type)) {
            createModel(vtc.getId(), vtc.getValue().path);
        } else if (VisuType.NODE.equals(vtc.getValue().type)) {
            createNode(vtc.getId());
        } else if (VisuType.TEXTURE.equals(vtc.getValue().type)) {
            createTexture(vtc.getId(), vtc.getValue().path);
        } else if (VisuType.BOX.equals(vtc.getValue().type)) {
            createBox(vtc.getId());
        }
    }

    private void createModel(final long id, String path) {
        String[] split = path.split(";");
        path = split[0];
        String texturePath = split.length > 1 ? split[1] : null;

        if (!path.contains("png")) {
          
                Node n = new Node("" + id);
                Spatial loadModel = eventAssembler.assetManager.loadModel(path);

                if (texturePath != null) {
                    Texture texture = eventAssembler.assetManager.loadTexture(texturePath);
                    Material mat = new Material(eventAssembler.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");

                    mat.setColor("Color", ColorRGBA.White);
                    mat.setTexture("ColorMap", texture);
                    loadModel.setMaterial(mat);
                }


                n.attachChild(loadModel);
                n.setUserData("modelPath", path);
                spatial = n;
            }
         else {  //if it is a texture, eg used for floor..
            BillboardControl bc = new BillboardControl();
            bc.setAlignment(BillboardControl.Alignment.Screen);
            Node holder = new Node("" + id);
            Picture bubble = new Picture("");
            bubble.setImage(eventAssembler.assetManager, path, true);

            bubble.setIgnoreTransform(false);
            bubble.setQueueBucket(Bucket.Transparent);
            bubble.setPosition(-0.5f, 0);
            bubble.scale(1.5f);
            holder.attachChild(bubble);

            holder.addControl(bc);
            spatial = holder;

        }
    }

    public void getSpatialFromModel(String modelName){
                final String name = modelName.split("_")[1];
                SceneGraphVisitor visitor = new SceneGraphVisitor() {
                    @Override
                    public void visit(Spatial spat) {
                        if (spat.getName() != null) {
                            if (spat.getName().equals(name)) {
       
                                attachBoundingBox(id, spat);
                            }
                        }
                    }
                };

                eventAssembler.rootNode.breadthFirstTraversal(visitor);
    }

    
    
    protected void attachBoundingBox(long id, Spatial geom) {
        BoundingVolume bound = geom.getWorldBound();
//       check if the selected component already has a BB
        if (bound instanceof BoundingBox &&((Node)geom).getChild("BoundingBox")==null ){
            BoundingBox bbox = (BoundingBox) bound;
            Vector3f extent = new Vector3f();
            bbox.getExtent(extent);
            WireBox wireBox = new WireBox();
            wireBox.fromBoundingBox(bbox);
            final Geometry selectionGeometry = new Geometry("BB" + id, wireBox);
//            selectionGeometry.setLocalTranslation(geom., extent.y*2, id);
            Material mat_box = new Material(eventAssembler.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat_box.setColor("Color", ColorRGBA.Blue);
            selectionGeometry.setMaterial(mat_box);
  
//            selectionGeometry.setLocalTranslation(bbox.getCenter());

            Node selectionShape = new Node("BoundingBox");
            selectionShape.attachChild(selectionGeometry);
            
            selectionShape.setLocalRotation(geom.getLocalRotation());
            Vector3f pos = geom.getParent().getLocalTranslation();
            selectionShape.setLocalTranslation(pos.x,pos.y+extent.y,pos.z);
          
            ((Node)geom).attachChild(selectionShape);

        }
    }

    public void removeBoundingBox(long id, Spatial geom){
         if (((Node)geom).getChild("BoundingBox")!=null){
            ((Node)geom).detachChildNamed("BoundingBox");
         }
         
        
    }
    
    private void createNode(long id) {
        spatial = new Node("" + id);
    }

    private void createTexture(long id, String path) {
        Material floor_mat = new Material(eventAssembler.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key = new TextureKey(path);
        key.setGenerateMips(true);
        Texture tex = eventAssembler.assetManager.loadTexture(key);
        tex.setWrap(Texture.WrapMode.Clamp);
        floor_mat.setTexture("ColorMap", tex);
        Geometry floor_geo = new Geometry("Floor", new Box(Vector3f.ZERO, 10f, 0f, 10f));
        floor_geo.setMaterial(floor_mat);
        floor_geo.setLocalTranslation(0, 0f, 0);
        spatial = floor_geo;
    }

    private void createBox(long id) {
        Geometry box1 = new Geometry("Box " + id, new Box(Vector3f.ZERO, 0.5f, 0.3f, 0.5f));
//        Material mat_default = new Material(eventAssembler.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat_default.setColor("Color", ColorRGBA.Gray.clone());
//        box1.setMaterial(mat_default);
        Material mat = new Material(eventAssembler.assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 32f);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.White.clone());
        mat.setColor("Diffuse", ColorRGBA.Gray.clone());
        mat.setColor("Specular", ColorRGBA.Gray.clone());
        box1.setMaterial(mat);
        box1.setLocalTranslation(0, 0.15f, 0);
        Node n = new Node("" + id);
        n.attachChild(box1);
        spatial = n;
    }

    private BitmapText createText(String label) {
        BitmapFont fnt = eventAssembler.assetManager.loadFont("Interface/Fonts/Default.fnt");

        BitmapFont polyFnt = new BitmapFont();
        polyFnt.setCharSet(fnt.getCharSet());
        Material[] matArray = new Material[fnt.getPageSize()];
        for (int i = 0; i < fnt.getPageSize(); i++) {
            Material mat = fnt.getPage(i);
            matArray[i] = new Material(mat.getMaterialDef());
            matArray[i].setTexture("ColorMap", mat.getTextureParam("ColorMap").getTextureValue());
            matArray[i].setBoolean("VertexColor", (Boolean) mat.getParam("VertexColor").getValue());
            matArray[i].getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
            matArray[i].getAdditionalRenderState().setPolyOffset(-1.0f, 1f);
        }
        polyFnt.setPages(matArray);

        BitmapText bmpText = new BitmapText(polyFnt, false);
        bmpText.setName("BMPText");
        bmpText.setQueueBucket(Bucket.Translucent);
        bmpText.setText(label);
        bmpText.setSize(0.5f);
        bmpText.updateGeometricState();


        if (bmpText.getControl(BillboardControl.class) == null) {
            bmpText.addControl(
                    new BillboardControl());
        }
        bmpText.setColor(ColorRGBA.White.clone());
//        bmpText.setBox(new Rectangle(-1f, -0.25f, 2f, 0.5f));
//        bmpText.setAlignment(BitmapFont.Align.Center);

        if (spatial != null) {  //place it on top of the object
            BoundingVolume volume = spatial.getWorldBound();
            if (volume instanceof BoundingBox) {
                Vector3f extent = ((BoundingBox) volume).getExtent(null);
                bmpText.setLocalTranslation(0f, 2 * extent.y + 0.95f, 0f);
            }
        }
        return bmpText;


    }

    class TranslationInterpolationControl extends AbstractControl {

        Vector3f source;
        Vector3f target;
        final float duration;
        float current = 0f;
        boolean started = false;

        public TranslationInterpolationControl(Vector3f target, float duration) {
            super();
            this.target = target;
            this.duration = duration;

        }

        public void setTarget(Vector3f target) {
            this.target = target;
            source = spatial.getLocalTranslation().clone();
            current = 0f;
        }

        @Override
        protected void controlUpdate(float tpf) {
            if (!started) {
                source = spatial.getLocalTranslation().clone();
                started = true;
            }
            if (current < duration) {
                float percentage = current / duration;
                Vector3f interpolation = new Vector3f();//spatial.getLocalTranslation().clone();
                interpolation.interpolate(source, target, percentage);
                spatial.setLocalTranslation(interpolation);
                eventAssembler.updateChildrenAndEdges(id);
                current += tpf;
            } else {
                this.spatial.removeControl(this);
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }

        @Override
        public Control cloneForSpatial(Spatial spatial) {
            return new VisuScenegraphComponent.TranslationInterpolationControl(target.clone(), duration);
        }
    }

    class RotationInterpolationControl extends AbstractControl {

        Quaternion source;
        Quaternion target;
        final float duration;
        float current = 0f;
        boolean started = false;

        public RotationInterpolationControl(Quaternion target, float duration) {
            super();
            this.target = target;
            this.duration = duration;

        }

        public void setTarget(Quaternion target) {
            this.target = target;
            source = spatial.getLocalRotation().clone();
            current = 0f;
        }

        @Override
        protected void controlUpdate(float tpf) {
            if (!started) {
                source = spatial.getLocalRotation().clone();
                started = true;
            }
            if (current < duration) {
                float percentage = current / duration;
                spatial.setLocalRotation(spatial.getLocalRotation().slerp(source, target, percentage));
                eventAssembler.updateChildrenAndEdges(id);
                current += tpf;
            } else {
                this.spatial.removeControl(this);
            }
        }

        @Override
        protected void controlRender(RenderManager rm, ViewPort vp) {
        }

        @Override
        public Control cloneForSpatial(Spatial spatial) {
            return new VisuScenegraphComponent.RotationInterpolationControl(target.clone(), duration);
        }
    }

    public String toString() {
        return "Comp{" + "id=" + idInfo + '}';
    }
}
