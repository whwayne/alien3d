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

public class Plan extends SceneObject {

	private float texCoords[] = new float[] {
			// FRONT
			 0.0f, 1.0f,
			 0.0f, 0.0f,
			 1.0f, 1.0f,
			 1.0f, 0.0f
		};

	private FloatBuffer planBuff;
	private FloatBuffer texBuff;
	
	private Texture texture;
	
	private float width;
	private float height;
	//private float depth;
	
	public Plan(String id, SceneObject parent, Renderer renderer, float width, float height, Point3f position, Point3f rotation, Point3f scale) {
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
				-width,  height,  0.5f
			};
		
		planBuff = Utils.toFloatBufferPositionZero(box);
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
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, planBuff);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuff);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			gl.glColor4f(1.0f, 1, 1, 1.0f);
			gl.glNormal3f(0,0,1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			//gl.glNormal3f(0,0,-1);
			//gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
		
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			
			for (SceneObject child : this.getChildList()) {
				child.render(delta);
			}
		this.end();
	}
	
}
