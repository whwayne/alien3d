package org.igs.android.ogl.engine.collision;

import java.util.ArrayList;

import org.igs.android.ogl.engine.math.Point3f;

import android.util.Log;


public class Line2f {

    public static final int VERTICAL = 1;
    public static final int ORDINARY = 2;
    public static final int HORIZONTAL = 3;

    public static final float ZERO = 0.001f;
    public int lineType;
    public float k, b;
    public Point3f pointS, pointE;
    public Point3f collisionPoint;
    
    private static ArrayList<Line2f> objPool = new ArrayList<Line2f>(50);

    public Line2f() {
        pointS = new Point3f();
        pointE = new Point3f();
        collisionPoint = new Point3f();
    }

    private Line2f(Point3f firstP, Point3f secondP) {
        generateLine(firstP, secondP);
        collisionPoint = new Point3f();
    }

    public static Line2f getInstance(Point3f firstP, Point3f secondP){
    	if(objPool.size() < 1){
    		return new Line2f(firstP,secondP);
    	}else {
    		Line2f line2f = objPool.remove(0);
    		line2f.clear();
    		line2f.generateLine(firstP, secondP);
    		return line2f;
		}
    }
    
    public static void addObj(ArrayList<Line2f> list){
    	if(list != null)
    	objPool.addAll(list);
    }
    
    public static void addObj(Line2f line2f){
    	if(line2f != null)
    	objPool.add(line2f);
    }
    
    public void clear(){
        pointS = new Point3f();
        pointE = new Point3f();
        collisionPoint = new Point3f();
        k = 0;
        b = 0;
        lineType = 0;
    }
    
    public void generateLine(Point3f firstP, Point3f secondP) {
        pointS = firstP;
        pointE = secondP;
        // means this line is vertical in xoz
        if (Math.abs(secondP.x - firstP.x) <= ZERO) {
            lineType = VERTICAL;
            b = firstP.x;
         // means this line is horizontal in xoz
        } else if (Math.abs(secondP.z - firstP.z) <= ZERO) {
            lineType = HORIZONTAL;
            k = 0;
            b = firstP.z;
        } else {
        	// means this is an ordinary line
            lineType = ORDINARY;
            k = (secondP.z - firstP.z) / (secondP.x - firstP.x);
            b = firstP.z - k * firstP.x;
        }
    }

    public float distanceBetweenP2fXZ(Point3f point) {
        return (float) (Math.abs(k * point.x - point.z + b) / Math.sqrt(k * k + 1));
    }

    public boolean isCross(Line2f lineInput) {
    	// means I am a ordinary line
        if (this.lineType == ORDINARY) {
            switch (lineInput.lineType) {
            case ORDINARY:
                return o_ordinaryLine(lineInput);
            case VERTICAL:
                return o_verticalLine(lineInput);
            case HORIZONTAL:
                return o_horizontal(lineInput);
            }
        } else if (this.lineType == HORIZONTAL) {// means I am a horizontal line
            switch (lineInput.lineType) {
            case ORDINARY:
                return h_ordinary(lineInput);
            case VERTICAL:
                return h_vertical(lineInput);
            case HORIZONTAL:
                return h_horizontal(lineInput);
            }
        } else {// means I am a vertical line
            switch (lineInput.lineType) {
            case ORDINARY:
                return v_ordinaryLine(lineInput);
            case VERTICAL:
                return v_verticalLine(lineInput);
            case HORIZONTAL:
                return v_horizontal(lineInput);
            }
        }
        return false;
    }

    private boolean o_ordinaryLine(Line2f lineInput) {
        if (this.k != lineInput.k) {
            collisionPoint.x = (this.b - lineInput.b) / (lineInput.k - this.k);
            collisionPoint.z = this.k * collisionPoint.x + this.b;

            if (Math.min(lineInput.pointS.x, lineInput.pointE.x) <= collisionPoint.x
                    && collisionPoint.x <= Math.max(lineInput.pointS.x,
                            lineInput.pointE.x)
                    && Math.min(pointS.x, pointE.x) <= collisionPoint.x
                    && collisionPoint.x <= Math.max(pointS.x, pointE.x)) {
                Log.i("o_o", "o_o");
                return true;
            } else {
                return false;
            }

        } else if (this.b == lineInput.b) {
            Log.i("o_o1", "o_o1");
            return true;
        } else {
            return false;
        }
    }

