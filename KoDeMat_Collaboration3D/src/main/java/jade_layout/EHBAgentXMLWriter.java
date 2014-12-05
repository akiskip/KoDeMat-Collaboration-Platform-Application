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
package jade_layout;

import com.hazelcast.core.IMap;
import java.lang.reflect.Method;
import kodemat.versioning.visuHistory.VisuComponentInfoEntry;



/**
 *
 * @author IBM
 */
public class EHBAgentXMLWriter {
    private static String FINALIZED_STRING="finalized";

    private Order creatOrder(IMap<Integer, VisuComponentInfoEntry> EHBOrdersMap) {
        try {

            Class cls = Class.forName("jade_layout.Order");
            Object order = cls.newInstance();
            //String parameter
            Class[] paramString = new Class[1];
            paramString[0] = String.class;
            //long parameter
            Class[] paramLong = new Class[1];
            paramLong[0] = Long.TYPE;
            //boolean parameter
            Class[] paramInt = new Class[1];
            paramInt[0] = Integer.TYPE;
            for (Integer key : EHBOrdersMap.keySet()) {
                try {
                VisuComponentInfoEntry field;
                if (EHBOrdersMap.get(key) instanceof VisuComponentInfoEntry) {// some elements are of type kodemat.versioning.visuHistory.VisuComponentInfoEntry
                    field = EHBOrdersMap.get(key);
                } else {
                    continue;
                }

                String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                if (field.getName().equals(FINALIZED_STRING)) // this is not part of the final order
                {
                    continue;
                }
                if (field.getName().equals("id")) // those are not string
                {
                    Method method = cls.getDeclaredMethod(methodName, paramLong);
                    method.invoke(order, Long.parseLong(field.getValue()));

                } else if (field.getName().equals("hohePrioritaet")) {
                    Method method = cls.getDeclaredMethod(methodName, paramInt);
                    method.invoke(order, (field.getValue().equals("0")) ? 0 : 1);
                } else {

                    Method method = cls.getDeclaredMethod(methodName, paramString);
                    method.invoke(order, field.getValue());
                }
                
                 } catch (Exception ex) {
                     continue;

        }

            }
            return (Order) order;
        } catch (Exception ex) {
            System.out.println("-" + ex.getMessage());


        }
        return null;
    }

    public String handleOrder(IMap<Integer, VisuComponentInfoEntry> OrdersMap) {
        // create order object from the map
        Order auftrag = creatOrder(OrdersMap);
        // used to be 0,1 for transport and 2 for manual
        String transportIndicator = auftrag.getAuftragsart();

        float[] preStart;
        float[] preGoal;

        int[] start;
        int[] goal;
        int gwp1 = 1, lwp1 = 0, gwp2 = 1, lwp2 = 0;
        // seems like 1 is transport 2 is Leerfahrt (manual?)
        if (!transportIndicator.equals("Leerfahrt")) {

            // 0,1,2 in details: initialNormal[0], initialNormal[1], z --> getStartXPosition,getStartYSchatlstellung,getStartZLWHohe
            preStart = new float[]{getFloatValue(auftrag.getStartXPosition()), getFloatValue(auftrag.getStartYSchatlstellung()), getFloatValue(auftrag.getStartZLWHohe())};
            // 3,4,5 in details: endNormal[0], endNormal[1], z --> getZielXPosition, getZielYSchatlstellung getZielZLWHohe
            preGoal = new float[]{getFloatValue(auftrag.getZielXPosition()), getFloatValue(auftrag.getZielYSchatlstellung()), getFloatValue(auftrag.getZielZLWHohe())};

            auftrag.setStartGWP("1");
            auftrag.setStartLWP("0");
            auftrag.setZielGWP("1");
            auftrag.setZielLWP("0");
            start = new int[]{getIntValue(auftrag.getStartXPosition()), getIntValue(auftrag.getStartYSchatlstellung()), getIntValue(auftrag.getStartZLWHohe())};
            goal = new int[]{getIntValue(auftrag.getZielXPosition()), getIntValue(auftrag.getZielYSchatlstellung()), getIntValue(auftrag.getZielZLWHohe())};



       } else {
            if (auftrag.getStartGWP()!= null)
              gwp1 = Integer.parseInt(auftrag.getStartGWP());
            if (auftrag.getStartLWP()!= null)
             lwp1 = Integer.parseInt(auftrag.getStartLWP());
            if (auftrag.getZielGWP()!= null)
             gwp2 = Integer.parseInt(auftrag.getZielGWP());
            if (auftrag.getZielLWP()!= null)
             lwp2 = Integer.parseInt(auftrag.getZielLWP());
            
            
            start = new int[]{getIntValue(auftrag.getStartXPosition()), getIntValue(auftrag.getStartYSchatlstellung()), getIntValue(auftrag.getStartZLWHohe())};
            goal = new int[]{getIntValue(auftrag.getZielXPosition()), getIntValue(auftrag.getZielYSchatlstellung()), getIntValue(auftrag.getZielZLWHohe())};


        }


        if ((start != null) && (goal != null)) {

            String auftragString = toString(auftrag, start, goal);



            return auftragString;

        }
        return "";

    }

    /**
     * Formats the int value into true or false boolean representation
     */
    private String formatBoolean(int integerValue) {
        if (integerValue == 0) {
            return "FALSE";
        } else {
            return "TRUE";
        }
    }

