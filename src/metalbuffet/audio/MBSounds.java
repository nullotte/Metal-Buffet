package metalbuffet.audio;

import arc.audio.*;
import mindustry.*;

public class MBSounds {
    public static Sound hyperlaserGunShoot, hyperlaserGunHit, hyperlaserGunReload;

    public static void load() {
        hyperlaserGunShoot = Vars.tree.loadSound("hyperlaser-gun-shoot");
        hyperlaserGunHit = Vars.tree.loadSound("hyperlaser-gun-hit");
        hyperlaserGunReload = Vars.tree.loadSound("hyperlaser-gun-reload");
    }
}
