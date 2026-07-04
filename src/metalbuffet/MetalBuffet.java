package metalbuffet;

import arc.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.util.*;
import metalbuffet.audio.*;
import metalbuffet.content.*;
import metalbuffet.graphics.*;
import metalbuffet.graphics.g3d.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.graphics.g3d.*;
import mindustry.mod.*;
import mindustry.type.*;

public class MetalBuffet extends Mod {
    public static MiniPlanetRenderer miniPlanetRenderer;

    public MetalBuffet() {
        Events.on(FileTreeInitEvent.class, e -> {
            MBSounds.load();
        });
    }

    @Override
    public void init() {
        miniPlanetRenderer = new MiniPlanetRenderer();
    }

    @Override
    public void loadContent() {
        MBUnitTypes.load();
        MBBlocks.load();
        MBPlanets.load();
        MBTechTree.load();
    }

    public static void testPlanetMapping(Planet planet, int divisions, boolean doSectors) {
        PlanetMapper.run(planet, divisions, doSectors);
    }

    public static void testBufferRender(Planet planet) {
        FrameBuffer buffer = new FrameBuffer(256, 256);
        buffer.begin(Color.clear);
        Draw.proj(0, 0, buffer.getWidth(), buffer.getHeight());

        PlanetParams params = new PlanetParams();
        params.planet = planet;
        params.viewW = buffer.getWidth();
        params.viewH = buffer.getHeight();
        params.zoom = 0.2f;
        params.camPos.set(params.planet.solarSystem.position).sub(params.planet.position).nor();
        params.planet.hasAtmosphere = false;
        params.drawSkybox = false;

        Vars.renderer.planets.render(params);
        buffer.end();
        Draw.flush();

        buffer.begin();
        Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, buffer.getWidth(), buffer.getHeight());
        new Fi("render-" + planet.name + ".png").writePng(pixmap);
        buffer.end();

        buffer.dispose();
    }
}
