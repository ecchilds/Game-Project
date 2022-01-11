package com.gradle.game;

import com.gradle.game.entities.*;
import com.gradle.game.entities.player.Player;
import com.gradle.game.entities.player.PlayerGamepadController;
import com.gradle.game.entities.player.PlayerManager;
import com.gradle.game.gui.FontTypes;
import com.gradle.game.gui.MultiLockCamera;
import com.gradle.game.gui.windows.WindowManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.GameListener;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.PropMapObjectLoader;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.GamepadEvent;
import de.gurkenlabs.litiengine.input.GamepadEvents;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.physics.IMovementController;

import java.awt.*;

public final class GameManager {
    private GameManager() {
    }

    private static GameType currentGameType = GameType.SINGLEPLAYER;

    //variables for adding new players via gamepad prompts
    private static GuiComponent prompt = null;
    private static GamepadEvents.GamepadReleasedListener start = null;

    public static void init() {

        // Initialize managers.
        WindowManager.init();
        PlayerManager.init();

        //set locked camera to player
        Camera camera = new MultiLockCamera(PlayerManager.getAll());
        camera.setClampToMap(true);
        Game.world().setCamera(camera);

        //allows doors to function, and know what to load
        //Environment.registerMapObjectLoader(new CustomPropMapObjectLoader());
        PropMapObjectLoader.registerCustomPropType(DoorWay.class);

        //set loader for background
        PropMapObjectLoader.registerCustomPropType(BackWall.class);

        Game.addGameListener(new GameListener() {
            @Override
            public void started() {
                Game.window().getRenderComponent().fadeIn(300);
            }
        });

        // add default game logic for when a level was loaded
        //Game.world().onLoaded(e -> {

            // spawn the player instance on the spawn point with the name "enter"
            //spawn(e, "enter");
        //});
    }

    public static void startGame() {
        Input.gamepads().onAdded(gamepad -> {
            if (currentGameType == GameType.SINGLEPLAYER) {
                PlayerGamepadController gamepadController = new PlayerGamepadController(
                        PlayerManager.getCurrent());
                PlayerManager.getCurrent().addController(gamepadController);

            } else { // co-op
                final double centerX = Game.window().getResolution().getWidth() / 2.0;
                final double bottom = Game.window().getResolution().getHeight();

                // add prompt for new player saying that they can join in.
                prompt = new GuiComponent(centerX-(450.0/2), bottom - 120) {
                    @Override
                    protected void initializeComponents() {
                        super.initializeComponents();
                        this.setFont(FontTypes.GEN);
                        this.getAppearance().setForeColor(new Color(255,255,255));
                        this.setTextShadow(true);
                        this.setTextShadowColor(new Color(0,0,0));
                        this.setDimension(450, FontTypes.GEN.getSize());
                        this.setText("Press start to join");
                        this.setVisible(true);
                    }
                };
                Game.screens().get("INGAME-SCREEN").getComponents().add(prompt);

                // add listener so new player can join in
                start = new GamepadEvents.GamepadReleasedListener() {
                    @Override
                    public void released(GamepadEvent event) {
                        Game.screens().get("INGAME-SCREEN").getComponents().remove(prompt);
                        PlayerManager.addPlayer("hoodie", event.getGamepad().getId());
                        gamepad.removeReleasedListener(Gamepad.Xbox.START, this);

                        prompt = null;
                        start = null;
                    }
                };
                gamepad.onReleased(Gamepad.Xbox.START, start);
            }
        });

        Input.gamepads().onRemoved(gamepad -> {
            if (currentGameType == GameType.COOP) {
                //TODO: menu for reselecting controller? disconnect player? anything but this!
                Player player = PlayerManager.getByGamepadId(gamepad.getId());
                if (player != null) {
                    player.removeController(IMovementController.class);
                } else if (prompt != null) {
                    Game.screens().get("INGAME-SCREEN").getComponents().remove(prompt);
                    gamepad.removeReleasedListener(Gamepad.Xbox.START, start);
                    prompt = null;
                    start = null;
                }

            } else { // single player
                PlayerManager.getCurrent().removeController(PlayerGamepadController.class);
            }
        });

        Game.window().getRenderComponent().fadeOut(300);
        Game.loop().perform(300, () -> {
            Game.screens().display("INGAME-SCREEN");
            PlayerManager.slowPlayers(2);
            spawn("mansion", "enter",1000);
        });
    }

    public static String getRoomName() {
        return Game.world().environment().getMap().getName();
    }

    public static GameType getCurrentGameType() {
        return currentGameType;
    }

    public static void setCurrentGameType(GameType newGameType) {
        currentGameType = newGameType;
    }

    //loads an environment, then spawns in a player
    public static void spawn(String mapName, String spawnpointName) {
        spawn(mapName, spawnpointName, 500);
    }
    public static void spawn(String mapName, String spawnpointName, int fade ) {
        //fade in regardless for easy debugging
        Environment e = Game.world().loadEnvironment(mapName);
        //e.addAmbientLight
        Game.window().getRenderComponent().fadeIn(fade);

        Spawnpoint enter = e.getSpawnpoint(spawnpointName);
        //System.out.println(enter.getSpawnPivotType());
        if (enter != null) {
            for (int i = 0; i < PlayerManager.size(); i++) {
                enter.setSpawnOffsetY(7f*(float)i*Math.pow(-1,i));
                enter.spawn(PlayerManager.get(i));
            }
            Game.loop().perform(fade, PlayerManager::unFreezePlayers);
        } else {
            System.err.println("ERROR: no such spawnpoint: " + spawnpointName);
        }
    }

    public static void spawnIn(Player player) {
        Game.world().environment().getSpawnpoint("enter").spawn(player);
    }
}
