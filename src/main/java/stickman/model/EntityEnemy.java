package stickman.model;

public interface EntityEnemy extends Entity {
    double getJumpHeight();

    void setJumpHeight(double jumpHeight);

    String getAttack();

    void setAttack(String attack);

    String getStartDirection();

    void setStartDirection(String startDirection);

    double getMovementRange();

    void setMovementRange(double movementRange);

    String getImagePathStart();

    void setImagePathStart(String imagePathStart);

    String getImagePathEnd();

    void setImagePathEnd(String imagePathEnd);
}
