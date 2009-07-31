package net.elizeu.ass;

import net.elizeu.ass.scenes.InGameScene;

import org.igs.android.ogl.engine.ActivityRendererBasedGame;
import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.scene.Scene;

public class AgeShapeSamples extends ActivityRendererBasedGame {

	@Override
	public Scene initSceneList() throws AndromedaException {
		InGameScene inGameScene = new InGameScene(getCurrentRenderer());
		addScene(InGameScene.ID, inGameScene);
		return inGameScene;
	}

}