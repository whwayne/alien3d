package org.igs.android.ogl.engine.shape;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject;
import org.igs.android.ogl.engine.utils.Utils;

public class Sphere extends SceneObject {

	private FloatBuffer sphereVertex;
	private FloatBuffer sphereNormal;
	private float sphere_parms[] = new float[3];
	
	private float radius;
	private int slices;
	private int stacks;
	
	public Sphere(String id, SceneObject parent, Renderer renderer, float radius, int slices, int stacks, Point3f position, Point3f rotation, Point3f scale) {
		super(id, parent, renderer, position, rotation, scale);
		this.radius = radius;
		this.slices = slices;
		this.stacks = stacks;
		this.buildBuffer(this.getRenderer().getGL10());
	}
	
	public float getRadius() {
		return this.radius;
	}
	
	private void buildBuffer(GL10 gl) {
		sphere_parms[0] = radius; 
		sphere_parms[1] = (float)slices; 
		sphere_parms[2] = (float)stacks;

		plotSpherePoints(radius, stacks, slices);
	}

	private void plotSpherePoints(float radius, int stacks, int slices)	{
		
		sphereVertex=Utils.allocateFloatBuffer( 4* 6 * stacks * (slices+1) );
		sphereNormal=Utils.allocateFloatBuffer( 4* 6 * stacks * (slices+1) );
		int i, j; 
		float slicestep, stackstep;

		stackstep = ((float)Math.PI) / stacks;
		slicestep = 2.0f * ((float)Math.PI) / slices;

		for (i = 0; i < stacks; ++i)		
		{
			float a = i * stackstep;
			float b = a + stackstep;

			float s0 =  (float)Math.sin(a);
			float s1 =  (float)Math.sin(b);

			float c0 =  (float)Math.cos(a);
			float c1 =  (float)Math.cos(b);

			float nv;
			for (j = 0; j <= slices; ++j)		
			{
				float c = j * slicestep;
				float x = (float)Math.cos(c);
				float y = (float)Math.sin(c);

				nv=x * s0;
				sphereNormal.put(nv);
				sphereVertex.put( nv * radius);

				nv=y * s0;
					sphereNormal.put(nv);
				sphereVertex.put( nv * radius);

				nv=c0;

				sphereNormal.put(nv);
				sphereVertex.put( nv * radius);

				nv=x * s1;

				sphereNormal.put(nv);
				sphereVertex.put( nv * radius);

				nv=y * s1;

				sphereNormal.put(nv);
				sphereVertex.put( nv * radius);

				nv=c1;

				sphereNormal.put(nv);
				sphereVertex.put( nv * radius);
			}
		}
		sphereNormal.position(0);
		sphereVertex.position(0);
	}
	
	@Override
	public void render(float delta) {
		GL10 gl = super.getRenderer().getGL10();
		int i, triangles;
		this.begin();
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_LIGHT0);

			this.applyTransformation();
			gl.glDisable(GL10.GL_TEXTURE_2D);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, sphereVertex);
			gl.glNormalPointer(GL10.GL_FLOAT, 0, sphereNormal);
	
			gl.glEnableClientState (GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState (GL10.GL_NORMAL_ARRAY);
			triangles = (slices + 1) * 2;
			for(i = 0; i < stacks; i++) {
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i * triangles, triangles);
			}
	
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
			
			for (SceneObject child : this.getChildList()) {
				child.render(delta);
			}
			gl.glEnable(GL10.GL_TEXTURE_2D);
		this.end();
	}
	
}
