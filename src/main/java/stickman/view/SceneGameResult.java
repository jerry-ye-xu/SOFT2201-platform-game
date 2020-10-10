package stickman.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class SceneGameResult {
    protected int width;
    protected int height;
    protected Scene scene;
    protected Pane pane;
    protected Rectangle screen;
    protected String sceneMessage;

    public SceneGameResult(int width, int height) {
        this.width = width;
        this.height = height;
        this.pane = new Pane();
        this.scene = new Scene(this.pane, this.width, this.height);
    }

    public void setSceneMessage(String sceneMessage) {
        this.sceneMessage = sceneMessage;
    }

    public String getSceneMessage() {
        return this.sceneMessage;
    }

    public void drawScene() {
        this.screen = new Rectangle(0, 0, width, height);
        this.screen.setFill(Paint.valueOf("BLACK"));
        this.screen.setViewOrder(10.0);

        Label sceneLabel = new Label(this.sceneMessage);
        sceneLabel.setLayoutX(width / 2);
        sceneLabel.setLayoutY(height / 2);
        sceneLabel.setViewOrder(0.0);

        this.pane.getChildren().addAll(this.screen, sceneLabel);
    }
}
