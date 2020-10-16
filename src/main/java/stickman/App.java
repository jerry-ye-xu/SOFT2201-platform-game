package stickman;

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

        String configPath = "config.json";
        String levelName = "levelOne";
        int width = 640;
        int height = 400;

        GameEngine model = new GameEngineImpl(configPath, levelName);
        GameWindow window = new GameWindow(model, width, height);

        primaryStage.setTitle("EntityViewStickman");
        primaryStage.setScene(window.getScene());
        primaryStage.show();

        window.run();
    }
}
