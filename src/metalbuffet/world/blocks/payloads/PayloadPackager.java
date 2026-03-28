package metalbuffet.world.blocks.payloads;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.blocks.payloads.*;
import mindustry.world.meta.*;

public class PayloadPackager extends PayloadBlock {
    public PayloadPackage packageBlock;
    public float craftTime = 30f;

    public PayloadPackager(String name) {
        super(name);
        rotate = true;
        acceptsPayload = true;
        outputsPayload = true;
        regionRotated1 = 1;
        regionRotated2 = 2;
    }

    @Override
    public void setStats() {
        stats.timePeriod = craftTime;
        super.setStats();
        stats.add(Stat.productionTime, craftTime / 60f, StatUnit.seconds);
    }

    @Override
    public void drawPlanRegion(BuildPlan plan, Eachable<BuildPlan> list) {
        Draw.rect(region, plan.drawx(), plan.drawy());
        Draw.rect(inRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);
        Draw.rect(outRegion, plan.drawx(), plan.drawy(), plan.rotation * 90f);
        Draw.rect(topRegion, plan.drawx(), plan.drawy());
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, inRegion, outRegion, topRegion};
    }

    public class PayloadPackagerBuild extends PayloadBlockBuild<Payload> {
        public boolean crafting;
        public float progress;

        @Override
        public void updateTile() {
            super.updateTile();

            crafting = false;
            if (payload instanceof BuildPayload buildPayload && buildPayload.block() == packageBlock) {
                moveOutPayload();
            } else if (moveInPayload() && efficiency > 0f) {
                crafting = true;
                progress += getProgressIncrease(craftTime);
                if (progress >= 1f) {
                    progress %= 1f;

                    BuildPayload payloadPackage = new BuildPayload(packageBlock, team);
                    payloadPackage.build.handlePayload(this, payload);

                    payload = payloadPackage;

                    Fx.producesmoke.at(this);
                    consume();
                }
            }
        }

        @Override
        public void draw() {
            Draw.rect(region, x, y);

            boolean fallback = true;
            for (int i = 0; i < 4; i++) {
                if (blends(i) && i != rotation) {
                    Draw.rect(inRegion, x, y, (i * 90f) - 180f);
                    fallback = false;
                }
            }
            if (fallback) Draw.rect(inRegion, x, y, rotation * 90f);

            Draw.rect(outRegion, x, y, rotdeg());

            // i cant be bothered to make it actually on top... it just looks weird when outputting...
            Draw.rect(topRegion, x, y);

            Draw.z(Layer.blockOver);
            if (payload != null) {
                if (crafting) {
                    Draw.scl(Interp.pow2Out.apply(1f - progress));
                    // TODO not a big fan of this
                    payload.drawShadow(1f);
                    Draw.rect(payload.icon(), x, y, payload instanceof BuildPayload ? 0f : payRotation - 90f);
                    Draw.scl();
                } else {
                    drawPayload();
                }
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(progress);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            progress = read.f();
        }
    }
}
