package dungeonmania.movingEntity;

import dungeonmania.staticEntity.*;
import dungeonmania.staticEntity.collectableEntity.Arrows;
import dungeonmania.staticEntity.collectableEntity.buildableEntity.BuildableEntity;
import dungeonmania.equipAndCollectablemodel.DurableEquipment;
import dungeonmania.response.models.ItemResponse;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private int space = 10;
    private List <StaticEntity> staticEntities = new ArrayList<>();  
    private List <BuildableEntity> buildableEntities = new ArrayList<>();  
    private List <StaticEntity> equippedWeapons = new ArrayList<>();  
    private List <StaticEntity> equippedDefenseEntities = new ArrayList<>();  

    public void storeEquippedWeapon(StaticEntity weapon) {
        /**
         * Check if we have equipped weapons, if the new staticEntity we collect is equippedWeapon, we add it to the list equippedWeapons
         * Don't add the equipped weapons/defense entities into List <StaticEntity> staticEntities and List <BuildableEntity> buildableEntities
         */
        equippedWeapons.add(weapon);
    }

    public void removeEquippedWeapon(StaticEntity weapon) {
        /**
         * Removes item from equippedWeapons list once durability is over
         */
        equippedWeapons.remove(weapon);
    }

    public void storeEquippedDefence(StaticEntity defenceItem) {
        /**
         * Check if we have equipped defenseEntities, if the new staticEntity we collect is defenseEntities, we add it to the list equippedWeapons (Consider you can wear armor and shield)
         * Don't add the equipped weapons/defense entities into List <StaticEntity> staticEntities and List <BuildableEntity> buildableEntities
         */
        equippedDefenseEntities.add(defenceItem);
    }

    public void storeToInventory(StaticEntity se) {
        staticEntities.add(se);
    }

    public void removeEquippedDefence(StaticEntity defenceItem) {
        /**
         * Removes item from equippedWeapons list once durability is over
         */
        equippedDefenseEntities.remove(defenceItem);
    }

    public StaticEntity getEquippedWeapon(String weaponType) {
        for(StaticEntity staticEntity : equippedWeapons) {
            // System.out.println("inventory.getStaticEntity --> staticEntitity = " + staticEntity.getEntityType()+ " staticEntityType = " + staticEntityType);
            if(staticEntity.getEntityType().equals(weaponType)) {
                return staticEntity;
            }
        }
        return null;
    }

    public StaticEntity getEquippedDefence(String defenceType) {
        for(StaticEntity staticEntity : equippedDefenseEntities) {
            // System.out.println("inventory.getStaticEntity --> staticEntitity = " + staticEntity.getEntityType()+ " staticEntityType = " + staticEntityType);
            if(staticEntity.getEntityType().equals(defenceType)) {
                return staticEntity;
            }
        }
        return null;
    }

    public List<StaticEntity> getEquippedWeapons() {
        return equippedWeapons;
    }

    public List<StaticEntity> getEquippedDefences() {
        return equippedDefenseEntities;
    }

    public StaticEntity getStaticEntity(String staticEntityType) {
        for(StaticEntity staticEntity : staticEntities) {
            if(staticEntity.getEntityType().equals(staticEntityType)) {
                return staticEntity;
            }
        }
        return null;
    }

    public StaticEntity getStaticEntity(int itemID) {
        for(StaticEntity staticEntity : staticEntities) {
            if(staticEntity.getId() == itemID) {
                return staticEntity;
            }
        }
        return null;
    }

    public List<BuildableEntity> getBuildableEntities() {
        return buildableEntities;
    }

    public List<String> getBuildableEntitiesStrings() {
        List <String> buildableString = new ArrayList<>();  
        
        for (BuildableEntity be : buildableEntities) {
            buildableString.add(be.getEntityType());
        }
        return buildableString;
    }

    public List<StaticEntity> getStaticEntities() {
        return staticEntities;
    }

    public void addBuildableItem(BuildableEntity craftItem) {
        if (!checkDuplicateBuildable(craftItem))  buildableEntities.add(craftItem);
    }

    public boolean checkDuplicateBuildable (BuildableEntity craftItem) {
        // Check if there the buildable entity exist already
        for(BuildableEntity buildableEntity : buildableEntities) {
            if(buildableEntity.getEntityType().contains(craftItem.getEntityType())) {
                return true;
            }
        }
        return false;
    }

    public void removecraftedEntity(String craftItemUsed) {
        for (StaticEntity usedItem: staticEntities) {
            if (usedItem.getEntityType().contains(craftItemUsed)) {
                staticEntities.remove((StaticEntity) usedItem);
                return;
            }
        }
    }

    public void removeItem(StaticEntity staticEntity) {
        staticEntities.remove(staticEntity);
    }

    public void removeBuildableEntity(BuildableEntity buildable) {
        buildableEntities.remove(buildable);
    }

    public int getCurrentInventorySpace() {
        return staticEntities.size();
    }

    public int getSpace() {
		return this.space;
	}

	public void setSpace(int space) {
		this.space = space;
	}

    public boolean hasItem(String entityType) {
        for (StaticEntity usedItem: staticEntities) {
            if (usedItem.getEntityType().contains(entityType)) 
                return true;
        }
        return false;
    }

    public void useInventory(StaticEntity EntityType) {
        for (StaticEntity usedItem: staticEntities) {
            if (usedItem.getEntityType().contains(EntityType.getEntityType())) {
                staticEntities.remove((StaticEntity) usedItem);
                return;
            }
        }
        return; 
    }

    public void useInventory(String EntityType) {
        for (StaticEntity usedItem: staticEntities) {
            if (usedItem.getEntityType().contains(EntityType)) {
                staticEntities.remove((StaticEntity) usedItem);
                return;
            }
        }
        return; 
    }

    public List<ItemResponse> getItemResponses() {
        List<ItemResponse> itemResponses = new ArrayList<>();
        for(StaticEntity staticEntity : staticEntities) {
            ItemResponse itemResponse = new ItemResponse(Integer.toString(staticEntity.getId()), staticEntity.getFilteredEntityType());
            itemResponses.add(itemResponse);
        }
        return itemResponses;
    }

    // public boolean haveSunstone() {
    //     for (StaticEntity se : getStaticEntities()) {
    //         if (se instanceof SunStone) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    public boolean checkForArrow() {
        for (StaticEntity se : getStaticEntities()) {
            if (se instanceof Arrows) {
                return true;
            }
        }
        return false;
    }

    public <T> boolean checkItem_inInventory(Class<T> className) {
        for (StaticEntity e : getStaticEntities()) {
            if (className.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

}
