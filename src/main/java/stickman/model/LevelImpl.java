package stickman.model;

import javafx.scene.image.ImageView;
import stickman.view.EntityView;
import stickman.view.EntityViewBlob;
import stickman.view.EntityViewFireball;
import stickman.view.EntityViewStickman;

import java.util.ArrayList;
import java.util.List;

public class LevelImpl implements Level {
    protected EntityImplStickman stickmanEntity;
    protected EntityViewStickman stickmanView;

    protected List<Entity> arrayEntities;

    protected List<Entity> arrayEnemies;
    protected List<EntityViewBlob> viewBlobs = new ArrayList<>();

    protected List<EntityMoving> arrayFireballs;
    protected List<EntityViewFireball> viewsFireballs = new ArrayList<>();

    protected double height;
    protected double width;
    protected double floorHeight;
    protected double heroXPosition;

    public LevelImpl(
        List<Entity> arrayEntities,
        List<Entity> arrayEnemies,
        EntityImplStickman stickmanEntity,
        double height,
        double width,
        double floorHeight,
        double heroXPosition
    ) {
        this.arrayEntities = arrayEntities;
        this.arrayEnemies = arrayEnemies;
        this.stickmanEntity = stickmanEntity;

        for (Entity enemy: arrayEnemies) {
            viewBlobs.add(new EntityViewBlob(enemy));
        }
        stickmanView = new EntityViewStickman(stickmanEntity);

        this.height = height;
        this.width = width;
        this.floorHeight = floorHeight;
        this.heroXPosition = heroXPosition;
    }

    @Override
    public List<Entity> getEntities() {
        return this.arrayEntities;
    }

    @Override
    public List<EntityViewBlob> getEntityViewBlobList() { return this.viewBlobs; }

    @Override
    public List<EntityViewFireball> getEntityViewFireballList() {
        return this.viewsFireballs;
    }

    @Override
    public EntityViewStickman getEntityViewStickman() { return this.stickmanView; }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public double getWidth() { return this.width; }

    @Override
    public void tick() {
    }

    @Override
    public double getFloorHeight() {
        return this.floorHeight;
    }

    @Override
    public double getHeroX() {
        return this.heroXPosition;
    }

    @Override
    public void jump() { }

    @Override
    public void moveLeft() { }

    @Override
    public void moveRight() { }

    @Override
    public void stopMoving() { }
}
