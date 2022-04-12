package com.gradle.game.entities.mobs;

import com.gradle.game.entities.player.Player;
import com.gradle.game.entities.player.PlayerManager;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.physics.IMovementController;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public abstract class Mob extends Creature implements IUpdateable {

    protected Polygon view;
    protected boolean alerted;


    protected Mob(String spritesheetName) {
        super(spritesheetName);
        this.view = createView();
        this.alerted = false;


        this.onCollision(e -> {
            if(e.getSource().getClass() == Player.class) {
                this.handleCollision((Player)e.getSource());
            }
//            Player player = null;
//            for (ICollisionEntity entity : e.getInvolvedEntities()) {
//                if (entity.getClass() == Player.class) {
//                    player = (Player) entity;
//                    System.out.println(player.getId());
//                    break;
//                }
//            }
//            if (player != null) {
//                this.handleCollision(player);
//            }
        });

        Game.loop().attach(this);
    }

    protected abstract void handleCollision(Player player);

    protected abstract Polygon createView();

    @Override
    public void update() { // mob AI goes here
        for (Player player : PlayerManager.getAll()) {
            if (view.intersects(player.getCollisionBox())) {
                this.alerted = true;
            }
        }
    }

    @Override
    public void setLocation(Point2D position) {
        double deltaX = position.getX() - this.getX();
        double deltaY = position.getY() - this.getY();

        this.view.translate((int)deltaX, (int)deltaY);

        super.setLocation(position);
    }

    @Override
    public void setFacingDirection(Direction facingDirection) {
        float rotation = facingDirection.toAngle() - this.getFacingDirection().toAngle();

        // prep for view rotation
        double cos = Math.cos(rotation);
        double sin = Math.sin(rotation);
        double x = this.getX();
        double y = this.getY();

        // rotate view
        for (int i = 0; i < view.npoints; i++) {
            double deltaX = view.xpoints[i] - x;
            double deltaY = view.ypoints[i] - y;

            view.xpoints[i] = (int)((deltaX*cos) - (deltaY*sin) + x + 0.5);
            view.ypoints[i] = (int)((deltaX*sin) + (deltaY*cos) + y + 0.5);
        }

        super.setFacingDirection(facingDirection);
    }

    @Override
    protected IMovementController createMovementController() {
        return new MobController(this);
    }
}
