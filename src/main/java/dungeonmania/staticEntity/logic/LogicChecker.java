package dungeonmania.staticEntity.logic;

import dungeonmania.Dungeon;

public interface LogicChecker {
    public void logicFulfilled(Dungeon dungeon);
    public void setLogic(String logic);
    public Logic getLogic();
}
