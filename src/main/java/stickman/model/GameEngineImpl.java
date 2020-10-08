package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import stickman.view.Platform;
import stickman.view.PlatformFactory;
import stickman.view.PlatformFactoryImpl;

public class GameEngineImpl implements GameEngine {
    private JSONObject jsonDict;
    private String levelName;
    private Level gameLevel;
    private Stickman stickman;

    public GameEngineImpl(String jsonConfigPath, String levelName) {
        // @TODO: Read JSON config
        this.levelName = levelName;
        this.jsonDict = parseJsonConfig(jsonConfigPath);
        this.stickman = this.buildStickman(levelName);
        this.gameLevel = this.buildLevel(levelName);
    }

    @Override
    public Level getCurrentLevel() {
        return this.gameLevel;
    }

    @Override
    public void startLevel() {

    }

    @Override
    public Stickman getStickman() {
        return this.stickman;
    }

    @Override
    public boolean jump() {
        if (this.stickman.canJump) {
            System.out.println("this.stickman.jump()");
            this.stickman.jump();
            System.out.println(this.stickman.getYPosition());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean moveLeft() {
        this.stickman.setMovement(true, false);
        return true;
    }

    @Override
    public boolean moveRight() {
        this.stickman.setMovement(false, true);
        return true;
    }

    @Override
    public boolean stopMoving() {
        this.stickman.setMovement(false, false);
        return true;
    }

    @Override
    public void tick() {
//        System.out.println("this.stickman");
        this.stickman.updateXPos(this.gameLevel);
        this.stickman.updateYPos(this.gameLevel);
//        System.out.println(this.stickman.getXPosition());
//        System.out.println(this.stickman.getYPosition());
//        System.out.println("this.updateXPos(this.gameLevel)");
//        System.out.println("this.updateYPos(this.gameLevel)");

//        this.getCurrentLevel().setHeroX();

        // Update non-stationary objects here.
    }

    /*
        Helper Functions
     */

    private Level buildLevel(String levelName) {
        double stickmanStartingXPos = (double) this.jsonDict.get("stickmanStartingPos");
        JSONObject levelDict = (JSONObject) this.jsonDict.get(levelName);

        JSONArray platformArrayJSON = parseJsonArray(levelDict, "platformList");
        JSONArray powerUpArrayJSON = parseJsonArray(levelDict, "powerUpList");
        JSONArray enemyArrayJSON = parseJsonArray(levelDict, "enemyList");

        List<Platform> platformList = this.buildPlatforms(platformArrayJSON);
        List<Entity> powerUpList = this.buildPowerUpEntities(powerUpArrayJSON);
//        List<Entity> enemyList = this.buildEnemyEntities(enemyArrayJSON);

        Entity flag = this.buildFlagEntity((JSONObject) levelDict.get("flagPosition"));

        List<Entity> entityList = new ArrayList<>();
        entityList.addAll(powerUpList);
        entityList.add(flag);

        System.out.println("entityList.size()");
        System.out.println(entityList.size());

        double height = ((Long) levelDict.get("height")).doubleValue();
        double width = ((Long) levelDict.get("width")).doubleValue();
        double floorHeight = ((Long) levelDict.get("floorHeight")).doubleValue();

        Level gameLevel = new LevelImpl(
            platformList,
            entityList,
            height,
            width,
            floorHeight,
            stickmanStartingXPos
        );

        return gameLevel;
    }

    private List<Platform> buildPlatforms(JSONArray arrayJSON) {
        List<Platform> objList = new ArrayList<>();
        PlatformFactory platformFactory = new PlatformFactoryImpl();

        for (Object obj: arrayJSON) {
            JSONObject objJSON = (JSONObject) obj;
            String objType = (String) objJSON.get("type");
            objList.add(platformFactory.createPlatform(objType, objJSON));
        }

        return objList;
    }

    private List<Entity> buildPowerUpEntities(JSONArray arrayJSON) {
        List<Entity> objList = new ArrayList<>();
        EntityFactory entityFactory = new EntityPowerUpFactory();

        for (Object obj: arrayJSON) {
            JSONObject objJSON = (JSONObject) obj;
            String objType = (String) objJSON.get("type");
            objList.add(entityFactory.createEntity(objType, objJSON));
        }

        return objList;
    }

    private Entity buildFlagEntity(JSONObject objJSON) {
        // No need to loop through an array since there can only be 1 flag per level.
        final double width = ((Long) objJSON.get("width")).doubleValue();
        final double height = ((Long) objJSON.get("height")).doubleValue();
        final double XPos = ((Long) objJSON.get("XPos")).doubleValue();
        final double YPos = ((Long) objJSON.get("YPos")).doubleValue();
        final String imagePath = (String) objJSON.get("imageName");
        final Layer layer = Layer.FOREGROUND;

        Entity flag = new EntityFlagImpl(
            width,
            height,
            XPos,
            YPos,
            imagePath,
            layer
        );

        return flag;
    }

//    private List<Entity> buildEnemyEntities(JSONArray arrayJSON) {
//        List<Entity> objList = new ArrayList<>();
//        EntityFactory entityFactory = new EnemyEntityFactory();
//
//        for (Object obj: arrayJSON) {
//            JSONObject objJSON = (JSONObject) obj;
//            String objType = (String) objJSON.get("type");
//            objList.add(entityFactory.createEntity(objType, objJSON));
//        }
//
//        return objList;
//    }

    private Stickman buildStickman(String levelName) {
        JSONObject levelDict = (JSONObject) this.jsonDict.get(levelName);

        String size = (String) this.jsonDict.get("stickmanSize");
        double startingXPos = (Double) this.jsonDict.get("stickmanStartingPos");
        double startingYPos = ((Long) levelDict.get("floorHeight")).doubleValue();

        final Layer layer = Layer.FOREGROUND;

        Stickman stickman = new Stickman(
            size,
            startingXPos,
            startingYPos,
            layer
        );

        return stickman;
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
