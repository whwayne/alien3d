package org.igs.android.ogl.engine.map.tiled;

import java.util.Hashtable;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject.TransparencyEnum;
import org.igs.android.ogl.engine.sprite.Sprite;
import org.igs.android.ogl.engine.texture.TextureBuffer;
import org.igs.android.ogl.engine.utils.Color;
import org.igs.android.ogl.engine.utils.Utils;

import android.graphics.Bitmap;

/**
*
* @author Elizeu Nogueira da Rosa Jr.
* @version 0.1
* @date 22.02.2008
* 
*/
public class TileSet {

    /** name of tileset */
    private String name;
    
    /** first image id, in map context*/
    private int firstGID;

    /** last image id, in map context*/
    private int lastGID;
    
    /** width in pixels to crop tiles */
    private int tileWidth;
    
    /** height in pixels to crop tiles */
    private int tileHeight; 
    
    /** path to image of tileSet */
    private String imageSource;
    
    private Color transparency;
    
    /** croped images container */
    private Sprite[] sprites;
    
    private Renderer renderer;
    private Map map;
    
    public TileSet(Renderer renderer, Map map) {
       this.renderer = renderer;
       this.map = map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFirstGID() {
        return firstGID;
    }

    public void setFirstGID(int firstGID) {
        this.firstGID = firstGID;
    }

    public int getLastGID() {
        return lastGID;
    }

    public void setLastGID(int lastGID) {
        this.lastGID = lastGID;
    }
    
    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public void setTransparency(Color color) {
    	this.transparency = color;
    }

    public Color getTransparency() {
    	return this.transparency;
    }

    public void setTileProperties(Integer id, Hashtable<String, Object> properties) {
    	this.sprites[id - this.firstGID].setProperties(properties);
    }

    public boolean existsTileProperties(Integer id) {
    	return this.sprites[id - this.firstGID].getProperties().size() > 0;
    }

    public Hashtable<String, Object> getTileProperties(Integer id) {
    	return this.sprites[id - this.firstGID].getProperties();
    }
    
    public void renderTile(int index, Point3f position) {
    	this.sprites[index - 1].setPosition(position);
    	this.sprites[index - 1].render(0f);
    }
    
    /**
     * Initializate the SpriteSheet imagens
     * All the properties must be adjusted
     * adjust all properties before call of this method
     */
	public final void initTiles() throws AndromedaException {
		Bitmap sourceBitmap = Utils.getAssetBitmap(this.imageSource, renderer.getContext());
		
		int frames = (sourceBitmap.getWidth() / this.getTileWidth()) * (sourceBitmap.getHeight() / this.getTileHeight()); 
		
		this.setLastGID(this.getFirstGID() + frames);
		this.sprites = new Sprite[frames];

		int spriteX = 0;
		int spriteY = 0;
		
		int index = 0;
		
		float width = (tileWidth / map.getMapProportion());
		float height = (tileHeight / map.getMapProportion());

		for (int i = 0; i < sourceBitmap.getHeight() / tileHeight; i++) {
			spriteX = 0;
			for (int j = 0; j < sourceBitmap.getWidth() / tileWidth; j++) {
				this.sprites[index] = new Sprite(
						"", 
						null, 
						renderer,
						TextureBuffer.getInstance().getTextureNoBuffered(
								Bitmap.createBitmap(
										sourceBitmap, 
										spriteX, 
										spriteY, 
										tileWidth, 
										tileHeight
								), 
								renderer.getGL10()
						),
						width, 
						height, 
						new Point3f(0f, 0f, 0f), 
						
						new Point3f(180f, 0f, 0f), 
						new Point3f(1f, 1f, 1f), 
						TransparencyEnum.ALPHA);
				index++;
				spriteX += tileWidth;
			}
			spriteY += tileHeight;
		}
		sourceBitmap.recycle();
	}
    
}
