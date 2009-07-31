package net.age.mvs.physics2d.math;

import org.igs.android.ogl.engine.math.SinCosTable;

public class Matrix2x2 {
	
	float aElements[][] = new float[2][2];

	public Matrix2x2() {
		aElements[0][0] = aElements[0][1] = aElements[1][0] = aElements[1][1] = 0.0f;
	}
	
	public Matrix2x2(float rotateByThisManyRadians) {
		float cosAngle = SinCosTable.cos(rotateByThisManyRadians);
		float sinAngle = SinCosTable.sin(rotateByThisManyRadians);

		aElements[0][0] = cosAngle; aElements[0][1] = -sinAngle;
		aElements[1][0] = sinAngle; aElements[1][1] =  cosAngle;			
	}
	
}