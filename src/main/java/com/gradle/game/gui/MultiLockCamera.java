package com.gradle.game.gui;

import com.gradle.game.entities.Player;
import de.gurkenlabs.litiengine.graphics.Camera;
import java.awt.geom.Point2D;
import java.util.List;

public class MultiLockCamera extends Camera {

    private List<Player> entities;

    public MultiLockCamera(List<Player> entities) {
        super();
        this.entities = entities;
        this.updateFocus();
    }

    @Override
    public void updateFocus() {
        final Point2D cameraLocation = this.getLockedCameraLocation();

        this.setFocus(cameraLocation);
        super.updateFocus();
    }


    protected Point2D getLockedCameraLocation() {
        float x = 0;
        float y = 0;
        int size = entities.size();
        for (int counter = 0; counter < size; counter++) {
            Point2D center = entities.get(counter).getCenter();
            x += center.getX();
            y += center.getY();
        }
        x /= size;
        y /= size;
        return new Point2D.Double(x, y);
    }
}
