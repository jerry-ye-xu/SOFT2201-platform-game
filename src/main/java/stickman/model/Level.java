package stickman.model;

import javafx.scene.layout.Pane;
import stickman.view.EntityViewBlob;
import stickman.view.EntityViewFireball;
import stickman.view.EntityViewImplMoving;
import stickman.view.EntityViewStickman;

import java.util.List;

public interface Level {
    List<Entity> getEntities();
//    List<EntityViewBlob> getEntityViewBlobList();
//    List<EntityViewFireball> getEntityViewFireballList();
//
    EntityViewStickman getEntityViewStickman();

    public List<EntityViewImplMoving> getEntityViewsMovingList();

    void addEntityViewsToPane(Pane pane);

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
