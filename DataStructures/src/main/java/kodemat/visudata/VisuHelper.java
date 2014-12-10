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
package kodemat.visudata;

import kodemat.visudata.visuComponents.VisuComponent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.IdGenerator;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import kodemat.admin.VisuKodematUser;
import kodemat.tele.test.TelegramField;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;
import kodemat.versioning.visuHistory.VisuHistoryEvent;
import kodemat.visudata.change.VisuAddToGlobalUndoCreate;
import kodemat.visudata.change.VisuAddToGlobalUndoDelete;
import kodemat.visudata.change.VisuCreateComponent;
import kodemat.visudata.change.VisuCreateEdge;
import kodemat.visudata.change.VisuDeleteComponent;
import kodemat.visudata.change.VisuDeleteEdge;
import kodemat.visudata.change.VisuEdgeSourceChange;
import kodemat.visudata.change.VisuEdgeTargetChange;
import kodemat.visudata.change.VisuInterpolationChange;
import kodemat.visudata.change.VisuLabelChange;
import kodemat.visudata.change.VisuMarkingChange;
import kodemat.visudata.change.VisuParentChange;
import kodemat.visudata.change.VisuRedoChange;
import kodemat.visudata.change.VisuRotationChange;
import kodemat.visudata.change.VisuScaleChange;
import kodemat.visudata.change.VisuTranslationChange;
import kodemat.visudata.change.VisuTypeChange;
import kodemat.visudata.change.VisuUndoChange;
import kodemat.visudata.change.VisuUndoDelete;
import kodemat.visudata.command.VisuChangeCommand;
import kodemat.visudata.command.VisuCommand;
import kodemat.visudata.visuComponents.IComponent;
import kodemat.visudata.visuComponents.VisuComponentHelper;

