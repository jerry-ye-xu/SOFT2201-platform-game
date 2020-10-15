package stickman.model;

import org.json.simple.JSONObject;

import java.lang.UnsupportedOperationException;
import java.util.ArrayList;
import java.util.List;

public class EntityListFactoryPlatform implements EntityListFactory {

    @Override
    public List<Entity> createPlatform(String platformType, JSONObject platformProperties) {
        if (platformType.equals("stationary")) {
            return this.createPlatformStationary(platformProperties);
        } else {
            throw new UnsupportedOperationException("Only platform of type 'stationary' is supported right now.");
        }
    }

    private List<Entity> createPlatformStationary(JSONObject entityProperties) {
        final double startWidth = ((Long) entityProperties.get("startWidth")).doubleValue();
        final double endWidth = ((Long) entityProperties.get("endWidth")).doubleValue();
        final double height = ((Long) entityProperties.get("height")).doubleValue();
        final String imagePath = (String) entityProperties.get("imageName");
        final Layer layer = Layer.ENTITY_LAYER;

        // Tiles should ideally be squares.
        int singleTileWidth = 12;
        int singleTileHeight = 12;

        List<Entity> tiles = new ArrayList<>();

        for (int i=0; i<(endWidth-startWidth); i=i+singleTileWidth) {
            Entity tileInstance = new EntityImplTile(
                "tile",
                singleTileWidth,
                singleTileHeight,
                startWidth + i,
                height,
                imagePath,
                layer
            );
            tiles.add(tileInstance);
        }

        return tiles;
    }
}
