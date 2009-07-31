package org.alien3d.sprite;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;

import org.alien3d.Alien3DException;
import org.alien3d.World;
import org.alien3d.entity.Renderable;
import org.alien3d.entity.Transformable;

import android.util.Log;

public class AnimatedSprite extends Renderable {

	private SpriteSheet spriteSheet;

	private Hashtable<String, Animation> animations;
	
	private Animation currentAnimation;
	
	private boolean autoUpdate;
	private double autoUpdateInterval;
	private double lastUpdateInterval;
	
	private boolean stoped;
	
	public AnimatedSprite(String id, World world, String fileName, float width, float height, int frameWidth, int frameHeight, int spacing) throws Alien3DException {
		super(world);
		spriteSheet = new SpriteSheet("", world, fileName, width, height, frameWidth, frameHeight, spacing);
		animations = new Hashtable<String, Animation>(1);
		autoUpdate = false;
		lastUpdateInterval = 0;
	}
	
	public void addAnimation(String name, int startFrame, int stopFrame) throws Alien3DException {
		animations.put(name, new Animation(this.spriteSheet, startFrame, stopFrame));
	}

	public void setCurrentAnimation(String name) throws Alien3DException {
		if (this.animations.containsKey(name)) {
			currentAnimation = this.animations.get(name);
		} else {
			throw new Alien3DException("Animation name '"+name+"' dont exists.");
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
		//this.spriteSheet.setPosition(getPosition().x, getPosition().y, getPosition().z);
		//this.spriteSheet.setRotation(getRotation().x, getRotation().y, getRotation().z);
		//this.spriteSheet.setScale(getScale().x, getScale().y, getScale().z);
		
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
	
	@Override
	public void render(Transformable transformable, GL10 gl, int delta) throws Alien3DException {
		try {
			this.verifyChanges();
			if (! stoped) {
				if (this.autoUpdate || this.currentAnimation.isPlaying()) {
					this.currentAnimation.renderCurrentFrame(transformable, gl, delta);
					if (this.lastUpdateInterval == 0) {
						this.lastUpdateInterval = System.currentTimeMillis();
						this.currentAnimation.renderNextFrame(transformable, gl, delta);
					} else {
						if (this.autoUpdateInterval <= System.currentTimeMillis() - this.lastUpdateInterval) {
							this.currentAnimation.renderNextFrame(transformable, gl, delta);
							this.lastUpdateInterval = System.currentTimeMillis();
						}
					}
				} else {
					this.currentAnimation.renderCurrentFrame(transformable, gl, delta);
				}
			}
		} catch (Alien3DException ex) {
			Log.e("AnimatedSprite", ex.getMessage());
		}
	}

	@Override
	public void update(Transformable transformable, GL10 gl, int delta, int fps) throws Alien3DException {
		
	}
	
	private class Animation {
		
		private int start;
		private int stop;
		
		private int nextFrame;
		private boolean playing;
		
		private SpriteSheet spriteSheet;
		
		Animation(SpriteSheet spriteSheet, int start, int stop) throws Alien3DException {
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
		
		void renderNextFrame(Transformable transformable, GL10 gl, int delta) throws Alien3DException {
			this.spriteSheet.renderFrame(transformable, gl, this.nextFrame, delta);
			this.gotoNextFrame();
		}

		void renderCurrentFrame(Transformable transformable, GL10 gl, int delta) throws Alien3DException {
			this.spriteSheet.renderFrame(transformable, gl, this.nextFrame, delta);
		}
		
	}

}
