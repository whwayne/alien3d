package org.igs.android.ogl.engine.collision;

import org.igs.android.ogl.engine.math.Point3f;

import android.util.Log;


public class Rectangle {
    public Point3f mLeftUpper;
    public Point3f mRightUpper;
    public Point3f mLeftLower;
    public Point3f mRightLower;

    // Array be convenient to loop
    // 0 up 1 left 2 bottom 3 right
    public Line2f[] lines;
    private Point3f center;

    public Rectangle() {
        mLeftUpper = new Point3f();
        mRightUpper = new Point3f();
        mLeftLower = new Point3f();
        mRightLower = new Point3f();

        center = new Point3f();
        lines = new Line2f[4];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new Line2f();
        }
    }

    public Rectangle(Point3f leftUpper, Point3f rightUpper, Point3f leftLower,
            Point3f rightLower) {
        mLeftUpper = leftUpper;
        mRightUpper = rightUpper;
        mLeftLower = leftLower;
        mRightLower = rightLower;

        generateLines();
        generateCenter();
    }

    public void generateLines() {
        if(lines == null){
            lines = new Line2f[4];
            for (int i = 0; i < lines.length; i++) {
                lines[i] = new Line2f();
        }
        }else {
            lines[0].clear();
            lines[1].clear();
            lines[2].clear();
            lines[3].clear();
        }
        lines[0].generateLine(mLeftUpper, mRightUpper);
        lines[1].generateLine(mLeftUpper, mLeftLower);
        lines[2].generateLine(mLeftLower, mRightLower);
        lines[3].generateLine(mRightLower, mRightUpper);
    }

    public void generateCenter() {
        if(center == null){
        center = new Point3f();}
        else {
            center.clear();
        }
        center.x = (mLeftUpper.x + mRightLower.x) / 2;
        center.y = mLeftUpper.y;
        center.z = (mLeftUpper.z + mRightLower.z) / 2;
    }

    @Override
    protected void finalize() throws Throwable {
        Log.e("Object finalize",this.getClass().getName());
        super.finalize();
    }
    
}