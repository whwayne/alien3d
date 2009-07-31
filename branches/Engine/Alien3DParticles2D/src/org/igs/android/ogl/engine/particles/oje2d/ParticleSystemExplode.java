package org.igs.android.ogl.engine.particles.oje2d;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject;
import org.igs.android.ogl.engine.sprite.Sprite;
import org.igs.android.ogl.engine.utils.Color;

public class ParticleSystemExplode extends SceneObject {

	private GLParticle[] particles = null;

    private int numberOfParticles;

    private float maxRadius;

    private float minSpeed;

    private float maxSpeed;

    private int minTimeToLive;

    private int maxTimeToLive;

    private boolean isDead;

    private long startTime;

    private Renderer renderer;
    
    public ParticleSystemExplode(String id, SceneObject parent, Renderer renderer, String mFilename, int mNumber, float particleWidth, float particleHeight, Point3f position, Point3f rotation, Point3f scale, TransparencyEnum transparencyType) throws AndromedaException {
    	super(id, parent, renderer, position, rotation, scale);
    	isDead = true;
    	this.renderer = renderer;
        numberOfParticles = mNumber;
        particles = new GLParticle[numberOfParticles];
        Sprite image = new Sprite("", this, this.renderer, mFilename, particleWidth, particleHeight, position, rotation, scale, transparencyType);
        for (int i = 0; i < numberOfParticles; i++)
            particles[i] = new GLParticle(image);
    }

    public void setRadius(float mMinRadius, float mMaxRadius) {
        maxRadius = mMaxRadius;
    }

    public void setSpeed(float mMinSpeed, float mMaxSpeed) {
        minSpeed = mMinSpeed;
        maxSpeed = mMaxSpeed;
    }

    public void setTimeToLive(int mMinTtl, int mMaxTtl) {
        minTimeToLive = mMinTtl;
        maxTimeToLive = mMaxTtl;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        isDead = false;

        // Init the particles
        for (int i = 0; i < numberOfParticles; i++) {
            // Get the random values
            int radius = 0;
            float speed =
                    (float) ((Math.random() * (maxSpeed - minSpeed)) + minSpeed);
            int ttl =
                    (int) ((Math.random() * (maxTimeToLive - minTimeToLive)) + minTimeToLive);
            int angle = (int) (Math.random() * 360);

            // Initialize the particle
            particles[i].init(radius, speed, 0.0f, ttl, angle, Color.white);
            particles[i].setAlpha(1.0f, 1.0f / ttl * 100);
        }
    }

    public void render(float delta) {
        // Only draw when alive
        if (!isDead) {
            // Go through the particles and draw if not dead & update it
            for (int i = 0; i < numberOfParticles; i++)
                if (!particles[i].isDead()) {
                    updateParticle(particles[i]);
                    particles[i].draw(this.getPosition().x, this.getPosition().y, delta);
                }
            // Check if system dead
            if ((System.currentTimeMillis() - startTime) > maxTimeToLive)
                isDead = true;
        }
    }
    
    /*
    public void draw(float mX, float mY, float delta) {
        // Only draw when alive
        if (!isDead) {
            // Go through the particles and draw if not dead & update it
            for (int i = 0; i < numberOfParticles; i++)
                if (!particles[i].isDead()) {
                    updateParticle(particles[i]);
                    particles[i].draw(mX, mY, delta);
                }
            // Check if system dead
            if ((System.currentTimeMillis() - startTime) > maxTimeToLive)
                isDead = true;
        }
    }
	*/
    
    private void updateParticle(GLParticle mParticle) {
        /*
         * Basically what it does is check when we increment the radius of the
         * particle so it gives an explosion effect, i.e., particles go from the
         * center to the maxRadius, based on a random speed and angle. We also
         * check when the particle has died, whether from exceeding it's time to
         * live or the maximum radius for the system.
         */
        mParticle.setCurrentTime(System.currentTimeMillis());
        if (mParticle.getCurrentTime() - mParticle.getLastTime() > maxRadius / mParticle.getRadiusSpeed() / 50000) {
            mParticle.setXPos((int) (Math.cos(Math.PI / 180 * -mParticle.getAngle()) * mParticle.getRadius()));
            mParticle.setYPos((int) (Math.sin(Math.PI / 180 * -mParticle.getAngle()) * mParticle.getRadius()));

            float radius = mParticle.getRadius() + mParticle.getRadiusSpeed();
            mParticle.setRadius(radius);
            mParticle.updateAlpha();

            // Check if dead from ttl
            if ((System.currentTimeMillis() - mParticle.getStartTime()) > mParticle .getTimeToLive())
                mParticle.kill();
            
            // Check if dead from reaching the goal radius
            if (mParticle.getRadius() > maxRadius)
                mParticle.kill();

            mParticle.setLastTime(mParticle.getCurrentTime());
        }
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
