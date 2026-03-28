package metalbuffet.content.blocks;

import metalbuffet.entities.bullet.*;
import metalbuffet.world.blocks.fun.*;
import mindustry.entities.bullet.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class MBFunBlocks {
    public static Block roryBlock, evilRoryBlock;

    public static void load() {
        BulletType roryBulletType = new TrackingSwordBulletType() {{
            damage = 999f;
            length = 9999f; // yea bro!
            lifetime = 120f;
            triggerTime = 60f;
            trackTime = 50f;
            flashTime = 5f;
            fadeInTime = 5f;

            trackLength = 60f;

            stepLength = 20f;
            stepCount = 100;
            stepLife = 3f;
            stepDelay = 0.3f;
        }};

        roryBlock = new RoryBlock("rory-block") {{
            requirements(Category.effect, BuildVisibility.sandboxOnly, ItemStack.with());
            size = 3;

            bulletType = roryBulletType;

            reload = 35f;
            range = 80f * 8f;
            consumePower(20f);
        }};

        evilRoryBlock = new RoryBlock("evil-rory-block") {{
            requirements(Category.effect, BuildVisibility.sandboxOnly, ItemStack.with());
            size = 3;

            bulletType = roryBulletType;

            reload = 35f;
            range = 80f * 8f;
            evil = true;
            consumePower(20f);
        }};
    }
}
