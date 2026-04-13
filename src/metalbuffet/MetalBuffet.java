package metalbuffet;

import metalbuffet.content.*;
import mindustry.mod.*;

public class MetalBuffet extends Mod {
    public MetalBuffet() {
    }

    @Override
    public void loadContent() {
        MBBlocks.load();
        MBPlanets.load();
        MBTechTree.load();
    }
}
