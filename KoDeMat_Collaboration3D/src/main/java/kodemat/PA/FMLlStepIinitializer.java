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
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;

/**
 * Class that holds the values for the maps of orders for different steps:
 * Beginne_Uebergabe_telegram,Uebergabe_Beendet_telegram,EHB_nach_Bearbeitungsstation_telegram
 * @author Amjad
 */
public class FMLlStepIinitializer {

    public FMLlStepIinitializer() {
    }

    void initStep_6_Map(IMap map) {
        map.put(0, new VisuComponentInfoEntry(0, "id", "0"));
        map.put(1, new VisuComponentInfoEntry(1, "auftragsart", "Leerfahrt"));
        map.put(2, new VisuComponentInfoEntry(2, "hohePrioritaet", "0"));
        map.put(3, new VisuComponentInfoEntry(3, "startType", "Position"));
        map.put(4, new VisuComponentInfoEntry(4, "ZielTyp", "Position"));
        map.put(5, new VisuComponentInfoEntry(5, "startModul", ""));
        map.put(6, new VisuComponentInfoEntry(6, "zielModul", ""));
        map.put(7, new VisuComponentInfoEntry(7, "startGWP", "1"));
        map.put(8, new VisuComponentInfoEntry(8, "startLWP", "0"));
        map.put(9, new VisuComponentInfoEntry(9, "zielGWP", "1"));
        map.put(10, new VisuComponentInfoEntry(10, "zielLWP", "0"));
        map.put(11, new VisuComponentInfoEntry(11, "startXPosition", "3873"));
        map.put(12, new VisuComponentInfoEntry(12, "zielXPosition", "2000"));
        map.put(13, new VisuComponentInfoEntry(13, "startYSchatlstellung", "13849"));
        map.put(14, new VisuComponentInfoEntry(14, "zielYSchatlstellung", "12000"));
        map.put(15, new VisuComponentInfoEntry(15, "startZLWHohe", "911"));
        map.put(16, new VisuComponentInfoEntry(16, "zielZLWHohe", "914"));
        map.put(17, new VisuComponentInfoEntry(17, "startGreifer", "B-----"));
        map.put(18, new VisuComponentInfoEntry(18, "startLastWechsel", "None"));
        map.put(19, new VisuComponentInfoEntry(19, "zielLastWechsel", "None"));
        map.put(20, new VisuComponentInfoEntry(20, "startZugriffsart", "Automatik"));
        map.put(21, new VisuComponentInfoEntry(21, "zielZugriffsart", "Automatik"));
        map.put(22, new VisuComponentInfoEntry(22, "erledigt", ""));
        map.put(23, new VisuComponentInfoEntry(23, "reserviertFuer", "none"));
        map.put(24, new VisuComponentInfoEntry(24, "einlasszeit", "0"));
        map.put(25, new VisuComponentInfoEntry(25, "finalized", "false"));

    }

