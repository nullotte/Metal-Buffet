package metalbuffet.graphics.g3d;

import arc.*;
import arc.graphics.*;
import arc.math.geom.*;
import arc.struct.*;

public class MeshUtils {
    public static final boolean packNormals = Core.gl30 != null && (Core.app.isMobile() || Core.graphics.getGLVersion().atLeast(3, 3));

    public static void verts(Mesh mesh, float[] floats, Vec3 a, Vec3 b, Vec3 c, Vec3 normal, float color, float emissive) {
        vert(mesh, floats, a.x, a.y, a.z, normal, color, emissive);
        vert(mesh, floats, b.x, b.y, b.z, normal, color, emissive);
        vert(mesh, floats, c.x, c.y, c.z, normal, color, emissive);
    }

    public static void vert(Mesh mesh, float[] floats, float x, float y, float z, Vec3 normal, float color, float emissive) {
        floats[0] = x;
        floats[1] = y;
        floats[2] = z;

        if (packNormals) {
            floats[3] = packNormals(normal.x, normal.y, normal.z);

            floats[4] = color;
            if (floats.length > 5) floats[5] = emissive;
        } else {
            floats[3] = normal.x;
            floats[4] = normal.y;
            floats[5] = normal.z;

            floats[6] = color;
            if (floats.length > 7) floats[7] = emissive;
        }

        mesh.getVerticesBuffer().put(floats);
    }

    public static void vertsTextured(Mesh mesh, float[] floats, Vec3 a, Vec3 b, Vec3 c, Vec3 normal) {
        vertTextured(mesh, floats, a.x, a.y, a.z, normal, 0f, 0f);
        vertTextured(mesh, floats, b.x, b.y, b.z, normal, 1f, 0f);
        vertTextured(mesh, floats, c.x, c.y, c.z, normal, 0.5f, 1f);
    }

    public static void vertTextured(Mesh mesh, float[] floats, float x, float y, float z, Vec3 normal, float texCoordsX, float texCoordsY) {
        floats[0] = x;
        floats[1] = y;
        floats[2] = z;

        if (packNormals) {
            floats[3] = packNormals(normal.x, normal.y, normal.z);

            floats[4] = texCoordsX;
            floats[5] = texCoordsY;
        } else {
            floats[3] = normal.x;
            floats[4] = normal.y;
            floats[5] = normal.z;

            floats[6] = texCoordsX;
            floats[7] = texCoordsY;
        }

        mesh.getVerticesBuffer().put(floats);
    }

    public static Mesh begin(int vertices, int indices, boolean normal, boolean emissive) {
        return begin(vertices, indices, normal, emissive, true, false);
    }

    public static Mesh begin(int vertices, int indices, boolean normal, boolean emissive, boolean color, boolean texture) {
        Seq<VertexAttribute> attributes = Seq.with(
                VertexAttribute.position3
        );

        if (normal) {
            //only GL30 supports GL_INT_2_10_10_10_REV
            attributes.add(packNormals ? VertexAttribute.packedNormal : VertexAttribute.normal);
        }

        if (color) {
            attributes.add(VertexAttribute.color);
        }

        if (emissive) {
            attributes.add(new VertexAttribute(4, GL20.GL_UNSIGNED_BYTE, true, "a_emissive"));
        }

        if (texture) {
            attributes.add(VertexAttribute.texCoords);
        }

        Mesh mesh = new Mesh(true, vertices, indices, attributes.toArray(VertexAttribute.class));

        mesh.getVerticesBuffer().limit(mesh.getVerticesBuffer().capacity());
        mesh.getVerticesBuffer().position(0);

        if (indices > 0) {
            mesh.getIndicesBuffer().limit(mesh.getIndicesBuffer().capacity());
            mesh.getIndicesBuffer().position(0);
        }

        return mesh;
    }

    public static Mesh end(Mesh mesh) {
        mesh.getVerticesBuffer().limit(mesh.getVerticesBuffer().position());
        if (mesh.getNumIndices() > 0) {
            mesh.getIndicesBuffer().limit(mesh.getIndicesBuffer().position());
        }

        return mesh;
    }

    public static void normal(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 out) {
        out.set(v2).sub(v1).crs(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z).nor();
    }

    public static float packNormals(float x, float y, float z) {
        int xs = x < -1f / 512f ? 1 : 0;
        int ys = y < -1f / 512f ? 1 : 0;
        int zs = z < -1f / 512f ? 1 : 0;

        int vi =
                zs << 29 | ((int) (z * 511 + (zs << 9)) & 511) << 20 |
                        ys << 19 | ((int) (y * 511 + (ys << 9)) & 511) << 10 |
                        xs << 9 | ((int) (x * 511 + (xs << 9)) & 511);
        return Float.intBitsToFloat(vi);
    }
}
