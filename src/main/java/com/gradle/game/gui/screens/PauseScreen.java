package com.gradle.game.gui.screens;

import com.gradle.game.entities.player.Player;
import com.gradle.game.entities.player.PlayerManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.Input;

import java.util.Objects;


public class PauseScreen extends MenuScreen {

    private final int playerId; //use to get player, to get gamepad, to add controller listeners
    //private GamepadEvents.GamepadPollListener gamepadListener;

    public PauseScreen(int player) {
        super("MENU-P"+player+"PAUSE");
        this.playerId = player;
    }

    @Override
    protected String getTitle() {
        return "Player "+(playerId+1)+" Pause Menu";
    }

    @Override
    protected String[] getMenuOptions() {
        return new String[]{"Save", "Configure Controllers", "Return", "Save and Exit"};
    }

    @Override
    protected void menuOptionSelect() {
        switch (this.menu.getCurrentSelection()) {
            case 0 -> {
                PlayerManager.get(playerId).saveGame();
                Game.screens().display("INGAME-SCREEN");
            }
            case 1 -> Game.screens().display("MENU-CONTROLLERS");
            case 2 -> {
                Game.screens().display("INGAME-SCREEN");
            }
            case 3 -> Game.exit();
        }
        this.menu.setEnabled(false);
    }

    @Override
    protected void setListeners() {
        Player player = Objects.requireNonNull(PlayerManager.get(playerId)); //TODO: more thorough checks for player logoff?
        if (player.isKeyboardControlled()) {
            Input.keyboard().onKeyReleased(this.keyListener);
        } else {
            Gamepad gamepad = player.getGamepad();
            gamepad.onReleased(gamepadButtonListener);
            gamepad.onPressed(Gamepad.Xbox.LEFT_STICK_Y, gamepadStickListener);
//            this.gamepadListener = event -> {
//                float poll = event.getValue();
//                String button = event.getComponentName();
//                //System.out.println(button);
//                // NOTE: using string literals instead of Gamepad.Xbox, or similar, because those don't seem to work.
//                //if (button.equals(Gamepad.Xbox.LEFT_STICK_Y) && Math.abs(poll) > event.getGamepad().getAxisDeadzone()) {
//                if (button.equals("Y Axis") && Math.abs(poll) > event.getGamepad().getAxisDeadzone()) {
//                    if (poll < 0) {
//                        menu.setCurrentSelection(Math.max(0, menu.getCurrentSelection() - 1));
//                    } else {
//                        menu.setCurrentSelection(Math.min(options, menu.getCurrentSelection() + 1));
//                    }
//                    for (ImageComponent comp : menu.getCellComponents()) {
//                        comp.setHovered(false);
//                    }
//                    menu.getCellComponents().get(menu.getCurrentSelection()).setHovered(true);
//                }
//                else if (button.equals("Button 0")) {
//                    menuOptionSelect();
//                    removeListeners();
//                }
//            };
        }
    }

    @Override
    protected void removeListeners() {
        Player player = Objects.requireNonNull(PlayerManager.get(playerId));
        if (player.isKeyboardControlled()) {
            Input.keyboard().removeKeyReleasedListener(keyListener);
        } else {
            Gamepad gamepad = player.getGamepad();
            gamepad.removeReleasedListener(gamepadButtonListener);
            gamepad.removePressedListener(Gamepad.Xbox.LEFT_STICK_Y, gamepadStickListener);
        }
    }
}
