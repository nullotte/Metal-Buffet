package metalbuffet.content.blocks;

import arc.graphics.*;
import metalbuffet.audio.*;
import metalbuffet.entities.bullet.*;
import mindustry.content.*;
import mindustry.entities.effect.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.meta.*;

public class MBTurrets {
    public static Block hyperlaserGun;

    public static void load() {
        hyperlaserGun = new PowerTurret("hyperlaser-gun") {{
            requirements(Category.turret, BuildVisibility.sandboxOnly, ItemStack.with());
            size = 5;

            shootType = new HyperlaserGunBulletType(4f) {{
                lifetime = 120f;
                hitSound = MBSounds.hyperlaserGunHit;
                hitSoundPitchRange = 0f;
            }};

            range = 4f * 120f;
            reload = 180f;
            outlineColor = Color.clear;
            shootSound = MBSounds.hyperlaserGunShoot;
            soundPitchMin = soundPitchMax = 1f;
            shootEffect = new SoundEffect(MBSounds.hyperlaserGunReload, Fx.none) {{
                startDelay = 30f;
                minPitch = maxPitch = 1f;
            }};
        }};
    }
}
