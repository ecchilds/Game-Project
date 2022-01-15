package com.gradle.game.entities.player;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.input.Gamepad;
import de.gurkenlabs.litiengine.input.GamepadEvents;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.physics.MovementController;
import de.gurkenlabs.litiengine.util.geom.GeometricUtilities;

import java.awt.geom.Point2D;

public class PlayerGamepadController extends MovementController<Player> {
    private final boolean rotateWithRightStick;
    private final double gamepadDeadzone = Game.config().input().getGamepadStickDeadzone();
    private final double gamepadRightStick = Game.config().input().getGamepadStickDeadzone();
    private int gamepadId;

    public static final int INPUT_COOLDOWN = 4;
    private int inputCooldownTimer = INPUT_COOLDOWN; // TODO: consider moving this to Game.loop.perform(delay) function on boolean variable

    private final GamepadEvents.GamepadReleasedListener buttonListener;
    private final GamepadEvents.GamepadPressedListener rStickXListener;
    private final GamepadEvents.GamepadPressedListener rStickYListener;

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

        // Button inputs here
        this.buttonListener = e -> {
            //System.out.println(e.getComponentId());
            switch (e.getComponentId()) {
                case "pov" -> { // dpad
                    float value = e.getValue();
                    if (value < 0.375f) { // up
                        player.loadCreaturesMenu();
                    }
//                    else if (value < 0.625f) { // right
//
//                    }
//                    else if (value < 0.875f) { // down
//
//                    }
//                    else { // left
//
//                    }
                }
                case "0" -> player.enterButton();
                case "7" -> player.loadPauseMenu(); // pause button
            }
        };

        // right stick listeners
        this.rStickXListener = e -> {
            if (inputCooldownTimer > INPUT_COOLDOWN) {
                if (e.getValue() < 0) {
                    player.windowLeft();
                } else {
                    player.windowRight();
                }
                inputCooldownTimer = 0;
            }
        };
        this.rStickYListener = e -> {
            if (inputCooldownTimer > INPUT_COOLDOWN) {
                if (e.getValue() < 0) {
                    player.windowUp();
                } else {
                    player.windowDown();
                }
                inputCooldownTimer = 0;
            }
        };

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

    public int getInputCooldownTimer() {
        return inputCooldownTimer;
    }

    @Override
    public void update() {
        inputCooldownTimer++;
        this.retrieveGamepadValues();
        super.update();
    }

    //majority of code taken from GamepadEntityController, adjusted for index errors
    private void retrieveGamepadValues() {
        final Gamepad gamepad = Input.gamepads().getById(this.gamepadId);
        if (this.gamepadId == -1 || gamepad == null) {
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

    public double getGamepadDeadzone() {
        return gamepadDeadzone;
    }

    public int getId() {
        return gamepadId;
    }

    @Override
    public void attach() {
        super.attach();
        if(gamepadId != -1) {
            Gamepad gamepad = Input.gamepads().getById(this.gamepadId);
            gamepad.onReleased(this.buttonListener);
            gamepad.onPressed(Gamepad.Xbox.RIGHT_STICK_X, rStickXListener);
            gamepad.onPressed(Gamepad.Xbox.RIGHT_STICK_Y, rStickYListener);
        } else
            System.out.println("WARNING: attached gamepad does not have listeners");
    }

    @Override
    public void detach() {
        super.detach();
        Gamepad gamepad = Input.gamepads().getById(this.gamepadId);
        if (gamepad == null) {
            System.err.println("ERROR: gamepad null on detachment.");
            System.out.println(this.gamepadId);
            Input.gamepads().getAll().forEach(gp -> System.out.println(gp.getId()));
        }
        gamepad.removeReleasedListener(this.buttonListener);
        gamepad.removePressedListener(Gamepad.Xbox.RIGHT_STICK_X, rStickXListener);
        gamepad.removePressedListener(Gamepad.Xbox.RIGHT_STICK_Y, rStickYListener);
    }
}
