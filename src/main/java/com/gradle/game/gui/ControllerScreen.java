package com.gradle.game.gui;


import com.gradle.game.entities.PlayerGamepadController;
import com.gradle.game.entities.PlayerKeyboardController;
import com.gradle.game.entities.PlayerManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.GuiProperties;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.input.KeyboardEntityController;
import de.gurkenlabs.litiengine.physics.MovementController;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.*;

public class ControllerScreen extends MenuScreen {

    private int instances;
    private int current = 1;
    private GuiComponent playerLabel;
    private static String previousScreenName;

    public ControllerScreen() {
        super("MENU-CONTROLLERS");
    }

    @Override
    public void prepare() {
        final double centerX = Game.window().getResolution().getWidth() / 2.0;
        final double centerY = Game.window().getResolution().getHeight() * 1 / 2;
        int options = Input.gamepads().getAll().size() + 1;
        final double buttonWidth = 200;
        final double buttonHeight = FontTypes.MENU.getSize();
        instances = PlayerManager.getAll().size();

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

        getComponents().add(menu);
        getComponents().add(playerLabel);

        super.prepare();
    }

    @Override
    protected void initializeComponents() {
        // parent's init is bad for this, while parent's parent's is empty.
        //super.initializeComponents();
    }

    @Override
    protected void menuOptionSelect() {

        //controller fuckery
        int selection = this.menu.getCurrentSelection();
        if(selection == 0) {
            PlayerManager.get(current-1).setController(
                    KeyboardEntityController.class,
                    new PlayerKeyboardController(PlayerManager.get(current-1))
            );
            //this.menu.getCellComponents().remove();
            //TODO: remove old options
        } else {
            PlayerManager.get(current-1).setController(
                    MovementController.class,
                    new PlayerGamepadController(PlayerManager.get(current-1), Input.gamepads().get(selection-1).getId())
            );
        }

        if (this.current < this.instances) {
            //TODO: controller fuckery
            // PlayerManager.get(current-1).setController(<???>);


            //set up next screen
            this.current++;
            this.playerLabel.setText("Player " + current);
        } else {
            this.menu.setEnabled(false);
            Game.screens().display(this.getPreviousScreen());

            this.getComponents().remove(menu);
            this.getComponents().remove(playerLabel);
        }
    }

    @Override
    protected void initMenu() {
        this.menu.getCellComponents().forEach(comp -> {
            comp.setFont(FontTypes.MENU);
            comp.getAppearance().setForeColor(new Color(255,255,255));
            comp.onClicked(e -> {
                menuOptionSelect();
            });
        });
    }

    public String getPreviousScreen() {
        return previousScreenName;
    }

    public static void setPreviousScreen(String screenName) {
        previousScreenName = screenName;
    }
}
