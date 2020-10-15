package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import stickman.view.EntityViewBlob;
import stickman.view.EntityViewFireball;
import stickman.view.EntityViewStickman;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

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

        EntityViewStickman stickman = this.getCurrentLevel().getEntityViewStickman();
        stickman.updateXPos(this.gameLevel);
        stickman.updateYPos(this.gameLevel);

        Level level = this.getCurrentLevel();
        for (EntityViewBlob blob: level.getEntityViewBlobList()) {
            blob.updateXPos();
            blob.updateYPos();
        }

        for (EntityViewFireball fireball: level.getEntityViewFireballList()) {
            fireball.updateXPos();
            fireball.updateYPos();
        }
    }

    /*
        Getters and setters
     */
}
