package net.elizeu.ats.scenes;

import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.map.tiled.Map;

public class MyMap extends Map {

	public MyMap(Renderer renderer, String fileName) {
		super(renderer, fileName);
	}

	@Override
	public float getMapProportion() {
		return 50.0f;
	}
	
	@Override
    public float getXProportion() {
    	return 2.8f;
    }

	@Override
    public float getYProportion() {
    	return -4.2f;
    }
	

}
