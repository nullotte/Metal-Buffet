package metalbuffet.content.blocks;

import metalbuffet.world.blocks.power.*;
import mindustry.type.*;
import mindustry.world.*;

public class MBPower {
    public static Block crankGenerator;

    public static void load() {
        crankGenerator = new CrankGenerator("crank-generator") {{
            requirements(Category.power, ItemStack.with());
            size = 2;
            powerProduction = 5f;
        }};
    }
}
