package org.igs.android.ogl.engine.math;

/**
 * 
 * This class is used to store a Point2f in a 3D space
 * 
 */
public strictfp class Point2f implements Cloneable {

	public float x;
	public float y;;

	public Point2f() {
		x = 0f;
		y = 0f;
	}
	
	public Point2f(float ix, float iy) {
		x = ix;
		y = iy;
	}
	
	public Point2f(float angle){
		x = (float)Math.cos(angle);
		y = (float)Math.sin(angle);
	}
	
	public Point2f(String pointString) {
		String[] splitted = pointString.split(",");
		x = Float.valueOf(splitted[0]);
		y = Float.valueOf(splitted[1]);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Point2f add(Point2f p) {
		return new Point2f(x + p.x, y + p.y);
	}

	public Point2f add(float x, float y) {
		return new Point2f(this.x + x, this.y + y);
	}
	
	public Point2f sub(Point2f p) {
		return new Point2f(x - p.x, y - p.y);
	}

	public float mul(Point2f p) {
		return (x * p.x + y * p.y);
	}

	public Point2f mul(float s) {
		return new Point2f(x * s, y * s);
	}

	public float cross(Point2f p) {
		return x * p.y - y * p.x;
	}

	public float cross2(Point2f p) {
		return y * p.x - x * p.y;
	}

	public Point2f div(float s) {
		return new Point2f(x / s, y / s);
	}

	public Point2f negate() {
		return new Point2f(-x, -y);
	}
	
	public Point2f normal() {
		return new Point2f(-y, x);
	}

	public void scale(float a) {
		x *= a; 
		y *= a;
	}
	
	public Point2f normalize() {
		float h = 1 / (float)Math.sqrt(this.mul(this));
		return this.mul(h);
	}
	static float invSqrt(float x){
	    float xhalf = 0.5f*x;
	    int i = Float.floatToIntBits(x); // get bits for floating value
	    i = 0x5f375a86- (i>>1); // gives initial guess y0
	    x = Float.intBitsToFloat(i); // convert bits back to float
	    x = x*(1.5f-xhalf*x*x); // Newton step, repeating increases accuracy
	    return x;
	 }

	public Point2f rotate(float cosa, float sina) {
		return new Point2f(x * cosa - y * sina, x * sina + y * cosa);
	}

	public Point2f rotate(float alpha) {
		return rotate((float)Math.cos(alpha), (float)Math.sin(alpha));
	}

	public float square() {
		return x * x + y * y;
	}

	public float length() {
		return (float)Math.sqrt(this.square());
	}

	public float angle() {
		return (float)Math.atan2(y, x);
	}

	public float distance(Point2f p) {
		return (float)Math.sqrt(p.sub(this).square());
	}

	public float squaredDistance(Point2f p) {
		return p.sub(this).square();
	}
	public void fromString(String pointString) {
		String[] splitted = pointString.split(",");
		x = Float.valueOf(splitted[0]);
		y = Float.valueOf(splitted[1]);
	}
	public String toString() {
		return String.valueOf(x) + "," + String.valueOf(y);
	}

    @Override
    public Point2f clone() {
        return new Point2f(this.x, this.y);
    }

}
