package org.igs.android.ogl.engine.shape;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject;
import org.igs.android.ogl.engine.texture.Texture;
import org.igs.android.ogl.engine.texture.TextureBuffer;
import org.igs.android.ogl.engine.utils.Utils;

import android.content.Context;

public class Cube extends SceneObject {

	private float texCoords[] = new float[] {
			// FRONT
			 0.0f, 1.0f,
			 0.0f, 0.0f,
			 1.0f, 1.0f,
			 1.0f, 0.0f,
			// BACK
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f,
			// LEFT
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f,
			// RIGHT
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f,
			// TOP
			 0.0f, 0.0f,
			 1.0f, 0.0f,
			 0.0f, 1.0f,
			 1.0f, 1.0f,
			// BOTTOM
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f
		};

	private FloatBuffer cubeBuff;
	private FloatBuffer texBuff;
	
	private Texture texture;
	
	private float width;
	private float height;
	//private float depth;
	
	public Cube(String id, SceneObject parent, Renderer renderer, float width, float height, float depth, Point3f position, Point3f rotation, Point3f scale) {
		super(id, parent, renderer, position, rotation, scale);
		this.width = width;
		this.height = height;
		this.prepareBuffers();
	}
	
	private void prepareBuffers() {
		float box[] = new float[] {
				// FRONT
				 width, -height,  0.5f,
				 width,  height,  0.5f,
				-width, -height,  0.5f,
				-width,  height,  0.5f,
				// BACK
				-width, -height, -0.5f,
				-width,  height, -0.5f,
				 width, -height, -0.5f,
				 width,  height, -0.5f,
				// LEFT
				-width, -height,  0.5f,
				-width,  height,  0.5f,
				-width, -height, -0.5f,
				-width,  height, -0.5f,
				// RIGHT
				 width, -height, -0.5f,
				 width,  height, -0.5f,
				 width, -height,  0.5f,
				 width,  height,  0.5f,
				// TOP
				-width,  height,  0.5f,
			 	 width,  height,  0.5f,
				-width,  height, -0.5f,
				 width,  height, -0.5f,
				// BOTTOM
				-width, -height,  0.5f,
				-width, -height, -0.5f,
				 width, -height,  0.5f,
				 width, -height, -0.5f,
			};
		
		cubeBuff = Utils.toFloatBufferPositionZero(box);
		texBuff = Utils.toFloatBufferPositionZero(texCoords);
		
	}
	
	public void setTexture(Context context, GL10 gl, String name) throws Exception {
		try {
			setTexture(TextureBuffer.getInstance().getTexture(context, gl, name));
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public void render(float delta) {
		GL10 gl = super.getRenderer().getGL10();
		this.begin();
			if (this.texture != null) {
				this.texture.bind(gl);
			}
			
			gl.glEnable(GL10.GL_DEPTH_TEST);
			
			gl.glFrontFace(GL10.GL_CCW);
		
			this.applyTransformation();
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuff);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			gl.glColor4f(1.0f, 1, 1, 1.0f);
			gl.glNormal3f(0,0,1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glNormal3f(0,0,-1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
		
			gl.glColor4f(1, 1.0f, 1, 1.0f);
			gl.glNormal3f(-1,0,0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
			gl.glNormal3f(1,0,0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
			
			gl.glColor4f(1, 1, 1.0f, 1.0f);
			gl.glNormal3f(0,1,0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
			gl.glNormal3f(0,-1,0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
			gl.glDisable(GL10.GL_DEPTH_TEST);
			
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			for (SceneObject child : this.getChildList()) {
				child.render(delta);
			}
		this.end();
	}

}
