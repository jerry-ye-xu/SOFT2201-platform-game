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

public class EntityViewStickman implements EntityView {
    protected static final double DEFAULT_SPEED = 2;
    protected static final double DEFAULT_JUMP = 12;
    protected static final double DROP_ACCEL = 0.40;

    private Entity entity;
    private boolean delete = false;
    private ImageView node;
    private String imagePath;

    private final Layer layer;
    private double width;
    private double height;
    private final double xSpeed;
    private double ySpeed;
    private double xPosition;
    private double yPosition;
    private double yOffset = 0;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean facingL = false;
    private boolean facingR = true;
    private boolean canJump = true;
    private boolean onPlatform = false;
    private final double startingXPos;
    private final double startingYPos;

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
    private int frameCount = 0;
    private int frameIdx = 0;

    public EntityViewStickman(Entity entity) {
        this.entity = entity;
        this.layer = this.entity.getLayer();

        this.width = this.entity.getWidth();
        this.height = this.entity.getHeight();

        System.out.println("yPosition: " + yPosition);
        this.xPosition = this.entity.getXPos();
        this.yPosition = this.entity.getYPos() - this.entity.getHeight();
        this.startingXPos = this.entity.getXPos();
        this.startingYPos = this.entity.getYPos();
        System.out.println("yPosition: " + yPosition);

        this.xSpeed = DEFAULT_SPEED;
        this.ySpeed = 0;

        this.imagePath = this.entity.getImagePath();
        URL imageURL = this.getClass().getResource(this.imagePath);
        this.node = new ImageView(imageURL.toExternalForm());
        this.node.setViewOrder(getViewOrder(this.entity.getLayer()));

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
            System.out.println("Shooting fireball...");
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

//            entityList.add(fireBallEntity);

            level.getEntityViewFireballList().add(new EntityViewFireball(fireBallEntity));
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
//            System.out.println("Update movingRight");
//            System.out.println("DEFAULT_SPEED: " + DEFAULT_SPEED);
//            System.out.println("this.xSpeed: " + this.xSpeed);
//            System.out.println("this.xPosition: " + this.xPosition);
            this.xPosition += this.xSpeed * DEFAULT_SPEED;
//            System.out.println("this.xPosition: " + this.xPosition);
            this.facingL = false;
            this.facingR = true;
        } else if (this.movingLeft) {
//            System.out.println("Update movingLeft");
//            System.out.println("DEFAULT_SPEED: " + DEFAULT_SPEED);
//            System.out.println("this.xSpeed: " + this.xSpeed);
//            System.out.println("this.xPosition: " + this.xPosition);
            this.xPosition -= this.xSpeed * DEFAULT_SPEED;
//            System.out.println("this.xPosition: " + this.xPosition);
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
        if (this.onPlatform) {
            this.ySpeed = 0;
        } else if (!this.onPlatform)  {
            this.ySpeed += DROP_ACCEL;
            this.yPosition += this.ySpeed;
        }
//        System.out.println("DECREASE: this.yPosition: " + this.yPosition);

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

    public void updateXPos() { }

    public void updateYPos() { }

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
