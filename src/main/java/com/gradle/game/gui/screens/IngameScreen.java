package com.gradle.game.gui.screens;
import com.gradle.game.gui.windows.WindowManager;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

public class IngameScreen extends GameScreen {
    public static final String NAME = "INGAME-SCREEN";

    public IngameScreen() {
        super(NAME);
    }

    @Override
    public void prepare() {
        super.prepare();
        WindowManager.suspendAll();
    }
}
