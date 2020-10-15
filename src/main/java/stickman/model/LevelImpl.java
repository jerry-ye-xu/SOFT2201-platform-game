package stickman.model;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import stickman.view.*;

import java.util.ArrayList;
import java.util.List;

public class LevelImpl implements Level {
    protected EntityImplStickman stickmanEntity;
    protected EntityViewStickman stickmanView;

    protected List<Entity> arrayEntities;
    protected List<EntityViewImplMoving> movingViews = new ArrayList<>();

    protected List<Entity> arrayEnemies;
    protected List<EntityViewBlob> viewBlobs = new ArrayList<>();

//    protected List<EntityMoving> arrayFireballs;
//    protected List<EntityViewFireball> viewsFireballs = new ArrayList<>();

    protected double height;
    protected double width;
    protected double floorHeight;
    protected double heroXPosition;

    public LevelImpl(
        List<Entity> arrayEntities,
//        List<Entity> arrayEnemies,
//        EntityImplStickman stickmanEntity,
        double height,
        double width,
        double floorHeight,
        double heroXPosition
    ) {
        this.arrayEntities = arrayEntities;
        this.createMovingEntityViews();

        this.height = height;
        this.width = width;
        this.floorHeight = floorHeight;
        this.heroXPosition = heroXPosition;
    }

    private void createMovingEntityViews() {
        // We keep the moving entities separate from the stationary entities in GameWindow.
        for (Entity entity: this.arrayEntities) {
            if (entity.getType().equals("blob")) {
//                System.out.println("BLOB2: this.xPosition - " + entity.getXPos());
//                System.out.println("BLOB2: this.yPosition - " + entity.getYPos());
//                System.out.println("BLOB2: this.startingXPos - " + entity.startingXPos);
//                System.out.println("BLOB2: this.movementRange - " + entity.movementRange);
//                System.out.println("BLOB2: this.movingRight - " + entity.movingRight);
//                System.out.println("BLOB2: this.movingLeft - " + entity.movingLeft);
//                System.out.println("BLOB2: this.height - " + entity.getHeight());
//                System.out.println("BLOB2: this.width - " + entity.getWidth());
                this.movingViews.add(new EntityViewBlob(entity));

            } else if (entity.getType().equals("stickman")) {
                this.stickmanView = new EntityViewStickman(entity);
            }
        }

        this.arrayEntities.removeIf(entity -> (entity.getType().equals("blob")));
        this.arrayEntities.removeIf(entity -> (entity.getType().equals("stickman")));

    }

    public void addEntityViewsToPane(Pane pane) {
        for (EntityViewImplMoving movingView: this.movingViews) {
//            System.out.println("movingView: this.xPosition - " + movingView.getEntity().getType());
//            System.out.println("movingView: this.xPosition - " + movingView.getXPosition());
//            System.out.println("movingView: this.yPosition - " + movingView.getYPosition());
//            System.out.println("BLOB2: this.startingXPos - " + movingView.startingXPos);
//            System.out.println("BLOB2: this.movementRange - " + movingView.movementRange);
//            System.out.println("movingView: this.movingRight - " + movingView.getFrameCount());
//            System.out.println("movingView: this.movingLeft - " + movingView.getNode());
//            System.out.println("BLOB2: this.height - " + movingView.height);
//            System.out.println("BLOB2: this.width - " + movingView.width);
//            System.out.println("movingView: this.imagePath - " + movingView.getEntity().getImagePath());


//            System.out.println("movingView: movingView.getNode().getLayoutX() - " + movingView.getNode().getLayoutX());
//            System.out.println("movingView: movingView.getNode().getLayoutY() - " + movingView.getNode().getLayoutY());
            pane.getChildren().add(movingView.getNode());
        }

        pane.getChildren().add(this.stickmanView.getNode());
    }

    @Override
    public List<Entity> getEntities() {
        return this.arrayEntities;
    }

    @Override
    public List<EntityViewImplMoving> getEntityViewsMovingList() { return this.movingViews; }

//    @Override
//    public List<EntityViewBlob> getEntityViewBlobList() { return this.viewBlobs; }
//
//    @Override
//    public List<EntityViewFireball> getEntityViewFireballList() { return this.viewsFireballs; }
//
    public EntityViewStickman getEntityViewStickman() { return this.stickmanView; }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public double getWidth() { return this.width; }

    @Override
    public void tick() {
        for (EntityViewImplMoving movingView: this.movingViews) {
            movingView.updateXPos();
            movingView.updateYPos();
        }

        this.stickmanView.updateXPos(this);
        this.stickmanView.updateYPos(this);
    }

    @Override
    public double getFloorHeight() { return this.floorHeight; }

    @Override
    public double getHeroX() { return this.heroXPosition; }

    @Override
    public void jump() { }

    @Override
    public void moveLeft() { }

    @Override
    public void moveRight() { }

    @Override
    public void stopMoving() { }
}
