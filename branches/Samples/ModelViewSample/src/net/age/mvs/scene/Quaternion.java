package net.age.mvs.scene;

public class Quaternion {

	float w, x, y, z;

	private float temporaryLength;
	
	public final float length()	{
	  return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	
	
	public final void normalize() {
		temporaryLength = this.length();
		x /= temporaryLength;
		y /= temporaryLength;
		z /= temporaryLength;
		w /= temporaryLength;
	}

	public void conjugate() {
	  x = -x;
	  y = -y;
	  z = -z;
	}
	
	public void copy(Quaternion from) {
		this.x = from.x;
		this.y = from.y;
		this.z = from.z;
		this.w = from.w;
	}
	
	public void mult(Quaternion a, Quaternion b) {
		x = a.w * b.x + a.x * b.w + a.y * b.z - a.z * b.y;
		y = a.w * b.y - a.x * b.z + a.y * b.w + a.z * b.x;
		z = a.w * b.z + a.x * b.y - a.y * b.x + a.z * b.w;
		w = a.w * b.w - a.x * b.x - a.y * b.y - a.z * b.z;
	}
	
	/*
	public final void rotateCamera(float angle, float x, float y, float z) {
	  quaternion temp, quat_view, result;

	  temp.x = x * sin(Angle/2);
	  temp.y = y * sin(Angle/2);
	  temp.z = z * sin(Angle/2);
	  temp.w = cos(Angle/2);

	  quat_view.x = View.x;
	  quat_view.y = View.y;
	  quat_view.z = View.z;
	  quat_view.w = 0;

	  result = mult(mult(temp, quat_view), conjugate(temp));

	  View.x = result.x;
	  View.y = result.y;
	  View.z = result.z;
	}
	
	gluLookAt(Position.x, Position.y, Position.z,
	          View.x, View.y, View.z, Up.x, Up.y, Up.z).

	*/
}
