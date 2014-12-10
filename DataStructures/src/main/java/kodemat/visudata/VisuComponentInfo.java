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
import java.util.ArrayList;
import kodemat.versioning.visuHistory.AbstractDistVisuData;
import kodemat.tele.teledata.AbstractDistributableField;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;
import kodemat.versioning.visuHistory.VisuHistoryEvent;


/**
 *
 * @author Koshkabb
 */
public class VisuComponentInfo extends AbstractDistVisuData {
    private String name;
    private VisuComponentInfoEntry infoEntry;

    /**
     * The list that holds the history events
     */
  

    public VisuComponentInfo() {
        super();
    }

    public VisuComponentInfo(long id, ArrayList<AbstractDistributableField> list) {
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

    }

    @Override
    public void writeData(ObjectDataOutput odo) throws IOException {
//       write the current size of the list to read it by deserialisation 
        odo.writeLong(id);
        odo.writeInt(visuDataList.size());

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
        for (int i = 0; i < size; i++) {
            infoEntry = new VisuComponentInfoEntry();
            infoEntry.readData(odi);
            visuDataList.add(infoEntry);
        }

    }

    @Override
  public void addEntryEvent(String user, String attribute, String info) {
       this.name = attribute;
        infoEntry = new VisuComponentInfoEntry(this.getVisuDataList().size(), attribute, info);
        visuDataList.add(infoEntry);
    }


}
