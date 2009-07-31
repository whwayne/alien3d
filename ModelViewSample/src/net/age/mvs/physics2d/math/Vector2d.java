package net.age.mvs.physics2d.math;

public class Vector2d {

	public float x, y;
	
	public Vector2d() {
		x = 0;
		y = 0;
	}
	
	public Vector2d(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public final void add(final Vector2d other) {
		x += other.x;
		y += other.y;
	}
	
	public final void sub(final Vector2d other) {
		x -= other.x;
		y -= other.y;
	}

	public static final void operatorSub(final Vector2d a, final Vector2d b, Vector2d into) {
		into.x = a.x - b.x;
		into.y = a.y - b.y;
	}
	/*
	inline vector_2 operator-( vector_2 const &A, vector_2 const &B )
	{
		return vector_2(A.X-B.X,A.Y-B.Y);
	}
	*/

	public static final void operatorSum(final Vector2d a, final Vector2d b, Vector2d into) {
		into.x = a.x + b.x;
		into.y = a.y + b.y;
	}
	/*
	inline vector_2 operator+( vector_2 const &A, vector_2 const &B )
	{
		return vector_2(A.X+B.X,A.Y+B.Y);
	}
	*/
	
	public static final void operatorMul(float a, final Vector2d b, Vector2d into) {
		into.x = a * b.x;
		into.y = a * b.y;
	}
	/*
	inline vector_2 operator*( real A, vector_2 const &B )
	{
		return vector_2(A*B.X,A*B.Y);
	}
	
	inline vector_2 operator*( vector_2 const &A, real B )
	{
		return vector_2(B*A.X,B*A.Y);
	}
	*/
	
	public static final void operatorDiv(final Vector2d a, final float b, Vector2d into) {
		into.x = a.x / b;
		into.y = a.y / b;
	}
	/*
	inline vector_2 operator/( vector_2 const &A, real B )
	{
		return vector_2(A.X/B,A.Y/B);
	}
	*/
	
	public static final float dotProduct(final Vector2d a, final Vector2d b) {
		return a.x * b.x + a.y*b.y;
	}
	/*
	inline real DotProduct( vector_2 const &A, vector_2 const &B )
	{
		return A.X*B.X + A.Y*B.Y;
	}
	*/

	public static final float perpDotProduct(final Vector2d a, final Vector2d b) {
		return a.x*b.y - a.y*b.x;
	}
	/*
	inline real PerpDotProduct( vector_2 const &A, vector_2 const &B )
	{
		return A.X*B.Y - A.Y*B.X;
	}
	*/

	public static final void getPerpendicular(final Vector2d a, Vector2d into) {
		into.x = -a.y;
		into.y = a.x;
	}
	/*
	inline vector_2 GetPerpendicular( vector_2 const &A )
	{
		return vector_2(-A.Y,A.X);
	}
	*/

	public static final float getLength(final Vector2d a) {
		return (float)Math.sqrt(a.x*a.x + a.y*a.y);
	}
	/*
	inline real GetLength( vector_2 const &A )
	{
		return r(sqrt(A.X*A.X + A.Y*A.Y));
	}
	*/

	public static final void getNormal(final Vector2d a, Vector2d into) {
		float oneOverLength = 1f / getLength(a);
		into.x = oneOverLength*a.x;
		into.y = oneOverLength*a.y;
	}
	/*
	inline vector_2 GetNormal( vector_2 const &A )
	{
		real OneOverLength = r(1)/GetLength(A);
		return vector_2(OneOverLength*A.X,OneOverLength*A.Y);
	}
	*/

	public static final void operatorMul(final Matrix2x2 a, final Vector2d b, Vector2d into) {
		into.x = a.aElements[0][0] * b.x + a.aElements[0][1] * b.y;
		into.y = a.aElements[1][0] * b.x + a.aElements[1][1] * b.y;
	}
	/*
	inline vector_2 operator*( matrix_2x2 const &A, vector_2 const &B )
	{
		return vector_2(A.aElements[0][0]*B.X + A.aElements[0][1]*B.Y,
						A.aElements[1][0]*B.X + A.aElements[1][1]*B.Y);
	}
	*/
	
}
