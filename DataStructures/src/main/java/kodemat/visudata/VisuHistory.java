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

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import kodemat.versioning.visuHistory.AbstractDistVisuData;
import kodemat.tele.teledata.AbstractDistributableField;
import kodemat.versioning.visuHistory.VisuHistoryEvent;
import kodemat.versioning.visuHistory.VisuHistoryRecordUtil;

/**
 *
 * @author Koshkabb
 */
public class VisuHistory extends AbstractDistVisuData {
    private String name;
    public AbstractDistributableField historyEvent;

    /**
     * The list that holds the history events
     */
  

    public VisuHistory() {
        super();
    }

    public VisuHistory(long id, ArrayList<AbstractDistributableField> list) {
       super(id,list);
    }
       @Override
    public String getName() {
       return this.name;
    }

    @Override
    public void setName(String name) {
this.name = name;
    }

    @Override
    public void addEntryEvent(String user, String componentName, VisuChange change) {
        this.name = componentName;
        historyEvent = new VisuHistoryEvent(0, user, VisuHistoryRecordUtil.getString(user, componentName, change));
        visuDataList.add(historyEvent);
    }

    @Override
    public void writeData(ObjectDataOutput odo) throws IOException {
//       write the current size of the list to read it by deserialisation 
        odo.writeLong(id);
        odo.writeInt(visuDataList.size());
//        System.out.println("Started Write Size " + getHistorySize());
        if (this.getVisuDataList() != null) {
            for (AbstractDistributableField e : this.getVisuDataList()) {
                e.writeData(odo);
//                System.out.println("Wrote event " + e.getValue());
            }
        }
    }

    @Override
    /**
     *Caution!!! The order of reading and writing the variables should be the same!!
     * 
     */
    public void readData(ObjectDataInput odi) throws IOException {
        visuDataList = new ArrayList<>();
        id = odi.readLong();
        int size = odi.readInt();
//        System.out.println("Read size " + size);

        for (int i = 0; i < size; i++) {
            historyEvent = new VisuHistoryEvent();
            historyEvent.readData(odi);
            visuDataList.add(historyEvent);

//            System.out.println("Read event" + historyEvent.getValue());

        }

    }

    @Override
    public void addEntryEvent(String user, String componentName, String info) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 




}
