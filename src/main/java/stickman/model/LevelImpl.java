package stickman.model;

import stickman.view.Platform;

import java.util.List;

public class LevelImpl implements Level{
    protected List<Entity> arrayEntities;
    protected List<Platform> arrayPlatforms;

    protected double height;
    protected double width;
    protected double floorHeight;
    protected double heroStartingXPosition;

    public LevelImpl(
        List<Platform> arrayPlatforms,
        List<Entity> arrayEntities,
        double height,
        double width,
        double floorHeight,
        double heroStartingXPosition
    ) {
        this.arrayPlatforms = arrayPlatforms;
        this.arrayEntities = arrayEntities;
        this.height = height;
        this.width = width;
        this.floorHeight = floorHeight;
        this.heroStartingXPosition = heroStartingXPosition;
    }

    @Override
    public List<Entity> getEntities() {
        return this.arrayEntities;
    }

    @Override
    public List<Platform> getPlatforms() {
        return this.arrayPlatforms;
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
        return this.heroStartingXPosition;
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
