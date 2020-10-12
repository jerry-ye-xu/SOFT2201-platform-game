package stickman.view;

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

public class SceneGameScore {
    protected int numLives;
    protected int score;
    protected Scene scene;
    protected Pane pane;
    protected Rectangle screen;
    protected Label screenLabel;
    protected String screenMessage;

    public SceneGameScore(int numLives, int score) {
        this.numLives = numLives;
        this.score = score;
//        this.pane = new Pane();

        this.livesLabel = new Label();
        this.livesLabel.setText("Score: " + this.score);
        this.livesLabel.setFont(Font.font("Arial", 24));
        this.livesLabel.setLayoutX(0);
//        System.out.println("screenLabel.getLayoutX(): " + screenLabel.getLayoutX());
        this.livesLabel.setLayoutY(35);
        this.livesLabel.setTextAlignment(TextAlignment.CENTER);
        this.livesLabel.setTextFill(Color.BLACK);

        this.scoreLabel = new Label();
        this.scoreLabel.setText("Score: " + this.score);
        this.scoreLabel.setFont(Font.font("Arial", 24));
        this.scoreLabel.setLayoutX(0);
//        System.out.println("screenLabel.getLayoutX(): " + screenLabel.getLayoutX());
        this.scoreLabel.setLayoutY(70);
        this.scoreLabel.setTextAlignment(TextAlignment.CENTER);
        this.scoreLabel.setTextFill(Color.BLACK);

//        this.scene = new Scene(this.pane, this.width, this.height);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setNumLives(int numLives) {
        this.numLives = numLives;
    }



    public Rectangle getScreen() { return this.screen; }

    public Label getScreenLabel() { return this.screenLabel; }

    public void setSceneMessage(String screenMessage) {
        this.screenMessage = screenMessage;
    }

    public String getSceneMessage() {
        return this.screenMessage;
    }

}
