package com.gradle.game.entities;
import com.gradle.game.gui.PauseScreen;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.*;
import de.gurkenlabs.litiengine.physics.IMovementController;

@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 100)
@CollisionInfo(collisionBoxWidth = 20, collisionBoxHeight = 6, collision = true, valign = Valign.DOWN)
public class Player extends Creature {
    private static Player instance;
    public int id;

    public static Player instance() throws Exception {
        throw new Exception("ERROR: deprecated function call");
        //if (instance == null) {
            //instance = new Player();
        //}

        //return instance;
    }

    protected Player() {
        this("hoodie");
    }

    protected Player(String spritesheetName) {
        super(spritesheetName);
        this.id = 0; // TODO: modify for multiple ids
        Game.screens().add(new PauseScreen(id));
    }

//    @Override
//    public String getSpritesheetName() {
//        return "hoodie";
//    }

    @Override
    protected IMovementController createMovementController() {
        // setup movement controller
        return new PlayerKeyboardController(this);
    }

    public void loadPauseMenu() {
        Game.screens().display("MENU-P"+id+"PAUSE");
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
