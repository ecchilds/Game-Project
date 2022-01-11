package com.gradle.game.entities.player;

import com.gradle.game.GameManager;
import com.gradle.game.GameType;
import de.gurkenlabs.litiengine.physics.IMovementController;

import java.util.ArrayList;
import java.util.List;

public final class PlayerManager {

    //configuration variables
    private static final float defaultVelocity = 100;
    private static int currentPlayerNum;

    private static final ArrayList<Player> players = new ArrayList<>();
    private static boolean initialized = false;

    public static void init() {
        GameType gameType = GameManager.getCurrentGameType();
        currentPlayerNum = 1;
        players.add(new Player("hoodie"));

        initialized = true;
    }

    public static Player getCurrent() {
        return get(0);
    }

    public static Player get(int playerNumber) {
        if(!initialized) {
            System.err.println("SEVERE ERROR: uninitialized PlayerManager accessed.");
            System.exit(1);
        }

        return players.get(playerNumber);
    }

    public static List<Player> getAll() {
        return players;
    }

    public static Player getByGamepadId(int id) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (!player.isKeyboardControlled() && player.getGamepad().getId() == id) {
                return player;
            }
        }
        System.err.println("ERROR: player with given gamepad id '"+id+"' does not exist.");
        return null;
    }

    public static void addPlayer(String spriteSheetName, boolean gamepad) {
        Player player = new Player(spriteSheetName, currentPlayerNum);
        currentPlayerNum++;
        if (gamepad) {
            player.setController(IMovementController.class, new PlayerGamepadController(player));
            player.setKeyboardControlled(false);
        }
        players.add(player);
        GameManager.spawnIn(player);
    }

    public static void addPlayer(String spriteSheetName, int gamepadId) {
        Player player = new Player(spriteSheetName, currentPlayerNum);
        currentPlayerNum++;
        player.setController(IMovementController.class, new PlayerGamepadController(player, gamepadId));
        player.setKeyboardControlled(false);
        players.add(player);
        GameManager.spawnIn(player);
    }

    public static void freezePlayers() {
        setPlayerSpeeds(0);
    }

    public static void unFreezePlayers() {
        setPlayerSpeeds(defaultVelocity);
    }

    public static void slowPlayers(int i) {
        setPlayerSpeeds(defaultVelocity/i);
    }

    public static void setPlayerSpeeds(float i) {
        players.forEach(p -> p.setVelocity(i));
    }

    public static int size() {
        return players.size();
    }
}
