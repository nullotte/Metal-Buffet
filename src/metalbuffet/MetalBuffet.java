package metalbuffet;

import metalbuffet.content.*;
import metalbuffet.graphics.*;
import mindustry.mod.*;
import mindustry.type.*;

public class MetalBuffet extends Mod {
    public MetalBuffet() {
    }

    @Override
    public void loadContent() {
        MBBlocks.load();
        MBPlanets.load();
        MBTechTree.load();
    }

    public static void testPlanetMapping(Planet planet, int divisions, boolean doSectors) {
        PlanetMapper.run(planet, divisions, doSectors);
    }
}
