package stickman.model;

public class EntityImplFireball extends EntityImpl implements EntityMoving {
    protected String attack;
    protected String startDirection;
    protected double movementRange;
    protected String imagePathStart;
    protected String imagePathEnd;

    public EntityImplFireball(
            String type,
            double width,
            double height,
            double XPos,
            double YPos,
            String imagePath,
            Layer layer,
            String attack,
            String startDirection,
            double movementRange,
            String imagePathStart,
            String imagePathEnd
    ) {
        super(type, width, height, XPos, YPos, imagePath, layer);

        this.attack = attack;
        this.startDirection = startDirection;
        this.movementRange = movementRange;
        this.imagePathStart = imagePathStart;
        this.imagePathEnd = imagePathEnd;
    }

    /*
        Getters and setters
     */

    public double getJumpHeight() { return 0; }

    public void setJumpHeight(double jumpHeight) { }

    public String getAttack() {
        return attack;
    }

    public void setAttack(String attack) {
        this.attack = attack;
    }

    public String getStartDirection() {
        return startDirection;
    }

    public void setStartDirection(String startDirection) {
        this.startDirection = startDirection;
    }

    public double getMovementRange() {
        return movementRange;
    }

    public void setMovementRange(double movementRange) {
        this.movementRange = movementRange;
    }

    public String getImagePathStart() {
        return imagePathStart;
    }

    public void setImagePathStart(String imagePathStart) {
        this.imagePathStart = imagePathStart;
    }

    public String getImagePathEnd() {
        return imagePathEnd;
    }

    public void setImagePathEnd(String imagePathEnd) {
        this.imagePathEnd = imagePathEnd;
    }

}

