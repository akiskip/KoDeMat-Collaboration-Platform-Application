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
package jade_layout.shapes;

import java.util.ArrayList;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuVector3f;

/**
 * Represents a straight shape.
 *
 * @author Koshkabb
 */
public class StraightShape implements Shape {

    /** Lower limit of the straight line.*/
    float limit_1;
    /** Upper limit of the straight line.*/
    float limit_2;
    /** The value of the first fixed coordinate.*/
    float coord_1;
    /**The value of the second fixed coordinate.*/
    float coord_2;
    /** The index of the variable coordinate.*/
    int pos_variable;
    /** The rotation in degrees of the object (if there is one)*/
    int rotation;

    /**
     * Initializes the global variables.
     * @param params    ArrayList containing all the data pertaining to the
     * global variables.
     */
    public StraightShape(ArrayList<Float> params) {
        limit_1 = params.get(0);
        limit_2 = params.get(1);
        coord_1 = params.get(2);
        coord_2 = params.get(3);
        pos_variable = Math.round(params.get(4));
        rotation = -1;
    }

    /**
     * Sets the rotation.
     * @param rot       Rotation in degrees
     */
    public void setRotationOn(int rot) {
        rotation = rot;
    }

    /**
     * Calculates the 3D coordinates given a normalized coordinate.
     * @param w     The normalized coordinate
     * @return      The 3D coordinates
     */
    @Override
    public VisuVector3f calculate3DCoordinates(float w) {
        float d = limit_1 + w * (limit_2 - limit_1);
        VisuVector3f result = new VisuVector3f();
        switch (pos_variable) {
            case 0:
                result.x = d;
                result.y = coord_1;
                result.z = coord_2;
                break;
            case 1:
                result.x = coord_1;
                result.y = d;
                result.z = coord_2;
                break;
            case 2:
                result.x = coord_1;
                result.y = coord_2;
                result.z = d;
                break;
        }
        return result;
    }

    /**
     * Calculates the rotation. If there is no special rotation, the previous
     * rotation is returned.
     * @param previous      The previous rotation in degrees
     * @param w             The normalized coordinate of position
     * @return              The calculated rotation
     */
    @Override
    public VisuRotation calculateRotation(VisuRotation previous, float w) {
        if (rotation != -1) {
            return new VisuRotation(0, rotation, 0);
        }
        return previous;
    }
}
