package stickman.model;

import org.json.simple.JSONObject;

public class PowerUpEntityFactory implements EntityFactory {

    @Override
    public Entity createEntity(String entityType, JSONObject entityProperties) {
        if (entityType.equals("mushroom")) {
            return this.createMushroomEntity(entityProperties);
        } else {
            throw new UnsupportedOperationException("Only powerup entity of type 'mushroom' is supoported right now.");
        }
    }

    private Entity createMushroomEntity(JSONObject entityProperties) {
        final double width = (double) entityProperties.get("width");
        final double height = (double) entityProperties.get("height");
        final double XPos = (double) entityProperties.get("XPos");
        final double YPos = (double) entityProperties.get("YPos");
        final String imagePath = (String) entityProperties.get("imagePath");
        final Layer layer = Layer.FOREGROUND;

        Entity mushroomInstance = new EntityMushroomImpl(
            width,
            height,
            XPos,
            YPos,
            imagePath,
            layer
        );

        return mushroomInstance;
    }
}
