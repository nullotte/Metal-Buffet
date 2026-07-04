package metalbuffet.content;

import metalbuffet.type.unit.*;
import mindustry.content.*;
import mindustry.type.*;

public class MBUnitTypes {
    public static UnitType miniErekir, miniSerpulo;

    public static void load() {
        miniErekir = new MiniPlanetUnitType(Planets.erekir);
        miniSerpulo = new MiniPlanetUnitType(Planets.serpulo);
    }
}
