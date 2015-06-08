/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.touch.visu.editor;

import kodemat.touch.visu.editor.nifty.panels.UndoRedoEditor;
import kodemat.touch.visu.editor.nifty.panels.BasicEditorPanel;
import kodemat.touch.visu.editor.nifty.panels.NoteHistoryEditor;
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
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
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
//import jade_layout.EHBAgentenTopicFrame;
import java.io.File;
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
//import kodemat.PA.HandoverEventManager;
import kodemat.admin.VisuKodematUser;
import kodemat.tele.test.TelegramField;
import kodemat.versioning.visuHistory.EHBOrderField;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;
import kodemat.versioning.visuHistory.VisuHistoryEvent;
import kodemat.touch.visu.appstates.TouchCameraControllerAppState;
import kodemat.visu.appstates.NiftyGuiAppState;
import kodemat.visu.appstates.VisuHazelcastAppState;
import kodemat.visu.editor.EditorManager;
import kodemat.visu.editor.IEditor;
import kodemat.visu.editor.VisuEditor;
import kodemat.touch.visu.editor.nifty.panels.TouchImportModelPanel;
import kodemat.touch.visu.editor.userActions.TouchImportModelUIAction;
import kodemat.touch.visu.editor.nifty.panels.TouchModelEditWindowPanel;
import kodemat.touch.visu.editor.nifty.panels.ViewCube;
import kodemat.visu.input.mouse.ClickSelection;
import kodemat.visu.editor.selection.JMESelectionHandler;
import kodemat.visu.editor.userActions.EdgeUIActionsFacade;
import kodemat.visu.editor.userActions.ImportModelUIAction;
import kodemat.visu.editor.userActions.NotesUIActionsFacade;
import kodemat.visu.editor.userActions.SceneUIActionsFacade;
import kodemat.touch.visu.input.multitouch.MultiTouchAdapter;
import kodemat.visu.swing.tables.EHBTelegramSyncedTable;
import kodemat.visu.swing.tables.TeleEditorTable;
import kodemat.visu.swing.tables.HistorySyncedTable;
import kodemat.visu.swing.tables.InfoSyncedTable;
import kodemat.visu.swing.tables.UsersSyncedTable;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuEdge;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;
import kodemat.touch.visu.editor.nifty.panels.TouchDragPanelEditor;

/**
 * Class that contains handler methods for the UI
 *
 * @author moro
 */
public class TouchVisuEditor implements IEditor {

    SimpleApplication visuClient;
    //TODO: check if editorManager is needed
    EditorManager editorManager;
    TouchCameraControllerAppState cameraControllerAppState;
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
    public SceneUIActionsFacade modelEditActionHandler;
    private EdgeUIActionsFacade edgeUIHandler;
    private NotesUIActionsFacade notesUIHandler;
    private final HazelcastInstance hazelcastClient;
    private TouchImportModelUIAction importModelUIHandler;
    private ClickSelection<VisuComponent> lastCursorSelection = null;
    private boolean clickToSelect;
    private boolean enableObjTouchRotate = false;
    private boolean enableObjTouchMove = false;
    private boolean isModificationPanelOpen =false;
    private boolean isDragPanelOpen = false;
    
    
    @Override
    public VisuHazelcastAppState getHazelcastAppState() {
        return hazelcastAppState;
    }

    public TouchVisuEditor(HazelcastInstance hazelcastClient) {
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
        importModelUIHandler = new TouchImportModelUIAction(this);


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

        UndoRedoEditor ure = new UndoRedoEditor(visuClient);
        ure.layout(this);

        NoteHistoryEditor nhe = new NoteHistoryEditor(visuClient);
        nhe.layout(this);

        TouchModelEditWindowPanel tme = new TouchModelEditWindowPanel(visuClient);
        tme.layout(this);
        
        TouchImportModelPanel ime = new TouchImportModelPanel(visuClient);
        ime.layout(this);
        
        ViewCube vce = new ViewCube(visuClient);
        vce.layout(this);
        
        TouchDragPanelEditor dpe = new TouchDragPanelEditor(visuClient);
        dpe.layout(this);
        
    }

