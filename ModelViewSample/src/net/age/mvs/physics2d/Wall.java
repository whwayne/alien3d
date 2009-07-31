package net.age.mvs.physics2d;

import net.age.mvs.physics2d.math.Vector2d;

//define wall by plane equation
public class Wall {

	// inward pointing
	Vector2d normal;	
	
	// ax + by + c = 0
	float c;

	// points for drawing wall
	Vector2d startPoint;
	Vector2d endPoint;
	
}
