package org.igs.android.ogl.engine.sprite;

import java.util.Enumeration;
import java.util.Hashtable;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject;

import android.util.Log;

public class AnimatedSprite extends SceneObject {

	private SpriteSheet spriteSheet;

	private Hashtable<String, Animation> animations;
	
	private Animation currentAnimation;
	
	private boolean autoUpdate;
	private double autoUpdateInterval;
	private double lastUpdateInterval;
	
	private boolean stoped;
	
	public AnimatedSprite(String id, SceneObject parent, Renderer renderer, String fileName, float width, float height, int frameWidth, int frameHeight, int spacing, Point3f position, Point3f rotation, Point3f scale, TransparencyEnum transparencyType) throws AndromedaException {
		super(id, parent, renderer, position, rotation, scale);
		spriteSheet = new SpriteSheet("", this, renderer, fileName, width, height, frameWidth, frameHeight, spacing, new Point3f(position), new Point3f(rotation), new Point3f(scale), transparencyType);
		animations = new Hashtable<String, Animation>(1);
		autoUpdate = false;
		lastUpdateInterval = 0;
	}
	
	public void addAnimation(String name, int startFrame, int stopFrame) throws AndromedaException {
		animations.put(name, new Animation(this.spriteSheet, startFrame, stopFrame));
	}

	public void setCurrentAnimation(String name) throws AndromedaException {
		if (this.animations.containsKey(name)) {
			currentAnimation = this.animations.get(name);
		} else {
			throw new AndromedaException(1, "Animation name '"+name+"' dont exists.");
		}
	}
	
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}
	
	public void setAutoUpdateInterval(float autoUpdateInterval) {
		this.autoUpdateInterval = autoUpdateInterval;
	}
	
	/**
	 * Play a entire animation
	 */
	public void play() {
		this.currentAnimation.play();
	}
	
	/**
	 * Start animation render
	 */
	public void start() {
		this.stoped = false;
	}

	/**
	 * Stop animation render
	 */
	public void stop() {
		this.stoped = true;
	}
	
	public boolean isStoped() {
		return this.stoped;
	}
	
	public boolean isAutoUpdate() {
		return this.autoUpdate;
	}
	
	/**
	 * Set current animation in first frame
	 */
	public void reset() {
		this.currentAnimation.reset();
	}

	/**
	 * Set all animations in first frame
	 */
	public void resetAll() {
		Enumeration<Animation> animEnum = this.animations.elements();
		while (animEnum.hasMoreElements()) {
			animEnum.nextElement().reset();
		}
	}

	public String getFileName() {
		return this.spriteSheet.getFileName();
	}
	
	public float getWidth() {
		return this.spriteSheet.getWidth();
	}
	
	public void setWidth(float width) {
		this.spriteSheet.setWidth(width);
	}

	public float getHeight() {
		return this.spriteSheet.getHeight();
	}

	public void setHeight(float height) {
		this.spriteSheet.setHeight(height);
	}
	
	public int getSpacing() {
		return this.spriteSheet.getSpacing();
	}
	
	private void redefine() {
		this.spriteSheet.setPosition(getPosition().x, getPosition().y, getPosition().z);
		this.spriteSheet.setRotation(getRotation().x, getRotation().y, getRotation().z);
		this.spriteSheet.setScale(getScale().x, getScale().y, getScale().z);
		//this.spriteSheet.setWidth(width);
		//this.spriteSheet.setHeight(height);
		//this.spriteSheet.setTransparencyType(transparencyType);
	}

	private void verifyChanges() {
		if (this.isModified()) {
			this.redefine();
			this.setModified(false);
		}
	}
	
	public void render(float delta) {
		try {
			this.verifyChanges();
			if (! stoped) {
				if (this.autoUpdate || this.currentAnimation.isPlaying()) {
					this.currentAnimation.renderCurrentFrame(delta);
					if (this.lastUpdateInterval == 0) {
						this.lastUpdateInterval = System.currentTimeMillis();
						this.currentAnimation.renderNextFrame(delta);
					} else {
						if (this.autoUpdateInterval <= System.currentTimeMillis() - this.lastUpdateInterval) {
							this.currentAnimation.renderNextFrame(delta);
							this.lastUpdateInterval = System.currentTimeMillis();
						}
					}
				} else {
					this.currentAnimation.renderCurrentFrame(delta);
				}
			}
		} catch (AndromedaException ex) {
			Log.e("AnimatedSprite", ex.getMessage());
		}
	}
	
	public void update(float delta) {
		
	}
	
	private class Animation {
		
		private int start;
		private int stop;
		
		private int nextFrame;
		private boolean playing;
		
		private SpriteSheet spriteSheet;
		
		Animation(SpriteSheet spriteSheet, int start, int stop) throws AndromedaException {
			this.start = start;
			this.stop = stop;
			this.spriteSheet = spriteSheet;
			playing = false;
			this.reset();
		}
		
		void play() {
			this.playing = true;
		}
		
		boolean isPlaying() {
			return this.playing;
		}
		
		void reset() {
			nextFrame = this.start - 1;
			this.playing = false;
		}
		
		void gotoNextFrame() {
			nextFrame++;
			if (nextFrame == this.stop) {
				this.reset();
			}
		}
		
		void renderNextFrame(float delta) throws AndromedaException {
			this.spriteSheet.renderFrame(this.nextFrame, delta);
			this.gotoNextFrame();
		}

		void renderCurrentFrame(float delta) throws AndromedaException {
			this.spriteSheet.renderFrame(this.nextFrame, delta);
		}
		
	}
	
}
