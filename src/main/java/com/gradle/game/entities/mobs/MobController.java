package com.gradle.game.entities.mobs;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.physics.MovementController;

import java.util.Random;

public class MobController extends MovementController<Mob> {

    // movement variables
    private static final int MOVEMENT_COOLDOWN = 20;
    protected Random directionGenerator;
    protected int sinceLastMove = 0;
    protected Direction moving = Direction.DOWN;

    public MobController(Mob mobileEntity) {
        super(mobileEntity);
        this.directionGenerator = new Random();
    }

    @Override
    public void update() {
        sinceLastMove++;

        super.update();

        if(sinceLastMove < MOVEMENT_COOLDOWN) {
            setDx((float) Math.cos(moving.toAngle()));
            setDy((float) Math.sin(moving.toAngle()));
        }
        else if(directionGenerator.nextInt(50) == 1) {
            sinceLastMove = 0;


            moving = Direction.fromAngle(directionGenerator.nextDouble(360.0F));
            setDx((float) Math.cos(moving.toAngle()));
            setDy((float) Math.sin(moving.toAngle()));
        }
    }
}
