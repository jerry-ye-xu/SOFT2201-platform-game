package stickman.model;

public class EntityImplBlob extends EntityImpl implements EntityMoving {
    protected static final double DEFAULT_SPEED = 0.75;

    protected String attack;
    protected String startDirection;
    protected double movementRange;
    protected String imagePathStart;
    protected String imagePathEnd;

    protected double startingXPos;
    protected boolean movingLeft;
    protected boolean movingRight;

    private final double xSpeed;
    private double ySpeed;

    public EntityImplBlob(
        String type,
        double width,
        double height,
        double XPos,
        double YPos,
        String imagePath,
        Layer layer,
        double jumpHeight,
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
        this.startingXPos = XPos;
        this.xSpeed = DEFAULT_SPEED;
    }

    public void updateXPos() {
        if (this.XPos < this.startingXPos - this.movementRange) {
            this.XPos = this.startingXPos - this.movementRange;
            this.setMovement(false, true);

        } else if (this.XPos > this.startingXPos + this.movementRange) {
            this.XPos = this.startingXPos + this.movementRange;
            this.setMovement(true, false);
        }

        if (this.movingRight) {
            this.XPos += this.xSpeed * DEFAULT_SPEED;
        } else if (this.movingLeft) {
            this.XPos -= this.xSpeed * DEFAULT_SPEED;
        }
    }

    public void updateYPos() { }

    protected void setMovement(boolean movingLeft, boolean movingRight) {
        this.movingLeft = movingLeft;
        this.movingRight = movingRight;
    }

    /*
        Getters and setters
     */

    public double getJumpHeight() {
        return 0;
    }

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
