package net.elizeu.ats;

import net.elizeu.ats.scenes.InGameScene;

import org.igs.android.ogl.engine.ActivityRendererBasedGame;
import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.scene.Scene;

/**
 * 
 * @author Elizeu Nogueira da Rosa Jr.
 * @version 0.1
 * @since 20.05.2009
 *
 */
public class AgeAnimationSamples extends ActivityRendererBasedGame {

	@Override
	public Scene initSceneList() throws AndromedaException {
		InGameScene inGameScene = new InGameScene(getCurrentRenderer());
		addScene(InGameScene.ID, inGameScene);
		return inGameScene;

	}

}