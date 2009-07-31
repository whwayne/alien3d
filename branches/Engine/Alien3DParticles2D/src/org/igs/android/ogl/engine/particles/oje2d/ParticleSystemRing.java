package org.igs.android.ogl.engine.particles.oje2d;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.scene.SceneObject;
import org.igs.android.ogl.engine.sprite.Sprite;
import org.igs.android.ogl.engine.utils.Color;

public class ParticleSystemRing extends SceneObject {

	private GLParticle[] particles = null;

    private int numberOfParticles;

    private int minRadius;

    private int maxRadius;

    private float minSpeed;

    private float maxSpeed;

    private int minTimeToLive;

    private int maxTimeToLive;

    private boolean forever;

    private boolean isDead;

    private long startTime;
    
    Renderer renderer;

    public ParticleSystemRing(String id, SceneObject parent, Renderer renderer, String mFilename, int mNumber, Point3f position, Point3f rotation, Point3f scale, TransparencyEnum transparencyType) throws AndromedaException {
    	super(id, parent, renderer, position, rotation, scale);
    	isDead = true;
        numberOfParticles = mNumber;
        particles = new GLParticle[numberOfParticles];
        this.renderer = renderer;
        // Create the particles (loading of textures)
        Sprite image = new Sprite("", this, renderer, mFilename, 32, 32, position, rotation, scale, transparencyType);
        for (int i = 0; i < numberOfParticles; i++)
            particles[i] = new GLParticle(image);
    }

    public void setRadius(int mMinRadius, int mMaxRadius) {
        minRadius = mMinRadius;
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

    public void setLoop(boolean mValue) {
        forever = mValue;
    }

    public void stop() {
    	this.isDead = true;
    }
    
    public void start() {
        startTime = System.currentTimeMillis();
        isDead = false;

        // Init the particles
        for (int i = 0; i < numberOfParticles; i++) {
            // Get the random values
            int radius =
                    (int) ((Math.random() * (maxRadius - minRadius)) + minRadius);
            float speed =
                    (float) ((Math.random() * (maxSpeed - minSpeed)) + minSpeed);
            int ttl =
                    (int) ((Math.random() * (maxTimeToLive - minTimeToLive)) + minTimeToLive);
            int angle = (int) (Math.random() * 360);
            Color color =
                    new Color((float) Math.random(), (float) Math.random(),
                            (float) Math.random(), (float) Math.random());

            // Initialize the particle
            particles[i].init(radius, 0, speed, ttl, angle, color);
            particles[i].setAlpha(1.0f, 1.0f / ttl * 100);
        }
    }

    public void render(float delta) {
        // Only draw when alive
        if (!isDead) {
            // Go through the particles and draw if not dead & update
            for (int i = 0; i < numberOfParticles; i++)
                if (!particles[i].isDead()) {
                    particles[i].draw(this.getPosition().x, this.getPosition().y, delta);
                    updateParticle(particles[i]);
                }

            // Check if system dead
            if ((System.currentTimeMillis() - startTime) > maxTimeToLive
                    && !forever)
                isDead = true;
        }
    }

    /*
    public void render(int mX, int mY, float delta) {
        // Only draw when alive
        if (!isDead) {
            // Go through the particles and draw if not dead & update
            for (int i = 0; i < numberOfParticles; i++)
                if (!particles[i].isDead()) {
                    particles[i].draw(mX, mY, delta);
                    updateParticle(particles[i]);
                }

            // Check if system dead
            if ((System.currentTimeMillis() - startTime) > maxTimeToLive
                    && !forever)
                isDead = true;
        }
    }
	*/
    
    private void updateParticle(GLParticle mParticle) {
        /*
         * This update checks when to update the particle, based on laps per
         * second (360 degrees). We update the particle's position based on the
         * angle and then the angle, based on the speed. Also some checking is
         * made to normalize the angle. The particle's death occurs when it
         * exceeds it's time to live.
         */
        mParticle.setCurrentTime(System.currentTimeMillis());
        if (mParticle.getCurrentTime() - mParticle.getLastTime() > 360 / mParticle
                .getRotationSpeed() / 1000) {
            mParticle.setXPos((int) (Math.cos(Math.PI / 180
                    * -mParticle.getAngle()) * mParticle.getRadius()));
            mParticle.setYPos((int) (Math.sin(Math.PI / 180
                    * -mParticle.getAngle()) * mParticle.getRadius()));

            float angle = mParticle.getAngle() + mParticle.getRotationSpeed();
            mParticle.setAngle(angle);
            mParticle.updateAlpha();

            if (angle > 360)
                angle -= 360;
            if (angle < 0)
                angle += 360;

            // Check if dead
            if ((System.currentTimeMillis() - mParticle.getStartTime()) > mParticle
                    .getTimeToLive())
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
        int radius =
                (int) ((Math.random() * (maxRadius - minRadius)) + minRadius);
        float speed =
                (float) ((Math.random() * (maxSpeed - minSpeed)) + minSpeed);
        int ttl =
                (int) ((Math.random() * (maxTimeToLive - minTimeToLive)) + minTimeToLive);
        int angle = (int) (Math.random() * 360);
        Color color =
                new Color((float) Math.random(), (float) Math.random(),
                        (float) Math.random(), (float) Math.random());

        // Initialize the particle
        mParticle.init(radius, 0, speed, ttl, angle, color);
        mParticle.setAlpha(1.0f, 1.0f / ttl * 100);
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
