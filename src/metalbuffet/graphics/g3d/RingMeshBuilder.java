package metalbuffet.graphics.g3d;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.noise.*;

import java.util.*;

// have fun!
public class RingMeshBuilder {
    private static final Rand rand = new Rand();
    private static final Vec2 tmp = new Vec2();
    private static final Vec2[] d6 = {
            new Vec2(0f, 1f),
            new Vec2(Mathf.sqrt3 / 2f, 0.5f),
            new Vec2(Mathf.sqrt3 / 2f, -0.5f),
            new Vec2(0f, -1f),
            new Vec2(-Mathf.sqrt3 / 2f, -0.5f),
            new Vec2(-Mathf.sqrt3 / 2f, 0.5f)
    };
    private static final Vec3[] array = new Vec3[6];

    static {
        for (int i = 0; i < 6; i++) {
            array[i] = new Vec3();
        }
    }

    public static Mesh buildRing(int seed, int innerRadius, int outerRadius, float radius, float rotation, Color color1, Color color2) {
        rand.setSeed(seed);

        float scale = radius / outerRadius;
        int range = (int) (outerRadius / 1.5f) + 2;
        Mesh mesh = MeshUtils.begin((range * range * 4) * 12, 0, true, false);

        float[] floats = new float[3 + (MeshUtils.packNormals ? 1 : 3) + 1];
        Vec3 nor = new Vec3();
        Color tmpCol = new Color();

        for (int x = -range; x < range; x++) {
            for (int y = -range; y < range; y++) {
                hexToSquare(x, y);
                if (tmp.len() > innerRadius && tmp.len() < outerRadius) {
                    tmpCol.set(color1).lerp(color2, Math.abs((float) Noise.rawNoise(60f + Mathf.curve(tmp.len() / outerRadius, (float) innerRadius / outerRadius, 1f) * 5f)));
                    tmpCol.a(rand.random(0.8f, 1f));
                    tmpCol.a *= (float) Math.abs(Noise.rawNoise(20f + Mathf.curve(tmp.len() / outerRadius, (float) innerRadius / outerRadius, 1f) * 7f)) * 0.6f + 0.4f;
                    float col = tmpCol.toFloatBits();

                    tmp.scl(scale);

                    for (int i = 0; i < 6; i++) {
                        array[i].set(tmp.x + d6[i].x * scale, 0f, tmp.y + d6[i].y * scale).rotate(Vec3.X, rotation);
                    }

                    MeshUtils.normal(array[0], array[2], array[4], nor);

                    MeshUtils.verts(mesh, floats, array[0], array[1], array[2], nor, col, 0f);
                    MeshUtils.verts(mesh, floats, array[0], array[2], array[3], nor, col, 0f);
                    MeshUtils.verts(mesh, floats, array[0], array[3], array[4], nor, col, 0f);
                    MeshUtils.verts(mesh, floats, array[0], array[4], array[5], nor, col, 0f);
                }
            }
        }

        return MeshUtils.end(mesh);
    }

    private static void hexToSquare(int x, int y) {
        tmp.set(
                Mathf.sqrt3 * (x + 0.5f * (y % 2)),
                1.5f * y
        );
    }
}