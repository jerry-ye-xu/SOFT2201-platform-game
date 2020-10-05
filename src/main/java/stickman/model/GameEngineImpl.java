package stickman.model;

import stickman.view.EntityView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.lang.UnsupportedOperationException;

import java.util.List;
import java.util.ArrayList;

public class GameEngineImpl implements GameEngine {
    private JSONObject jsonDict;
    private String levelName;
    private Level gameLevel;

    public GameEngineImpl(String jsonConfigPath, String levelName) {
        // @TODO: Read JSON config
        this.jsonDict = parseJsonConfig(jsonConfigPath);
        this.gameLevel = this.buildLevel(levelName);
    }

    @Override
    public Level getCurrentLevel() {
        return null;
    }

    @Override
    public void startLevel() {

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

    @Override
    public void tick() {

    }

    /*
        Helper Functions
     */

    private Level buildLevel(String levelName) {
        JSONObject levelDict = (JSONObject) this.jsonDict.get(levelName);

        JSONArray platformArrayJSON = parseJsonArray(levelDict, "platformList");
        JSONArray mushroomArrayJSON = parseJsonArray(levelDict, "mushroomList");
        JSONArray enemyArrayJSON = parseJsonArray(levelDict, "enemyList");

        List<Platform> platformObjectList = this.buildPlatforms(platformArrayJSON);

        return null;
    }

    private List<Platform> buildPlatforms(JSONArray platformArrayJSON) {
        List<Platform> platformList = new ArrayList<>();
        PlatformFactory platformFactory = new PlatformFactoryStationary();

        for (Object platform: platformArrayJSON) {
            JSONObject platformJSON = (JSONObject) platform;
            if (platformJSON.get("type").equals("stationary")) {
                platformList.add(platformFactory.createPlatform(platformJSON));
            } else {
                throw new UnsupportedOperationException("Only platform of type 'stationary' is supported right now.");
            }
        }

        return platformList;
    }

    private JSONArray parseJsonArray(JSONObject jsonDict, String jsonList) {
        JSONArray itemList = (JSONArray) jsonDict.get(jsonList);
        return itemList;
    }

    private JSONObject parseJsonConfig(String configPath) {
        JSONObject jsonDict = null;

        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(configPath)) {
            Object obj = parser.parse(reader);
            jsonDict = (JSONObject) obj;

            return jsonDict;

        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return jsonDict;
    }
}
