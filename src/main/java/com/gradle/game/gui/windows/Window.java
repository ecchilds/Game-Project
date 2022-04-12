package com.gradle.game.gui.windows;

import com.gradle.game.entities.player.PlayerManager;
import com.gradle.game.gui.FontTypes;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.Appearance;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.tweening.TweenType;

import java.util.List;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Window extends GuiComponent {

    // Parameter variables
    protected static final Font FONT = FontTypes.GEN;
    protected static final int HEADER_OFFSET = 5;
    protected static final int HEADER_HEIGHT = FONT.getSize()+(HEADER_OFFSET*2);
    protected static final int DEFAULT_WIDTH = 350;
    protected static final int DEFAULT_HEIGHT = 350;

    // Component variables
    protected GuiComponent header;
    protected GuiComponent body;
    protected GuiComponent title;
    protected GuiComponent xButton;

    // Event Listeners
    private List<suspensionListener> suspensionListeners = new CopyOnWriteArrayList<>();

    private int playerId;

    private double headerPercentageX;
    private double headerPercentageY;

    public Window(String name, int playerId) {
        this(name, playerId, Game.window().getWidth()*0.2, Game.window().getHeight()*0.2);
    }

    public Window(String name, int playerId, double x, double y) {
        this(x, y);
        this.playerId = playerId;
        this.setName(name);
    }

    public Window(String name, int playerId, double x, double y, double width, double height) {
        this(x, y, width, height);
        this.playerId = playerId;
        this.setName(name);
    }

    protected Window(double x, double y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    protected Window(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();

        // Bar containing window name and "X" button.
        header = new GuiComponent(this.getX(), this.getY(), this.getWidth(), HEADER_HEIGHT) {

            @Override
            protected void initializeComponents() {
                super.initializeComponents();

                // Appearance of bar.
                Appearance appearance = this.getAppearance();
                appearance.setBackgroundColor1(new Color(80,80,80));
                appearance.setTransparentBackground(false);
                this.getAppearanceHovered().update(appearance);

                this.setFont(FONT);
                //TODO: fill with ":" symbols.

                Double titleWidth = FONT.getStringBounds(getTitle(), new FontRenderContext(new AffineTransform(), false, true)).getWidth();
                title = new GuiComponent(this.getX(), this.getY(), titleWidth+(HEADER_OFFSET*2), HEADER_HEIGHT) {
                    @Override
                    protected void initializeComponents() {
                        super.initializeComponents();

                        this.setFont(FONT);
                        this.setText(getTitle());
                        this.getAppearance().setForeColor(Color.BLACK);
                        this.getAppearanceHovered().setForeColor(Color.BLACK);
                    }
                };

                Double end = this.getX() + this.getWidth();
                Double xWidth = FONT.getStringBounds("X", new FontRenderContext(new AffineTransform(), false, true)).getWidth();
                xButton = new GuiComponent(end-(xWidth+(HEADER_OFFSET*2)), this.getY(), xWidth+(HEADER_OFFSET*2), HEADER_HEIGHT) {
                    @Override
                    protected void initializeComponents() {
                        super.initializeComponents();

                        this.setFont(FONT);
                        this.setText("X");
                        this.getAppearance().setForeColor(Color.BLACK);
                    }
                };

                this.getComponents().add(0, title);
                this.getComponents().add(1, xButton);
            }
        };

        // Ensure that menu closes when "x" button is clicked.
        xButton.onClicked(e -> {
            onXPress();
        });

        // Allow window to be moved.
        header.onMousePressed(e -> {
            MouseEvent event = e.getEvent();
            this.headerPercentageX = (event.getX()-this.getX())/this.getWidth();
            this.headerPercentageY = (event.getY()-this.getY())/HEADER_HEIGHT;
        });
        header.onMouseDragged(e -> {
            MouseEvent event = e.getEvent();
            this.setTweenValues(TweenType.POSITION_XY, new float[]{
                    (float)(event.getX()-this.getWidth()*headerPercentageX),
                    (float)(event.getY()-HEADER_HEIGHT*headerPercentageY)});
        });

        // Custom appearance.
        body = buildBody(this.getX(), this.getY()+HEADER_HEIGHT, getWidth(), getHeight()-HEADER_HEIGHT);

        // Window Border.
        this.getAppearance().setBorderStyle(new BasicStroke(6));
        this.getAppearance().setBorderColor(Color.BLACK);
        this.getAppearanceHovered().setBorderStyle(new BasicStroke(6));
        this.getAppearanceHovered().setBorderColor(Color.BLACK);

        this.getComponents().add(header);
        this.getComponents().add(body);
    }

    protected abstract GuiComponent buildBody(double x, double y, double width, double height);

    protected abstract String getTitle();

    // controller inputs
    public abstract void up();
    public abstract void right();
    public abstract void down();
    public abstract void left();
    public abstract void enter();

    // runs after suspension
    protected void onXPress() {
        this.suspend();
    }

    @Override
    public void prepare() {
        super.prepare();

        PlayerManager.get(playerId).addActiveWindow(this);
    }

    @Override
    public void suspend() {
        super.suspend();

        PlayerManager.get(playerId).removeActiveWindow(this);

        for(suspensionListener i : suspensionListeners) {
            i.run();
        }
    }

    public void onSuspend(suspensionListener newListener) {
        this.suspensionListeners.add(newListener);
    }

    @FunctionalInterface
    public interface suspensionListener {
        void run();
    }

    // bug fix stuff
    @Override
    public void setX(final double x) {
        setLocation(x, this.getY());
    }
    @Override
    public void setY(final double y) {
        setLocation(this.getX(), y);
    }
}
