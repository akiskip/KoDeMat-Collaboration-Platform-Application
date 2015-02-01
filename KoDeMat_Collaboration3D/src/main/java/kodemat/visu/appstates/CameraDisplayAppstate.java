
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kodemat.visu.appstates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.Camera;
import kodemat.visudata.VisuHelper;
import kodemat.visudata.VisuType;
import kodemat.visudata.VisuVector3f;
import kodemat.visudata.visuComponents.VisuComponent;

/**
 *
 * @author Institute fml (TU Munich) and Institute FLW (TU Dortmund)
 */
public class CameraDisplayAppstate extends AbstractAppState  {
    VisuHelper helper;
    private Camera cam;
    private final String userId;
    private VisuComponent cameraNode;
    private float timeCount  = 0l;
    private float time =1f;



    public CameraDisplayAppstate(VisuHelper helper,String userID) {
        this.helper = helper;
        userId =userID;
  createCamera(userId);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
    
      cam = app.getCamera();
               
    }

    @Override
    public void update(float tpf) {
   
     timeCount =timeCount+ tpf;

if(timeCount>=time){
     VisuVector3f camPos = new VisuVector3f(cam.getLocation().x,cam.getLocation().y,cam.getLocation().z);
//       helper.getComponent("camera_"+ userId).setTranslation(camPos);
      System.out.println("Camera pos x "+camPos.x+"y "+camPos.y);
       timeCount= 0f;
}
    }
    private void createCamera(String userId) {

     cameraNode = helper.createComponent("camera_"+ userId);
            cameraNode.setType(new VisuType(VisuType.MODEL, "CeMAT_Assets/Pallete/palette.j3o"));
            cameraNode.setTranslation(new VisuVector3f(0f,0f,0f));
    }
}
