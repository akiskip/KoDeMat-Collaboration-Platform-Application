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

import kodemat.visu.editor.nifty.panels.DragPanelEditor;
import kodemat.visu.editor.nifty.panels.UndoRedoEditor;
import kodemat.visu.editor.nifty.panels.BasicEditorPanel;
import kodemat.visu.editor.nifty.panels.NoteHistoryEditor;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ITopic;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.AbstractController;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;
import graphXML.ExportGEXF;
import graphXML.ImportGEXF;
import jade_layout.EHBAgentenTopicFrame;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import kodemat.PA.HandoverEventManager;
import kodemat.admin.VisuKodematUser;
import kodemat.tele.test.TelegramField;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;
import kodemat.versioning.visuHistory.VisuHistoryEvent;
import kodemat.visu.appstates.CameraControllerAppState;
import kodemat.visu.appstates.NiftyGuiAppState;
import kodemat.visu.appstates.VisuHazelcastAppState;
import kodemat.visu.editor.nifty.panels.ModelEditWindowPanel;
import kodemat.visu.input.mouse.ClickSelection;
import kodemat.visu.editor.selection.JMESelectionHandler;
import kodemat.visu.editor.userActions.EdgeUIActionsFacade;
import kodemat.visu.editor.userActions.ImportModelUIAction;
import kodemat.visu.editor.userActions.NotesUIActionsFacade;
import kodemat.visu.editor.userActions.SceneUIActionsFacade;
import kodemat.visu.swing.tables.EHBTelegramSyncedTable;
import kodemat.PA.HandoverManagerWindow;
import kodemat.visu.swing.tables.TeleEditorTable;
import kodemat.visu.swing.tables.HistorySyncedTable;
import kodemat.visu.swing.tables.InfoSyncedTable;
import kodemat.visu.swing.tables.UsersSyncedTable;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuEdge;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;

