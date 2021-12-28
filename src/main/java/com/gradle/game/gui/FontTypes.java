package com.gradle.game.gui;

import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.Font;

public class FontTypes {
    public static final Font MENU = Resources.fonts().get("Teletactile-3zavL.ttf").deriveFont(24f);
    public static final Font GEN = MENU.deriveFont(12f);
    public static final Font TITLE = Resources.fonts().get("AAbsoluteEmpire-EaXpg.ttf").deriveFont(64f);
}
