package stickman.model;

import stickman.view.EntityViewStickman;

public class EntityImplMushroom extends EntityImpl {
    public EntityImplMushroom(
        double width,
        double height,
        double XPos,
        double YPos,
        String imagePath,
        Layer layer
    ) {
        super(width, height, XPos, YPos, imagePath, layer);
    }

    public void powerUpStickman(EntityViewStickman entityViewStickman) {
        entityViewStickman.setMushroomPowerUp(true);
    }
}
