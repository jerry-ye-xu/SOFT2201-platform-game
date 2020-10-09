package stickman.model;

import stickman.view.EntityViewStickman;

public class EntityImplFlag extends EntityImpl {

    public EntityImplFlag(
            double width,
            double height,
            double XPos,
            double YPos,
            String imagePath,
            Layer layer
    ) {
        super(width, height, XPos, YPos, imagePath, layer);
    }

    public void winStatusStickman(EntityViewStickman entityViewStickman) {
        entityViewStickman.setWinStatus(true);
    }
}
