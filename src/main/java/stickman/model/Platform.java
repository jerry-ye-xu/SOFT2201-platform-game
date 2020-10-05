package stickman.model;

import stickman.model.Layer;

public interface Platform {

    double getStartWidth();
    double getEndWidth();

    double getHeight();

    String getImagePath();

    Layer getLayer();

}
