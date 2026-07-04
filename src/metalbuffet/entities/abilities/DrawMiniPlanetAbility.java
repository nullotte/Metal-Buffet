package metalbuffet.entities.abilities;

import arc.*;
import arc.graphics.*;
import arc.graphics.Texture.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.geom.*;
import arc.util.*;
import metalbuffet.*;
import mindustry.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;

public class DrawMiniPlanetAbility extends Ability {
    public static final CustomVec3 lightDir = new CustomVec3();
    public static final Vec3 rotatedVec = new Vec3();
    public static final float bufferScaleFactor = 1.2f;

    public Planet drawPlanet;

    public Mat3D mat;
    public FrameBuffer buffer;
    public PlanetParams planetParams;

    {
        display = false;
    }

    public DrawMiniPlanetAbility(Planet drawPlanet) {
        this.drawPlanet = drawPlanet;
    }

    @Override
    public void update(Unit unit) {
        if (mat == null) {
            mat = new Mat3D();
        }
        mat.rotate(Mat3D.unrotate(Tmp.v31.set(Vec3.Z), mat), -unit.vel.y * 3f);
        mat.rotate(Mat3D.unrotate(Tmp.v31.set(Vec3.Y), mat), unit.vel.x * 3f);
    }

    @Override
    public void draw(Unit unit) {
        if (mat == null) {
            mat = new Mat3D();
        }

        if (buffer == null) {
            buffer = new FrameBuffer(
                    (int) (unit.hitSize * 8f * Vars.unitCollisionRadiusScale * bufferScaleFactor),
                    (int) (unit.hitSize * 8f * Vars.unitCollisionRadiusScale * bufferScaleFactor)
            );
        }

        if (planetParams == null) {
            planetParams = new PlanetParams();
            planetParams.planet = drawPlanet;
            planetParams.viewW = buffer.getWidth();
            planetParams.viewH = buffer.getHeight();
        }

        buffer.getTexture().setFilter(TextureFilter.linear);
        buffer.begin(Color.clear);

        Mat3D.unrotate(rotatedVec.set(Vec3.X), mat);
        Vec3 prevLightDir = Shaders.planet.lightDir;
        Shaders.planet.lightDir = lightDir;
        MetalBuffet.miniPlanetRenderer.render(planetParams, mat);
        Shaders.planet.lightDir = prevLightDir;

        buffer.end();

        Draw.rect(Draw.wrap(buffer.getTexture()), unit.x, unit.y);

        // horrible
        Core.app.post(() -> {
            if (buffer != null && !unit.isAdded()) {
                buffer.dispose();
                buffer = null;
            }
        });
    }

    // ????? ??? ?????????????
    public static class CustomVec3 extends Vec3 {
        @Override
        public Vec3 nor() {
            set(rotatedVec);
            return this;
        }
    }
}
