package com.gradle.game;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.gui.screens.Screen;

import java.awt.*;

public class MenuScreen extends Screen {
    private Menu menu;

    protected MenuScreen() {
        super("MENU");
    }

    // taken from
    // https://github.com/gurkenlabs/litiengine-ldjam46/blob/master/src/de/gurkenlabs/ldjam46/gui/MenuScreen.java
    @Override
    public void prepare() {
        super.prepare();
        //Game.loop().attach(this);
        Game.window().getRenderComponent().setBackground(Color.BLACK);

        // init menu buttons (i think)
        initMenuComponents();
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        final double centerX = Game.window().getResolution().getWidth() / 2.0;
        final double centerY = Game.window().getResolution().getHeight() * 1 / 2;
        final double buttonWidth = 450;

        this.menu = new Menu(centerX - buttonWidth / 2, centerY * 1.3, buttonWidth, centerY / 2, "Play", "Exit");

        // initialize keyboard inputs here. see
        // https://github.com/gurkenlabs/litiengine-ldjam46/blob/master/src/de/gurkenlabs/ldjam46/gui/MenuScreen.java
    }

    // override render function

    // put here so it can be easily overridden in child classes, for different looks
    public void initMenuComponents() {
        this.menu.getCellComponents().forEach(comp -> {
            // initialize individual components
        });
        this.menu.setEnabled(true);
        this.menu.getCellComponents().get(0).setHovered(true);
    }
}
