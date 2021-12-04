package dungeonmania.staticEntity;

import dungeonmania.Dungeon;
import dungeonmania.movingEntity.Player;

public interface TickItemUse {
    public void doItemUse(Player player, Dungeon dungeon);
}
