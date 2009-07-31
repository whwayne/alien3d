package org.igs.android.ogl.engine;

import java.util.Enumeration;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.AbstractRenderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.Light;
import org.igs.android.ogl.engine.scene.SceneObject.TransparencyEnum;
import org.igs.android.ogl.engine.sprite.Sprite;

import android.content.Context;
import android.view.SurfaceView;

/**
 * 
 * @author Elizeu Nogueira da Rosa Jr.
 * @version 0.1
 * @since 03.10.2009
 *
 */
public class GameRenderer extends AbstractRenderer {

	private ActivityRendererBasedGame gameActivity;
	
	private Sprite logo;
	
	private long initTime = 0L;
	private boolean inGame = false;
	
	public GameRenderer(ActivityRendererBasedGame gameActivity, Context context, SurfaceView view, boolean useTranslucentBackground) {
		super(context, view, useTranslucentBackground);
		this.gameActivity = gameActivity;
	}

	public boolean initializeResources() throws AndromedaException {
		super.initGL();
		this.gameActivity.enterOnScene(this.gameActivity.initSceneList(), null, null);
		logo = new Sprite("", null, this, "logo.bmp", super.getView().getWidth(), super.getView().getHeight(), new Point3f(0f, 0f, 0f), new Point3f(0f, 0f, 0f), new Point3f(1f, 1f, 1f), TransparencyEnum.NONE);
		return true;
	}
	
	public void render(float delta) {
		
		//Limpa o buffer de cores
		super.getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		//Set a pilha ativa
        super.getGL10().glMatrixMode(GL10.GL_MODELVIEW);
        //Limpa todas as transformaçõeses da pilha GL_MODELVIEW
        super.getGL10().glLoadIdentity();
        //Renderiza os objetos
        
        //Aqui é onde deveria estar a camera
        //super.getGL10().glRotatef(-45, 0, 1, 0);
        //GL11.glTranslatef(-player.getX(), -player.getY(), -50);

        if ((System.currentTimeMillis() - this.initTime) >= 2000) {
        	this.inGame = true;
        }
        
        if (inGame) {
	        Enumeration<Light> lightEnum = this.gameActivity.getLightListEnum(); 
	        
	        while (lightEnum.hasMoreElements()) {
	        	lightEnum.nextElement().render();
	        }
	
	        if ((this.gameActivity.getOutTransiction() == null) && (this.gameActivity.getInTransiction() == null)) {
	        	this.gameActivity.getCurrentScene().render(delta);
	        }
	        
	        if (this.gameActivity.getOutTransiction() != null) {
	            this.gameActivity.getOutTransiction().render(delta);
	
	            if (this.gameActivity.getOutTransiction().isComplete()) {
	                this.gameActivity.setOutTransiction(null);
	                this.gameActivity.changeScene();
	            }
	        } else if (this.gameActivity.getInTransiction() != null) {
	            this.gameActivity.getInTransiction().render(delta);
	            if (this.gameActivity.getInTransiction().isComplete()) {
	                this.gameActivity.setInTransiction(null);
	            }
	        }
        } else {
        	logo.renderOrtho(delta);
        }
	}

	public void update(float delta, float fps) {
		if (inGame) {
			this.gameActivity.getCurrentScene().update(delta, fps);
		} else {
			if (initTime == 0L) {
				initTime = System.currentTimeMillis();
			}
		}
	}

}
