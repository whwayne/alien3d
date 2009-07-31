package org.igs.android.ogl.engine.particles.oje2d;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject;
import org.igs.android.ogl.engine.scene.SceneObject.TransparencyEnum;
import org.igs.android.ogl.engine.sprite.Sprite;
import org.igs.android.ogl.engine.utils.Color;

public class GLParticle {

    /** The image used by the particle */
    private Sprite image = null;

    /** The particle's X position */
    private int xPos;

    /** The particle's Y position */
    private int yPos;

    private float radius;

    private float radiusSpeed;

    private float rotationSpeed;

    private int timeToLive;

    private float angle;

    // private GLColor color = null;
    private float alpha;

    private float alphaStep;

    private long startTime;

    private long lastTime;

    private long currentTime;

    private boolean isDead;

    public GLParticle(Sprite mImage) {
        image = mImage;
    }
    
    public GLParticle(String id, SceneObject parent, Renderer renderer, String mFilename, int width, int height, TransparencyEnum transparencyType, Point3f position, Point3f rotation, Point3f scale) throws AndromedaException {
        image = new Sprite("", parent, renderer, mFilename, width, height, position, rotation, scale, transparencyType);
    }

    public TransparencyEnum getTransparencyType() {
    	return image.getTransparencyType();
    }
    
    public void setTransparencyType(TransparencyEnum transparencyType) {
    	this.image.setTransparencyType(transparencyType);
    }
    
    // Speed: laps per second
    // Time to live: milliseconds
    public void init(int mRadius, float mRadiusSpeed, float mRotationSpeed,
            int mTtl, int mAngle, Color mColor) {
        radius = mRadius;
        radiusSpeed = mRadiusSpeed;
        rotationSpeed = mRotationSpeed;
        timeToLive = mTtl;
        angle = mAngle;

        image.setColor(mColor);

        startTime = System.currentTimeMillis();
        lastTime = startTime;
        currentTime = lastTime;

        isDead = false;
    }

    // Snow (or rain) special case
    public void init(int mX, int mY, float mSpeed, int mTtl, Color mColor) {
        xPos = mX;
        radius = mY;
        radiusSpeed = mSpeed;
        timeToLive = mTtl;

        image.setColor(mColor);

        startTime = System.currentTimeMillis();
        lastTime = startTime;
        currentTime = lastTime;

        isDead = false;
    }

    public void setAlpha(float mAlpha, float mStep) {
        alpha = mAlpha;
        alphaStep = mStep;
    }

    public void updateAlpha() {
        alpha -= alphaStep;
        if (alpha < 0.0f)
            kill();
    }

    public void draw(float mX, float mY, float delta) {
        if (!isDead) {
        	image.getPosition().x = mX + xPos;
        	image.getPosition().y = mX + yPos;
        	image.getColor().a = alpha;
            image.render(delta);
        }
    }

    public void setRadius(float mRadius) {
        radius = mRadius;
    }

    public void setXPos(int mX) {
        xPos = mX;
    }

    public void setYPos(int mY) {
        yPos = mY;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public boolean isDead() {
        return isDead;
    }

    public float getRadiusSpeed() {
        return radiusSpeed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public float getAngle() {
        return angle;
    }

    public float getRadius() {
        return radius;
    }

    public void kill() {
        isDead = true;
    }

    public void setCurrentTime(long mCurrentTime) {
        currentTime = mCurrentTime;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setAngle(float mAngle) {
        angle = mAngle;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setLastTime(long mLastTime) {
        lastTime = mLastTime;
    }
	
}
