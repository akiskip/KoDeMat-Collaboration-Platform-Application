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

import jade_layout.shapes.CircularShape;
import jade_layout.shapes.Shape;
import jade_layout.shapes.StraightShape;
import java.util.ArrayList;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuVector3f;

/**
 * Represents an oval shape, formed of straight shapes and circular shapes.
 * @author Koshkabb
 */
public class OvalShape implements Shape {

    /** The shapes that this shape contains*/
    ArrayList<Shape> shapes;
    /** ArrayList that indicates how the shapes are distributed*/
    ArrayList<Float> partitions;

    /**
     * The ArrayLists are filled in with the necessary data.
     * @param params    The data.
     */
    public OvalShape(ArrayList<ArrayList<Float>> params) {
        shapes = new ArrayList<Shape>();
        StraightShape ss = new StraightShape(params.get(0));
        ss.setRotationOn(90);
        shapes.add(ss);
        ss = new StraightShape(params.get(1));
        ss.setRotationOn(90);
        shapes.add(ss);
        shapes.add(new CircularShape(params.get(2), true));
        ss = new StraightShape(params.get(3));
        ss.setRotationOn(270);
        shapes.add(ss);
        ss = new StraightShape(params.get(4));
        ss.setRotationOn(270);
        shapes.add(ss);
        shapes.add(new CircularShape(params.get(5), false));
        shapes.add(new CircularShape(params.get(6), true));
        partitions = params.get(7);
    }

    /**
     * Calculates the partition to which the postion pertains.
     * @param w       The normalized coordinate
     * @return          The index of the partition/shape
     */
    private int getPartition(float w) {
        int i;
        for (i = 0; i < partitions.size(); i++) {
            if (w < partitions.get(i)) {
                break;
            }
        }
        return i;
    }

    /**
     * Calculates the normalized coordinate with respect to the partition.
     * @param w     The general normalized coordinate
     * @param i     The index of the partition/shape
     * @return      The normalized coordinate with respect to the partition
     */
    private float normalizeToPartition(float w, int i) {

        Float range;
        Float initial;
        if (i == 0) {
            range = partitions.get(i);
            initial = 0f;
        } else if (i == partitions.size()) {
            range = 1 - partitions.get(i - 1);
            initial = partitions.get(i - 1);
        } else {
            range = partitions.get(i) - partitions.get(i - 1);
            initial = partitions.get(i - 1);
        }
        return (w - initial) / range;
    }

    /**
     * Calculates the 3D coordinates in this shape, depending to which partition
     * it corresponds to.
     * @param w     The normalized coordinate
     * @return      The 3D coordinates
     */
    @Override
    public VisuVector3f calculate3DCoordinates(float w) {
        int part = getPartition(w);
        float val = normalizeToPartition(w, part);
        return shapes.get(part).calculate3DCoordinates(val);
    }

    /**
     * Calculates the rotation in this shape, depending to which partition it
     * corresponds to.
     * @param previous   The previous rotation
     * @param w            The normalized coordinate
     * @return          The rotation
     */
    @Override
    public VisuRotation calculateRotation(VisuRotation previous, float w) {
        int part = getPartition(w);
        float val = normalizeToPartition(w, part);
        return shapes.get(part).calculateRotation(previous, val);
    }
}