/**
 *TODO: Make the component editing classes protected to prevent users from calling classes without specified all attributes, eg. Creating
 * a component without setting up the type
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuHelper {

    //changed to non-final because should be editable when importing a Gephi layout
     private boolean serverMode;
    final HazelcastInstance client;
    private final IdGenerator idGenerator;
    final IMap<String, Long> ids;
    final IMap<Long, String> names;
    final IMap<Long, VisuVector3f> translations;
    final IMap<Long, VisuRotation> rotations;
    final IMap<Long, VisuVector3f> scales;
    final IMap<Long, String> labels;
    final IMap<Long, Long> parents;
    final IMap<Long, VisuType> types;
    final IMap<Long, VisuInterpolation> interpolations;
    final IMap<Long, VisuMarking> markings;
    final IMap<Long, ArrayList<String>> notes;
    final IMap<Long, String> note;
    final IMap<Long, VisuUndoDeleteData> undoDeletes;
    final IMap<Long, Long> deleteSeq;
    final IMap<Long, VisuChatMessage> chat;
    final IMap<Long, Long> referencesToEdges;
    //Edge implementation
    final IMap<String, Long> edgeIds;
    final IMap<Long, String> edgeNames;
    final IMap<Long, Long> edgeSources;
    final IMap<Long, Long> edgeTargets;
    final IMap<Long, VisuColor> edgeColors;
    final IQueue<VisuCommand> commands;
    final IList<String> globalHistory;

    final IMap<Long, VisuComponentInfo> compoenentInfo;

    
    final Stack<VisuChangeCommand> undoStack;
    final Stack<VisuChangeCommand> redoStack;
    final Stack<VisuChangeCommand> globalUndoStack;
    final Stack<VisuChangeCommand> globalRedoStack;
    final IMap<String, VisuKodematUser> users;
    final IMap<String, ArrayList<Long>> selectedComponents;
    
// list for telegram editor
    final IMap<Integer,TelegramField> telegrams;
    
    
//    orders list for the EHB, should be moved out of the helper later, initialised by the jade_layout_client class
    private final IMap<Integer, VisuComponentInfoEntry> EHBOrdersMap;
    
    private boolean isClient;
    private VisuComponentHelper componentHelper;
    
   

    private  String clientUUID;
    private  String username;
    
    
    public VisuHelper(HazelcastInstance client, boolean serverMode, boolean isClient) {
        this.client = client;

        this.serverMode = serverMode;
        this.isClient = isClient;

//        init the telegrams list
        telegrams = client.getMap("telegrams");
        
       EHBOrdersMap = client.getMap("EHBOrdersMap");
        
   //        the following maps refer to the attributes of the visuComponents that are distributed to clients
        ids = client.getMap("kodemat.visu.ids");
        names = client.getMap("kodemat.visu.names");
        parents = client.getMap("kodemat.visu.parents");
        referencesToEdges = client.getMap("kodemat.visu.referencestoedges");
        edgeNames = client.getMap("kodemat.visu.edge.names");
        edgeIds = client.getMap("kodemat.visu.edge.ids");
        edgeSources = client.getMap("kodemat.visu.edge.sources");
        edgeTargets = client.getMap("kodemat.visu.edge.targets");
        commands = client.getQueue("kodemat.visu.commands");
        
        translations = client.getMap("kodemat.visu.translations");
        rotations = client.getMap("kodemat.visu.rotations");
        scales = client.getMap("kodemat.visu.scales");
        labels = client.getMap("kodemat.visu.labels");
        types = client.getMap("kodemat.visu.types");
        interpolations = client.getMap("kodemat.visu.interpolations");      
//        TODO: Remove marking attribute since not used
        markings = client.getMap("kodemat.visu.markings");  
        notes = client.getMap("kodemat.visu.notes");
         note = client.getMap("kodemat.visu.note");
        chat = client.getMap("kodemat.visu.chat");
         //non component-specific maps
        //Edge implementation
        edgeColors = client.getMap("kodemat.visu.edge.colors");
//        TODO: check and evt revise undo functionality
        undoDeletes = client.getMap("kodemat.visu.undoDeletes");
        deleteSeq = client.getMap("kodemat.visu.deleteSeq");
        
        compoenentInfo = client.getMap("kodemat.visu.componentInformation");
        
        globalHistory = client.getList("kodemat.visu.history");
        undoStack = new Stack<VisuChangeCommand>();
        redoStack = new Stack<VisuChangeCommand>();
        globalUndoStack = new Stack<VisuChangeCommand>();
        globalRedoStack = new Stack<VisuChangeCommand>();
      
//        the map that holds which components are currently selected from any user
        selectedComponents = client.getMap("kodemat.visu.selectedcomponents");
//        TODO: move it to the renderer, every user has a map of currently selectable, consider adding isSelected to IComponent
       
//        helper class for component specific methods
        componentHelper = new VisuComponentHelper(this);
        idGenerator = client.getIdGenerator("kodemat.visu.idgenerator");	
        users = client.getMap("users");
        
        //should be called after all the user related maps have been initialised
        this.initUserRelatedMaps();
       
        if (this.isServerMode())
            clientUUID = null;
        }

    public IMap<Integer, VisuComponentInfoEntry> getEHBOrdersMap() {
        return EHBOrdersMap;
    }

   

    public IMap<Long, VisuComponentInfo> getCompoenentInfo() {
        return compoenentInfo;
    }
    
    public VisuHelper(HazelcastInstance client) {
        this(client, false, true);
    }

     public VisuComponent createComponent(String name, Boolean... param){
         return componentHelper.createComponent(name, param);
     }
     public VisuComponent createComponent(Long id, String name, Boolean... param){
         return componentHelper.createComponent(id, name, param);
     }
    public void deleteComponent(String name, Boolean... param){
       componentHelper.deleteComponent(name, param);
    }
    
//    Caution the following should be used to delete multiple components, I dont know why Bibiana used it like this
    private VisuUndoDeleteData deleteComponent(String name, Long next){
       return componentHelper.deleteComponent(name, next);
    }

        public String getClientUUID(){
        return client.getLocalEndpoint().getUuid();
    }
        
        
        public  String getUsername(){
            String userName = this.getClientUsername(getClientUUID());
            if(userName != null)
            return  userName;
            else
                return this.getClientUUID();
        }
        
    /**
     * Initialise the maps that hold user related info and objects
     * should be called after all the user related maps have been initialised
     */
     private void initUserRelatedMaps(){
         //for each user set up a array list that will hold which objects have been selected from this user
     this.getSelectedComponents().put(this.getUserLastName(),new ArrayList<Long>());
     }
    
     public void deleteAllComponents(){
        ArrayList<String> toBeDeleted = new ArrayList<>();
         
         for (String compName : this.getNames().values()){
             toBeDeleted.add(compName);
         }
         this.deleteComponents(toBeDeleted);
     }
     
    /**
     *
     * @param comps: Ordered list of names of components to be erased. They will
     * be erased in this order.
     */
    public void deleteComponents(ArrayList<String> comps) {
        int num = 1;
        VisuUndoDeleteData next = null;
        for (String name : comps) {
            if (next == null) {
                Long id = -1L;
                next = deleteComponent(name, id);
            } else {
                next = deleteComponent(name, next.id);
            }
            num++;
        }
        VisuChangeCommand deleteCommand = new VisuChangeCommand(new VisuUndoDelete(next, next.id), this.getClientUUID());
        addToStack(deleteCommand, false);

    }

    public void deleteComponent(IComponent comp) {
        deleteComponent(comp.getName());
    }

    public VisuComponent getComponent(String name) {
      return  componentHelper.getComponent(name);
    }

    public VisuComponent getComponent(Long id) {
      return  componentHelper.getComponent(id);
    }

    public List<VisuComponent> getAllComponents() {
     return  componentHelper.getAllComponents();
    }

    //Edge helper methods
    public VisuEdge createEdge(String name, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        VisuEdge ve = null;
        if (edgeIds.get(name) != null) {
            deleteEdge(name);
        }

        try {
            Long newId = getIdGenerator().newId();
            edgeIds.put(name, newId);
            edgeNames.put(newId, name);
            ve = new VisuEdge(name, this, serverMode);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return ve;
    }

    public VisuEdge createEdge(Long id, String name, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        VisuEdge ve = null;
        if (edgeIds.get(name) != null) {
            deleteEdge(name);
        }

        try {
            edgeIds.put(name, id);
            edgeNames.put(id, name);
            ve = new VisuEdge(name, this, serverMode);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return ve;
    }

    private void deleteEdge(String name) {
        try {
            Long id = (Long) edgeIds.get(name);

            edgeNames.remove(id);
            edgeSources.remove(id);
            edgeTargets.remove(id);
            edgeIds.remove(name);

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public void deleteEdge(VisuEdge edge) {
        deleteEdge(edge.getName());
    }

    public VisuEdge getEdge(String name) {
        if (edgeIds.get(name) != null) {
            return new VisuEdge(name, this, serverMode);
        } else {
            return null;
        }
    }

    public VisuEdge getEdge(Long id) {
        if (edgeNames.get(id) != null) {
            return new VisuEdge(id, this, serverMode);
        } else {
            return null;
        }
    }

    public List<VisuEdge> getAllEdges() {
        List<VisuEdge> list = new ArrayList<VisuEdge>(edgeIds.size());
        List<Long> values = new ArrayList(edgeIds.values());
        for (Long id : values) {
            list.add(getEdge(id));
        }
        return list;
    }

    public void applyChange(VisuChange change, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;

        if (change instanceof VisuCreateComponent) {
            //components are directly created
        } else if (change instanceof VisuDeleteComponent) {
            this.deleteComponent(getComponent(((VisuDeleteComponent)change).getId()));
        } else if (change instanceof VisuUndoChange) {
            if (serverMode) {
                if(!isClient){
                    globalUndo(((VisuUndoChange) change).getId());
                }
            }
        } else if (change instanceof VisuRedoChange) {
            if (serverMode) {
                if(!isClient){
                    globalRedo();
                }
            }
        } else if (change instanceof VisuAddToGlobalUndoDelete) {
            if (serverMode) {
                if (!isClient) {
                    VisuAddToGlobalUndoDelete vatgud = (VisuAddToGlobalUndoDelete) change;
                    if (!vatgud.isUndo) {
                        globalUndoStack.push(new VisuChangeCommand(vatgud.vud,this.getClientUUID()));
                    } else {
                        globalRedoStack.push(new VisuChangeCommand(vatgud.vud,this.getClientUUID()));
                    }
                }
            } else if (change instanceof VisuAddToGlobalUndoCreate) {
                if (serverMode) {
                    if (!isClient) {
                        VisuAddToGlobalUndoCreate vatguc = (VisuAddToGlobalUndoCreate) change;
                        if (!vatguc.isUndo) {
                            globalUndoStack.push(new VisuChangeCommand(vatguc.vdc,this.getClientUUID()));
                        } else {
                            globalRedoStack.push(new VisuChangeCommand(vatguc.vdc,this.getClientUUID()));
                        }
                    }
                }
            }
        } else if (change instanceof VisuCreateEdge) {
            //edges  are directly created
        } else if (change instanceof VisuDeleteEdge) {
            this.deleteEdge(getEdge(((VisuDeleteEdge)change).getId()));
        } else if (change instanceof VisuEdgeSourceChange) {
            VisuEdge e = getEdge(((VisuEdgeSourceChange)change).getId());
            if (e != null) {
                e.applyChange(change, isUndo);
            }
        } else if (change instanceof VisuEdgeTargetChange) {
            VisuEdge e = getEdge(((VisuEdgeTargetChange)change).getId());
            if (e != null) {
                e.applyChange(change, isUndo);
            }
        } else if (change instanceof VisuAttributeChange) {
//          only component history will be kept 
//            String changestr = visuHistory.getString(this.getUserLastName(), change, isUndo);
//            if (changestr != null) {
//                globalHistory.add(changestr);
//            }
            VisuComponent c = getComponent(((VisuAttributeChange)change).getId());
            if (c != null) {
                c.applyChange(change, isUndo);
            }
        } else {
            //TODO: are there any other changes?? Yes, CompositeChanges
            throw new IllegalArgumentException("Received unknown change class");
        }
    }

    public List<String> geGlobaltHistory() {
        return globalHistory;
    }

    public void performGlobalUndo() {

        commands.offer(new VisuChangeCommand(new VisuUndoChange(0),this.getClientUUID()));
    }

    public void performGlobalUndo(int numTimes) {

        commands.offer(new VisuChangeCommand(new VisuUndoChange(numTimes),this.getClientUUID()));
    }

    public void performGlobalRedo() {
        commands.offer(new VisuChangeCommand(new VisuRedoChange(-1),this.getClientUUID()));
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public void undo() {
        try {
            VisuChange lastCommand = undoStack.pop().getChange();
            goBack(lastCommand, true);
        } catch (EmptyStackException e) {
        }
    }

    public void redo() {
        try {
            VisuChange lastCommand = redoStack.pop().getChange();
            goBack(lastCommand, false);
        } catch (EmptyStackException e) {
        }
    }

    private void globalUndo(long numTimes) {

        try {
            if (numTimes == 0) {
                VisuChange lastCommand = globalUndoStack.pop().getChange();
                goBack(lastCommand, true);
            } else {
                for (int i = 0; i < numTimes; i++) {
                    VisuChange lastCommand = globalUndoStack.pop().getChange();
                    goBack(lastCommand, true);
                    if (lastCommand instanceof VisuDeleteComponent || lastCommand instanceof VisuUndoDelete) {
                        lastCommand = globalUndoStack.pop().getChange();
                        goBack(lastCommand, true);
                        i--;
                    }
                }
            }
            for (int i = globalHistory.size() - 1; i > 0; i--) {
                if (!globalHistory.get(i).contains("UNDO")) {
                    globalHistory.remove(i);
                    break;
                }
            }
        } catch (EmptyStackException e) {
            System.out.println("Global undo stack is empty!");
        }
    }

    private void globalRedo() {

        try {
            VisuChange lastCommand = globalRedoStack.pop().getChange();
            goBack(lastCommand, false);
        } catch (EmptyStackException e) {
            System.out.println("Global redo stack is empty!");
        }
    }

    private void goBack(VisuChange command, boolean isUndo) {
        
        if (command instanceof VisuUndoDelete) {//We need to create the component again as it was before
            VisuComponent c = getComponent(((VisuUndoDelete)command).getId());
            if (c == null) {
                VisuUndoDelete vud = (VisuUndoDelete) command;
                undoDelete(vud.getValue(), isUndo, -1);
            }
        } else if (command instanceof VisuDeleteComponent) {
            VisuComponent c = getComponent(((VisuDeleteComponent)command).getId());
            ArrayList<String> toDelete = new ArrayList<String>();
            VisuDeleteComponent vdc = (VisuDeleteComponent) command;
            if (vdc.next != -1) {
                long toErase = vdc.getId();
                while (toErase != -1) {
                    toDelete.add(getComponent(toErase).getName());
                    toErase = (long) deleteSeq.get(toErase);
                }
                deleteComponents(toDelete);
            } else {
                applyChange(vdc, isUndo);
            }

        } else {
            applyChange(command, isUndo);
        }
    }

    private void undoDelete(VisuUndoDeleteData vudd, boolean isUndo, long previous) {
        long id = vudd.id;
        createComponent(id, vudd.name, true);

        if (serverMode) {
            if (!isClient) {
                if (vudd.translation != null) {
                    translations.put(id, vudd.translation);
                }
                if (vudd.rotation != null) {
                    rotations.put(id, vudd.rotation);
                }
                if (vudd.scale != null) {
                    scales.put(id, vudd.scale);
                }
                if (vudd.label != null) {
                    labels.put(id, vudd.label);
                }
                if (vudd.parent != -1) {
                    parents.put(id, vudd.parent);
                }
                if (vudd.type != null) {
                    types.put(id, vudd.type);
                }
                if (vudd.interpolation != null) {
                    interpolations.put(id, vudd.interpolation);
                }
                if (vudd.marking != null) {
                    markings.put(id, vudd.marking);
                }
            }
        } else {
            if (vudd.translation != null) {
                commands.offer(new VisuChangeCommand(new VisuTranslationChange(vudd.translation, id),this.getClientUUID()));
            }
            if (vudd.rotation != null) {
                commands.offer(new VisuChangeCommand(new VisuRotationChange(vudd.rotation, id),this.getClientUUID()));
            }
            if (vudd.scale != null) {
                commands.offer(new VisuChangeCommand(new VisuScaleChange(vudd.scale, id),this.getClientUUID()));
            }
            if (vudd.label != null) {
                commands.offer(new VisuChangeCommand(new VisuLabelChange(vudd.label, id),this.getClientUUID()));
            }
            if (vudd.parent != -1) {
                commands.offer(new VisuChangeCommand(new VisuParentChange(vudd.parent, id),this.getClientUUID()));
            }
            if (vudd.type != null) {
                commands.offer(new VisuChangeCommand(new VisuTypeChange(vudd.type, id),this.getClientUUID()));
            }
            if (vudd.interpolation != null) {
                commands.offer(new VisuChangeCommand(new VisuInterpolationChange(vudd.interpolation, id),this.getClientUUID()));
            }
            if (vudd.marking != null) {
                commands.offer(new VisuChangeCommand(new VisuMarkingChange(vudd.marking, id),this.getClientUUID()));
            }
        }

//This leave commented        Transaction tra = client.getTransaction();
//        transactionalContext.beginTransaction();
        try {
            deleteSeq.put(id, previous);
//            transactionalContext.commitTransaction();
        } catch (Throwable t) {
//            transactionalContext.rollbackTransaction();
        }
        if (undoDeletes.get(vudd.next) != null) {
            undoDelete((VisuUndoDeleteData) undoDeletes.get(vudd.next), isUndo, id);
        } else {
            VisuDeleteComponent vdc = new VisuDeleteComponent(id);
            vdc.next = previous;
            VisuChangeCommand deleteCommand = new VisuChangeCommand(vdc,this.getClientUUID());
            addToStack(deleteCommand, isUndo);
        }
    }

    public void addToStack(VisuChangeCommand lastCondition, boolean isUndo) {
        if (!serverMode && lastCondition.getChange() instanceof VisuUndoDelete) {
            VisuAddToGlobalUndoDelete vatgud = new VisuAddToGlobalUndoDelete(-1);
            vatgud.isUndo = isUndo;
            vatgud.vud = (VisuUndoDelete) lastCondition.getChange();
            commands.offer(new VisuChangeCommand(vatgud,this.getClientUUID()));
        } else if (!serverMode && lastCondition.getChange() instanceof VisuDeleteComponent) {
            VisuAddToGlobalUndoCreate vatguc = new VisuAddToGlobalUndoCreate(-1);
            vatguc.isUndo = isUndo;
            vatguc.vdc = (VisuDeleteComponent) lastCondition.getChange();
            commands.offer(new VisuChangeCommand(vatguc,this.getClientUUID()));
        }
        if (!isUndo) {
            if (serverMode) {
                if (!isClient) {
                    globalUndoStack.push(lastCondition);
                }
            } else {
                undoStack.push(lastCondition);
            }
        } else {
            if (serverMode) {
                if (!isClient) {
                    globalRedoStack.push(lastCondition);
                }
            } else {
                redoStack.push(lastCondition);
            }
        }
    }

    public boolean isServerMode() {
        return serverMode;
    }
    public void setServerMode(boolean isServer) {
       this.serverMode = isServer;
    }

    public HazelcastInstance getClient() {
        return client;
    }

    public IMap<String, Long> getIds() {
        return ids;
    }

    public IMap<Long, String> getNames() {
        return names;
    }

    public IMap<Long, VisuVector3f> getTranslations() {
        return translations;
    }

    public IMap<Long, VisuRotation> getRotations() {
        return rotations;
    }

    public IMap<Long, VisuVector3f> getScales() {
        return scales;
    }

    public IMap<Long, String> getLabels() {
        return labels;
    }

    public IMap<Long, Long> getParents() {
        return parents;
    }

    public IMap<Long, VisuType> getTypes() {
        return types;
    }

    public IMap<Long, VisuInterpolation> getInterpolations() {
        return interpolations;
    }

    public IMap<Long, VisuMarking> getMarkings() {
        return markings;
    }

    public IMap<Long, ArrayList<String>> getNotes() {
        return notes;
    }

    public IMap<Long, String> getNote() {
        return note;
    }

    public IMap<Long, VisuUndoDeleteData> getUndoDeletes() {
        return undoDeletes;
    }

    public IMap<Long, Long> getDeleteSeq() {
        return deleteSeq;
    }

    public IMap<Long, VisuChatMessage> getChat() {
        return chat;
    }

    public IMap<Long, Long> getReferencesToEdges() {
        return referencesToEdges;
    }

    public IMap<String, Long> getEdgeIds() {
        return edgeIds;
    }

    public IMap<Long, String> getEdgeNames() {
        return edgeNames;
    }

    public IMap<Long, Long> getEdgeSources() {
        return edgeSources;
    }

    public IMap<Long, Long> getEdgeTargets() {
        return edgeTargets;
    }

    public IMap<Long, VisuColor> getEdgeColors() {
        return edgeColors;
    }

    public IQueue<VisuCommand> getCommands() {
        return commands;
    }

    public IList<String> getGlobalHistory() {
        return globalHistory;
    }


    public Stack<VisuChangeCommand> getUndoStack() {
        return undoStack;
    }

    public Stack<VisuChangeCommand> getRedoStack() {
        return redoStack;
    }

    public Stack<VisuChangeCommand> getGlobalUndoStack() {
        return globalUndoStack;
    }

    public Stack<VisuChangeCommand> getGlobalRedoStack() {
        return globalRedoStack;
    }
    
    public IMap<Integer,TelegramField> getTelegrams(){
        return this.telegrams;
    }
    
    
      public String getUserLastName(){
        if(isClient == true){
String name = this.getClientUsername(this.getClientUUID());

            return getUsers().get(name).getLast_name();
        }
        else 
            return this.getClientUUID();
}
      
      
      public String getClientUsername(String uuid){
         String userName="";
     
            for(VisuKodematUser user: getUsers().values()){
                if(user.getUserUUID().equals(uuid)) {
                    userName = user.getUsername();
                return userName;
                }
         }
      return null;
}


    public IMap<String, ArrayList<Long>> getSelectedComponents() {
        return selectedComponents;
    }

    /**
     * Key is the user name
     * @return 
     */
    public IMap<String, VisuKodematUser> getUsers() {
        return users;
    }

    
    public boolean getServerMode(){
        return this.serverMode;
    }

    public void createTelegramHazelcastMap(String mapName){
        try{
    IMap<Integer, VisuComponentInfoEntry> newMap = this.getClient().getMap(mapName);
        }
        catch(Exception ex){
            System.err.printf("Problem when creating map "+mapName);
        }
    }
    
    public IMap<Integer, VisuComponentInfoEntry> getHazelcastMap(String mapName){
       try{
       return (this.getClient().getMap(mapName));
        }
        catch(Exception ex){
            System.err.printf("Problem when creating map "+mapName);
       return null;
        }
       
    }
    
    
}
