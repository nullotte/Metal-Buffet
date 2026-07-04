package metalbuffet;

import arc.*;
import metalbuffet.audio.*;
import metalbuffet.content.*;
import metalbuffet.graphics.*;
import metalbuffet.graphics.g3d.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.type.*;

public class MetalBuffet extends Mod {
    public static MiniPlanetRenderer miniPlanetRenderer;

    public MetalBuffet() {
        Events.on(FileTreeInitEvent.class, e -> {
            MBSounds.load();
        });
    }

    @Override
    public void init() {
        miniPlanetRenderer = new MiniPlanetRenderer();
    }

    @Override
    public void loadContent() {
        MBUnitTypes.load();
        MBBlocks.load();
        MBPlanets.load();
        MBTechTree.load();
    }

    public static void testPlanetMapping(Planet planet, int divisions, boolean doSectors) {
        PlanetMapper.run(planet, divisions, doSectors);
    }
}
