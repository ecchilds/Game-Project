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

                // window navigation
                // TODO: raise issue over vk_kp_* codes not working.
                case KeyEvent.VK_NUMPAD8 -> player.windowUp(); // numpad up
                case KeyEvent.VK_NUMPAD6 -> player.windowRight(); // numpad right
                case KeyEvent.VK_NUMPAD2 -> player.windowDown(); // numpad Down
                case KeyEvent.VK_NUMPAD4 -> player.windowLeft(); // numpad left
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
