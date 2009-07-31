package org.igs.android.ogl.engine.scene.transiction;

import org.igs.android.ogl.engine.Renderer;

public abstract class Transiction {
	
	private Renderer renderer;
	
	public Transiction(Renderer renderer) {
		this.renderer = renderer;
	}
	
	protected Renderer getRenderer() {
		return this.renderer;
	}
	
	public abstract void render(float delta);
	
	public abstract boolean isComplete();
	
	public abstract void init();
	
}
