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

import Interfaces.LayoutClientImpl;
import kodemat.visudata.visuComponents.VisuComponent;
import kodemat.visudata.VisuEdge;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuInterpolation;
import kodemat.visudata.VisuRotation;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
public class TestWithEdges extends LayoutClientImpl {

    /*
     * Haselcast Client
     */
    private VisuComponent box;
    VisuComponent kurve;
    VisuComponent gurtetrei;
    VisuComponent gurtetrei1;
    
    private VisuComponent codeaTestNode;
    private VisuComponent codeaTestModel;

    public TestWithEdges() {
        //conf file for haselcast

        super();

    }

    /**
     * 2
     *
     * @param count
     */
    public void startTest(int count) {
        
        codeaTestNode = this.createNode("codeaTestNode");
        setNodeTranslation(codeaTestNode, new VisuVector3f(18f, 0.0f, 8f));
        
        codeaTestModel = this.createModel("codeaTestModel", "Models/flw/codeatest/halle_lowpoly.obj;Models/flw/codeatest/halle_lowpoly_ao.jpg");
//        codeaTestModel = this.createModel("codeaTestModel", "Models/flw/codeatest/test.obj;Models/flw/codeatest/texture.jpg");
        this.addChildNode(codeaTestModel.getId(), codeaTestNode.getId());
        this.setNodeTranslation(codeaTestModel, new VisuVector3f(0f, 0f, 0f));
        this.setNodeRotation(codeaTestModel, new VisuRotation(0, 0, 0));
        this.setNodeScale(codeaTestModel, new VisuVector3f(0.006f, 0.006f, 0.006f));

        float xoffset = 13f;
        float yoffset = 0.6f;
        float zoffset = 12;
        
        int counter;

//        counter = 1;
//        VisuComponent tp = helper.getComponent("TP" + counter);
//        if (tp == null) {
//            tp = createLoad("TP" + counter, new VisuVector3f(xoffset + 0f, 0.15f, zoffset + 0f));
//        }

        VisuVector3f translation;
        
        counter = 1;
        translation = new VisuVector3f(xoffset + 0f, yoffset, zoffset + 0f);
        VisuComponent tp1 = helper.getComponent("TP" + counter);
        if (tp1 == null) {
            tp1 = createLoad("TP" + counter, translation);
        }
        else {
            tp1.setTranslation(translation);
        }
        counter = 2;
//        translation = new VisuVector3f(xoffset + 3f, yoffset, zoffset + 0f);
        translation = new VisuVector3f(xoffset + 3.6f, yoffset, zoffset + 0f);
        VisuComponent tp2 = helper.getComponent("TP" + counter);
        if (tp2 == null) {
            tp2 = createLoad("TP" + counter, translation);
        }
        else {
            tp2.setTranslation(translation);
        }
        counter = 3;
        translation = new VisuVector3f(xoffset + 5f, yoffset, zoffset + 0f);
        VisuComponent tp3 = helper.getComponent("TP" + counter);
        if (tp3 == null) {
            tp3 = createLoad("TP" + counter, translation);
        }
        else {
            tp3.setTranslation(translation);
        }
        counter = 4;
        translation = new VisuVector3f(xoffset + 10f, yoffset, zoffset + 0f);
        VisuComponent tp4 = helper.getComponent("TP" + counter);
        if (tp4 == null) {
            tp4 = createLoad("TP" + counter, translation);
        }
        else {
            tp4.setTranslation(translation);
        }
        counter = 5;
        translation = new VisuVector3f(xoffset + 11.5f, yoffset, zoffset + -1.5f);
        VisuComponent tp5 = helper.getComponent("TP" + counter);
        if (tp5 == null) {
            tp5 = createLoad("TP" + counter, translation);
        }
        else {
            tp5.setTranslation(translation);
        }
        counter = 6;
        translation = new VisuVector3f(xoffset + 11.5f, yoffset, zoffset + -6.5f);
        VisuComponent tp6 = helper.getComponent("TP" + counter);
        if (tp6 == null) {
            tp6 = createLoad("TP" + counter, translation);
        }
        else {
            tp6.setTranslation(translation);
        }
        counter = 7;
        translation = new VisuVector3f(xoffset + 10f, yoffset, zoffset + -8f);
        VisuComponent tp7 = helper.getComponent("TP" + counter);
        if (tp7 == null) {
            tp7 = createLoad("TP" + counter, translation);
        }
        else {
            tp7.setTranslation(translation);
        }
        counter = 8;
        translation = new VisuVector3f(xoffset + 5f, yoffset, zoffset + -8f);
        VisuComponent tp8 = helper.getComponent("TP" + counter);
        if (tp8 == null) {
            tp8 = createLoad("TP" + counter, translation);
        }
        else {
            tp8.setTranslation(translation);
        }
        counter = 9;
        translation = new VisuVector3f(xoffset + 3f, yoffset, zoffset + -8f);
//        translation = new VisuVector3f(xoffset + 6f, yoffset, zoffset + -8f);
        VisuComponent tp9 = helper.getComponent("TP" + counter);
        if (tp9 == null) {
            tp9 = createLoad("TP" + counter, translation);
        }
        else {
            tp9.setTranslation(translation);
        }
        counter = 10;
        translation = new VisuVector3f(xoffset + 0f, yoffset, zoffset + -8f);
        VisuComponent tp10 = helper.getComponent("TP" + counter);
        if (tp10 == null) {
            tp10 = createLoad("TP" + counter, translation);
        }
        else {
            tp10.setTranslation(translation);
        }
        counter = 11;
        translation = new VisuVector3f(xoffset + -1.5f, yoffset, zoffset + -6.5f);
        VisuComponent tp11 = helper.getComponent("TP" + counter);
        if (tp11 == null) {
            tp11 = createLoad("TP" + counter, translation);
        }
        else {
            tp11.setTranslation(translation);
        }
        counter = 12;
        translation = new VisuVector3f(xoffset + -1.5f, yoffset, zoffset + -1.5f);
        VisuComponent tp12 = helper.getComponent("TP" + counter);
        if (tp12 == null) {
            tp12 = createLoad("TP" + counter, translation);
        }
        else {
            tp12.setTranslation(translation);
        }
        counter = 13;
//        translation = new VisuVector3f(xoffset + 3f, yoffset, zoffset + 1f);
        translation = new VisuVector3f(xoffset + 2f, yoffset, zoffset + 2f);
        VisuComponent tp13 = helper.getComponent("TP" + counter);
        if (tp13 == null) {
            tp13 = createLoad("TP" + counter, translation);
        }
        else {
            tp13.setTranslation(translation);
        }
        counter = 14;
//        translation = new VisuVector3f(xoffset + 6f, yoffset, zoffset + -1f);
        translation = new VisuVector3f(xoffset + 6.7f, yoffset, zoffset + -2f);
        VisuComponent tp14 = helper.getComponent("TP" + counter);
        if (tp14 == null) {
            tp14 = createLoad("TP" + counter, translation);
        }
        else {
            tp14.setTranslation(translation);
        }
        counter = 15;
//        translation = new VisuVector3f(xoffset + 6f, yoffset, zoffset + -7f);
        translation = new VisuVector3f(xoffset + 6.9f, yoffset, zoffset + -6f);
        VisuComponent tp15 = helper.getComponent("TP" + counter);
        if (tp15 == null) {
            tp15 = createLoad("TP" + counter, translation);
        }
        else {
            tp15.setTranslation(translation);
        }
        counter = 16;
        translation = new VisuVector3f(xoffset + 2f, yoffset, zoffset + -9f);
        VisuComponent tp16 = helper.getComponent("TP" + counter);
        if (tp16 == null) {
            tp16 = createLoad("TP" + counter, translation);
        }
        else {
            tp16.setTranslation(translation);
        }

//        counter = 1;
//        VisuEdge e = helper.getEdge("Module_" + counter);
//        if (e == null) {
//            e = helper.createEdge("Module_" + counter);
//            e.setSource();
//            e.setTarget();
//        }
//        
        counter = 1;
        VisuEdge e1 = helper.getEdge("Module_" + counter);
        if (e1 == null) {
            e1 = helper.createEdge("Module_" + counter);
            e1.setSource(tp1.getId());
            e1.setTarget(tp2.getId());
        }

        counter = 2;
        VisuEdge e2 = helper.getEdge("Module_" + counter);
        if (e2 == null) {
            e2 = helper.createEdge("Module_" + counter);
            e2.setSource(tp2.getId());
            e2.setTarget(tp3.getId());
        }
        counter = 3;
        VisuEdge e3 = helper.getEdge("Module_" + counter);
        if (e3 == null) {
            e3 = helper.createEdge("Module_" + counter);
            e3.setSource(tp3.getId());
            e3.setTarget(tp4.getId());
        }
        counter = 4;
        VisuEdge e4 = helper.getEdge("Module_" + counter);
        if (e4 == null) {
            e4 = helper.createEdge("Module_" + counter);
            e4.setSource(tp4.getId());
            e4.setTarget(tp5.getId());
        }
        counter = 5;
        VisuEdge e5 = helper.getEdge("Module_" + counter);
        if (e5 == null) {
            e5 = helper.createEdge("Module_" + counter);
            e5.setSource(tp5.getId());
            e5.setTarget(tp6.getId());
        }
        counter = 6;
        VisuEdge e6 = helper.getEdge("Module_" + counter);
        if (e6 == null) {
            e6 = helper.createEdge("Module_" + counter);
            e6.setSource(tp6.getId());
            e6.setTarget(tp7.getId());
        }
        counter = 7;
        VisuEdge e7 = helper.getEdge("Module_" + counter);
        if (e7 == null) {
            e7 = helper.createEdge("Module_" + counter);
            e7.setSource(tp7.getId());
            e7.setTarget(tp8.getId());
        }
        counter = 8;
        VisuEdge e8 = helper.getEdge("Module_" + counter);
        if (e8 == null) {
            e8 = helper.createEdge("Module_" + counter);
            e8.setSource(tp8.getId());
            e8.setTarget(tp9.getId());
        }
        counter = 9;
        VisuEdge e9 = helper.getEdge("Module_" + counter);
        if (e9 == null) {
            e9 = helper.createEdge("Module_" + counter);
            e9.setSource(tp9.getId());
            e9.setTarget(tp10.getId());
        }
        counter = 10;
        VisuEdge e10 = helper.getEdge("Module_" + counter);
        if (e10 == null) {
            e10 = helper.createEdge("Module_" + counter);
            e10.setSource(tp10.getId());
            e10.setTarget(tp11.getId());
        }
         counter = 11;
        VisuEdge e11 = helper.getEdge("Module_" + counter);
        if (e11 == null) {
            e11 = helper.createEdge("Module_" + counter);
            e11.setSource(tp11.getId());
            e11.setTarget(tp12.getId());
        }
         counter = 12;
        VisuEdge e12 = helper.getEdge("Module_" + counter);
        if (e12 == null) {
            e12 = helper.createEdge("Module_" + counter);
            e12.setSource(tp12.getId());
            e12.setTarget(tp1.getId());
        }
        counter = 13;
        VisuEdge e13 = helper.getEdge("Module_" + counter);
        if (e13 == null) {
            e13 = helper.createEdge("Module_" + counter);
            e13.setSource(tp13.getId());
            e13.setTarget(tp2.getId());
        }
         counter = 14;
        VisuEdge e14 = helper.getEdge("Module_" + counter);
        if (e14 == null) {
            e14 = helper.createEdge("Module_" + counter);
            e14.setSource(tp3.getId());
            e14.setTarget(tp14.getId());
        }
         counter = 15;
        VisuEdge e15 = helper.getEdge("Module_" + counter);
        if (e15 == null) {
            e15 = helper.createEdge("Module_" + counter);
            e15.setSource(tp14.getId());
            e15.setTarget(tp15.getId());
        }
         counter = 16;
        VisuEdge e16 = helper.getEdge("Module_" + counter);
        if (e16 == null) {
            e16 = helper.createEdge("Module_" + counter);
            e16.setSource(tp15.getId());
            e16.setTarget(tp8.getId());
        }
         counter = 17;
        VisuEdge e17 = helper.getEdge("Module_" + counter);
        if (e17 == null) {
            e17 = helper.createEdge("Module_" + counter);
            e17.setSource(tp9.getId());
            e17.setTarget(tp16.getId());
        }
        
        
    }



