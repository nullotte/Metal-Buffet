package metalbuffet.entities.bullet;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.defense.turrets.Turret.*;

public class TrackingSwordBulletType extends BulletType {
    public float length, trackTime, trackLength, triggerTime, flashTime, fadeInTime, stepLength, stepLife, stepDelay;
    public int stepCount;
    public String sprite = "metal-buffet-rory-sword";

    public Sound triggerSound = Sounds.explosionQuad;

    public TextureRegion region;

    public TrackingSwordBulletType() {
        speed = 0f;
        layer = Layer.bullet - 1f;
        hitEffect = despawnEffect = Fx.none;
        collidesTiles = false;
        collides = false;
    }

    @Override
    public void init() {
        super.init();
        drawSize = stepLength * stepCount * 1.2f;
    }

    @Override
    public void load() {
        super.load();
        region = Core.atlas.find(sprite);
    }

    @Override
    public void update(Bullet b) {
        super.update(b);

        if (b.data instanceof Posc target) {
            if (b.time < trackTime) {
                float trackOffset = 0f;
                if (b.data instanceof Hitboxc hitbox) {
                    trackOffset = hitbox.hitSize() * 2f;
                }
                b.set(target.x() - Angles.trnsx(b.rotation(), trackLength + trackOffset), target.y() - Angles.trnsy(b.rotation(), trackLength + trackOffset));
            } else if (b.time > triggerTime) {
                b.data = null;
                Damage.collideLaser(b, length, false, false, pierceCap);
                triggerSound.at(b);
            }
        }
    }

    @Override
    public void draw(Bullet b) {
        super.draw(b);

        if (b.time < triggerTime) {
            if (b.time > triggerTime - flashTime) {
                Draw.mixcol(Color.white, 1f);
            } else {
                Draw.color(Color.white, Color.red, Mathf.clamp(b.time / (triggerTime - flashTime)));
            }
            Draw.alpha(b.time > fadeInTime ? 1f : b.time / fadeInTime);
            Draw.rect(region, b.x, b.y, b.rotation() - 90f);
        } else {
            float totalTime = b.time - triggerTime; // 0 to 30
            Draw.color(Color.red);
            for (int i = 0; i < stepCount; i++) {
                float stepTime = totalTime - (stepDelay * i);
                if (stepTime > stepLife) continue;
                Draw.alpha(0.7f * (1f - Mathf.clamp(stepTime / stepLife)));
                Draw.rect(region, b.x + Angles.trnsx(b.rotation(), stepLength * i), b.y + Angles.trnsy(b.rotation(), stepLength * i), b.rotation() - 90f);
            }
        }
    }
}
