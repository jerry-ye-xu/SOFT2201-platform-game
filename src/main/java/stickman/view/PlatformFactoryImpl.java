package stickman.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import org.json.simple.JSONObject;
import stickman.model.Layer;

import java.lang.UnsupportedOperationException;

public class PlatformFactoryImpl implements PlatformFactory {

    @Override
    public Platform createPlatform(String platformType, JSONObject platformProperties) {
        if (platformType.equals("stationary")) {
            return this.createPlatformStationary(platformProperties);
        } else {
            throw new UnsupportedOperationException("Only platform of type 'stationary' is supported right now.");
        }
    }

    private Platform createPlatformStationary(JSONObject platformProperties) {
        final double startWidth = (double) platformProperties.get("startWidth");
        final double endWidth = (double) platformProperties.get("endWidth");
        final double height = (double) platformProperties.get("height");
        final String imagePath = (String) platformProperties.get("imagePath");
        final Layer layer = Layer.FOREGROUND;

//        TilePane platformTiles = new TilePane();
        HBox platformHBox = new HBox();
        platformHBox.setSpacing(0);
        platformHBox.setAlignment(Pos.CENTER);
        platformHBox.setFillHeight(false);
        platformHBox.setLayoutX(startWidth);
        platformHBox.setLayoutY(height);

        // Tiles should ideally be squares.
        int singleTileWidth = 5;
        int singleTileHeight = 5;

        for (int i=0; i<(endWidth-startWidth); i=i+singleTileWidth) {
            platformHBox.getChildren().add(
                    new ImageView(new Image(
                            imagePath,
                            singleTileWidth,
                            singleTileHeight,
                            false,
                            true
                    ))
            );

//            @TODO: figure out whether we can actually use this.
//            platformHBox.setMargin(tileObj, Insets(0, 0, 0, 0));
        }

        Platform platformInstance = new PlatformStationary(
                startWidth,
                endWidth,
                height,
                imagePath,
                layer,
                platformHBox
        );

        return platformInstance;
    }
}
