package metalbuffet.graphics.g3d;

import arc.graphics.*;
import arc.math.geom.*;

public class OctahedronMeshBuilder {
    private static final Vec3[] tmpVectors = new Vec3[3];
    static {
        for (int i = 0; i < 3; i++) {
            tmpVectors[i] = new Vec3();
        }
    }

    public static Mesh buildTexturedOctahedron() {
        Mesh mesh = MeshUtils.begin(8 * 3, 0, true, false, false, true);
        float[] floats = new float[3 + (MeshUtils.packNormals ? 1 : 3) + 2];
        Vec3 nor = new Vec3();

        for (int i = 0; i < 4; i++) {
            int x1 = Geometry.d4x(i), z1 = Geometry.d4y(i), x2 = Geometry.d4x(i + 1), z2 = Geometry.d4y(i + 1);

            tmpVectors[0].set(x1, 0f, z1);
            tmpVectors[1].set(0f, 1f, 0f);
            tmpVectors[2].set(x2, 0f, z2);
            MeshUtils.normal(tmpVectors[0], tmpVectors[1], tmpVectors[2], nor);
            MeshUtils.vertsTextured(mesh, floats, tmpVectors[0], tmpVectors[1], tmpVectors[2], nor);

            tmpVectors[0].set(x1, 0f, z1);
            tmpVectors[1].set(x2, 0f, z2);
            tmpVectors[2].set(0f, -1f, 0f);
            MeshUtils.normal(tmpVectors[0], tmpVectors[1], tmpVectors[2], nor);
            MeshUtils.vertsTextured(mesh, floats, tmpVectors[0], tmpVectors[1], tmpVectors[2], nor);
        }

        return MeshUtils.end(mesh);
    }
}
