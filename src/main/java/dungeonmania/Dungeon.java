package dungeonmania;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entity.*;
import dungeonmania.entity.blockingStates.UnblockState;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.goal.Goal;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.staticEntity.SwampTile;
import dungeonmania.staticEntity.Spawner;
import dungeonmania.staticEntity.collectableEntity.Bomb;
import dungeonmania.staticEntity.collectableEntity.HealthPotion;
import dungeonmania.staticEntity.collectableEntity.InvincibilityPotion;
import dungeonmania.staticEntity.collectableEntity.buildableEntity.BuildableEntity;
import dungeonmania.util.Position;

public class Dungeon {
    /**
     * @author Abi / Alan(Basic setup)
     * Comment from Alan: This is a basic setup, you prob need to add more variables / methods.
     */
    private String id;
    private String gameMode;
    private Goal goal;
    private String dungeonName;
    private int width;
    private int height;
    private Player player;
    private Spawner spawner;
    private Position playerEntryPos;

    private List <Entity> entities = new ArrayList<>();
    private List <EntityResponse> entitiesResponse = new ArrayList<>();

    public Dungeon(String id, String gameMode, String dungeonName) {
        this.id = id;
        this.gameMode = gameMode;
        this.dungeonName = dungeonName;
        this.spawner = new Spawner();
    }

    //method
    //getters and setters
    /**
     * find all entities in the Position p.
     * @author Alan
     * @pre p(x,y) : x > 0 & y > 0
     * @param p
     * @return null if the position has no entity (ground)
     */
    public List<Entity> getEntities(Position p) {
        return entities.stream().filter(e -> e != null && e.getCurr_position().equals(p)).collect(Collectors.toList());
    }

