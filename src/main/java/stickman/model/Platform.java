package stickman.model;

import stickman.model.Layer;

import javafx.scene.layout.HBox;

public interface Platform {

    double getStartWidth();
    double getEndWidth();

    double getHeight();

    String getImagePath();

    Layer getLayer();

    HBox getHBox();

}
