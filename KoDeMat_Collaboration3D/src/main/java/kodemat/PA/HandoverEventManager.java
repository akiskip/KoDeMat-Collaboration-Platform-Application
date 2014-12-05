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
package kodemat.PA;

import com.hazelcast.core.IMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import kodemat.tele.test.TelegramField;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;
import kodemat.visudata.VisuHelper;

/**
 *
 * @author Kipouridis
 */
public class HandoverEventManager {

    private IMap<Integer, TelegramField> telegramsMap;
    private final VisuHelper visuHelper;
    private FMLlStepIinitializer fmlMapsInit;

    public HandoverEventManager(VisuHelper helper) {

        this.visuHelper = helper;
        telegramsMap = visuHelper.getTelegrams();
        fmlMapsInit = new FMLlStepIinitializer();
        initAggregateTelegramsMap();

        initStep_1_Map();
        initStep_2_Map();
        initStep_6_Map();
        initStep_7_Map();
        initStep_9_Map();

    }

    public VisuHelper getVisuHelper() {
        return this.visuHelper;
    }

    public void showManagerGUI() {
        new HandoverManagerWindow(getVisuHelper()).setVisible(true);
    }

    /*
     * Map tha holds the process steps for the uebergabe process
     */
    private void initAggregateTelegramsMap() {

        telegramsMap.put(0, new TelegramField(0, "FLW", "Warten"));
        telegramsMap.put(1, new TelegramField(1, "FLW", "Behälter_aufgestellt_telegram"));
        telegramsMap.put(2, new TelegramField(2, "FLW", "Transportauftrag_telegram"));
        telegramsMap.put(3, new TelegramField(3, "FLW", "Behälter_angekommen_telegram"));
        telegramsMap.put(4, new TelegramField(4, "FLW", "Uebergabeanfrage_telegram"));
        telegramsMap.put(5, new TelegramField(5, "FLW", "Stapelfreigabe_telegram"));
        telegramsMap.put(6, new TelegramField(6, "FML", "Beginne_Uebergabe_telegram"));
        telegramsMap.put(7, new TelegramField(7, "FML", "Uebergabe_Beendet_telegram"));
        telegramsMap.put(8, new TelegramField(8, "FLW", "Staperfreigabe_aufgehoben_telegram"));
        telegramsMap.put(9, new TelegramField(9, "FML", "EHB_nach_Bearbeitungsstation_telegram"));

        this.createIndividualTelegramMaps();
    }

    private void createIndividualTelegramMaps() {

        Iterator it = telegramsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String attributeName = ((TelegramField) pairs.getValue()).getName();
            String attributeValue = ((TelegramField) pairs.getValue()).getName();

            this.visuHelper.createTelegramHazelcastMap(attributeValue);

            it.remove(); // avoids a ConcurrentModificationException
        }


    }

    private void initStep_1_Map() {
//        get the map 
        IMap map = this.visuHelper.getHazelcastMap(telegramsMap.get(1).getValue());

        map.put(0, new VisuComponentInfoEntry(0, "key", "value"));
        map.put(1, new VisuComponentInfoEntry(1, "key", "value"));
        map.put(2, new VisuComponentInfoEntry(2, "key", "value"));

    }

    private void initStep_2_Map() {
//        get the map 
        IMap map = this.visuHelper.getHazelcastMap(telegramsMap.get(2).getValue());

        map.put(0, new VisuComponentInfoEntry(0, "key", "value"));
        map.put(1, new VisuComponentInfoEntry(1, "key", "value"));
        map.put(2, new VisuComponentInfoEntry(2, "key", "value"));

    }

    /**
     * Init the map for the order of this step
     */
    private void initStep_6_Map() {
//        get the map 
        IMap map = this.visuHelper.getHazelcastMap(telegramsMap.get(6).getValue());
        // populate the map with values for this step
        fmlMapsInit.initStep_6_Map(map);
    }

    /**
     * Init the map for the order of this step
     */
    private void initStep_7_Map() {
//        get the map 
        IMap map = this.visuHelper.getHazelcastMap(telegramsMap.get(7).getValue());
        // populate the map with values for this step
        fmlMapsInit.initStep_7_Map(map);


    }

    /**
     * Init the map for the order of this step
     */
    private void initStep_9_Map() {
//        get the map 
        IMap map = this.visuHelper.getHazelcastMap(telegramsMap.get(9).getValue());
        // populate the map with values for this step
        fmlMapsInit.initStep_9_Map(map);
    }
}
