package stickman.model;

import java.net.URL;

public class EntityImpl implements Entity {

    protected final String type;
    protected final double width;
    protected final double height;
    protected double XPos;
    protected double YPos;
    protected final String imagePath;
    protected final Layer layer;

    public EntityImpl(
            String type,
            double width,
            double height,
            double XPos,
            double YPos,
            String imagePath,
            Layer layer
    ) {
        this.type = type;
        this.width = width;
        this.height = height;
        this.XPos = XPos;
        this.YPos = YPos;
        this.imagePath= imagePath;
        this.layer = layer;
    }

    @Override
    public String getType() { return this.type; }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getXPos() { return this.XPos; }

    @Override
    public double getYPos() { return this.YPos; }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

}