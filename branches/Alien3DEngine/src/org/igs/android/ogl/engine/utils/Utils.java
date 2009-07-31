package org.igs.android.ogl.engine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL11;

import org.igs.android.ogl.engine.AndromedaException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils {

	public static final float PI_OVER_180 = (float)(Math.PI / 180.0f);
	
	public static float getAngle(float x1, float y1, float x2, float y2) {
		return (float)(Math.atan2(y2 - y1, x2 - x1) * 180 / Math.PI);
	}
	
	public static float gauss(float x, float deviation, float expvalue){
		return (float)Math.pow(Math.E, -0.5f * Math.pow((x-expvalue)/deviation, 2)); // e ^ (x^2)
	}
	
	public static int toFixed(float x) {
        return (int) (x * 65536);
    }

    public static int toFixed(int x) {
        return x * 65536;
    }

    public static InputStream getFileInputStream(String resName) throws IOException {
    	return Utils.class.getResourceAsStream(resName);
    }

    public static final Bitmap getAssetBitmap(String srcFilename, Context context) throws AndromedaException {
        try {
        	InputStream is = context.getResources().getAssets().open(srcFilename);
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            throw new AndromedaException(AndromedaException.ASSET_BITMAP_LOAD_EXCEPTION, "Error getting "+srcFilename+" from assets!\n"+e.getMessage());
        }
    }

	public static Bitmap loadBitmap(int id, Context context){
		 Bitmap bitmap = null;
		 InputStream is = null;
		 try {
			 is = context.getResources().openRawResource(id);
			 bitmap = BitmapFactory.decodeStream(is);
		 } finally {
			 try {
				 is.close();
			 } catch(IOException e) {
			 }
		 }
		 return bitmap;
	}
    
    public static final ByteBuffer getByteBuffer(Bitmap bmp) throws Exception {
        try {
    		ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight() * bmp.getWidth() * 4);
    		bb.order(ByteOrder.nativeOrder());
    		IntBuffer ib = bb.asIntBuffer();

    		for (int y = 0; y < bmp.getHeight(); y++)
    			for (int x = 0; x < bmp.getWidth(); x++) {
    				ib.put(bmp.getPixel(x, y));
    			}
    		ib.position(0);
    		bb.position(0);
    		return bb;
        } catch (Exception e) {
            throw new Exception("Error getting ByteBuffer from assets!\n"+e.getMessage());
        }
    }
    
	public static FloatBuffer allocateFloatBuffer(int capacity){
		ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asFloatBuffer();
	}

	public static ByteBuffer allocateByteBuffer(int capacity){
		ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb;
	}
	
	public static IntBuffer allocateInttBuffer(int capacity){
		ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asIntBuffer();
	}
	
	public static ShortBuffer allocateShortBuffer(int capacity){
		ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asShortBuffer();
	}
	
	public static void addVertex3f(FloatBuffer buffer,float x,float y,float z){
		buffer.put(x);
		buffer.put(y);
		buffer.put(z);
	}
	
	public static void addIndex(ShortBuffer buffer,int index1,int index2,int index3){
		buffer.put((short) index1);
		buffer.put((short) index2);
		buffer.put((short) index3);
	}
	
	public static void addCoord2f(FloatBuffer buffer,float x,float y){
		buffer.put(x);
		buffer.put(y);
	}
	
	public static void addColorf(FloatBuffer buffer,float r,float g,float b,float a){
		buffer.put(r);
		buffer.put(g);
		buffer.put(b);
		buffer.put(a);
	}

	/**
	 * Use FloatBuffer.wrap
	 * @param values
	 * @return
	 */
	public static FloatBuffer toFloatBufferPositionZero(float[] values) {
		ByteBuffer vbb = ByteBuffer.allocateDirect(values.length*4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer=vbb.asFloatBuffer();
        buffer.put(values);
        buffer.position(0);
		return buffer;
	}
	
	/**
	 * Use ShortBuffer.wrap
	 * @param values
	 * @return
	 */
	public static ShortBuffer toShortBuffer(short[] values) {
		ByteBuffer vbb = ByteBuffer.allocateDirect(values.length*2);
        vbb.order(ByteOrder.nativeOrder());
        ShortBuffer buffer=vbb.asShortBuffer();
        buffer.put(values);
        buffer.position(0);
		return buffer;
	}
	
	public static ByteBuffer toByteBuffer(byte[] values) {
		ByteBuffer mIndexBuffer2 = ByteBuffer.allocateDirect(values.length);
		mIndexBuffer2.put(values);
		mIndexBuffer2.position(0);
		return mIndexBuffer2;
	}

	public static void billboardCurrentMatrix(GL11 gl) {
		FloatBuffer modelview = allocateFloatBuffer(16 * 4);
		gl.glGetFloatv(GL11.GL_MODELVIEW_MATRIX , modelview);
		for (int i=0; i < 3; i++) {	
			for(int j = 0; j < 3; j++) {
				if ( i==j ) 
					modelview.put(i * 4 + j, 1.0f);
				else 
					modelview.put(i*4+j, 0.0f);
			}
		}
		gl.glLoadMatrixf(modelview);
	}

}
