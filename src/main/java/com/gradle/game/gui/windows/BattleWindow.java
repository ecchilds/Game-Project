package com.gradle.game.gui.windows;

import com.gradle.game.Sounds;
import com.gradle.game.entities.player.PlayerManager;
import com.gradle.game.gui.FontTypes;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.Appearance;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageComponentList;

import java.awt.*;
import java.util.List;

public class BattleWindow extends Window{

    private ImageComponentList menu; //TODO: learn to implement sliders from ListField
    private int selectedColumn = 0;
    //private int selectedRow;

    public BattleWindow(String name, int playerId) {
        super(name, playerId);
    }

    //TODO: add constructor that takes monsters, 2 player and 1-2 enemy monsters

    @Override
    protected GuiComponent buildBody(double x, double y, double width, double height) {
        return new GuiComponent(x, y, width, height) {
            @Override
            protected void initializeComponents() {
                super.initializeComponents();

                //init menu
                menu = new ImageComponentList(x, y+(height*0.9), width, height*0.1, 1, 3, null, null);
                Appearance appearance = menu.getAppearance();
                appearance.setBackgroundColor1(Color.BLACK);
                appearance.setTransparentBackground(false);
                menu.getAppearanceHovered().update(appearance);

                //attach menu
                this.getComponents().add(menu);
            }
        };
    }

    @Override
    protected String getTitle() {
        return "Battle!";
    }

    @Override
    public void up() {
//        if(selectedRow-1 >= 0) {
//            menu.getCellComponents().get(selectedColumn+selectedRow).setHovered(false);
//            selectedRow--;
//            menu.getCellComponents().get(selectedColumn+selectedRow).setHovered(true);
//        }
//        afterMove();
    }

    @Override
    public void right() {
        if(selectedColumn+1 < menu.getColumns()) {
            menu.getCellComponents().get(selectedColumn).setHovered(false);
            selectedColumn++;
            menu.getCellComponents().get(selectedColumn).setHovered(true);
        }
        afterMove();
    }

    @Override
    public void down() {
//        if(selectedRow+1 < menu.getRows()) {
//            menu.getCellComponents().get(selectedColumn+selectedRow).setHovered(false);
//            selectedRow++;
//            menu.getCellComponents().get(selectedColumn+selectedRow).setHovered(true);
//        }
//        afterMove();
    }

    @Override
    public void left() {
        if(selectedColumn-1 >= 0) {
            menu.getCellComponents().get(selectedColumn).setHovered(false);
            selectedColumn--;
            menu.getCellComponents().get(selectedColumn).setHovered(true);
        }
        afterMove();
    }

    @Override
    public void enter() {
        Game.audio().playSound(Sounds.MENU_SELECT);
    }

    private void afterMove() {;
        Game.audio().playSound(Sounds.MENU_HOVER);
    }

    @Override
    public void prepare() {
        super.prepare();

        List<ImageComponent> cells = menu.getCellComponents();

        cells.forEach(comp -> {
            Appearance appearance = comp.getAppearance();
            comp.setFont(FontTypes.GEN);
            appearance.setForeColor(Color.WHITE);
        });

        cells.get(0).setText("Command");
        cells.get(1).setText("Ability");
        cells.get(2).setText("Item");

        selectedColumn = 0;
        //selectedRow = 0;
        this.menu.getCellComponents().get(0).setHovered(true);

        PlayerManager.freezePlayers();
    }

    @Override
    protected void onXPress() {
        PlayerManager.unFreezePlayers();
        super.onXPress();
    }
}
