package metalbuffet.world.blocks.fun;

import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.io.*;
import metalbuffet.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

public class RoryBlock extends Block {
    public BulletType bulletType;
    public float reload, range;
    public boolean evil;

    public Sound shootSound = Sounds.unitCreate;

    public RoryBlock(String name) {
        super(name);
        update = true;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        Drawf.dashCircle(x * Vars.tilesize + offset, y * Vars.tilesize + offset, range, Pal.placing);
    }

    public class RoryBuild extends Building {
        public float progress;
        public int roryRotation;

        @Override
        public void updateTile() {
            super.updateTile();

            if (progress < 1f) {
                progress += getProgressIncrease(reload);
            } else {
                Unit unit = evil ? Units.closest(team, x, y, range,u -> true) : Units.closestEnemy(team, x, y, range, u -> true);
                if (unit != null) {
                    roryRotation = (roryRotation + Mathf.random(1, 7)) % 8;
                    Team prevTeam = team;
                    if (evil) {
                        team = Vars.state.rules.waveTeam;
                    }
                    Bullet bullet = bulletType.create(this, x, y, roryRotation * 45f);
                    team = prevTeam;
                    bullet.data = unit;

                    shootSound.at(unit);

                    progress %= 1f;
                }
            }
        }

        @Override
        public void drawSelect() {
            Drawf.dashCircle(x, y, range, team.color);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(progress);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            progress = read.f();
        }
    }
}
