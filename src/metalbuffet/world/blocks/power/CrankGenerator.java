package metalbuffet.world.blocks.power;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.power.*;

public class CrankGenerator extends PowerGenerator {
    public float crankLength = 40f, latchRange = 80f, latchStrength = 0.2f, crankAngleMultiplier = 0.01f, crankDuration = 30f;
    public float clawRestingLength = 8f, clawMinOffset = 2f, clawMinAngle = 5f;
    public TextureRegion bottom, rotator, clawHinge, clawNub, clawLine, clawHingeOutline, clawNubOutline;
    public TextureRegion[] clawRegions, clawOutlineRegions;

    public CrankGenerator(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();

        bottom = Core.atlas.find(name + "-bottom");
        rotator = Core.atlas.find(name + "-rotator");

        clawLine = Core.atlas.find(name + "-claw-line");

        clawHinge = Core.atlas.find(name + "-claw-hinge");
        clawNub = Core.atlas.find(name + "-claw-nub");
        clawHingeOutline = Core.atlas.find(name + "-claw-hinge-outline");
        clawNubOutline = Core.atlas.find(name + "-claw-nub-outline");

        clawRegions = new TextureRegion[2];
        clawOutlineRegions = new TextureRegion[2];
        for (int i = 0; i < 2; i++) {
            clawRegions[i] = Core.atlas.find(name + "-claw" + i);
            clawOutlineRegions[i] = Core.atlas.find(name + "-claw" + i + "-outline");
        }
    }

    @Override
    public void getRegionsToOutline(Seq<TextureRegion> out) {
        out.add(clawHinge, clawNub);
        out.add(clawRegions);
    }

    public class CrankGeneratorBuild extends GeneratorBuild {
        public float crankAngle = -1f, duration, clawAngle;
        public Vec2 clawPosition = new Vec2();

        @Override
        public void created() {
            clawPosition.set(x, y + clawRestingLength);
        }

        @Override
        public void updateTile() {
            Unit crankUnit = Units.closest(team, x, y, latchRange, u -> true);
            if (crankUnit != null) {
                Tmp.v1.set(crankUnit).sub(this).setLength(crankLength).add(this).lerpDelta(crankUnit, 1f - latchStrength);
                crankUnit.set(Tmp.v1);

                float angle = angleTo(crankUnit);
                if (crankAngle < 0f) {
                    crankAngle = angle;
                }
                duration = Mathf.clamp(duration + Angles.angleDist(angle, crankAngle) * crankAngleMultiplier);
                crankAngle = angle;

                clawPosition.set(crankUnit.x, crankUnit.y - clawMinOffset - (crankUnit.hitSize / 2f));
                clawAngle = Mathf.lerpDelta(clawAngle, clawMinAngle + (crankUnit.hitSize / 2f), 0.1f);
            } else {
                crankAngle = -1f;
                clawPosition.lerpDelta(x, y + clawRestingLength, 0.1f);
                clawAngle = Mathf.lerpDelta(clawAngle, 0f, 0.1f);
            }

            duration = Mathf.clamp(duration - getProgressIncrease(crankDuration));
            productionEfficiency = Mathf.clamp(duration * 2f);
        }

        @Override
        public void draw() {
            Draw.rect(bottom, x, y);
            Draw.rect(rotator, x, y, Mathf.maxZero(crankAngle));
            Draw.rect(region, x, y);

            Draw.z(Layer.flyingUnitLow - 0.1f);
            Lines.stroke(clawLine.height * clawLine.scl());
            Draw.z(Layer.flyingUnit + 0.1f);
            Lines.line(clawLine, x, y, clawPosition.x, clawPosition.y, false);
            Draw.rect(clawNubOutline, x, y, Mathf.maxZero(crankAngle));
            for (int i = 0; i < 2; i++) {
                Draw.rect(clawOutlineRegions[i], clawPosition.x, clawPosition.y, clawAngle * -Mathf.signs[i]);
            }
            Draw.rect(clawHingeOutline, clawPosition.x, clawPosition.y);

            Draw.z(Layer.flyingUnit + 0.2f);
            for (int i = 0; i < 2; i++) {
                Draw.rect(clawRegions[i], clawPosition.x, clawPosition.y, clawAngle * -Mathf.signs[i]);
            }
            Draw.rect(clawHinge, clawPosition.x, clawPosition.y);
        }
    }
}
