package stickman.model;

import stickman.view.EntityViewBlob;
import stickman.view.EntityViewStickman;

import java.util.List;

public interface GameEngine {
    Level getCurrentLevel();

    void startLevel();

    // Hero inputs - boolean for success (possibly for sound feedback)
    boolean jump();
    boolean fire();
    boolean moveLeft();
    boolean moveRight();
    boolean stopMoving();

    void tick();
}
