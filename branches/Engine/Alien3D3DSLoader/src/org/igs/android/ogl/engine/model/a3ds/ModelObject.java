package org.igs.android.ogl.engine.model.a3ds;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import org.igs.android.ogl.engine.math.GameMath;
import org.igs.android.ogl.engine.math.Point2f;
import org.igs.android.ogl.engine.math.Point3f;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * This class is used to keep the data of object.
 * 
 */
public final class ModelObject implements Cloneable {

	public int numOfVerts; // number of vertex
	public int numOfFaces; // number of faces
	public int numTexVertex; // number of coordinate
	public int materialID; // material's ID
	public boolean bHasTexture; // is there any texture?
	public String strName; // name of object
	public Point3f[] Verts; // vertex
	public Point3f[] Normals; // normals of vertex
	public Point2f[] TexVerts; // UV coordinate
	public tFace[] Faces; // faces's vertex index

	static public int toFixed(float x) {
		return (int) (x * 65536.0f);
	}

	/**
	 * create the buffer for render according the data imported
	 */
	public void createBuffer() {
		ByteBuffer bb = ByteBuffer.allocateDirect(numOfVerts * 3 * 4);
		bb.order(ByteOrder.nativeOrder());
		mVertexBuffer = bb.asIntBuffer();

		bb = ByteBuffer.allocateDirect(numTexVertex * 2 * 4);
		bb.order(ByteOrder.nativeOrder());
		mTexVertBuffer = bb.asFloatBuffer();

		bb = ByteBuffer.allocateDirect(numOfFaces * 3 * 2);
		bb.order(ByteOrder.nativeOrder());
		mIndexBuffer = bb.asShortBuffer();

		for (int index = 0; index < numOfVerts; index++) {
			mVertexBuffer.put(toFixed(Verts[index].x));
			mVertexBuffer.put(toFixed(Verts[index].y));
			mVertexBuffer.put(toFixed(Verts[index].z));
		}
		for (int index = 0; index < numTexVertex; index++) {
			mTexVertBuffer.put(TexVerts[index].x);
			mTexVertBuffer.put(TexVerts[index].y);
		}
		for (int index = 0; index < numOfFaces; index++) {
			mIndexBuffer.put((short) Faces[index].verIndex[0]);
			mIndexBuffer.put((short) Faces[index].verIndex[1]);
			mIndexBuffer.put((short) Faces[index].verIndex[2]);
		}
	}

	public void createVertexBuffer() {
		ByteBuffer bb = ByteBuffer.allocateDirect(numOfVerts * 3 * 4);
		bb.order(ByteOrder.nativeOrder());
		mVertexBuffer = bb.asIntBuffer();
		for (int index = 0; index < numOfVerts; index++) {
			mVertexBuffer.put(toFixed(Verts[index].x));
			mVertexBuffer.put(toFixed(Verts[index].y));
			mVertexBuffer.put(toFixed(Verts[index].z));
		}
	}

	public Point3f getCenter() {
		float BIG_NUMBER = 1e37f;
		Point3f center = new Point3f();
		Point3f leftLower = new Point3f(BIG_NUMBER, BIG_NUMBER, -BIG_NUMBER);
		Point3f rightUpper = new Point3f(-BIG_NUMBER, -BIG_NUMBER, +BIG_NUMBER);

		for (int index = 0; index < this.numOfVerts; index++) {
			if (this.Verts[index].x < leftLower.x) // get the minimize on x axis
			{
				leftLower.x = this.Verts[index].x;
			} else if (this.Verts[index].x > rightUpper.x) // get the maximize
															// on x axis
			{
				rightUpper.x = this.Verts[index].x;
			}
			if (this.Verts[index].y < leftLower.y) // get the minimize on y axis
			{
				leftLower.y = this.Verts[index].y;
			} else if (this.Verts[index].y > rightUpper.y) // get the maximize
															// on y axis
			{
				rightUpper.y = this.Verts[index].y;
			}
			if (this.Verts[index].z < rightUpper.z) // get the minimize on z
													// axis
			{
				rightUpper.z = this.Verts[index].z;
			} else if (this.Verts[index].z > leftLower.z) // get the maximize on
															// z axis
			{
				leftLower.z = this.Verts[index].z;
			}
		}

		center.x = (leftLower.x + rightUpper.x) / 2;
		center.y = (leftLower.y + rightUpper.y) / 2;
		center.z = (leftLower.z + rightUpper.z) / 2;

		System.out.println("wheel's " + center.toString());
		return center;
	}

