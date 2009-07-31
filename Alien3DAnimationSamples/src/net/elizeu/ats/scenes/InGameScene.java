package net.elizeu.ats.scenes;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.Keyboard;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.map.tiled.Tile;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.Camera;
import org.igs.android.ogl.engine.scene.Scene;
import org.igs.android.ogl.engine.scene.SceneObject;
import org.igs.android.ogl.engine.scene.SceneObject.TransparencyEnum;
import org.igs.android.ogl.engine.sprite.AnimatedSprite;

import android.util.Log;

/**
 * 
 * @author Elizeu Nogueira da Rosa Jr.
 * @version 0.1
 * @since 20.05.2009
 *
 */
public class InGameScene extends Scene {

	public static final Long ID = new Long(1L);
	
	enum ANIMATION {
		DOWN,
		UP,
		RIGH,
		LEFT
	}
	private ANIMATION currentAnimation;
	
	private MyMap map;
	
	/* Animated Sprites */
	private AnimatedSprite lady;
	
	/*
	private AnimatedSprite campFire;
	private AnimatedSprite fire;
	*/
	
	private Camera camera;
	
	public InGameScene(Renderer renderer) {
		super(renderer);
	}

	@Override
	public void enter() {
		try {
			map = new MyMap(getRenderer(), "maps/firstmap.tmx");
			map.initialize();
			
			lady = new AnimatedSprite("lady", null, getRenderer(), "tiles/lady/lady_normal.png", 0.32f, 0.48f, 32, 48, 0, new Point3f(2.5f, -2.5f, 0), new Point3f(180, 0, 0), new Point3f(1, 1, 1), TransparencyEnum.ALPHA);
			
			lady.addAnimation("down", 1, 4);
			lady.addAnimation("up", 13, 16);
			lady.addAnimation("left", 5, 8);
			lady.addAnimation("right", 9, 12);
			
			
			
			lady.setAutoUpdate(false);
			lady.setAutoUpdateInterval(100f);
			lady.setCurrentAnimation("down");
			lady.start();
			
			camera = new Camera(getRenderer(), Camera.CAMERA_UP, lady);
			map.getLayerList().get(1).addEntity(lady);
			
			/*
			campFire = new AnimatedSprite("campfire", null, getRenderer(), "tiles/campfire1.png", 0.34f, 0.38f, 34, 38, 0, new Point3f(1, -1, 0), new Point3f(180, 0, 0), new Point3f(1, 1, 1), TransparencyEnum.NONE);
			campFire.addAnimation("running", 1, 16);

			campFire.setAutoUpdate(true);
			campFire.setAutoUpdateInterval(100f);
			campFire.setCurrentAnimation("running");
			campFire.start();
			
			fire = new AnimatedSprite("fire", null, getRenderer(), "tiles/fire.png", 0.32f, 0.64f, 32, 64, 0, new Point3f(-2, 1, 0), new Point3f(180, 0, 0), new Point3f(1, 1, 1), TransparencyEnum.ALPHA);
			fire.addAnimation("running", 1, 8);

			fire.setAutoUpdate(true);
			fire.setAutoUpdateInterval(100f);
			fire.setCurrentAnimation("running");
			fire.start();
			*/
			
			map.moveMapX(1);
			map.moveMapY(0);
		} catch (Exception ex) {
			Log.e("ModelLoader: ", ex.getMessage());
		}
	}

	@Override
	public void render(float delta) {
		
		
		for (SceneObject sceneObject : this.getSceneObjectList()) {
			sceneObject.render(delta);
		}
		
		getRenderer().getGL10().glScalef(2f, 2f, 2f);
		camera.render();
		map.render(delta);
		//campFire.render(delta);
		//fire.render(delta);
		//lady.render(delta);
	}

	@Override
	public void update(float delta, float fps) {
		try {
			//Log.d("FPS", Float.toString(fps));
			this.lady.setAutoUpdate(false);

			if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_DOWN)) {
				if (currentAnimation != ANIMATION.DOWN) {
					this.lady.setCurrentAnimation("down");
					this.currentAnimation = ANIMATION.DOWN;
				}
				Tile tile = this.map.getLayerList().get(1).findTileAsXY(this.lady.getPosition().x, lady.getPosition().y  + -0.005f * (delta / 5));
				if (tile == null) {
					this.lady.addTranslation(0, -0.005f * (delta / 5), 0);
					map.moveMapY(-0.005f * (delta / 5));
					this.lady.setAutoUpdate(true);
				}
			} else if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_UP)) {
				if (currentAnimation != ANIMATION.UP) {
					this.lady.setCurrentAnimation("up");
					this.currentAnimation = ANIMATION.UP;
				}
				Tile tile = this.map.getLayerList().get(1).findTileAsXY(this.lady.getPosition().x, lady.getPosition().y  + 0.005f * (delta / 5));
				if (tile == null) {
					this.lady.addTranslation(0, 0.005f * (delta / 5), 0);
					map.moveMapY(0.005f * (delta / 5));
					this.lady.setAutoUpdate(true);
				}
			} else if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_LEFT)) {
				if (currentAnimation != ANIMATION.LEFT) {
					this.lady.setCurrentAnimation("left");
					this.currentAnimation = ANIMATION.LEFT;
				}
				Tile tile = this.map.getLayerList().get(1).findTileAsXY(this.lady.getPosition().x + -0.005f * (delta / 5), lady.getPosition().y);
				if (tile == null) {
					this.lady.addTranslation(-0.005f * (delta / 5), 0, 0);
					map.moveMapX(-0.005f * (delta / 5));
					this.lady.setAutoUpdate(true);
				}
			} else if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_RIGHT)) {
				if (currentAnimation != ANIMATION.RIGH) {
					this.lady.setCurrentAnimation("right");
					this.currentAnimation = ANIMATION.RIGH;
				}
				Tile tile = this.map.getLayerList().get(1).findTileAsXY(this.lady.getPosition().x + 0.005f * (delta / 5), lady.getPosition().y);
				if (tile == null) {
					this.lady.addTranslation(0.005f * (delta / 5), 0, 0);
					map.moveMapX(0.005f * (delta / 5));
					this.lady.setAutoUpdate(true);
				}
			}
		} catch (AndromedaException ex) {
			Log.e("ModelViewScene", ex.getMessage());
		}
	}

	@Override
	public void leave() {
		
	}
	
	@Override
	public void destroy() {
		
	}
	
}
