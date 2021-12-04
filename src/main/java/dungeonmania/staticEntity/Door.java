package dungeonmania.staticEntity;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.movingEntity.Spider;
import dungeonmania.staticEntity.collectableEntity.Key;
import dungeonmania.staticEntity.collectableEntity.buildableEntity.BuildableEntity;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private int keyNo;
    private boolean unlocked = false;

    /**
     * Constructor for Door
     * @param posix_entityType is the string that is after the entityType "Door_1"
     */

    public Door(int id, Position curr_position) {
        super(id, "door", curr_position);

        //beginning it's blocking
        currBlockingState = blockingState;
    }

    public Door(int id, String posix_entityType, Position curr_position) {
        super(id, "door_" + posix_entityType, curr_position);

        //beginning it's blocking
        currBlockingState = blockingState;
        if (posix_entityType.equals("unlocked")) {
            unlocked = true;
            currBlockingState = unblockingState;
        }
    }

    public int getKeyNo() {
        return this.keyNo;
    }

    public void setKeyNo(int keyNo) {
        this.keyNo = keyNo;
        if (keyNo == -1) {
            unlocked = true;
            currBlockingState = unblockingState;
        }
    }

    public boolean getUnlocked() {
        return this.unlocked;
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        // ========== Skin Management ==========
        if (entity instanceof MovingEntity || entity instanceof Boulder) {
            // For skins
            // To make Moving entites + Boulder skin show over door skin
            dungeon.skinsOrdering(entity, this);
        }

        // ========== Pre conditions ==========
        // Will have to interact with the other entity instead if more than 1 entity on same tile
        // Or already have interact hence, on same tile
        if (
            (dungeon.getEntities(this.getCurr_position()).size() > 1 && unlocked == true) || 
            entity.getCurr_position() == this.getCurr_position()
        ) return;
        
    
        // ========== Movement onto tile ==========
        // Spider can move on locked doors
        if (entity instanceof Spider || unlocked) {
            if (entity instanceof Player) {
                Player player_entity = (Player) entity;
                player_entity.setPrev_position(getCurr_position());
            }
            entity.setCurr_position(this.getCurr_position());
            dungeon.updateEntityResponse(entity);
        }
        // If locked, cannot move through door
        else if (!(entity instanceof Player) && unlocked == false) return;

        // ========== Unlock Door ==========
        // Must be player to unlock door
        if (!(entity instanceof Player)) return;
        Player player_entity = (Player) entity;
        Inventory player_bag = player_entity.getInventory();
        List<StaticEntity> keys = player_bag.getStaticEntities().stream().filter(e -> e instanceof Key).collect(Collectors.toList());

        // Check if player has sun stone first
        if (player_bag.getStaticEntity("sun_stone") != null) {
            player_entity.setPrev_position(getCurr_position());
            player_entity.setCurr_position(this.getCurr_position());
            this.unlocked = true;
            // Changing skins
            this.setEntityType("door_unlocked");
            // player_bag.removeItem(key);
            dungeon.updateEntityResponse(this);
            currBlockingState = unblockingState;
            BuildableEntity.createable(dungeon, player_bag);
        } else {
            for (StaticEntity key: keys) {
                if (((Key) key).getKeyNo() == this.getKeyNo()) {
                    player_entity.setPrev_position(getCurr_position());
                    player_entity.setCurr_position(this.getCurr_position());
                    this.unlocked = true;
                    // Changing skins
                    this.setEntityType("door_unlocked");
                    player_bag.removeItem(key);
                    dungeon.updateEntityResponse(this);
                    currBlockingState = unblockingState;
                    BuildableEntity.createable(dungeon, player_bag);
                    break;
                }
            }
        }
        return;
    }

}