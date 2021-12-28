package com.gradle.game.gui;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.input.Input;


public class PauseScreen extends MenuScreen {

    public PauseScreen(int player) {
        super("MENU-P"+player+"PAUSE");
    }

    @Override
    protected String[] getMenuOptions() {
        return new String[]{"Configure Controllers", "Return"};
    }

    @Override
    protected void menuOptionSelect() {
        switch (this.menu.getCurrentSelection()) {
            case 0:
                ControllerScreen.setPreviousScreen(this.getName());
                Game.screens().display("MENU-CONTROLLERS");
                break;
            case 1:
                Game.screens().display("INGAME-SCREEN");
                break;
        }
        this.menu.setEnabled(false);
    }

}
