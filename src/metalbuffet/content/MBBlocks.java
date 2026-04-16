package metalbuffet.content;

import metalbuffet.content.blocks.*;

public class MBBlocks {
    public static void load() {
        MBFunBlocks.load();
        MBTurrets.load();
        MBPower.load();
        MBPayloadBlocks.load();
    }
}
