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
package kodemat.visu;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import java.io.IOException;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.command.VisuChangeToWorldPosition;
import kodemat.visudata.command.VisuCommand;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class TestServerCommands {
    HazelcastInstance client;
    VisuHelper helper;

    public TestServerCommands() {
        ClientConfig cfg = fileBuild("./prop.properties");
        client = HazelcastClient.newHazelcastClient(cfg);
        helper = new VisuHelper(client);
    }
    
    private void startTest() {
        IQueue<VisuCommand> commands;
        commands = client.getQueue("kodemat.visu.commands");
        commands.addItemListener(new CommandListener(), true);
        commands.add(new VisuChangeToWorldPosition(12));
        shutdown();
    }
    
    private ClientConfig fileBuild(String FileName) {

        ClientConfig cfg_test = new ClientConfig();
        try {
            cfg_test = new XmlClientConfigBuilder(FileName).build();
        } catch (IOException e) {
            System.err.print("Error: " + e.getMessage());
        }
        return cfg_test;


    }
    
    public void shutdown(){
        client.shutdown();
    }
    
    public void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            
        }
    }
    
    public static void main(String[] args) {
        TestServerCommands tc = new TestServerCommands();
        tc.startTest();
    }
    
    class CommandListener implements ItemListener<VisuCommand> {

        @Override
        public void itemAdded(ItemEvent<VisuCommand> ie) {
            System.out.println("ITEM ADDED");
            
        }

        @Override
        public void itemRemoved(ItemEvent<VisuCommand> ie) {
            System.out.println("ITEM REMOVED");
        }
    }

    
}
