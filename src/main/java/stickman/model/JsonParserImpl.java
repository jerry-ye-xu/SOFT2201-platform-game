package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonParserImpl implements JsonParser {
    private JSONObject jsonDict;
    private String levelName;

    public JsonParserImpl(String jsonConfigPath, String levelName) {
        this.jsonDict = parseJsonConfig(jsonConfigPath);
        this.levelName = levelName;
    }

    @Override
    public Level buildLevel() {
        double stickmanStartingXPos = (double) this.jsonDict.get("stickmanStartingPos");
        JSONObject levelDict = (JSONObject) this.jsonDict.get(this.levelName);

        JSONArray platformArrayJSON = parseJsonArray(levelDict, "platformList");
        JSONArray powerUpArrayJSON = parseJsonArray(levelDict, "powerUpList");
        JSONArray enemyArrayJSON = parseJsonArray(levelDict, "enemyList");

//        System.out.println("enemyArrayJSON: " + enemyArrayJSON);

        List<Entity> platformList = this.buildPlatforms(platformArrayJSON);
        List<Entity> powerUpList = this.buildPowerUpEntities(powerUpArrayJSON);
        List<Entity> enemyList = this.buildEnemyEntities(enemyArrayJSON);

        Entity flag = this.buildFlagEntity((JSONObject) levelDict.get("flagPosition"));

        Entity stickman = this.buildStickman(this.levelName);

        List<Entity> entityList = new ArrayList<>();
        entityList.addAll(platformList);
        entityList.addAll(powerUpList);
        entityList.addAll(enemyList);
        entityList.add(flag);
        entityList.add(stickman);

//        System.out.println("entityList.size()");
//        System.out.println(entityListStationary.size());

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
        EntityListFactory platformFactory = new EntityListFactoryPlatform();
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
            String objType = (String) objJSON.get("type");
            objList.add(entityFactory.createEntity(objType, objJSON));
        }

        return objList;
    }

    private Entity buildStickman(String levelName) {
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

//        System.out.println("startingYPos: " + startingYPos);
//        System.out.println("height: " + height);

        Entity stickman = new EntityImplStickman(
                "stickman",
                width,
                height,
                startingXPos,
                startingYPos - height,
                imagePath,
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
