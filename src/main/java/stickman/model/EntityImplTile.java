package stickman.model;

public class EntityImplTile extends EntityImpl {

    public EntityImplTile(
        String type,
        double width,
        double height,
        double XPos,
        double YPos,
        String imagePath,
        Layer layer
    ) {
        super(type, width, height, XPos, YPos, imagePath, layer);
    }
}
