package stickman;

import stickman.model.Class1;
import stickman.model.Class2;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
import stickman.view.SceneGameResult;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Map<String, String> params = this.getParameters().getNamed();

//        String levelName = params.get("levelName");
//        String configPath = params.get("configPath");
//        Class1 c1 = new Class1();
//        System.out.println(c1.getX());
//
//        Class1 c2 = new Class2();
//        System.out.println(c2.getX());
        String configPath = "config.json";
        String levelName = "levelOne";
        int width = 640;
        int height = 400;

        GameEngine model = new GameEngineImpl(configPath, levelName);
//        System.out.println("bob");
        GameWindow window = new GameWindow(model, width, height);
//        System.out.println("bob");
        window.run();
//        System.out.println("bob");

        primaryStage.setTitle("EntityViewStickman");
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(window.getScene());
        primaryStage.show();

        window.run();
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
