package metalbuffet.world.blocks.payloads;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.world.blocks.payloads.*;

public class PayloadPackage extends PayloadBlock {
    public float payloadSize = 48f;

    public TextureRegion bottomRegion;

    public PayloadPackage(String name) {
        super(name);
        rebuildable = false;
    }

    @Override
    public void load() {
        super.load();
        bottomRegion = Core.atlas.find(name + "-bottom");
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[]{bottomRegion, region};
    }

    public class PayloadPackageBuild extends PayloadBlockBuild<Payload> {
        @Override
        public void updateTile() {
            super.updateTile();

            if (tile != Vars.emptyTile) {
                kill();
            }
        }

        @Override
        public void onRemoved() {
            carried = tile == Vars.emptyTile;
            updatePayload();
            super.onRemoved();
        }

        @Override
        public void draw() {
            Draw.rect(bottomRegion, x, y);
            if (payload != null) {
                Vec2 scaledSize = Scaling.bounded.apply(
                        payload.icon().width * region.scl(),
                        payload.icon().height * region.scl(),
                        payloadSize * region.scl(),
                        payloadSize * region.scl()
                );
                Draw.rect(payload.icon(), x, y, scaledSize.x, scaledSize.y);
            }
            Draw.rect(region, x, y);
        }
    }
}
