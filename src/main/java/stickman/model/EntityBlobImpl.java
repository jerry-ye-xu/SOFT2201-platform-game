package stickman.model;

public class EntityBlobImpl extends EntityImpl {
    protected double jumpHeight;
    protected String attack;
    protected String startDirection;
    protected double movement;

    public EntityBlobImpl(
            double width,
            double height,
            double XPos,
            double YPos,
            String imagePath,
            Layer layer,
            double jumpHeight,
            String attack,
            String startDirection,
            double movement
    ) {
        super(width, height, XPos, YPos, imagePath, layer);

        this.jumpHeight = jumpHeight;
        this.attack = attack;
        this.startDirection = startDirection;
        this.movement = movement;
    }
}
