package com.gradle.game.entities.player;
import com.gradle.game.SaveGame;
import com.gradle.game.gui.screens.PauseScreen;
import com.gradle.game.gui.windows.CreaturesWindow;
import com.gradle.game.gui.windows.Window;
import com.gradle.game.gui.windows.WindowManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.physics.IMovementController;

import java.util.ArrayDeque;


@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 100)
@CollisionInfo(collisionBoxWidth = 12, collisionBoxHeight = 6, collision = true, valign = Valign.DOWN)
public class Player extends Creature {
    private final int id;
    private final String characterName;
    private final SaveGame save;
    private boolean keyboard = true;

    private final ArrayDeque<Window> activeWindows; // NOTE: if this produce concurrency errors, replace with a ConcurrentLinkedDeque.
    private PlayerControllerManager controllers;

//    private IKeyboard.KeyTypedListener keyboardWindowListener;
//    private GamepadEvents.GamepadPressedListener gamepadWindowListener;

    @Deprecated
    public static Player instance() throws Exception {
        throw new Exception("ERROR: deprecated function call");
        //if (instance == null) {
            //instance = new Player();
        //}

        //return instance;
    }

    protected Player(String spritesheetName) {
        this(spritesheetName, 0, "bob");
    }
    protected Player(String spritesheetName, int id, String name) {
        super(spritesheetName);
        this.id = id;
        this.characterName = name;
        this.activeWindows = new ArrayDeque<>();
        this.save = SaveGame.loadSavedGameFile(characterName);
        Game.screens().add(new PauseScreen(id));

        CreaturesWindow creaturesWindow = new CreaturesWindow("P" + id + "-CREATURES", id);
        creaturesWindow.onSuspend(() -> {
            activeWindows.remove(creaturesWindow);
        });
        WindowManager.add(creaturesWindow);
    }

    @Override
    protected IMovementController createMovementController() {
        // setup movement controller
        keyboard = true;
        return new PlayerKeyboardController(this);
    }

    // ========================================================================================================================
    // Controller/window interaction functions

    public void loadPauseMenu() {
        if(Game.screens().current().getName().equals("INGAME-SCREEN")) {
            Game.screens().display("MENU-P" + id + "PAUSE");
            PlayerManager.freezePlayers();
        } else {
            Game.screens().display("INGAME-SCREEN");
        }
    }

    public void loadCreaturesMenu() {
        if(Game.screens().current().getName().equals("INGAME-SCREEN")) {
            Window window = WindowManager.get("P"+id+"-CREATURES");
            window.toggleSuspension();
            if (window.isSuspended()) {
                activeWindows.remove(window);
            } else {
                activeWindows.push(window);
            }
        }
    }

    //functions to be called by a window on preparation and suspension
    public void addActiveWindow(Window window) {
        activeWindows.add(window);
    }
    public void removeActiveWindow(Window window) {
        activeWindows.remove(window);
    }

    // adds a window that is destroyed upon closing
    public <T extends Window> void addTemporaryWindow(T window) {
        Game.screens().get("INGAME-SCREEN").getComponents().add(window);
        window.onSuspend(() -> {
            Game.screens().get("INGAME-SCREEN").getComponents().remove(window);
        });
        window.prepare();

        activeWindows.push(window);
    }

    public void enterButton() {
        if(!activeWindows.isEmpty())
            this.activeWindows.peek().enter();
    }

    // Window navigation functions
    public void windowUp() {
        if(!activeWindows.isEmpty())
            this.activeWindows.peek().up();
    }
    public void windowRight() {
        if(!activeWindows.isEmpty())
            this.activeWindows.peek().right();
    }
    public void windowDown() {
        if(!activeWindows.isEmpty())
            this.activeWindows.peek().down();
    }
    public void windowLeft() {
        if(!activeWindows.isEmpty())
            this.activeWindows.peek().left();
    }

    // ========================================================================================================================
    // Getter/setter functions

    public Gamepad getGamepad() {
        if (keyboard) {
            return null;
        }
        // id
        return Input.gamepads().getById(this.getController(PlayerGamepadController.class).getId());
    }

    public int getId() {
        return this.id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public SaveGame getSave() {
        return save;
    }

    public void saveGame() {
        this.save.saveGame(this.characterName);
    }

    public boolean isKeyboardControlled() {
        return keyboard;
    }

    public void setKeyboardControlled(boolean k) {
        this.keyboard = k;
    }

    // ========================================================================================================================
    // Overrides to make PlayerEntityControllers class work follow.

    private PlayerControllerManager controllers() {
        if(this.controllers == null) {
            this.controllers = new PlayerControllerManager();
        }
        return controllers;
    }

    @Override
    public <T extends IEntityController> void setController(Class<T> clss, T controller) {
        this.controllers().setController(clss, controller);
    }

    @Override
    public void attachControllers() {
        this.controllers().attachAll();
    }

    @Override
    public void detachControllers() {
        this.controllers().detachAll();
    }

    @Override
    public IEntityAnimationController<?> animations() {
        return this.controllers().getAnimationController();
    }

    @Override
    public void addController(IEntityController controller) {
        this.controllers().addController(controller);
    }

    public <T extends IEntityController> void removeController(Class<T> clss) {
        this.controllers().clearControllers(clss);
    }
    //@Override
    //protected EntityControllers getControllers() {
    //    return this.controllers;
    //}

    @Override
    protected void updateAnimationController() {
        IEntityAnimationController<?> controller = this.createAnimationController();
        this.controllers().addController(controller);
        if (Game.world().environment() != null && Game.world().environment().isLoaded()) {
            Game.loop().attach(controller);
        }
    }

    @Override
    public <T extends IEntityController> T getController(Class<T> clss) {
        return this.controllers().getController(clss);
    }

//    @Override
//    public void update() {
//
//    }

//    @Override
//    protected IEntityAnimationController<?> createAnimationController() {
//        IEntityAnimationController<?> controller = new CreatureAnimationController<>(this, true);
//        controller.add(new Animation("hoodie-walk-up", true, true));
//        controller.add(new Animation("player-walk-down", true, true));
//        controller.add(new Animation("player-idle-up", true, true));
//        controller.add(new Animation(Resources.images().get("player-idle-down.png"), true, false));
//
//        return controller;
//    }
}
