/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.touch.visu.input.multitouch;

import PQSDKMultiTouch.PQMTClient;
import PQSDKMultiTouch.PQMTClientConstant;
import PQSDKMultiTouch.TouchClientRequest;
import PQSDKMultiTouch.TouchGesture;
import PQSDKMultiTouch.TouchPoint;
import com.itextpdf.text.log.SysoLogger;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.net.ConnectException;
import java.util.Vector;
import javax.swing.JOptionPane;
import kodemat.touch.visu.appstates.TouchCameraControllerAppState;
import kodemat.touch.visu.editor.TouchVisuEditor;
import kodemat.visu.editor.selection.JMESelectionHandler;
import kodemat.touch.visu.input.camera.controller.AbstractTouchHandlerCameraController;
import kodemat.visudata.visuComponents.VisuComponent;


/**
 *
 * @author OzgeA
 */
public class MultiTouchAdapter extends PQMTClient {

    //AbstractTouchHandlerCameraController TouchCamControl;
    AbstractTouchHandlerCameraController TouchCamControl;
    TouchVisuEditor touchVisuEditor;
    int param1, param2, param3, param4, dx, dy, dxy;
    int rotateangle = 0;
    float paralel_downX, paralel_downY, paralel_dx, paralel_dy;
    JMESelectionHandler SelectedObjectHandler;
    TouchVisuEditor TVisEditor;
    TouchCameraControllerAppState appstate;
    public MultiTouchAdapter() throws Exception {
        InitServer();
        TouchCamControl.getAbstractTouchHandlerCamC();

    }
    
      public MultiTouchAdapter(TouchVisuEditor TVisEditor, TouchCameraControllerAppState Camappstate) throws Exception{
        InitServer();
        this.TVisEditor=TVisEditor;
        this.appstate = Camappstate;
    }
    private int InitServer() throws Exception {
        int err_code = PQMTClientConstant.PQ_MT_SUCESS;
        try {
            if ((err_code = ConnectServer()) != PQMTClientConstant.PQ_MT_SUCESS) {
                JOptionPane.showMessageDialog(null, "connect server fail, socket error code:" + err_code);
                return err_code;
            }
        } catch (ConnectException ex) {
            JOptionPane.showMessageDialog(null, "Please Run the PQLabs MultiTouch Platform(Server) first.");
            return err_code;
        }

        TouchClientRequest clientRequest = new TouchClientRequest();
        clientRequest.app_id = PQMTClient.GetTrialAppID();

        try {
            clientRequest.type = PQMTClientConstant.RQST_RAWDATA_ALL | PQMTClientConstant.RQST_GESTURE_ALL;
            if ((err_code = SendRequest(clientRequest)) != PQMTClientConstant.PQ_MT_SUCESS) {
                JOptionPane.showMessageDialog(null, "send request  fail,  error code:" + err_code);
                return err_code;
            }
            if ((err_code = GetServerResolution()) != PQMTClientConstant.PQ_MT_SUCESS) {
                JOptionPane.showMessageDialog(null, "get server resolution fail,  error code:" + err_code);
                return err_code;
            }
            System.out.println("connected, start receive:" + err_code);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }



        return err_code;

    }

    @Override
    public int OnTouchFrame(int frame_id, int time_stamp, Vector<TouchPoint> point_list) {

        for (TouchPoint point : point_list) {
            String message = "Touch at " + point.m_x + " " + point.m_y + "with size" + point.m_dx + "*" + point.m_dy;
            System.out.println(message);
        }
        System.out.println("At on touch frame");

        //JOptionPane.showMessageDialog(null, "At OnTouchFrame method");
        return PQMTClientConstant.PQ_MT_SUCESS;
    }

