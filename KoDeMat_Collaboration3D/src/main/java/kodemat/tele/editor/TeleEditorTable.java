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
package kodemat.tele.editor;

import ca.odell.glazedlists.BasicEventList;
import kodemat.tele.test.TelegramField;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapEvent;
import java.awt.BorderLayout;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import kodemat.visudata.VisuHelper;

/**
 *
 * @author Orthodoxos Kipouridis. Moritz Roidl
 */
public class TeleEditorTable {
//    private final HazelcastInstance client;

    private final VisuHelper helper;
    EventList<TelegramField> telegramEventList;
    IMap<Integer, TelegramField> hazelcastList;
//    public TeleEditorTable() {
//        this.initialize();
//        
//    }
    private JTable swingTable;

    public TeleEditorTable(VisuHelper helper) {
        this.helper = helper;
        this.initialize();


    }


    public void initialize() {

//       get the hazelcast list
        hazelcastList = helper.getTelegrams();
//       create an Eventlist of Telegrams using the hazelcast list
        telegramEventList = GlazedLists.threadSafeList(new BasicEventList());

        this.sortedMap(hazelcastList);

        helper.getTelegrams().addEntryListener(new TelegramEntryListener(), true);
//       connector: Interface that helps attaching listeners to the elements
        ObservableElementList.Connector<TelegramField> telegramConnector = GlazedLists.beanConnector(TelegramField.class);

//        an event list of elements that are going to be observed
        EventList<TelegramField> observedTelegrams = new ObservableElementList<TelegramField>(telegramEventList, telegramConnector);

        
        FunctionList.AdvancedFunction<TelegramField, TelegramField> forward = new FunctionList.AdvancedFunction<TelegramField, TelegramField>() {

            @Override
            public TelegramField reevaluate(TelegramField a, TelegramField b) {
                 System.out.println("Fwd REEVALUATE " + (a == null ? "null" : a.getId()) );
                 
                return a;
            }

            @Override
            public void dispose(TelegramField a, TelegramField b) {
                
            }

            @Override
            public TelegramField evaluate(TelegramField a) {
                 System.out.println("Fwd EVALUATE " + (a == null ? "null" : a.getId()) );
                return a;
            }
        };
        FunctionList.AdvancedFunction<TelegramField, TelegramField> reverse = new FunctionList.AdvancedFunction<TelegramField, TelegramField>() {

            @Override
            public TelegramField reevaluate(TelegramField a, TelegramField b) {
                System.out.println("REVERSE REEVALUATE " + (a == null ? "null" : a.getId()) );
                hazelcastList.put(a.getId(), a);
                return b;
            }

            @Override
            public void dispose(TelegramField a, TelegramField b) {
                hazelcastList.remove(a.getId());
            }

            @Override
            public TelegramField evaluate(TelegramField a) {
                System.out.println("REVERSE EVALUATE " + (a == null ? "null" : a.getId()) );
                hazelcastList.put(a.getId(), a);
                return a;
            }
        };
        
        FunctionList<TelegramField, TelegramField> functionList = new FunctionList<>(observedTelegrams, forward, reverse);

        // build a JTable
        String[] propertyNames = {"id", "name", "value"};
        String[] columnLabels = {"IDs", "Name", "Value"};
        boolean[] editable = new boolean[3];
        editable[0] = false;
        editable[1] = true;
        editable[2] = true;

        TableFormat<TelegramField> tf = GlazedLists.tableFormat(TelegramField.class, propertyNames, columnLabels, editable);
         swingTable = new JTable(new EventTableModel<TelegramField>(functionList, tf));
        JPanel leftPanel = new JPanel();
        // place the table in a JFrame
        TelegramEditorFrame editorFrame = new TelegramEditorFrame();
        
       JSplitPane pane = editorFrame.getjSplitPane();
        
        leftPanel.add(new JScrollPane(swingTable), BorderLayout.CENTER);
        
        pane.setLeftComponent(leftPanel);
        
        JButton addFieldButton = new JButton();
        addFieldButton.setText("Add Field");
        addFieldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        
        JPanel rightPanel = new JPanel();
//        rightPanel.setLayout(Layou);
        rightPanel.add(addFieldButton);
      
        
        JButton removeFieldButton = new JButton();
        removeFieldButton.setText("Remove Field");
        removeFieldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        rightPanel.add(removeFieldButton);
         pane.setRightComponent(rightPanel);
        
        // show the frame
        editorFrame.pack();
        editorFrame.setLocationRelativeTo(null);
        editorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editorFrame.setVisible(true);



    }

    //TODO: get atomic number
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
        hazelcastList.put(hazelcastList.size(), new TelegramField(hazelcastList.size(), "New Field "));
       
    }
    
    
    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Integer row = swingTable.getSelectedRow();
        Integer id = (Integer)swingTable.getValueAt(row, 0);
        try {
            if(hazelcastList.get(id) != null)
              hazelcastList.remove(id);
            else
                System.err.println("There is not such an entry in telegram list" +id);
        } catch (Exception e) {
        }
      
 
    }

    
    public void increment() throws InterruptedException {
        hazelcastList.put(hazelcastList.size(), new TelegramField(hazelcastList.size(), "test 1"));
        hazelcastList.put(hazelcastList.size(), new TelegramField(hazelcastList.size(), "test 2"));
        hazelcastList.put(hazelcastList.size(), new TelegramField(hazelcastList.size(), "test 3"));

    }

    public class TelegramEntryListener implements EntryListener<Integer, TelegramField> {

        @Override
        public void entryAdded(EntryEvent<Integer, TelegramField> ee) {
            System.out.println("entryAdded " + (ee.getValue() == null ? "null" : ee.getValue().getId()));
            telegramEventList.add(ee.getValue());

        }

        @Override
        public void entryRemoved(EntryEvent<Integer, TelegramField> ee) {
            System.out.println("entryRemoved " + (ee.getValue() == null ? "null" : ee.getValue().getId()));
            telegramEventList.remove((int) ee.getKey());
        }

        @Override
        public void entryUpdated(EntryEvent<Integer, TelegramField> ee) {
            System.out.println("entryUpdated " + (ee.getValue() == null ? "null" : ee.getValue().getId()));
            telegramEventList.set(ee.getKey(), ee.getValue());

        }

        @Override
        public void entryEvicted(EntryEvent<Integer, TelegramField> ee) {
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

    public void sortedMap(IMap<Integer, TelegramField> map) {
        SortedSet<Integer> keys = new TreeSet<Integer>(map.keySet());
        for (Integer key : keys) {
            TelegramField value = map.get(key);
            telegramEventList.add(value);
        }
    }
    
    
}
