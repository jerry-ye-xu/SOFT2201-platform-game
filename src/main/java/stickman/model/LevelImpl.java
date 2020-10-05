package stickman.model;

import java.util.List;

public class LevelImpl implements Level{
    public List<Entity> arrayEntities;
    protected List<Platform> arrayPlatforms;

    @Override
    public List<Entity> getEntities() {
        return null;
    }

    @Override
    public List<Platform> getPlatforms() {
        return null;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public void tick() {

    }

    @Override
    public double getFloorHeight() {
        return 0;
    }

    @Override
    public double getHeroX() {
        return 0;
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
