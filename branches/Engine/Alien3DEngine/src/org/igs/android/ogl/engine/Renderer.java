package org.igs.android.ogl.engine;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.view.SurfaceView;

/**
 * A generic renderer interface.
 */
public interface Renderer {

	boolean initializeResources() throws AndromedaException;
	
    /**
     * @return the EGL configuration specification desired by the renderer.
     */
    int[] getConfigSpec();

    /**
     * Surface created.
     * Called when the surface is created. Called when the application
     * starts, and whenever the GPU is reinitialized. This will
     * typically happen when the device awakes after going to sleep.
     * Set your textures here.
     */
    void surfaceCreated(GL10 gl);
    /**
     * Surface changed size.
     * Called after the surface is created and whenever
     * the OpenGL ES surface size changes. Set your viewport here.
     * @param gl
     * @param width
     * @param height
     */
    void sizeChanged(int width, int height);
    /**
     * Draw the current frame.
     * @param gl
     */
    void update(float delta, float fps);
    
    void render(float delta);
    
    SurfaceView getView();
    
    Context getContext();
    
    GL10 getGL10();
    
    void setGL10(GL10 gl);
    	
    void initGL();
    
    void leaveOrtho();
    
    void enterOrtho();
    
}
