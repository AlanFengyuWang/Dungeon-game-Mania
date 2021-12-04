package dungeonmania;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.gameMode.GameMode;
import dungeonmania.gameMode.HardMode;
import dungeonmania.gameMode.PeacefulMode;
import dungeonmania.goal.*;
import dungeonmania.dungeonGenerator.MazeGenerator;
import dungeonmania.entity.Entity;
import dungeonmania.entity.PlayerInteract;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.staticEntity.FloorSwitch;
import dungeonmania.staticEntity.*;
import dungeonmania.staticEntity.collectableEntity.Bomb;
import dungeonmania.staticEntity.collectableEntity.buildableEntity.*;
import dungeonmania.staticEntity.logic.WireSwitchChecker;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.JSONfile;
import dungeonmania.util.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class DungeonManiaController {
    //Global variables: Dungeons
    private Dungeon curr_dungeon = null;
    private int gameID = 0;  //temporarily use int easier to count, we update it everytime when we store a new game, later when we store it to dungeon we need to convert it to String. (Alan)
    List<Position> switch_pos = new ArrayList<>();
    List<Entity> temp_two = new ArrayList<>();
    GameMode game_mode = null;
    public DungeonManiaController() {
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("Standard", "Peaceful", "Hard");
    }
    
    /**
     * /dungeons
     * 
     * Done for you.
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * 
     * @param dungeonName
     * @param gameMode
     * @return
     * @throws IllegalArgumentException
     * @pre dungeonName: It does not contain .json
     */
    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        /**
        * @author: David / Alan
        * Date: 28/10/2021
        */
        List<String> existingDungeons = dungeons();

        // ========== Exceptions ==========
        //Check if the dungeon modes and name are valid
        if (!getGameModes().contains(gameMode)) throw new IllegalArgumentException("Not a valid game mode");
        if (!existingDungeons.contains(dungeonName)) throw new IllegalArgumentException("Dungeon does not exist");

        //create game ID
        String d_id = "dungeon_" + gameMode + "_" + Integer.toString(gameID);
        Dungeon dungeon = new Dungeon(d_id, gameMode, dungeonName);

        //try to load dungeon 
        String file = null;
        try {
            file = FileLoader.loadResourceFile("/dungeons/" + dungeonName + ".json");
        } catch (IOException e) {
            System.out.println("No such dungeon map");
        } 
        catch (IllegalArgumentException e) {
            System.out.println("No such dungeon map");
        }

        //convert Json string to json object
        JsonObject convertedObject = JsonParser.parseString(file).getAsJsonObject();

        // Load JSON file
        JSONfile.loadJSONfile(dungeon, convertedObject);

        JsonObject goal = convertedObject.getAsJsonObject("goal-condition");
        dungeon.setGoal(GoalFactory.createGoal(goal, dungeon));

        //update the curr dungeon for later references
        curr_dungeon = dungeon;

        if (gameMode.equals("Peaceful")) {
            game_mode = new PeacefulMode(curr_dungeon);
            game_mode.setEntityStats();
        } else if (gameMode.equals("Hard")) {
            game_mode = new HardMode(curr_dungeon);
            game_mode.setEntityStats();
        }

        // ========== Link all switches with wires ==========
        WireSwitchChecker.activeWires(curr_dungeon);
        // ========== Check all logic switches ==========
        WireSwitchChecker.triggerSwitches(curr_dungeon);

        // return new_dungeon;
        return curr_dungeon.getDungeonResponse();
    }
    
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        /**
         * returns same DungeonResponse with no changes but creates a json file with all stored entities
         * @author: David
         * @date: 30/10/2021
         */

        gameID++;
        // Save to Json file
        // JSONfile.saveJSONfile(curr_dungeon, name);
        JSONfile.saveJSONfile(curr_dungeon, name);
        
        return curr_dungeon.getDungeonResponse();
    }

    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        // Load saved json file
        /**
         * @author Alan and David
         * @date 30.10.2021
         */
        List<String> savedExist = allGames();

        if(!savedExist.contains(name)) throw new IllegalArgumentException("No such save file");
        String file = null;
        try {
            file = FileLoader.loadResourceFile("/savedDungeons/" + name + ".json");
        } catch (IOException e) {
            System.out.println("No such save file");
        } catch (IllegalArgumentException e) {
            System.out.println("No such save file");
        }
        
        //convert Json string to json object
        JsonObject convertedObject = JsonParser.parseString(file).getAsJsonObject();

        String gameMode = convertedObject.getAsJsonObject().get("gameMode").toString();

        Dungeon dungeon = new Dungeon(name, gameMode, name);
        // Load JSON file
        JSONfile.loadJSONfile(dungeon, convertedObject);

        JsonObject goal = convertedObject.getAsJsonObject("goal-condition");
        dungeon.setGoal(GoalFactory.createGoal(goal, dungeon));

        // ========== Link all switches with wires ==========
        WireSwitchChecker.activeWires(dungeon);
        // ========== Check all logic switches ==========
        WireSwitchChecker.triggerSwitches(dungeon);

        // //update the curr dungeon for later references
        curr_dungeon = dungeon;

        return curr_dungeon.getDungeonResponse();
    }

    public List<String> allGames() {
        /**
         * returns all saved games which are stored in a folder called "savedDungeons"
         * @author: David
         * @date: 30/10/2021
         */
        try {
            return FileLoader.listFileNamesInResourceDirectory("/savedDungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public DungeonResponse tick(String itemUsed, Direction movementDirection) throws IllegalArgumentException, InvalidActionException {
        /**
         * return a new dungeonResponse after tick
         * @author: Alan(player, other moving entities) & William(bomb)
         * @date: 30/10/2021
         */
        // ========== Exception ==========
        if(!(movementDirection.equals(Direction.NONE)) && itemUsed != null) {
            throw new InvalidActionException("The player cannot uses item and move at the same time");
        }

        //get player_entity
        Player player_entity = (Player) curr_dungeon.getEntity("player");
        if(player_entity == null) 
            throw new IllegalAccessError("Player does not exist");
        
        //move player
        if(!(movementDirection.equals(Direction.NONE)))
            player_entity.doMove(movementDirection, curr_dungeon);

        //consume item
        if(itemUsed != null) {
            if(!player_entity.hasItem(Integer.parseInt(itemUsed))) throw new InvalidActionException("The player has no such item");

            Entity itemUsing = player_entity.getInventory().getStaticEntity(Integer.parseInt(itemUsed));
            if(!(itemUsing instanceof TickItemUse)) throw new IllegalArgumentException("The item used is not a bomb, health potion, invincibility potion or an invisibility potion, or null");

            //use it now
            TickItemUse tickItemUse = (TickItemUse) itemUsing;
            tickItemUse.doItemUse(player_entity, curr_dungeon);
        }

        //update the player entity
        curr_dungeon.updateEntityResponse(player_entity);

        //move all moveable entities
        Iterator<Integer> entityID = curr_dungeon.getEntitiesID().iterator();

        // ====== Code to trigger the bomb ======
        List<Entity> bombsSet = curr_dungeon.getEntities().stream().filter(e -> e instanceof Bomb).collect(Collectors.toList());
        for (Entity entity: bombsSet) {
            Bomb bombs = (Bomb) entity;
            if (bombs.isBombSet()) {
                // Check if there is an adjacent triggered switch
                List<FloorSwitch> switchesAdjacent = WireSwitchChecker.getAdjacentSwitches(curr_dungeon, bombs);
                Boolean willExplode = switchesAdjacent.stream().anyMatch(e -> e.getTriggered() == true);
                if (willExplode) bombs.explode(curr_dungeon);
            }
        }
        // ====== end of bomb trigger code ======

        while(entityID.hasNext()) {
            //get the entity based on id
            Entity entity = curr_dungeon.getEntity(entityID.next());
            
            //if it's not player
            if (!(entity instanceof Player) && entity instanceof MovingEntity) {
                MovingEntity mv = (MovingEntity) entity;
                mv.doMove(player_entity, curr_dungeon);;

                //update other moving entities
                curr_dungeon.updateEntityResponse(entity);
            }
        }
        
        // ========== Spawns new enemies ==========
        curr_dungeon.spawnEntities();

        // ========== Check all logic switches ==========
        WireSwitchChecker.triggerSwitches(curr_dungeon);

        return curr_dungeon.getDungeonResponse();
    }

    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        /**
         * @author Alan
         */
        //Interacting with mercenary
        //get player
        Entity player = curr_dungeon.getEntity("player");

        //try both entityID as int and as string
        Entity interactSubject = curr_dungeon.getEntity(entityId);
        //try both entityID as int it as string
        if(interactSubject == null) {
            interactSubject = curr_dungeon.getEntity(Integer.parseInt(entityId));
            if(interactSubject == null)
                throw new IllegalArgumentException("Entity id " + entityId + " is invalid");
        }

        //if it's more than 2 cardinal tiles away, throw error
        if(!Position.isAdjacent(player.getCurr_position(), interactSubject.getCurr_position(), 2)) {
            throw new InvalidActionException("More than 2 cardinal tiles away");
        }

        //change the mercenary to ally or destory the zombie spawner
        //
        if(interactSubject instanceof PlayerInteract) {
            PlayerInteract playerInteractSubject = (PlayerInteract) interactSubject;
            playerInteractSubject.doPlayerInteract((Player)player, curr_dungeon);
        }
        return curr_dungeon.getDungeonResponse();
    }

    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        /**
         * @author David
         * @date 1.11.2021
         */
        // Check for exceptions
        if (!curr_dungeon.getBuildableEntities_strings().contains(buildable)) {
            if (BuildableEntity.allBuildableEntities().contains(buildable)) {
                throw new InvalidActionException("Player does not have sufficient items to craft the buildable");
            }
            else throw new IllegalArgumentException("Buildables can only be bow or shield or sceptre or midnight armour");
        }
        
        Player player_entity = (Player) curr_dungeon.getEntity("player");
        // Craft buildable entity
        BuildableEntity.craft(curr_dungeon, player_entity, buildable);

        return curr_dungeon.getDungeonResponse();
    }

    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String gameMode) 
        throws IllegalArgumentException {
        /**
         * @author Abigail
         */
        MazeGenerator mg = new MazeGenerator();
        mg.RandomizedPrims(50, 50, new Position(xStart, yStart), new Position(xEnd, yEnd));

        String dungeonName = JSONfile.createMazeJSONFile(mg);
        return newGame(dungeonName, gameMode);
    }


    public DungeonResponse rewind(int ticks) {
        /**
        * @author Alan
        */
        return null;
    }
}