package org.igs.android.ogl.engine.sprite;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject;
import org.igs.android.ogl.engine.texture.Texture;
import org.igs.android.ogl.engine.texture.TextureBuffer;
import org.igs.android.ogl.engine.utils.Color;
import org.igs.android.ogl.engine.utils.Utils;

public class Sprite extends SceneObject {

	private Texture texture;
	
	private FloatBuffer texCoords;
	private FloatBuffer vertCoords;
	
	private float width;
	private float height;
	
	private Color color;
	
	private boolean changed;
	
	public Sprite(String id, SceneObject parent, Renderer renderer, String name, float width, float height, Point3f position, Point3f rotation, Point3f scale, TransparencyEnum transparencyType) throws AndromedaException {
		this(id, parent, renderer, TextureBuffer.getInstance().getTexture(renderer.getContext(), renderer.getGL10(), name), width, height, position, rotation, scale, transparencyType);
	}

	public Sprite(String id, SceneObject parent, Renderer renderer, Texture texture, float width, float height, Point3f position, Point3f rotation, Point3f scale, TransparencyEnum transparencyType) throws AndromedaException {
		super(id, parent, renderer, position, rotation, scale);
		try {
			this.width = width;
			this.height = height;
			this.texture = texture;
			this.setTransparencyType(transparencyType);
			this.buildBuffers();
		} catch (Exception ex) {
			throw new AndromedaException(AndromedaException.TEXTURE_NOT_FOUND_EXCEPTION, ex.getMessage());
		}
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public TransparencyEnum getTransparencyType() {
		return this.texture.getTransparencyType();
	}

	public void setTransparencyType(TransparencyEnum transparencyType) {
		this.texture.setTransparencyType(transparencyType);
	}
	
    public float getWidth() {
        return this.width;
    }
	
	public void setWidth(float width) {
		this.width = width;
		this.changed = true;
	}

    public float getHeight() {
        return this.height;
    }
	
	public void setHeight(float height) {
		this.height = height;
		this.changed = true;
	}
    
    private void buildBuffers() {
    	/*
    	float[] textCoords = new float[]{
    			0, 0,
    			0, texture.getHeight(),
    			texture.getWidth(), texture.getHeight(),
    			texture.getWidth(), 0
    	};
    	*/
    	float[] textCoords = new float[]{
    			0, 0, //Bottom Left
    			1, 0, //Bottom Right
    			0, 1, //Top Left
    			1, 1 //Top Right
    	};
    	/*
    	float[] vertCoords = new float[]{
    			-1.0f, 1.0f, 0.0f, // Top Left
    			1.0f, 1.0f, 0.0f,  // Top Right
    			1.0f,-1.0f, 0.0f,  // Bottom Right
    			-1.0f,-1.0f, 0.0f  // Bottom Left
    	};
    	*/
		
    	/*
    	float[] vertCoords = new float[]{
    			0, 0, 0,
    			10, -10, 0,
    			-10, 10, 0,
    			10, 10, 0
    	};
		*/
    	float[] vertCoords = new float[]{
    			0, 0, 0,
    			width, 0, 0,
    			0, height, 0,
    			width, height, 0
    	};
    	
    	
    	/*
    	float[] vertCoords = new float[]{
    			0, 0, 0,
    			0, texture.getHeight(), 0,
    			texture.getWidth(), texture.getHeight(), 0,
    			texture.getWidth(), 0, 0
    	};
    	*/
    	this.texCoords = Utils.toFloatBufferPositionZero(textCoords);
    	this.vertCoords = Utils.toFloatBufferPositionZero(vertCoords);
    }
    
    private void redefine() {
    	vertCoords.put(3, width);
    	vertCoords.put(9, width);
    	vertCoords.put(7, height);
    	vertCoords.put(10, height);
    }

    public void renderOrtho(float delta) {
    	if (this.changed) {
    		this.redefine();
    		this.changed = false;
    	}
    	this.beginOrtho();
    	
    		this.translate();
	    	this.rotate();
	    	
	    	this.scale();
	    	this.enableTexture();
		    	this.enableTransparency(this.texture.getTransparencyType());
			    	this.texture.bind(this.getRenderer().getGL10());
			    	this.enableVertex();
				    	this.getRenderer().getGL10().glVertexPointer(3, GL10.GL_FLOAT, 0, vertCoords);
				    	this.getRenderer().getGL10().glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
				    	this.getRenderer().getGL10().glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			    	this.disableVertex();
		    	this.disableTransparency();
	    	this.disableTexture();
    	this.endOrtho();
    }

    public void render(float delta) {
    	if (this.changed) {
    		this.redefine();
    		this.changed = false;
    	}
    	this.begin();
	    	
	    	this.translate();
	    	this.rotate();
	    	this.scale();
	    	this.enableTexture();
		    	this.enableTransparency(this.texture.getTransparencyType());
			    	this.texture.bind(this.getRenderer().getGL10());
			    	this.enableVertex();
				    	this.getRenderer().getGL10().glVertexPointer(3, GL10.GL_FLOAT, 0, vertCoords);
				    	this.getRenderer().getGL10().glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
				    	this.getRenderer().getGL10().glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			    	this.disableVertex();
		    	this.disableTransparency();
	    	this.disableTexture();
    	this.end();
    	/*
    	gl.glPushMatrix();
    	
    	gl.glRotatef(rotation.x, 1, 0, 0);
    	gl.glRotatef(rotation.y, 0, 1, 0);
    	gl.glRotatef(rotation.z, 0, 0, 1);
    	
    	if (this.color != null) {
    		gl.glColor4f(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
    	}
    	gl.glTranslatef(this.x, this.y, 0);
		texture.bind(gl);
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertCoords);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPopMatrix();
		*/
    }

    public void destroy() {
    	
    }
    
}
