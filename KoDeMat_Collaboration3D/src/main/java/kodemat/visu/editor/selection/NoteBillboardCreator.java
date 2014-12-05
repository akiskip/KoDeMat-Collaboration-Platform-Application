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
package kodemat.visu.editor.selection;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.ui.Picture;
import java.util.ArrayList;
import kodemat.visu.input.camera.controller.KeepSizeControl;

/**
 *
 * @author Koshkabb
 */
public class NoteBillboardCreator {
    
    public static Node createNote(AssetManager assetManager, String noteText) {
        ArrayList<String> lines = cutLines(noteText);
        BillboardControl bc = new BillboardControl();
        bc.setAlignment(BillboardControl.Alignment.Screen);
        
//        KeepSizeControl ksc = new KeepSizeControl();
        
        Node holder = new Node("Note");
        
        String noteImage = "Others/note.png";
        
        Picture note = new Picture("");
        note.setImage(assetManager, noteImage, true);
        
        float height = 3;
        if (lines.size() > height * 2) {
//            height = lines.size() / 1.8f;
            height = lines.size() / 1.8f;
        }
        note.scale(6, height, 1); //the height is approx twice the number of lines


        note.setIgnoreTransform(false);
        note.setQueueBucket(RenderQueue.Bucket.Transparent);
        note.setPosition(0, 0);
        holder.attachChild(note);
        /*    Material mat = bubble.getMaterial();
         mat.getAdditionalRenderState().setPolyOffset(-1.0f,-1.0f);
         bubble.setMaterial(mat);
         */
        float trans = 0 - lines.size() * 0.3f;
        if (lines.size() == 1) {
            trans = 2;
        }
        float imagePos = -height / 2;
        note.setLocalTranslation(-3.5f, imagePos, -.05f);
        BitmapFont fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
        
        float mult, initial = imagePos + height - 0.8f;
        
        for (int i = 0; i < lines.size(); i++) {
            BitmapText label = new BitmapText(fnt, false);
            label.setSize(.45f);
            label.setText(lines.get(i));
            
            float textWidth = label.getLineWidth();
            float textOffset = textWidth / 2;
            //   label.setBox(new Rectangle(-textOffset, 0, textWidth, label.getHeight()));
            label.setColor(ColorRGBA.Black);
            
            label.setAlignment(BitmapFont.Align.Left);
//            label.setAlignment(BitmapFont.Align.Center);
            label.setQueueBucket(RenderQueue.Bucket.Translucent);
            
            label.setLocalTranslation(-2.95f, initial - i * 0.4f, 0);
            
            
            holder.attachChild(label);
        }

//        holder.scale(1f,2f,1f);

        
        
        
        holder.addControl(bc);
        
//        holder.addControl(ksc);
        holder.setLocalTranslation(0, 4.5f+lines.size()/6f, 0);
        holder.scale(2, 2, 1);
        return holder;
    }
    
    private static ArrayList<String> cutLines(String msg) {
        boolean done = false;
        ArrayList<String> result = new ArrayList<String>();
        String rest = msg;
        int lim = 20;
        while (!done) {
            if (rest.length() >= lim + 1) {
                if (rest.charAt(lim) == ' ') {
                    result.add(rest.substring(0, lim + 1));
                    rest = rest.substring(lim + 2);
                } else {
                    boolean found = false;
                    int j = lim;
                    while (!found && j >= lim / 2) {
                        if (rest.charAt(j) == ' ') {
                            found = true;
                        } else {
                            j--;
                        }
                    }
                    if (found) {
                        result.add(rest.substring(0, j));
                        rest = rest.substring(j + 1);
                    } else {
                        result.add(rest.substring(0, lim) + "-");
                        rest = rest.substring(lim);
                    }
                }
            } else {
                result.add(rest);
                rest = "";
                done = true;
            }
        }
        return result;
    }
}
