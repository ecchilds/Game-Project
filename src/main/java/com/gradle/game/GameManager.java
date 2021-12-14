package com.gradle.game;

import com.gradle.game.entities.BackWall;
//import com.gradle.game.entities.CustomPropMapObjectLoader;
import com.gradle.game.entities.DoorWay;
import com.gradle.game.entities.Player;
import com.gradle.game.entities.PlayerManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
//import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.PropMapObjectLoader;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.PositionLockCamera;

public final class GameManager {
    private GameManager() {
    }

    private static GameType currentGameType = GameType.SINGLEPLAYER;
    private static int players = 1;

    public static void init() {

        //set locked camera to player
        //TODO: modify for multiplayer
        Camera camera = new PositionLockCamera(Player.instance());
        camera.setClampToMap(true);
        Game.world().setCamera(camera);

        PlayerManager.init();

        //allows doors to function, and know what to load
        //Environment.registerMapObjectLoader(new CustomPropMapObjectLoader());
        PropMapObjectLoader.registerCustomPropType(DoorWay.class);

        //set loader for background
        PropMapObjectLoader.registerCustomPropType(BackWall.class);

//        Input.gamepads().onAdded(e -> {
//            //TODO: add multiplayer support
//            if (Input.gamepads().getAll().size() < 2) {
//                if (currentGameType == GameType.SINGLEPLAYER) {
//                    //TODO: attach GamePad to player
//                    IMovementController gamepadController = new GamepadEntityController<Player>(PlayerManager.getCurrent(), false);
//                    Player.instance().addController(gamepadController);
//                } else if (currentGameType == GameType.COOP) {
//
//                }
//            }
//        });

        // add default game logic for when a level was loaded
        Game.world().onLoaded(e -> {

            // spawn the player instance on the spawn point with the name "enter"
            //spawn(e, "enter");
        });
    }

    public static String getRoom() {
        return Game.world().environment().getMap().getName();
    }

    public static GameType getCurrentGameType() {
        return currentGameType;
    }

    public static boolean spawn(Environment e, String spawnpointName) {
        //fade in regardless for easy debugging
        Game.window().getRenderComponent().fadeIn(500);

        Spawnpoint enter = e.getSpawnpoint(spawnpointName);
        if (enter != null) {
            enter.spawn(Player.instance());
            Game.loop().perform(500, () -> {
                //Player.instance().setVelocity(100);
                PlayerManager.unFreezePlayers();
            });
            return true;
        }
        return false;
    }
}
