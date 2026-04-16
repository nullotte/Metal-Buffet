package metalbuffet.entities.bullet;

import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;

public class HyperlaserGunBulletType extends BasicBulletType {
    public static Color blue = Color.valueOf("2e9cf9");
    public static Effect hitEffect = new Effect(60f, e -> {
        if (!(e.data instanceof Unit unit)) return;

        Draw.alpha(e.fout());
        Draw.mixcol(blue, 1f);
        Draw.rect(unit.type.fullIcon, e.x, e.y, e.rotation);
    });

    public HyperlaserGunBulletType(float speed) {
        super(speed, 999999f); // lol
    }

    @Override
    public void hitEntity(Bullet b, Hitboxc entity, float health) {
        entity.remove();
        hitSound.at(entity);
        if (entity instanceof Unit unit) {
            hitEffect.at(unit.x, unit.y, unit.rotation - 90f, unit);
        }
    }
}
