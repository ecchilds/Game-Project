package com.gradle.game.gui.windows;


import com.gradle.game.gui.FontTypes;
import de.gurkenlabs.litiengine.gui.Appearance;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.Menu;

import java.awt.*;

public class CreaturesWindow extends Window {

    private Menu menu;

    public CreaturesWindow(String name) {this(name, 100, 200, 150, 200);}
    public CreaturesWindow(String name, double x, double y) {super(name, x, y);}
    public CreaturesWindow(String name, double x, double y, double width, double height) {super(name, x, y, width, height);}

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

    @Override
    public void prepare() {
        super.prepare();

        menu.getCellComponents().forEach(comp -> {
            comp.setFont(FontTypes.GEN);
            comp.getAppearance().setForeColor(Color.WHITE);
        });
    }
}
