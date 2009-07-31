package net.age.mvs.scene;

import org.igs.android.ogl.engine.Keyboard;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.model.a3ds.Model;
import org.igs.android.ogl.engine.model.a3ds.ModelImport;
import org.igs.android.ogl.engine.scene.Scene;

import android.util.Log;

public class ModelViewScene extends Scene {

	/* Represents a model */
	private Model model;
	
	/* 3DS Model Importer */
	private ModelImport mi;
	
	public ModelViewScene(Renderer renderer) {
		super(renderer);
		mi = new ModelImport();
	}
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void enter() {
		try {
			/* Create a model with initial parameter */
			model = new Model("", null, getRenderer(), new Point3f(0f, 0f, 0f), new Point3f(0f, 0f, 0f), new Point3f(0.3f, 0.3f, 0.3f));
			/* Import a Model */
			mi.import3DS(getRenderer(), model, "Fighter1.3ds");
			/* Generate Vertex/Normals/Faces/Texture Coords Buffers*/
			model.generate();
		} catch (Exception ex) {
			Log.e("ModelLoaderException", ex.getMessage());
		}
	}

	@Override
	public void leave() {
		
	}

	@Override
	public void render(float delta) {
		/* Render a Module */
		getRenderer().getGL10().glRotatef(45, 1, 0, 0);
		
		model.render(delta);
	}

	@Override
	public void update(float delta, float fps) {
		if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_DOWN)) {
			/* Rotate X Axis */
			model.addRotation(1f, 0, 0);
		} else
		if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_UP)) {
			/* Rotate X Axis */
			model.addRotation(-1f, 0, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_LEFT)) {
			/* Rotate Y Axis */
			model.addRotation(0, 0, -1f);
		} else
		if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_RIGHT)) {
			/* Rotate Y Axis */
			model.addRotation(0, 0, 1f);
		}
		
	}

}
