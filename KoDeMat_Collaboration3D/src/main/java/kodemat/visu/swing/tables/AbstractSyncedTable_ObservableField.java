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
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JTable;
import kodemat.admin.VisuKodematUser;


/**
 * Abstract class that implements swing Tables that are sync the Hazelcast lists
 * with the swing text fields using Glazedlists The generic parameters are T:
 * the
 *
 * @author Orthodoxos Kipouridis. Moritz Roidl
 */
public abstract class AbstractSyncedTable_ObservableField<T, K> {

    /**
     * The event glazed list that will be used to update the hazelcast list when
     * the user edits a field
     */
    EventList<VisuKodematUser> glazedList;
    /**
     * The hazelcast IMap that synchronises with the fields. Every read/write
     * should be done here
     */
    IMap hazelcastList;
    private FunctionList functionList;
    EventList observedTelegrams;
    ObservableElementList.Connector<VisuKodematUser> telegramConnector;
    public TableFormat tf;
    public JTable swingTable;
    /**
     * The class type of the hazelcast values
     */
    public T tableValueType;
    public K hazelcastKeyType;
    public final String compName;

    public AbstractSyncedTable_ObservableField(IMap hazelcastMap, T ValuesType, String componentName) {
//       get the hazelcast list
        hazelcastList = hazelcastMap;
        tableValueType = ValuesType;
   compName = componentName;
        //caution, always after variables assignment
        initialize();
    }

    public void initialize() {

//        sort the list according to keys
        glazedList = sortedMap(hazelcastList);

//       add an entry listener to listen to updatea of the hazelcast map
        hazelcastList.addEntryListener(new ObjectEntryListener(), true);

//       connector: Interface that helps attaching listeners to the elements
        telegramConnector = GlazedLists.beanConnector(VisuKodematUser.class);

//        an event list of elements that are going to be observed
        observedTelegrams = new ObservableElementList<>(glazedList, telegramConnector);

        this.setupGlazedList();


        setUpSwingTable(tableValueType);
    }

    public void setupGlazedList() {
        FunctionList.AdvancedFunction<VisuKodematUser, VisuKodematUser> forward = new FunctionList.AdvancedFunction<VisuKodematUser, VisuKodematUser>() {
            @Override
            public VisuKodematUser reevaluate(VisuKodematUser a, VisuKodematUser b) {

                return a;
            }

            @Override
            public void dispose(VisuKodematUser a, VisuKodematUser b) {
            }

            @Override
            public VisuKodematUser evaluate(VisuKodematUser a) {

                return a;
            }
        };
        FunctionList.AdvancedFunction<VisuKodematUser, VisuKodematUser> reverse = new FunctionList.AdvancedFunction<VisuKodematUser, VisuKodematUser>() {
            @Override
            public VisuKodematUser reevaluate(VisuKodematUser a, VisuKodematUser b) {
                   System.out.println("REVERSE REEVALUATE " + (a == null ? "null" : a.getId()) );
                hazelcastList.put(a.getId(), a);
                return b;
            }

            @Override
            public void dispose(VisuKodematUser a, VisuKodematUser b) {
                hazelcastList.remove(a.getId());
            }

            @Override
            public VisuKodematUser evaluate(VisuKodematUser a) {
               
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

    public void addEntryToTable(int index, VisuKodematUser tele) {
        hazelcastList.put(index, tele);
    }

    public class ObservableListener implements ItemListener<VisuKodematUser> {

        @Override
        public void itemAdded(ItemEvent<VisuKodematUser> ie) {
            System.out.println("Entry added key=" + ie.getItem().getId() + ", value=" + ie.getItem());
            glazedList.add(ie.getItem());

        }

        @Override
        public void itemRemoved(ItemEvent<VisuKodematUser> ie) {
            System.out.println("Entry remove key=" + ie.getItem().getId() + ", value=" + ie.getItem());
            for (VisuKodematUser obsObj : glazedList) {
                if (obsObj.getId() == ie.getItem().getId()) {
                    glazedList.remove(obsObj);
                }
            }
        }
    }

    //The first parameter should be generalized
    public class ObjectEntryListener implements EntryListener<Integer, VisuKodematUser> {

        @Override
        public void entryAdded(EntryEvent<Integer, VisuKodematUser> ee) {
            glazedList.add(ee.getValue());

        }

        @Override
        public void entryRemoved(EntryEvent<Integer, VisuKodematUser> ee) {
            System.out.println("entryRemoved " + (ee.getValue() == null ? "null" : ee.getValue().getId()));
            glazedList.remove((int) ee.getKey());
        }

        @Override
        public void entryUpdated(EntryEvent<Integer, VisuKodematUser> ee) {
            //Check if glazedlists get only Integers as keys
             System.out.println("updated " + (ee.getValue() == null ? "null" : ee.getValue().getId()));
            glazedList.set((Integer) ee.getKey(), ee.getValue());

        }

        @Override
        public void entryEvicted(EntryEvent<Integer, VisuKodematUser> ee) {
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

    public EventList<VisuKodematUser> getGlazedList() {
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

    public ObservableElementList.Connector<VisuKodematUser> getTelegramConnector() {
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
    public EventList<VisuKodematUser> sortedMap(IMap map) {
        //       create an Eventlist of Telegrams using the hazelcast list
        EventList<VisuKodematUser> eventList = GlazedLists.threadSafeList(new BasicEventList());

        SortedSet keys = new TreeSet(map.keySet());
        for (Object key : keys) {
            VisuKodematUser value = (VisuKodematUser) map.get(key);
            eventList.add(value);
        }

        return eventList;
    }
}
