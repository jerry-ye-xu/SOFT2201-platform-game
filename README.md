## Table of Contents

- [Table of Contents](#table-of-contents)
- [Setup](#setup)
- [JSON File](#json-file)

---

## Setup

To run the code, you will need to place the JavaFX SDK-11.0.2 into the root directory.

Set up a new IntelliJ project, and configure the VM option to be

```bash
--module-path ./javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml,javafx.media
```
Alternatively you can overwrite `./javafx-sdk-11.0.2/lib` to another <path> of your choice.


The main class should be `stickman.App` with `JRE=11`.


## JSON File

The structure of the JSON file is briefly outlined.

```
-- stickmanSize
-- stickmanStartingPos
-- cloudVelocity
-- levelOne
    -- height
        ...
    -- flagPosition
        -- height
            ...
        -- XPos
        -- imageName
    -- platformList
        --
            -- type
                ...
            -- imageName
        ...
        --
            -- type
                ...
            -- imageName
    -- powerUpList
        --
            -- type
                ...
            -- imageName
        ...
        --
            -- type
                ...
            -- imageName
    -- enemyList
        --
            -- type
                ...
            -- imageName
        ...
        --
            -- type
                ...
            -- imageName
-- levelTwo
    ...
```
Each level has their own config w.r.t the height and width of the level and the entities (both moving and stationary, friendly and enemy).

The `platformList` specifies the tile entites that are put together to form platforms. A enemy object can only move on the platforms or on the floor.

The only supported enemyObject is 'blob'. This entity has no jump and only moves left and right.

For `powerUpList` the only supported powerup in this game is the mushroom, which allows the stickman to shoot fireballs.

Feel free to configure the levels, but blobs falling is not supported so they must sit on a platform and their movement range be within the immediate platform they spawn on.

Once the 'blob' is hit with a fireball, it disappears.

__How to load different levels__

You can choose a different level by changing the `levelName` variable in the `App.java` file in `/src`.

__Shooting fireballs__

Once you have obtained a mushroom powerup, you can shoot fireballs with `SPACE-BAR`.

If you lose a life, the powerup disappears and you will need to get another mushroom. Note that the mushrooms, once eaten do not reappear!
