package com.gradle.game.entities;

import com.gradle.game.GameManager;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
//import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.AnimationInfo;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.Prop;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.EntityAnimationController;
import de.gurkenlabs.litiengine.graphics.animation.IEntityAnimationController;

@CollisionInfo(collision = true)
@AnimationInfo(spritePrefix = "prop-doorway") //TODO: check if this works
public class DoorWay extends Prop {

    //private String mapToOpen;
    //private Direction facing;

    //up, down, or left. probably
    public DoorWay(String spritesheetName) {
        super(spritesheetName);

//        this.facing = this.getProperties().getEnumValue("direction", Direction.class);
//        if (facing == null) {
//            this.facing = Direction.DOWN;
//        }

        // set event that occurs on player collision
        // TODO: specify collision with player only, not any entity, then
        this.onCollision(e -> {
            //if (e.getInvolvedEntities().contains(Player.class)) {
            //Player.instance().setVelocity(0);
            PlayerManager.freezePlayers();
            Game.window().getRenderComponent().fadeOut(1000);

            Game.loop().perform(1500, () -> {
                // remove player before unloading the environment or the instance's animation controller will be disposed
                Game.world().environment().removeAll(PlayerManager.getAll());
                //Game.world().environment().remove(Player.instance());

                //Load an environment, then spawn the player
                String room = GameManager.getRoom();
                Environment env = Game.world().loadEnvironment(getMapToOpen());
                GameManager.spawn(env, room+"-door");
            });
            //}
        });
    }

    @Override
    protected IEntityAnimationController<?> createAnimationController() {
        IEntityAnimationController<?> controller = new EntityAnimationController<>(this);

//        controller.add(new Animation("prop-doorway-left", true, false));
        controller.add(new Animation("prop-doorwayright", true, true));
        controller.add(new Animation("prop-doorway", true, true));
//        controller.add(new Animation("prop-doorway-up", true, false));

        //controller.addRule(x -> this.facing == Direction.LEFT, x -> "prop-doorway-left");
        controller.addRule(x -> this.getFacing() == Direction.DOWN, x -> "prop-doorway");
        controller.addRule(x -> this.getFacing() == Direction.RIGHT, x -> "prop-doorwayright");
        //controller.addRule(x -> this.facing == Direction.UP, x -> "prop-doorway-up");

        return controller;
    }

    private String getMapToOpen() {
        return this.getProperties().getStringValue("destination");
    }

    private Direction getFacing() {
        return this.getProperties().getEnumValue("direction", Direction.class);
    }

    // called by CustomPropObjectLoader.
//    public void setFacing(Direction direction) {
//        this.facing = direction;
//    }
}