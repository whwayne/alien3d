package net.elizeu.android.aerolitos.scenes.items;

public interface EntityManager {

    public void removeEntity(AbstractEntity entity);

    public void addEntity(AbstractEntity entity);

    public void rockDestroyed(AbstractEntity entity, float size);

    public void playerHit();

    public void shotFired();

}
