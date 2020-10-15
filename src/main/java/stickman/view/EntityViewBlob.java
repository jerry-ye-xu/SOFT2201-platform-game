package stickman.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import stickman.model.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EntityViewBlob extends EntityViewImplMoving {
    protected static final double DEFAULT_SPEED = 0.75;

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

    private Entity entity;
    private String imagePath;
    private boolean delete = false;
    private ImageView node;

    private List<String> standingFrames;
    private int numStandingFrames;
    private char[] tagList = {'a', 'b'};
    private double movementRange;
    private final double startingXPos;

    private final int frameCountRate = 90;
    private int frameCount = 0;
    private int frameIdx = 0;

    public EntityViewBlob(Entity entity) {
        super(entity);
        this.entity = entity;
        this.imagePath = entity.getImagePath();

        this.layer = this.entity.getLayer();

        this.width = this.entity.getWidth();
        this.height = this.entity.getHeight();

        this.xSpeed = DEFAULT_SPEED;
//        this.ySpeed = 0;
//
        this.startingXPos = this.entity.getXPos();
        this.xPosition = this.entity.getXPos();
        this.yPosition = this.entity.getYPos();

        EntityMoving entityEnemy = (EntityMoving) this.entity;
        this.setMovementRange(entityEnemy);
        this.setInitialDirection(entityEnemy);

        this.numStandingFrames = this.tagList.length;
        String pathStart = ((EntityImplBlob) this.entity).getImagePathStart();
        String pathEnd = ((EntityImplBlob) this.entity).getImagePathEnd();
        this.loadEntityFrames(pathStart, pathEnd, this.tagList);

        this.imagePath = this.entity.getImagePath();
        URL imageURL = this.getClass().getResource(this.imagePath);
        this.node = new ImageView(imageURL.toExternalForm());
        this.node.setViewOrder(getViewOrder(this.entity.getLayer()));
    }

    @Override
    public void update(double xViewportOffset) {
        String imagePath = this.chooseFrame();

        if (!this.imagePath.equals(imagePath)) {
            this.imagePath = imagePath;
            this.node.setImage(new Image(this.imagePath));
        }



        this.updateYPos();
        this.updateXPos();

        this.node.setX(this.xPosition - xViewportOffset);
        this.node.setY(this.yPosition);
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

    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public boolean isMarkedForDelete() {
        return this.delete;
    }

    /*
        Movement
     */

    protected void setMovement(boolean movingLeft, boolean movingRight) {
        this.movingLeft = movingLeft;
        this.movingRight = movingRight;
    }

    public void updateXPos() {
        if (this.xPosition < this.startingXPos - this.movementRange) {
            this.xPosition = this.startingXPos - this.movementRange;
            this.setMovement(false, true);

        } else if (this.xPosition > this.startingXPos + this.movementRange) {
            this.xPosition = this.startingXPos + this.movementRange;
            this.setMovement(true, false);
        }

        if (this.movingRight) {
            this.xPosition += this.xSpeed * DEFAULT_SPEED;
        } else if (this.movingLeft) {
            this.xPosition -= this.xSpeed * DEFAULT_SPEED;
        }
    }

    public void updateYPos() { }

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

    private void setMovementRange(EntityMoving entityEnemy) {
        this.movementRange = entityEnemy.getMovementRange();
    }

    private void setInitialDirection(EntityMoving entityEnemy) {
        if(entityEnemy.getStartDirection().equals("right")) {
            this.movingRight = true;
        } else if (entityEnemy.getStartDirection().equals("left")) {
            this.movingLeft = true;
        }
    }
}