    @Override
    public int OnTouchGesture(TouchGesture touch_gesture) {

        System.out.println("At on touch gesture");
       
        
        switch (touch_gesture.m_type) {

            case PQMTClientConstant.TG_DOWN:
                if (touch_gesture.m_params.get(0).intValue() != 0) {
                    System.out.println("Touch x: " + touch_gesture.m_params.get(0).intValue());
                    System.out.println("Touch y: " + touch_gesture.m_params.get(1).intValue());
                }
                param1 = touch_gesture.m_params.get(0).intValue();
                param2 = touch_gesture.m_params.get(1).intValue();
                
                if(TVisEditor.isEnableObjTouchMove()){// after gui selection it will be mapped to true
                  //  TVisEditor.setNumberClicks(2);
                  //  TVisEditor.setWaitForClick_Move(true);
                  //  TVisEditor.moveTo(); 
                    
                }

                
                break;
             
            case PQMTClientConstant.TG_MOVE:
                dx =  param1- touch_gesture.m_params.get(0).intValue();
                dy =  param2- touch_gesture.m_params.get(1).intValue();
                
                break;
            
            case PQMTClientConstant.TG_MULTI_MOVE:

                break;
                     
            case PQMTClientConstant.TG_MOVE_UP:
                //second drag click behaviour
                
                break;
            
            case PQMTClientConstant.TG_MULTI_MOVE_UP:
                //second drag click behaviour

                
                break;
                
            case PQMTClientConstant.TG_UP:

                break;
                
            
            case PQMTClientConstant.TG_SECOND_DOWN:

                break;
            case PQMTClientConstant.TG_SPLIT_START:
                param1 = touch_gesture.m_params.get(0).intValue();
                param2 = touch_gesture.m_params.get(1).intValue();
                param3 = touch_gesture.m_params.get(2).intValue();
                param4 = touch_gesture.m_params.get(3).intValue();
             
                System.out.println("zoom starts");
                break;
            case PQMTClientConstant.TG_SPLIT_APART:
                dx = param3 - touch_gesture.m_params.get(2).intValue();
                dy = param4 - touch_gesture.m_params.get(3).intValue();
                
                System.out.println("TVisEditor.getMarkerSelection() " + TVisEditor.getMarkerSelection());
                if(TVisEditor.getMarkerSelection() != null){
                   // TVisEditor.modelEditActionHandler.scale(dx*0.001f);
                }
                // 
                System.out.println("zoom in ");
                break;
            case PQMTClientConstant.TG_SPLIT_CLOSE:
                dx = param3 - touch_gesture.m_params.get(2).intValue();
                dy = param4 - touch_gesture.m_params.get(3).intValue();
                System.out.println("TVisEditor.getMarkerSelection() " + TVisEditor.getMarkerSelection());
                 if(TVisEditor.getMarkerSelection() != null){
                    //TVisEditor.modelEditActionHandler.scale(-dx*0.001f);
                }
                System.out.println("zoom out ");
                break;

            case PQMTClientConstant.TG_ROTATE_START:
                param1 = touch_gesture.m_params.get(0).intValue();
                param2 = touch_gesture.m_params.get(1).intValue();
                param3 = touch_gesture.m_params.get(2).intValue();
                param4 = touch_gesture.m_params.get(3).intValue();
                
                System.out.println("start touch_gesture.m_params.get(0).intValue(): " + touch_gesture.m_params.get(0).intValue());
                        System.out.println("start touch_gesture.m_params.get(0).intValue(): " + touch_gesture.m_params.get(1).intValue());
                        System.out.println("start touch_gesture.m_params.get(0).intValue(): " + touch_gesture.m_params.get(2).intValue());
                        System.out.println("start touch_gesture.m_params.get(0).intValue(): " + touch_gesture.m_params.get(3).intValue());

                System.out.println("rotate starts ");
            case PQMTClientConstant.TG_ROTATE_CLOCK:
                dx = param1 - touch_gesture.m_params.get(2).intValue();
                dy = param2 - touch_gesture.m_params.get(3).intValue();
               // appstate.getCameraControllerT().setRotateclockwise(true);
                if(TVisEditor.isEnableObjTouchRotate()){
                    //change later
                   // if(touch_gesture.m_params.get(0).intValue()!=0){
                        rotateangle -= 2; 
                        TVisEditor.rotate(rotateangle);
                        System.out.println("touch_gesture.m_params.get(0).intValue(): " + touch_gesture.m_params.get(0).intValue());
                        System.out.println("touch_gesture.m_params.get(0).intValue(): " + touch_gesture.m_params.get(1).intValue());
                        System.out.println("touch_gesture.m_params.get(0).intValue(): " + touch_gesture.m_params.get(2).intValue());
                        System.out.println("touch_gesture.m_params.get(0).intValue(): " + touch_gesture.m_params.get(3).intValue());
                        
                  //  }
                    
                }
                else {
                
            //    if(dx<0)
                    for(int i =0; i<10; i++)
                appstate.getCameraControllerT().rotateCamera(Vector3f.UNIT_Y, 0.001f);
            //    else
            //    appstate.getCameraControllerT().rotateCamera(Vector3f.UNIT_Y, dx*0.0001f);
                
                } 
                System.out.println("rotates clockwise ");
                break;
            case PQMTClientConstant.TG_ROTATE_ANTICLOCK:
                dx = param1 - touch_gesture.m_params.get(2).intValue();
                dy = param2 - touch_gesture.m_params.get(3).intValue();
                
                 if(TVisEditor.isEnableObjTouchRotate()){
                    //change later
                     //if(touch_gesture.m_params.get(0).intValue()!=0){
                         rotateangle += 2;
                                 //(0.002f*(touch_gesture.m_params.get(0).intValue()*57));
                         TVisEditor.rotate(rotateangle);
                  //   }
                    
                }
                else {
              //  if(dx<0)
                     for(int i =0; i<10; i++)
                appstate.getCameraControllerT().rotateCamera(Vector3f.UNIT_Y, -0.001f);
              //  else
              //  appstate.getCameraControllerT().rotateCamera(Vector3f.UNIT_Y, -dx*0.0001f);
                 }
                System.out.println("rotates anticlockwise ");
                break;
            case PQMTClientConstant.TG_NEAR_PARREL_DOWN:
                paralel_downX = touch_gesture.m_params.get(0).intValue();
                paralel_downY = touch_gesture.m_params.get(1).intValue();
                System.out.println("paralel_downX: " + paralel_downX);
                System.out.println("paralel_downY: " + paralel_downY);
                break;
            case PQMTClientConstant.TG_NEAR_PARREL_MOVE_UP:
                paralel_dy = paralel_downY -touch_gesture.m_params.get(1).intValue() ;
                
                System.out.println("move up y value: " + touch_gesture.m_params.get(1).intValue());
                System.out.println("move up difference dy: " + paralel_dy);
                appstate.getCameraControllerT().moveCameraAxisY(paralel_dy*0.001f);
                break;
            case PQMTClientConstant.TG_NEAR_PARREL_MOVE_DOWN:
                paralel_dy = paralel_downY - touch_gesture.m_params.get(1).intValue();
                
                System.out.println("move down y value: " + touch_gesture.m_params.get(1).intValue());
                System.out.println("move down difference dy: " + paralel_dy);
                appstate.getCameraControllerT().moveCameraAxisY(paralel_dy*0.001f);
                break;
            case PQMTClientConstant.TG_NEAR_PARREL_MOVE_LEFT:
                paralel_dx = touch_gesture.m_params.get(0).intValue() - paralel_downX;
                
                System.out.println("move left x value: " + touch_gesture.m_params.get(0).intValue());
                System.out.println("move left difference dx: " + paralel_dx);
                appstate.getCameraControllerT().moveCameraAxisX(paralel_dx*0.001f);
                break;
            case PQMTClientConstant.TG_NEAR_PARREL_MOVE_RIGHT:
                paralel_dx = touch_gesture.m_params.get(0).intValue() - paralel_downX;
                
                System.out.println("move right x value: " + touch_gesture.m_params.get(0).intValue());
                System.out.println("move right difference dx: " + paralel_dx);
                appstate.getCameraControllerT().moveCameraAxisX(paralel_dx*0.001f);
                break;
            default:
                break;


        }
        return PQMTClientConstant.PQ_MT_SUCESS;
    }

    public void setJmeSelectionHandler(JMESelectionHandler selectedObjectHandler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
