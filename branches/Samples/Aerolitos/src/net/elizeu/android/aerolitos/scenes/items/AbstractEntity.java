package net.elizeu.android.aerolitos.scenes.items;

import org.igs.android.ogl.engine.scene.SceneObject;

public abstract class AbstractEntity {

    private SceneObject sceneObject;
    
    protected void setSceneObject(SceneObject sceneObject) {
    	this.sceneObject = sceneObject;	
    }
    
    public SceneObject getSceneObject() {
    	return this.sceneObject;
    }
    
    private float HALF_WIDTH = 2.6f;
    private float HALF_HEIGHT = 3.5f;
    
    public void update(long keyCode, EntityManager manager, float delta) {
    	sceneObject.getPosition().x += (sceneObject.getVelocity().x * delta) / 1000.0f;  
    	sceneObject.getPosition().y += (sceneObject.getVelocity().y * delta) / 1000.0f;

	    if (sceneObject.getPosition().x < -HALF_WIDTH) {
	    	sceneObject.getPosition().x = HALF_WIDTH - 0.1f;
	    }
	    if (sceneObject.getPosition().x > HALF_WIDTH) {
	    	sceneObject.getPosition().x = -(HALF_WIDTH - 0.1f);
	    }
	    if (sceneObject.getPosition().y < -HALF_HEIGHT) {
	    	sceneObject.getPosition().y = HALF_HEIGHT - 0.1f;
	    }
	    if (sceneObject.getPosition().y > HALF_HEIGHT) {
	    	sceneObject.getPosition().y = -(HALF_HEIGHT - 0.1f);
	    }
    }

    //private int count = 0;
    
    public boolean collides(AbstractEntity other) {
    	/*
    	if (count > 50) {
    		
    		Log.d("this:" + this.getClass().getName(), this.getSceneObject().getPosition().toString());
    		Log.d("other" + other.getClass().getName(), other.getSceneObject().getPosition().toString());
    		float distance = this.getSceneObject().getPosition().distanceSquaredTo2D(other.getSceneObject().getPosition());
    		Log.d("distanceSquaredTo2D", Float.toString(distance));
    		float distance2 =  this.getSceneObject().getPosition().distanceTo2D(other.getSceneObject().getPosition());
    		Log.d("distance", Float.toString(distance2));
    		Log.d("size", Float.toString(this.getSize() + other.getSize()));
    		count = 0;
    	} else {
    		count++;
    	}
    	*/
    	float distance =  this.getSceneObject().getPosition().distanceTo2D(other.getSceneObject().getPosition());
    	float size = this.getSize() + other.getSize();
    	/*
    	if (distance < size) {
    		Log.d("this:" + this.getClass().getName(), this.getSceneObject().getPosition().toString());
    		Log.d("other" + other.getClass().getName(), other.getSceneObject().getPosition().toString());
    		Log.d("distance", Float.toString(distance));
    		Log.d("size", Float.toString(this.getSize() + other.getSize()));
    	}
    	*/
    	return (distance < size);
    }
	
    public abstract void render(float delta);

    public abstract float getSize();

    public abstract void collide(EntityManager manager, AbstractEntity other);

}
