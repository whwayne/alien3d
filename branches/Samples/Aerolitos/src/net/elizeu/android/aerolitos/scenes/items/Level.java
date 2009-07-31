package net.elizeu.android.aerolitos.scenes.items;

import java.util.ArrayList;
import java.util.List;

import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.particles.oje2d.ParticleSystemExplode;


public class Level implements EntityManager {

    /** The current score */
    private int score;
    
    /** The number of lifes left */
    private int life = 10;
    
    /** True if the game is over */
    private boolean gameOver;
    
    /** The current level of play */
    private int level;

    /** The timeout for the game over message before resetting to the menu */
    private int gameOverTimeout;
    
    private Player player;
    
    /** The entities in the game */
    private List<AbstractEntity> entities = new ArrayList<AbstractEntity>();
    
    /** The list of entities to be added at the next opportunity */
    private List<AbstractEntity> addList = new ArrayList<AbstractEntity>();
    
    /** The list of entities to be removed at the next opportunity */
    private List<AbstractEntity> removeList = new ArrayList<AbstractEntity>();
	
    private Renderer renderer;
    private ParticleSystemExplode particleExplosion;
    
    public Level(ParticleSystemExplode particleExplosion, Renderer renderer) {
    	this.renderer = renderer;
    	this.particleExplosion = particleExplosion;
    }
    
	public void addEntity(AbstractEntity entity) {
		addList.add(entity);
	}

	public void playerHit() {
        life--;
        if (life < 0) {
            gameOver = true;
            gameOverTimeout = 6000;
            removeEntity(player);
        }
	}

	public void removeEntity(AbstractEntity entity) {
		removeList.add(entity);
	}

	public void rockDestroyed(AbstractEntity entity, float size) {
		score += (4 - size) * 100;
		//particleExplosion.setPosition(entity.getSceneObject().getPosition().x - 0.25f, entity.getSceneObject().getPosition().y - 0.25f, 0);
		
		/*
		Log.d("X", Float.toString(entity.getSceneObject().getPosition().x));
		Log.d("Y", Float.toString(entity.getSceneObject().getPosition().y));
		
		Log.d("X Explosion", Float.toString(particleExplosion.getPosition().x));
		Log.d("Y Explosion", Float.toString(particleExplosion.getPosition().y));
		*/
		
		//particleExplosion.start();
	}

	public void shotFired() {
		// TODO Auto-generated method stub
		
	}
	
    private void spawnRocks(int count) {
        int fails = 0;
        for (int i = 0; i < count; i++) {
        	float xp = (float)(-3.5f + (Math.random() * 3.5f));
        	float yp = (float)(-2.6f + (Math.random() * 2.6f));
        	
            Rock rock = new Rock(renderer, xp, yp, 0.1f);
            if (!rock.collides(player)) {
                entities.add(rock);
            } else {
                i--;
                fails++;
            }
            if (fails > 5) {
                return;
            }
        }
    }

    public void init() throws Exception {
        entities.clear();

        player = new Player(renderer);
        entities.add(player);

        life = 125;
        score = 0;
        level = 5;
        gameOver = false;

        spawnRocks(level);
    }
    
    public void render(float delta) {
    	for (AbstractEntity entity : this.entities) {
            entity.render(delta);
        }
    }
    
    public void update(long keyCode, float delta) {
        if (gameOver) {
            gameOverTimeout -= delta;
            if (gameOverTimeout < 0) {
                //window.changeToState(MenuState.NAME);
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            AbstractEntity entity = (AbstractEntity) entities.get(i);
            for (int j = i + 1; j < entities.size(); j++) {
                AbstractEntity other = (AbstractEntity) entities.get(j);

                if (entity.collides(other)) {
                    entity.collide(this, other);
                    other.collide(this, entity);
                }
            }
        }
        entities.removeAll(removeList);
        entities.addAll(addList);
        removeList.clear();
        addList.clear();
        int rockCount = 0;

        for (AbstractEntity entity : this.entities) {
        	entity.update(keyCode, this, delta);

            if (entity instanceof Rock) {
                rockCount++;
            }
        }
        if (rockCount == 0) {
            level++;
            spawnRocks(level);
        }
    }
    
}
