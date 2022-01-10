package com.gradle.game.gui;

import com.gradle.game.entities.Player;
import com.gradle.game.entities.PlayerManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.ImageComponent;

import java.util.Objects;


public class PauseScreen extends MenuScreen {

    private final int playerId; //use to get player, to get gamepad, to add controller listeners
    //private GamepadEvents.GamepadPollListener gamepadListener;

    public PauseScreen(int player) {
        super("MENU-P"+player+"PAUSE");
        this.playerId = player;
    }

    @Override
    protected String[] getMenuOptions() {
        return new String[]{"Configure Controllers", "Return"};
    }

    @Override
    protected void setListeners() {
        Player player = Objects.requireNonNull(PlayerManager.get(playerId)); //TODO: more thorough checks for player logoff?
        if (player.isKeyboardControlled()) {
            super.setListeners();
        } else {
            PlayerManager.freezePlayers();
            this.gamepadListener = event -> {
                float poll = event.getValue();
                String button = event.getComponent();
                System.out.println(button); //TODO: figure out how to specify axis. see what its name is.
                //NOTE: using string literals instead of Gamepad.Xbox, or similar, because those don't seem to work.
                if (button.equals("Y Axis") && Math.abs(poll) > event.getGamepad().getAxisDeadzone()) {
                //if (Math.abs(poll) > event.getGamepad().getAxisDeadzone()) {
                    if (poll < 0) {
                        menu.setCurrentSelection(Math.max(0, menu.getCurrentSelection() - 1));
                    } else {
                        menu.setCurrentSelection(Math.min(options, menu.getCurrentSelection() + 1));
                    }
                    for (ImageComponent comp : menu.getCellComponents()) {
                        comp.setHovered(false);
                    }
                    menu.getCellComponents().get(menu.getCurrentSelection()).setHovered(true);
                }
                else if (button.equals("Button 0")) {
                    menuOptionSelect();
                    removeListeners();
                }
            };
            player.getGamepad().onPoll(gamepadListener);
        }
    }

    @Override
    protected void removeListeners() {
        Player player = Objects.requireNonNull(PlayerManager.get(playerId));
        if (player.isKeyboardControlled()) {
            super.removeListeners();
        } else {
            player.getGamepad().removePollListener(this.gamepadListener);
        }
    }

    @Override
    protected void menuOptionSelect() {
        switch (this.menu.getCurrentSelection()) {
            case 0 -> Game.screens().display("MENU-CONTROLLERS");
            case 1 -> {
                Game.screens().display("INGAME-SCREEN");
                PlayerManager.unFreezePlayers();
            }
        }
        this.menu.setEnabled(false);
    }
}
