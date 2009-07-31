package org.igs.android.ogl.engine.scene;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
 
public abstract class SceneObject {

	public enum TransparencyEnum {
		ALPHA,
		ALL,
		NONE;
	}

	private Renderer renderer;
	
	private GL10 gl;
	
	private Point3f rotation;
	private Point3f position;
	private Point3f scale;
	
	private Point3f velocity;
	
	private String id;
	
	private SceneObject parent;
	
	private List<SceneObject> childList;

	private Hashtable<String, Object> properties;
	
	private boolean modified;
	
	/**
	 * 
	 * @param id Object Identifier
	 * @param parent Parent Object
	 * @param renderer Engine Renderer
	 * @param rotation Actual Rotation
	 * @param position Actual Position
	 * @param scale Actual Scale
	 */
	public SceneObject(String id, SceneObject parent, Renderer renderer, Point3f position, Point3f rotation, Point3f scale) {
		this.id = id;
		this.parent = parent;
		this.renderer = renderer;
		this.gl = this.renderer.getGL10();
		this.rotation = rotation;
		this.position = position;
		this.scale = scale;
		
		childList = new LinkedList<SceneObject>();
		properties = new Hashtable<String, Object>(1);
	}
	
	public boolean isModified() {
		return this.modified;
	}
	
	protected void setModified(boolean modified) {
		this.modified = true;
	}
	
	public String getId() {
		return id;
	}
	
	public SceneObject getParent()	{
		return parent;
	}

	public List<SceneObject> getChildList() {
		return childList;
	}
	
	public SceneObject findChildById(String id) throws AndromedaException {
		for (SceneObject child : this.getChildList()) {
			if (child.getId().equals(id)) {
				return child;
			}
		}
		throw new AndromedaException(AndromedaException.NODE_NOT_FOUND_EXCEPTION);
	}

	public void removeChild(SceneObject n)	{
		childList.remove(n);
	}

	public void remove() {
		if (parent != null) {
			parent.removeChild(this);
			parent = null;
		}
	}
	
	public Hashtable<String, Object> getProperties() {
		return properties;
	}
	
	public void setProperties(Hashtable<String, Object> properties) {
		this.properties = properties;
	}

	public void addProperty(String name, Object value) {
		this.properties.put(name, value);
	}
	
	public Object getProperty(String name) throws AndromedaException {
		if (propertyExists(name)) {
			return properties.get(name);
		}
		throw new AndromedaException(AndromedaException.PROPERTY_NOT_FOUND_EXCEPTION, name);
	}
	
	public boolean propertyExists(String name) {
		return properties.containsKey(name);
	}
	
	public void removeProperty(String key) {
		properties.remove(key);
	}
	
	public Renderer getRenderer() {
		return renderer;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	public final Point3f getRotation() {
		return rotation;
	}

	public final void setRotation(Point3f rotation) {
		this.rotation = rotation;
		modified = true;
	}

	public final void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
		modified = true;
	}
	
	public final Point3f getPosition() {
		return position;
	}

	public final void setPosition(Point3f position) {
		this.position = position;
		modified = true;
	}

	public final void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
		modified = true;
	}
	
	public final Point3f getScale() {
		return scale;
	}

	public final void setScale(Point3f scale) {
		this.scale = scale;
		modified = true;
	}
	
	public final void setScale(float x, float y, float z) {
		scale.x = x;
		scale.y = y;
		scale.z = z;
		modified = true;
	}

	public final void begin() {
		gl.glPushMatrix();
	}
	
	public final void end() {
		gl.glPopMatrix();
	}

	public final void beginOrtho() {
		renderer.enterOrtho();
	}
	
	public final void endOrtho() {
		renderer.leaveOrtho();
	}
	
	public final void enableTransparency(TransparencyEnum transparencyType) {
		if (! (transparencyType == null) && ! (transparencyType == TransparencyEnum.NONE)) {
			gl.glEnable(GL10.GL_BLEND);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			if (transparencyType == TransparencyEnum.ALL) {
				gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			} else {
				gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			}
			gl.glEnable(GL10.GL_ALPHA_TEST);
			gl.glDisable(GL10.GL_DEPTH_TEST);
		}
	}

	public final void disableTransparency() {
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_ALPHA_TEST);
		gl.glEnable(GL10.GL_DEPTH_TEST);
	}
	
	public final void enableAntialias() {
		gl.glEnable(GL10.GL_LINE_SMOOTH);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);			
	}

	public final void disableAntialias() {
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_LINE_SMOOTH);
	}
	
	public final void enableTexture() {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glActiveTexture(GL10.GL_TEXTURE0);
	}

	public final void disableTexture() {
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glActiveTexture(GL10.GL_TEXTURE0);
	}

	public final void enableVertex() {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);		
	}
	
	public final void disableVertex() {
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public final void rotate() {
		gl.glRotatef(rotation.x, 1, 0, 0);
		gl.glRotatef(rotation.y, 0, 1, 0);
		gl.glRotatef(rotation.z, 0, 0, 1);
	}
	
	public final void addRotation(float x, float y, float z) {
		rotation.x += x;
		rotation.y += y;
		rotation.z += z;
		modified = true;
	}

	public final void translate() {
		gl.glTranslatef(this.position.x, this.position.y, this.position.z);
	}
	
	public final void addTranslation(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
		modified = true;
	}

	public final void scale() {
		gl.glScalef(scale.x, scale.y, scale.z);
	}
	
	public final void addScale(float x, float y, float z) {
		scale.x += x;
		scale.y += y;
		scale.z += z;
		modified = true;
	}
	
	protected void applyTransformation() {
		gl.glTranslatef(position.x, position.y, position.z);
		gl.glRotatef(rotation.x, 1f, 0f, 0f);

		/*
		if (this.rotation.y >= 360) {
			this.rotation.y = 0;
		} else if (this.rotation.y <= 0) {
			this.rotation.y = 360;
		} 
		*/
		gl.glRotatef(rotation.y, 0f, 1f, 0f);
		gl.glRotatef(rotation.z, 0f, 0f, 1f);
		gl.glScalef(scale.x, scale.y, scale.z);
	}
	
	public abstract void render(float delta);
	
	/* TODO Hack Methods */
	
	public void setVelocity(Point3f velocity) {
		this.velocity = velocity;
	}
	
	public Point3f getVelocity() {
		if (velocity == null)
			velocity = new Point3f(0, 0, 0);
		
		return this.velocity;
	}
	
}
