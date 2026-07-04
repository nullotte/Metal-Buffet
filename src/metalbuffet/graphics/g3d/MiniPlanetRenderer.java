package metalbuffet.graphics.g3d;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;

public class MiniPlanetRenderer extends PlanetRenderer {
    public void render(PlanetParams params, Mat3D mat) {
        Draw.flush();
        Gl.clear(Gl.depthBufferBit);
        Gl.enable(Gl.depthTest);
        Gl.depthMask(true);

        Gl.enable(Gl.cullFace);
        Gl.cullFace(Gl.back);

        //bloom.blending = false;

        cam.resize(params.viewW, params.viewH);
        cam.position.set(Vec3.X).scl(params.planet.radius + 1.5f);
        cam.up.set(Vec3.Y);
        cam.lookAt(Vec3.Zero);
        cam.update();

        projector.proj(cam.combined);
        batch.proj(cam.combined);

        Events.fire(Trigger.universeDrawBegin);

        //begin bloom
        //bloom.resize(params.viewW, params.viewH);
        //bloom.capture();

        Events.fire(Trigger.universeDraw);

        renderPlanet(params.planet, params, mat);
        // TODO cant render atmosphere...

        //bloom.render();

        Events.fire(Trigger.universeDrawEnd);

        Gl.enable(Gl.blend);

        Gl.disable(Gl.cullFace);
        Gl.disable(Gl.depthTest);

        cam.update();
    }

    public void renderPlanet(Planet planet, PlanetParams params, Mat3D mat) {
        cam.update();
        planet.draw(params, cam.combined, mat);
    }
}
