package net.elizeu.android.aerolitos;

import net.elizeu.android.aerolitos.scenes.AerolitosScene;

import org.igs.android.ogl.engine.ActivityRendererBasedGame;
import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.scene.Scene;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @author Elizeu Nogueira da Rosa Jr.
 * @version 0.1
 * @since 03.10.2009
 *
 */
public class Aerolitos extends ActivityRendererBasedGame {

	@Override
	public Scene initSceneList() throws AndromedaException {
		
		AerolitosScene aerolitosScene = new AerolitosScene(this.getCurrentRenderer());
		this.addScene(AerolitosScene.ID, aerolitosScene);
		this.addScene(1L, aerolitosScene);
		return aerolitosScene;
	}

	public void testeToast(String string){
		 Context context = getApplicationContext();
		 CharSequence text = (CharSequence) string;
		 int duration = Toast.LENGTH_SHORT;
		 Toast toast = Toast.makeText(context, text, duration);
		 toast.show();
	}
	
}