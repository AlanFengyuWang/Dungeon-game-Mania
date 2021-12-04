package dungeonmania.staticEntity.collectableEntity;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Position;

public class Anduril extends CollectableEntity{
    private int damage_enhance_multiply = 4;
    public Anduril(int id, Position curr_position) {
        super(id, "anduril", curr_position);
    }

    @Override
    public void doUse(Entity playerEntity) {
        if (!(playerEntity instanceof Player)) {
            return;
        }
        Player mvEntity = (Player) playerEntity;
        mvEntity.getInventory().getEquippedWeapons().clear();
        mvEntity.setAttackDamage(mvEntity.getAttackDamage() * damage_enhance_multiply);
        mvEntity.getInventory().storeEquippedWeapon(this);
        
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        return;
    }
    
}
