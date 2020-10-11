package stickman.model;

import java.util.List;

public class LevelImpl implements Level{
    protected List<Entity> arrayEntities;
    protected List<EntityImplFireball> arrayFireballs;
//    protected List<Entity> arrayEntities;

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
    public boolean jump() {
        return false;
    }

    @Override
    public boolean moveLeft() {
        return false;
    }

    @Override
    public boolean moveRight() {
        return false;
    }

    @Override
    public boolean stopMoving() {
        return false;
    }
}
