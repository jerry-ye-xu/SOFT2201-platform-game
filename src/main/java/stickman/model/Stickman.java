package stickman.model;

import javafx.scene.image.ImageView;
import stickman.view.Platform;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Stickman {
    protected static final double DEFAULT_SPEED = 1;
    protected static final double DEFAULT_JUMP = 12;
    protected static final double DROP_ACCEL = 0.60;

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
        this.size = size;
        this.layer = layer;

        this.xSpeed = DEFAULT_SPEED;
        this.ySpeed = 0;

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

        for (Platform platform: level.getPlatforms()) {
            if (
                this.xPosition < platform.getStartWidth() &&
                this.xPosition > platform.getEndWidth() &&
                this.yPosition == (platform.getHeight() + platform.getHBox().getHeight()) &&
                this.onPlatform == true
            ) {
                // If you walk off a platform then fall down.
                this.onPlatform = false;
                this.updateYPos(level);
            }
        }

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



//        boolean canLandOnPlatform = false;
//
//        for (Platform platform: level.getPlatforms()) {
//            double platformHeight = platform.getHeight() + platform.getHBox().getHeight();
//            if (
//                this.xPosition >= platform.getStartWidth() &&
//                this.xPosition <= platform.getEndWidth() &&
//                this.yPosition >= platformHeight &&
//                this.onPlatform == false
//            ) {
//                // If you jump higher than the platform, then you can land on it.
//                canLandOnPlatform = true;
//            }
//
//            if (canLandOnPlatform && this.yPosition == platformHeight) {
//                this.yPosition = platform.getHeight() + platform.getHBox().getHeight();
//                this.onPlatform = true;
//                this.canJump = true;
//            }
//        }

        // Cannot fall lower than floorHeight.
        if (this.yPosition > level.getFloorHeight() - this.height) {
            this.yPosition = level.getFloorHeight() - this.height;
            this.canJump = true;
            this.ySpeed = 0;
        }
    }

    public ImageView updateFrame() {
        // If jumping, then reset frameIdx;
        System.out.println("this.frameCount: " + frameCount);
//        if (!this.canJump) {
//            this.frameIdx = 0;
//            String imagePath = walkLeftFrames.get(this.frameIdx);
//            return buildStickmanImage(imagePath);
//        }
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
        return buildStickmanImage(imagePath);
    }

    private ImageView buildStickmanImage(
        String imagePath
    ) {
        URL imageURL = this.getClass().getResource(imagePath);
        ImageView stickmanImage = new ImageView(imageURL.toExternalForm());
        stickmanImage.setX(this.xPosition);
        stickmanImage.setY(this.yPosition);
        stickmanImage.setFitWidth(this.width);
        stickmanImage.setFitHeight(this.height);
        stickmanImage.setPreserveRatio(true);

        return stickmanImage;
    }

    public double getXPosition() { return this.xPosition; }
    public double getYPosition() { return this.yPosition; }

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

    public Layer getLayer() {
        return this.layer;
    }

    private List<String> loadFrames(String pathStart, String pathEnd, int startInt, int numFrames) {
        List<String> imageFrames = new ArrayList<>();
        for (int i = startInt; i < startInt + numFrames; i++) {
            imageFrames.add(pathStart + i + pathEnd);
        }

        return imageFrames;
    }

    public boolean doUpdateFrame() {
        if (frameCount == frameCountRate) {
            System.out.println("frameCount == frameCountRate");
            frameCount = 0;
            return true;
        } else {
            System.out.println("else");
            frameCount += 1;
            return false;
        }
    }
    public int getFrameCount() { return this.frameCount; }
    public void setFrameCount(int frameCount) { this.frameCount = frameCount; }
}
