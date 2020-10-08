package stickman.model;

public interface GameEngine {
    Level getCurrentLevel();

    void startLevel();

    Stickman getStickman();

    // Hero inputs - boolean for success (possibly for sound feedback)
    boolean jump();
    boolean moveLeft();
    boolean moveRight();
    boolean stopMoving();

    void tick();
}
