package org.igs.android.ogl.engine.sprite;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.event.TouchAreaEventHandler;
import org.igs.android.ogl.engine.event.TouchEventHandler;
import org.igs.android.ogl.engine.gui.TouchMap;
import org.igs.android.ogl.engine.gui.TouchMapItem;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.Scene;
import org.igs.android.ogl.engine.scene.SceneObject;

import android.view.MotionEvent;

public class TouchSprite extends SceneObject implements TouchEventHandler {

	private TouchAreaEventHandler handler;
	
	private Sprite touchDownSprite;
	private Sprite touchUpSprite;
	private Sprite renderableSprite;
	
	private float width;
	private float height;

	private Scene scene;

	private TouchMap touchMap;

	public TouchSprite(String id, SceneObject parent, Scene scene, String upSpriteName, String downSpriteName, float x, float y, float width, float height, TouchAreaEventHandler handler, TouchMap touchMap, Point3f position, Point3f rotation, Point3f scale, TransparencyEnum transparencyType) throws AndromedaException {
		super(id, parent, scene.getRenderer(), position, rotation, scale);
		touchDownSprite = new Sprite("", this, scene.getRenderer(), downSpriteName, width, height, new Point3f(position), new Point3f(rotation), new Point3f(scale), transparencyType);
		touchUpSprite = new Sprite("", this, scene.getRenderer(), upSpriteName, width, height, new Point3f(position), new Point3f(rotation), new Point3f(scale), transparencyType);
		this.handler = handler;
		this.scene = scene;
		this.width = width;
		this.height = height;
		this.touchMap = touchMap;
		renderableSprite = touchUpSprite;
	}

	public void setTransparencyType(TransparencyEnum transparencyType) {
		touchDownSprite.setTransparencyType(transparencyType);
		touchUpSprite.setTransparencyType(transparencyType);
		renderableSprite.setTransparencyType(transparencyType);
	}
	
	public TransparencyEnum getTransparencyType() {
		return this.touchDownSprite.getTransparencyType();
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

	public void render(float delta) {
		this.verifyChanges();
		renderableSprite.render(delta);
	}

	private void verifyChanges() {
		if (this.isModified()) {
			this.redefine();
			this.setModified(false);
		}
	}
	
	private void redefine() {
		touchDownSprite.setPosition(getPosition().x, getPosition().y, getPosition().z);
		touchDownSprite.setRotation(getRotation().x, getRotation().y, getRotation().z);
		touchDownSprite.setScale(getScale().x, getScale().y, getScale().z);
		touchDownSprite.setWidth(width);
		touchDownSprite.setHeight(height);

		touchUpSprite.setPosition(getPosition().x, getPosition().y, getPosition().z);
		touchUpSprite.setRotation(getRotation().x, getRotation().y, getRotation().z);
		touchUpSprite.setScale(getScale().x, getScale().y, getScale().z);
		touchUpSprite.setWidth(width);
		touchUpSprite.setHeight(height);

		renderableSprite.setPosition(getPosition().x, getPosition().y, getPosition().z);
		renderableSprite.setRotation(getRotation().x, getRotation().y, getRotation().z);
		renderableSprite.setScale(getScale().x, getScale().y, getScale().z);
		renderableSprite.setWidth(width);
		renderableSprite.setHeight(height);
	}
	
	public void register() {
		scene.addTouchEvent(this);
	}

	public void unRegister() {
		scene.removeTouchEvent(this);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			TouchMapItem item = this.touchMap.verifyTouch(this.getPosition().x, this.getPosition().y, event.getX(), event.getY()); 
			if (item != null) {
				this.renderableSprite = this.touchDownSprite;
				this.handler.onTouchAreaDownEvent(item.getId(), event);
			}
		} else
		if (event.getAction() == MotionEvent.ACTION_UP) {
			this.renderableSprite = this.touchUpSprite;
			TouchMapItem item = this.touchMap.verifyTouch(this.getPosition().x, this.getPosition().y, event.getX(), event.getY()); 
			if (item != null) {
				this.handler.onTouchAreaUpEvent(item.getId(), event);
			}
		} else
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			TouchMapItem item = this.touchMap.verifyTouch(this.getPosition().x, this.getPosition().y, event.getX(), event.getY()); 
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
	
}
