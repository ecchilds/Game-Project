package com.gradle.game.entities;

import de.gurkenlabs.litiengine.input.KeyboardEntityController;

import java.awt.event.KeyEvent;

//TODO: replace this, or chage it to be more flexible
public class PlayerKeyboardController extends KeyboardEntityController<Player> {
    public PlayerKeyboardController(Player player) {
        super(player);
        this.addUpKey(KeyEvent.VK_UP);
        this.addDownKey(KeyEvent.VK_DOWN);
        this.addLeftKey(KeyEvent.VK_LEFT);
        this.addRightKey(KeyEvent.VK_RIGHT);
    }
}
