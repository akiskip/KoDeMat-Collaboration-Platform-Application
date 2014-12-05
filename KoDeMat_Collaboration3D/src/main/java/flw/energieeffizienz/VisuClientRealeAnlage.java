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
import com.hazelcast.config.ListenerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import kodemat.visudata.VisuHelper;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuClientRealeAnlage {

    VisuHelper helper;
    HazelcastInstance client;
    TestTransportOrderWindow window;
    VisualizationControl visualizationControl;
    ITopic<String> cmdTopic;
    ITopic<String> evtTopic;

    private HazelcastInstance getHazelcastClient(String serverAddress) {

//        Properties globalProps = new Properties();
//        try {
//            globalProps.load(new FileReader("./prop.properties"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        String serverAddress = globalProps.getProperty("hazelcast.client.addresses");
//        if (serverAddress == null || serverAddress.equals("")) {
//            throw new IllegalStateException("Could not find server adress");
//        } else {
//            System.out.println("CONNECTING TO SERVER " + serverAddress);
//        }

        LifecycleListener lcl = new LifecycleListener() {
            @Override
            public void stateChanged(LifecycleEvent le) {
                LifecycleEvent.LifecycleState state = le.getState();

                window.setClientState(state.toString());

//                    if (state == LifecycleEvent.LifecycleState.SHUTDOWN) {
//                       
//                    }
//                    if (state == LifecycleEvent.LifecycleState.STARTED) {
//                        
//
//                    }
            }
        };

        ClientConfig clientConfig = new ClientConfig();

        clientConfig.addAddress(serverAddress);

        clientConfig.setSmartRouting(true);
        clientConfig.setRedoOperation(true);
        ListenerConfig lc = new ListenerConfig(lcl);
        clientConfig.addListenerConfig(lc);

        System.out.println("Start Hazelcast Connection");

        client = HazelcastClient.newHazelcastClient(clientConfig);

        System.out.println("Created Hazelcast Client");

        helper = new VisuHelper(client, true, false);

        System.out.println("Created VisuHelper");

        visualizationControl = new VisualizationControl(helper, client, window);

        System.out.println("Created Hazelcast Connection");

        return client;
    }

    private void run() {
        cmdTopic = client.getTopic("kodemat-eea-commands");
        evtTopic = client.getTopic("kodemat-eea-events");

        evtTopic.addMessageListener(new MessageListener<String>() {
            @Override
            public void onMessage(Message<String> msg) {
//                System.out.printf("%d %s\n",
//                        msg.getPublishTime(), msg.getMessageObject());

                visualizationControl.processEvent(msg.getMessageObject());
                window.addLogMessage(msg.getMessageObject());


//                switch (msg.getMessageObject().trim()) {
//                    case "ipc04.01_09.LS_06 true":
//                        cmdTopic.publish("init");
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException ie) {
//                        }
//                        cmdTopic.publish("automove on");
//                        cmdTopic.publish("nach 1");
//                        break;
//                    case "ipc14.04_05.LS_03 true":
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException ie) {
//                        }
//                        cmdTopic.publish("von 1");
//                        cmdTopic.publish("nach 3");
//                        break;
//                    case "ipc14.04_10.LS_09 true":
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException ie) {
//                        }
//                        cmdTopic.publish("von 3");
//                        cmdTopic.publish("nach 1");
//                        break;
//                }
            }
        });

        cmdTopic.addMessageListener(new MessageListener<String>() {
            @Override
            public void onMessage(Message<String> msg) {
                window.addLogMessage("COMMAND " + msg.getMessageObject());
            }
        });
    }

    public void setWindow(TestTransportOrderWindow w) {
        window = w;
    }

    public void shutdown() {
        if (visualizationControl != null) {
            visualizationControl.shutdown();
        }
        if (client != null) {
            client.shutdown();
        }

    }

    public void start(String serverAdress) {
        this.client = getHazelcastClient(serverAdress);
        this.run();
    }

    public void publishCommand(String cmd) {
        if (client != null) {
            cmdTopic.publish(cmd);
        }
    }

    public void publishEvent(String evt) {
        if (client != null) {
            evtTopic.publish(evt);
        }
    }
    public static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss.SSS");

    public static String formatTime(long millis) {
        return time.format(new Date(millis));
    }
}
