package stickman.model;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import stickman.view.EntityView;
import stickman.view.EntityViewImpl;
import stickman.view.EntityViewImplMoving;
import stickman.view.EntityViewStickman;

import java.util.List;

public class EntityViewCollisionImpl implements EntityViewCollision{
    public EntityViewCollisionImpl() { }

    public void handleCollision(
        EntityViewStickman stickman,
        List<Entity> entities,
        List<EntityViewImplMoving> movingViews,
        List<EntityView> entityViews
    ) {
        handleStickmanCollision(
          stickman,
          entities,
          movingViews,
          entityViews
        );

        handleEntitiesCollision(
            stickman,
            movingViews,
            entityViews
        );
    }

    public void handleStickmanCollision(
        EntityViewStickman stickman,
        List<Entity> entities,
        List<EntityViewImplMoving> movingViews,
        List<EntityView> entityViews
    ) {
        int onPlatformTiles = 0;
        ImageView stickmanImage = (ImageView) stickman.getNode();

        /*
            Moving Entities
         */

        for (EntityViewImplMoving movingView: movingViews) {
            ImageView movingViewImage = (ImageView) movingView.getNode();
            if (stickmanImage.intersects(movingViewImage.getLayoutBounds())) {
                if (movingView.getEntity().getType().equals("blob")) {
                    stickman.decreaseLive();
                    stickman.setMushroomPowerUp(false);
                    stickman.resetPosition();
                }
            }
        }

        /*
            Stationary Entities
         */

        for (EntityView entityView: entityViews) {
            ImageView entityViewImage = (ImageView) entityView.getNode();
            if (stickmanImage.intersects(entityViewImage.getLayoutBounds())) {
                if (entityView.getEntity().getType().equals("tile")) {
                    if ((stickmanImage.getY() < entityViewImage.getY())
                    ) {
                        onPlatformTiles += 1;
                        break;
                    } else {
                        stickman.setYspeed(0);
                    }

                } else if (entityView.getEntity().getType().equals("flag")) {
                    stickman.setWinStatus(true);

                } else if (entityView.getEntity().getType().equals("mushroom")) {
                    entities.remove(entityView.getEntity());
                    stickman.increaseScore(100);
                    stickman.setMushroomPowerUp(true);
                }
            }
        }

        if (onPlatformTiles > 0)  {
            stickman.setOnPlatform(true);
        } else {
            stickman.setOnPlatform(false);
        }
    }

    public void handleEntitiesCollision(
            EntityViewStickman stickman,
            List<EntityViewImplMoving> movingViews,
            List<EntityView> entityViews
    ) {
        for (EntityViewImplMoving movingView: movingViews) {
            ImageView movingViewImage = (ImageView) movingView.getNode();

            for (EntityViewImplMoving movingView2: movingViews) {
                ImageView movingViewImage2 = (ImageView) movingView2.getNode();
                if (movingViewImage.intersects(movingViewImage2.getLayoutBounds())
                ) {
                    if (
                        movingView.getEntity().getType().equals("fireball") &&
                        movingView2.getEntity().getType().equals("blob")
                    ) {
                        stickman.increaseScore(50);

                        movingView.markForDelete();
                        movingView2.markForDelete();
                    }
                }
            }

            // If fireball hits a stationary object remove it.
            for (EntityView entityView: entityViews) {
                ImageView entityViewImage = (ImageView) entityView.getNode();
                if (movingViewImage.intersects(entityViewImage.getLayoutBounds())) {
                    if (movingView.getEntity().getType().equals("fireball")) {
                        entityView.markForDelete();
                    }
                }
            }

        }
    }
}
