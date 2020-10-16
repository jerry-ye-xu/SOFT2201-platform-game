package stickman.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import stickman.model.Entity;
import stickman.model.Layer;

import java.util.ArrayList;
import java.util.List;

public class EntityViewImplMoving extends EntityViewImpl {
    protected static final double DEFAULT_SPEED = 1;
    protected static final double DEFAULT_JUMP = 12;
    protected static final double DROP_ACCEL = 0.40;
    protected final int frameCountRate = 45;

    protected final Layer layer;
    protected double width;
    protected double height;
    protected double xSpeed;
    protected double ySpeed;
    protected double xPosition;
    protected double yPosition;
    protected boolean movingLeft = false;
    protected boolean movingRight = false;
    protected boolean canJump;
    protected final double startingXPos;

    protected int frameCount = 0;
    protected int frameIdx = 0;

    public EntityViewImplMoving(Entity entity) {
        super(entity);
        this.layer = this.entity.getLayer();

        this.width = this.entity.getWidth();
        this.height = this.entity.getHeight();

        this.xPosition = this.entity.getXPos();
        this.yPosition = this.entity.getYPos();

        this.startingXPos = this.entity.getXPos();

        this.xSpeed = DEFAULT_SPEED;
        this.ySpeed = 0;
    }

    /*
        Movement
     */

    protected void setMovement(boolean movingLeft, boolean movingRight) {
        this.movingLeft = movingLeft;
        this.movingRight = movingRight;
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

    private List<String> loadFramePaths(String pathStart, String pathEnd, int startInt, int numFrames) {
        List<String> imageFrames = new ArrayList<>();
        for (int i = startInt; i < startInt + numFrames; i++) {
            imageFrames.add(pathStart + i + pathEnd);
        }

        return imageFrames;
    }
}
