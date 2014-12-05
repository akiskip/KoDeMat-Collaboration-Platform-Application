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
package flw.energieeffizienz;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.*;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuEdge;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;

/**
 *
 * @author mari
 */
public class VisuServerWithImlSimulationListener {

    VisuHelper helper;
    HazelcastInstance hz;
//   private TransactionContext transactionalContext;

    public static void main(String args[]) {
        VisuServerWithImlSimulationListener server = new VisuServerWithImlSimulationListener();
        server.server_Start();

    }

    public void server_Start() {
        Config cfg = xmlFileBuild("./hazelcastServer.xml");
        hz = Hazelcast.newHazelcastInstance(cfg);
        helper = new VisuHelper(hz, true, false);

        //FLW mfc
        IMap<Integer, Integer> loadModule = hz.getMap("loadModule");
        IMap<Integer, Double> loadPosition = hz.getMap("loadPosition");

        EntryListener loadModuleListener = new EntryListener() {
            @Override
            public void entryAdded(EntryEvent ee) {

//                System.out.println("Load " + ee.getKey() + " added to module " + ee.getValue());
                VisuEdge edge = helper.getEdge("Module_" + ee.getValue());
                if (edge != null) {
                    VisuComponent comp = getOrCreateLoad("Load_" + ee.getKey());
                    comp.setReferenceToEdge(edge.getId());
                }
            }

            @Override
            public void entryRemoved(EntryEvent ee) {
//                System.out.println("Load " + ee.getKey() + " removed ");
            }

            @Override
            public void entryUpdated(EntryEvent ee) {
//                System.out.println("Load " + ee.getKey() + " updated to module " + ee.getValue());
            }

            @Override
            public void entryEvicted(EntryEvent ee) {
            }

            @Override
            public void mapEvicted(MapEvent me) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mapCleared(MapEvent me) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        loadModule.addEntryListener(loadModuleListener, true);

        EntryListener loadPositionListener = new EntryListener() {
            @Override
            public void entryAdded(EntryEvent ee) {
//                System.out.println("Load " + ee.getKey() + " set to position " + ee.getValue());
                VisuComponent comp = getOrCreateLoad("Load_" + ee.getKey());
                Double d  = (Double)ee.getValue();
                comp.setTranslation(new VisuVector3f(d.floatValue(), 0f, 0f));

            }

            @Override
            public void entryRemoved(EntryEvent ee) {
//                System.out.println("Load " + ee.getKey() + " removed from pos map");
            }

            @Override
            public void entryUpdated(EntryEvent ee) {
//                System.out.println("Load pos " + ee.getKey() + " updated to " + ee.getValue());
            }

            @Override
            public void entryEvicted(EntryEvent ee) {
            }

            @Override
            public void mapEvicted(MapEvent me) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mapCleared(MapEvent me) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        loadPosition.addEntryListener(loadPositionListener, true);

    }

    private VisuComponent getOrCreateLoad(String name) {
        VisuHelper visuHelper = helper;

        VisuComponent box = visuHelper.getComponent(name);
        if (box == null) {
            box = visuHelper.createComponent(name);
            box.setType(new VisuType(VisuType.BOX, null));
            box.setRotation(new VisuRotation(0, 0, 0));
            box.setScale(new VisuVector3f(0.4f, 0.4f, 0.4f));
            box.setLabel(name);
//            box.setInterpolation(new VisuInterpolation(true, true, true));
        }

        return box;
    }

    public Config xmlFileBuild(String FileName) {

        Config cfg_test = new Config();
        try {
            cfg_test = new XmlConfigBuilder(FileName).build();
        } catch (Exception e) {
            System.err.print("Error: " + e.getMessage());

        }
        return cfg_test;
    }
}
