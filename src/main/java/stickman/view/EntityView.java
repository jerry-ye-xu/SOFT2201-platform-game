package stickman.view;

import javafx.scene.Node;
import stickman.model.Entity;

public interface EntityView {

    void update(double xViewportOffset);

    public void updateXPos();

    public void updateYPos();

    public Entity getEntity();

    boolean matchesEntity(Entity entity);

    void markForDelete();

    Node getNode();

    boolean isMarkedForDelete();
}
