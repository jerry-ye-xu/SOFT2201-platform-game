package stickman.model;

public class EntityMushroomImpl extends EntityImpl {
    public EntityMushroomImpl(
        double width,
        double height,
        double XPos,
        double YPos,
        String imagePath,
        Layer layer
    ) {
        super(width, height, XPos, YPos, imagePath, layer);
    }

    public void powerUpStickman(Stickman stickman) {
        stickman.setMushroomPowerUp(true);
    }
}
