package stickman.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import stickman.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EntityViewBlob extends EntityViewImplMoving {
    private final Layer layer;
    private double width;
    private double height;
    private final double xSpeed;
    private double ySpeed;
    private double xPosition;
    private double yPosition;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean canJump;

    private List<String> standingFrames;
    private int numStandingFrames;
    private char[] tagList = {'a', 'b'};
    private double movementRange;
    private final double startingXPos;

    private int frameCount = 0;
    private int frameIdx = 0;

    public EntityViewBlob(Entity entity) {
        super(entity);

        this.layer = this.entity.getLayer();

        this.width = this.entity.getWidth();
        this.height = this.entity.getHeight();

        this.xSpeed = DEFAULT_SPEED;
        this.ySpeed = 0;

        this.startingXPos = this.entity.getXPos();

        EntityEnemy entityEnemy = (EntityEnemy) this.entity;
        this.setMovementRange(entityEnemy);
        this.setInitialDirection(entityEnemy);

        this.numStandingFrames = this.tagList.length;
        this.loadEntityFrames("slimeB", ".png", this.tagList);
    }

    @Override
    public void update(double xViewportOffset) {
        String imagePath = this.chooseFrame();

        if (!this.imagePath.equals(imagePath)) {
            this.imagePath = imagePath;
            this.node.setImage(new Image(this.imagePath));
        }
        this.updateXPos();
        this.updateYPos();
        this.node.setX(this.xPosition - xViewportOffset);
        this.node.setY(this.yPosition);
        this.node.setFitWidth(this.width);
        this.node.setFitHeight(this.height);
        this.node.setPreserveRatio(true);
    }

    /*
        Movement
     */

    public void updateXPos() {
        if (this.xPosition < this.startingXPos - this.movementRange) {
            this.xPosition = this.startingXPos - this.movementRange;
            this.setMovement(false, true);

        } else if (this.xPosition > this.startingXPos + this.movementRange) {
            this.xPosition = this.startingXPos - this.movementRange;
            this.setMovement(true, false);
        }

        if (this.movingRight) {
            this.xPosition += this.xSpeed * DEFAULT_SPEED;
        } else if (this.movingLeft) {
            this.xPosition -= this.xSpeed * DEFAULT_SPEED;
        }
    }

    public void updateYPos() { }

    /*
        Loading and updating frames
     */

    private void loadEntityFrames(String pathStart, String pathEnd, char[] tagList) {
        this.standingFrames = this.loadFramePaths(
            pathStart,
            pathEnd,
            tagList
        );
    }

    private List<String> loadFramePaths(String pathStart, String pathEnd, char[] tagList) {
        List<String> imageFrames = new ArrayList<>();
        for (char tag: tagList) {
            imageFrames.add(pathStart + tag + pathEnd);
        }

        return imageFrames;
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

    private String chooseFrame() {
        String imagePath;
        if (doUpdateFrame()) {
            this.frameIdx = (this.frameIdx + 1) % this.numStandingFrames;
        }
        imagePath = standingFrames.get(this.frameIdx);
        return imagePath;
    }

    /*
        Initialisation Helpers
     */

    private void setMovementRange(EntityEnemy entityEnemy) {
        this.movementRange = entityEnemy.getMovementRange();
    }

    private void setInitialDirection(EntityEnemy entityEnemy) {
        if(entityEnemy.getStartDirection().equals("right")) {
            this.movingRight = true;
        } else if (entityEnemy.getStartDirection().equals("left")) {
            this.movingLeft = true;
        }
    }
}
