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

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import com.jme3.network.Server;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuClientRealeAnlageTestdaten {

    HazelcastInstance client;

    public HazelcastInstance getHazelcastClient() {

        Properties globalProps = new Properties();
        try {
            globalProps.load(new FileReader("./prop.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String serverAddress = globalProps.getProperty("hazelcast.client.addresses");
        if (serverAddress == null || serverAddress.equals("")) {
            throw new IllegalStateException("Could not find server adress");
        } else {
            System.out.println("CONNECTING TO SERVER " + serverAddress);
        }

        ClientConfig clientConfig = new ClientConfig();

        clientConfig.addAddress(serverAddress);

        clientConfig.setSmartRouting(true);
        clientConfig.setRedoOperation(true);
        client = HazelcastClient.newHazelcastClient(clientConfig);

        return client;
    }

    public void run() {
        client = getHazelcastClient();
        ITopic<String> topic = client.getTopic("kodemat-energie-effizienz");


        topic.addMessageListener(new MessageListener<String>() {

            @Override
            public void onMessage(Message<String> msg) {
                Logger.getLogger(Server.class.getName()).log(Level.INFO, "{0} {1}: {2}", new Object[]{formatTime(msg.getPublishTime()), msg.getPublishingMember().getUuid(), msg.getMessageObject()});
            }
        });

    }

    public static void main(String[] args) {
        VisuClientRealeAnlageTestdaten c = new VisuClientRealeAnlageTestdaten();
        c.run();
    }
    
    public static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss.SSS");
    
    public static String formatTime(long millis){
        return time.format(new Date(millis));
    }
}
