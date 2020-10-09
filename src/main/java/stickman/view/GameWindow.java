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
    private static final double VIEWPORT_MARGIN = 320.0;

    public GameWindow(GameEngine model, int width, int height) {
        this.model = model;
        this.pane = new Pane();
        this.width = width;
        this.scene = new Scene(
            this.pane,
            width,
            height
        );

        this.entityViews = new ArrayList<>();

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(model);

        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        this.backgroundDrawer = new BlockedBackground();
        this.backgroundDrawer.draw(model, this.pane);
        this.pane.getChildren().add(this.model.getEntityViewStickman().getNode());
    }

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

        List<Entity> entities = this.model.getCurrentLevel().getEntities();

        for (EntityView entityView: entityViews) {
            entityView.markForDelete();
        }

        this.refreshStickmanFrame(this.model, xViewportOffset);
        double heroXPos = model.getEntityViewStickman().getXPosition();
//        System.out.println("BEFORE -= xViewportOffset");
//        System.out.println("xViewportOffset: " + xViewportOffset);
//        System.out.println("heroXPos: " + heroXPos);
        heroXPos -= xViewportOffset;

        if (heroXPos < VIEWPORT_MARGIN) {
            if (xViewportOffset >= 0) { // Don't go further left than the start of the level
                xViewportOffset -= VIEWPORT_MARGIN - heroXPos;
                if (xViewportOffset < 0) {
                    xViewportOffset = 0;
                }
            }
        } else if (heroXPos > width - VIEWPORT_MARGIN) {
//            System.out.println("MOVING THE FRAME...");
//            System.out.println("heroXPos: " + heroXPos);
//            System.out.println("xViewportOffset: " + xViewportOffset);
            xViewportOffset += heroXPos - (width - VIEWPORT_MARGIN);
//            System.out.println("xViewportOffset: " + xViewportOffset);

            double stickmanWidth = this.model.getEntityViewStickman().getWidth();

            if (xViewportOffset >= this.model.getCurrentLevel().getWidth() - width + stickmanWidth) {
                xViewportOffset = this.model.getCurrentLevel().getWidth() - width + stickmanWidth;
            }
        }

//        System.out.println("AFTER updating xViewportOffset");
//        System.out.println("heroXPos: " + heroXPos);
//        System.out.println("VIEWPORT_MARGIN: " + VIEWPORT_MARGIN);
//        System.out.println("width: " + width);
//        System.out.println("xViewportOffset: " + xViewportOffset);

        backgroundDrawer.update(xViewportOffset);

        for (Entity entity: entities) {
            boolean notFound = true;
            for (EntityView view: entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update(xViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView;
                if (entity.getType().equals("blob")) {
                    entityView = new EntityViewBlob(entity);
                } else {
                    entityView = new EntityViewImpl(entity);
                }
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

    private void refreshStickmanFrame(GameEngine model, double xViewportOffset) {
        EntityViewStickman stickman = model.getEntityViewStickman();
        stickman.update(xViewportOffset);
//        EntityViewStickman stickman = model.getEntityViewStickman();
//        this.pane.getChildren().remove(this.previousStickmanFrame);
//        this.previousStickmanFrame = stickman.update(xViewportOffset);
//        this.pane.getChildren().add(this.previousStickmanFrame);
    }

//    private void addStationaryEntities(GameEngine model, Pane pane) {
//        List<Platform> platformList = model.getCurrentLevel().getPlatforms();
//
//        for (Platform platform: platformList) {
//            pane.getChildren().add(platform.getHBox());
//        }
//    }
}