    public void initStep_7_Map(IMap map) {
        map.put(0, new VisuComponentInfoEntry(0, "id", "1"));
        map.put(1, new VisuComponentInfoEntry(1, "auftragsart", "Transport"));
        map.put(2, new VisuComponentInfoEntry(2, "hohePrioritaet", "0"));
        map.put(3, new VisuComponentInfoEntry(3, "startType", "Position"));
        map.put(4, new VisuComponentInfoEntry(4, "ZielTyp", "Position"));
        map.put(5, new VisuComponentInfoEntry(5, "startModul", ""));
        map.put(6, new VisuComponentInfoEntry(6, "zielModul", ""));
        map.put(7, new VisuComponentInfoEntry(7, "startGWP", "1"));
        map.put(8, new VisuComponentInfoEntry(8, "startLWP", "0"));
        map.put(9, new VisuComponentInfoEntry(9, "zielGWP", "1"));
        map.put(10, new VisuComponentInfoEntry(10, "zielLWP", "0"));
        map.put(11, new VisuComponentInfoEntry(11, "startXPosition", "3873"));
        map.put(12, new VisuComponentInfoEntry(12, "zielXPosition", "2000"));
        map.put(13, new VisuComponentInfoEntry(13, "startYSchatlstellung", "13849"));
        map.put(14, new VisuComponentInfoEntry(14, "zielYSchatlstellung", "12000"));
        map.put(15, new VisuComponentInfoEntry(15, "startZLWHohe", "911"));
        map.put(16, new VisuComponentInfoEntry(16, "zielZLWHohe", "914"));
        map.put(17, new VisuComponentInfoEntry(17, "startGreifer", "B-----"));
        map.put(18, new VisuComponentInfoEntry(18, "startLastWechsel", "Aufnahme"));
        map.put(19, new VisuComponentInfoEntry(19, "zielLastWechsel", "Abgabe"));
        map.put(20, new VisuComponentInfoEntry(20, "startZugriffsart", "Automatik"));
        map.put(21, new VisuComponentInfoEntry(21, "zielZugriffsart", "Automatik"));
        map.put(22, new VisuComponentInfoEntry(22, "erledigt", ""));
        map.put(23, new VisuComponentInfoEntry(23, "reserviertFuer", "none"));
        map.put(24, new VisuComponentInfoEntry(24, "einlasszeit", "0"));
        map.put(25, new VisuComponentInfoEntry(25, "finalized", "false"));

    }

    public void initStep_9_Map(IMap map) {
        map.put(0, new VisuComponentInfoEntry(0, "id", "2"));
        map.put(1, new VisuComponentInfoEntry(1, "auftragsart", "Leerfahrt"));
        map.put(2, new VisuComponentInfoEntry(2, "hohePrioritaet", "0"));
        map.put(3, new VisuComponentInfoEntry(3, "startType", "Position"));
        map.put(4, new VisuComponentInfoEntry(4, "ZielTyp", "Position"));
        map.put(5, new VisuComponentInfoEntry(5, "startModul", ""));
        map.put(6, new VisuComponentInfoEntry(6, "zielModul", ""));
        map.put(7, new VisuComponentInfoEntry(7, "startGWP", "1"));
        map.put(8, new VisuComponentInfoEntry(8, "startLWP", "0"));
        map.put(9, new VisuComponentInfoEntry(9, "zielGWP", "1"));
        map.put(10, new VisuComponentInfoEntry(10, "zielLWP", "0"));
        map.put(11, new VisuComponentInfoEntry(11, "startXPosition", "3873"));
        map.put(12, new VisuComponentInfoEntry(12, "zielXPosition", "2000"));
        map.put(13, new VisuComponentInfoEntry(13, "startYSchatlstellung", "13849"));
        map.put(14, new VisuComponentInfoEntry(14, "zielYSchatlstellung", "12000"));
        map.put(15, new VisuComponentInfoEntry(15, "startZLWHohe", "911"));
        map.put(16, new VisuComponentInfoEntry(16, "zielZLWHohe", "914"));
        map.put(17, new VisuComponentInfoEntry(17, "startGreifer", "B-----"));
        map.put(18, new VisuComponentInfoEntry(18, "startLastWechsel", "None"));
        map.put(19, new VisuComponentInfoEntry(19, "zielLastWechsel", "None"));
        map.put(20, new VisuComponentInfoEntry(20, "startZugriffsart", "Automatik"));
        map.put(21, new VisuComponentInfoEntry(21, "zielZugriffsart", "Automatik"));
        map.put(22, new VisuComponentInfoEntry(22, "erledigt", ""));
        map.put(23, new VisuComponentInfoEntry(23, "reserviertFuer", "none"));
        map.put(24, new VisuComponentInfoEntry(24, "einlasszeit", "0"));
        map.put(25, new VisuComponentInfoEntry(25, "finalized", "false"));

    }
}
