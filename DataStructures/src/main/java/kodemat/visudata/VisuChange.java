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

import com.hazelcast.nio.serialization.DataSerializable;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
//TODO: Check if IdentifiedDataSerializable can replace DeataSerialisable

/*Serialization: IdentifiedDataSerializable is a slightly optimized version of 
 * DataSerializable that doesn’t use class name, thus reflection for deserializing the objects. 
 * This way de-serialization is faster and it is actually language independent. */
public interface VisuChange extends DataSerializable {

    public static short CREATE = 0;
    public static short DELETE = 1;
    public static short TRANSLATION = 2;
    public static short ROTATION = 3;
    public static short SCALE = 4;
    public static short LABEL = 5;
    public static short PARENT = 6;
    public static short TYPE = 7;
    public static short INTERPOLATION = 8;
    public static short MARKING = 9;
    public static short UNDO = 10;
    public static short REDO = 11;
    public static short UNDO_DELETE = 14;
    public static short ADD_TO_GLOBAL_UNDO_DELETE = 15;
    public static short ADD_TO_GLOBAL_UNDO_CREATE = 16;
    public static short CHAT_MESSAGE = 17;
    public static short NEW_CHANGE = 18;
    public static short EDGE_SOURCE = 19;
    public static short EDGE_TARGET = 20;
    public static short CREATE_EDGE = 21;
    public static short DELETE_EDGE = 22;
    public static short EDGE_COLOR = 23;
    public static short REFERENCE_TO_EDGE = 24;
    public static short COMPOSITE_COMPONENT_CHANGE = 25;
    
    

    short getType();
}
