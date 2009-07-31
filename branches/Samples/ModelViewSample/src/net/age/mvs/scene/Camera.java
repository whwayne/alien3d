package net.age.mvs.scene;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject;

import android.opengl.GLU;

public class Camera {

	public Point3f eye;
	public SceneObject target;
	public Point3f roll;
	
	public Camera(SceneObject target) {
		this.eye = new Point3f(2, 2, 2);
		this.roll = new Point3f();
		this.target = target;
	}
	
	public void render(GL10 gl) {
        GLU.gluLookAt(
        		gl,
                eye.x,
                eye.y,
                eye.z,
                target.getPosition().x,
                target.getPosition().y,
                target.getPosition().z,
                roll.x,roll.y,roll.z);
	}
	
}
