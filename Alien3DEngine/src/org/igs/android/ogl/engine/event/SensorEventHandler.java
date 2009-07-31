package org.igs.android.ogl.engine.event;

public interface SensorEventHandler {

    void onSensorChanged(int sensor, float[] values);    

    void onAccuracyChanged(int sensor, int accuracy);
	
}
