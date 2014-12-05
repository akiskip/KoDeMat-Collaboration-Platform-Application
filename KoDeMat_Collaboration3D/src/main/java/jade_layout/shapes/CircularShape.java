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
 * Represents a circular shape
 * @author Koshkabb
 */
public class CircularShape implements Shape {

    /** The lower limit of the angle in radians.*/
    float limit_1;
    /** The upper limit of the angle in radians.*/
    float limit_2; 
    /** The radius of the circular shape.*/
    float radius; 
    /** The first coordinate of the center of the circular shape.*/
    float center_1; 
    /** The second coordinate of the center of the circular shape.*/
    float center_2; 
    /** The value of the fixed coordinate in this shape.*/
    float val_fixed;
    /** The index of the fixed coordinate in this shape.*/
    int fixed;
    /** Indicates if the radians increase clockwise.*/
    boolean clockWise;

    /**
     * The global variables are initialized.
     * @param params        ArrayList containing all the required data for 
     * initialization
     * @param clockWise     Indicates if the radians increase clockwise
     */
    public CircularShape(ArrayList<Float> params, boolean clockWise) {
        this.limit_1 = params.get(0);
        this.limit_2 = params.get(1);
        this.radius = params.get(2);
        this.center_1 = params.get(3);
        this.center_2 = params.get(4);
        this.val_fixed = params.get(5);
        this.fixed = Math.round(params.get(6));

        this.clockWise = clockWise;
    }

    /**
     * Calculates the 3D coordinates given a normalized coordinate.
     * @param w     The normalized coordinate
     * @return 
     */
    @Override
    public VisuVector3f calculate3DCoordinates(float w) {
        float angle = limit_1 + w * (limit_2 - limit_1);
        float x, z;
        if (clockWise) {
            if (angle <= Math.PI / 2) {
                z = (float) (-Math.cos(angle) * radius + center_2);
                x = (float) (-Math.sin(angle) * radius + center_1);
            } else {
                z = (float) (+Math.cos(Math.PI - angle) * radius + center_2);
                x = (float) (-Math.sin(Math.PI - angle) * radius + center_1);
            }
        } else {
            if (angle <= Math.PI / 2) {
                z = (float) (Math.cos(angle) * radius + center_2);
                x = (float) (Math.sin(angle) * radius + center_1);
            } else {
                z = (float) (-Math.cos(Math.PI - angle) * radius + center_2);
                x = (float) (Math.sin(Math.PI - angle) * radius + center_1);
            }
        }
        VisuVector3f result = new VisuVector3f();
        switch (fixed) {
            case 0:
                result.x = val_fixed;
                result.y = x;
                result.z = z;
                break;
            case 1:
                result.x = x;
                result.y = val_fixed;
                result.z = z;
                break;
            case 2:
                result.x = x;
                result.y = z;
                result.z = val_fixed;
                break;
        }
        return result;

    }

    /**
     * Calculates the rotation in this position.
     * @param previous       The previous rotation
     * @param w             The normalized location
     * @return              The rotation
     */
    @Override
    public VisuRotation calculateRotation(VisuRotation previous, float w) {
        float degrees = (1 - w) * 180 - 90;
        if (!clockWise) {
         degrees += 180;
         }
        previous.y = degrees;
        return previous;
    }
}
