package org.igs.android.ogl.engine.sprite;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject;
import org.igs.android.ogl.engine.texture.TextureBuffer;
import org.igs.android.ogl.engine.utils.Utils;

import android.graphics.Bitmap;

public class SpriteSheet extends SceneObject {

	private String fileName;

	private float width;
	private float height;
	
	
	private int frameWidth;
	private int frameHeight;
	private int spacing;
	
	private int frames;
	private int nextFrame = 0;
	
	private Sprite[] sprites;
	
	private TransparencyEnum transparencyType; 
	
	public SpriteSheet(String id, SceneObject parent, Renderer renderer, String fileName, float width, float height, int frameWidth, int frameHeight, int spacing, Point3f position, Point3f rotation, Point3f scale, TransparencyEnum transparencyType) throws AndromedaException {
		super(id, parent, renderer, position, rotation, scale);
		this.transparencyType = transparencyType;
		this.fileName = fileName;
		this.width = width;
		this.height = height;
		this.spacing = spacing;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.build();
	}

	public void setTransparencyType(TransparencyEnum transparencyType) {
		this.transparencyType = transparencyType;
		super.setModified(true);
	}
	
	public TransparencyEnum getTransparencyType() {
		return this.transparencyType;
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
			sprite.setPosition(getPosition().x, getPosition().y, getPosition().z);
			sprite.setRotation(getRotation().x, getRotation().y, getRotation().z);
			sprite.setScale(getScale().x, getScale().y, getScale().z);
			/*		
			sprite.setPosition(getPosition());
			sprite.setRotation(getRotation());
			sprite.setScale(getScale());
			*/
			sprite.setWidth(width);
			sprite.setHeight(height);
			sprite.setTransparencyType(transparencyType);
		}
	}
	
	private void build() throws AndromedaException {
		Bitmap sourceBitmap = Utils.getAssetBitmap(this.fileName, getRenderer().getContext());
		
		this.frames = ((sourceBitmap.getWidth() / frameWidth) * (sourceBitmap.getHeight() / frameHeight));
		this.sprites = new Sprite[this.frames];

		int spriteX = 0;
		int spriteY = 0;
		
		int index = 0;
		
		for (int i = 0; i < sourceBitmap.getHeight() / frameHeight; i++) {
			spriteX = 0;
			for (int j = 0; j < sourceBitmap.getWidth() / frameWidth; j++) {
				this.sprites[index] = new Sprite("", this, 
						getRenderer(),
						TextureBuffer.getInstance().getTextureNoBuffered(Bitmap.createBitmap(sourceBitmap, spriteX, spriteY, frameWidth, frameHeight), getRenderer().getGL10()),
						width, height, getPosition(), getRotation(), getScale(), this.getTransparencyType());
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
	
	public Sprite[] getFrameRange(int start, int stop) throws AndromedaException {
		this.verifyChanges();
		if ((start > 0) && (stop <= this.frames)) {
			Sprite[] result = new Sprite[stop - start + 1];
			System.arraycopy(this.sprites, start - 1, result, 0, stop - start + 1);
			return result;
		} else {
			throw new AndromedaException(12, "Frame index ou of bounds!");
		}
	}
	
	public int getActualFrame() {
		return this.nextFrame + 1; 
	}
	
	public void renderNextFrame(float delta) {
		this.verifyChanges();
		this.sprites[this.nextFrame].render(delta);
		this.nextFrame++;
		if (this.nextFrame >= this.frames) {
			this.nextFrame = 0;
		}
	}

	public void renderFrame(int index, float delta) throws AndromedaException {
		this.verifyChanges();
		if (index <= this.frames) {
			this.sprites[index].render(delta);
		} else {
			throw new AndromedaException(12, "Frame index ou of bounds!");
		}
	}
	
	public void render(float delta) {
		this.sprites[this.nextFrame].render(delta);
	}
	
}
