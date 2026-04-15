package metalbuffet.graphics;

import arc.*;
import arc.files.*;
import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import metalbuffet.math.*;
import metalbuffet.math.KDTree.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.g3d.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.type.*;

public class PlanetMapper {
    private static final Color tmpColor = new Color();

    private static final Color[] difficultyColors = {
            Color.valueOf("95ff75"),
            Color.valueOf("f2ff64"),
            Color.valueOf("ffbf82"),
            Color.valueOf("ff8779"),
            Color.valueOf("ff96de")
    };

    public static <T extends TileColorData> T getNearestColorDataPtile(int x, int y, float radius, Pixmap pixmap, KDTree<T> tree) {
        float py = (((float) y / pixmap.height) * 2f) - 1f;
        float angle = -((float) x / pixmap.width) * 360f;
        float length = Mathf.sqrt(1f - Mathf.sqr(py));
        float px = Angles.trnsx(angle, length), pz = Angles.trnsy(angle, length);
        return tree.nearestNode(new float[]{px * radius, -py * radius, pz * radius}).object;
    }

    public static void run(Planet planet, int divisions, boolean doSectors) {
        Pixmap pixmap = new Pixmap(3072, 1536);

        PlanetGrid grid = PlanetGrid.create(divisions);
        Seq<TileColorData> envColorDataSeq = new Seq<>();
        for (Ptile tile : grid.tiles) {
            planet.generator.getColor(tile.v, tmpColor);
            tmpColor.a = 1f;
            envColorDataSeq.add(new TileColorData(tile, tmpColor.rgba()));
        }
        KDTree<TileColorData> envColorDataTree = new KDTree<>(3, envColorDataSeq.map(colorData -> new NodeConstructionData<>(new float[]{colorData.tile.v.x, colorData.tile.v.y, colorData.tile.v.z}, colorData)));

        pixmap.each((x, y) -> {
            TileColorData nearestEnvColorData = getNearestColorDataPtile(x, y, 1f, pixmap, envColorDataTree);
            if (nearestEnvColorData != null) {
                pixmap.set(x, y, nearestEnvColorData.color);
            }
        });

        if (doSectors) {
            Seq<SectorColorData> sectorColorDataSeq = new Seq<>();
            for (Sector sector : planet.sectors) {
                tmpColor.set(difficultyColors[Math.min((int) (sector.threat / 0.25f), difficultyColors.length)]).a(sector.preset == null ? 0f : 0.7f);
                sectorColorDataSeq.add(new SectorColorData(sector, tmpColor.rgba()));
            }
            KDTree<TileColorData> sectorColorDataTree = new KDTree<>(3, sectorColorDataSeq.map(colorData -> new NodeConstructionData<>(new float[]{colorData.tile.v.x, colorData.tile.v.y, colorData.tile.v.z}, colorData)));

            Pixmap difficultyPixmap = new Pixmap(pixmap.width, pixmap.height);
            difficultyPixmap.each((x, y) -> {
                TileColorData nearestSectorColorData = getNearestColorDataPtile(x, y, 1f, difficultyPixmap, sectorColorDataTree);
                if (nearestSectorColorData != null) {
                    difficultyPixmap.set(x, y, nearestSectorColorData.color);
                }
            });

            pixmap.draw(difficultyPixmap, true);

            for (SectorColorData sectorColorData : sectorColorDataSeq) {
                if (sectorColorData.sector.preset != null && sectorColorData.sector.preset.fullIcon != Icon.terrain.getRegion()) {
                    Pixmap iconPixmap = Core.atlas.getPixmap(sectorColorData.sector.preset.fullIcon).crop();

                    int targetX = 0, targetY = 0;
                    for (int x = 0; x < pixmap.width; x++) {
                        for (int y = 0; y < pixmap.height; y++) {
                            float py = (((float) y / pixmap.height) * 2f) - 1f;
                            float angle = -((float) x / pixmap.width) * 360f;
                            float radius = Mathf.sqrt(1f - Mathf.sqr(py));
                            float px = Angles.trnsx(angle, radius), pz = Angles.trnsy(angle, radius);
                            float dst = Tmp.v31.set(sectorColorData.tile.v).dst2(px, -py, pz);

                            float npy = (((float) targetY / pixmap.height) * 2f) - 1f;
                            float nangle = -((float) targetX / pixmap.width) * 360f;
                            float nradius = Mathf.sqrt(1f - Mathf.sqr(npy));
                            float npx = Angles.trnsx(nangle, nradius), npz = Angles.trnsy(nangle, nradius);
                            float ndst = Tmp.v31.set(sectorColorData.tile.v).dst2(npx, -npy, npz);

                            if (dst < ndst) {
                                targetX = x;
                                targetY = y;
                            }
                        }
                    }

                    pixmap.draw(iconPixmap, targetX - (iconPixmap.width / 2), targetY - (iconPixmap.height / 2), true);
                    // 2 more draws to wrap around the x axis
                    pixmap.draw(iconPixmap, targetX - (iconPixmap.width / 2) - pixmap.width, targetY - (iconPixmap.height / 2), true);
                    pixmap.draw(iconPixmap, targetX - (iconPixmap.width / 2) + pixmap.width, targetY - (iconPixmap.height / 2), true);
                }
            }
        }

        Fi file = new Fi("map-" + planet.name + ".png");
        file.writePng(pixmap);
    }

    public static class TileColorData {
        public float x, y;
        public Ptile tile;
        public int color;

        public TileColorData(Ptile tile, int color) {
            this.x = Angles.angle(tile.v.x, tile.v.z) / 360f;
            this.y = (-tile.v.y + 1f) / 2f;
            this.tile = tile;
            this.color = color;
        }
    }

    public static class SectorColorData extends TileColorData {
        public Sector sector;

        public SectorColorData(Sector sector, int color) {
            super(sector.tile, color);
            this.sector = sector;
        }
    }
}
