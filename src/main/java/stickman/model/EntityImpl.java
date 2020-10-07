package stickman.model;

import java.net.URL;

public class EntityImpl implements Entity {

    protected final double width;
    protected final double height;
    protected final double XPos;
    protected final double YPos;
    protected final String imagePath;
    protected final Layer layer;

    public EntityImpl(
        double width,
        double height,
        double XPos,
        double YPos,
        String imagePath,
        Layer layer
    ) {
        this.width = width;
        this.height = height;
        this.XPos = XPos;
        this.YPos = YPos;
//        URL imageURL = this.getClass().getResource(imagePath);
//        System.out.println("imagePath");
//        System.out.println(imagePath);
//        System.out.println("imageUrl");
//        System.out.println("imageURL.toExternalForm()");
//        System.out.println(imageURL);
//        System.out.println(imageURL.toExternalForm());
//        this.imagePath = imageURL.toExternalForm();
        this.imagePath= imagePath;
        this.layer = layer;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getXPos() { return this.XPos; }

    @Override
    public double getYPos() { return this.YPos; }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

}
