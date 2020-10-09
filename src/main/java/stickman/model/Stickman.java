package stickman.model;

import javafx.scene.image.ImageView;
import stickman.view.EntityViewImplMoving;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Stickman extends EntityViewImplMoving {
    protected static final double DEFAULT_SPEED = 1.5;
    protected static final double DEFAULT_JUMP = 12;
    protected static final double DROP_ACCEL = 0.40;

    protected final String size;
    protected final Layer layer;
    protected double width;
    protected double height;
    protected final double xSpeed;
    protected double ySpeed;
    protected double xPosition;
    protected double yPosition;
    protected boolean movingLeft = false;
    protected boolean movingRight = false;
    protected boolean facingL = false;
    protected boolean facingR = true;
    protected boolean canJump = true;
    protected boolean onPlatform = false;

    protected int numLives = 4;
    protected int score = 0;

    protected boolean mushroomPowerUp = false;
    protected boolean winStatus = false;

    protected final int frameCountRate = 60;
    protected int frameCount = 0;
    protected int frameIdx = 0;
    protected ImageView currentFrame;

    List<String> standingLeftFrames;
    List<String> standingRightFrames;
    List<String> walkRightFrames;
    List<String> walkLeftFrames;

    protected final int idxStartFramesMoveRight = 1;
    protected final int numFramesMoveRight = 4;
    protected final int idxStartFramesMoveLeft = 5;
    protected final int numFramesMoveLeft = 4;
    protected final int idxStartFramesStillRight = 1;
    protected final int numFramesStillRight = 3;
    protected final int idxStartFramesStillLeft = 4;
    protected final int numFramesStillLeft = 3;

    public Stickman(
        String size,
        double startingXPos,
        double startingYPos,
        Layer layer
    ) {
        super(startingXPos, startingYPos, layer);
        this.xSpeed = DEFAULT_SPEED;
        this.ySpeed = 0;

        this.size = size;

        this.setCharacterSize(size);
        this.xPosition = startingXPos;
        this.yPosition = startingYPos - this.height;

        this.standingLeftFrames = this.loadFrames(
            "/ch_stand",
            ".png",
            this.idxStartFramesStillLeft,
            this.numFramesStillLeft
        );
        this.standingRightFrames = this.loadFrames(
                "/ch_stand",
                ".png",
                this.idxStartFramesStillRight,
                this.numFramesStillRight
        );
        this.walkRightFrames = this.loadFrames(
            "/ch_walk",
            ".png",
            this.idxStartFramesMoveRight,
            this.numFramesMoveRight
        );
        this.walkLeftFrames = this.loadFrames(
            "/ch_walk",
            ".png",
            this.idxStartFramesMoveLeft,
            this.numFramesMoveLeft
        );
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
            this.xPosition += this.xSpeed * DEFAULT_SPEED;
            this.facingL = false;
            this.facingR = true;
        } else if (this.movingLeft) {
            this.xPosition -= this.xSpeed * DEFAULT_SPEED;
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
        if (this.xPosition > level.getWidth()) {
            this.xPosition = level.getWidth() - 1;
        } else if (this.xPosition < 0) {
            this.xPosition = 1;
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

    @Override
    public ImageView update(double xViewportOffset) {
        String imagePath = this.chooseFrame();
        URL imageURL = this.getClass().getResource(imagePath);
        ImageView stickmanImage = new ImageView(imageURL.toExternalForm());
        stickmanImage.setX(this.xPosition - xViewportOffset);
        stickmanImage.setY(this.yPosition);
        stickmanImage.setFitWidth(this.width);
        stickmanImage.setFitHeight(this.height);
        stickmanImage.setPreserveRatio(true);

        return stickmanImage;
    }

    @Override
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

    private ImageView buildStickmanImage(
        String imagePath,
        double xViewportOffset
    ) {
        URL imageURL = this.getClass().getResource(imagePath);
        ImageView stickmanImage = new ImageView(imageURL.toExternalForm());
        stickmanImage.setX(this.xPosition - xViewportOffset);
        stickmanImage.setY(this.yPosition);
        stickmanImage.setFitWidth(this.width);
        stickmanImage.setFitHeight(this.height);
        stickmanImage.setPreserveRatio(true);

        return stickmanImage;
    }

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

    public void setCharacterSize(String size) {
        if (size.equals("normal")) {
            this.width = 30;
            this.height = 45;
        } else if (size.equals("large")) {
            this.width = 45;
            this.height = 60;
        }
    }

}
