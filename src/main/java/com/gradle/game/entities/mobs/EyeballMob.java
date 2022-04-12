package com.gradle.game.entities.mobs;

import com.gradle.game.entities.player.Player;
import com.gradle.game.gui.windows.BattleWindow;
import com.gradle.game.gui.windows.Window;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.entities.CollisionInfo;
import de.gurkenlabs.litiengine.entities.EntityInfo;

import java.awt.*;

@EntityInfo(width = 32, height = 32)
//@MovementInfo(velocity = 100)
@CollisionInfo(collisionBoxWidth = 20, collisionBoxHeight = 6, collision = true, valign = Valign.DOWN)
public class EyeballMob extends Mob {

    public EyeballMob() {
        this("eyeball");
    }

    protected EyeballMob(String spritesheetName) {
        super(spritesheetName);
    }

    @Override
    protected void handleCollision(Player player) {

        // freeze all players and begin battle (freezing is now handled by the battle window)
        player.addTemporaryWindow(new BattleWindow("eye-battle", player.getId()));
    }

    @Override
    protected Polygon createView() {
        return new Polygon(new int[]{-1, 1, -2, -2}, new int[]{0, 0, 4, 4}, 4);
    }
}
