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
import stickman.model.Platform;
import stickman.model.Stickman;

import javax.swing.text.AsyncBoxView;
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

        double heroXPos = model.getStickman().getXPosition();
        heroXPos -= xViewportOffset;
        this.refreshStickmanFrame(this.model, xViewportOffset);

//        if (heroXPos < VIEWPORT_MARGIN) {
//            if (xViewportOffset >= 0) { // Don't go further left than the start of the level
//                xViewportOffset += heroXPos - VIEWPORT_MARGIN;
//                if (xViewportOffset < 0) {
//                    xViewportOffset = 0;
//                }
//            }
//        } else if (heroXPos > (this.model.getCurrentLevel().getWidth() - VIEWPORT_MARGIN)) {
//            System.out.println("AT THE END OF THE LEVEL");
//            System.out.println("this.model.getCurrentLevel().getWidth(): " + this.model.getCurrentLevel().getWidth());
//            xViewportOffset += heroXPos - VIEWPORT_MARGIN;
//            if (xViewportOffset > this.model.getCurrentLevel().getWidth() - width) {
//                xViewportOffset = this.model.getCurrentLevel().getWidth() - width;
//            }
//        } else if (heroXPos > (width - VIEWPORT_MARGIN)) {
//            System.out.println("heroXPos > width - VIEWPORT_MARGIN: " + (heroXPos > width - VIEWPORT_MARGIN));
//            System.out.println("heroXPos: " + heroXPos);
//            System.out.println("xViewportOffset: " + xViewportOffset);
//            xViewportOffset = heroXPos - VIEWPORT_MARGIN;
//        }

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

//        else if (heroXPos > (this.model.getCurrentLevel().getWidth() - VIEWPORT_MARGIN)) {
//            System.out.println("heroXPos > (this.model.getCurrentLevel().getWidth() - VIEWPORT_MARGIN): " + (heroXPos > (this.model.getCurrentLevel().getWidth() - VIEWPORT_MARGIN)));
//            if (xViewportOffset <= this.model.getCurrentLevel().getWidth()) {
//                System.out.println("xViewportOffset: " + xViewportOffset);
//                xViewportOffset += heroXPos + VIEWPORT_MARGIN;
//                if (xViewportOffset > this.model.getCurrentLevel().getWidth()) {
//                    System.out.println("xViewportOffset: " + xViewportOffset);
//                    xViewportOffset += this.model.getCurrentLevel().getWidth() - VIEWPORT_MARGIN;
//                }
//            }
//        }


        System.out.println("heroXPos: " + heroXPos);
        System.out.println("VIEWPORT_MARGIN: " + VIEWPORT_MARGIN);
        System.out.println("width: " + width);
        System.out.println("xViewportOffset: " + xViewportOffset);

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

    private void refreshStickmanFrame(GameEngine model, double xViewportOffset) {
        Stickman stickman = model.getStickman();
        this.pane.getChildren().remove(this.previousStickmanFrame);
        this.previousStickmanFrame = stickman.updateFrame(xViewportOffset);
        this.pane.getChildren().add(this.previousStickmanFrame);
    }

//    private void addStationaryEntities(GameEngine model, Pane pane) {
//        List<Platform> platformList = model.getCurrentLevel().getPlatforms();
//
//        for (Platform platform: platformList) {
//            pane.getChildren().add(platform.getHBox());
//        }
//    }
}
