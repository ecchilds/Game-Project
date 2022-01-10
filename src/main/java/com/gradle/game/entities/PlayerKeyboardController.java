package com.gradle.game.entities;

import de.gurkenlabs.litiengine.input.IKeyboard;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.input.KeyboardEntityController;

import java.awt.event.KeyEvent;

public class PlayerKeyboardController extends KeyboardEntityController<Player> {

    private final IKeyboard.KeyTypedListener pauseMenuListener;

    public PlayerKeyboardController(Player player) {
        super(player);
        this.addUpKey(KeyEvent.VK_UP);
        this.addDownKey(KeyEvent.VK_DOWN);
        this.addLeftKey(KeyEvent.VK_LEFT);
        this.addRightKey(KeyEvent.VK_RIGHT);

        //player specific non-movement inputs
        pauseMenuListener = e -> player.loadPauseMenu();
    }

    @Override
    public void attach() {
        super.attach();
        Input.keyboard().onKeyTyped(KeyEvent.VK_ESCAPE, pauseMenuListener);
    }

    @Override
    public void detach() {
        super.detach();
        Input.keyboard().removeKeyTypedListener(KeyEvent.VK_ESCAPE, pauseMenuListener);
    }
}
