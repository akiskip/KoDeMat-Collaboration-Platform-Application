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
package kodemat.versioning.visuHistory;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import kodemat.tele.teledata.IObservableData;
import kodemat.tele.teledata.AbstractDistributableField;
import kodemat.versioning.visuHistory.VisuHistoryEvent;
import kodemat.versioning.visuHistory.VisuHistoryRecordUtil;
import kodemat.visudata.VisuChange;

/**
 *
 * @author Kipouridis
 */
public abstract class AbstractDistVisuData implements DataSerializable,IObservableData<Long, ArrayList<AbstractDistributableField>>  {

    /**
     * The list that holds the history events
     */
    public ArrayList<AbstractDistributableField> visuDataList;
   
    public Long id;



    public AbstractDistVisuData() {
    }

    public AbstractDistVisuData(long id, ArrayList<AbstractDistributableField> list) {
        this.visuDataList = list;
        this.id = id;

    }

    public abstract void addEntryEvent(String user, String componentName, VisuChange change) ;
    
    public abstract void addEntryEvent(String user, String componentName, String info) ;

    public ArrayList<AbstractDistributableField> getVisuDataList() {
        ArrayList<AbstractDistributableField> list = this.visuDataList;
        return list;
    }

    public AbstractDistributableField getLastVisuDataEntry() {
        return this.getVisuDataList().get(visuDataList.size() - 1);
    }

    @Override
    public abstract void writeData(ObjectDataOutput odo) throws IOException;

 
    @Override
    public abstract void readData(ObjectDataInput odi) throws IOException ;
    

    @Override
    public Long getId() {
        return this.id;
    }

 
    @Override
    public ArrayList<AbstractDistributableField> getValue() {
        return this.getVisuDataList();
    }

    @Override
    public void setValue(ArrayList<AbstractDistributableField> value) {
this.visuDataList = value;
    }



}
