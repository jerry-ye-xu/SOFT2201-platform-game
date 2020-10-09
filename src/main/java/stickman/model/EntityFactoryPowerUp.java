package stickman.model;

import org.json.simple.JSONObject;

public class EntityFactoryPowerUp implements EntityFactory {

    @Override
    public Entity createEntity(String entityType, JSONObject entityProperties) {
        if (entityType.equals("mushroom")) {
            return this.createMushroomEntity(entityProperties);
        } else {
            throw new UnsupportedOperationException("Only powerup entity of type 'mushroom' is supoported right now.");
        }
    }

    private Entity createMushroomEntity(JSONObject entityProperties) {
        final double width = ((Long) entityProperties.get("width")).doubleValue();
        final double height = ((Long) entityProperties.get("height")).doubleValue();
        final double XPos = ((Long) entityProperties.get("XPos")).doubleValue();
        final double YPos = ((Long) entityProperties.get("YPos")).doubleValue();
        final String imagePath = (String) entityProperties.get("imageName");
        final Layer layer = Layer.ENTITY_LAYER;

        Entity mushroomInstance = new EntityImplMushroom(
            "mushroom",
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
