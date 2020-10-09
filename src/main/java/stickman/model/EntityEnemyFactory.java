package stickman.model;

import org.json.simple.JSONObject;

public class EntityEnemyFactory implements EntityFactory {

    @Override
    public Entity createEntity(String entityType, JSONObject entityProperties) {
        if (entityType.equals("blob")) {
            return this.createBlobEntity(entityProperties);
        } else {
            throw new UnsupportedOperationException("Only powerup entity of type 'mushroom' is supoported right now.");
        }
    }

    private Entity createBlobEntity(JSONObject entityProperties) {
        final double width = 36;
        final double height = 36;
        final double XPos = ((Long) entityProperties.get("XPos")).doubleValue();
        final double YPos = ((Long) entityProperties.get("YPos")).doubleValue();
        final String imagePath = (String) entityProperties.get("imageName");
        final Layer layer = Layer.BACKGROUND;

        final double jumpHeight = ((Long) entityProperties.get("jumpHeight")).doubleValue();
        final String attack = (String) entityProperties.get("attack");

        Entity blobInstance = new EntityBlobImpl(
            width,
            height,
            XPos,
            YPos,
            imagePath,
            layer,

        );

        return mushroomInstance;
    }
}
