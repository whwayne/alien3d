package org.igs.android.ogl.engine.scene;

import java.util.ArrayList;
import java.util.List;

import org.igs.android.ogl.engine.ActivityConnector;
import org.igs.android.ogl.engine.Renderer;

public abstract class Scene extends ActivityConnector {

	private Renderer renderer;
	
	private List<SceneObject> sceneObjectList;
	
	public Scene(Renderer renderer) {
		super(renderer.getContext());
		this.renderer = renderer;
	}
	
	public Renderer getRenderer() {
		return this.renderer;
	}
	
	public final List<SceneObject> getSceneObjectList() {
		if (this.sceneObjectList == null) {
			this.setSceneObjectList(new ArrayList<SceneObject>());
		}
		return this.sceneObjectList;
	}
	
	public final void setSceneObjectList(List<SceneObject> sceneObjectList) {
		this.sceneObjectList = sceneObjectList;
	}
	
	public abstract void render(float delta);
	
	public abstract void update(float delta, float fps);
	
	public abstract void destroy();
	
	public abstract void enter();
	
	public abstract void leave();
	
}