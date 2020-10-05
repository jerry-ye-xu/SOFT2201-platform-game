package stickman.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import org.json.simple.JSONObject;

import java.nio.charset.IllegalCharsetNameException;

public class PlatformFactoryStationary implements PlatformFactory {

    @Override
    public Platform createPlatform(JSONObject platformProperties) {
        final int startWidth = (int) platformProperties.get("startWidth");
        final int endWidth = (int) platformProperties.get("endWidth");
        final int height = (int) platformProperties.get("height");
        final String imagePath = (String) platformProperties.get("imagePath");
        final Layer layer = Layer.FOREGROUND;

//        TilePane platformTiles = new TilePane();
        HBox platformHBox = new HBox();
        platformHBox.setSpacing(0);
        platformHBox.setAlignment(Pos.CENTER);
        platformHBox.setFillHeight(false);

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

            platformHBox.setLayoutX(startWidth);
            platformHBox.setLayoutY(height);

//            @TODO: figure out whether we can actually use this.
//            platformHBox.setMargin(tileObj, Insets(0, 0, 0, 0));
        }

        ImageView singleTile = new ImageView(new Image(
            imagePath,
            8,
            8,
            false,
            true
        ));

        Platform platformInstance = new PlatformStationary(
            startWidth,
            endWidth,
            height,
            imagePath,
            layer
        );

        // @TODO Move this into constructor method if possible.

//        platformInstance.

        return null;
    }

    private void createPlatformImages() {

    }
}
