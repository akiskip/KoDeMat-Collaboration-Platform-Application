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

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.util.List;

/**
 *Class that is used to display
 * the information about the current hazelcast session, number of connected clients
 * username etc
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class VisuClientStateView extends Node implements Control, ClientStateChangeInterface {

    private BitmapText[] labels;
    private String[] statLabels;
    private boolean changed = true;
    private boolean enabled = true;
    private final StringBuilder stringBuilder = new StringBuilder();

    public VisuClientStateView(String name, AssetManager manager) {
        super(name);

        setQueueBucket(RenderQueue.Bucket.Gui);
        setCullHint(Spatial.CullHint.Never);

        statLabels = new String[]{"Client Status = No Connection : Press G to Connect to Server", "", "", "Press F1 for Help"};
        labels = new BitmapText[statLabels.length];

        BitmapFont font = manager.loadFont("Interface/Fonts/Console.fnt");
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new BitmapText(font);
            labels[i].setLocalTranslation(0, -labels[i].getLineHeight() * (i), 0);
            attachChild(labels[i]);
        }

        addControl();
    }

    private void addControl() {
        addControl(this);
    }

    @Override
    public void update(float tpf) {

        if (!isEnabled()) {
            return;
        }

        if (changed) {
            for (int i = 0; i < labels.length; i++) {
                stringBuilder.setLength(0);
                stringBuilder.append(statLabels[i]);
                labels[i].setText(stringBuilder);
            }
            changed = false;
        }


    }

    public Control cloneForSpatial(Spatial spatial) {
        return (Control) spatial;
    }

    public void setSpatial(Spatial spatial) {
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void render(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void changeLocalClientState(String status, List<String> InetAddrCollection) {
        
        statLabels[0] = "Client Status = " + status + " with Server at " + InetAddrCollection;
        changed = true;
    }
    
    @Override
    public void setNumberOfClients(int numberofclients) {

        statLabels[1] = ("Total Connected Clients =  " + numberofclients);
        changed = true;
    }
    
    @Override
    public void changeRemoteState(String client, String add, String behavior) {
     
//        resetTimer();

        String[] splittedMessage = client.split("@");
        String ipaddress = add;
        String name = splittedMessage[splittedMessage.length - 1];
        if (behavior.equals("connect")) {
            statLabels[2] = ("Client " + name + " connected from " + ipaddress);
            changed = true;
        } else if (behavior.equals("disconnect")) {
            statLabels[2] = ("Client " + name + " from " + ipaddress + " disconnected ");
            changed = true;
        }
        



    }

    @Override
    public void setCommandQueueInfo(int size) {
        statLabels[3] = "Press F1 for help  / Commands on Server =  " + size;
        changed = true;
    }
}
