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

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Orthodoxos Kipouridis. Moritz Roidl
 */
public class VisuClientConnectionInfo {

    String serverAddress;
    String name;
    String passwd;
    Integer connTimeout;
    private ClientConfig clientConfig;

    public VisuClientConnectionInfo(String serverAddress, String name, String passwd, Integer connTimeout) {
        clientConfig = new ClientConfig();

        this.serverAddress = serverAddress;
        this.name = name;
        this.passwd = passwd;
        this.connTimeout = connTimeout;

        setUpClientConnectionInfo();
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public void setUpClientConnectionInfo() {


        try {

            clientConfig = new ClientConfig();

            clientConfig.addAddress(serverAddress);

            if (name != null) {
                clientConfig.getGroupConfig().setName(name);
            }
            if (passwd != null) {
                clientConfig.getGroupConfig().setPassword(passwd);
            }
            if (connTimeout != null) {
                clientConfig.setConnectionTimeout(connTimeout.intValue());
            }


            clientConfig.setSmartRouting(true);
         
            clientConfig.setRedoOperation(true);

        } catch (Exception e) {
            System.err.print("Error: " + e.getMessage());
        }


    }

    public ClientConfig getCliectConfig() {
        return clientConfig;
    }

    public ClientConfig fileBuild(String FileName) {

        clientConfig = new ClientConfig();
        try {
            clientConfig = new XmlClientConfigBuilder(FileName).build();
        } catch (Exception e) {
            System.err.print("Error: " + e.getMessage());
        }
        return clientConfig;


    }
}
