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
import com.hazelcast.core.HazelcastInstance;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author Kipouridis
 */
public class VisuHazelcastClient {
    private HazelcastInstance client;
    
    
    
      public HazelcastInstance getHaselcastClient() {

        Properties globalProps = new Properties();
        try {
            globalProps.load(new FileReader("./prop.properties"));
        } catch (FileNotFoundException ex) {
//            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
        }
        ArrayList<String> serverAddresses = new ArrayList<>();
       String addr = globalProps.getProperty("hazelcast.client.addresses");
        serverAddresses.add(addr);
        if (addr == null || addr.equals("")) {         
         System.out.println("Could not find server adress, using localhost:5802");
        } else {
            System.out.println("CONNECTING TO SERVER " + addr);
        }

        ClientConfig clientConfig = new ClientConfig();

        clientConfig.getNetworkConfig().setAddresses(serverAddresses);

        clientConfig.setSmartRouting(true);
        clientConfig.setRedoOperation(true);
        client = HazelcastClient.newHazelcastClient(clientConfig);
//        client = Hazelcast.newHazelcastInstance(this.fileBuild(propertiesFile));
    
        return client;
    }
      public HazelcastInstance getHaselcastClient(ClientConfig cfg) {

        Properties globalProps = new Properties();
        try {
            globalProps.load(new FileReader("./prop.properties"));
        } catch (FileNotFoundException ex) {
//            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
//            Exceptions.printStackTrace(ex);
        }
        ArrayList<String> serverAddresses = new ArrayList<>();
       String addr = globalProps.getProperty("hazelcast.client.addresses");
        serverAddresses.add(addr);
        if (addr == null || addr.equals("")) {         
         System.out.println("Could not find server adress, using localhost:5802");
        } else {
            System.out.println("CONNECTING TO SERVER " + addr);
        }

        ClientConfig clientConfig = cfg;

        clientConfig.setSmartRouting(true);
        clientConfig.setRedoOperation(true);
        client = HazelcastClient.newHazelcastClient(clientConfig);
//        client = Hazelcast.newHazelcastInstance(this.fileBuild(propertiesFile));
    
        return client;
    }

}
