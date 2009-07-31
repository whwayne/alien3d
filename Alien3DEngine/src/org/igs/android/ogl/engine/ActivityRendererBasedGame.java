package org.igs.android.ogl.engine;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.igs.android.ogl.engine.event.SensorEventHandler;
import org.igs.android.ogl.engine.event.TouchEventHandler;
import org.igs.android.ogl.engine.scene.Light;
import org.igs.android.ogl.engine.scene.Scene;
import org.igs.android.ogl.engine.scene.transiction.Transiction;

import android.app.Activity;
import android.hardware.SensorListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class ActivityRendererBasedGame extends Activity implements SensorListener {
	
	private GLSurfaceView mGLSurfaceView;
	
	private Renderer currentRenderer;
	
	private Hashtable<Long, Scene> sceneList;
	private Hashtable<Long, Light> lightList;
	
	private List<TouchEventHandler> touchEventList;
	private List<SensorEventHandler> sensorEventList;
	
	private Scene newScene;
	private Scene currentScene;
	private Transiction inTransiction;
	private Transiction outTransiction;
	
	/**
	 * Initialize the render List and return current renderer;
	 * @param renderList
	 */
	public abstract Scene initSceneList() throws AndromedaException;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeGame();
    }
    
    private void initializeGame() {
        mGLSurfaceView = new GLSurfaceView(this);
        this.currentRenderer = new GameRenderer(this, this, mGLSurfaceView, false);
        mGLSurfaceView.enterOnRenderer(this.currentRenderer);
        setContentView(mGLSurfaceView);
    }
    
    public void changeScene() {
		this.currentScene.leave();
    	this.currentScene = newScene;
    	this.currentScene.enter();
    	newScene = null;
    }

    public void enterOnScene(Scene scene, Transiction in, Transiction out) {
    	this.setInTransiction(in);
    	this.setOutTransiction(out);
    	if (this.currentScene == null) {
    		this.currentScene = scene;
    		this.currentScene.enter();
    	} else {
    		newScene = scene;
    	}
    }

    public void enterOnScene(Long id, Transiction in, Transiction out) throws AndromedaException {
    	this.setInTransiction(in);
    	this.setOutTransiction(out);
    	Scene scene = this.getScene(id);
    	if (this.currentScene == null) {
    		this.currentScene = scene;
    		this.currentScene.enter();
    	} else {
    		newScene = scene;
    	}
    }
    
    public Scene getCurrentScene() {
    	return this.currentScene;
    }
    
    public Transiction getInTransiction() {
    	return this.inTransiction;
    }

    public void setInTransiction(Transiction in) {
    	this.inTransiction = in;
    }
    
    public Transiction getOutTransiction() {
    	return this.outTransiction;
    }

    public void setOutTransiction(Transiction out) {
    	this.outTransiction = out;
    }
    
    public Enumeration<Light> getLightListEnum() {
    	return this.getLightList().elements();
    }
    
    private final Hashtable<Long, Light> getLightList() {
    	if (this.lightList == null) {
    		this.setLightList(new Hashtable<Long, Light>(1));
    	}
    	return this.lightList;
    }

    void addLight(Long id, Light light) {
    	this.getLightList().put(id, light);
    }
    
    private final void setLightList(Hashtable<Long, Light> lightList) {
    	this.lightList = lightList;
    }
    
    final Light getLight(Long id) throws AndromedaException {
    	if (this.getLightList().containsKey(id)) {
    		return this.getLightList().get(id);
    	} else {
    		throw new AndromedaException(AndromedaException.LIGHT_NOT_FOUND_EXCEPTION);
    	}
    }
    
    final void removeLight(Long id) {
    	if (this.getLightList().containsKey(id)) {
    		this.getLightList().remove(id);
    	}
    }

    final boolean lightExists(Long id) {
    	return this.getLightList().containsKey(id);
    }
    
    public final boolean sceneExists(Long id) {
    	return this.getSceneList().containsKey(id);
    }
    
    public final Scene getScene(Long id) throws AndromedaException {
    	if (this.getSceneList().containsKey(id)) {
    		return this.getSceneList().get(id);
    	} else {
    		throw new AndromedaException(AndromedaException.SCENE_NOT_FOUND_EXCEPTION);
    	}
    }
    
    public final void removeScene(Long id) {
    	if (this.getSceneList().containsKey(id)) {
    		this.getSceneList().remove(id);
    	}
    }

    public final void removeAndDestroyScene(Long id) {
    	if (this.getSceneList().containsKey(id)) {
    		Scene scene = this.getSceneList().get(id);
    		this.getSceneList().remove(id);
    		scene.destroy();
    	}
    }
    
    public final void addScene(Long id, Scene scene) {
    	this.getSceneList().put(id, scene);
    }
    
    public Enumeration<Scene> getSceneListEnum() {
    	return this.getSceneList().elements();
    }
    
    private final Hashtable<Long, Scene> getSceneList() {
    	if (this.sceneList == null) {
    		this.setSceneList(new Hashtable<Long, Scene>(2));
    	}
    	return this.sceneList;
    }
    
    private final void setSceneList(Hashtable<Long, Scene> sceneList) {
    	this.sceneList = sceneList;
    }

    /* Touch handler events */
	private final List<TouchEventHandler> getTouchEventList() {
		if (this.touchEventList == null) {
			this.setTouchEventList(new ArrayList<TouchEventHandler>(5));
		}
		return this.touchEventList;
	}
	
	private void setTouchEventList(ArrayList<TouchEventHandler> touchEventList) {
		this.touchEventList = touchEventList;
		
	}
	
	void addTouchEvent(TouchEventHandler touchEvent) {
		this.getTouchEventList().add(touchEvent);
	}

	void removeTouchEvent(TouchEventHandler touchEvent) {
		if (this.getTouchEventList().contains(touchEvent)) {
			this.getTouchEventList().remove(touchEvent);
		}
	}

    /* Sendor handler events */
	private final List<SensorEventHandler> getSensorEventList() {
		if (this.sensorEventList == null) {
			this.setSensorEventList(new ArrayList<SensorEventHandler>(5));
		}
		return this.sensorEventList;
	}
	
	private void setSensorEventList(ArrayList<SensorEventHandler> sensorEventList) {
		this.sensorEventList = sensorEventList;
		
	}
	
	void addSensorEvent(SensorEventHandler sensorEvent) {
		this.getSensorEventList().add(sensorEvent);
	}

	void removeSensorEvent(SensorEventHandler sensorEvent) {
		if (this.getSensorEventList().contains(sensorEvent)) {
			this.getSensorEventList().remove(sensorEvent);
		}
	}
	
    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
    	if (mGLSurfaceView != null) {
    		mGLSurfaceView.onResume();
    	}
    }

    @Override
    protected void onStop() {
        super.onStop();
    }    
    
    //TODO Hack
    protected void onDestroy() {
    	if (mGLSurfaceView != null) {
    		mGLSurfaceView.onDestroy();
    	} else {
    		super.onDestroy();
    	}
    }
    
    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        if (mGLSurfaceView != null) {
        	mGLSurfaceView.onPause();
        }
    }

	public Renderer getCurrentRenderer() {
		return this.currentRenderer;
	}

	public GLSurfaceView getSurfaceView() {
		return this.mGLSurfaceView;
	}
	
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	Keyboard.setKeyDown(keyCode);
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	Keyboard.setKeyUp(keyCode);
        return super.onKeyUp(keyCode, event);
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
    	return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
    	for (TouchEventHandler touch : this.getTouchEventList()) {
    		touch.onTouchEvent(event);
    	}
    	return super.onTouchEvent(event);
    }

    /**
     * 
     * The method of the interface SensorListener
     * 
     */
    public void onSensorChanged(int sensor, float[] values) {
    	for (SensorEventHandler sensorEvent : this.getSensorEventList()) {
    		sensorEvent.onSensorChanged(sensor, values);
    	}
    }

    /**
     * 
     * The method of the interface SensorListener
     * 
     */
    public void onAccuracyChanged(int sensor, int accuracy) {
    	for (SensorEventHandler sensorEvent : this.getSensorEventList()) {
    		sensorEvent.onAccuracyChanged(sensor, accuracy);
    	}
    }
    
}