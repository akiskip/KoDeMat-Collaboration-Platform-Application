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

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.EventTableModel;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Kipouridis
 */
public class VisuSyncedTableModel<E extends Object> extends EventTableModel  {

    
    Color rowColor = null;
    public VisuSyncedTableModel(EventList source, TableFormat tableFormat) {
        super(source, tableFormat);
    }

    

    public VisuSyncedTableModel(EventList source, String[] propertyNames, String[] columnLabels, boolean[] writable) {
        super(source, propertyNames, columnLabels, writable);
    }

   
   
    
    public void setRowColour(int row, Color c) {
//        rowColours.set(row, c);
        rowColor = c;
        fireTableRowsUpdated(row, row);
    }

    public Color getRowColour(int row) {
//        return rowColours.get(row);
        return rowColor;
    }

//    @Override
//    public int getRowCount() {
//        return 3;
//    }
//
//    @Override
//    public int getColumnCount() {
//        return 3;
//    }
//
//    @Override
//    public Object getValueAt(int row, int column) {
//        return String.format("%d %d", row, column);
//    }

   
    
}
