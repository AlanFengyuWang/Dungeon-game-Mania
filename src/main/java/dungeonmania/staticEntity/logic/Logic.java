package dungeonmania.staticEntity.logic;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;

public interface Logic {
    public boolean logicActivate(Dungeon dungeon, Entity center);
    public String getLogicString();
}
