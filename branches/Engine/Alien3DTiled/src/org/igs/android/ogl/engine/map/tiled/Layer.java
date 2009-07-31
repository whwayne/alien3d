package org.igs.android.ogl.engine.map.tiled;

import java.util.ArrayList;
import java.util.List;

import org.igs.android.ogl.engine.scene.SceneObject;

/**
*
* @author Elizeu Nogueira da Rosa Jr.
* @version 0.1
* @date 22.02.2008
* 
*/
public class Layer {

    private Map map;
    private String name;
    private int width;
    private int height;
   
    private List<Tile> tileList;

    private List<SceneObject> entities;
    
    public Layer(Map map) {
        this.map = map;
    }
    
    public List<SceneObject> getEntities() {
    	if (this.entities == null) {
    		this.entities = new ArrayList<SceneObject>(5);
    	}
    	return this.entities;
    }
    
    public void addEntity(SceneObject entity) {
    	this.getEntities().add(entity);
    }

    public void removeEntity(SceneObject entity) {
    	this.getEntities().remove(entity);
    }
    
    public Map getMap() {
        return this.map;
    }
    
    public final String getName() {
        return name;
    }

    final void setName(String name) {
        this.name = name;
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

    public List<Tile> getTileList() {
        return tileList;
    }

    public void setTileList(List<Tile> tileList) {
        this.tileList = tileList;
    }

    /*
    public void replaceTile(Integer oldTile, Integer newTile) {
    	Enumeration<Integer> ids = this.tileList.keys();
    	while (ids.hasMoreElements()) {
    		
    	}
        for (Integer i : this.getTileList()) {
            if (i.compareTo(oldTile) == 0) {
                i = newTile;
            }
        }
    }
	*/

    public Tile findTileAsXY(float x, float y) {
    	for (Tile tile : this.tileList)
    		if (tile.id > 0)
    			if (tile.position.x >= x)
    				if (tile.position.y <= y)
    					if (tile.position.x <= x + map.getTileWidth() / map.getMapProportion())
    						if (tile.position.y >= y + -(map.getTileWidth() / map.getMapProportion()))
    							return tile;
    	return null;
    }
    
    public void render(float delta, float x, float y) {
    	for (Tile tile : this.tileList)
    		if (tile.id > 0)
    			if (tile.position.x >= x - 0.5f)
    				if (tile.position.y <= y + 0.5f)
    					if (tile.position.x <= x + map.getXProportion())
    						if (tile.position.y >= y + map.getYProportion())
    						    this.map.drawTile(tile.id, tile.position);
    	
    	for (SceneObject obj : this.getEntities())
    		obj.render(delta);
    }
    
}
