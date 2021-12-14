package com.gradle.game.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.AnimationInfo;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.entities.Prop;
import de.gurkenlabs.litiengine.graphics.ICamera;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

@CollisionInfo(collision = false)
@AnimationInfo(spritePrefix = "prop-nightsky")
public class BackWall extends Prop {

    //position of upper-left corner with regards to player
    private static final double offsetX = 0.0;
    private static final double offsetY = 0.0;
    private static final ICamera referenceViewport = Game.world().camera();

//    public BackWall(IEntity e) {
//        super("prop-nightsky");
//        this.referenceEntity = e;
//    }

    public BackWall(String spritesheetName) {
        super(spritesheetName);
        //this.referenceEntity = Player.instance();
    }

    public ICamera getReferenceViewport() {
        return referenceViewport;
    }

    @Override
    public Point2D getLocation() {
        return getReferenceViewport().getMapLocation(new Double(offsetX, offsetY));
    }
}
