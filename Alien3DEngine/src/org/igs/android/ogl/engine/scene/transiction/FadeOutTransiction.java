package org.igs.android.ogl.engine.scene.transiction;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.utils.Color;
import org.igs.android.ogl.engine.utils.Utils;

public class FadeOutTransiction extends Transiction {

	private Color fadeTo;
	/** The total time for fade out */
	private int total = 350;
	/** The current timeout value */
	private int value;
	
	private FloatBuffer vertCoords;
	
	public FadeOutTransiction(Renderer renderer, Color fadeTo) {
		super(renderer);
		this.fadeTo = fadeTo;
		this.buildBuffer(renderer.getView().getWidth(), renderer.getView().getHeight());
		this.init();
	}

	private void buildBuffer(float width, float height) {
    	float[] vertCoords = new float[]{
    			0, 0, 0,
    			width, 0, 0,
    			0, height, 0,
    			width, height, 0
    	};
		this.vertCoords = Utils.toFloatBufferPositionZero(vertCoords);
	}

	@Override
	public void init() {
		value = total;
	}
	
	@Override
	public void render(float delta) {
		if (delta > 50) {
			delta = 50;
		}
		
		float alpha = 1 - (((float) value) / ((float) total));
		value -= delta;
		
		super.getRenderer().enterOrtho();
		
		GL10 gl = this.getRenderer().getGL10();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glColor4f(fadeTo.r, fadeTo.g, fadeTo.b, alpha);
		
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertCoords);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		
		this.getRenderer().leaveOrtho();
		
	}

	@Override
	public boolean isComplete() {
		return value <= 0;
	}

}
