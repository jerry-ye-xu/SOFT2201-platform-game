package stickman.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import stickman.model.Layer;

import java.util.ArrayList;
import java.util.List;

public class EntityViewImplMoving extends EntityViewImpl {
    protected static final double DEFAULT_SPEED = 1;
    protected static final double DEFAULT_JUMP = 12;
    protected static final double DROP_ACCEL = 0.40;

    protected final Layer layer;
    protected double width;
    protected double height;
    protected final double xSpeed;
    protected double ySpeed;
    protected double xPosition;
    protected double yPosition;
    protected boolean movingLeft;
    protected boolean movingRight;
    protected boolean facingL;
    protected boolean facingR;
    protected boolean canJump = true;

    protected final int frameCountRate = 60;
    protected int frameCount = 0;
    protected int frameIdx = 0;
    protected ImageView currentFrame;

    List<String> standingLeftFrames;
    List<String> standingRightFrames;
    List<String> walkRightFrames;
    List<String> walkLeftFrames;

    protected final int idxStartFramesMoveRight;
    protected final int numFramesMoveRight;
    protected final int idxStartFramesMoveLeft;
    protected final int numFramesMoveLeft;
    protected final int idxStartFramesStillRight;
    protected final int numFramesStillRight;
    protected final int idxStartFramesStillLeft;
    protected final int numFramesStillLeft;

    public EntityViewImplMoving(
        double startingXPos,
        double startingYPos,
        Layer layer
    ) {
        super();
        this.layer = layer;

        this.xPosition = startingXPos;
        this.yPosition = startingYPos - this.height;

        this.xSpeed = DEFAULT_SPEED;
        this.ySpeed = 0;
    }

    public void jump() {
        if (this.canJump) {
            this.ySpeed -= DEFAULT_JUMP;
        }

        // Character cannot jump twice before landing.
        this.canJump = false;
        this.frameIdx = 0;

    }

    @Override
    public void update(double xViewportOffset) {
        String newPath = entity.getImagePath();
        if (!imagePath.equals(newPath)) {
            imagePath = newPath;
            node.setImage(new Image(imagePath));
        }
        node.setX(entity.getXPos() - xViewportOffset);
        node.setY(entity.getYPos());
        node.setFitHeight(entity.getHeight());
        node.setFitWidth(entity.getWidth());
        node.setPreserveRatio(true);
        delete = false;
    }

    /*
        Getters and setters
     */

    public double getXPosition() { return this.xPosition; }

    public double getYPosition() { return this.yPosition; }

    public Layer getLayer() {
        return this.layer;
    }

    public int getFrameCount() { return this.frameCount; }

    public void setFrameCount(int frameCount) { this.frameCount = frameCount; }

    /*
        Loading and updating frames
     */

    private List<String> loadFrames(String pathStart, String pathEnd, int startInt, int numFrames) {
        List<String> imageFrames = new ArrayList<>();
        for (int i = startInt; i < startInt + numFrames; i++) {
            imageFrames.add(pathStart + i + pathEnd);
        }

        return imageFrames;
    }

    public boolean doUpdateFrame() {
        if (frameCount == frameCountRate) {
            frameCount = 0;
            return true;
        } else {
            frameCount += 1;
            return false;
        }
    }
}
