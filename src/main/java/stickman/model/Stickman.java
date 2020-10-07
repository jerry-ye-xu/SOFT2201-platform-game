package stickman.model;

public class Stickman {

    private final String size;
    private final double startingXPos;
    private boolean mushroomPowerUp = false;
    private boolean winStatus = false;
    private int numLives = 4;
    private int score = 0;

    public Stickman(
        String size,
        double startingXPos
    ) {
        this.size = size;
        this.startingXPos = startingXPos;
    }

    public void setMushroomPowerUp(boolean mushroomPowerUp) {
        this.mushroomPowerUp = mushroomPowerUp;
    }

    public void setWinStatus(boolean winStatus) {
        this.winStatus = winStatus;
    }
}
