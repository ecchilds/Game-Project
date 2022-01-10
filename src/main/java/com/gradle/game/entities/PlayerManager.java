package com.gradle.game.entities;

import com.gradle.game.GameManager;
import com.gradle.game.GameType;
import de.gurkenlabs.litiengine.physics.IMovementController;

import java.util.ArrayList;
import java.util.List;

public final class PlayerManager {

    //configuration variables
    private static final float defaultVelocity = 100;

    private static final ArrayList<Player> players = new ArrayList<>();
    private static boolean initialized = false;

    public static void init() {
        GameType gameType = GameManager.getCurrentGameType();
        if (gameType == GameType.SINGLEPLAYER) {
            players.add(new Player("hoodie"));
        } else {
            players.add(new Player("hoodie"));
            addPlayer("hoodie", true);
        }

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

    public static void addPlayer(String spriteSheetName, boolean gamepad) {
        Player player = new Player(spriteSheetName);
        if (gamepad) {
            player.setController(IMovementController.class, new PlayerGamepadController(player));
        }
        players.add(player);
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
}
