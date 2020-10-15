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
import stickman.model.Level;

import java.util.ArrayList;
import java.util.List;

public class GameWindow {
    private final int width;
    private final int height;
    private SceneStats gameStats;
    private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews;
    private BackgroundDrawer backgroundDrawer;

    private int onPlatformTiles = 0;
    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
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

        this.model.getCurrentLevel().addEntityViewToPane(this.pane);

        EntityViewStickman stickmanView = this.model.getCurrentLevel().getEntityViewStickman();
//        List<EntityViewBlob> blobView = this.model.getCurrentLevel().getEntityViewBlobList();

//        this.pane.getChildren().add(stickmanView.getNode());

//        for (EntityViewBlob blob: blobView) {
//            this.pane.getChildren().add(blob.getNode());
//        }

//        this.gameStats = new SceneStats(
//            stickmanView.getNumLives(),
//            stickmanView.getScore()
//        );
//        this.pane.getChildren().addAll(
//                gameStats.getlivesLabel(),
//                gameStats.getScoreLabel()
//        );
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

        EntityViewStickman stickmanView = this.model.getCurrentLevel().getEntityViewStickman();
//        List<EntityViewBlob> blobViewList =  this.model.getCurrentLevel().getEntityViewBlobList();
//        List<EntityViewFireball> fireballViewList = this.model.getCurrentLevel().getEntityViewFireballList();

//        stickmanView.update(xViewportOffset);
//        this.gameStats.updateStats(stickmanView);

        /*
            Handling Blob Collision
         */

//        for (EntityViewBlob blob: blobViewList) {
//            blob.update(xViewportOffset);
//        }
//
//        for (EntityViewBlob blob: blobViewList) {
//            ImageView stickmanImage = (ImageView) stickmanView.getNode();
//            ImageView blobImage = (ImageView) blob.getNode();
//            if (stickmanImage.intersects(blobImage.getLayoutBounds())) {
//                stickmanView.decreaseLive();
//                stickmanView.setMushroomPowerUp(false);
//                stickmanView.resetPosition();
//            }
//        }

        /*
            Handling Fireball mechanism
         */

//        for (EntityViewFireball fireball: fireballViewList) {
//            fireball.update(xViewportOffset);
//
//            if (!pane.getChildren().contains(fireball.getNode())) {
//                pane.getChildren().add(fireball.getNode());
//            }
//
//            ImageView fireballView = (ImageView) fireball.getNode();
//            for (EntityViewBlob blob: blobViewList) {
//                ImageView blobView = (ImageView) blob.getNode();
//                if (fireballView.intersects(blobView.getLayoutBounds())) {
//                    stickmanView.increaseScore(50);
//
//                    blob.markForDelete();
//                    fireball.markForDelete();
//                    pane.getChildren().remove(blob.getNode());
//                }
//            }
//
//            // If fireball hits a stationary object remove it.
//            for (EntityView entityView: entityViews) {
//                ImageView entityNode = (ImageView) entityView.getNode();
//                if (fireballView.intersects(entityNode.getLayoutBounds())) {
//                    fireball.markForDelete();
//                }
//            }
//
//            if (fireball.isMarkedForDelete()) {
//                pane.getChildren().remove(fireball.getNode());
//            }
//
//        }
//
//        fireballViewList.removeIf(EntityView::isMarkedForDelete);
//        blobViewList.removeIf(EntityView::isMarkedForDelete);

        /*
            Handling Tile/Platform Collision
         */

//        onPlatformTiles = 0;
//        for (EntityView entityView: entityViews) {
//            ImageView stickmanImage = (ImageView) stickmanView.getNode();
//            ImageView entityImage = (ImageView) entityView.getNode();
//            if (
//                stickmanImage.intersects(entityImage.getLayoutBounds()) &&
//                entityView.getEntity().getType().equals("tile")
//            ) {
//                if ((stickmanImage.getY() < entityImage.getY())
//                ) {
//                    onPlatformTiles += 1;
//                    break;
//                } else {
//                    stickmanView.setYspeed(0);
//                }
//            }
//        }
//        if (onPlatformTiles > 0)  {
//            stickmanView.setOnPlatform(true);
//        } else {
//            stickmanView.setOnPlatform(false);
//        }

        /*
            Handling stationary entities
         */

        List<Entity> entities = this.model.getCurrentLevel().getEntities();

        for (EntityView entityView: entityViews) {
            entityView.markForDelete();
        }

//        for (EntityView entityView: entityViews) {
//            ImageView stickmanImage = (ImageView) stickmanView.getNode();
//            ImageView entityImage = (ImageView) entityView.getNode();
//            if (
//                stickmanImage.intersects(entityImage.getLayoutBounds()) &&
//                entityView.getEntity().getType().equals("flag")
//            ) {
////                System.out.println("Intersects an flag");
//                stickmanView.setWinStatus(true);
//            }
//
//            if (
//                stickmanImage.intersects(entityImage.getLayoutBounds()) &&
//                entityView.getEntity().getType().equals("mushroom")
//            ) {
////                System.out.println("Intersects an mushroom");
//                entities.remove(entityView.getEntity());
//                stickmanView.increaseScore(100);
//                stickmanView.setMushroomPowerUp(true);
//            }
//        }

//        double heroXPos = stickmanView.getXPosition();
//        heroXPos -= xViewportOffset;
//
//        if (heroXPos < VIEWPORT_MARGIN) {
//            if (xViewportOffset >= 0) { // Don't go further left than the start of the level
//                xViewportOffset -= VIEWPORT_MARGIN - heroXPos;
//                if (xViewportOffset < 0) {
//                    xViewportOffset = 0;
//                }
//            }
//        } else if (heroXPos > width - VIEWPORT_MARGIN) {
//            xViewportOffset += heroXPos - (width - VIEWPORT_MARGIN);
//
//            double stickmanWidth = stickmanView.getWidth();
//
//            if (xViewportOffset >= this.model.getCurrentLevel().getWidth() - width + stickmanWidth) {
//                xViewportOffset = this.model.getCurrentLevel().getWidth() - width + stickmanWidth;
//            }
//        }

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
                entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView: entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }

        backgroundDrawer.update(xViewportOffset);
        entityViews.removeIf(EntityView::isMarkedForDelete);

        /*
            Game ending conditions.
         */

//        if (stickmanView.getNumLives() == 0) {
//            SceneGameResult deathScene = new SceneGameResult(this.width, this.height, "You lose! =(");
//            this.pane.getChildren().removeAll();
//            this.pane.getChildren().addAll(deathScene.getScreen(), deathScene.getScreenLabel());
//        }
//
//        if (stickmanView.getWinStatus()) {
//            SceneGameResult winScene = new SceneGameResult(this.width, this.height, "You win! =D");
//            this.pane.getChildren().removeAll();
//            this.pane.getChildren().addAll(winScene.getScreen(), winScene.getScreenLabel());
//        }

    }
}
