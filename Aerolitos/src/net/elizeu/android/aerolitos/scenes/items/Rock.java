package net.elizeu.android.aerolitos.scenes.items;

import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.shape.Sphere;

public class Rock extends AbstractEntity {

    private float rotateSpeed = (float) (Math.random() * 0.5f) + 1;

    private Sphere sphere;
    private Renderer renderer;
    
    public Rock(Renderer renderer, float x, float y, float size) {
    	/*
        this(renderer, x, y, size,  
                (float) (-4 + (Math.random() * 20)),
                (float) (-4 + (Math.random() * 20)));
        */
        this(renderer, x, y, size, 0.1f, 0.1f);
    	
    }

    private Rock(Renderer renderer, float x, float y, float size, float vx, float vy) {
    	this.renderer = renderer;
    	sphere = new Sphere("", null, renderer, size, 15, 15, new Point3f(x, y, 0), new Point3f(0, 0, 0), new Point3f(1, 1, 1));
    	sphere.setVelocity(new Point3f(vx, vy, 0));
    	this.setSceneObject(sphere);
    	
    	
    }

    @Override
    public void update(long keyCode, EntityManager manager, float delta) {
        super.update(keyCode, manager, delta);
        this.getSceneObject().getRotation().z += (delta / 10.0f) * rotateSpeed;
    }

    public void render(float delta) {
    	this.sphere.render(delta);
    }

    public float getSize() {
        return this.sphere.getRadius();
    }

    void split(EntityManager manager, AbstractEntity reason) {
        manager.removeEntity(this);
        manager.rockDestroyed(this, this.getSize());
        if (this.getSize() > 0.1) {
            float dx = this.getSceneObject().getPosition().x - reason.getSceneObject().getPosition().x;
            float dy = this.getSceneObject().getPosition().y - reason.getSceneObject().getPosition().y;
            dx *= (this.getSize() * 0.2f);
            dy *= (this.getSize() * 0.2f);
            float speed = 0.3f;
            Rock rock1 = new Rock(this.renderer, this.getSceneObject().getPosition().x + dy, this.getSceneObject().getPosition().y - dx, this.getSize() / 2, dy * speed, -dx * speed);
            Rock rock2 = new Rock(this.renderer, this.getSceneObject().getPosition().x - dy, this.getSceneObject().getPosition().y + dx, this.getSize() / 2, -dy * speed, dx * speed);

            manager.addEntity(rock1);
            manager.addEntity(rock2);
        }
    }

    public void collide(EntityManager manager, AbstractEntity other) {
    	this.getSceneObject().getVelocity().x = this.getSceneObject().getPosition().x - other.getSceneObject().getPosition().x;
    	this.getSceneObject().getVelocity().y = this.getSceneObject().getPosition().y - other.getSceneObject().getPosition().y;
        rotateSpeed = -rotateSpeed;
    }
    
}
