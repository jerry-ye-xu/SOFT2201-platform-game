package stickman.model;

import stickman.view.EntityViewImplMoving;
import stickman.view.EntityViewStickman;

import java.util.List;

public class GameEngineImpl implements GameEngine {
    private Level gameLevel;

    public GameEngineImpl(String jsonConfigPath, String levelName) {
        // @TODO: Read JSON config
        JsonParser jsonParser = new JsonParserImpl(jsonConfigPath, levelName);
        this.gameLevel = jsonParser.buildLevel();
    }

    @Override
    public Level getCurrentLevel() {
        return this.gameLevel;
    }

    @Override
    public void startLevel() { }

    @Override
    public boolean jump() {
        EntityViewStickman stickman = this.getCurrentLevel().getEntityViewStickman();
        if (stickman.getCanJump()) {
            stickman.setOnPlatform(false);
            stickman.jump();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean fire() {
        EntityViewStickman stickman = this.getCurrentLevel().getEntityViewStickman();
        stickman.fire(this.getCurrentLevel());
        return true;
    }

    @Override
    public boolean moveLeft() {
        EntityViewStickman stickman = this.getCurrentLevel().getEntityViewStickman();
        stickman.setMovement(true, false);
        return true;
    }

    @Override
    public boolean moveRight() {
        EntityViewStickman stickman = this.getCurrentLevel().getEntityViewStickman();
        stickman.setMovement(false, true);
        return true;
    }

    @Override
    public boolean stopMoving() {
        EntityViewStickman stickman = this.getCurrentLevel().getEntityViewStickman();
        stickman.setMovement(false, false);
        return true;
    }

    @Override
    public void tick() {
        this.gameLevel.tick();
    }

    /*
        Getters and setters
     */
}
