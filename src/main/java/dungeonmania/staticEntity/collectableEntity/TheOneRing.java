package dungeonmania.staticEntity.collectableEntity;
import java.util.Iterator;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Player;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.util.Position;

public class TheOneRing extends CollectableEntity {
    /**
     * Constructor for TheOneRing
     * @param posix_entityType is the string that is after the entityType "TheOneRing_1"
     */

    public TheOneRing(int id, Position curr_position) {
        super(id, "one_ring", curr_position);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
    }

    @Override
    public void doUse(Entity playerEntity) {
        Player character = (Player) playerEntity;
        if (character.getOneRing() != null) {
            //remove the dead entity
            if(character.getHealth() <= 0) {
                // Check if player's inventory is empty or not
                if (character.getInventory().getCurrentInventorySpace() > 0) {
                    Iterator<StaticEntity> it = character.getInventory().getStaticEntities().iterator();
                    while(it.hasNext()) {
                        StaticEntity se = it.next();
                        // if inventory contains one ring set player health to full health
                        if (se.getEntityType().contains("one_ring")) {
                            character.setHealth(100);
                            it.remove();
                        } 
                    }
                }
            }
        }
    }

}