/**
 * Class that contains handler methods for the UI
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuEditor implements IEditor {

    SimpleApplication visuClient;
    //TODO: check if editorManager is needed
    EditorManager editorManager;
    CameraControllerAppState cameraControllerAppState;
    NiftyGuiAppState guiAppState;
    private InputManager inputManager;
    Element editorPanel;
    private ClickSelection<VisuComponent> markerSelection = null;
    EditorPanelController editorPanelController;
    VisuHelper visuHelper;
    private VisuHazelcastAppState hazelcastAppState;
    JMESelectionHandler currentNoteHandler;
    JMESelectionHandler selectedObjectHandler;
    private JMESelectionHandler unSelectedObject;
    public static final String LOCAL_UNDO_BUTTON = "BasicEditor_LOCAL_UNDO_BUTTON";
    public static final String LOCAL_REDO_BUTTON = "BasicEditor_LOCAL_REDO_BUTTON";
    public static final String GLOBAL_BUTTON = "BasicEditor_GLOBAL_BUTTON";
    public static final String MODIFICATION_MENU_BUTTON = "BasicEditor_MODIFICATION_BUTTON";
    public static final String ESC_BUTTON = "BasicEditor_ESC_BUTTON";
    private KeyListener keyListener = new KeyListener();
    private boolean waitForClick_Move = false;
    private boolean openHistory = false;
    private int numberClicks = 0;
    private SceneUIActionsFacade modelEditActionHandler;
    private EdgeUIActionsFacade edgeUIHandler;
    private NotesUIActionsFacade notesUIHandler;
    private final HazelcastInstance hazelcastClient;
    private ImportModelUIAction importModelUIHandler;
    private ClickSelection<VisuComponent> lastCursorSelection = null;
    private boolean clickToSelect;


    @Override
    public VisuHazelcastAppState getHazelcastAppState() {
        return hazelcastAppState;
    }

    public VisuEditor(HazelcastInstance hazelcastClient) {
        this.hazelcastClient = hazelcastClient;
    }

    @Override
    public void initialize(Application visuClient) {

        System.out.println("INITIALIZE BasicEditor START");

        this.visuClient = (SimpleApplication) visuClient;
        //initialize ActionHandlers 
        modelEditActionHandler = new SceneUIActionsFacade(this);
        edgeUIHandler = new EdgeUIActionsFacade(this);
        notesUIHandler = new NotesUIActionsFacade(this);
        importModelUIHandler = new ImportModelUIAction(this);


//        get the camera controller
        this.cameraControllerAppState = this.getCammeraControllAppState();
        this.guiAppState = visuClient.getStateManager().getState(NiftyGuiAppState.class);
        this.hazelcastAppState = visuClient.getStateManager().getState(VisuHazelcastAppState.class);
        this.inputManager = guiAppState.getInputManager();
        hazelcastAppState.getClient();

        visuClient.getCamera().setLocation(new Vector3f(0f, 35f, 10f));
         
        //initialise core variables
        this.getVisuHelper();

        this.createEssentials();
        //init 
        this.initExtraInputMapping();
       
        System.out.println("INITIALIZE BasicEditor END");
    }

 
    
    
    @Override
    public void initExtraInputMapping() {
        inputManager.addMapping(LOCAL_UNDO_BUTTON, new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping(LOCAL_REDO_BUTTON, new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping(GLOBAL_BUTTON, new KeyTrigger(KeyInput.KEY_O));

        inputManager.addMapping(MODIFICATION_MENU_BUTTON, new KeyTrigger(KeyInput.KEY_F4));
        inputManager.addMapping(ESC_BUTTON, new KeyTrigger(KeyInput.KEY_ESCAPE));
       
        inputManager.addListener(keyListener, LOCAL_UNDO_BUTTON);
        inputManager.addListener(keyListener, LOCAL_REDO_BUTTON);
        inputManager.addListener(keyListener, GLOBAL_BUTTON);
        inputManager.addListener(keyListener, MODIFICATION_MENU_BUTTON);
        inputManager.addListener(keyListener, ESC_BUTTON);

    }

    @Override
    public void initUI() {
        //initialise Nifty Panels

        BasicEditorPanel bep = new BasicEditorPanel(visuClient);
        bep.layout(this);


//        SimpleModificationsEditor sme = new SimpleModificationsEditor(visuClient);
        ModelEditWindowPanel sme = new ModelEditWindowPanel(visuClient);
        sme.layout(this);


        UndoRedoEditor ure = new UndoRedoEditor(visuClient);
        ure.layout(this);

        NoteHistoryEditor nhe = new NoteHistoryEditor(visuClient);
        nhe.layout(this);

        DragPanelEditor dpe = new DragPanelEditor(visuClient);
        dpe.layout(this);
    }

    public void loadSceneFromAsset() {
        Node scene = (Node) visuClient.getAssetManager().loadModel("Scenes/CeMatScene_PA.j3o");
        importModelUIHandler.addComponentsFromBlenderScene(scene);
    }

    public SimpleApplication getVisuClient() {
        return visuClient;
    }

    public void getCompInfo() {
//       this.getVisuHelper().getComponentHistories().get(10).getVisuDataList()
    }

    public CameraControllerAppState getCammeraControllAppState() {
        return visuClient.getStateManager().getState(CameraControllerAppState.class);
    }

    public void test() {
        if (getMarkerSelection() != null) {
            VisuComponent comp = getMarkerSelection().getComponent();
            System.out.println("Clicked Test on " + comp.getName());
        }
        Logger root = Logger.getLogger("");
        Handler[] handlers = root.getHandlers();
        System.out.println("LOGGING HANDLERS");

        for (int i = 0; i < handlers.length; i++) {
            if (handlers[i] instanceof ConsoleHandler) {
                ((ConsoleHandler) handlers[i]).setLevel(Level.WARNING);
            }
//            System.out.println(handlers[i].);
        }
    }
//    TeleEditorTable teleEditor;

    public void teleEditorTest() {

//        loadSceneFromAsset();

          UsersSyncedTable<VisuKodematUser, VisuKodematUser> usersTable = new UsersSyncedTable(this.visuHelper.getUsers(), VisuKodematUser.class, "Users Connected");
         
        
        if (this.visuHelper != null && selectedObjectHandler != null) {
            IMap<Integer, VisuComponentInfoEntry> ehbOrdersMap = this.visuHelper.getEHBOrdersMap();
          EHBTelegramSyncedTable<VisuComponentInfoEntry, VisuComponentInfoEntry> ordersEditor = new EHBTelegramSyncedTable(this.visuHelper.getEHBOrdersMap(), this.getVisuHelper(), VisuComponentInfoEntry.class, "EHB Order Editor");
      HandoverEventManager  manager = new HandoverEventManager(this.visuHelper);
      manager.showManagerGUI();
//long SelectedId = selectedObject.getModel().getId()
    
        
        } 
    }

    public void createEdge() {
        edgeUIHandler.createEdge();
    }

    public void moveTo() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        String text = "";
        Element e = nifty.getCurrentScreen().findElementByName("comment");
        if (!waitForClick_Move) {
            waitForClick_Move = true;
            text = "            ||Click anywhere to start moving the object.";
            numberClicks = 1;
        } else {
            waitForClick_Move = false;
        }

        if (e != null) {
            e.getRenderer(TextRenderer.class).setText(text);
            e.setConstraintWidth(new SizeValue(e.getRenderer(TextRenderer.class).getTextWidth() + ""));
            screen.layoutLayers();
        }

    }

    @Override
    public VisuHelper getVisuHelper() {
        if (hazelcastClient != null && visuHelper == null) {
            this.visuHelper = new VisuHelper(hazelcastClient);

            if (visuHelper == null) {
                throw new IllegalStateException("VisuHelper is not created!!");
            }
        }
        return this.visuHelper;
    }

    public void createEssentials() {

        if (hazelcastAppState == null) {
            this.hazelcastAppState = visuClient.getStateManager().getState(VisuHazelcastAppState.class);
        }
        if (selectedObjectHandler == null) {
            selectedObjectHandler = new JMESelectionHandler(this.visuClient, this.visuHelper, true);
        }
//        if (currentNoteHandler == null) {
//            this.currentNoteHandler = new JMESelectionHandler(this.visuClient, this.visuHelper,  false);
//        }

    }

    public void openModificatorPanel() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        Element e = screen.findElementByName("selected ");

            Element e2 = nifty.getCurrentScreen().findElementByName("drag-panel");
            e2.setFocusable(true);
            e2.setVisible(true);
            screen.findElementByName("rotation-slider").setVisible(false);
            screen.findElementByName("rotation-slider").setFocusable(false);
//               VisuComponent selectedComp = selectedObjectHandler.getSelectedModel();
//               if(selectedComp != null){
//                 try{
//          
//            float rot;
//          
//            if (selectedComp.getRotation() == null) {
//                rot = 0;
//            } else {
//                rot = selectedComp.getRotation().y;
//            }
//            nifty.getCurrentScreen().findNiftyControl("rotation-slider", Slider.class).setValue(rot);
//            e.getRenderer(TextRenderer.class).setText("Selected object: " + selectedObjectHandler.getSelectedModel().getName());
//             e.setConstraintWidth(new SizeValue(e.getRenderer(TextRenderer.class).getTextWidth() + ""));
//            }catch(Exception ex){
//                System.out.println("Exeption in mod panel "+ex);
//            }
             
     Element   win = screen.findElementByName("editModelWindow");
        win.setVisible(true);
        screen.layoutLayers();

   
    }

    public void closeModificator() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");

     
        Element e2 = nifty.getCurrentScreen().findElementByName("editModelWindow");
        e2.setFocusable(false);
        e2.setVisible(false);

        updateSelectedComponentsMap(null);
      
    }

    public void showHistory() {
        if (this.visuHelper != null && selectedObjectHandler.getSelectedModel() != null) {
            if(selectedObjectHandler.getSelectedModel().getName().contains("stapler")){
           HistorySyncedTable<TelegramField, TelegramField> historyEditor = new HistorySyncedTable(this.getVisuHelper().getTelegrams(), TelegramField.class, selectedObjectHandler.getSelectedModel().getName());

            }
            if(selectedObjectHandler.getSelectedModel().getName().contains("ehb")){
                ITopic topic = hazelcastClient.getTopic("katze1Topic");
       JFrame agentenTopicFrame =   new EHBAgentenTopicFrame(topic);
       agentenTopicFrame.setVisible(true);
       agentenTopicFrame.setTitle("Messages from "+selectedObjectHandler.getSelectedModel());
            }else{
            HistorySyncedTable<VisuHistoryEvent, VisuHistoryEvent> historyEditor = new HistorySyncedTable(this.getObjectSelection().getSelectedModel().getHistoryMap(), VisuHistoryEvent.class, selectedObjectHandler.getSelectedModel().getName());
            }
        }
    }

    public void showComponentInfo() {
        selectedObjectHandler.getSelectedModel().addInfo("attribute", "value");

// TeleEditorTable teleEditor = new TeleEditorTable(this.getVisuHelper());
        if (this.visuHelper != null && selectedObjectHandler.getSelectedModel() != null) {
              if (selectedObjectHandler.getSelectedModel().getName().contains("ehb")) {
//          TeleEditorTable teleEditor = new TeleEditorTable(this.getVisuHelper(),selectedObjectHandler.getSelectedModel().getId());
  IMap<Integer, VisuComponentInfoEntry> ehbOrdersMap = this.visuHelper.getEHBOrdersMap();
          EHBTelegramSyncedTable<VisuComponentInfoEntry, VisuComponentInfoEntry> ordersEditor = new EHBTelegramSyncedTable(this.visuHelper.getEHBOrdersMap(), this.getVisuHelper(), VisuComponentInfoEntry.class, "EHB Order Editor");
        }  
            
              if (selectedObjectHandler.getSelectedModel().getName().contains("f")) {
            InfoSyncedTable<VisuComponentInfoEntry, VisuComponentInfoEntry> infoEditor = new InfoSyncedTable(this.visuHelper.getClient().getMap("stapler_lego_Telegrams"),this.getVisuHelper(),VisuComponentInfoEntry.class,selectedObjectHandler.getSelectedModel().getName());
//          TeleEditorTable teleEditor = new TeleEditorTable(this.getVisuHelper(),selectedObjectHandler.getSelectedModel().getId());

        }  
              else{
              InfoSyncedTable<VisuComponentInfoEntry, VisuComponentInfoEntry> infoEditor = new InfoSyncedTable(this.getObjectSelection().getSelectedModel().getVisuInfoMap(),this.getVisuHelper(),VisuComponentInfoEntry.class,selectedObjectHandler.getSelectedModel().getName());
//          TeleEditorTable teleEditor = new TeleEditorTable(this.getVisuHelper(),selectedObjectHandler.getSelectedModel().getId());
              }
        }
    
    }

    public void globalUndo() {
        visuHelper.performGlobalUndo();
    }

    public void undo(int numberUndos) {
        for (int i = 0; i < numberUndos; i++) {
            visuHelper.performGlobalUndo();
        }
    }

    public void globalRedo() {
        visuHelper.performGlobalRedo();
    }

    public void localUndo() {
        visuHelper.undo();
    }

    public void localRedo() {
        visuHelper.redo();
    }

    /**
     * @return the lastSelection
     */
    public ClickSelection<VisuComponent> getLastSelection() {
        return lastCursorSelection;


    }

    public void addFocusArrow() {
        if (hazelcastAppState.getClient() != null) {
            ClickSelection<VisuComponent> lastSelection = getLastSelection();
            if (lastSelection != null ) {
                    Long id = visuHelper.getIdGenerator().newId();

                    VisuComponent newMarking = visuHelper.getComponent("Marking " + id);
                    if (newMarking == null) {
                        newMarking = visuHelper.createComponent(id, "Marking " + id);
                        newMarking.setType(new VisuType(VisuType.MODEL, "CeMAT_Assets/Marking/marking.j3o"));
                        newMarking.setLabel("Focus Requested by "+this.getVisuHelper().getUsername());
                    }
                    Vector3f normal = lastSelection.getCollisionResult().getContactNormal();
                    Vector3f contactPoint = lastSelection.getCollisionResult().getContactPoint();
                    Vector3f contactObjectCoords = lastSelection.getCollisionResult().getGeometry().getWorldTranslation();
              
                    Vector3f v = lastSelection.getSpatial().worldToLocal(contactObjectCoords, new Vector3f());
                   
//                    newMarking.setParent(this.getObjectSelection().getSelectedModel().getId());
                    newMarking.setTranslation(new VisuVector3f(contactPoint.x, contactPoint.y + 0.1f, contactPoint.z));
               
                }
        }

    }

    public void showOrHideNote() {
        hazelcastAppState.threadPool.execute(new Runnable() {
            @Override
            public void run() {
                selectedObjectHandler.showOrHideNote();
            }
        });
    }
    /**
     * -------------------------------------------------------------------------
     */
    public void switchGlobal() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        Element toolbarUR = screen.findElementByName("UR-panel");
        toolbarUR.setFocusable(!toolbarUR.isFocusable());
        toolbarUR.setVisible(!toolbarUR.isVisible());
    }

    @Override
    public void cleanup() {
    }

    public void toogleClickToSelect(boolean toggle) {
//            openModificatorPanel();
        clickToSelect = toggle;
    }

    /**
     * update the map so that it points to the current object beeing selected by
     * the user
     *
     * @param selection
     */
    @Override
    public void updateSelectedComponentsMap(ClickSelection<VisuComponent> selection) {
        try{
       String username = visuHelper.getUsername();
        if(selection != null && !selection.getComponent().getName().contains("boden") ){
        long selectedCompId = selection.getComponent().getId();
        
        ArrayList<Long> selectedObjectsList = visuHelper.getSelectedComponents().get(username);
        //for one selected component, extend for more
        selectedObjectsList.add(0,selectedCompId);
        visuHelper.getSelectedComponents().put(username, selectedObjectsList);
       }
       else //if nothing is selected, clear the selection list
       {
        ArrayList<Long> selectedObjectsList = visuHelper.getSelectedComponents().get(username);
        selectedObjectsList.clear();
        visuHelper.getSelectedComponents().put(username, selectedObjectsList);
       }
        } catch(Exception ex){
            System.out.println(ex);
        }
    }

    @Override
    public void handleClick(final ClickSelection<VisuComponent> selection) {

//        System.out.println("Clicked on " + selection.getComponent().getName() + " with id " + selection.getComponent().id);

//        if (selection.getClickCount() == 1 && !selection.isRight() && clickToSelect) {
        if (selection.getClickCount() == 1 && !selection.isRight() ) {
//            clickToSelect = false;
  
            if (!selection.getComponent().getName().contains("boden")) {
            //update selection 
                    this.updateSelectedComponentsMap(selection);
//                   open the modification panel gui
                    openModificatorPanel();
            }
            else{ //if the users clicks the ground click the selection
//                  updateSelectedComponentsMap(null);
            } 
        }
        if (selection.getClickCount() == 1 && selection.isSpecial()) {            
            if (cameraControllerAppState.getCameraController().getDragOriginSelection() != null) {
                //Moritz Roidl, Orthodoxos Kipouridis Changing to the new edge interface
                VisuComponent source = (VisuComponent) cameraControllerAppState.getCameraController().getDragOriginSelection().getComponent();

                if (source != null) {
                    VisuComponent target = selection.getComponent();
                    if (target != null && source.getId() != target.getId()) {
                        VisuEdge edge = visuHelper.createEdge(source.getName() + "->" + target.getName());
                        edge.setSource(source.getId());
                        edge.setTarget(target.getId());
                    }
                }
            }
        }
        if (selection.getClickCount() == 1 && waitForClick_Move ) {
            if (numberClicks == 1) {

                Spatial clone = selectedObjectHandler.getSpatial().clone();
                ((Node)clone).detachChildNamed("BMPText");
                ((Node)clone).detachChildNamed("BoundingBox");
//                make the clone have the same rotation with the selected object
                clone.setLocalRotation(selectedObjectHandler.getSpatial().getWorldRotation());
                clone.setLocalScale(selectedObjectHandler.getSpatial().getLocalScale());
                clone.setMaterial(null);
                clone.setName("Ghost");

                Material mat_tt = new Material(visuClient.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                mat_tt.setTexture("ColorMap", visuClient.getAssetManager().loadTexture("Textures/metalbackground1.png"));
                mat_tt.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha); // activate transparency
                clone.setMaterial(mat_tt);
                clone.setQueueBucket(RenderQueue.Bucket.Transparent);
                visuClient.getRootNode().attachChild(clone);
                cameraControllerAppState.getCameraController().setCollisionMarker(clone, true);
                numberClicks = 2;
            } else if (numberClicks == 2) {
                VisuComponent model = selectedObjectHandler.getSelectedModel();
                    Vector3f v = selection.getCollisionResult().getContactPoint();
                    //Vector3f v = selected.getParent().worldToLocal(contactPoint, new Vector3f());
                    model.setTranslation(new VisuVector3f(v.x, v.y, v.z));
                    cameraControllerAppState.getCameraController().setCollisionMarker(null);
                    visuClient.getRootNode().detachChildNamed("Ghost");
                    moveTo();
                
                numberClicks = 0;
            }

        }
        //TODO: for drag and drop edge points
/*
         if (selection.getClickCount() == 2 && waitForClick_EdgePoint && !selection.isSpecial()) {
         Runnable r = new Runnable() {
         @Override
         public void run() {
         setEdgePoint(selection);
         cameraControllerAppState.getCameraController().setCollisionMarker(null);
         visuClient.getRootNode().detachChildNamed("Ghost_EdgePoint");
         waitForClick_EdgePoint = false;
         }
         };
         r.run();           
         }*/
        if (selection.getClickCount() == 2 ) {
            System.out.println("CURSOR POS: " + selection.getCollisionResult().getContactPoint());
            cameraControllerAppState.getCursorGeo().setLocalTranslation(selection.getCollisionResult().getContactPoint().clone());
            cameraControllerAppState.getCursorGeo().setCullHint(CullHint.Inherit);
            markerSelection = selection;
            lastCursorSelection = selection; //return to previous
        }
        //On double click change focus to conatct point
        if (selection.getClickCount()> 1 && selection.isSpecial()) {
            cameraControllerAppState.getCameraController().setCamFocus(selection.getCollisionResult().getContactPoint());
        }
    }

    public void setSelectedObject(JMESelectionHandler selection) {
        this.selectedObjectHandler = selection;
    }

    /**
     *
     * @return
     */
    @Override
    public JMESelectionHandler getObjectSelection() {
        return this.selectedObjectHandler;
    }

    /**
     * Get the visuComponent that the user has marked with the cursor when
     * double clicked
     *
     * @return cursorSelection
     */
    @Override
    public ClickSelection<VisuComponent> getMarkerSelection() {
        return markerSelection;


    }

    public void saveCameraView() {
        cameraControllerAppState.getCameraController().saveCameraView();
    }

    public void loadCameraView() {
        cameraControllerAppState.getCameraController().loadCameraView();
    }

    public void enableDisablePan() {
        cameraControllerAppState.getCameraController().enableDisablePan();

    }

    public void enableDisableCentering() {
        cameraControllerAppState.getCameraController().enableDisableCentering();
    }

    public void openRotationSlide() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        Element rotationSlider = screen.findElementByName("rotation-slider");
        rotationSlider.setFocusable(!rotationSlider.isFocusable());
        rotationSlider.setVisible(!rotationSlider.isVisible());
    }

    public void importGEXF() {
        new ImportGEXF(this.visuHelper);
    }

    public void exportGEXF() {
        new ExportGEXF(this.visuHelper);
        JOptionPane.showMessageDialog(null, "Successfully extracted file");
    }

    public void rotate() {
        modelEditActionHandler.rotate(numberClicks);
    }

    public void scale() {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        String scaleStr = screen.findNiftyControl("scale-field", TextField.class).getText();

        if (!scaleStr.equals("")) {
            float scaleNum = Float.parseFloat(scaleStr);
            modelEditActionHandler.scale(scaleNum);

        }
        screen.findNiftyControl("scale-field", TextField.class).setText("");
    }

    public void deleteComponent() {
        int n = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + selectedObjectHandler.getSelectedModel().getName() + "?",
                "Warning",
                JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {

            selectedObjectHandler.delete();
//           closeModificator();
        }
    }

    public void deleteAllComponents() {
        this.getVisuHelper().deleteAllComponents();
    }

    public void moveUp(float factor) {
        Vector3f direction = cameraControllerAppState.getCameraController().getCameraDirection();
        modelEditActionHandler.moveUp(direction, factor);
    }

    public void moveDown(float factor) {
        Vector3f direction = cameraControllerAppState.getCameraController().getCameraDirection();
        modelEditActionHandler.moveDown(direction, factor);
    }

    public void moveLeft(float factor) {

        Vector3f direction = cameraControllerAppState.getCameraController().getCameraLeft();

        modelEditActionHandler.moveLeft(direction, factor);
    }

    public void moveRight(float factor) {
        Vector3f direction = cameraControllerAppState.getCameraController().getCameraLeft();

        modelEditActionHandler.moveRight(direction, factor);
    }

    public void rotate(float value) {
        modelEditActionHandler.rotate(value);
    }

    /**
     * Select and import a binary model to the scenegraph.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void importModelComponent() throws FileNotFoundException, IOException {

        try {
            Vector3f markerPos = this.getMarkerSelection().getCollisionResult().getContactPoint();

            importModelUIHandler.addModelToSelectedComponent(new VisuVector3f(markerPos.x, markerPos.y, markerPos.z));
        } catch (Exception ex) {
            System.out.println("No marker (double click) was selected, new model will be placed in position 0,0,0  " + ex);
            importModelUIHandler.addModelToSelectedComponent(null);
        }
    }

    @Override
    public void displayClientMessage(ArrayList<String> info) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static class EditorPanelController extends AbstractController {

        private VisuEditor basicEditor;

        @Override
        public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
        }

        @Override
        public void onStartScreen() {
        }

        @Override
        public boolean inputEvent(NiftyInputEvent inputEvent) {
            return false;
        }

        public void test() {
            basicEditor.hazelcastAppState.threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.test();
                }
            });
        }

        public void createBox() {
            basicEditor.hazelcastAppState.threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    basicEditor.createEdge();
                }
            });
        }

        /**
         * @return the basicEditor
         */
        public VisuEditor getBasicEditor() {
            return basicEditor;
        }

        /**
         * @param basicEditor the basicEditor to set
         */
        public void setBasicEditor(VisuEditor basicEditor) {
            this.basicEditor = basicEditor;
        }
    }

    private class KeyListener implements ActionListener {

        public void onAction(String name, boolean value, float tpf) {
            if (!value) {
                return;
            }
            if (name.equals(LOCAL_UNDO_BUTTON)) {
                if (visuHelper != null) {
                    visuHelper.undo();
                }
            }
            if (name.equals(LOCAL_REDO_BUTTON)) {
                if (visuHelper != null) {
                    visuHelper.redo();
                }
            }
            if (name.equals(GLOBAL_BUTTON)) {
                switchGlobal();
            }
            if (name.equals(MODIFICATION_MENU_BUTTON)) {
                openModificatorPanel();
            }
            if (name.equals(ESC_BUTTON)) {
               //deselect and close the panel
                closeModificator();
            }
        }
    }
}
