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
import stickman.view.EntityViewFireball;
import stickman.model.GameEngine;

import java.util.ArrayList;
import java.util.List;

public class GameWindow {
    private final int width;
    private final int height;
    private SceneStats gameStats;
    private Scene scene;
    private Pane pane;
    private GameEngine model;
    private ImageView previousStickmanFrame;
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
        this.pane.getChildren().add(this.model.getEntityViewStickman().getNode());

        for (EntityViewBlob blob: this.model.getEntityViewBlobList()) {
            System.out.println("blob: " + blob);
            this.pane.getChildren().add(blob.getNode());
        }
        int numLives = this.model.getEntityViewStickman().getNumLives();
        int score = this.model.getEntityViewStickman().getScore();
        this.gameStats = new SceneStats(numLives, score);
        this.pane.getChildren().addAll(gameStats.getlivesLabel(), gameStats.getScoreLabel());
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
        this.gameStats.updateStats(this.model.getEntityViewStickman());
        this.refreshStickmanFrame(this.model, xViewportOffset);

        List<Entity> entities = this.model.getCurrentLevel().getEntities();

        for (EntityView entityView: entityViews) {
            entityView.markForDelete();
        }

        for (EntityViewBlob blob: this.model.getEntityViewBlobList()) {
            blob.update(xViewportOffset);
        }

        for (EntityViewFireball fireball: this.model.getCurrentLevel().getEntityViewFireballList()) {
            fireball.update(xViewportOffset);

            if (!pane.getChildren().contains(fireball.getNode())) {
                pane.getChildren().add(fireball.getNode());
            }

            ImageView fireballView = (ImageView) fireball.getNode();
            for (EntityViewBlob blob: this.model.getEntityViewBlobList()) {
                ImageView blobView = (ImageView) blob.getNode();
                if (fireballView.intersects(blobView.getLayoutBounds())) {
                    this.model.getEntityViewStickman().increaseScore(50);

                    blob.markForDelete();
                    fireball.markForDelete();
                    pane.getChildren().remove(blob.getNode());
                }
            }

            for (EntityView entityView: entityViews) {
                ImageView entityNode = (ImageView) entityView.getNode();
                if (fireballView.intersects(entityNode.getLayoutBounds())) {
                    fireball.markForDelete();
                }
            }

            if (fireball.isMarkedForDelete()) {
                pane.getChildren().remove(fireball.getNode());
            }

        }

        this.model.getCurrentLevel().getEntityViewFireballList().removeIf(EntityView::isMarkedForDelete);
        this.model.getEntityViewBlobList().removeIf(EntityView::isMarkedForDelete);

        for (EntityViewBlob blob: this.model.getEntityViewBlobList()) {
            ImageView stickmanView = (ImageView) this.model.getEntityViewStickman().getNode();
            ImageView blobView = (ImageView) blob.getNode();
            if (stickmanView.intersects(blobView.getLayoutBounds())) {
                this.model.getEntityViewStickman().decreaseLive();
                this.model.getEntityViewStickman().setMushroomPowerUp(false);
                this.model.getEntityViewStickman().resetPosition();
            }
        }

        onPlatformTiles = 0;
        for (EntityView entityView: entityViews) {
            ImageView stickmanView = (ImageView) this.model.getEntityViewStickman().getNode();
            ImageView entityImage = (ImageView) entityView.getNode();
            if (
                stickmanView.intersects(entityImage.getLayoutBounds()) &&
                entityView.getEntity().getType().equals("tile")
            ) {
                if ((stickmanView.getY() < entityImage.getY())
                ) {
                    onPlatformTiles += 1;
                    break;
                } else {
                    this.model.getEntityViewStickman().setYspeed(0);
                }
            }
        }
        if (onPlatformTiles > 0)  {
            this.model.getEntityViewStickman().setOnPlatform(true);
        } else {
            this.model.getEntityViewStickman().setOnPlatform(false);
        }


//        if (this.model.getEntityViewStickman().getOnPlatform()) {
//            System.out.println("this.model.getEntityViewStickman().getOnPlatform(): " + this.model.getEntityViewStickman().getOnPlatform());
//            this.model.getEntityViewStickman().setYOffset(this.yViewportOffset);
//        } else {
//            this.model.getEntityViewStickman().setYOffset(0);
//        }


        for (EntityView entityView: entityViews) {
            ImageView stickmanView = (ImageView) this.model.getEntityViewStickman().getNode();
            ImageView entityImage = (ImageView) entityView.getNode();
//            System.out.println("blobView.getLayoutBounds(): " + blobView.getLayoutBounds());
//            System.out.println("stickmanView.getLayoutBounds(): " + stickmanView.getLayoutBounds());
            if (
                stickmanView.intersects(entityImage.getLayoutBounds()) &&
                entityView.getEntity().getType().equals("flag")
            ) {
                System.out.println("Intersects an flag");
                this.model.getEntityViewStickman().setWinStatus(true);
            }
            if (
                stickmanView.intersects(entityImage.getLayoutBounds()) &&
                entityView.getEntity().getType().equals("mushroom")
            ) {
                System.out.println("Intersects an mushroom");
                this.model.getCurrentLevel().getEntities().remove(entityView.getEntity());
                this.model.getEntityViewStickman().increaseScore(100);
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
//                    System.out.println(view.getEntity());getType()
//                    System.out.println(view.getEntity().);
                    if (view.getEntity().getType().equals("fireball")) {
                        System.out.println(view.getEntity().getType());
                        System.out.println(view.getNode().getLayoutX());
                        System.out.println(view.getNode().getLayoutY());
                    }
                    view.update(xViewportOffset);
                    break;
                }
            }
            if (notFound) {
                EntityView entityView;
//                if (entity.getType().equals("fireball")) {
//                    entityView = new EntityViewFireball(entity);
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

        entityViews.removeIf(EntityView::isMarkedForDelete);

        if (this.model.getEntityViewStickman().getNumLives() == 0) {
            SceneGameResult deathScene = new SceneGameResult(this.width, this.height, "You lose! =(");
            this.pane.getChildren().removeAll();
            this.pane.getChildren().addAll(deathScene.getScreen(), deathScene.getScreenLabel());
        }

        if (this.model.getEntityViewStickman().getWinStatus()) {
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

    private void updateGameScore() {

    }

//    private void addStationaryEntities(GameEngine model, Pane pane) {
//        List<Platform> platformList = model.getCurrentLevel().getPlatforms();
//
//        for (Platform platform: platformList) {
//            pane.getChildren().add(platform.getHBox());
//        }
//    }
}
