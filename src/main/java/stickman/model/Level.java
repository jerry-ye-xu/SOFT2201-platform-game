package stickman.model;

import stickman.view.EntityViewBlob;
import stickman.view.EntityViewFireball;
import stickman.view.EntityViewStickman;

import java.util.List;

public interface Level {
    List<Entity> getEntities();
    List<EntityViewBlob> getEntityViewBlobList();
    List<EntityViewFireball> getEntityViewFireballList();

    EntityViewStickman getEntityViewStickman();

    double getHeight();
    double getWidth();

    void tick();

    double getFloorHeight();
    double getHeroX();

    void jump();
    void moveLeft();
    void moveRight();
    void stopMoving();
}
