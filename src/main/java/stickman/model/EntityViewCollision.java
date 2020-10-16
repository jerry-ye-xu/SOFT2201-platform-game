package stickman.model;

import stickman.view.EntityView;
import stickman.view.EntityViewImplMoving;
import stickman.view.EntityViewStickman;

import java.util.List;

public interface EntityViewCollision {
    void handleCollision(
            EntityViewStickman stickman,
            List<Entity> entities,
            List<EntityViewImplMoving> movingViews,
            List<EntityView> entityViews
    );
}
