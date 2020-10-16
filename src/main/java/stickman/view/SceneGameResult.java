package stickman.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class SceneGameResult {
    protected int width;
    protected int height;
    protected Scene scene;
    protected Pane pane;
    protected Rectangle screen;
    protected Label screenLabel;
    protected String screenMessage;

    public SceneGameResult(int width, int height, String screenMessage) {
        this.width = width;
        this.height = height;
        this.pane = new Pane();

        this.screen = new Rectangle(0, 0, width, height);
        this.screen.setFill(Paint.valueOf("BLACK"));
        this.screen.setViewOrder(10.0);

        this.screenMessage = screenMessage;
        this.screenLabel = new Label();
        this.screenLabel.setText(this.screenMessage);
        this.screenLabel.setFont(Font.font("Arial", 32));
        this.screenLabel.setLayoutX(width / 2 - 90);
//        System.out.println("screenLabel.getLayoutX(): " + screenLabel.getLayoutX());
        this.screenLabel.setLayoutY(height / 2);
        this.screenLabel.setTextAlignment(TextAlignment.CENTER);
        this.screenLabel.setTextFill(Color.ALICEBLUE);

        this.scene = new Scene(this.pane, this.width, this.height);
    }

    public Scene getScene() { return this.scene; }

    public Rectangle getScreen() { return this.screen; }

    public Label getScreenLabel() { return this.screenLabel; }

    public void setSceneMessage(String screenMessage) {
        this.screenMessage = screenMessage;
    }

    public String getSceneMessage() {
        return this.screenMessage;
    }
}
