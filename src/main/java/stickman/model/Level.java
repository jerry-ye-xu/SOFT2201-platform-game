package stickman.model;

import stickman.view.EntityViewFireball;

import java.util.List;

public interface Level {
    List<Entity> getEntities();
    List<EntityViewFireball> getEntityViewFireballList();

    double getHeight();
    double getWidth();

    void tick();

    double getFloorHeight();
    double getHeroX();

    boolean jump();
    boolean moveLeft();
    boolean moveRight();
    boolean stopMoving();
}
