package org.igs.android.ogl.engine;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLU;
import android.view.SurfaceView;

public abstract class AbstractRenderer implements Renderer {

	private Context context;
	private SurfaceView view;
	private boolean mTranslucentBackground;
	private GL10 gl10;
	
	public AbstractRenderer(Context context, SurfaceView view, boolean useTranslucentBackground) {
		mTranslucentBackground = useTranslucentBackground;
		this.view = view;
		this.context = context;
	}

	
	public final SurfaceView getView() {
		return view;
	}

	public final Context getContext() {
		return this.context;
	}
	
    public GL10 getGL10() {
    	return this.gl10;
    }
	
    public void setGL10(GL10 gl) {
    	this.gl10 = gl;
    }
	
	public void initGL() {
		getGL10().glDisable(GL10.GL_DITHER); 
		/*
		getGL10().glViewport(0, 0, getView().getWidth(), getView().getHeight());
		getGL10().glMatrixMode(GL10.GL_PROJECTION);
		getGL10().glLoadIdentity();
        float ratio = (float) getView().getWidth() / getView().getHeight();
        GLU.gluPerspective(getGL10(), 45.0f, ratio * 2, 1, 500f);
        getGL10().glMatrixMode(GL10.GL_MODELVIEW);
        getGL10().glLoadIdentity();
        */
        //getGL10().glClearColor(0, 0, 0, 0);
        
		/*
		   gl.glClearColor (0.0f, 0.0f, 0.0f, 1f);
		   gl.glShadeModel (GL10.GL_SMOOTH);
		   gl.glEnable(GL10.GL_LIGHTING);
		   gl.glEnable(GL10.GL_LIGHT0);
		   gl.glEnable(GL10.GL_DEPTH_TEST);
		*/
/*	   
	   float light_ambient[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	   float light_diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	   float light_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
	   
	   float light_position[] = { 1.0f, 1.0f, 1.0f, 0.0f };
*/	   
	   /*
	   gl.glLightfv (GL10.GL_LIGHT0, GL10.GL_AMBIENT, FloatBuffer.wrap(light_ambient));
	   gl.glLightfv (GL10.GL_LIGHT0, GL10.GL_DIFFUSE, FloatBuffer.wrap(light_diffuse));
	   gl.glLightfv (GL10.GL_LIGHT0, GL10.GL_SPECULAR, FloatBuffer.wrap(light_specular));
	   gl.glLightfv (GL10.GL_LIGHT0, GL10.GL_POSITION, FloatBuffer.wrap(light_position));
	   */
		/*
	   this.getGL10().glEnable (GL10.GL_LIGHTING);
	   this.getGL10().glEnable (GL10.GL_LIGHT0);
	   this.getGL10().glEnable(GL10.GL_DEPTH_TEST);
	   */
	}
	
    protected void initGLBackup(GL10 gl) {
    	/* Enable lights */

    	gl.glViewport(0, 0, getView().getWidth(), getView().getHeight());
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        float ratio = (float) getView().getWidth() / getView().getHeight();
        GLU.gluPerspective(gl, 45.0f, ratio, 1, 500f);
        
        /*
        gl.glFrontFace(GL10.GL_CW);
        gl.glShadeModel(GL10.GL_FLAT);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        */
        //gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        
        //gl.glEnable(GL10.GL_CULL_FACE);
        /*
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		*/
        /*
        gl.glEnable(GL10.GL_SMOOTH_LINE_WIDTH_RANGE);
        gl.glEnable(GL10.GL_SMOOTH_POINT_SIZE_RANGE);
        gl.glEnable(GL10.GL_SMOOTH);
        */
        /*
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
        
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		*/
        
        
        /*
		//gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepthf(1.0f);
		
		gl.glShadeModel(GL10.GL_FLAT);
        */
        //gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        //gl.glShadeModel(GL10.GL_SMOOTH);
        //gl.glEnable(GL10.GL_TEXTURE_2D);
        
    }
	
    /**
     * Enter the orthographic mode by first recording the current state, 
     * next changing us into orthographic projection.
     */
    public void enterOrtho() {
        // store the current state of the renderer
        GL10 gl = this.gl10;
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glPushMatrix();
        // now enter orthographic projection
        gl.glLoadIdentity();
        gl.glOrthof(0, this.getView().getWidth(), this.getView().getHeight(), 0, -1, 1);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glDisable(GL10.GL_LIGHTING);
    }

    /**
     * Leave the orthographic mode by restoring the state we store
     * in enterOrtho()
     * 
     * @see enterOrtho()
     */
    public void leaveOrtho() {
    	GL10 gl = this.gl10;
        // restore the state of the renderer
    	gl.glEnable(GL10.GL_LIGHTING);
        gl.glPopMatrix();
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glPopMatrix();
    }
	
	public final int[] getConfigSpec() {
		if (mTranslucentBackground) {
			// We want a depth buffer and an alpha buffer
			int[] configSpec = { EGL10.EGL_RED_SIZE, 8, EGL10.EGL_GREEN_SIZE,
					8, EGL10.EGL_BLUE_SIZE, 8, EGL10.EGL_ALPHA_SIZE, 8,
					EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
			return configSpec;
		} else {
			// We want a depth buffer, don't care about the
			// details of the color buffer.
			int[] configSpec = { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
			return configSpec;
		}
	}

	float eye_z, frust_near, frust_far;

	
    public void sizeChanged(int w, int h) {
    	
    	this.gl10.glViewport(0, 0,w,h);
    	this.gl10.glMatrixMode(GL10.GL_PROJECTION);
    	this.gl10.glLoadIdentity();
   	    if (w <= h)
   	    	this.gl10.glOrthof(-2.5f, 2.5f, -2.5f*(float)h/(float)w,2.5f*(float)h/(float)w, -10.0f, 10.0f);
    	else
    		this.gl10.glOrthof (-2.5f*(float)w/(float)h,2.5f*(float)w/(float)h, -2.5f, 2.5f, -10.0f, 10.0f);
    	
    	this.gl10.glMatrixMode(GL10.GL_MODELVIEW);
    	this.gl10.glLoadIdentity();
    	this.gl10.glDisable(GL10.GL_DITHER);
    	   
    	/*
    	gl.glViewport (0, 0,  w,  h);
    	gl.glMatrixMode (GL10.GL_PROJECTION);
    	gl. glLoadIdentity();
    	GLU.gluPerspective(gl,40.0f, (float) w/(float) h, 1.0f, 20.0f);
    	gl.glMatrixMode(GL10.GL_MODELVIEW);
    	gl.glLoadIdentity();
    	*/
    	  
    }
	public final void surfaceCreated(GL10 gl) {
		
	}

}
