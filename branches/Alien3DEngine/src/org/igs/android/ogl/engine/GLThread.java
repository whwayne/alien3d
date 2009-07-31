package org.igs.android.ogl.engine;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;
import android.view.SurfaceHolder;

/**
 * A generic GL Thread. Takes care of initializing EGL and GL. Delegates
 * to a Renderer instance to do the actual drawing.
 *
 */
public class GLThread extends Thread {
	
	private static final Semaphore sEglSemaphore = new Semaphore(1);
	private boolean mSizeChanged = true;
	private GLWrapper mGLWrapper;
	private SurfaceHolder mHolder;

	private boolean isRendererResourceInitialized = false;; 
	
    /** The time at which the last fps was recorded */
    private long lastFPS;
    /** The time since the last frame */
    private long lastFrame;
    /** The frames per second we're currently running at */
    private int fps;
    /** The remainder of time left over from last frame */
    //private int remainder = 0;
    /** The recorded frames per second */
    private int recordedFPS;
    
    public GLThread(Renderer renderer, GLWrapper mGLWrapper, SurfaceHolder mHolder) {
        super();
        this.mGLWrapper = mGLWrapper;
        this.mHolder = mHolder;
        mDone = false;
        mWidth = 0;
        mHeight = 0;
        mRenderer = renderer;
        setName("AGEThread");
    }

    @Override
    public void run() {
        /*
         * When the android framework launches a second instance of
         * an activity, the new instance's onCreate() method may be
         * called before the first instance returns from onDestroy().
         *
         * This semaphore ensures that only one instance at a time
         * accesses EGL.
         */
        try {
            try {
            sEglSemaphore.acquire();
            } catch (InterruptedException e) {
                return;
            }
            guardedRun();
        } catch (AndromedaException ex) {
        	Log.e("AGEThread", ex.getMessage());
        } catch (InterruptedException ex) {
        	Log.e("AGEThread", ex.getMessage());
        } finally {
            sEglSemaphore.release();
        }
    }

    private void guardedRun() throws InterruptedException, AndromedaException {
        mEglHelper = new EglHelper(this.mGLWrapper);
        /*
         * Specify a configuration for our opengl session
         * and grab the first configuration that matches is
         */
        int[] configSpec = mRenderer.getConfigSpec();
        mEglHelper.start(configSpec);

        GL10 gl = null;
        boolean tellRendererSurfaceCreated = true;
        boolean tellRendererSurfaceChanged = true;

        
        lastFPS = System.currentTimeMillis();
        lastFrame = System.currentTimeMillis();
        
        /*
         * This is our main activity thread's loop, we go until
         * asked to quit.
         */
        while (!mDone) {

        	//int timeStep = 10;
        	
            int delta = (int) (System.currentTimeMillis() - lastFrame);
            lastFrame = System.currentTimeMillis();

            //int steps = delta / timeStep;
            //remainder = delta % timeStep;
        	
            /*
             *  Update the asynchronous state (window size)
             */
            int w, h;
            boolean changed;
            boolean needStart = false;
            
            synchronized (this) {
            	
                Runnable r;
                while ((r = getEvent()) != null) {
                    r.run();
                }
                
                if (mPaused) {
                    mEglHelper.finish();
                    needStart = true;
                }
                
                if(needToWait()) {
                    while (needToWait()) {
                        wait();
                    }
                }
                
                if (mDone) {
                    break;
                }
                changed = mSizeChanged;
                w = mWidth;
                h = mHeight;
                mSizeChanged = false;
            }
            
            if (needStart) {
                mEglHelper.start(configSpec);
                tellRendererSurfaceCreated = true;
                changed = true;
            }
            if (changed) {
                gl = (GL10) mEglHelper.createSurface(mHolder);
                tellRendererSurfaceChanged = true;
                //TODO Remove this line
                changed = false;
            }
            if (tellRendererSurfaceCreated) {
                mRenderer.surfaceCreated(gl);
                tellRendererSurfaceCreated = false;
            }
            if (tellRendererSurfaceChanged) {
            	mRenderer.setGL10(gl);            	
                mRenderer.sizeChanged(w, h);
                tellRendererSurfaceChanged = false;
            }
            if (! isRendererResourceInitialized) {

            	mRenderer.initGL();
            	isRendererResourceInitialized = mRenderer.initializeResources();
            }
            if ((w > 0) && (h > 0) && isRendererResourceInitialized) {
            	
            	mRenderer.update(delta, recordedFPS);
            	
                if (System.currentTimeMillis() - lastFPS > 1000) {
                    lastFPS = System.currentTimeMillis();
                    recordedFPS = fps;
                    fps = 0;
                }
                fps++;
            	
                /* draw a frame here */
                mRenderer.render(delta);

                /*
                 * Once we're done with GL, we need to call swapBuffers()
                 * to instruct the system to display the rendered frame
                 */
                mEglHelper.swap();
            }
         }

        /*
         * clean-up everything...
         */
        mEglHelper.finish();
    }

    private boolean needToWait() {
        return (mPaused || (! mHasFocus) || (! mHasSurface) || mContextLost)
            && (! mDone);
    }

    public void surfaceCreated() {
        synchronized(this) {
            mHasSurface = true;
            mContextLost = false;
            notify();
        }
    }

    public void surfaceDestroyed() {
        synchronized(this) {
            mHasSurface = false;
            notify();
        }
    }

    public void onPause() {
        synchronized (this) {
            mPaused = true;
        }
    }

    public void onResume() {
        synchronized (this) {
            mPaused = false;
            notify();
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        synchronized (this) {
            mHasFocus = hasFocus;
            if (mHasFocus == true) {
                notify();
            }
        }
    }
    public void onWindowResize(int w, int h) {
        synchronized (this) {
            mWidth = w;
            mHeight = h;
            mSizeChanged = true;
        }
    }

    public void requestExitAndWait() {
        // don't call this from GLThread thread or it is a guaranteed
        // deadlock!
        synchronized(this) {
            mDone = true;
            notify();
        }
        /*
        try {
            join();
        	Thread.currentThread().interrupt();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        */
    }

    /**
     * Queue an "event" to be run on the GL rendering thread.
     * @param r the runnable to be run on the GL rendering thread.
     */
    public void queueEvent(Runnable r) {
        synchronized(this) {
            mEventQueue.add(r);
        }
    }

    private Runnable getEvent() {
        synchronized(this) {
            if (mEventQueue.size() > 0) {
                return mEventQueue.remove(0);
            }

        }
        return null;
    }

    private boolean mDone;
    private boolean mPaused;
    private boolean mHasFocus;
    private boolean mHasSurface;
    private boolean mContextLost;
    private int mWidth;
    private int mHeight;
    private Renderer mRenderer;
    private ArrayList<Runnable> mEventQueue = new ArrayList<Runnable>();
    private EglHelper mEglHelper;
}
