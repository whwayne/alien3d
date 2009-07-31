package org.igs.android.ogl.engine.map.tiled;

import org.igs.android.ogl.engine.math.Point3f;

public class Tile {

	public int id;
	public Point3f position;
	
	public Tile(int id, Point3f position) {
		this.id = id;
		this.position = position;
	}

}
