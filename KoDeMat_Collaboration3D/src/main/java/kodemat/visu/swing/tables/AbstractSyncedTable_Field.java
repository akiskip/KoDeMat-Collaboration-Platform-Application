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
package kodemat.visu.swing.tables;

import ca.odell.glazedlists.BasicEventList;
import kodemat.tele.test.TelegramField;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.gui.TableFormat;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import com.hazelcast.core.MapEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JTable;
import kodemat.tele.teledata.AbstractDistributableField;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;
import kodemat.visudata.VisuHelper;

/**
 * Abstract class that implements swing Tables that are sync the Hazelcast lists
 * with the swing text fields using Glazedlists The generic parameters are T:
 * the
 *
 * @author Orthodoxos Kipouridis. Moritz Roidl
 */
public abstract class AbstractSyncedTable_Field<T, K> {

    /**
     * The event glazed list that will be used to update the hazelcast list when
     * the user edits a field
     */
    EventList<AbstractDistributableField> glazedList;
    /**
     * The hazelcast IMap that synchronises with the fields. Every read/write
     * should be done here
     */
    IMap hazelcastList;
    private FunctionList functionList;
    EventList observedTelegrams;
    ObservableElementList.Connector<AbstractDistributableField> telegramConnector;
    public TableFormat tf;
    public JTable swingTable;
    /**
     * The class type of the hazelcast values
     */
    public T tableValueType;
    public K hazelcastKeyType;
    public final String compName;
    private  VisuHelper helper = null;

    public VisuHelper getHelper() {
        return helper;
    }

    public AbstractSyncedTable_Field(IMap hazelcastMap, T ValuesType, String componentName) {
//       get the hazelcast list
        hazelcastList = hazelcastMap;
        tableValueType = ValuesType;
   compName = componentName;
        //caution, always after variables assignment
        initialize();
    }
    public AbstractSyncedTable_Field(IMap hazelcastMap, VisuHelper helper, T ValuesType, String componentName) {
//       get the hazelcast list
        hazelcastList = hazelcastMap;
        tableValueType = ValuesType;
   compName = componentName;
   this.helper = helper;
        //caution, always after variables assignment
        initialize();
    }

    public void initialize() {

//        sort the list according to keys
        glazedList = sortedMap(hazelcastList);

//       add an entry listener to listen to updatea of the hazelcast map
        hazelcastList.addEntryListener(new ObjectEntryListener(), true);

//       connector: Interface that helps attaching listeners to the elements
        telegramConnector = GlazedLists.beanConnector(AbstractDistributableField.class);

//        an event list of elements that are going to be observed
        observedTelegrams = new ObservableElementList<>(glazedList, telegramConnector);

        this.setupGlazedList();


        setUpSwingTable(tableValueType);
    }

    public void setupGlazedList() {
        FunctionList.AdvancedFunction<AbstractDistributableField, AbstractDistributableField> forward = new FunctionList.AdvancedFunction<AbstractDistributableField, AbstractDistributableField>() {
            @Override
            public AbstractDistributableField reevaluate(AbstractDistributableField a, AbstractDistributableField b) {

                return a;
            }

            @Override
            public void dispose(AbstractDistributableField a, AbstractDistributableField b) {
            }

            @Override
            public AbstractDistributableField evaluate(AbstractDistributableField a) {

                return a;
            }
        };
        FunctionList.AdvancedFunction<AbstractDistributableField, AbstractDistributableField> reverse = new FunctionList.AdvancedFunction<AbstractDistributableField, AbstractDistributableField>() {
            @Override
            public AbstractDistributableField reevaluate(AbstractDistributableField a, AbstractDistributableField b) {
                   System.out.println("REVERSE REEVALUATE " + (a == null ? "null" : a.getId()) );
                hazelcastList.put(a.getId(), a);
                return b;
            }

            @Override
            public void dispose(AbstractDistributableField a, AbstractDistributableField b) {
                hazelcastList.remove(a.getId());
            }

            @Override
            public AbstractDistributableField evaluate(AbstractDistributableField a) {
               
                  System.out.println("REVERSE EVALUATE " + (a == null ? "null" : a.getId()) );
                hazelcastList.put(a.getId(), a);
                return a;
            }
        };

        functionList = new FunctionList<>(observedTelegrams, forward, reverse);

    }

