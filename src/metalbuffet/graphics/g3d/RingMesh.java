package metalbuffet.graphics.g3d;

import arc.graphics.*;
import arc.math.geom.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;

public class RingMesh extends PlanetMesh {
    public RingMesh(Planet planet, int seed, int innerRadius, int outerRadius, float radius, float rotation, Color color1, Color color2) {
        this.planet = planet;
        this.shader = Shaders.unlit;
        this.mesh = RingMeshBuilder.buildRing(seed, innerRadius, outerRadius, radius, rotation, color1, color2);
    }

    @Override
    public void preRender(PlanetParams params) {
        //Shaders.clouds.planet = planet;
        //Shaders.clouds.lightDir.set(planet.solarSystem.position).sub(planet.position).rotate(Vec3.Y, planet.getRotation()).nor();
        //Shaders.clouds.ambientColor.set(planet.solarSystem.lightColor);
        //Shaders.clouds.alpha = 1f;
    }

    @Override
    public void render(PlanetParams params, Mat3D projection, Mat3D transform) {
        Gl.disable(Gl.cullFace);
        super.render(params, projection, transform);
        Gl.enable(Gl.cullFace);
    }
}