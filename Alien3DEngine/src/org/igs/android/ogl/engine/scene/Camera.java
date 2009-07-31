package org.igs.android.ogl.engine.scene;

import org.igs.android.ogl.engine.Renderer;

public class Camera {

	public static final int CAMERA_BACK = 1;
	public static final int CAMERA_SIDE = 2;
	public static final int CAMERA_UP = 3;
	public static final int CAMERA_ORTHO = 4;
	public static final int CAMERA_TESTE = 5;
	public static final int CAMERA_RESET = 6;
	
	private Renderer renderer;
	private int cameraType;
	private SceneObject target;
	
	public Camera(Renderer renderer, int cameraType, SceneObject target) {
		this.renderer = renderer;
		this.cameraType = cameraType;
		this.target = target;
	}
	
	public void setTarget(SceneObject target) {
		this.target = target;
	}
	
	public SceneObject getTarget() {
		return this.target;
	}
	
	public int getCameraType() {
		return this.cameraType;
	}

	public void setCameraType(int cameraType) {
		this.cameraType = cameraType;
	}
	
	public void render() {
		/*
        if(this.getCameraType() == CAMERA_BACK) {
    		renderer.getGL10().glRotatef(30, 1, 0, 0); //  Vista por traz/cima da nave 30 graus de inclinacao 
        }
        if(this.getCameraType()==CAMERA_SIDE) {
        	renderer.getGL10().glRotatef( -90, 0, 1, 0);// Vista lateral tipo Plataforma 2D
        }
        if(this.getCameraType()==CAMERA_UP) {
        	renderer.getGL10().glRotatef(90, 1, 0, 0); //  Vista por cima da nave
        }
        if(this.getCameraType()==CAMERA_ORTHO) {
        	renderer.getGL10().glRotatef(20, 1, 0, 0); //
        	renderer.getGL10().glRotatef( -30, 0, 1, 0); // 
        }
        if(this.getCameraType()==CAMERA_TESTE) {
        	renderer.getGL10().glRotatef(90, 1, 0, 0); //  Vista por cima da nave
        }

        if(this.getCameraType()==CAMERA_RESET) {
        	renderer.getGL10().glRotatef(0, 1, 1, 1); //  Vista por cima da nave
        }
		*/
		//if (this.cameraType == CameraTypeEnum.PLATAFORM) {
			renderer.getGL10().glTranslatef(-target.getPosition().x, -target.getPosition().y, 5);
		//}
		
	}
	
}
