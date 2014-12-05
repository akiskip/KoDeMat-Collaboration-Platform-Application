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

import jade_layout.shapes.OvalShape;
import jade_layout.shapes.Shape;
import jade_layout.shapes.StraightShape;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuVector3f;

/**
 * Class that translates coordinates: [0,1] ranged coordinates <--> 3D coordinates
 * @author Koshkabb
 */
public class VisuCoordinateTranslator {

    /**Indicates the direction in which a model can move. */
    HashMap<String, Integer> limitedDirections;
    /** Given a waypoint and a direction, returns the shape of this*/
    HashMap<String, Shape> limitedCoordinates;
    /** Holds the limits of the ground, given a waypoint and a direction.*/
    HashMap<String, Float[]> limitedGroundCoordinates;

    /**
     * Initializes the values in the Hashmaps.
     */
    public VisuCoordinateTranslator() {
        limitedDirections = new HashMap<String, Integer>();
        limitedCoordinates = new HashMap<String, Shape>();
        limitedGroundCoordinates = new HashMap<String, Float[]>();
        Float c[] = {22f, 2f, 8.5f, -1.7f, 0f};
        limitedDirections.put("Status_GWP01", 0);
        ArrayList a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        StraightShape ss = new StraightShape(a);
        limitedCoordinates.put("0.1.0", ss);


        c = new Float[]{-0.97f, 0.88f, 0f, -0.1f, 1f};
        limitedDirections.put("Status_ETKa1", 1);
        a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        ss = new StraightShape(a);
        limitedCoordinates.put("1.1.0", ss);

        limitedDirections.put("Status_ETKa2", 1);

        c = new Float[]{0.07f, -0.74f, 0f, 0f, 1f};
        limitedDirections.put("LAM_ETKa1", 2);
        a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        ss = new StraightShape(a);
        limitedCoordinates.put("2.1.0", ss);

        c = new Float[]{0.07f, -0.74f, 0f, 0f, 1f};
        limitedDirections.put("LAM_ETKa2", 2);
        a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        ss = new StraightShape(a);


        limitedCoordinates.put("2.1.0", ss);

        ArrayList<ArrayList<Float>> arrayOval = new ArrayList<ArrayList<Float>>();
        c = new Float[]{6.3f, -2f, 7.3f, -14.38f, 0f};//straight_1
        a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        arrayOval.add(a);

        c = new Float[]{-2f, -4.8f, 7.3f, -14.38f, 0f};//straight_2
        a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        arrayOval.add(a);

        c = new Float[]{3.072f, 0.0695f, 2.826f, -5.424f, -17.2f, 7.3f, 1f};//circular_3
        a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        arrayOval.add(a);

        c = new Float[]{-4.8f, -2f, 7.3f, -19.715f, 0f};//straight_4
        a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        arrayOval.add(a);

        c = new Float[]{-2f, 6.3f, 7.3f, -19.715f, 0f};//straight_5
        a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        arrayOval.add(a);

        c = new Float[]{3.038f, 0.1032f, 2.833f, 6.567f, -17.2f, 7.3f, 1f};//circular_6
        a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        arrayOval.add(a);

        c = new Float[]{3.1302f, 0.0114f, 2.8202f, -2.0298f, -17.2f, 7.3f, 1f};//circular_7
        a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        arrayOval.add(a);
        
        c = new Float[]{0.41f, 0.48f, 0.6f, 0.67f, 0.78f, 0.92f};//partitions
        a = new ArrayList<Float>();
        a.addAll(Arrays.asList(c));
        arrayOval.add(a);


        OvalShape os = new OvalShape(arrayOval);

        for (int i = 2; i < 11; i++) {
            for (int j = 0; j < 3; j++) {
                limitedCoordinates.put("1." + i + "." + j, os);
                limitedCoordinates.put("2." + i + "." + j, ss);
            }
        }

        
       
       c = new Float[]{2.0785f, -1.8972f};
        limitedGroundCoordinates.put("0.1.0", c);
         c = new Float[]{-6.3859f,-0.4682f};
        limitedGroundCoordinates.put("1.1.0", c);
    }

    /**
     * Calculates the 3D coordiantes of a coordinate in the range [0,1].
     * @param modelName     The name of the model whose coordinate needs to be 
     * calculated
     * @param coordinate    The [0,1] coordinate
     * @param wp            The waypoint of the model
     * @return 
     */
    public VisuVector3f getVisuCoordinates(String modelName, float coordinate, String wp) {
        Integer direction = limitedDirections.get(modelName);

        Shape shape = limitedCoordinates.get(direction + "." + wp);
        if (shape == null) {
            return null;
        }
        return shape.calculate3DCoordinates(checkDistance(coordinate));
    }

    /**
     * Calculates de rotation of a model.
     * @param modelName         The name of the model
     * @param coordinate        The coordinate of the model in range [0,1]
     * @param wp                The waypoint of the model
     * @param previous           The previous rotation of the model
     * @return 
     */
    public VisuRotation getVisuRotation(String modelName, float coordinate, String wp, VisuRotation previous) {
        Integer direction = limitedDirections.get(modelName);

        Shape shape = limitedCoordinates.get(direction + "." + wp);
        if (shape == null) {
            return null;
        }
        return shape.calculateRotation(previous, checkDistance(coordinate));
    }

    /**
     * Transforms 3D coordinates into normalized coordinates (in range [0,1])
     * @param coordinates     The 3D coordinates
     * @return                  The normalized coordinates
     */
    public float[] getNormalCoordinates(VisuVector3f coordinates) {
        //Assume we are in 1.0
        Float[] limits = limitedGroundCoordinates.get("0.1.0");
        float y = (coordinates.x - limits[0]) / (limits[1] - limits[0]);
        limits = limitedGroundCoordinates.get("1.1.0");
        float x = (coordinates.z - limits[0]) / (limits[1] - limits[0]);
        float[] finalCoordinates = new float[]{checkDistance(x), checkDistance(y)};
        return finalCoordinates;
    }

    /**
     * Checks if a normalized coordinate is inside its range.
     * @param d     The coordinate
     * @return      The fixed normalized coordinate
     */
    private float checkDistance(float d) {
        if (d < 0) {
            return 0f;
        }
        if (d > 1) {
            return 1f;
        }
        return d;
    }
}
