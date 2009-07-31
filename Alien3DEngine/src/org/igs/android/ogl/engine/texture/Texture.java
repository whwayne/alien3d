package org.igs.android.ogl.engine.texture;

import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.scene.SceneObject.TransparencyEnum;
import org.igs.android.ogl.engine.utils.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class Texture {

	private int texId;
	
	private int width;
	private int height;
	
	private TransparencyEnum transparencyType;

	Texture() {
		
	}

	public TransparencyEnum getTransparencyType() {
		return transparencyType;
	}

	public void setTransparencyType(TransparencyEnum transparencyType) {
		this.transparencyType = transparencyType;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/*
	void loadTexture(Context context, GL10 gl, String name) throws Exception {
		Bitmap bmp = Utils.getAssetBitmap(name, context);
        int[] size = new int[]{bmp.getWidth(), bmp.getHeight()};
        ByteBuffer byteBuffer = Utils.getByteBuffer(bmp);
        byteBuffer.position(0);

        int[] tmp_tex = new int[1];
        
        gl.glGenTextures(1, tmp_tex, 0);
        texId = tmp_tex[0];
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, size[0], size[1], 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, byteBuffer);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	}
	*/
	
	void loadTexture(Context context, GL10 gl, String name) throws AndromedaException {
		try {
			gl.glEnable(GL10.GL_TEXTURE_2D);
	        int[] textures = new int[1];
	        gl.glGenTextures(1, textures, 0);
	
	        texId = textures[0];
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
	
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
	
	        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
	
	        Bitmap bmp = Utils.getAssetBitmap(name, context);
	
	        this.width = bmp.getWidth();
	        this.height = bmp.getHeight();
	        
	        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
	        bmp.recycle();
		} catch (Exception ex) {
			throw new AndromedaException(AndromedaException.TEXTURE_CREATE_EXCEPTION, ex.getMessage());
		}
	}

	void loadTexture(Bitmap bmp, GL10 gl) throws AndromedaException {
		try {
			gl.glEnable(GL10.GL_TEXTURE_2D);
	        int[] textures = new int[1];
	        gl.glGenTextures(1, textures, 0);
	
	        texId = textures[0];
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
	
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
	
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
	        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
	
	        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);
	
	        this.width = bmp.getWidth();
	        this.height = bmp.getHeight();
	        
	        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
	        bmp.recycle();
		} catch (Exception ex) {
			throw new AndromedaException(AndromedaException.TEXTURE_CREATE_EXCEPTION, ex.getMessage());
		}
	}
	
	public void bind(GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
	}

	void loadColorTexture(GL10 gl, byte r, byte g, byte b) {
		ByteBuffer byteBuffer = Utils.allocateByteBuffer(12);
		//unsigned char data[12];	// a 2x2 texture at 24 bits

		// Store the data
		for(int i = 0; i < 12; i += 3) {
			byteBuffer.put(i, r);
			byteBuffer.put(i + 1, g);
			byteBuffer.put(i + 2, b);
			/*
			data[i] = r;
			data[i+1] = g;
			data[i+2] = b;
			*/
		}

		gl.glEnable(GL10.GL_TEXTURE_2D);
        int[] textures = new int[1];
        // Generate the OpenGL texture id
        gl.glGenTextures(1, textures, 0);
		
		// Bind this texture to its id
        texId = textures[0];
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

		//gl.glPixelStorei(GL10.GL_UNPACK_ALIGNMENT, 1);

		/*
		if (!game_option_mipmap) // <CUSTOM> if you don't use an mipmap option, delete this statement
		{
			// Not use mipmapping filter
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
			glTexImage2D(GL_TEXTURE_2D, 0, 3, 2, 2, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
		}
		else
		{
		*/
			// Use mipmapping filter
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, 3, 2, 2, 0, GL10.GL_RGB, GL10.GL_BYTE, byteBuffer);
			// Generate the texture
			//gl.gluBuild2DMipmaps(GL_TEXTURE_2D, 3, 2, 2, GL_RGB, GL_UNSIGNED_BYTE, data);
		//}
		
	}	
}
