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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import kodemat.visudata.VisuChange;
import kodemat.visudata.change.VisuCreateComponent;
import kodemat.visudata.change.VisuDeleteComponent;
import kodemat.visudata.change.VisuInterpolationChange;
import kodemat.visudata.change.VisuLabelChange;
import kodemat.visudata.change.VisuMarkingChange;
import kodemat.visudata.change.VisuRotationChange;
import kodemat.visudata.change.VisuScaleChange;
import kodemat.visudata.change.VisuTranslationChange;
import kodemat.visudata.change.VisuTypeChange;

/**
 *
 * @author Orthodoxos Kipouridis, Moritz Roidl
 */
public class VisuHistoryRecordUtil {
     private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
     
     
     
     
        public static String getString(String user, String componentName, VisuChange change, Boolean... param) {
        assert param.length <= 1;
        boolean isUndo = param.length > 0 ? param[0].booleanValue() : false;
        Date date = new Date();
        String component = componentName;

//        if (change instanceof VisuAttributeChange) {
//            VisuComponent visucomponent = visuHelper.getComponent(((VisuAttributeChange)change).getId());
//            if (visucomponent != null) {
//                component = visucomponent.getName();
//            }
//        }
//        if (component.contains("BoundingBox")) {
//            return null;
//        }
//        String result = "[" + dateFormat.format(date) + "] -" + user + ": ";
        String result = "[" + dateFormat.format(date) + "] - " ;

//        System.out.println("user " + user);
        if (isUndo) {
            result += "[UNDO] ";
        }
        if (change instanceof VisuCreateComponent) {
            VisuCreateComponent vcm = (VisuCreateComponent) change;
            result += component + " was created";
        }
        if (change instanceof VisuDeleteComponent) {
            VisuDeleteComponent vdc = (VisuDeleteComponent) change;
            result += component + " was deleted";
        }
        if (change instanceof VisuTranslationChange) {
            VisuTranslationChange vtc = (VisuTranslationChange) change;
            result += "set translation to " + vtc.getValue();
            return result;

        }
        if (change instanceof VisuRotationChange) {
            VisuRotationChange vrc = (VisuRotationChange) change;
            result += "set rotation to " + vrc.getValue();
            return result;

        }
        if (change instanceof VisuScaleChange) {
            VisuScaleChange vsc = (VisuScaleChange) change;
            result += "scaled to " + vsc.getValue();
            return result;

        }
        if (change instanceof VisuLabelChange) {
            VisuLabelChange vlc = (VisuLabelChange) change;
            if (vlc.getValue().equals("") || vlc.getValue().contains("Locked")) {
                return null;
            }
            result += "set label to " + vlc.getValue();
            return result;

        }
//        if (change instanceof VisuParentChange) {
//            VisuParentChange vpc = (VisuParentChange) change;
//            result += "changed parent of " + component + " to " + visuHelper.getComponent(vpc.getValue()).getName();
//            return result;
//
//        }
        if (change instanceof VisuTypeChange) {
            VisuTypeChange vtc = (VisuTypeChange) change;
            result += "set type of to " + vtc.getValue();
            return result;

        }
        if (change instanceof VisuInterpolationChange) {
            VisuInterpolationChange vic = (VisuInterpolationChange) change;
            result += "changed interpolation to " + vic.getValue();
            return result;

        }
        if (change instanceof VisuMarkingChange) {
            VisuMarkingChange vmc = (VisuMarkingChange) change;
            result += "changed marking of to " + vmc.getValue();
            return result;

        }
        return null;
    }
    
}
