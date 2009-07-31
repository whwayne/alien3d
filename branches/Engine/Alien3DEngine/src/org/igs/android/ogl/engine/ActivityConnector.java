package org.igs.android.ogl.engine;

import org.igs.android.ogl.engine.event.SensorEventHandler;
import org.igs.android.ogl.engine.event.TouchEventHandler;
import org.igs.android.ogl.engine.scene.Light;
import org.igs.android.ogl.engine.scene.Scene;
import org.igs.android.ogl.engine.scene.transiction.Transiction;

import android.content.Context;

public class ActivityConnector {

	private ActivityRendererBasedGame activity;
	
	public ActivityConnector(Context context) {
		this.activity = (ActivityRendererBasedGame)context;
	}
	
	public void enterOnScene(Scene scene, Transiction in, Transiction out) {
		this.activity.enterOnScene(scene, in, out);
	}

	public void enterOnScene(Long id, Transiction in, Transiction out) throws AndromedaException {
		this.activity.enterOnScene(id, in, out);
	}
	
	public void addLight(Long id, Light light) {
		this.activity.addLight(id, light);
	}
	
    public final Light getLight(Long id) throws AndromedaException {
    	return activity.getLight(id);
    }
    
    public final void removeLight(Long id) {
    	activity.removeLight(id);
    }

    public final boolean lightExists(Long id) {
    	return this.activity.lightExists(id);
    }
	
	public void addTouchEvent(TouchEventHandler touchEvent) {
		this.activity.addTouchEvent(touchEvent);
	}

	public void removeTouchEvent(TouchEventHandler touchEvent) {
		this.activity.removeTouchEvent(touchEvent);
	}

	public void addSensorEvent(SensorEventHandler sensorEvent) {
		this.activity.addSensorEvent(sensorEvent);
	}

	public void removeSensorEvent(SensorEventHandler sensorEvent) {
		this.activity.removeSensorEvent(sensorEvent);
	}
	
}
