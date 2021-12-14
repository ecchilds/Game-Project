package com.gradle.game.entities;

// Custom prop loader; helps initializes doors, can be set to help
// initialize other props as well. Currently, unused.

import de.gurkenlabs.litiengine.entities.Prop;
import de.gurkenlabs.litiengine.environment.PropMapObjectLoader;
import de.gurkenlabs.litiengine.environment.tilemap.IMapObject;

public class CustomPropMapObjectLoader extends PropMapObjectLoader {

    @Override
    protected Prop createNewProp(IMapObject mapObject, String spriteSheet) {
        Prop prop = super.createNewProp(mapObject, spriteSheet);

        if (prop instanceof DoorWay) {
            DoorWay door = (DoorWay) prop;
            //door.setMapToOpen(door.getProperties().getStringValue("destination")); //tag needs to be map name for connected env
        }

        return prop;
    }
}