    public void loadSceneFromAsset() {
        Node scene = (Node) visuClient.getAssetManager().loadModel("Scenes/CeMatScene.j3o");
        importModelUIHandler.addComponentsFromBlenderScene(scene);
    }

    public SimpleApplication getVisuClient() {
        return visuClient;
    }

    public void getCompInfo() {
//       this.getVisuHelper().getComponentHistories().get(10).getVisuDataList()
    }

    public TouchCameraControllerAppState getCammeraControllAppState() {
        return visuClient.getStateManager().getState(TouchCameraControllerAppState.class);
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
//            IMap<Integer, VisuComponentInfoEntry> ehbOrdersMap = this.visuHelper.getEHBOrdersMap();
         // EHBTelegramSyncedTable<EHBOrderField, EHBOrderField> ordersEditor = new EHBTelegramSyncedTable(this.visuHelper.getEHBOrdersMap(), EHBOrderField.class, "EHB Order Editor");
//          EHBTelegramSyncedTable<VisuComponentInfoEntry, VisuComponentInfoEntry> ordersEditor = new EHBTelegramSyncedTable(this.visuHelper.getEHBOrdersMap(), this.getVisuHelper(), VisuComponentInfoEntry.class, "EHB Order Editor");

//          HandoverEventManager  manager = new HandoverEventManager(this.visuHelper);
//      manager.showManagerGUI();
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
            System.out.println("waitForClick_Move "+ waitForClick_Move);
           // text = "            ||Click anywhere to start moving the object.";
            numberClicks = 1;
            
            //!! without ghost numberClicks = 2;
        } else {
            System.out.println("waitForClick_Move "+ waitForClick_Move);
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

         /*
            Element e2 = nifty.getCurrentScreen().findElementByName("drag-panel");
            e2.setFocusable(true);
            e2.setVisible(true);
            screen.findElementByName("rotation-slider").setVisible(false);
            screen.findElementByName("rotation-slider").setFocusable(false);
*/
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
        
        int x= (int)inputManager.getCursorPosition().x;//selection.getCollisionResult().getContactPoint().x;
        int y= (int)inputManager.getCursorPosition().y;
        String stringx = (x-120) + ""; 
        String stringy = (-y+520) + "";
        win.setConstraintX(SizeValue.px(x-120));
        
        AppSettings settings = new AppSettings(true);
        System.out.println("settings.getHeight()" + settings.getHeight());
        System.out.println("point: " + ((-y*settings.getHeight()/480)+((settings.getHeight()/480)*520)));
        win.setConstraintY(SizeValue.px((-y*settings.getHeight()/480)+((settings.getHeight()/480)*520)));
        screen.layoutLayers();
        isModificationPanelOpen = true;
   
    }

