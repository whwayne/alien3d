package net.age.mvs;

import net.age.mvs.scene.ModelViewScene;

import org.igs.android.ogl.engine.ActivityRendererBasedGame;
import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.scene.Scene;

/**
 *
 * 3DS Model Loader Sample
 * 
 * @author Elizeu Nogueira da Rosa Jr.
 * @version 0.1
 * @since 27.05.2009
 *
 */
public class ModelViewSample extends ActivityRendererBasedGame {

	@Override
	/* Super class scene list initialization*/
	public Scene initSceneList() throws AndromedaException {
		/* Create a renderable scene*/
		ModelViewScene scene = new ModelViewScene(getCurrentRenderer());
		/* Add a scene into world */
		this.addScene(1L, scene);
		/* Return a initial game scene */
		return scene;
	}

}