    /**
     * Method for defining the properties for the swing table that will be
     * created
     *
     * @param contentClassType the type of the class that will be used as input
     * to the table. It matches the type of the valus htat the Hazelcast IMaps
     * contain
     */
    public abstract void setUpSwingTable(Object contentClassType);

    public void addEntryToTable(int index, AbstractDistributableField tele) {
        hazelcastList.put(index, tele);
    }

    public class ObservableListener implements ItemListener<AbstractDistributableField> {

        @Override
        public void itemAdded(ItemEvent<AbstractDistributableField> ie) {
            System.out.println("Entry added key=" + ie.getItem().getId() + ", value=" + ie.getItem());
            glazedList.add(ie.getItem());

        }

        @Override
        public void itemRemoved(ItemEvent<AbstractDistributableField> ie) {
            System.out.println("Entry remove key=" + ie.getItem().getId() + ", value=" + ie.getItem());
            for (AbstractDistributableField obsObj : glazedList) {
                if (obsObj.getId() == ie.getItem().getId()) {
                    glazedList.remove(obsObj);
                }
            }
        }
    }

    //The first parameter should be generalized
    public class ObjectEntryListener implements EntryListener<Integer, AbstractDistributableField> {

        @Override
        public void entryAdded(EntryEvent<Integer, AbstractDistributableField> ee) {
            glazedList.add(ee.getValue());

        }

        @Override
        public void entryRemoved(EntryEvent<Integer, AbstractDistributableField> ee) {
            System.out.println("entryRemoved " + (ee.getValue() == null ? "null" : ee.getValue().getId()));
            glazedList.remove((int) ee.getKey());
        }

        @Override
        public void entryUpdated(EntryEvent<Integer, AbstractDistributableField> ee) {
            //Check if glazedlists get only Integers as keys
             System.out.println("updated " + (ee.getValue() == null ? "null" : ee.getValue().getId()));
            glazedList.set((Integer) ee.getKey(), ee.getValue());

        }

        @Override
        public void entryEvicted(EntryEvent<Integer, AbstractDistributableField> ee) {
        }

        @Override
        public void mapEvicted(MapEvent me) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void mapCleared(MapEvent me) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    public EventList<AbstractDistributableField> getGlazedList() {
        return glazedList;
    }

    public IMap getHazelcastList() {
        return hazelcastList;
    }

    public FunctionList getFunctionList() {
        return functionList;
    }

    public EventList getObservedTelegrams() {
        return observedTelegrams;
    }

    public ObservableElementList.Connector<AbstractDistributableField> getTelegramConnector() {
        return telegramConnector;
    }

    public TableFormat getTf() {
        return tf;
    }

    public JTable getSwingTable() {
        return swingTable;
    }

    public T getTableValueType() {
        return tableValueType;
    }

    public K getHazelcastKeyType() {
        return hazelcastKeyType;
    }

    /**
     * Return a list of a shorted map based on the keys, used so that the
     * following swing table can be set up (TODO: better docu)
     *
     * @param map
     * @return
     */
    public EventList<AbstractDistributableField> sortedMap(IMap map) {
        //       create an Eventlist of Telegrams using the hazelcast list
        EventList<AbstractDistributableField> eventList = GlazedLists.threadSafeList(new BasicEventList());

        SortedSet keys = new TreeSet(map.keySet());
        for (Object key : keys) {
            AbstractDistributableField value = (AbstractDistributableField) map.get(key);
            eventList.add(value);
        }

        return eventList;
    }
    
    /**
     * Search the map for entries that indicate that the user has selected a row of the table, and clear the entry.
     * Should be maybe revised to allow multiple selection from a single user
     */
    public void clearSelectionField(){
          Iterator it = hazelcastList.entrySet().iterator();
          String username = helper.getUsername();
    while (it.hasNext()) {
        Map.Entry pairs = (Map.Entry)it.next();
        AbstractDistributableField entry = (AbstractDistributableField)pairs.getValue();
      if(entry.getUserSelected().contains(username))
      {
           entry.setUserSelected("");
            getHazelcastList().put(pairs.getKey(), entry);
      }
        it.remove(); // avoids a ConcurrentModificationException
    }
       
        
    }
}