    public void openModificatorPanel(ClickSelection<VisuComponent> selection) {
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");
        Element e = screen.findElementByName("selected ");

         /*
            Element e2 = nifty.getCurrentScreen().findElementByName("drag-panel");
            e2.setFocusable(true);
            e2.setVisible(true);
            screen.findElementByName("rotation-slider").setVisible(false);
            screen.findElementByName("rotation-slider").setFocusable(false);
*/
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
        
        int x1= (int)inputManager.getCursorPosition().x;//selection.getCollisionResult().getContactPoint().x;
        int y1= (int)inputManager.getCursorPosition().y;
        Vector3f contactPoint = selection.getCollisionResult().getContactPoint();
        Vector3f dir = getCammeraControllAppState().getCameraController().getCameraDirection();
        Vector3f cam = getCammeraControllAppState().getCameraControllerT().getCam();
        //contactPoint.subtractLocal(dir).divideLocal((float) (cam_z*0.01));
        //dir.addLocal(contactPoint);
        //contactPoint.addLocal(cam);
        Vector3f normal = selection.getCollisionResult().getContactNormal();
                    
        Vector3f contactObjectCoords = selection.getCollisionResult().getGeometry().getWorldTranslation();  
                    Vector3f contactObjectCoordsloca = selection.getCollisionResult().getGeometry().getLocalTranslation();
                    Vector3f v = selection.getSpatial().worldToLocal(contactObjectCoords, new Vector3f());
                    System.out.println("contactObjectCoords " + contactObjectCoords);
                    System.out.println("contactObjectCoordsloca " + contactObjectCoordsloca);
        System.out.println("dir " + dir);
       System.out.println("cam " + cam);
       System.out.println("normal " + normal);
         
        Vector3f isit=contactObjectCoords.subtractLocal(cam);
                    dir.dot(contactObjectCoords.subtract(cam));
                    System.out.println("contactObjectCoords " + isit);
       // Vector3f v = selection.getCollisionResult().getContactPoint();
        int x =(int) isit.x ;
        int y = (int) isit.z;
        System.out.println("open position x: " + x + " open position y: " + y);

        
        //String stringx = (x-120) + ""; 
        //String stringy = (-y+520) + "";
        String stringx = (x) + ""; 
        String stringy = (y) + "";
        //win.setConstraintX(SizeValue.px(x-120));
        win.setConstraintX(SizeValue.px(x));
        win.setConstraintY(SizeValue.px(y));
        
        AppSettings settings = new AppSettings(true);
        System.out.println("settings.getHeight()" + settings.getHeight());
        System.out.println("point: " + ((-y*settings.getHeight()/480)+((settings.getHeight()/480)*520)));
        //win.setConstraintY(SizeValue.px((-y*settings.getHeight()/480)+((settings.getHeight()/480)*520)));
        screen.layoutLayers();

   
    }


    
    public void closeModificator() {
        isModificationPanelOpen = false;
        getHazelcastAppState().threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //!!!!
                    if(isEnableObjTouchMove()==true){
                        setNumberClicks(0);
                        setWaitForClick_Move(false);
                        setEnableObjTouchMove(false);
                    }
                        
                    else if(isEnableObjTouchRotate()==true)
                        setEnableObjTouchRotate(false);
                    
        Nifty nifty = guiAppState.getNifty();
        Screen screen = nifty.getScreen("hidden");

     
        Element e2 = nifty.getCurrentScreen().findElementByName("editModelWindow");
        e2.setFocusable(false);
        e2.setVisible(false);
        
        DragPanelClose();

        updateSelectedComponentsMap(null);
                   
                }
            });
        
      
    }

    public void showHistory() {
        if (this.visuHelper != null && selectedObjectHandler.getSelectedModel() != null) {
            if(selectedObjectHandler.getSelectedModel().getName().contains("stapler")){
           HistorySyncedTable<TelegramField, TelegramField> historyEditor = new HistorySyncedTable(this.getVisuHelper().getTelegrams(), TelegramField.class, selectedObjectHandler.getSelectedModel().getName());

            }
            if(selectedObjectHandler.getSelectedModel().getName().contains("ehb")){
                ITopic topic = hazelcastClient.getTopic("katze1Topic");
//       JFrame agentenTopicFrame =   new EHBAgentenTopicFrame(topic);
//       agentenTopicFrame.setVisible(true);
//       agentenTopicFrame.setTitle("Messages from "+selectedObjectHandler.getSelectedModel());
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
//  IMap<Integer, VisuComponentInfoEntry> ehbOrdersMap = this.visuHelper.getEHBOrdersMap();
//          EHBTelegramSyncedTable<VisuComponentInfoEntry, VisuComponentInfoEntry> ordersEditor = new EHBTelegramSyncedTable(this.visuHelper.getEHBOrdersMap(), this.getVisuHelper(), VisuComponentInfoEntry.class, "EHB Order Editor");
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

        System.out.println("Clicked on " + selection.getComponent().getName() + " with id " + selection.getComponent().id);

//        if (selection.getClickCount() == 1 && !selection.isRight() && clickToSelect) {
        if (selection.getClickCount() == 1 && !selection.isRight() ) {
//            clickToSelect = false;
  
            if (!selection.getComponent().getName().contains("boden")) {
            //update selection 
                    if(isEnableObjTouchMove()==true){
                        numberClicks = 0;
                        waitForClick_Move = false;
                        setEnableObjTouchMove(false);
                    }
                    else if(isEnableObjTouchRotate()==true)
                        setEnableObjTouchRotate(false);
                    
                    else if(isModificationPanelOpen){
                        closeModificator();
                    }
                    this.updateSelectedComponentsMap(selection);
//                   open the modification panel gui
                    //openModificatorPanel(selection);
                    openModificatorPanel();
            }
            else{ //if the users clicks the ground click the selection
//                  updateSelectedComponentsMap(null);
                if(isModificationPanelOpen && isEnableObjTouchMove()==false && isEnableObjTouchRotate()==false){
                        closeModificator();
                    }
            } 
        }
        if (selection.getClickCount() == 1 && selection.isSpecial()) {            
            if (cameraControllerAppState.getCameraControllerT().getDragOriginSelection() != null) {
                //moro Changing to the new edge interface
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
        System.out.println("setwaitforclick = "+waitForClick_Move);
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
                    System.out.println("model position x: " + v.x + " model position y: " + v.y + " model position z: " + v.z);
                    cameraControllerAppState.getCameraControllerT().setCollisionMarker(null);
                    visuClient.getRootNode().detachChildNamed("Ghost");
                    
                    moveTo();
                  
                    setEnableObjTouchMove(false);
                    //!
                    //openModificatorPanel(selection);
                    openModificatorPanel();
                
                    //numberClicks = 0;
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
            cameraControllerAppState.getCameraControllerT().setCamFocus(selection.getCollisionResult().getContactPoint());
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

         public void importModelComponentDirect(File fileName) throws FileNotFoundException, IOException {
          //   importModelUIHandler.addModelToSelectedComponentDirect(fileName, cameraControllerAppState, new VisuVector3f(inputManager.getCursorPosition().x, inputManager.getCursorPosition().y, 0));
        try {
            Vector3f markerPos = this.getMarkerSelection().getCollisionResult().getContactPoint();
            importModelUIHandler.addModelToSelectedComponentDirect(fileName, cameraControllerAppState, new VisuVector3f(markerPos.x, markerPos.y, markerPos.z));
        } catch (Exception ex) {
            System.out.println("No marker (double click) was selected, new model will be placed in position 0,0,0  " + ex);
            importModelUIHandler.addModelToSelectedComponentDirect(fileName, cameraControllerAppState, null);
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
//   //         basicEditor.hazelcastAppState.threadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    basicEditor.test();
//                }
//            });
        }

        public void createBox() {
            basicEditor.getHazelcastAppState().threadPool.execute(new Runnable() {
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
    
    
    
    public void setWaitForClick_Move(boolean waitForClick_Move){
        System.out.println("setwaitforclick = "+waitForClick_Move);
        this.waitForClick_Move=waitForClick_Move;
        
    }
    
    
    public boolean isEnableObjTouchRotate() {
        return enableObjTouchRotate;
    }

    public void setEnableObjTouchRotate(boolean enableObjTouchRotate) {
        this.enableObjTouchRotate = enableObjTouchRotate;
    }

    public boolean isEnableObjTouchMove() {
        return enableObjTouchMove;
    }

    public void setEnableObjTouchMove(boolean enableObjTouchMove) {
        this.enableObjTouchMove = enableObjTouchMove;
    }

        /**
     * @return the isDragPanelOpen
     */
    public boolean isDragPanelOpen() {
        return isDragPanelOpen;
    }

    /**
     * @param isDragPanelOpen the isDragPanelOpen to set
     */
    public void setIsDragPanelOpen(boolean isDragPanelOpen) {
        this.isDragPanelOpen = isDragPanelOpen;
    }
    
    public void DragPanelClose(){
        if(isDragPanelOpen == true){
            Nifty nifty = guiAppState.getNifty();
            Element e3 = nifty.getCurrentScreen().findElementByName("drag-modification-panel");
        
            setIsDragPanelOpen(false);
            e3.setVisible(false);
        }
            
    }
    public int getNumberClicks() {
        return numberClicks;
    }

    public void setNumberClicks(int numberClicks) {
        this.numberClicks = numberClicks;
    }

    public InputManager getInputManager(){
        return inputManager;
    }
}
