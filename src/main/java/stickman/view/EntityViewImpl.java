package stickman.view;

import stickman.model.Entity;
import stickman.model.Layer;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class EntityViewImpl implements EntityView {
    protected Entity entity;
    protected boolean delete = false;
    protected ImageView node;
    protected String imagePath;
    protected double xPosition;
    protected double yPosition;

    EntityViewImpl(Entity entity) {
        this.entity = entity;

        this.imagePath = this.entity.getImagePath();
        URL imageURL = this.getClass().getResource(this.imagePath);
        this.node = new ImageView(imageURL.toExternalForm());
        this.node.setViewOrder(getViewOrder(this.entity.getLayer()));

        this.xPosition = entity.getXPos();
        this.yPosition = entity.getYPos();

        this.node.setX(this.entity.getXPos());
        this.node.setY(this.entity.getYPos());

        this.node.setFitHeight(entity.getHeight());
        this.node.setFitWidth(entity.getWidth());
        this.node.setPreserveRatio(true);
        this.delete = false;

    }

    private double getViewOrder(Layer layer) {
        switch (layer) {
            case BACKGROUND: return 100.0;
            case ENTITY_LAYER: return 75.0;
            case FOREGROUND: return 50.0;
            case EFFECT: return 25.0;
            default: throw new IllegalStateException("Javac doesn't understand how enums work so now I have to exist");
        }
    }

    @Override
    public void update(double xViewportOffset) {
        String newPath = entity.getImagePath();
        if (!imagePath.equals(newPath)) {
            imagePath = newPath;
            node.setImage(new Image(imagePath));
        }

        node.setX(this.xPosition - xViewportOffset);
        node.setY(this.yPosition);
        node.setFitHeight(entity.getHeight());
        node.setFitWidth(entity.getWidth());
        node.setPreserveRatio(true);
        delete = false;
    }

    public void updateXPos() { }

    public void updateYPos() { }

    @Override
    public Entity getEntity() { return this.entity; }

    @Override
    public boolean matchesEntity(Entity entity) {
        return this.entity.equals(entity);
    }

    @Override
    public void markForDelete() {
        this.delete = true;
    }

    @Override
    public Node getNode() { return this.node; }

    @Override
    public boolean isMarkedForDelete() {
        return this.delete;
    }
}
