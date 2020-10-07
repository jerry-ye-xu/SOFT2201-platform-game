package stickman.model;

public class EntityFlagImpl extends EntityImpl {

    public EntityFlagImpl(
            double width,
            double height,
            double XPos,
            double YPos,
            String imagePath,
            Layer layer
    ) {
        super(width, height, XPos, YPos, imagePath, layer);
    }

    public void winStatusStickman(Stickman stickman) {
        stickman.setWinStatus(true);
    }
}
