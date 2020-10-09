package stickman.model;

public class EntityTileImpl extends EntityImpl {

    public EntityTileImpl(
        double width,
        double height,
        double XPos,
        double YPos,
        String imagePath,
        Layer layer
    ) {
        super(width, height, XPos, YPos, imagePath, layer);
    }
}
