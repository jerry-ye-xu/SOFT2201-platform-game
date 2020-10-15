package stickman.model;

public interface Entity {
    String getImagePath();
    String getType();
    double getXPos();
    double getYPos();
    double getHeight();
    double getWidth();
    Layer getLayer();

    void updateXPos();
    void updateYPos();
}
