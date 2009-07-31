package org.igs.android.ogl.engine.math;

public class GameMath {

    public static final float RADIANS_PER_DEGREE = (float) (Math.PI / 180.0);
    public static final float DEGREES_PER_RADIAN = (float) (180.0 / Math.PI);

    public static final int BIG_ENDIAN = 0x02;
    public static final int LITTLE_ENDIAN = 0x04;
    
    public static final float PI = 3.14159265358979323846f;
    
    ///< floating point epsilon for single precision. todo: verify epsilon value and usage
    public static final float EPSILON = 0.00001f;
    
    ///< epsilon value squared
	public static final float EPSILON_SQUARED = EPSILON * EPSILON;
    
	/**
	 * PI*2
	 */
	public static final float TWO_PI = PI * 2;
    
    public static float clamp(float val, float min, float max) {
        if (val < min) {
            return min;
        } else if (val > max) {
            return max;
        } else {
            return val;
        }
    }

    public static int clamp(int val, int min, int max) {
        if (val < min) {
            return min;
        } else if (val > max) {
            return max;
        } else {
            return val;
        }
    }

    public static float len(float x, float y) {
        return (float) Math.sqrt(x * x + y * y);
    }

    public static float dist(float x1, float y1, float x2, float y2) {
        return len(x2 - x1, y2 - y1);
    }

    public static float getMinAngle(float cur, float target) {
        cur = normaliseAngle(cur);
        target = normaliseAngle(target);
        float diff = target - cur;
        if (diff > Math.PI) {
            diff -= Math.PI * 2;
        } else if (diff < -Math.PI) {
            diff += Math.PI * 2;
        }
        return diff;
    }

    public static float normaliseAngle(float a) {
        if (a >= 0 && a <= Math.PI * 2) {
            return a;
        }

        while (a < 0) {
            a += 2 * Math.PI;
        }
        while (a > 2 * Math.PI) {
            a -= 2 * Math.PI;
        }
        return a;
    }

    public final static float lineCircleIntersect(float x1, float y1, float x2, float y2, float r) {

        if ((x1 * x1 + y1 * y1) < r * r) {
            // Already inside
            return Float.NaN;
        }

        float dx = x2 - x1;
        float dy = y2 - y1;

        float a, b, c;
        a = dx * dx + dy * dy;
        b = 2.0f * (dx * x1 + dy * y1);
        c = (x1 * x1 + y1 * y1) - r * r;

        float d = b * b - 4 * a * c;
        if (d < 0) {
            return Float.NaN;
        }

        d = (float) Math.sqrt(d);
        float u;
        float u1 = (-b + d) / (2.0f * a);
        float u2 = (-b - d) / (2.0f * a);

        if (u1 > 0 && u1 < 1.0f && u2 > 0 && u2 < 1.0f) {
            u = Math.min(u1, u2);
        } else if (u1 > 0 && u1 < 1.0f) {
            u = u1;
        } else if (u2 > 0 && u2 < 1.0f) {
            u = u2;
        } else {
            u = Float.NaN;
        }
        return u;
    }

    public static float lerp(float v1, float v2, float dx) {
        return v2 * dx + (1 - dx) * v1;
    }
    
    public static int byte2int(byte[] b,int endian)
    {
            int result=0;
            if(endian==LITTLE_ENDIAN)
            {
                    for(int i=0;i<b.length;i++)
                    {
                            result|=((b[i]&0xff)<<i*8);
                    }
            }
            else
            {
                    for(int i=b.length-1;i>=0;i--)
                    {
                            result|=((b[i]&0xff)<<(b.length-i-1)*8);
                    }
            }
            return result;
    }
    
    public static byte[] int2byte(int num,int endian)
    {
        byte[] targets=new byte[4];
        if(endian==LITTLE_ENDIAN)
        {
                for(int i=0;i<4;i++)
                {
                        targets[i]=(byte)((num>>i*8)&0xff);
                }
        }
        else
        {
                for(int i=3;i>=0;i--)
                {
                        targets[i]=(byte)((num>>(3-i)*8)&0xff);
                }
        }
        return targets;
    }
	
    
    
}
