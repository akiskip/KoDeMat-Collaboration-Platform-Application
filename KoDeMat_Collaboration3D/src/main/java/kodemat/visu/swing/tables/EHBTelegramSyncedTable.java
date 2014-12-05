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

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.EventTableModel;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapEvent;
import jade_layout.EHBAgentXMLWriter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Collections;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import kodemat.tele.teledata.AbstractDistributableField;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;
import kodemat.visudata.VisuHelper;

/**
 *
 * @author Orthodoxos Kipouridis. Moritz Roidl
 */
public class EHBTelegramSyncedTable<T,K> extends AbstractSyncedTable_Field {
    private VisuSyncedTableModel<AbstractDistributableField> tModel;
    private JTextArea xmlTextArea;
    private EHBAgentXMLWriter xmlWriter;

    public EHBTelegramSyncedTable(IMap hazelcastMap, VisuHelper helper, T ValuesType, String componentName )
    {
        super(hazelcastMap,helper,ValuesType,componentName);
        getHazelcastList().addEntryListener(new XMLFieldListener(), true);
        xmlWriter = new EHBAgentXMLWriter();
       
    }

    @Override
    public void setUpSwingTable(Object contentClassType) {
             //TODO: build a JTable based on the hazelcast map
        String[] propertyNames = {"id", "name", "value","userSelected"};
        String[] columnLabels = {"IDs", "Name", "Value","Edited By"};
        boolean[] editable = new boolean[4];
        editable[0] = false;
        editable[1] = true;
        editable[2] = true;
        editable[3] = false;

        //TODO: should get the class type instead of the the first entrys type
        tf = GlazedLists.tableFormat((Class<?>)contentClassType, propertyNames, columnLabels, editable);
        tModel= new VisuSyncedTableModel<AbstractDistributableField>(getFunctionList(), tf);
       
       
      //        set model and renderer to the table   
        swingTable = new JTable(tModel);
        
        swingTable.setDefaultRenderer(Object.class, new TableCellRenderer ()
{
    private DefaultTableCellRenderer DEFAULT_RENDERER =  new DefaultTableCellRenderer();
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
  
        
        VisuSyncedTableModel model = (VisuSyncedTableModel) table.getModel();
       String userSelected =  model.getValueAt(row, 3).toString();
                Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!table.isRowSelected(row)) {
                    if (getHelper().getUsers().get(userSelected) != null && !userSelected.equalsIgnoreCase("")) {

                        final Color rowColor = getHelper().getUsers().get(userSelected).getColor();
                        // loop the columns of the selected row and set the background.
                        for (int i = 0; i < model.getColumnCount(); i++) {
                            Component cell = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, i);
                            cell.setBackground(rowColor);
                        }
                    } else {
                        c.setBackground(table.getBackground());
                    }
                }
                return c;
            }
        });
    
        JPanel leftPanel = new JPanel();
         JPanel rightPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS)); 
        // place the table in a JFrame
        TelegramEditorFrame editorFrame = new TelegramEditorFrame();
        
       editorFrame.setTitle("System properties for "+compName);
        
        JSplitPane tablePane = editorFrame.getjSplitPane();
        JScrollPane tableScrollPane = new JScrollPane(swingTable);
        
         xmlTextArea = new JTextArea();
        xmlTextArea.setLayout(new BorderLayout());
        JScrollPane XMLScrollPane = new JScrollPane(xmlTextArea);
        xmlTextArea.setLineWrap(true);
       
//        create a scrollpane for the left component 
      JSplitPane  leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
 tableScrollPane, XMLScrollPane);
leftSplitPane.setOneTouchExpandable(true);
leftSplitPane.setDividerLocation(100);
        
        
        leftPanel.add(leftSplitPane);

        tablePane.setLeftComponent(leftPanel);

        
        JButton addFieldButton = new JButton();
        addFieldButton.setText("Add Field");
        addFieldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
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
        
        JButton submitFieldButton = new JButton();
        submitFieldButton.setText("Submit Order");
        submitFieldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });
        rightPanel.add(submitFieldButton);
        
        JButton loadXMLFieldButton = new JButton();
        loadXMLFieldButton.setText("Import XML");
        loadXMLFieldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadXMLFieldButton(evt);
            }

            private void loadXMLFieldButton(ActionEvent evt) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        rightPanel.add(loadXMLFieldButton);
        
        JButton saveXMLButton = new JButton();
        saveXMLButton.setText("Export XML");
        saveXMLButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveXMLButton(evt);
            }

            private void saveXMLButton(ActionEvent evt) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        rightPanel.add(saveXMLButton);
        
    
        tablePane.setRightComponent(rightPanel);
        
      swingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        swingTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        public void valueChanged(ListSelectionEvent event) {
//            clear previous selections
           clearSelectionField();

           if( !event.getValueIsAdjusting() && swingTable.getSelectedRow() >= 0)  {

        K id = (K)swingTable.getValueAt(swingTable.getSelectedRow(), 0);


                VisuComponentInfoEntry selectedEntry = (VisuComponentInfoEntry) getHazelcastList().get(id);
                if (getHelper().getUsername() != null && !getHelper().getUsername().isEmpty()) {
                    selectedEntry.setUserSelected(getHelper().getUsername());

                } else {
                    selectedEntry.setUserSelected("");
                }

                getHazelcastList().put(id, selectedEntry);
            }
       //update the user selection field, add an if clause for the case that a row gets deleted
           
           
        }
    });
        
        // show the frame
        editorFrame.pack();
        editorFrame.setLocationRelativeTo(null);
        editorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editorFrame.setVisible(true);
        editorFrame.toFront();
    
    }
       //TODO: get atomic number
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
       int newID = 0;
        if(!getHazelcastList().isEmpty()){
        newID =  (int)Collections.max(getHazelcastList().keySet())+1;
        }
        
        getHazelcastList().put(newID, new VisuComponentInfoEntry(newID, "New Attribute","New Value"));
    }
       //TODO: get atomic number
    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {
       //send parser XML here
        IMap ordersMap = getHazelcastList();
        System.out.println(((VisuComponentInfoEntry)ordersMap.get(0)).getValue());
        
    }

    
    
        private <K> void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Integer row = swingTable.getSelectedRow();
        K id = (K)swingTable.getValueAt(row, 0);
        try {
            if (getHazelcastList().get(id) != null) {
                getHazelcastList().remove(id);
            } else {
                System.err.println("There is not such an entry in telegram list " + id);
            }
        } catch (Exception e) {
        }
    }
        
        
        public void setXMLTextArea(String text){
            
            this.xmlTextArea.setText(text);
            
            
        }
        
        public void updateXML(){
            String xml = xmlWriter.handleOrder(getHazelcastList());
            setXMLTextArea(xml);
        
        }
        
      public class XMLFieldListener implements EntryListener<Integer, AbstractDistributableField> {

        @Override
        public void entryAdded(EntryEvent<Integer, AbstractDistributableField> ee) {
            updateXML();
        }

        @Override
        public void entryRemoved(EntryEvent<Integer, AbstractDistributableField> ee) {
            updateXML();
        }

        @Override
        public void entryUpdated(EntryEvent<Integer, AbstractDistributableField> ee) {
            updateXML();
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
        
}