    private boolean o_verticalLine(Line2f lineInput) {
        collisionPoint.x = lineInput.pointS.x;
        collisionPoint.z = k * lineInput.pointS.x + b;

        if (Math.min(pointS.x, pointE.x) <= collisionPoint.x
                && collisionPoint.x <= Math.max(pointS.x, pointE.x)
                && Math.min(pointS.z, pointE.z) <= collisionPoint.z
                && collisionPoint.z <= Math.max(pointS.z, pointE.z)
                && Math.min(lineInput.pointS.z, lineInput.pointE.z) <= collisionPoint.z
                && collisionPoint.z <= Math.max(lineInput.pointS.z,
                        lineInput.pointE.z)) {
            Log.i("o_v", "o_v");
            return true;
        } else {
            return false;
        }
    }

    private boolean v_ordinaryLine(Line2f lineInput) {
        collisionPoint.x = pointS.x;
        collisionPoint.z = lineInput.k * pointS.x + lineInput.b;

        if (Math.min(lineInput.pointS.x, lineInput.pointE.x) <= collisionPoint.x
                && collisionPoint.x <= Math.max(lineInput.pointS.x,
                        lineInput.pointE.x)
                && Math.min(pointS.z, pointE.z) <= collisionPoint.z
                && collisionPoint.z <= Math.max(pointS.z, pointE.z)) {
            Log.i("v_o", "v_o");
            Log.e("k " + lineInput.k + " b " + lineInput.b, "");
            return true;
        } else {
            return false;
        }
    }

    private boolean v_verticalLine(Line2f lineInput) {
        if (this.b == lineInput.b) {
            Log.i("v_v", "v_v");
            return true;
        } else {
            return false;
        }
    }

    private boolean o_horizontal(Line2f lineInput) {
        collisionPoint.x = (lineInput.b - this.b) / this.k;
        collisionPoint.z = lineInput.b;
        if (Math.min(lineInput.pointS.x, lineInput.pointE.x) <= collisionPoint.x
                && collisionPoint.x <= Math.max(lineInput.pointS.x,
                        lineInput.pointE.x)
                && Math.min(pointS.z, pointE.z) <= collisionPoint.z
                && collisionPoint.z <= Math.max(pointS.z, pointE.z)) {
            Log.i("o_h", "o_h");
            return true;
        } else {
            return false;
        }
    }

    private boolean v_horizontal(Line2f lineInput) {
        collisionPoint.x = this.b;
        collisionPoint.z = lineInput.b;
        if (Math.min(lineInput.pointS.x, lineInput.pointE.x) <= collisionPoint.x
                && collisionPoint.x <= Math.max(lineInput.pointS.x,
                        lineInput.pointE.x)
                && Math.min(pointS.z, pointE.z) <= collisionPoint.z
                && collisionPoint.z <= Math.max(pointS.z, pointE.z)) {
            Log.i("v_h", "v_h");
            return true;
        } else {
            return false;
        }
    }

    private boolean h_horizontal(Line2f lineInput) {
        return false;
    }

    private boolean h_vertical(Line2f lineInput) {
        collisionPoint.z = this.b;
        collisionPoint.x = lineInput.b;
        if (Math.min(pointS.x, pointE.x) <= collisionPoint.x
                && collisionPoint.x <= Math.max(pointS.x, pointE.x)
                && Math.min(lineInput.pointS.z, lineInput.pointE.z) <= collisionPoint.z
                && collisionPoint.z <= Math.max(lineInput.pointS.z,
                        lineInput.pointE.z)) {
            Log.i("h_v", "h_v");
            return true;
        } else {
            return false;
        }
    }

    private boolean h_ordinary(Line2f lineInput) {
        collisionPoint.x = (this.b - lineInput.b) / lineInput.k;
        collisionPoint.z = this.b;
        if (Math.min(pointS.x, pointE.x) <= collisionPoint.x
                && collisionPoint.x <= Math.max(pointS.x, pointE.x)
                && Math.min(lineInput.pointS.x, lineInput.pointE.x) <= collisionPoint.x
                && collisionPoint.x <= Math.max(lineInput.pointS.x,
                        lineInput.pointE.x)) {
            Log.i("h_o", "h_o");
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        Log.e("Object finalize",this.getClass().getName());
        super.finalize();
    }
    
}
