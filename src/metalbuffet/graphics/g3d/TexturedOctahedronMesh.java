package metalbuffet.graphics.g3d;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.geom.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;

public class TexturedOctahedronMesh extends HexMesh {
    public TextureRegion region;
    public Texture texture;
    public Color color = Color.white.cpy();

    public TexturedOctahedronMesh(Planet planet) {
        this.planet = planet;
        this.shader = Shaders.planet;
        this.mesh = OctahedronMeshBuilder.buildTexturedOctahedron();

        region = Core.atlas.find("router");
    }

    @Override
    public void render(PlanetParams params, Mat3D projection, Mat3D transform) {
        if (mesh.isDisposed()) return;

        preRender(params);
        if (texture == null) {
            texture = Pixmaps.noiseTex(128, 128);
        }

        shader.bind();
        shader.setUniformMatrix4("u_proj", projection.val);
        shader.setUniformMatrix4("u_trans", transform.val);
        texture.bind(0);
        shader.setUniformi("u_texture", 0);
        shader.apply();
        mesh.render(shader, Gl.triangles);
    }
}