	public void resetVertice(Point3f point) {
		Point3f center = getCenter();
		System.out.println(center.toString());
		for (int index = 0; index < this.numOfVerts; index++) {
			// Verts[index].x = Verts[index].x - center.x + point.x;
			// Verts[index].y = Verts[index].y - center.y + point.y;
			// Verts[index].z = Verts[index].z - center.z + point.z;
			Verts[index].x = Verts[index].x - center.x;
			Verts[index].y = Verts[index].y - center.y;
			Verts[index].z = Verts[index].z - center.z;
		}
	}

	public void loadBitmap(GL10 gl, InputStream is) {
		Bitmap bitmap = BitmapFactory.decodeStream(is);
		int pic_width = bitmap.getWidth();
		int pic_height = bitmap.getHeight();

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(pic_width
				* pic_height * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		IntBuffer intBuffer = byteBuffer.asIntBuffer();

		int color;
		byte[] argb;
		for (int y = 0; y < pic_height; y++) {
			for (int x = 0; x < pic_width; x++) {
				/*
				 * Ã¿Ò»¸öÍ¼ÏñÏñËØÍ¨¹ýÒ»¸ö4×Ö½ÚÕûÊýÀ´Õ¹ÏÖ¡£
				 * ×î¸ßÎ»×Ö½ÚÓÃ×÷alphaÍ¨µÀ£»»»ÑÔÖ®ÓÃÀ´ÊµÏÖÍ¸Ã÷/²»Í¸Ã÷¿ØÖÆ¡£
				 * 255´ú±íÍêÈ«²»Í¸Ã÷£»0Ôò´ú±íÍêÈ«Í¸Ã÷¡£
				 * ½ÓÏÂÀ´Ò»¸ö×Ö½ÚÊÇredºìÉ«Í¨µÀ£»255´ú±íÍêÈ«ÊÇºìÉ«¡£
				 * ÒÀ´ÎÀàÍÆ½ÓÏÂÀ´Á½¸ö×Ö½ÚÏàÓ¦µÄÊµÏÖÂÌÉ«ºÍÀ¶É«Í¨µÀ¡£
				 */
				color = bitmap.getPixel(x, y);
				argb = GameMath.int2byte(color, GameMath.BIG_ENDIAN);
				if (argb[1] == -64 && argb[2] == 0 && argb[3] == -64) // ¼´11000000,00000000,11000000,·ÛºìÉ«
				{
					argb[0] = 0;
				}
				intBuffer.put(GameMath.byte2int(argb, GameMath.BIG_ENDIAN));
			}
		}
		// for (int y = 0; y < pic_height; y++)
		// {
		// for (int x = 0; x < pic_width; x++)
		// {
		// intBuffer.put(bitmap.getPixel(x, y));
		// }
		// }

		for (int i = 0; i < pic_width * pic_height * 4; i += 4) {
			byte temp = byteBuffer.get(i);
			byteBuffer.put(i, byteBuffer.get(i + 2));
			byteBuffer.put(i + 2, temp);

		}
		byteBuffer.position(0);
		intBuffer.position(0);

		gl.glGenTextures(1, textureID, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID[0]);
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, pic_width,
				pic_height, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, byteBuffer);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);
	}

	public void draw(GL10 gl) {
		mTexVertBuffer.position(0);
		mVertexBuffer.position(0);
		mIndexBuffer.position(0);

		if (textureID[0] != -1) {
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID[0]);
		}
		gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexVertBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, numOfFaces * 3,
				GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
	}

	private IntBuffer mVertexBuffer;
	private FloatBuffer mTexVertBuffer;
	private ShortBuffer mIndexBuffer;

	private int[] textureID = new int[] { -1 };
};

/**
 * This class is used to keep the vertex index and coordinate index.
 */
class tFace implements Cloneable {
	public int[] verIndex; // vertex index
	public int[] coordIndex; // coordinate index

	public tFace() {
		verIndex = new int[3];
		coordIndex = new int[2];
	}

	public Object clone() {
		tFace face = null;
		try {
			face = (tFace) super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println(e.toString());
		}
		face.verIndex = verIndex.clone();
		face.coordIndex = coordIndex.clone();
		return face;
	}
}
