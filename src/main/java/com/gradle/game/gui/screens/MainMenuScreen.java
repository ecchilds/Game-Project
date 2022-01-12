package com.gradle.game.gui.screens;

import com.gradle.game.GameManager;
import com.gradle.game.GameType;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.Input;

public class MainMenuScreen extends MenuScreen {

    public MainMenuScreen() {
        super("MENU-MAIN");

        gamepadButtonListener = event -> { // A button
            menuOptionSelectWrapper();
        };
    }

    protected String getTitle() {return "Gamer Zone";}

    protected String[] getMenuOptions() {
        return new String[]{"Start", "Co-op", "Exit"};
    }

    protected void menuOptionSelect() {
        switch (this.menu.getCurrentSelection()) {
            case 0 -> GameManager.startGame();
            case 1 -> {
                GameManager.setCurrentGameType(GameType.COOP);
                GameManager.startGame();
            }
            case 2 -> Game.exit();
        }
    }

    @Override
    protected void setListeners() {
        Input.keyboard().onKeyReleased(keyListener);
        Input.gamepads().getAll().forEach(gp -> {
            gp.onPressed(Gamepad.Xbox.LEFT_STICK_Y, gamepadStickListener);
            gp.onReleased(Gamepad.Xbox.A, gamepadButtonListener);
        });
    }

    @Override
    protected void removeListeners() {
        Input.keyboard().removeKeyReleasedListener(keyListener);
        Input.gamepads().getAll().forEach(gp -> {
            gp.removePressedListener(Gamepad.Xbox.LEFT_STICK_Y, gamepadStickListener);
            gp.removeReleasedListener(Gamepad.Xbox.A, gamepadButtonListener);
        });
    }
}
