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
import com.hazelcast.core.IMap;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import kodemat.tele.teledata.AbstractDistributableField;
import kodemat.tele.test.TelegramField;

/**
 *
 * @author Orthodoxos Kipouridis. Moritz Roidl
 */
public class DSASyncedTable<T,K> extends AbstractSyncedTable_Field {
    
    public DSASyncedTable(IMap hazelcastMap, T ValuesType)
    {
        super(hazelcastMap,ValuesType,"Stapler");
        
    }

    @Override
    public void setUpSwingTable(Object contentClassType) {
        //TODO: build a JTable based on the hazelcast map
        String[] propertyNames = {"id", "name", "value"};
        String[] columnLabels = {"IDs", "Name", "Value"};
        boolean[] editable = new boolean[3];
        editable[0] = false;
        editable[1] = true;
        editable[2] = true;

        //TODO: should get the class type instead of the the first entrys type
        tf = GlazedLists.tableFormat((Class<?>)contentClassType, propertyNames, columnLabels, editable);
        swingTable = new JTable(new EventTableModel<AbstractDistributableField>(super.getFunctionList(), tf));
        
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
        getHazelcastList().put(getHazelcastList().size(), new TelegramField(getHazelcastList().size(), "New Field "));
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
}
