package com.gradle.game;

import com.gradle.game.entities.*;
//import com.gradle.game.entities.CustomPropMapObjectLoader;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.PropMapObjectLoader;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.PositionLockCamera;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.physics.IMovementController;

public final class GameManager {
    private GameManager() {
    }

    private static GameType currentGameType = GameType.SINGLEPLAYER;
    //private static int players = 1;

    public static void init() {

        PlayerManager.init();

        //set locked camera to player
        //TODO: modify camera positioning for multiplayer
        Camera camera = new PositionLockCamera(PlayerManager.getCurrent());
        camera.setClampToMap(true);
        Game.world().setCamera(camera);

        //allows doors to function, and know what to load
        //Environment.registerMapObjectLoader(new CustomPropMapObjectLoader());
        PropMapObjectLoader.registerCustomPropType(DoorWay.class);

        //set loader for background
        PropMapObjectLoader.registerCustomPropType(BackWall.class);

//        Input.gamepads().onAdded(e -> {
//            if (currentGameType == GameType.SINGLEPLAYER) {
//                //TODO: fix so that multiple gamepadControllers are not attached to same player.
//                // a menu to set current controller would be prudent. This should access the
//                // current gamepadcontroller attached to the player.
//                IMovementController gamepadController = new PlayerGamepadController(
//                        PlayerManager.getCurrent());
//                PlayerManager.getCurrent().addController(gamepadController);
//            } else if (currentGameType == GameType.COOP) {
//                //TODO: add multiplayer support
//            }
//        });

        // add default game logic for when a level was loaded
        Game.world().onLoaded(e -> {

            // spawn the player instance on the spawn point with the name "enter"
            //spawn(e, "enter");
        });
    }

    public static void loadLevel() {
        if (Game.screens().current().getName() == "MENU-MAIN") {
            Game.window().getRenderComponent().fadeOut(500);
            Game.loop().perform(500, () -> {
                Game.screens().display("INGAME-SCREEN");
                PlayerManager.slowPlayers(2);
                spawn("mansion", "enter",1000);
            });
        } else {
            spawn("mansion", "enter");
        }
    }

    public static String getRoom() {
        return Game.world().environment().getMap().getName();
    }

    public static GameType getCurrentGameType() {
        return currentGameType;
    }

    //loads an environment, then spawns in a player
    public static boolean spawn(String mapName, String spawnpointName) {
        return spawn(mapName, spawnpointName, 500);
    }
    public static boolean spawn(String mapName, String spawnpointName, int fade ) {
        //fade in regardless for easy debugging
        Environment e = Game.world().loadEnvironment(mapName);
        Game.window().getRenderComponent().fadeIn(fade);

        Spawnpoint enter = e.getSpawnpoint(spawnpointName);
        if (enter != null) {
            enter.spawn(PlayerManager.getCurrent());
            Game.loop().perform(fade, () -> {
                //Player.instance().setVelocity(100);
                PlayerManager.unFreezePlayers();
            });
            return true;
        }
        return false;
    }
}
