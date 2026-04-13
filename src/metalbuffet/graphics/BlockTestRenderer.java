package metalbuffet.graphics;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.*;
import mindustry.graphics.*;

public class BlockTestRenderer {
    public void draw() {
        if (Vars.control.input.block == null) return;

        float tx = Core.input.mouseWorldX() - (Core.input.mouseWorldX() % Vars.tilesize);
        float ty = Core.input.mouseWorldY() - (Core.input.mouseWorldY() % Vars.tilesize);

        int range = 6;
        int outerRange = range + 2;
        Draw.z(Layer.blockUnder - 1f);
        Draw.color(Pal.accent);
        for (int rx = -outerRange; rx <= outerRange; rx++) {
            for (int ry = -outerRange; ry <= outerRange; ry++) {
                float px = rx * Vars.tilesize + tx;
                float py = ry * Vars.tilesize + ty;
                Draw.alpha(0.6f * (1f - Mathf.clamp(Mathf.dst(Core.input.mouseWorldX(), Core.input.mouseWorldY(), px, py) / (range * Vars.tilesize))));
                Fill.square(px, py, 3f);
            }
        }
        Draw.reset();
    }
}
