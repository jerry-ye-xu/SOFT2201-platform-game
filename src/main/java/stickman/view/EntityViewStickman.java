package stickman.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import stickman.model.Entity;
import stickman.model.EntityImplFireball;
import stickman.model.Layer;
import stickman.model.Level;
import stickman.view.EntityViewImplMoving;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EntityViewStickman extends EntityViewImplMoving {
    protected static final double DEFAULT_SPEED = 6;
    protected static final double DEFAULT_JUMP = 12;
    protected static final double DROP_ACCEL = 0.65;

    private final double xSpeed;
    private double yOffset = 0;

    private final double startingXPos;
    private final double startingYPos;

    private boolean facingL = false;
    private boolean facingR = true;

    private boolean canJump = true;
    private boolean onPlatform = false;

    private int numLives = 3;
    private int score = 0;

    private boolean mushroomPowerUp = false;
    private boolean winStatus = false;

    private List<String> standingLeftFrames;
    private List<String> standingRightFrames;
    private List<String> walkRightFrames;
    private List<String> walkLeftFrames;

    private final int idxStartFramesMoveRight = 1;
    private final int numFramesMoveRight = 4;
    private final int idxStartFramesMoveLeft = 5;
    private final int numFramesMoveLeft = 4;
    private final int idxStartFramesStillRight = 1;
    private final int numFramesStillRight = 3;
    private final int idxStartFramesStillLeft = 4;
    private final int numFramesStillLeft = 3;

    private final int frameCountRate = 30;

    public EntityViewStickman(Entity entity) {
        super(entity);

        this.xSpeed = DEFAULT_SPEED;
        this.ySpeed = 0;

        this.startingXPos = this.entity.getXPos();
        this.startingYPos = this.entity.getYPos();

        this.loadEntityFrames();
    }

    public void jump() {
        if (this.canJump) {
            this.ySpeed -= DEFAULT_JUMP;
        }

        // Character cannot jump twice before landing.
        this.canJump = false;
        this.frameIdx = 0;
    }

    public boolean fire(Level level) {
        if (this.mushroomPowerUp) {
            List<Entity> entityList = level.getEntities();

            String startDirection;
            if (this.facingL) {
                startDirection = "left";
            } else {
                startDirection = "right";
            }

            EntityImplFireball fireBallEntity = new EntityImplFireball(
                "fireball",
                25,
                25,
                this.getXPosition(),
                this.getYPosition() - this.yOffset,
                "/fireball1.png",
                Layer.ENTITY_LAYER,
                "none",
                startDirection,
                250,
                "fireball",
                ".png"
            );

            level.getEntityViewsMovingList().add(new EntityViewFireball(fireBallEntity));
        }
        return false;
    }
    public void setMovement(boolean movingLeft, boolean movingRight) {
        this.movingLeft = movingLeft;
        this.movingRight = movingRight;
        this.frameIdx = 0;
    }

    public void updateXPos(Level level) {
        if (this.movingRight) {
            this.xPosition += this.xSpeed;
            this.facingL = false;
            this.facingR = true;
        } else if (this.movingLeft) {
            this.xPosition -= this.xSpeed;
            this.facingL = true;
            this.facingR = false;
        }

        // Cannot go past the boundaries of the stage.
        if (this.xPosition >= level.getWidth()) {
            this.xPosition = level.getWidth();
        } else if (this.xPosition < 0) {
            this.xPosition = 0;
        }
    }

    public void updateYPos(Level level) {
        if (this.onPlatform) {
            this.ySpeed = 0;
            this.canJump = true;
        } else if (!this.onPlatform)  {
            this.ySpeed += DROP_ACCEL;
            this.yPosition += this.ySpeed;
        }

        if (this.onPlatform) {
            this.canJump = true;
        }

        // Cannot fall lower than floor height.
        if (this.yPosition > level.getFloorHeight() - this.height) {
            this.yPosition = level.getFloorHeight() - this.height;
            this.canJump = true;
            this.ySpeed = 0;
        }
    }

    public double getYOffset() {
        return this.yOffset;
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    public void resetPosition() {
        this.xPosition = this.startingXPos;
        this.yPosition = this.startingYPos;
        this.canJump = true;
        this.ySpeed = 0;
    }

    private double getViewOrder(Layer layer) {
        switch (layer) {
            case BACKGROUND: return 100.0;
            case ENTITY_LAYER: return 75.0;
            case FOREGROUND: return 50.0;
            case EFFECT: return 25.0;
            default: throw new IllegalStateException("Javac doesn't understand how enums work so now I have to exist");
        }
    }

    /*
        Loading and Updating Frames
     */

    private void loadEntityFrames() {
        String pathStartStanding = "/ch_stand";
        String pathEndStanding = ".png";

        String pathStartWalking = "/ch_walk";
        String pathEndWalking = ".png";

        this.standingLeftFrames = this.loadFramePaths(
                pathStartStanding,
                pathEndStanding,
                this.idxStartFramesStillLeft,
                this.numFramesStillLeft
        );
        this.standingRightFrames = this.loadFramePaths(
                pathStartStanding,
                pathEndStanding,
                this.idxStartFramesStillRight,
                this.numFramesStillRight
        );
        this.walkRightFrames = this.loadFramePaths(
                pathStartWalking,
                pathEndWalking,
                this.idxStartFramesMoveRight,
                this.numFramesMoveRight
        );
        this.walkLeftFrames = this.loadFramePaths(
                pathStartWalking,
                pathEndWalking,
                this.idxStartFramesMoveLeft,
                this.numFramesMoveLeft
        );
    }

    @Override
    public void update(double xViewportOffset) {
        String imagePath = this.chooseFrame();
        if (!this.imagePath.equals(imagePath)) {
            this.imagePath = imagePath;
            this.node.setImage(new Image(this.imagePath));
        }
        this.node.setX(this.xPosition - xViewportOffset);
        this.node.setY(this.yPosition - this.yOffset);
        this.node.setFitWidth(this.width);
        this.node.setFitHeight(this.height);
        this.node.setPreserveRatio(true);
    }

    @Override
    public boolean matchesEntity(Entity entity) {
        return this.entity.equals(entity);
    }

    @Override
    public void markForDelete() {
        this.delete = true;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public boolean isMarkedForDelete() {
        return this.delete;
    }

    private String chooseFrame() {
        String imagePath;
        if (this.movingRight) {
            if (doUpdateFrame()) {
                this.frameIdx = (this.frameIdx + 1) % this.numFramesMoveRight;
            }

            imagePath = walkRightFrames.get(this.frameIdx);

        } else if (this.movingLeft) {
            if (doUpdateFrame()) {
                this.frameIdx = (this.frameIdx + 1) % this.numFramesMoveLeft;
            }

            imagePath = walkLeftFrames.get(this.frameIdx);

        } else {
            if (this.facingL) {
                if (doUpdateFrame()) {
                    this.frameIdx = (this.frameIdx + 1) % this.numFramesStillLeft;
                }
                imagePath = standingLeftFrames.get(this.frameIdx);

            } else {
                if (doUpdateFrame()) {
                    this.frameIdx = (this.frameIdx + 1) % this.numFramesStillRight;
                }
                imagePath = standingRightFrames.get(this.frameIdx);

            }
        }
        return imagePath;
    }

    private boolean doUpdateFrame() {
        if (frameCount == frameCountRate) {
            frameCount = 0;
            return true;
        } else {
            frameCount += 1;
            return false;
        }
    }

    private List<String> loadFramePaths(String pathStart, String pathEnd, int startInt, int numFrames) {
        List<String> imageFrames = new ArrayList<>();
        for (int i = startInt; i < startInt + numFrames; i++) {
            imageFrames.add(pathStart + i + pathEnd);
        }

        return imageFrames;
    }

    /*
        Getters and Setters
     */

    public double getXPosition() { return this.xPosition; }

    public double getYPosition() { return this.yPosition; }

    public Layer getLayer() {
        return this.layer;
    }

    public int getFrameCount() { return this.frameCount; }

    public void setFrameCount(int frameCount) { this.frameCount = frameCount; }

    public double getWidth() { return this.width; }

    public double getHeight() { return this.height; }

    public Entity getEntity() { return this.entity; }

    public void setCanJump(boolean canJump) { this.canJump = canJump; }

    public boolean getCanJump() { return this.canJump; }

    public int getNumLives() { return this.numLives; }

    public void decreaseLive() { System.out.println("this.numLives: " + this.numLives); this.numLives -= 1; }

    public int getScore() { return this.score; }

    public void setOnPlatform(boolean onPlatform) { this.onPlatform = onPlatform; }

    public boolean getOnPlatform() { return this.onPlatform; }

    public void increaseScore(int increment) { this.score += increment; }

    public void setMushroomPowerUp(boolean mushroomPowerUp) {
        this.mushroomPowerUp = mushroomPowerUp;
    }

    public boolean getMushroomPowerUp() { return this.mushroomPowerUp; }

    public boolean getWinStatus() { return this.winStatus; }

    public void setWinStatus(boolean winStatus) {
        this.winStatus = winStatus;
    }

    public void setYspeed(int ySpeed) {
        this.ySpeed = 0;
    }

    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
    }
}