    private String generateAuftragID() {
        String finalID = "GeneratedAuftrag_" + System.currentTimeMillis();
        return finalID;
    }

    /**
     * Converts this task into a xml format.
     *
     * @return The task in xml format
     */
    public String toString(Order order, int[] start, int[] goal) {
        String generatedAuftragID = generateAuftragID();
        String xml = "<envelope><write><Auftrag>";
        xml += "<DataName>" + generatedAuftragID + "</DataName>";

        if (order.getAuftragsart() != null) {
            xml += "<Auftragsart>" + order.getAuftragsart() + "</Auftragsart>";
        }
        if (order.getEinlasszeit() != null) {
            xml += "<Einlasszeit>" + order.getEinlasszeit() + "</Einlasszeit>";
        }

        xml += "<HohePrioritaet>" + formatBoolean(order.getHohePrioritaet()) + "</HohePrioritaet>";

        if (order.getReserviertFuer() != null) {
            xml += "<ReserviertFuer>" + order.getReserviertFuer() + "</ReserviertFuer>";
        }
        if (order.getErledigt() != null) {
            xml += "<Erledigt>" + order.getErledigt() + "</Erledigt>";
        }

        if (order.getStartGWP() != null) {
            xml += "<StartGWP>" + order.getStartGWP() + "</StartGWP>";
        }
        if (order.getStartLWP() != null) {
            xml += "<StartLWP>" + order.getStartLWP() + "</StartLWP>";
        }
        if (order.getZielGWP() != null) {
            xml += "<ZielGWP>" + order.getZielGWP() + "</ZielGWP>";
        }
        if (order.getZielLWP() != null) {
            xml += "<ZielLWP>" + order.getZielLWP() + "</ZielLWP>";
        }

        xml += "<StartLastwechsel>";
        xml += "<DataName>StartLW_" + generatedAuftragID + "</DataName>";


        if (order.getStartType() != null) {
            xml += "<Typ>" + order.getStartType() + "</Typ>";
        }

        if (order.getStartModul() != null) {
            xml += "<Modul>" + order.getStartModul() + "</Modul>";
        }

        //if (order.getStart != null) {
        //   xml += "<TE>" + StartTE + "</TE>";
        //}

        if (order.getStartLastWechsel() != null) {
            xml += "<Wechsel>" + order.getStartLastWechsel() + "</Wechsel>";
        }
        if (order.getStartZugriffsart() != null) {
            xml += "<Zugriffsart>" + order.getStartZugriffsart() + "</Zugriffsart>";
        }
        if (order.getStartGreifer() != null) {
            xml += "<Greiferkonfig>" + order.getStartGreifer() + "</Greiferkonfig>";
        }

        xml += "<Greifkoordinaten>";
        xml += "<DataName>StartLW_Koord_" + generatedAuftragID + "</DataName>";

        xml += "<X>" + start[0] + "</X>";
        xml += "<Y>" + start[1] + "</Y>";
        xml += "<Z>" + start[2] + "</Z>";

        xml += "</Greifkoordinaten>";
        xml += "</StartLastwechsel>";

        xml += "<ZielLastwechsel>";
        xml += "<DataName>ZielLW_" + generatedAuftragID + "</DataName>";


        if (order.getZielTyp() != null) {
            xml += "<Typ>" + order.getZielTyp() + "</Typ>";
        }

        if (order.getZielModul() != null) {
            xml += "<Modul>" + order.getZielModul() + "</Modul>";
        }

        if (order.getZielLastWechsel() != null) {
            xml += "<Wechsel>" + order.getZielLastWechsel() + "</Wechsel>";
        }
        if (order.getZielZugriffsart() != null) {
            xml += "<Zugriffsart>" + order.getZielZugriffsart() + "</Zugriffsart>";
        }
        if (order.getZielGreifer() != null) {
            xml += "<Greiferkonfig>" + order.getZielGreifer() + "</Greiferkonfig>";
        }
        xml += "<Greifkoordinaten>";
        xml += "<DataName>ZielLW_Koord_" + generatedAuftragID + "</DataName>";

        xml += "<X>" + goal[0] + "</X>";
        xml += "<Y>" + goal[1] + "</Y>";
        xml += "<Z>" + goal[2] + "</Z>";

        xml += "</Greifkoordinaten>";
        xml += "</ZielLastwechsel>";

        xml += "</Auftrag></write><delete><Verhandlung>"
                + "<condition name=\"Auftrag\" operation=\"EQ\" value=\"" + generatedAuftragID + "\"/>"
                + "</Verhandlung></delete></envelope>";

        return xml;



    }

      /* This methdod returns int parsed value of string 
     * it catches exception in case the value was not provided
     * @param: 
     */
    public static int getIntValue(String value) {
        try {
            int valueInt = Integer.parseInt(value);
            return valueInt;
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }

    }

    /**
     * This methdod returns float parsed value of string it catches exception in
     * case the value was not provided
     *
     * @param:
     */
    public static Float getFloatValue(String value) {
        try {
            Float valueFloat = Float.parseFloat(value);
            return valueFloat;
        } catch (NullPointerException| NumberFormatException e) {
            return Float.NaN;
        }

    }
}
