package com.gradle.game.entities.player;

import com.gradle.game.entities.player.Player;
import de.gurkenlabs.litiengine.input.IKeyboard;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.input.KeyboardEntityController;

import java.awt.event.KeyEvent;

public class PlayerKeyboardController extends KeyboardEntityController<Player> {

    private final IKeyboard.KeyTypedListener pauseMenuListener;
    private final IKeyboard.KeyReleasedListener keyListener;

    public PlayerKeyboardController(Player player) {
        super(player);
        this.addUpKey(KeyEvent.VK_UP);
        this.addDownKey(KeyEvent.VK_DOWN);
        this.addLeftKey(KeyEvent.VK_LEFT);
        this.addRightKey(KeyEvent.VK_RIGHT);

        //player specific non-movement inputs
        pauseMenuListener = e -> player.loadPauseMenu();
        keyListener = e -> {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_1 -> player.loadCreaturesMenu();
            }
        };
    }

    @Override
    public void attach() {
        super.attach();
        Input.keyboard().onKeyTyped(KeyEvent.VK_ESCAPE, pauseMenuListener);
        Input.keyboard().onKeyReleased(keyListener);
    }

    @Override
    public void detach() {
        super.detach();
        Input.keyboard().removeKeyTypedListener(KeyEvent.VK_ESCAPE, pauseMenuListener);
        Input.keyboard().removeKeyReleasedListener(keyListener);
    }
}
