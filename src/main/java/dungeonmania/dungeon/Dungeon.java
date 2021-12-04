package dungeonmania.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import dungeonmania.entity.*;
import dungeonmania.entity.blockingStates.UnblockState;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.movingEntity.Player;
import dungeonmania.goal.Goal;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.SwampTile;
import dungeonmania.staticEntity.Spawner;
import dungeonmania.staticEntity.collectableEntity.Bomb;
import dungeonmania.staticEntity.collectableEntity.HealthPotion;
import dungeonmania.staticEntity.collectableEntity.InvincibilityPotion;
import dungeonmania.util.Position;

public class Dungeon {
    /**
     * @author Abi / Alan(Basic setup)
     */
    private String id;
    private String gameMode;
    private Goal goal;
    private JsonObject goalJson;
    private String dungeonName;
    private int width;
    private int height;
    private Player player;  //when the player travles back, this is the currPlayer
    private Spawner spawner;
    private int tick;           
    private Position playerEntryPos;
    // private List<String> buildableEntities;

    private Bomb bomb = null;

    private List <Entity> entities = new ArrayList<>();
    private List <EntityResponse> entitiesResponse = new ArrayList<>();

    public Dungeon(String id, String gameMode, String dungeonName) {
        this.id = id;
        this.gameMode = gameMode;
        this.dungeonName = dungeonName;
        this.tick = 0;  //Assumption: Before player moves, it's at tick 0. --Alan
        this.player = (Player) getEntity(0);
        this.spawner = new Spawner();
    }

    public Dungeon(String id, String gameMode, String dungeonName, Goal goal) {
        this.id = id;
        this.gameMode = gameMode;
        this.dungeonName = dungeonName;
        this.tick = 0;  //Assumption: Before player moves, it's at tick 1. --Alan
        this.spawner = new Spawner();
        this.player = (Player) getEntity(0);
        this.goal = goal;
    }

    //deep copy
    public Dungeon(Dungeon that, Goal goal) {
        this(that.getId(), that.getGameMode(), that.getDungeonName(), that.goal);
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
        return entities.stream().filter(e -> e != null && e.getCurr_position() != null && e.getCurr_position().equals(p)).collect(Collectors.toList());
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

    /**
     * tick has to be only one increment of the previous tick
     * @param newTick
     */
    public void updateTick() {
        tick ++;
    }

    public int getTick() {
        return this.tick;
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

    public JsonObject getGoalJson() {
		return this.goalJson;
	}

	public void setGoalJson(JsonObject goalJson) {
		this.goalJson = goalJson;
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
        //TODO: Width and height should be dynamically changining, needs to figure out the width and height every ticks based on which entities are furthest away.
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
        // System.out.println(entity.getcurrBlockingState() instanceof UnblockState);
        return (entity == null || entity.getcurrBlockingState() instanceof UnblockState);
	}

    public Entity getEntity(Integer id) {
        return entities.stream().filter(e -> e.getId() == id).findAny().orElse(null);
    }

    public DungeonResponse getDungeonResponse() {
        //get inventory
        Player player_entity = (Player) getEntity(0);
        Inventory inventory = player_entity.getInventory();

        // System.out.println("dungeon.getDungeonResponse --> this.getId() = " + this.getId() + 
        // ", this.getDungeonName() = " + this.getDungeonName() + ", this.getFilteredEntityType() = " + this.getFilteredEntityType() +
        // ", inventory.getItemResponses() = " + inventory.getItemResponses() + ", this.getBuildableEntities_strings() = " + 
        // this.getBuildableEntities_strings() + ", this.getGoalString() = " + this.getGoalString());

        return new DungeonResponse(this.getId(), 
        this.getDungeonName(), this.getFilteredEntityType(), inventory.getItemResponses(),
        this.getBuildableEntities_strings(), this.getGoalString());
    }

    public List<String> getBuildableEntities_strings() {
        if ((Player) getEntity(0) != null) {
            Inventory player_bag = ((Player) getEntity(0)).getInventory();
            return player_bag.getBuildableEntitiesStrings();
        }
        return new ArrayList<>();
    }

    public String getGoalString() {
        // System.out.println("Dungeon.getGoalString --> goal == null ? " + goal==null);
        if (this.goal.checkComplete()) {
            return "";
        }
        return this.goal.constructGoalString();
    }

    public void setGoal(Goal goal) {
        this.player = (Player) getEntity(0);
        this.goal = goal;
    }

    public Goal getGoal(){
        return goal;
    }

    public Entity getEntity(String entityType) {
        for(Entity entity : entities) {
            // if(!(entity.getEntityType().contains("wall")))
                // System.out.println("Dungeon/getEntity --> entityTypeGiven = " + entityType + " entityTypeSearching = " + entity.getEntityType());
            if(entity.getEntityType().contains(entityType)) {
                // if(entity.getEntityType().contains("potion"))
                    // System.out.println("Dungeon/getEntity --> returned entity = " + entity.getEntityType());
                return entity;
            }
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
            if (className.isInstance(e)) {
                listClassEntities.add((T)e);
            }
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
        // System.out.println("Dungeon.entitiesResponse --> entitiesResponse" + entitiesResponse);

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

        //set the entities response when the entity is not removed. Otherwise, don't need to change the value as it's removed
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
        // System.out.println("dungeon/deleteEntity index_old_er = " + index_old_er);
        if(index_old_er != -1)
            entities.remove(index_old_er);

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

    /**
     * the current player becomes older_player
     */
    // public void updateCurrPlayer(Player currPlayer) {
    //     //below is the curr player
    //     player = (Player)this.getEntity("player");

    //     //add the older_player to the dungeon
    //     addEntityToDungeon((Entity)player, "older_player", 1);

    //     //replace the curr player
    //     this.deleteEntity(player);
    //     this.deleteEntityResponse(player);

    //     //add the curr player to the dungeon
    //     addEntityToDungeon((Entity)currPlayer, "player", 0);

    //     //update the dungeon curr player
    //     this.player = currPlayer;

    //     assert(this.getEntity("player") != null);
    //     assert(this.getEntity("older_player") != null);
    // }

    // public void addEntityToDungeon(Entity entity, String entityType, int EntityID) {
    //     //create new entity
    //     EntityCreator entityCreator = new EntityCreator();
    //     Entity newEntity = entityCreator.createEntity(EntityID, entityType, 
    //     Integer.toString(entity.getCurr_position().getX()), 
    //     Integer.toString(entity.getCurr_position().getY()), entity.getExtraVal(), entity.getMovement_factor());

    //     this.addEntity(newEntity);

    //     //create entity responses
    //     //get the new entity response
    //     EntityResponse new_er = new EntityResponse(Integer.toString(EntityID), entityType, newEntity.getCurr_position(), newEntity.IsInteractable());

    //     this.addEntitiesResponse(new_er);
    // }

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
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

    public Goal getGoals() {
		return this.goal;
	}

	public void setGoals(Goal goal) {
		this.goal = goal;
	}

    // public void spawnEntities() {
    //     spawner.spawn(this);
    // }

    public Spawner getSpawner() {
        return spawner;
    }

    public void setPlayerEntryPos(Position newPos) {
        this.playerEntryPos = newPos;
    }

    public Position getPlayerEntryPos() {
        return this.playerEntryPos;
    }

    public Bomb getBomb() {
        return this.bomb;
    }

    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }

    // public List<String> getBuildableEntities() {
	// 	return this.buildableEntities;
	// }

	// public void setBuildableEntities(List<String> buildableEntities) {
	// 	this.buildableEntities = buildableEntities;
	// }


}
