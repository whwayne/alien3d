package org.igs.android.ogl.engine.texture;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.AndromedaException;

import android.content.Context;
import android.graphics.Bitmap;

public class TextureBuffer {

	private static TextureBuffer instance;
	
	public Map<String, Texture> textureMap;
	
	private TextureBuffer() {
		textureMap = new HashMap<String, Texture>();
	}
	
	/**
	 * 
	 * @return Singleton TextureBuffer instance
	 * 
	 */
	public static synchronized TextureBuffer getInstance() {
		if (instance == null) {
			instance = new TextureBuffer();
		}
		return instance;
	}

	/**
	 * 
	 * @param context Activity context
	 * @param gl GL10 
	 * @param name Texture path in assets
	 * @return Texture 
	 * @throws Exception
	 * 
	 */
	public Texture getTexture(Context context, GL10 gl, String name) throws AndromedaException {
		if (textureMap.containsKey(name)) {
			return textureMap.get(name);
		} else {
			Texture tx = new Texture();
			tx.loadTexture(context, gl, name);
			textureMap.put(name, tx);
			return tx;
		}
	}

	public Texture getTextureNoBuffered(Bitmap bitmap, GL10 gl) throws AndromedaException {
		Texture tx = new Texture();
		tx.loadTexture(bitmap, gl);
		return tx;
	}

	public Texture getTextureByRgbColor(GL10 gl, byte r, byte g, byte b) throws AndromedaException {
		Texture tx = new Texture();
		tx.loadColorTexture(gl, r, g, b);
		return tx;
	}
	
}
