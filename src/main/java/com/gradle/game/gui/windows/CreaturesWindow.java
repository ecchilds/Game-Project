package com.gradle.game.gui.windows;


import com.gradle.game.Sounds;
import com.gradle.game.gui.FontTypes;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.Appearance;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.Menu;

import java.awt.*;

public class CreaturesWindow extends Window {

    private static final int OPTIONS = 4;

    private Menu menu;

    public CreaturesWindow(String name, int playerId) {this(name, playerId, 100, 200, 150, 200);}
    public CreaturesWindow(String name, int playerId, double x, double y) {super(name, playerId, x, y);}
    public CreaturesWindow(String name, int playerId, double x, double y, double width, double height) {super(name, playerId, x, y, width, height);}

    @Override
    protected GuiComponent buildBody(double x, double y, double width, double height) {
        return new GuiComponent(x, y, width, height) {
            @Override
            protected void initializeComponents() {
                super.initializeComponents();

                Appearance appearance = this.getAppearance();
                appearance.setBackgroundColor1(Color.BLACK);
                appearance.setTransparentBackground(false);
                this.getAppearanceHovered().update(appearance);

                menu = new Menu(this.getX(), this.getY(), this.getWidth(), this.getHeight(), "None", "None", "None", "None");

                this.getComponents().add(menu);
            }
        };
    }

    @Override
    protected String getTitle() {
        return "Creatures";
    }

    //input functions
    @Override
    public void up() {
        this.menu.setCurrentSelection(Math.max(0, this.menu.getCurrentSelection() - 1));
        afterMove();
    }
    @Override
    public void right() {
    }
    @Override
    public void down() {
        this.menu.setCurrentSelection(Math.min(this.OPTIONS, this.menu.getCurrentSelection() + 1));
        afterMove();
    }
    @Override
    public void left() {
    }
    @Override
    public void enter() {
        Game.audio().playSound(Sounds.MENU_SELECT);
    }

    private void afterMove() {
        for (ImageComponent comp : this.menu.getCellComponents()) {
            comp.setHovered(false);
        }
        this.menu.getCellComponents().get(this.menu.getCurrentSelection()).setHovered(true);
        Game.audio().playSound(Sounds.MENU_HOVER);
    }

    @Override
    public void prepare() {
        super.prepare();

        menu.getCellComponents().forEach(comp -> {
            comp.setFont(FontTypes.GEN);
            comp.getAppearance().setForeColor(Color.WHITE);
        });

        this.menu.getCellComponents().get(0).setSelected(true);
        this.menu.getCellComponents().get(0).setHovered(true);
    }
}
