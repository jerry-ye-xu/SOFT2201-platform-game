package stickman.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import stickman.model.Entity;
import stickman.model.EntityImplBlob;
import stickman.model.EntityImplMushroom;
import stickman.model.GameEngine;

import java.util.ArrayList;
import java.util.List;

public class GameWindow {
    private final int width;
    private final int height;
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
        this.height = height;
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

        for (EntityViewBlob blob: this.model.getEntityViewBlobList()) {
            System.out.println("blob: " + blob);
            this.pane.getChildren().add(blob.getNode());
        }

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

        for (EntityViewBlob blob: this.model.getEntityViewBlobList()) {
            blob.update(xViewportOffset);
        }

        for (EntityViewBlob blob: this.model.getEntityViewBlobList()) {
            ImageView stickmanView = (ImageView) this.model.getEntityViewStickman().getNode();
            ImageView blobView = (ImageView) blob.getNode();
            if (stickmanView.intersects(blobView.getLayoutBounds())) {
                System.out.println("Intersects!");
                this.model.getEntityViewStickman().decreaseLive();
                this.model.getEntityViewStickman().setMushroomPowerUp(false);
                this.model.getEntityViewStickman().resetPosition();
            }
        }

        for (EntityView entityView: entityViews) {
            ImageView stickmanView = (ImageView) this.model.getEntityViewStickman().getNode();
            ImageView blobView = (ImageView) entityView.getNode();
//            System.out.println("blobView.getLayoutBounds(): " + blobView.getLayoutBounds());
//            System.out.println("stickmanView.getLayoutBounds(): " + stickmanView.getLayoutBounds());
            if (
                stickmanView.intersects(blobView.getLayoutBounds()) &&
                entityView.getEntity().getType().equals("flag")
            ) {
                System.out.println("Intersects an flag");
                this.model.getEntityViewStickman().setWinStatus(true);
            }
            if (
                stickmanView.intersects(blobView.getLayoutBounds()) &&
                entityView.getEntity().getType().equals("mushroom")
            ) {
                System.out.println("Intersects an mushroom");
                this.model.getCurrentLevel().getEntities().remove(entityView.getEntity());
                this.model.getEntityViewStickman().setMushroomPowerUp(true);
            }
        }

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
//                    ImageView blobView = (ImageView) view.getNode();
//                    System.out.println("view.getLayoutBounds(): " + blobView.getLayoutBounds());
//                    System.out.println(view.getEntity());
                    view.update(xViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView;
//                if (entity.getType().equals("blob")) {
//                    entityView = new EntityViewBlob(entity);
//                } else {
//                    entityView = new EntityViewImpl(entity);
//                }
                entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);

//                ImageView blobView = (ImageView) entityView.getNode();
//                System.out.println("blobView.getLayoutBounds(): " + blobView.getLayoutBounds());
//                System.out.println(entityView.getEntity());
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView: entityViews) {
//            System.out.println(entityView);
//            System.out.println(entityView.getEntity());
//            System.out.println(entityView.getNode());
//            System.out.println(entityView.getNode().getLayoutBounds());
            if (entityView.isMarkedForDelete()) {
                System.out.println(entityView);
                System.out.println(entityView.getEntity());
                System.out.println(entityView.getNode());
                System.out.println(entityView.getNode().getLayoutBounds());
                pane.getChildren().remove(entityView.getNode());
            }
        }

//        for (EntityView entityView: entityViews) {
//            ImageView stickmanView = (ImageView) this.model.getEntityViewStickman().getNode();
//            System.out.println(entityView);
//            System.out.println(entityView.getEntity());
//            System.out.println(entityView.getNode());
//            System.out.println(entityView.getNode().getLayoutBounds());
//            ImageView blobView = (ImageView) entityView.getNode();
//            if (
//                stickmanView.intersects(blobView.getLayoutBounds())
//            ) {
//                System.out.println("Intersects mushroom. Get powerup!");
//                this.model.getEntityViewStickman().setMushroomPowerUp(true);
//                entityView.markForDelete();
//                pane.getChildren().remove(entityView.getNode());
//            }
//        }
        entityViews.removeIf(EntityView::isMarkedForDelete);

        if (this.model.getEntityViewStickman().getNumLives() == 0) {
//            System.out.println("LOSING GAME SCENE.");
            SceneGameResult deathScene = new SceneGameResult(this.width, this.height, "You lose! =(");
            this.pane.getChildren().removeAll();
            this.pane.getChildren().addAll(deathScene.getScreen(), deathScene.getScreenLabel());
        }

        if (this.model.getEntityViewStickman().getWinStatus()) {
//            System.out.println("LOSING GAME SCENE.");
            SceneGameResult winScene = new SceneGameResult(this.width, this.height, "You win! =D");
            this.pane.getChildren().removeAll();
            this.pane.getChildren().addAll(winScene.getScreen(), winScene.getScreenLabel());
        }

//        if (
//            this.model.getEntityViewStickman().getNode().intersects(entityView.getNode().getLayoutBounds()) &&
//            entityView.getEntity().getType().equals("flag")
//        ) {
//            SceneGameResult winScene = new SceneGameResult(this.width, this.height, "You win! =D");
//            this.pane.getChildren().removeAll();
//            this.pane.getChildren().addAll(winScene.getScreen(), winScene.getScreenLabel());
//        }
    }

    private void refreshStickmanFrame(GameEngine model, double xViewportOffset) {
        EntityViewStickman stickman = model.getEntityViewStickman();
        stickman.update(xViewportOffset);
//        EntityViewStickman stickman = model.getEntityViewStickman();
//        this.pane.getChildren().remove(this.previousStickmanFrame);
//        this.previousStickmanFrame = stickman.update(xViewportOffset);
//        this.pane.getChildren().add(this.previousStickmanFrame);
    }

    private boolean hasCollided(EntityViewStickman stickman, EntityView entityView) {
        double entityViewHeight = entityView.getNode().getBoundsInParent().getHeight();
        double entityViewWidth = entityView.getNode().getBoundsInParent().getWidth();
        double entityViewXPos = entityView.getNode().getLayoutX();
        double entityViewYPos = entityView.getNode().getLayoutY();

        double stickmanXPos = stickman.getXPosition();
        double stickmanYPos = stickman.getYPosition();
        double stickmanWidth = stickman.getWidth();
        double stickmanHeight = stickman.getHeight();

        return (
            (stickmanXPos < (entityViewXPos + entityViewWidth)) &&
            ((stickmanXPos + stickmanWidth) > entityViewXPos) &&
            (stickmanYPos < (entityViewYPos + entityViewHeight)) &&
            ((stickmanYPos + stickmanHeight) > entityViewYPos)
        );
    }

//    private void addStationaryEntities(GameEngine model, Pane pane) {
//        List<Platform> platformList = model.getCurrentLevel().getPlatforms();
//
//        for (Platform platform: platformList) {
//            pane.getChildren().add(platform.getHBox());
//        }
//    }
}
