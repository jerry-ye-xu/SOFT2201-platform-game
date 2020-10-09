package stickman.model;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EntityFactoryEnemy implements EntityFactory {

    @Override
    public Entity createEntity(String entityType, JSONObject entityProperties) {
        if (entityType.equals("blob")) {
            return this.createBlobEntity(entityProperties);
        } else {
            throw new UnsupportedOperationException("Only enemy entity of type 'blob' is supported right now.");
        }
    }

    private Entity createBlobEntity(JSONObject entityProperties) {
        final double width = 36;
        final double height = 36;
        final double XPos = ((Long) entityProperties.get("XPos")).doubleValue();
        final double YPos = ((Long) entityProperties.get("YPos")).doubleValue();
        final Layer layer = Layer.ENTITY_LAYER;

        final double jumpHeight = ((Long) entityProperties.get("jumpHeight")).doubleValue();
        final String attack = (String) entityProperties.get("attack");
        final String startDirection = (String) entityProperties.get("startDirection");
        final double movement = ((Long) entityProperties.get("movement")).doubleValue();

        final String colour = (String) entityProperties.get("colour");

        List<String> imagePathList = this.getImagePathForColour(colour);

        // Load the first image to be consistent with EntityView.
        String imagePath = imagePathList.get(0) + "a" + imagePathList.get(1);

        Entity blobInstance = new EntityImplBlob(
            width,
            height,
            XPos,
            YPos,
            imagePath,
            layer,
            jumpHeight,
            attack,
            startDirection,
            movement,
            imagePathList.get(0),
            imagePathList.get(1)
        );

        return blobInstance;
    }

    private List<String> getImagePathForColour(String colour) {
        List<String> imagePathList = new ArrayList<>();
        if (colour.equals("blue")) {
            imagePathList.add("/slimeB");
        } else if (colour.equals("green")) {
            imagePathList.add("/slimeG");
        } else if (colour.equals("purple")) {
            imagePathList.add("/slimeP");
        } else if (colour.equals("red")) {
            imagePathList.add("/slimeR");
        } else if (colour.equals("yellow")) {
            imagePathList.add("/slimeY");
        } else {
            throw new UnsupportedOperationException("'blob' colour specified is not supported right now.");
        }
        // So far only this type is supported.
        imagePathList.add(".png");
        return imagePathList;
    }
}
