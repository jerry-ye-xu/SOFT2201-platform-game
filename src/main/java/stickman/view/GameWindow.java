package stickman.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import stickman.model.Entity;
import stickman.model.GameEngine;
import stickman.model.Stickman;

import java.util.ArrayList;
import java.util.List;

public class GameWindow {
    private final int width;
    private Scene scene;
    private Pane pane;
    private GameEngine model;
    private ImageView previousStickmanFrame;
    private List<EntityView> entityViews;
    private BackgroundDrawer backgroundDrawer;

    private double xViewportOffset = 0.0;
    private static final double VIEWPORT_MARGIN = 280.0;

    public GameWindow(GameEngine model, int width, int height) {
        this.model = model;
        this.pane = new Pane();
        this.width = width;
        this.scene = new Scene(this.pane, width, height);

        this.entityViews = new ArrayList<>();

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(model);

        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        this.backgroundDrawer = new BlockedBackground();

        this.backgroundDrawer.draw(model, this.pane);
        this.addStationaryEntities(model, this.pane);
//        this.addCharacter(model, this.pane);
    }

//    private void addCharacter(GameEngine model, Pane pane) {
//        Stickman stickman = model.getStickman();
//        this.previousStickmanFrame = stickman.updateFrame();
//        pane.getChildren().add(this.previousStickmanFrame);
//    }

    public Scene getScene() {
        return this.scene;
    }

    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void draw() {
        this.model.tick();
        this.refreshStickmanFrame(this.model);

        List<Entity> entities = this.model.getCurrentLevel().getEntities();

        for (EntityView entityView: entityViews) {
            entityView.markForDelete();
        }

        double heroXPos = model.getStickman().getXPosition();
        heroXPos -= xViewportOffset;

        if (heroXPos < VIEWPORT_MARGIN) {
            if (xViewportOffset >= 0) { // Don't go further left than the start of the level
                xViewportOffset -= VIEWPORT_MARGIN - heroXPos;
                if (xViewportOffset < 0) {
                    xViewportOffset = 0;
                }
            }
        } else if (heroXPos > width - VIEWPORT_MARGIN) {
            xViewportOffset += heroXPos - (width - VIEWPORT_MARGIN);
        }

        backgroundDrawer.update(xViewportOffset);

        for (Entity entity: entities) {
            boolean notFound = true;
            for (EntityView view: entityViews) {
                if (view.matchesEntity(entity)) {
//                    System.out.println("GameWindow view.matchesEntity(entity)");
//                    System.out.println(entity);
                    notFound = false;
                    view.update(xViewportOffset);
                    break;
                }
            }
            if (notFound) {
//                System.out.println("GameWindow view.matchesEntity(entity): notFound = false");
//                System.out.println(entity);
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView: entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }
        entityViews.removeIf(EntityView::isMarkedForDelete);
    }

    private void refreshStickmanFrame(GameEngine model) {
        Stickman stickman = model.getStickman();
        this.pane.getChildren().remove(this.previousStickmanFrame);
        this.previousStickmanFrame = stickman.updateFrame();
        this.pane.getChildren().add(this.previousStickmanFrame);
    }

    private void addStationaryEntities(GameEngine model, Pane pane) {
        List<Platform> platformList = model.getCurrentLevel().getPlatforms();

        for (Platform platform: platformList) {
            pane.getChildren().add(platform.getHBox());
        }
    }
}
