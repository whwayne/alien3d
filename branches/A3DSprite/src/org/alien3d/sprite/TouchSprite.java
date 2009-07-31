package org.alien3d.sprite;

import javax.microedition.khronos.opengles.GL10;

import org.alien3d.Alien3DException;
import org.alien3d.World;
import org.alien3d.entity.Renderable;
import org.alien3d.entity.Transformable;
import org.alien3d.event.TouchAreaEventHandler;
import org.alien3d.event.TouchEventHandler;
import org.alien3d.scene.gui.TouchMap;
import org.alien3d.scene.gui.TouchMapItem;

import android.view.MotionEvent;

public class TouchSprite extends Renderable implements TouchEventHandler {

	private TouchAreaEventHandler handler;
	
	private Sprite touchDownSprite;
	private Sprite touchUpSprite;
	private Sprite renderableSprite;
	
	private float width;
	private float height;

	private TouchMap touchMap;

	private Transformable transformable;
	
	public TouchSprite(String id, World world, String upSpriteName, String downSpriteName, float x, float y, float width, float height, TouchAreaEventHandler handler, TouchMap touchMap) throws Alien3DException {
		super(world);
		touchDownSprite = new Sprite("", world, downSpriteName, width, height);
		touchUpSprite = new Sprite("", world, upSpriteName, width, height);
		this.handler = handler;
		this.width = width;
		this.height = height;
		this.touchMap = touchMap;
		renderableSprite = touchUpSprite;
	}

	public void setTransformable(Transformable transformable) {
		this.transformable = transformable; 
	}
	
	public void setTransparencyType(int transparencyType) {
		touchDownSprite.enableTransparency(true, transparencyType);
		touchUpSprite.enableTransparency(true, transparencyType);
		renderableSprite.enableTransparency(true, transparencyType);
	}
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
		super.setModified(true);
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
		super.setModified(true);
	}

	private void verifyChanges() {
		if (this.isModified()) {
			this.redefine();
			this.setModified(false);
		}
	}
	
	private void redefine() {
		/*
		touchDownSprite.setPosition(getPosition().x, getPosition().y, getPosition().z);
		touchDownSprite.setRotation(getRotation().x, getRotation().y, getRotation().z);
		touchDownSprite.setScale(getScale().x, getScale().y, getScale().z);
		*/
		touchDownSprite.setWidth(width);
		touchDownSprite.setHeight(height);

		/*
		touchUpSprite.setPosition(getPosition().x, getPosition().y, getPosition().z);
		touchUpSprite.setRotation(getRotation().x, getRotation().y, getRotation().z);
		touchUpSprite.setScale(getScale().x, getScale().y, getScale().z);
		*/
		touchUpSprite.setWidth(width);
		touchUpSprite.setHeight(height);

		/*
		renderableSprite.setPosition(getPosition().x, getPosition().y, getPosition().z);
		renderableSprite.setRotation(getRotation().x, getRotation().y, getRotation().z);
		renderableSprite.setScale(getScale().x, getScale().y, getScale().z);
		*/
		renderableSprite.setWidth(width);
		renderableSprite.setHeight(height);
	}
	
	public void register() {
		world.addTouchEvent(this);
	}

	public void unRegister() {
		world.removeTouchEvent(this);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			TouchMapItem item = this.touchMap.verifyTouch(transformable.position.x, transformable.position.y, event.getX(), event.getY()); 
			if (item != null) {
				this.renderableSprite = this.touchDownSprite;
				this.handler.onTouchAreaDownEvent(item.getId(), event);
			}
		} else
		if (event.getAction() == MotionEvent.ACTION_UP) {
			this.renderableSprite = this.touchUpSprite;
			TouchMapItem item = this.touchMap.verifyTouch(transformable.position.x, transformable.position.y, event.getX(), event.getY()); 
			if (item != null) {
				this.handler.onTouchAreaUpEvent(item.getId(), event);
			}
		} else
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			TouchMapItem item = this.touchMap.verifyTouch(transformable.position.x, transformable.position.y, event.getX(), event.getY()); 
			if (item != null) {
				this.renderableSprite = this.touchDownSprite;
				this.handler.onTouchAreaLeaveEvent(item.getId(), event);
			} else {
				this.renderableSprite = this.touchUpSprite;
			}
		}		
		return false;
	}
	
	public void destroy() {
		touchDownSprite.destroy();
		touchDownSprite = null;
		touchUpSprite.destroy();
		touchUpSprite = null;
		renderableSprite.destroy();
		renderableSprite = null;
	}

	@Override
	public void render(Transformable transformable, GL10 gl, int delta)
			throws Alien3DException {
		this.verifyChanges();
		renderableSprite.render(transformable, gl, delta);
	}

	@Override
	public void update(Transformable transformable, GL10 gl, int delta, int fps) throws Alien3DException {
		
	}
	
}
