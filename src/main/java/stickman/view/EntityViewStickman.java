package stickman.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import stickman.model.Entity;
import stickman.model.Layer;
import stickman.model.Level;
import stickman.view.EntityViewImplMoving;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EntityViewStickman extends EntityViewImplMoving {
    protected static final double DEFAULT_SPEED = 2;
    protected static final double DEFAULT_JUMP = 12;
    protected static final double DROP_ACCEL = 0.40;

    private final Layer layer;
    private double width;
    private double height;
    private final double xSpeed;
    private double ySpeed;
    private double xPosition;
    private double yPosition;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean facingL = false;
    private boolean facingR = true;
    private boolean canJump = true;
    private boolean onPlatform = false;

    private int numLives = 4;
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

    private int frameCount = 0;
    private int frameIdx = 0;

    public EntityViewStickman(Entity entity) {
        super(entity);
        this.entity = entity;
        this.layer = this.entity.getLayer();

        this.width = this.entity.getWidth();
        this.height = this.entity.getHeight();

        System.out.println("yPosition: " + yPosition);
        this.xPosition = this.entity.getXPos();
        this.yPosition = this.entity.getYPos() - this.entity.getHeight();
        System.out.println("yPosition: " + yPosition);

        this.xSpeed = DEFAULT_SPEED;
        this.ySpeed = 0;

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

    public void setMovement(boolean movingLeft, boolean movingRight) {
        this.movingLeft = movingLeft;
        this.movingRight = movingRight;
        this.frameIdx = 0;
    }

    public void updateXPos(Level level) {
        if (this.movingRight) {
            System.out.println("Update movingRight");
            System.out.println("DEFAULT_SPEED: " + DEFAULT_SPEED);
            System.out.println("this.xSpeed: " + this.xSpeed);
            System.out.println("this.xPosition: " + this.xPosition);
            this.xPosition += this.xSpeed * DEFAULT_SPEED;
            System.out.println("this.xPosition: " + this.xPosition);
            this.facingL = false;
            this.facingR = true;
        } else if (this.movingLeft) {
            System.out.println("Update movingLeft");
            System.out.println("DEFAULT_SPEED: " + DEFAULT_SPEED);
            System.out.println("this.xSpeed: " + this.xSpeed);
            System.out.println("this.xPosition: " + this.xPosition);
            this.xPosition -= this.xSpeed * DEFAULT_SPEED;
            System.out.println("this.xPosition: " + this.xPosition);
            this.facingL = true;
            this.facingR = false;
        }

//        for (Platform platform: level.getPlatforms()) {
//            if (
//                this.xPosition < platform.getStartWidth() &&
//                this.xPosition > platform.getEndWidth() &&
//                this.yPosition == (platform.getHeight() + platform.getHBox().getHeight()) &&
//                this.onPlatform == true
//            ) {
//                // If you walk off a platform then fall down.
//                this.onPlatform = false;
//                this.updateYPos(level);
//            }
//        }

        // Cannot go past the boundaries of the stage.
        if (this.xPosition >= level.getWidth()) {
            this.xPosition = level.getWidth();
        } else if (this.xPosition < 0) {
            this.xPosition = 0;
        }
    }

    public void updateYPos(Level level) {
        this.ySpeed += DROP_ACCEL;
        this.yPosition += this.ySpeed;
//        System.out.println("DECREASE: this.yPosition: " + this.yPosition);

        boolean canLandOnPlatform = false;

//        for (Platform platform: level.getPlatforms()) {
//            double platformHeight = platform.getHeight() - platform.getHBox().getHeight();
//            System.out.println("this.xPosition: " + this.xPosition);
//            System.out.println("platform.getStartWidth(): " + platform.getStartWidth());
//            System.out.println("platform.getEndWidth(): " + platform.getEndWidth());
//            System.out.println("this.xPosition: " + this.xPosition);
//            System.out.println("this.yPosition: " + this.yPosition);
//            System.out.println("this.onPlatform: " + this.onPlatform);
//            System.out.println("platformHeight: " + platformHeight);
//            if (
//                this.xPosition >= platform.getStartWidth() &&
//                this.xPosition <= platform.getEndWidth() &&
//                this.yPosition < platformHeight &&
//                this.onPlatform == false
//            ) {
//                // If you jump higher than the platform, then you can land on it.
//                canLandOnPlatform = true;
//            }
//
//            if (canLandOnPlatform && this.yPosition <= platformHeight) {
//                this.yPosition = platform.getHeight() - platform.getHBox().getHeight();
//                this.onPlatform = true;
//                this.canJump = true;
//                this.ySpeed = 0;
//            }

        // Cannot fall lower than floorHeight.
        if (this.yPosition > level.getFloorHeight() - this.height) {
            this.yPosition = level.getFloorHeight() - this.height;
            this.canJump = true;
            this.ySpeed = 0;
        }
    }

    /*
        Loading and Updating Frames
     */

    private void loadEntityFrames() {
        this.standingLeftFrames = this.loadFramePaths(
                "/ch_stand",
                ".png",
                this.idxStartFramesStillLeft,
                this.numFramesStillLeft
        );
        this.standingRightFrames = this.loadFramePaths(
                "/ch_stand",
                ".png",
                this.idxStartFramesStillRight,
                this.numFramesStillRight
        );
        this.walkRightFrames = this.loadFramePaths(
                "/ch_walk",
                ".png",
                this.idxStartFramesMoveRight,
                this.numFramesMoveRight
        );
        this.walkLeftFrames = this.loadFramePaths(
                "/ch_walk",
                ".png",
                this.idxStartFramesMoveLeft,
                this.numFramesMoveLeft
        );
    }

    @Override
    public void update(double xViewportOffset) {
        String imagePath = this.chooseFrame();
        System.out.println("BEFORE stickman UPDATE");
        System.out.println("xPosition: " + xPosition);
        System.out.println("xViewportOffset: " + xViewportOffset);
        if (!this.imagePath.equals(imagePath)) {
            this.imagePath = imagePath;
            this.node.setImage(new Image(this.imagePath));
        }
        System.out.println("AFTER stickman UPDATE");
        System.out.println("xPosition: " + xPosition);
        System.out.println("xViewportOffset: " + xViewportOffset);
        this.node.setX(this.xPosition - xViewportOffset);
        this.node.setY(this.yPosition);
        this.node.setFitWidth(this.width);
        this.node.setFitHeight(this.height);
        this.node.setPreserveRatio(true);
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
//                System.out.println("standingRightFrames: " + standingRightFrames);
//                System.out.println("frameIdx: " + frameIdx);
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

//    private ImageView buildStickmanImage(
//        String imagePath,
//        double xViewportOffset
//    ) {
//        URL imageURL = this.getClass().getResource(imagePath);
//        ImageView stickmanImage = new ImageView(imageURL.toExternalForm());
//        stickmanImage.setX(this.xPosition - xViewportOffset);
//        stickmanImage.setY(this.yPosition);
//        stickmanImage.setFitWidth(this.width);
//        stickmanImage.setFitHeight(this.height);
//        stickmanImage.setPreserveRatio(true);
//
//        return stickmanImage;
//    }

    /*
        Getters and Setters
     */

    public double getXPosition() { return this.xPosition; }

    public double getWidth() { return this.width; }

    public boolean getCanJump() { return this.canJump; }

    public int getLives() { return this.numLives; }

    public void decreaseLive() { this.numLives -= -1; }

    public int getScore() { return this.score; }

    public void setOnPlatform(boolean onPlatform) { this.onPlatform = onPlatform; }

    public boolean getOnPlatform() { return this.onPlatform; }

    public void increaseScore(int increment) { this.score += increment; }

    public void setMushroomPowerUp(boolean mushroomPowerUp) {
        this.mushroomPowerUp = mushroomPowerUp;
    }

    public void setWinStatus(boolean winStatus) {
        this.winStatus = winStatus;
    }

}
