package stickman.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import stickman.model.*;

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

        this.model.getCurrentLevel().addEntityViewsToPane(this.pane);

        EntityViewStickman stickmanView = this.model.getCurrentLevel().getEntityViewStickman();

        this.gameStats = new SceneStats(
            stickmanView.getNumLives(),
            stickmanView.getScore()
        );
        this.pane.getChildren().addAll(
                gameStats.getlivesLabel(),
                gameStats.getScoreLabel()
        );
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
        this.model.getCurrentLevel().addEntityViewsToPane(this.pane);

        EntityViewStickman stickmanView = this.model.getCurrentLevel().getEntityViewStickman();
        List<Entity> entities = this.model.getCurrentLevel().getEntities();
        List<EntityViewImplMoving> movingViews = this.model.getCurrentLevel().getEntityViewsMovingList();

        EntityViewCollision entityViewCollision = new EntityViewCollisionImpl();

        for (EntityViewImplMoving movingView: movingViews) {
            movingView.update(xViewportOffset);

            if (movingView.isMarkedForDelete()) {
                pane.getChildren().remove(movingView.getNode());
            }
        }

        movingViews.removeIf(EntityView::isMarkedForDelete);

        stickmanView.update(xViewportOffset);
        this.gameStats.updateStats(stickmanView);

        /*
            Handling Collision
         */

        entityViewCollision.handleCollision(
                stickmanView,
                entities,
                movingViews,
                entityViews

        );

        /*
            Handling stationary entities
         */

        for (EntityView entityView: entityViews) {
            entityView.markForDelete();
        }

        for (EntityView entityView: entityViews) {
            ImageView stickmanImage = (ImageView) stickmanView.getNode();
            ImageView entityImage = (ImageView) entityView.getNode();
            if (
                stickmanImage.intersects(entityImage.getLayoutBounds()) &&
                entityView.getEntity().getType().equals("flag")
            ) {
                stickmanView.setWinStatus(true);
            }

            if (
                stickmanImage.intersects(entityImage.getLayoutBounds()) &&
                entityView.getEntity().getType().equals("mushroom")
            ) {
                entities.remove(entityView.getEntity());
                stickmanView.increaseScore(100);
                stickmanView.setMushroomPowerUp(true);
            }
        }

        double heroXPos = stickmanView.getXPosition();
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

            double stickmanWidth = stickmanView.getWidth();

            if (xViewportOffset >= this.model.getCurrentLevel().getWidth() - width + stickmanWidth) {
                xViewportOffset = this.model.getCurrentLevel().getWidth() - width + stickmanWidth;
            }
        }

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

        if (stickmanView.getNumLives() == 0) {
            SceneGameResult deathScene = new SceneGameResult(this.width, this.height, "You lose! =(");
            this.pane.getChildren().removeAll();
            this.pane.getChildren().addAll(deathScene.getScreen(), deathScene.getScreenLabel());
        }

        if (stickmanView.getWinStatus()) {
            SceneGameResult winScene = new SceneGameResult(this.width, this.height, "You win! =D");
            this.pane.getChildren().removeAll();
            this.pane.getChildren().addAll(winScene.getScreen(), winScene.getScreenLabel());
        }

    }
}
