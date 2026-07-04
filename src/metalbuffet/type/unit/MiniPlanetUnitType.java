package metalbuffet.type.unit;

import metalbuffet.entities.abilities.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.type.*;

public class MiniPlanetUnitType extends UnitType {
    public Planet drawPlanet;

    public MiniPlanetUnitType(Planet drawPlanet) {
        super("mini-" + drawPlanet.name);
        constructor = ElevationMoveUnit::create;

        drag = 0.07f;
        speed = 2f;
        rotateSpeed = 5f;
        accel = 0.09f;
        health = 100000f;
        itemCapacity = 0;

        drawCell = false;
        drawBody = false;

        this.drawPlanet = drawPlanet;
        abilities.add(new DrawMiniPlanetAbility(drawPlanet));
        hitSize = drawPlanet.radius * 64f;
        softShadowScl = hitSize / 10f;
        abilities.add(new AreaDamageAbility());
    }
}