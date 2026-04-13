package metalbuffet.content;

import arc.graphics.*;
import metalbuffet.graphics.g3d.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.maps.planet.*;
import mindustry.type.*;

public class MBPlanets {
    public static Planet planetWithRings;

    public static void load() {
        planetWithRings = new Planet("planet-with-rings", Planets.sun, 1f, 2) {{
            meshLoader = () -> new MultiMesh(
                    new ShaderSphereMesh(this, Shaders.planet, 2),
                    new RingMesh(this, 0, 80, 120, 3f, 20f, Color.valueOf("ebfffd"), Color.valueOf("ade4ff"))
            );

            alwaysUnlocked = true;
            generator = new AsteroidGenerator();
        }};
    }
}
