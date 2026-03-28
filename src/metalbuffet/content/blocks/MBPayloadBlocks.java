package metalbuffet.content.blocks;

import metalbuffet.world.blocks.payloads.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class MBPayloadBlocks {
    public static Block payloadPackage, payloadPackager, payloadUnpackager;

    public static void load() {
        payloadPackage = new PayloadPackage("payload-package") {{
            requirements(Category.units, BuildVisibility.hidden, ItemStack.with());
            size = 2;
        }};

        payloadPackager = new PayloadPackager("payload-packager") {{
            requirements(Category.units, ItemStack.with(
                    Items.copper, 160,
                    Items.lead, 80,
                    Items.silicon, 80
            ));
            size = 3;
            itemCapacity = 80;

            packageBlock = (PayloadPackage) payloadPackage;

            consumeItem(Items.graphite, 40);
            consumePower(2f);
        }};

        payloadUnpackager = new PayloadUnpackager("payload-unpackager") {{
            requirements(Category.units, ItemStack.with(
                    Items.copper, 160,
                    Items.lead, 80,
                    Items.silicon, 80
            ));
            size = 3;

            consumePower(2f);
        }};
    }
}
