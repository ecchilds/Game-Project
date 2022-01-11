package com.gradle.game.entities.player;
import com.gradle.game.gui.screens.PauseScreen;
import com.gradle.game.gui.windows.CreaturesWindow;
import com.gradle.game.gui.windows.WindowManager;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.graphics.RenderType;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.physics.IMovementController;


@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 100)
@CollisionInfo(collisionBoxWidth = 20, collisionBoxHeight = 6, collision = true, valign = Valign.DOWN)
public class Player extends Creature {
    public int id;
    private boolean keyboard = true;

    private PlayerControllerManager controllers;

    @Deprecated
    public static Player instance() throws Exception {
        throw new Exception("ERROR: deprecated function call");
        //if (instance == null) {
            //instance = new Player();
        //}

        //return instance;
    }

//    protected Player() {
//        this("hoodie");
//    }

    protected Player(String spritesheetName) {
        this(spritesheetName, 0);
    }
    protected Player(String spritesheetName, int id) {
        super(spritesheetName);
        this.id = id;
        Game.screens().add(new PauseScreen(id));

        WindowManager.add(new CreaturesWindow("P"+id+"-CREATURES"));
    }

//    @Override
//    public String getSpritesheetName() {
//        return "hoodie";
//    }

    @Override
    protected IMovementController createMovementController() {
        // setup movement controller
        keyboard = true;
        return new PlayerKeyboardController(this);
    }

    public void loadPauseMenu() {
        if(Game.screens().current().getName().equals("INGAME-SCREEN")) {
            Game.screens().display("MENU-P" + id + "PAUSE");
            PlayerManager.freezePlayers();
        } else {
            Game.screens().display("INGAME-SCREEN");
            PlayerManager.unFreezePlayers();
        }
    }

    public void loadCreaturesMenu() {
        if(Game.screens().current().getName().equals("INGAME-SCREEN")) {
            WindowManager.toggleDisplay("P"+id+"-CREATURES");
        }
    }

    public Gamepad getGamepad() {
        if (keyboard) {
            return null;
        }
        // id
        return Input.gamepads().getById(this.getController(PlayerGamepadController.class).getId());
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
