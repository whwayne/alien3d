package org.igs.android.ogl.engine.scene;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point4f;

public class Light {
	
	private Renderer renderer;
	
	private boolean setuped;

	private Point4f ambient;
	private Point4f diffuse;
	private Point4f pos;
	private Point4f specular;
	
	
	/*
	private float lightAmbient[] = new float[] { 0.2f, 0.3f, 0.6f, 1.0f };
	private float lightDiffuse[] = new float[] { 0.2f, 0.3f, 0.6f, 1.0f };
	private float[] lightPos = new float[] {0,0,0,1};
	private float specular[] = new float[] {1, 1, 1, 1};
	
	float matAmbient[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	float matDiffuse[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	*/
	
	public Light(Renderer renderer) {
		this.renderer = renderer;
	}

	public Point4f getAmbient() {
		return ambient;
	}

	public void setAmbient(Point4f ambient) {
		this.ambient = ambient;
	}

	public Point4f getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(Point4f diffuse) {
		this.diffuse = diffuse;
	}

	public Point4f getPos() {
		return pos;
	}

	public void setPos(Point4f pos) {
		this.pos = pos;
	}

	public Point4f getSpecular() {
		return specular;
	}

	public void setSpecular(Point4f specular) {
		this.specular = specular;
	}
	
    private void setUpLight() {
    	GL10 gl = renderer.getGL10();
    	/*
    	FloatBuffer buffer;

        buffer = Utils.allocateFloatBuffer(4 * 4);
        buffer.put(1).put(1).put(1).put(1);
        buffer.flip();
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, buffer);

        buffer = Utils.allocateFloatBuffer(4 * 4);
        buffer.put(1).put(1).put(1).put(1);
        buffer.flip();
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, buffer);

        // setup the ambient light 
        buffer = Utils.allocateFloatBuffer(4 * 4);
        buffer.put(0.8f).put(0.8f).put(0.8f).put(0.8f);
        buffer.flip();
        gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, buffer);
        gl.glLightModelx(GL10.GL_LIGHT_MODEL_TWO_SIDE, GL10.GL_TRUE);

        // set up the position of the light
        buffer = Utils.allocateFloatBuffer(4 * 4);
        buffer.put(10).put(10).put(5).put(0);
        buffer.flip();
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, buffer);

        gl.glEnable(GL10.GL_LIGHT0);    
        
        
        */
        this.setuped = true;
    	
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, new float[]{ambient.x, ambient.y, ambient.z, ambient.w},	0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, new float[]{diffuse.x, diffuse.y, diffuse.z, diffuse.w}, 0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, new float[]{pos.x, pos.y, pos.z, pos.w}, 0);
		// rgba color & intensity
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, new float[]{specular.x, specular.y, specular.z, specular.w}, 0);		
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glMaterialx(GL10.GL_FRONT, GL10.GL_SHININESS, 128);
    }
    
    public void render() {
    	if (! setuped) {
    		this.setUpLight();
    	}
    	
    	//GL10 gl = renderer.getGL10();
    	/*
        material.put(1).put(1).put(1).put(1);
        material.flip();
        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, material);
        gl.glMaterialfv(GL10.GL_BACK, GL10.GL_DIFFUSE, material);
        */
    }
	
}
