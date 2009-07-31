package org.alien3d.sprite;

import javax.microedition.khronos.opengles.GL10;

import org.alien3d.Alien3DException;
import org.alien3d.World;
import org.alien3d.entity.Renderable;
import org.alien3d.entity.Transformable;
import org.alien3d.texture.TextureBuffer;
import org.alien3d.util.Utils;

import android.graphics.Bitmap;

public class SpriteSheet extends Renderable {

	private String fileName;

	private float width;
	private float height;
	
	
	private int frameWidth;
	private int frameHeight;
	private int spacing;
	
	private int frames;
	private int nextFrame = 0;
	
	private Sprite[] sprites;
	
	public SpriteSheet(String id, World world, String fileName, float width, float height, int frameWidth, int frameHeight, int spacing) throws Alien3DException {
		super(world);
		this.fileName = fileName;
		this.width = width;
		this.height = height;
		this.spacing = spacing;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.build();
	}

	public String getFileName() {
		return this.fileName;
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		this.width = width;
		super.setModified(true);
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
		super.setModified(true);
	}
	
	public int getSpacing() {
		return spacing;
	}
	
	public int getFrameCount() {
		return this.frames;
	}
	
	private void redefine() {
		for (Sprite sprite : this.sprites) {
			//sprite.setPosition(getPosition().x, getPosition().y, getPosition().z);
			//sprite.setRotation(getRotation().x, getRotation().y, getRotation().z);
			//sprite.setScale(getScale().x, getScale().y, getScale().z);
			
			/*		
			sprite.setPosition(getPosition());
			sprite.setRotation(getRotation());
			sprite.setScale(getScale());
			*/
			sprite.setWidth(width);
			sprite.setHeight(height);
			sprite.enableTransparency(true, transparencyType);
		}
	}
	
	private void build() throws Alien3DException {
		Bitmap sourceBitmap = Utils.getAssetBitmap(this.fileName, world.getContext());
		
		this.frames = ((sourceBitmap.getWidth() / frameWidth) * (sourceBitmap.getHeight() / frameHeight));
		this.sprites = new Sprite[this.frames];

		int spriteX = 0;
		int spriteY = 0;
		
		int index = 0;
		
		for (int i = 0; i < sourceBitmap.getHeight() / frameHeight; i++) {
			spriteX = 0;
			for (int j = 0; j < sourceBitmap.getWidth() / frameWidth; j++) {
				this.sprites[index] = new Sprite("", world, 
						TextureBuffer.getInstance().getTextureNoBuffered(Bitmap.createBitmap(sourceBitmap, spriteX, spriteY, frameWidth, frameHeight), world.getGL()),
						width, height);
				index++;
				spriteX += spacing + frameWidth;
			}
			spriteY += spacing + frameHeight;
		}
		sourceBitmap.recycle();
	}

	private void verifyChanges() {
		if (this.isModified()) {
			this.redefine();
			this.setModified(false);
		}
	}
	
	public Sprite[] getFrameRange(int start, int stop) throws Alien3DException {
		this.verifyChanges();
		if ((start > 0) && (stop <= this.frames)) {
			Sprite[] result = new Sprite[stop - start + 1];
			System.arraycopy(this.sprites, start - 1, result, 0, stop - start + 1);
			return result;
		} else {
			throw new Alien3DException("Frame index ou of bounds!");
		}
	}
	
	public int getActualFrame() {
		return this.nextFrame + 1; 
	}
	
	public void renderNextFrame(Transformable transformable, GL10 gl, int delta) throws Alien3DException {
		this.verifyChanges();
		this.sprites[this.nextFrame].render(transformable, gl, delta);
		this.nextFrame++;
		if (this.nextFrame >= this.frames) {
			this.nextFrame = 0;
		}
	}

	public void renderFrame(Transformable transformable, GL10 gl, int index, int delta) throws Alien3DException {
		this.verifyChanges();
		if (index <= this.frames) {
			this.sprites[index].render(transformable, gl, delta);
		} else {
			throw new Alien3DException("Frame index ou of bounds!");
		}
	}
	
	public void render(float delta) {
		
	}

	@Override
	public void render(Transformable transformable, GL10 gl, int delta)
			throws Alien3DException {
		this.sprites[this.nextFrame].render(transformable, gl, delta);
		
	}

	@Override
	public void update(Transformable transformable, GL10 gl, int delta, int fps)
			throws Alien3DException {
		// TODO Auto-generated method stub
		
	}
	
}
