package com.gradle.game.gui.screens;


import com.gradle.game.entities.player.PlayerGamepadController;
import com.gradle.game.entities.player.PlayerKeyboardController;
import com.gradle.game.entities.player.PlayerManager;
import com.gradle.game.gui.FontTypes;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.GuiProperties;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.input.GamepadEvents;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.physics.MovementController;

import java.awt.*;
import java.util.ArrayList;

public class ControllerScreen extends MenuScreen {

    private int instances;
    private int currentPlayer = 0;
    private int currentGamepad = 1;
    private ArrayList<Integer> selectedGamepads = new ArrayList<>();
    private GuiComponent playerLabel;

    protected GamepadEvents.GamepadReleasedListener gamepadReleasedListener;

    public ControllerScreen() {
        super("MENU-CONTROLLERS");
        this.mouseEnabled = false;

        this.keyListener = event -> {

            // check if keyboard has been selected
            if (menu.getCellComponents().get(0).isEnabled()) {
                PlayerManager.get(currentPlayer).setController(
                        MovementController.class,
                        new PlayerKeyboardController(PlayerManager.get(currentPlayer))
                );
                PlayerManager.get(currentPlayer).setKeyboardControlled(true);
                menu.getCellComponents().get(0).setEnabled(false);
                menuOptionSelect();
            }
        };

        this.gamepadReleasedListener = event -> {
            int id = event.getGamepad().getId();

            // check if gamepad has already been selected
            if (!selectedGamepads.contains(id)) {
                selectedGamepads.add(id);
                PlayerManager.get(currentPlayer).setController(
                        MovementController.class,
                        new PlayerGamepadController(PlayerManager.get(currentPlayer), id)
                );
                PlayerManager.get(currentPlayer).setKeyboardControlled(false);

                menu.getCellComponents().get(currentGamepad).setEnabled(false);

                currentGamepad++;

                menuOptionSelect();
            }
        };

        //TODO: gamepad removed listener for safety
    }

    @Override
    public void prepare() {
        final double centerX = Game.window().getResolution().getWidth() / 2.0;
        final double centerY = Game.window().getResolution().getHeight() * 1 / 2;
        this.options = Input.gamepads().getAll().size() + 1;
        final double buttonWidth = 200;
        final double buttonHeight = FontTypes.MENU.getSize();

        instances = PlayerManager.getAll().size();
        currentPlayer = 0;
        currentGamepad = 1;
        selectedGamepads = new ArrayList<>();

        this.menuOptions = new String[options];
        for (int i = 1; i < options; i++) {
            menuOptions[i] = "Controller " + i;
        }
        menuOptions[0] = "Keyboard";

        this.playerLabel = new GuiComponent(centerX * 0.3, centerY - FontTypes.MENU.getSize()/2.0) {
            @Override
            protected void initializeComponents() {
                super.initializeComponents();
                this.setFont(FontTypes.MENU);
                this.getAppearance().setForeColor(new Color(255,255,255));
                this.setText("Player 1");
                this.setDimension(450, FontTypes.MENU.getSize());
                this.getAppearanceHovered().update(GuiProperties.getDefaultAppearance());
            }
        };

        this.menu = new Menu(centerX * 1.2, centerY - (buttonHeight * options / 2), buttonWidth, buttonHeight * options, menuOptions);
        this.menu.setForwardMouseEvents(false);

        this.getComponents().add(menu);
        this.getComponents().add(playerLabel);

        super.prepare();
    }

    @Override
    protected void initializeComponents() {
        // Parent's version is bad for this, while parent's parent's is empty.
        // Thus, leaving this empty causes no ill effects.
    }

    @Override
    protected void setListeners() {
        Game.loop().perform(100, () -> {
            Input.keyboard().onKeyReleased(keyListener);
            Input.gamepads().getAll().forEach(controller -> controller.onReleased(gamepadReleasedListener));
        });
    }

    @Override
    protected void removeListeners() {
        Input.keyboard().removeKeyReleasedListener(keyListener);
        Input.gamepads().getAll().forEach(controller -> controller.removeReleasedListener(gamepadReleasedListener));
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected String[] getMenuOptions() {
        return null;
    }

    @Override
    protected void menuOptionSelect() {

        // this is mostly just used to clean up after listener procs

        if (this.currentPlayer < this.instances-1 && this.currentPlayer < this.options-1) {
            //set up next screen
            this.currentPlayer++;
            this.playerLabel.setText("Player " + (currentPlayer +1));
        } else {
            this.menu.setEnabled(false);
            Game.screens().display("INGAME-SCREEN");

            this.removeListeners();

            this.getComponents().remove(menu);
            this.getComponents().remove(playerLabel);
        }
    }

    @Override
    protected void initMenu() {
        this.menu.getCellComponents().forEach(comp -> {
            comp.setFont(FontTypes.MENU);
            comp.getAppearance().setForeColor(new Color(255,255,255));
            comp.setForwardMouseEvents(false);
        });
    }
}
