package net.elizeu.android.aerolitos.scenes;

import net.elizeu.android.aerolitos.Aerolitos;
import net.elizeu.android.aerolitos.scenes.items.Level;

import org.igs.android.ogl.engine.Keyboard;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.event.TouchAreaEventHandler;
import org.igs.android.ogl.engine.event.TouchEventHandler;
import org.igs.android.ogl.engine.gui.TouchMap;
import org.igs.android.ogl.engine.gui.TouchMapItem;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.particles.oje2d.ParticleSystemExplode;
import org.igs.android.ogl.engine.scene.Scene;
import org.igs.android.ogl.engine.scene.SceneObject.TransparencyEnum;
import org.igs.android.ogl.engine.sprite.TouchSprite;

import android.util.Log;
import android.view.MotionEvent;

public class AerolitosScene extends Scene implements TouchAreaEventHandler, TouchEventHandler {

	public static final Long ID = new Long(1L);

	//private Sprite sprite;

	private static final Long ligth0 = new Long(0L);

	private TouchSprite directional;
	
	public static final long D_UP = 1;
	public static final long D_DOWN = 2;
	public static final long D_LEFT = 3;
	public static final long D_RIGHT = 4;
	public static final long D_FIRE = 5;
	
	private Level level;
	
	private long keyCode = 0;
	
	private ParticleSystemExplode particleExplosion;
	
	public AerolitosScene(Renderer renderer) {
		super(renderer);
	}

	@Override
	public void enter() {
		super.addTouchEvent(this);
		try {
			//this.addLight(ligth0, new Light(this.getRenderer()));
			//sprite = new Sprite(this.getRenderer(), "textures/glass.bmp", 0, 0, this.getRenderer().getView().getWidth(), this.getRenderer().getView().getHeight());
			TouchMap touchMap = new TouchMap();
			touchMap.addTouchMapItem(new TouchMapItem(D_UP, 22, 0, 20, 20));
			touchMap.addTouchMapItem(new TouchMapItem(D_DOWN, 22, 44, 20, 20));
			touchMap.addTouchMapItem(new TouchMapItem(D_LEFT, 0, 22, 20, 20));
			touchMap.addTouchMapItem(new TouchMapItem(D_RIGHT, 44, 22, 20, 20));
			directional = new TouchSprite("", null, this, "direcional.bmp", "direcional.bmp", 0, 5, 0.64f, 0.64f, this, touchMap, new Point3f(0, 0, 0), new Point3f(0, 0, 0), new Point3f(1f, 1f, 1f), TransparencyEnum.NONE);
			directional.register();
			
			particleExplosion = new ParticleSystemExplode("", null, getRenderer(), "particles/basicblur.png", 20, 0.5f, 0.5f, new Point3f(1, 1, 0), new Point3f(0, 0, 0), new Point3f(1f, 1f, 1f), TransparencyEnum.NONE);
			particleExplosion.setRadius(0, 0.5f);
			particleExplosion.setSpeed(0.1f, 0.2f);
			particleExplosion.setTimeToLive(4000, 6000);			

			level = new Level(particleExplosion, this.getRenderer());
			level.init();
			
			//directional.setTransparency(true);
		} catch (Exception ex) {
			Log.e("AerolitosState.load.models/player.3ds", ex.getMessage());
		}
	}
	
	@Override
	public void leave() {
		directional.unRegister();
		this.destroy();
	}

	@Override
	public void render(float delta) {
		//sprite.render();
		//this.getRenderer().getGL10().glRotatef(-75, 1, 0, 0);
		level.render(delta);
		//directional.render(delta);
		particleExplosion.render(delta);
	}

	public static boolean isFirePressed() {
		return Keyboard.isKeyDown(Keyboard.KEYCODE_A) ? true : false;
	}
	
	@Override
	public void update(float delta, float fps) {
		if (Keyboard.isKeyDown(Keyboard.KEYCODE_0)) {
			((Aerolitos)this.getRenderer().getContext()).testeToast("sdfsdfsdf");
		}
		level.update(keyCode, delta);
	}

	@Override
	public void destroy() {
		this.removeLight(ligth0);
	}
	
	public boolean onTouchAreaDownEvent(long id, MotionEvent event) {
		this.keyCode = id;
		return false;
	}

	public boolean onTouchAreaLeaveEvent(long id, MotionEvent event) {
		this.keyCode = id;
		return false;
	}

	public boolean onTouchAreaUpEvent(long id, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	/* Handle general touch event */
	public boolean onTouchEvent(MotionEvent event) {
		keyCode = 0;
		return false;
	}

}
