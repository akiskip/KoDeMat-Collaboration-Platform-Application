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
package kodemat.visu.layoutScripts;


import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import java.io.IOException;
import kodemat.visudata.VisuHelper;

/**
 *
 * @author Koshkabb
 */
public class TestClientGlobal {

    /*
     * Haselcast Client
     */
    HazelcastInstance client;
    VisuHelper helper;


    public TestClientGlobal() {
        //conf file for haselcast
        ClientConfig cfg = fileBuild("./prop.properties");
        client = HazelcastClient.newHazelcastClient(cfg);
        helper = new VisuHelper(client);
    }

    public void startTest(int count) {
        System.out.println("UNDO:");
        helper.performGlobalUndo();
        sleep(3000);
        System.out.println("REDO:");
        helper.performGlobalRedo();
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

    public void shutdown() {
        client.shutdown();
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
        }
    }

    public static void main(String[] args) {
        TestClientGlobal tc = new TestClientGlobal();
        for (int i = 0; i < 1; i++) {
            tc.startTest(i);
        }
        tc.shutdown();
    }
}
