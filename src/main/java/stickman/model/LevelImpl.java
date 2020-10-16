package stickman.model;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import stickman.view.*;

import java.util.ArrayList;
import java.util.List;

public class LevelImpl implements Level {
    protected EntityViewStickman stickmanView;

    protected List<Entity> arrayEntities;
    protected List<EntityViewImplMoving> movingViews = new ArrayList<>();

    protected double height;
    protected double width;
    protected double floorHeight;
    protected double heroXPosition;

    public LevelImpl(
        List<Entity> arrayEntities,
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
            if (!pane.getChildren().contains(movingView.getNode())) {
                pane.getChildren().add(movingView.getNode());
            }
        }

        if (!pane.getChildren().contains(this.stickmanView.getNode())) {
            pane.getChildren().add(this.stickmanView.getNode());
        }
    }

    @Override
    public List<Entity> getEntities() {
        return this.arrayEntities;
    }

    @Override
    public List<EntityViewImplMoving> getEntityViewsMovingList() { return this.movingViews; }

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
