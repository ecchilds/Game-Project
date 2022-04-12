package com.gradle.game.entities.player;

import com.gradle.game.GameManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.physics.IMovementController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerManager {

    //configuration variables
    private static final float defaultVelocity = 100;
    private static int currentPlayerNum;

    private static final Map<Integer, Player> players = new ConcurrentHashMap<>();
    private static boolean initialized = false;

    public static void init() {
        currentPlayerNum = 1;
        players.put(0, new Player("hoodie"));

        initialized = true;
    }

    public static Player getCurrent() {
        return get(0);
    }

    public static Player get(int playerNumber) {
        if(!initialized) {
            System.err.println("SEVERE ERROR: uninitialized PlayerManager accessed.");
            Game.exit();
        }

        return players.get(playerNumber);
    }

    public static List<Player> getAll() {
        return players.values().stream().toList();
    }

    public static Player getByGamepadId(int id) {
        for (Player player : players.values()) {
            if (!player.isKeyboardControlled() && player.getGamepad().getId() == id) {
                return player;
            }
        }
        System.err.println("ERROR: player with given gamepad id '"+id+"' does not exist.");
        return null;
    }

    public static void addPlayer(String spriteSheetName, String characterName, boolean gamepad) {
        Player player = new Player(spriteSheetName, currentPlayerNum, characterName);
        if (gamepad) {
            player.setController(IMovementController.class, new PlayerGamepadController(player));
            player.setKeyboardControlled(false);
        }
        players.put(currentPlayerNum, player);
        currentPlayerNum++;
        GameManager.spawnIn(player);
    }

    public static void addPlayer(String spriteSheetName, String characterName, int gamepadId) {
        Player player = new Player(spriteSheetName, currentPlayerNum, characterName);
        player.setController(IMovementController.class, new PlayerGamepadController(player, gamepadId));
        player.setKeyboardControlled(false);
        players.put(currentPlayerNum, player);
        currentPlayerNum++;
        GameManager.spawnIn(player);
    }

    public static void saveGames() {
        players.values().forEach(Player::saveGame);
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
        players.values().forEach(p -> p.setVelocity(i));
    }

    public static int size() {
        return players.size();
    }
}
