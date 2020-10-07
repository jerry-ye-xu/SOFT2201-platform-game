package stickman.view;

import stickman.model.Layer;
import javafx.scene.layout.HBox;

public class PlatformStationary implements Platform {

    protected final double startWidth;
    protected final double endWidth;
    protected final double height;
    protected final String imagePath;
    protected final Layer layer;

    protected final HBox platformHBox;

    public PlatformStationary(
        double startWidth,
        double endWidth,
        double height,
        String imagePath,
        Layer layer,
        HBox platformHBox
    ) {
        this.startWidth = startWidth;
        this.endWidth = endWidth;
        this.height = height;
        this.imagePath = imagePath;
        this.layer = layer;
        this.platformHBox = platformHBox;
    }

    @Override
    public double getStartWidth() {
        return this.startWidth;
    }

    @Override
    public double getEndWidth() {
        return this.endWidth;
    }

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

    @Override
    public HBox getHBox() { return this.platformHBox; }
}
