package org.igs.android.ogl.engine.shape;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject;
import org.igs.android.ogl.engine.utils.Utils;

public class Torus extends SceneObject {

	private FloatBuffer p = null;
	private FloatBuffer q = null;
	private FloatBuffer v = null;
	private FloatBuffer n = null;
	private float parms[] = new float[4];
	
	private float ir;
	private float or;
	private int sides;
	private int rings;
	
	private int triangles;
	
	public Torus(String id, SceneObject parent, Renderer renderer, float ir, float or, int sides, int rings, Point3f position, Point3f rotation, Point3f scale) {
		super(id, parent, renderer, position, rotation, scale);
		this.ir = ir;
		this.or = or;
		this.sides = sides;
		this.rings = rings;
		this.buildBuffers();
	}

	private void buildBuffers() {
		int SIZEOF = 4;
		int i, j, k;
		float s, t, x, y, z, twopi, nx, ny, nz;
		float sin_s, cos_s, cos_t, sin_t, twopi_s, twopi_t;
		float twopi_sides, twopi_rings;

		// maybe clear buffer.
		if (v != null) {
			if (parms[0] != ir || parms[1] != or || parms[2] != sides
					|| parms[3] != rings) {

				// free(v);
				// free(n);
				n = v = null; // maybe free later.

				super.getRenderer().getGL10().glVertexPointer(3, GL10.GL_FLOAT, 0, Utils
						.allocateFloatBuffer(0));
				super.getRenderer().getGL10().glNormalPointer(GL10.GL_FLOAT, 0, Utils
						.allocateFloatBuffer(0));
			}
		}

		if (v == null) {
			parms[0] = ir;
			parms[1] = or;
			parms[2] = (float) sides;
			parms[3] = (float) rings;

			// this size is maybe wrong.
			p = v = Utils.allocateFloatBuffer((int) (sides * (rings + 1)
					* 2 * 3 * SIZEOF));
			q = n = Utils.allocateFloatBuffer((int) (sides * (rings + 1)
					* 2 * 3 * SIZEOF));

			twopi = 2.0f * (float) Math.PI;
			twopi_sides = twopi / sides;
			twopi_rings = twopi / rings;

			for (i = 0; i < sides; i++) {
				for (j = 0; j <= rings; j++) {
					for (k = 1; k >= 0; k--) {
						s = (i + k) % sides + 0.5f;
						t = (float) (j % rings);

						twopi_s = s * twopi_sides;
						twopi_t = t * twopi_rings;

						cos_s = (float) Math.cos(twopi_s);
						sin_s = (float) Math.sin(twopi_s);

						cos_t = (float) Math.cos(twopi_t);
						sin_t = (float) Math.sin(twopi_t);

						x = (or + ir * (float) cos_s) * (float) cos_t;
						y = (or + ir * (float) cos_s) * (float) sin_t;
						z = ir * (float) sin_s;

						p.put(x);
						p.put(y);
						p.put(z);

						nx = (float) cos_s * (float) cos_t;
						ny = (float) cos_s * (float) sin_t;
						nz = (float) sin_s;

						q.put(nx);
						q.put(ny);
						q.put(nz);
					}
				}
			}
		}

		v.position(0);
		n.position(0);
		triangles = ((int) rings + 1) * 2;
	}
	
	@Override
	public void render(float delta) {
		
		GL10 gl = this.getRenderer().getGL10();
		this.begin();
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_LIGHT0);
	
			this.applyTransformation();
			gl.glDisable(GL10.GL_TEXTURE_2D);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, v);
			gl.glNormalPointer(GL10.GL_FLOAT, 0, n);
	
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
	
			triangles = ((int) rings + 1) * 2;
	
			for (int i = 0; i < sides; i++) {
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, triangles * i, triangles);
			}
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			
			for (SceneObject child : this.getChildList()) {
				child.render(delta);
			}
			
	    this.end();
	}

}
