package metalbuffet;

import metalbuffet.content.*;
import mindustry.mod.*;

public class MetalBuffet extends Mod {
    @Override
    public void loadContent() {
        MBBlocks.load();
        MBTechTree.load();
    }
}
