package metalbuffet.entities.abilities;

import mindustry.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;

public class AreaDamageAbility extends Ability {
    public float damage = 1000f;

    @Override
    public void update(Unit unit) {
        Damage.damage(unit.team, unit.x, unit.y, unit.hitSize * Vars.unitCollisionRadiusScale, damage);
    }
}