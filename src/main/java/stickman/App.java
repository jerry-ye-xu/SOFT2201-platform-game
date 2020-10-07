package stickman;

import javafx.application.Application;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import stickman.model.GameEngine;
import stickman.model.GameEngineImpl;
import stickman.view.GameWindow;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONObject;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
//        Map<String, String> params = this.getParameters().getNamed();
//
//        String levelName = params.get("levelName");
//        String configPath = params.get("configPath");

        String configPath = "config.json";
        String levelName = "levelOne";

        JSONObject jsonObj = this.parseJsonConfig("config.json");
        System.out.println(jsonObj);

        System.out.println(jsonObj.get("stickmanStartingPos"));
        System.out.println(jsonObj.get("stickmanSize"));
        System.out.println(jsonObj.get("cloudVelocity"));
        System.out.println(jsonObj.get("levelOne"));

        JSONObject levelDict = (JSONObject) jsonObj.get(levelName);

        System.out.println("levelDict");
        System.out.println(levelDict);

        JSONArray platformArrayJSON = parseJsonArray(levelDict, "powerUpList");

        System.out.println("platformArrayJSON");
        System.out.println(platformArrayJSON);

        JSONArray arrayJSON = platformArrayJSON;

        for (Object obj: arrayJSON) {
            JSONObject objJSON = (JSONObject) obj;
            String objType = (String) objJSON.get("type");
            System.out.println("objType");
            System.out.println(objType);
            System.out.println("XPos");
            System.out.println(objJSON.get("XPos"));
        }

//        System.out.println(platformArrayJSON.get("platform"));

//        GameEngine model = new GameEngineImpl(configPath, levelName);
//        System.out.println("bob");
//        GameWindow window = new GameWindow(model, 640, 400);
//        System.out.println("bob");
//        window.run();
//        System.out.println("bob");

//        primaryStage.setTitle("Stickman");
//        primaryStage.setScene(window.getScene());
//        primaryStage.show();
//
//        window.run();
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

        System.out.println("jsonDict is...");
        System.out.println(jsonDict);
        return jsonDict;
    }
}
