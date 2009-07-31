package net.elizeu.ass.scenes;

import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.math.Point4f;
import org.igs.android.ogl.engine.scene.Light;
import org.igs.android.ogl.engine.scene.Scene;
import org.igs.android.ogl.engine.shape.Sphere;
import org.igs.android.ogl.engine.shape.Torus;

public class InGameScene extends Scene {

	public static final Long ID = new Long(3L);
	
	private float mAngle;

	private Sphere earth;
	private Sphere moon;
	
	private Torus torus;
	
	public InGameScene(Renderer renderer) {
		super(renderer);
	}

	@Override
	public void render(float delta) {
		earth.render(delta);
		torus.render(delta);
	}

	@Override
	public void update(float delta, float fps) {
		earth.addRotation(mAngle, 0, 0);
		moon.addRotation(mAngle * 2.0f, mAngle * 2.0f, 0);
		moon.setPosition(0.5f, 0.5f, 0.5f);
		mAngle += 1.2f;
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enter() {
		earth = new Sphere("", null, this.getRenderer(), 0.5f, 15, 15, new Point3f(0, 0, 0), new Point3f(0, 0, 0), new Point3f(0.5f, 0.5f, 0.5f));
		moon = new Sphere("", null, this.getRenderer(), 0.3f, 15, 15, new Point3f(1, 1, 0), new Point3f(0, 0, 0), new Point3f(1f, 1f, 1f));
		//earth.addChild(moon);
	
		torus = new Torus("", null, getRenderer(), 1, 3, 8, 20,  new Point3f(0f, 0f, 0), new Point3f(0, 0, 0), new Point3f(0.5f, 0.5f, 0.5f));
		
		Light light1 = new Light(this.getRenderer());
		light1.setAmbient(new Point4f(0.2f, 0.2f, 0.2f, 1.0f));
		light1.setPos(new Point4f(0.0f, 5.0f, 10.0f, 1.0f));
		light1.setDiffuse(new Point4f(0.3f, 0.3f, 0.3f, 1.0f));
		light1.setSpecular(new Point4f(0.8f, 0.8f, 0.8f, 1.0f));
		this.addLight(1L, light1);
		
	}

	@Override
	public void leave() {
		
	}

}