    public static void main(String[] args) {
        TestWithEdges tc = new TestWithEdges();

        for (int i = 0; i < 1; i++) {
            tc.startTest(i);
        }
        tc.shutdown();
    }

    @Override
    public void addConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {

        sleep(700);



        box.setTranslation(new VisuVector3f(0, 2.2f, -1));
        sleep(700);
        box.setTranslation(new VisuVector3f(0, 2.2f, -2f));
        sleep(700);
        box.changeToWorldPosition();
        box.setInterpolation(new VisuInterpolation(false, false, false));


    }

    private VisuComponent createLoad(String name, VisuVector3f position) {
        VisuHelper visuHelper = helper;

        //VisuComponent parent = visuHelper.getComponent("Floor");

        Long boxId = visuHelper.getIdGenerator().newId();
        Long nodeId = visuHelper.getIdGenerator().newId();

        VisuComponent node = visuHelper.getComponent("Node for Box " + boxId);
        if (node == null) {
            node = visuHelper.createComponent(nodeId, "Node for Box " + boxId);
            node.setType(new VisuType(VisuType.NODE, null));
        }


        node.setTranslation(position);
        node.setRotation(new VisuRotation(0, 0, 0));
        node.setScale(new VisuVector3f(1f, 1f, 1f));
        node.setInterpolation(new VisuInterpolation(true, true, true));
        //node.setParent(parent.getId());


        VisuComponent box = visuHelper.getComponent(name);
        if (box == null) {
            box = visuHelper.createComponent(name);
            box.setType(new VisuType(VisuType.BOX, null));
        }

        box.setRotation(new VisuRotation(0, 0, 0));
        box.setInterpolation(new VisuInterpolation(true, true, true));
        box.setScale(new VisuVector3f(.2f, .2f, .2f));
        box.setTranslation(new VisuVector3f(0, 0, 0));

        box.setParent(node.getId());

        return box;
    }
}
