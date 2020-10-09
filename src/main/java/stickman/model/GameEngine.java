package stickman.model;

import stickman.view.EntityViewStickman;

public interface GameEngine {
    Level getCurrentLevel();

    void startLevel();

    EntityViewStickman getEntityViewStickman();

    // Hero inputs - boolean for success (possibly for sound feedback)
    boolean jump();
    boolean moveLeft();
    boolean moveRight();
    boolean stopMoving();

    void tick();
}
