package net.elizeu.android.aerolitos.scenes.items;

import net.elizeu.android.aerolitos.scenes.AerolitosScene;

import org.igs.android.ogl.engine.Keyboard;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.igs.android.ogl.engine.model.a3ds.Model;
import org.igs.android.ogl.engine.model.a3ds.ModelImport;
import org.igs.android.ogl.engine.texture.Texture;
import org.igs.android.ogl.engine.texture.TextureBuffer;

public class Player extends AbstractEntity  {

    private Texture texture;
 
    private Model model;

    public float forwardX;

    public float forwardY;

    private int shotTimeout;

    private int shotInterval = 1000;

    private Renderer renderer;

    private ModelImport mi = new ModelImport();
    
    public Player(Renderer renderer) throws Exception {
    	this.renderer = renderer;
    	model = new Model("", null, renderer, new Point3f(0, 0, 0), new Point3f(90f, 0, 0), new Point3f(0.1f, 0.1f, 0.1f));
		mi.import3DS(renderer, model, "models/fighter_laser.3ds");
		model.generate();
		model.addProperty("lives", new Long(5L));
		this.setSceneObject(model);
		texture = TextureBuffer.getInstance().getTexture(renderer.getContext(), renderer.getGL10(), "models/player.bmp");
    }

    @Override
    public void update(long keyCode, EntityManager manager, float delta) {
    	if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_LEFT)) {
        //if (keyCode == AerolitosScene.D_LEFT) {
        	this.getSceneObject().getRotation().y -= (delta / -15.0f);
        } else if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_RIGHT)) {
        //if (keyCode == AerolitosScene.D_RIGHT) {
        	this.getSceneObject().getRotation().y += (delta / -15.0f);
        } 
        //this.getSceneObject().roll(inclinationAngle);
        forwardX = (float) -Math.sin(Math.toRadians(this.getSceneObject().getRotation().y));
        forwardY = (float) Math.cos(Math.toRadians(this.getSceneObject().getRotation().y));
        
        shotTimeout -= delta;
        if (shotTimeout <= 0) {
        	if (AerolitosScene.isFirePressed()) {
                fire(manager);
                shotTimeout = shotInterval;
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_UP)) {
        //if (keyCode == AerolitosScene.D_UP) {
        	this.getSceneObject().getVelocity().x += (forwardX * delta / 15) / 100.0f;
        	this.getSceneObject().getVelocity().y += (forwardY * delta / 15) / 100.0f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEYCODE_DPAD_DOWN)) {
        //if (keyCode ==  AerolitosScene.D_DOWN) {
        	this.getSceneObject().getVelocity().x -= (forwardX * delta / 15) / 100.0f;
        	this.getSceneObject().getVelocity().y -= (forwardY * delta / 15) / 100.0f;
        }
        super.update(keyCode, manager, delta);
    }

    private void fire(EntityManager manager) {
    	
        Shot shot = new Shot(this.renderer,
                this.getSceneObject().getPosition().x + (forwardX / 3),
                this.getSceneObject().getPosition().y + (forwardY / 3),
                forwardX,
                forwardY, 0.03f); 
        
		//shot.getSceneObject().getVelocity().inverse();
		//shot.getSceneObject().getPosition().inverse();
        manager.addEntity(shot);
        manager.shotFired();
    }

    public void render(float delta) {
    	this.texture.bind(this.renderer.getGL10());
    	this.getSceneObject().render(delta);
    }

    public float getSize() {
        return 0.3f;
    }

    public void collide(EntityManager manager, AbstractEntity other) {
        if (other instanceof Rock) {
        	this.getSceneObject().getVelocity().x = this.getSceneObject().getPosition().x - other.getSceneObject().getPosition().x;
        	this.getSceneObject().getVelocity().y = this.getSceneObject().getPosition().y - other.getSceneObject().getPosition().y;
            ((Rock) other).split(manager, this);
            manager.playerHit();
        }
    }
	
}
