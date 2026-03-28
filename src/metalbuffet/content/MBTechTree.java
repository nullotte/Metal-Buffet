package metalbuffet.content;

import arc.util.*;
import mindustry.content.*;
import mindustry.content.TechTree.*;
import mindustry.ctype.*;

import static metalbuffet.content.blocks.MBPayloadBlocks.*;
import static mindustry.content.TechTree.*;

// i guess bro
public class MBTechTree {
    public static void load() {
        attachNode(Blocks.payloadConveyor, () -> {
            node(payloadPackager, () -> {
                node(payloadUnpackager);
            });
        });
    }

    public static void attachNode(UnlockableContent parent, Runnable children) {
        Reflect.set(TechTree.class, "context", TechTree.all.find(n -> n.content == parent));
        children.run();
        Reflect.set(TechTree.class, "context", null);
    }
}
