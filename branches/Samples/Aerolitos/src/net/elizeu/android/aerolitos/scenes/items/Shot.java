package net.elizeu.android.aerolitos.scenes.items;

import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.shape.Sphere;

public class Shot extends AbstractEntity {

	private Sphere sphere;
	
    private float life = 3000f;
	
	public Shot(Renderer renderer, float x, float y, float vx, float vy, float size) {
		sphere = new Sphere("", null, renderer, size, 15, 15, new Point3f(x, y, 0), new Point3f(0, 0, 0), new Point3f(1, 1, 1));
		sphere.getVelocity().setValues(vx, vy, 0);
		this.setSceneObject(sphere);
	}
	
	@Override
	public float getSize() {
		return this.sphere.getRadius();
	}

    @Override
    public void update(long keyCode, EntityManager manager, float delta) {
        super.update(keyCode, manager, delta);
        life -= delta;
        if (life < 0) {
            manager.removeEntity(this);
        }
        //this.sphere.update(delta);
    }
	
	@Override
	public void render(float delta) {
		this.sphere.render(delta);
	}

	@Override
	public void collide(EntityManager manager, AbstractEntity other) {
        if (other instanceof Rock) {
            ((Rock) other).split(manager, this);
            manager.removeEntity(this);
        }
	}
	
}
