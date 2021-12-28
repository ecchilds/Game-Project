package com.gradle.game.entities;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.IMobileEntity;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.physics.MovementController;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;

import java.awt.geom.Point2D;

public class PlayerGamepadController extends MovementController<Player> {
    private boolean rotateWithRightStick;
    private int gamepadId;
    private double gamepadDeadzone = Game.config().input().getGamepadStickDeadzone();
    private double gamepadRightStick = Game.config().input().getGamepadStickDeadzone();

    public PlayerGamepadController(Player player) {
        this(player, Input.gamepads().current().getId());
    }

    public PlayerGamepadController(Player player, int gamepadId) {
        this(player, gamepadId, false);
    }

    public PlayerGamepadController(Player player, int gamepadId, boolean rotateWithRightStick) {

        super(player);
        this.gamepadId = gamepadId;
        this.rotateWithRightStick = rotateWithRightStick;

        Gamepad gamepad = Input.gamepads().getById(gamepadId);
        gamepad.onPressed(Gamepad.Xbox.START, e -> {
            player.loadPauseMenu();
        });

        //TODO: add an "onadded" listener which prompts user.
        Input.gamepads().onRemoved(pad -> {
            if (this.gamepadId == pad.getId()) {
                this.gamepadId = -1;
//                final Gamepad newGamePad = Input.gamepads().current();
//                if (newGamePad != null) {
//                    this.gamepadId = newGamePad.getId();
//                }
            }
        });
    }

    @Override
    public void update() {
        this.retrieveGamepadValues();
        super.update();
    }

    //majority of code taken from GamepadEntityController, adjusted for index errors
    private void retrieveGamepadValues() {
        final Gamepad gamepad = Input.gamepads().getById(this.gamepadId);
        if (this.gamepadId == -1 || this.gamepadId != -1 && gamepad == null) {
                return;
        }

        final float x = gamepad.getPollData(Gamepad.Axis.X);
        final float y = gamepad.getPollData(Gamepad.Axis.Y);

        if (Math.abs(x) > gamepadDeadzone) {
            this.setDx(x);
        }
        if (Math.abs(y) > gamepadDeadzone) {
            this.setDy(y);
        }

        if (this.rotateWithRightStick) {
            final float rightX = gamepad.getPollData(Gamepad.Axis.RX);
            final float rightY = gamepad.getPollData(Gamepad.Axis.RY);
            float targetX = 0;
            float targetY = 0;
            if (Math.abs(rightX) > gamepadRightStick) {
                targetX = rightX;
            }
            if (Math.abs(rightY) > gamepadRightStick) {
                targetY = rightY;
            }
            if (targetX != 0 || targetY != 0) {
                final Point2D target = new Point2D.Double(
                        this.getEntity().getCenter().getX() + targetX,
                        this.getEntity().getCenter().getY() + targetY);
                final double angle = GeometricUtilities.calcRotationAngleInDegrees(this.getEntity().getCenter(), target);
                this.getEntity().setAngle((float) angle);
            }
        }
    }
}
