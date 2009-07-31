package org.igs.android.ogl.engine.particles.oje2d;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject;
import org.igs.android.ogl.engine.sprite.Sprite;
import org.igs.android.ogl.engine.utils.Color;

public class ParticleSystemSnow extends SceneObject {

    private GLParticle[] particles = null;

    private int numberOfParticles;

    private int startX;

    private int startY;

    private int endX;

    private int endY;

    private float minSpeed;

    private float maxSpeed;

    private int minTimeToLive;

    private int maxTimeToLive;

    private float alphaStep;

    private boolean forever;

    private boolean isDead;

    private long startTime;

    Renderer renderer;
    
    public void setArea(int mStartX, int mStartY, int mEndX, int mEndY) {
        startX = mStartX;
        startY = mStartY;
        endX = mEndX;
        endY = mEndY;
    }

    public ParticleSystemSnow(String id, SceneObject parent, Renderer renderer, String mFilename, int mNumber, Point3f position, Point3f rotation, Point3f scale, TransparencyEnum transparencyType) throws AndromedaException {
    	super(id, parent, renderer, position, rotation, scale);
    	isDead = true;
        numberOfParticles = mNumber;
        particles = new GLParticle[numberOfParticles];
        this.renderer = renderer;
        // Create the particles (loading of textures)
        Sprite image = new Sprite("", this, renderer, mFilename, 4, 10, position, rotation, scale, transparencyType);
        for (int i = 0; i < numberOfParticles; i++)
            particles[i] = new GLParticle(image);
    }

    public void setSpeed(float mMinSpeed, float mMaxSpeed) {
        minSpeed = mMinSpeed;
        maxSpeed = mMaxSpeed;
    }

    public void setTimeToLive(int mMinTtl, int mMaxTtl) {
        minTimeToLive = mMinTtl;
        maxTimeToLive = mMaxTtl;
    }

    public void setLoop(boolean mValue) {
        forever = mValue;
    }

    public void setAlphaStep(float mStep) {
        alphaStep = mStep;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        isDead = false;

        // Init the particles
        for (int i = 0; i < numberOfParticles; i++) {
            // Get the random values
            int xPos = (int) ((Math.random() * (endX - startX)) + startX);
            int yPos = (int) ((Math.random() * (endY - startY)) + startY);
            ;
            float speed =
                    (float) ((Math.random() * (maxSpeed - minSpeed)) + minSpeed);
            int ttl =
                    (int) ((Math.random() * (maxTimeToLive - minTimeToLive)) + minTimeToLive);

            // Initialize the particle
            particles[i].init(xPos, yPos, speed, ttl, Color.white);
            particles[i].setAlpha(1.0f, alphaStep);
        }
    }

    public void stop() {
        isDead = true;
    }

    public void render(float delta) {
        // Only draw when alive
        if (!isDead) {
            // Go through the particles and draw if not dead & update it
            for (int i = 0; i < numberOfParticles; i++)
                if (!particles[i].isDead()) {
                    particles[i].draw(0, 0, delta); // The particle draws relative to
                                             // it's position
                    updateParticle(particles[i]);
                }

            // Check if system dead (if not looping forever)
            if ((System.currentTimeMillis() - startTime) > maxTimeToLive
                    && !forever)
                isDead = true;
        }
    }

    private void updateParticle(GLParticle mParticle) {
        /*
         * In this particle system, we update the particle's position on the Y's
         * axis, based on it's speed to create a snow (or rain) effect.
         */
        mParticle.setCurrentTime(System.currentTimeMillis());
        if (mParticle.getCurrentTime() - mParticle.getLastTime() > (float) mParticle
                .getRadiusSpeed() / 1000) {
            // Update the particle's Y's axis
            if (Math.random() > 0.5)
                mParticle.setXPos((int) (mParticle.getXPos() + mParticle
                        .getRadiusSpeed() / 5));
            else
                mParticle.setXPos((int) (mParticle.getXPos() - mParticle
                        .getRadiusSpeed() / 5));
            mParticle.setYPos((int) (mParticle.getRadius()));

            // Update the distance and the angle
            float radius = mParticle.getRadius() + mParticle.getRadiusSpeed();
            mParticle.setRadius(radius);
            mParticle.updateAlpha();

            // Check if dead from ttl
            if ((System.currentTimeMillis() - mParticle.getStartTime()) > mParticle
                    .getTimeToLive())
                mParticle.kill();

            // Check if dead from getting out of the area
            if (mParticle.getXPos() < startX || mParticle.getXPos() > endX
                    || mParticle.getYPos() < startY
                    || mParticle.getYPos() > endY)
                mParticle.kill();

            // If the user has set this to loop forever, create
            // a new particle if this one died
            if (forever && mParticle.isDead())
                resurrect(mParticle);

            mParticle.setLastTime(mParticle.getCurrentTime());
        }
    }

    private void resurrect(GLParticle mParticle) {
        // Get the random values
        int xPos = (int) ((Math.random() * (endX - startX)) + startX);
        int yPos = startY;
        float speed =
                (float) ((Math.random() * (maxSpeed - minSpeed)) + minSpeed);
        int ttl =
                (int) ((Math.random() * (maxTimeToLive - minTimeToLive)) + minTimeToLive);

        // Initialize the particle
        mParticle.init(xPos, yPos, speed, ttl, Color.white);
        mParticle.setAlpha(1.0f, alphaStep);
    }

    public boolean isDead() {
        return isDead;
    }

    public int getLiveParticleCount() {
        // If we are not dead
        if (!isDead) {
            // Go through the particles and count the live ones
            int value = 0;
            for (int i = 0; i < numberOfParticles; i++)
                if (!particles[i].isDead())
                    value++;
            return value;
        }
        return 0;
    }

    public int getParticleCount() {
        return numberOfParticles;
    }
	
	
}
