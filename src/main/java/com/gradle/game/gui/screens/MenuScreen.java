package com.gradle.game.gui.screens;

import com.gradle.game.Sounds;
import com.gradle.game.gui.FontTypes;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.GuiProperties;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.GamepadEvents;
import de.gurkenlabs.litiengine.input.IKeyboard;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class MenuScreen extends Screen implements IUpdateable {
    protected Menu menu;
    protected String[] menuOptions = {"Play", "Exit"};
    protected int options;
    protected boolean mouseEnabled = true;

    protected static final int INPUT_COOLDOWN = 11;
    protected int inputCooldownTimer = 0;

    protected IKeyboard.KeyReleasedListener keyListener;
    protected GamepadEvents.GamepadReleasedListener gamepadButtonListener;
    protected GamepadEvents.GamepadPressedListener gamepadStickListener;

    protected MenuScreen(String screenName) {
        super(screenName);

        //used for keyboard input. added to Input by prepare.
        keyListener = event -> {
            if (isSuspended()) {
                return;
            }

            initMenuKeyboardNav(event.getKeyCode());

            if ((event.getKeyCode() == KeyEvent.VK_ENTER || event.getKeyCode() == KeyEvent.VK_SPACE) && menu.isEnabled()) {
                //Game.audio().playSound("confirm.wav");
                menuOptionSelectWrapper();
            }
        };

        gamepadStickListener = event -> { // Left stick Y
            if (inputCooldownTimer > INPUT_COOLDOWN) {
                if (event.getValue() < 0) {
                    menu.setCurrentSelection(Math.max(0, menu.getCurrentSelection() - 1));
                } else {
                    menu.setCurrentSelection(Math.min(options, menu.getCurrentSelection() + 1));
                }
                for (ImageComponent comp : menu.getCellComponents()) {
                    comp.setHovered(false);
                }
                menu.getCellComponents().get(menu.getCurrentSelection()).setHovered(true);
                inputCooldownTimer = 0;
            }
        };
        gamepadButtonListener = event -> {
            switch (event.getComponentId()) {
                case "0" -> menuOptionSelectWrapper();
                case "1" -> Game.screens().display("INGAME-SCREEN");
            }
        };

        Game.loop().attach(this);
    }

    // override to change title
    protected abstract String getTitle();

    // override to change menu items
    protected abstract String[] getMenuOptions();

    //override for inputs
    protected abstract void menuOptionSelect();

    // lots of code taken from
    // https://github.com/gurkenlabs/litiengine-ldjam46/blob/master/src/de/gurkenlabs/ldjam46/gui/MenuScreen.java

    // runs when menu is opened
    @Override
    public void prepare() {
        this.menu.setEnabled(true);
        super.prepare();
        //Game.loop().attach(this);
        Game.window().getRenderComponent().setBackground(Color.BLACK);

        initMenu();
        this.menu.getCellComponents().get(0).setSelected(true);
        this.menu.getCellComponents().get(0).setHovered(true);

        //menu control. must be initialized here so that it does not mess with player navigation.
        Game.loop().perform(1, this::setListeners);
    }

    // runs before constructor. Why? I don't know.
    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        final double centerX = Game.window().getResolution().getWidth() / 2.0;
        final double centerY = Game.window().getResolution().getHeight() * 1 / 2;
        final double buttonWidth = 450;

        this.options = getMenuOptions().length-1;
        this.menu = new Menu(centerX - buttonWidth / 2, centerY, buttonWidth, centerY / 4 * (options+1), getMenuOptions());

        GuiComponent title = new GuiComponent(centerX - 450/2.0, centerY/2) {
            @Override
            protected void initializeComponents() {
                super.initializeComponents();
                this.setFont(FontTypes.TITLE);
                this.getAppearance().setForeColor(new Color(255,255,255));
                this.setText(getTitle());
                this.setDimension(450, FontTypes.TITLE.getSize());
                this.getAppearanceHovered().update(GuiProperties.getDefaultAppearance());
            }
        };

        // https://github.com/gurkenlabs/litiengine-ldjam46/blob/master/src/de/gurkenlabs/ldjam46/gui/MenuScreen.java

        this.getComponents().add(title);
        this.getComponents().add(this.menu);
    }

    protected void setListeners() {
        //must be delayed one tick so that it doesn't run immediately, should the screen switch be
        //activated by another listener.
        Input.keyboard().onKeyReleased(keyListener);
        Input.gamepads().getAll().forEach(gp -> {
            gp.onPressed(Gamepad.Xbox.LEFT_STICK_Y, gamepadStickListener);
            gp.onReleased(gamepadButtonListener);
        });
    }

    protected void removeListeners() {
        Input.keyboard().removeKeyReleasedListener(keyListener);
        Input.gamepads().getAll().forEach(gp -> {
            gp.removePressedListener(Gamepad.Xbox.LEFT_STICK_Y, gamepadStickListener);
            gp.removeReleasedListener(gamepadButtonListener);
        });
    }

    // override to change appearances
    protected void initMenu() {
        this.menu.getCellComponents().forEach(comp -> {
            // initialize individual components
            comp.setFont(FontTypes.MENU);
            comp.getAppearance().setForeColor(new Color(255,255,255));
            //comp.setWidth(comp.getText().length()*33);
            comp.setHeight(FontTypes.MENU.getSize());
            comp.getAppearance().setBorderColor(new Color(255,255,255));
            comp.getAppearance().setBorderRadius(2);
            if (mouseEnabled) {
                comp.onClicked(e -> menuOptionSelectWrapper());
            }
            comp.onHovered(e -> Game.audio().playSound(Sounds.MENU_HOVER));
        });
    }

    // NEVER call menuOptionSelect raw, always use this. (Except for controllerscreen, that's a special case).
    protected void menuOptionSelectWrapper() {
        menuOptionSelect();
        this.menu.setEnabled(false); //disables all buttons, so they can't be entered multiple times
        Game.loop().perform(1, this::removeListeners);
        Game.audio().playSound(Sounds.MENU_SELECT);
    }

    // don't override. this makes life easy
    private void initMenuKeyboardNav(int keycode) {
        if (keycode == KeyEvent.VK_UP || keycode == KeyEvent.VK_W || keycode == KeyEvent.VK_DOWN || keycode == KeyEvent.VK_S) {
            if (keycode == KeyEvent.VK_DOWN || keycode == KeyEvent.VK_S) {
                this.menu.setCurrentSelection(Math.min(this.options, this.menu.getCurrentSelection() + 1));
            } else {
                this.menu.setCurrentSelection(Math.max(0, this.menu.getCurrentSelection() - 1));
            }
            //TODO: implement custom menu to handle select/hover as one?
            // can be done in menu's "setCurrentSelection" function, which
            // already has a loop to deselect.
            for (ImageComponent comp : this.menu.getCellComponents()) {
                comp.setHovered(false);
            }
            this.menu.getCellComponents().get(this.menu.getCurrentSelection()).setHovered(true);
            Game.audio().playSound(Sounds.MENU_HOVER);
        }
    }

    @Override
    public void suspend() {
        super.suspend();
        this.removeListeners();
    }

    @Override
    public void update() {
        this.inputCooldownTimer++;
    }
}