    public boolean usedValidItem(Entity entity) {
        return (entity == null || (!(entity instanceof Bomb) && !(entity instanceof HealthPotion) && !(entity instanceof InvincibilityPotion)));
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntitiesPosition() {
        return entities;
    }

    public SwampTile getSwamp(Position p) {
        List<Entity> entities = getEntities(p);
        for(Entity entity : entities) {
            if(entity.getEntityType().contains("swamp")) {
                return (SwampTile)entity;
            }
        }
        return null;
    }

    public Entity getEntity(Position p) {
        return entities.stream().filter(e -> e != null && e.getCurr_position().equals(p)).findAny().orElse(null);
    }

    public int calculateDistanceBetweenPositions(Position from, Position to) {
        int distWithSwamp = Position.calculateDistanceBetween(from, to);

        //add distance if it's swamp
        SwampTile swamp = getSwamp(to);
        
        //if there's swamp
        if(swamp != null) {
            distWithSwamp += swamp.getMovement_factor();
        }
        return distWithSwamp;
    }
    
    public List<Position> getUnblockablePositions() {
        List<Position> UnblockablePositions = new ArrayList<>();
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                Entity entity = getEntity(new Position(i, j));
                //when entity is a tile(null) or unblockable entity, add
                if(entity == null) {
                    UnblockablePositions.add(new Position(i, j));
                }
                else if(entity.getcurrBlockingState() instanceof UnblockState) {
                    UnblockablePositions.add(entity.getCurr_position());
                }
            }
        }
        return UnblockablePositions;
    }

    public boolean isUnblock(Position position) {
		Entity entity = getEntity(position);
        return (entity == null || entity.getcurrBlockingState() instanceof UnblockState);
	}

    public Entity getEntity(Integer id) {
        return entities.stream().filter(e -> e.getId() == id).findAny().orElse(null);
    }

    public DungeonResponse getDungeonResponse() {
        //get inventory
        Player player_entity = (Player) getEntity(0);
        List<ItemResponse> inventory = new ArrayList<>();
        if (player_entity != null) inventory = player_entity.getItemResponses();

        return new DungeonResponse(this.getId(), 
        this.getDungeonName(), this.getFilteredEntityType(), inventory,
        this.getBuildableEntities_strings(), this.getGoalString());
    }

    public List<String> getBuildableEntities_strings() {
        if ((Player) getEntity(0) != null) {
            Inventory player_bag = ((Player) getEntity(0)).getInventory();
            // Check if there are still buildable items
            BuildableEntity.createable(this, player_bag);
            return player_bag.getBuildableEntitiesStrings();
        }
        return new ArrayList<>();
    }

    public String getGoalString() {
        if (this.goal.checkComplete()) return "";
        return this.goal.constructGoalString();
    }

    public Entity getEntity(String entityType) {
        for(Entity entity : entities) {
            if(entity.getEntityType().contains(entityType)) return entity;
        }
        return null;
    }
    
    public List<String> getEntityList() {
        return entities.stream().map(e -> e.getEntityType()).collect(Collectors.toList());
    }

    /**
     * Generate a list of entities of a specified class.
     * @author Abigail
     * @param  className the class specified to be in the returning list
     *                   passed through as className.class
     * @return List with all entities of the specified className
     *         Returns an empty list if no such entities are found
     */
    public <T> List<T> getEntitiesByClass(Class<T> className) {
        List<T> listClassEntities = new ArrayList<T>();

        for (Entity e : this.entities) {
            if (className.isInstance(e)) listClassEntities.add((T) e);
        }
        return listClassEntities;
    }

    /**
     * add entity response
     * @param er
     * @return list of entity responses
     * @pre er != null
     * @post not null
     */
    public void addEntitiesResponse(EntityResponse er) {
        entitiesResponse.add(er);
    }

    public List<EntityResponse> getEntitiesResponse() {
        return entitiesResponse;
    }

    public List<EntityResponse> getFilteredEntityType() {
        List<EntityResponse> filteredEntityresponses = new ArrayList<>();
        for(EntityResponse ers : entitiesResponse) {
            //find its entity
            Entity entity = getEntity(Integer.parseInt(ers.getId()));

            EntityResponse new_er = new EntityResponse(ers.getId(), entity.getFilteredEntityType(), ers.getPosition(), ers.isInteractable());
            filteredEntityresponses.add(new_er);
        }
        return filteredEntityresponses;
    }

    public void addEntityResponses(List<EntityResponse> ers) {
        this.entitiesResponse = ers;
    }

    public EntityResponse findEntityResponse(String type) {
        for(EntityResponse er : entitiesResponse) {
            if(er.getType().equals(type)) {
                return er;
            }
        }
        return null;
    }

    public Entity findEntity(int id) {
        for(Entity er : entities) {
            if(er.getId() == id) {
                return er;
            }
        }
        return null;
    }

    public void updateEntityResponse(Entity er) {
        //get the new entity response
        EntityResponse new_er = new EntityResponse(Integer.toString(er.getId()), er.getEntityType(), er.getCurr_position(), er.IsInteractable());

        //get the index of the old response from the entitiesResponse based on the type
        int index_old_er = entitiesResponse.indexOf(
            entitiesResponse.stream().filter(
                e -> e.getId().equals(Integer.toString(er.getId()))
            ).findAny().orElse(null)
        );

        //set the entities response
        //when the entity is not removed. Otherwise, don't need to change the value as it's removed
        if(index_old_er >= 0) {
            entitiesResponse.set(index_old_er, new_er);
        }
    }

    public void updateEntitiesResponse() {
        for(Entity entity : entities) {
            updateEntityResponse(entity);
        }
    }


    public void deleteEntityResponse(Entity er) {
        //get the index of the old response from the entitiesResponse based on the type
        int index_old_er = entitiesResponse.indexOf(
            entitiesResponse.stream().filter(
                e -> e.getType().contains(er.getEntityType()) && 
                e.getId().contains(Integer.toString(er.getId()))
            ).findAny().orElse(null)
        );

        //delete the entity if it does exist in the dungeon
        if(index_old_er >= 0) {
            entitiesResponse.remove(index_old_er);

            //delete the enetiy from the dungeon
            // this.deleteEntity(er);
        }
    }

    public void deleteEntity(Entity er) {
        //get the index of the old response from the entitiesResponse based on the type
        int index_old_er = entities.indexOf(
            entities.stream().filter(
                e -> e.getEntityType().equals(er.getEntityType()) && e.getId() == er.getId()    //for primitive type, we use ==
            ).findAny().orElse(null)
        );

        //delete entity from the dungeon
        if(index_old_er != -1) entities.remove(index_old_er);
        //delete the response so the frontend can respond
        this.deleteEntityResponse(er);
    }

    public void skinsOrdering(Entity entity, Entity static_entity) {
        int high_priority = entitiesResponse.indexOf(
            entitiesResponse.stream().filter(
                e -> e.getId().equals(Integer.toString(entity.getId()))
            ).findAny().orElse(null)
        );

        int low_priority = entitiesResponse.indexOf(
            entitiesResponse.stream().filter(
                e -> e.getId().equals(Integer.toString(static_entity.getId()))
            ).findAny().orElse(null)
        );

        // Moving entity (+ Boulder) skins will appear above static entity
        // Skins already appear on top of static entities
        if ((high_priority - low_priority) > 0) return;
        
        Entity newEntity = findEntity(entity.getId());
        EntityResponse newEntityResponse = null;
        for(EntityResponse er : entitiesResponse) {
            if(er.getId().equals(Integer.toString(entity.getId()))) {
                newEntityResponse = er;
                entitiesResponse.remove(er);
                break;
            }
        }
        entitiesResponse.add(newEntityResponse);
        entities.add(newEntity);
        entities.remove(newEntity);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getDungeonName() {
		return this.dungeonName;
	}

	public void setDungeonName(String dungeonName) {
		this.dungeonName = dungeonName;
	}

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public String getId() {
		return this.id;
	}

    public List<Integer> getEntitiesID() {
        List<Integer> ids = new ArrayList<>();
        for(Entity entity : entities) {
            ids.add(entity.getId());
        }
        return ids;
    }

	public void setId(String id) {
		this.id = id;
	}

	public String getGameMode() {
		return this.gameMode;
	}

	public void setGameMode(String gameMode) {
		this.gameMode = gameMode;
	}

    public int getWidth() {
        //get the position of the entity who stands on the left most and the right most
        return findRightMostPosition() - findLeftMostPosition();
    }
    
    public int getHeight() {
        return findBottomMostPosition() - findTopMostPosition();
    }
    
    public int findLeftMostPosition() {
        int leftMostX = 10000;
        for(MovingEntity mv : getAllMovingEntities()) {
            if(mv.getCurr_position().getX() < leftMostX) {
                leftMostX = mv.getCurr_position().getX();
            }
        }
        if(leftMostX == 10000) throw new IllegalAccessError("The dungeon leftMostX has to be initialzed");
        return leftMostX;
    }
    
    public int findRightMostPosition() {
        int rightMostX = 0;
        for(MovingEntity mv : getAllMovingEntities()) {
            if(mv.getCurr_position().getY() > rightMostX) {
                rightMostX = mv.getCurr_position().getY();
            }
        }
        if(rightMostX == 0) throw new IllegalAccessError("The dungeon rightMostX has to be initialzed");
        return rightMostX;
    }
    
    public int findBottomMostPosition() {
        int bottomMostY = 10000;
        for(MovingEntity mv : getAllMovingEntities()) {
            if(mv.getCurr_position().getY() < bottomMostY) {
                bottomMostY = mv.getCurr_position().getY();
            }
        }
        if(bottomMostY == 10000) throw new IllegalAccessError("The dungeon bottomMostY has to be initialzed");
        return bottomMostY;
    }
    
    public int findTopMostPosition() {
        int topMostY = 0;
        for(MovingEntity mv : getAllMovingEntities()) {
            if(mv.getCurr_position().getY() > topMostY) {
                topMostY = mv.getCurr_position().getY();
            }
        }
        if(topMostY == 0) throw new IllegalAccessError("The dungeon topMostY has to be initialzed");
        return topMostY;
    }
    
    public List<MovingEntity> getAllMovingEntities() {
        List<MovingEntity> mvList = new ArrayList<>();
        for(Entity entity : this.getEntities()) {
            if(entity instanceof MovingEntity)
                mvList.add((MovingEntity) entity);
        }
        return mvList;
    }
    

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

    public Goal getGoals() {
		return this.goal;
	}

	public void setGoal(Goal goal) {
		this.goal = goal;
	}

    public void spawnEntities() {
        spawner.spawn(this);
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public void setPlayerEntryPos(Position newPos) {
        this.playerEntryPos = newPos;
    }

    public Position getPlayerEntryPos() {
        return this.playerEntryPos;
    }

    public int uniqueEntityID() {
        int newEntityNo = this.entities.size() + 1;
        while (true) {
            if (this.getEntity(newEntityNo) == null) break;
            newEntityNo += 1;
        }
        return newEntityNo;
    }

    public void adjustDifficulty(MovingEntity entity) {
        if (getGameMode().equals("Peaceful")) {
            entity.setAttackDamage(0);
        }
    }

}
