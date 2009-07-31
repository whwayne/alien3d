package org.alien3d.sprite;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.alien3d.Alien3DException;
import org.alien3d.World;
import org.alien3d.entity.Renderable;
import org.alien3d.entity.Transformable;
import org.alien3d.texture.Texture;
import org.alien3d.texture.TextureBuffer;
import org.alien3d.util.Utils;

public class Sprite extends Renderable {

	private FloatBuffer texCoords;
	private FloatBuffer vertCoords;
	
	private float width;
	private float height;
	
	private boolean changed;
	
	public Sprite(String id, World world, String name, float width, float height) throws Alien3DException {
		this(id, world, TextureBuffer.getInstance().getTexture(world.getContext(), world.getGL(), name), width, height);
	}

	public Sprite(String id, World world, Texture texture, float width, float height) throws Alien3DException {
		super(world);
		try {
			this.width = width;
			this.height = height;
			this.texture = texture;
			this.buildBuffers();
		} catch (Exception ex) {
			throw new Alien3DException(ex.getMessage());
		}
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

    public void renderOrtho(Transformable transformable, GL10 gl, float delta) throws Alien3DException {
    	if (this.changed) {
    		this.redefine();
    		this.changed = false;
    	}
    	world.beginOrtho();
    		
    		transformable.applyTransformation();
    		transformable.enableTexture();
    		transformable.enableTransparency(this.texture.getTransparencyType());
			texture.bind(world.getGL());
			transformable.enableVertex();
			world.getGL().glVertexPointer(3, GL10.GL_FLOAT, 0, vertCoords);
			world.getGL().glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
			world.getGL().glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			transformable.disableVertex();
			transformable.disableTransparency();
	    	transformable.disableTexture();
    	world.endOrtho();
    }

    public void destroy() {
    	
    }

	@Override
	public void render(Transformable transformable, GL10 gl, int delta) throws Alien3DException {
    	if (this.changed) {
    		this.redefine();
    		this.changed = false;
    	}
    	transformable.begin();
    		transformable.applyTransformation();
    		transformable.enableTexture();
    		transformable.enableTransparency(this.texture.getTransparencyType());
			texture.bind(world.getGL());
			transformable.enableVertex();
			world.getGL().glVertexPointer(3, GL10.GL_FLOAT, 0, vertCoords);
			world.getGL().glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
			world.getGL().glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			transformable.disableVertex();
			transformable.disableTransparency();
			transformable.disableTexture();
		transformable.end();
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

	@Override
	public void update(Transformable transformable, GL10 gl, int delta, int fps)
			throws Alien3DException {
		
	}
    
}
