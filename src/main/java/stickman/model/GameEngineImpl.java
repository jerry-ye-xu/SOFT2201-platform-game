package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import stickman.view.EntityView;
import stickman.view.EntityViewBlob;
import stickman.view.EntityViewStickman;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

public class GameEngineImpl implements GameEngine {
    private JSONObject jsonDict;
    private String levelName;
    private Level gameLevel;
    private EntityViewStickman entityViewStickman;
    private List<EntityViewBlob> entityViewBlobList;

    private List<Entity> platformList;
    private List<Entity> powerUpList;
    private List<Entity> enemyList;

    public GameEngineImpl(String jsonConfigPath, String levelName) {
        // @TODO: Read JSON config
        this.levelName = levelName;
        this.jsonDict = parseJsonConfig(jsonConfigPath);
        this.entityViewStickman = this.buildStickman(levelName);
        this.gameLevel = this.buildLevel(levelName);

        entityViewBlobList = new ArrayList<>();
        for (Entity enemy: enemyList) {
            entityViewBlobList.add(new EntityViewBlob(enemy));
        }
    }

    @Override
    public Level getCurrentLevel() {
        return this.gameLevel;
    }

    @Override
    public void startLevel() { }

    @Override
    public EntityViewStickman getEntityViewStickman() {
        return this.entityViewStickman;
    }

    @Override
    public boolean jump() {
        if (this.entityViewStickman.getCanJump()) {
            System.out.println("this.entityViewStickman.jump()");
            this.entityViewStickman.jump();
            System.out.println(this.entityViewStickman.getYPosition());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean moveLeft() {
        this.entityViewStickman.setMovement(true, false);
        return true;
    }

    @Override
    public boolean moveRight() {
        this.entityViewStickman.setMovement(false, true);
        return true;
    }

    @Override
    public boolean stopMoving() {
        this.entityViewStickman.setMovement(false, false);
        return true;
    }

    @Override
    public void tick() {
//        System.out.println("this.entityViewStickman");
        this.entityViewStickman.updateXPos(this.gameLevel);
        this.entityViewStickman.updateYPos(this.gameLevel);

        for (EntityViewBlob blob: entityViewBlobList) {
            blob.updateXPos();
            blob.updateYPos();
        }

//        System.out.println(this.entityViewStickman.getXPosition());
//        System.out.println(this.entityViewStickman.getYPosition());
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

//        System.out.println("enemyArrayJSON: " + enemyArrayJSON);

        this.platformList = this.buildPlatforms(platformArrayJSON);
        this.powerUpList = this.buildPowerUpEntities(powerUpArrayJSON);
        this.enemyList = this.buildEnemyEntities(enemyArrayJSON);

        Entity flag = this.buildFlagEntity((JSONObject) levelDict.get("flagPosition"));

        List<Entity> entityList = new ArrayList<>();
        entityList.addAll(platformList);
        entityList.addAll(powerUpList);
//        entityList.addAll(enemyList);
        entityList.add(flag);

        System.out.println("entityList.size()");
        System.out.println(entityList.size());

        double height = ((Long) levelDict.get("height")).doubleValue();
        double width = ((Long) levelDict.get("width")).doubleValue();
        double floorHeight = ((Long) levelDict.get("floorHeight")).doubleValue();

        Level gameLevel = new LevelImpl(
            entityList,
            height,
            width,
            floorHeight,
            stickmanStartingXPos
        );

        return gameLevel;
    }

    private List<Entity> buildPlatforms(JSONArray arrayJSON) {
        List<Entity> objList = new ArrayList<>();
        EntityFactoryPlatform platformFactory = new EntityFactoryPlatformImpl();
        List<Entity> tmpList;

        for (Object obj: arrayJSON) {
            JSONObject objJSON = (JSONObject) obj;
            String objType = (String) objJSON.get("type");
            tmpList = platformFactory.createPlatform(objType, objJSON);
            objList.addAll(tmpList);
        }

        return objList;
    }

    private List<Entity> buildPowerUpEntities(JSONArray arrayJSON) {
        List<Entity> objList = new ArrayList<>();
        EntityFactory entityFactory = new EntityFactoryPowerUp();

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
        final Layer layer = Layer.ENTITY_LAYER;

        Entity flag = new EntityImplFlag(
            "flag",
            width,
            height,
            XPos,
            YPos - height,
            imagePath,
            layer
        );

        return flag;
    }

    private List<Entity> buildEnemyEntities(JSONArray arrayJSON) {
        List<Entity> objList = new ArrayList<>();
        EntityFactory entityFactory = new EntityFactoryEnemy();
        for (Object obj: arrayJSON) {
            JSONObject objJSON = (JSONObject) obj;
            System.out.println("objJSON: " + objJSON);
            String objType = (String) objJSON.get("type");
            System.out.println("objType: " + objType);
            objList.add(entityFactory.createEntity(objType, objJSON));
        }

        return objList;
    }

    private EntityViewStickman buildStickman(String levelName) {
        JSONObject levelDict = (JSONObject) this.jsonDict.get(levelName);

        String size = (String) this.jsonDict.get("stickmanSize");
        int width;
        int height;
        if (size.equals("normal")) {
            width = 30;
            height = 45;
        } else if (size.equals("large")) {
            width = 45;
            height = 60;
        } else {
            throw new UnsupportedOperationException("Only enemy entity of type 'blob' is supported right now.");
        }

        double startingXPos = (Double) this.jsonDict.get("stickmanStartingPos");
        double startingYPos = ((Long) levelDict.get("floorHeight")).doubleValue();

        String imagePath = "/ch_stand1.png";
        final Layer layer = Layer.FOREGROUND;

        System.out.println("startingYPos: " + startingYPos);
        System.out.println("height: " + height);

        Entity stickmanEntity = new EntityImplStickman(
            "stickman",
            width,
            height,
            startingXPos,
            startingYPos - height,
            imagePath,
            layer
        );

        EntityViewStickman stickman = new EntityViewStickman(stickmanEntity);

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

    /*
        Getters and setters
     */

    public List<EntityViewBlob> getEntityViewBlobList() { return this.entityViewBlobList; }
}
