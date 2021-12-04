package dungeonmania.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import dungeonmania.Dungeon;
import dungeonmania.dungeonGenerator.MazeGenerator;
import dungeonmania.entity.Entity;
import dungeonmania.entity.EntityCreator;
import dungeonmania.equipAndCollectablemodel.DurableEquipment;
import dungeonmania.movingEntity.Inventory;
import dungeonmania.movingEntity.MovingEntity;
import dungeonmania.movingEntity.Player;
import dungeonmania.movingEntity.Spider;
import dungeonmania.movingEntity.moveBehaviour.SpiderMoveBehavior;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.staticEntity.*;
import dungeonmania.staticEntity.collectableEntity.*;
import dungeonmania.staticEntity.collectableEntity.buildableEntity.BuildableEntity;
import dungeonmania.staticEntity.logic.Logic;
import dungeonmania.staticEntity.logic.LogicChecker;

/**
 * JSON file handlers
 * @author: David
 * @date: 06/11/2021
 */
public final class JSONfile {
    private static JsonElement goals;
    
    public static void loadJSONfile(Dungeon dungeon, JsonObject convertedObject) {
        final JsonArray entities = convertedObject.getAsJsonArray("entities");      //get json array so we can iterate 

        // If width and height given, initialize them
        setDungeonSize(dungeon, convertedObject);

        //player starts at 0, entities start at 1
        int entity_id = 1;
        for(JsonElement element : entities) {
            //create entities
            EntityCreator entityCreator = new EntityCreator();
            String entityType = element.getAsJsonObject().get("type").toString();

            //if it's type swamp, need to get movement factor
            int movement_factor = getMovementFactor(element, entityType);

            entityType = convertStringToEntityName(entityType);
            Entity entity = entityCreator.createEntity(entity_id, entityType, element.getAsJsonObject().get("x").toString(), element.getAsJsonObject().get("y").toString(), movement_factor);
            
            // Only for guranteed extra variables
            intializeExtraVar(entity, element);

            // Set logic variables
            if (entity instanceof LogicChecker) setLogic(element, (LogicChecker) entity);
            
            // ========== From save game ==========
            // Set current stats if from saved game
            if (entity instanceof MovingEntity) setStats(element, (MovingEntity) entity);
            // Load spawn timer from zombie spawner
            if (entity instanceof ZombieToastSpawner) zombieSpawns((ZombieToastSpawner) entity, element);
            // Spider path
            if (entity instanceof Spider) spiderPath((Spider) entity, element);
            // Durability
            if (entity instanceof DurableEquipment) setDurability(element, (DurableEquipment) entity);
            // Bombs placed
            if (entity instanceof Bomb) isBombsPlaced(element, (Bomb) entity);

            //get entityResponse
            EntityResponse er = null;
            if(entity != null) {
                er = new EntityResponse(Integer.toString(entity.getId()), entity.getFilteredEntityType(), entity.getCurr_position(), entity.IsInteractable());
                dungeon.addEntity(entity);
                dungeon.addEntitiesResponse(er);
            }
            entity_id++;
        }

        // Set spawn timer for enemies
        dungeonSpawns(dungeon, convertedObject);
        dungeon.setPlayer((Player) dungeon.getEntity(0));

        // Get player's entry position
        getEntryPos(dungeon, convertedObject);

        // Get goals
        goals = convertedObject.getAsJsonObject().get("goal-condition");

        // Only works for saved Json files (from DungeonManiaController.saveGame)
        getInventory(dungeon, convertedObject);
    }

    public static void getEntryPos(Dungeon dungeon, JsonObject convertedObject) {
        // Retrive entry position from file
        try {
            JsonElement entry = convertedObject.getAsJsonObject().get("entry");      //get json array so we can iterate 
            Position entryPos = null;
            int x = Integer.valueOf(entry.getAsJsonObject().get("x").toString());
            int y = Integer.valueOf(entry.getAsJsonObject().get("y").toString());
            entryPos = new Position(x,y);
            // playerEntryPos = entryPos;
            dungeon.setPlayerEntryPos(entryPos);
        } catch (NullPointerException e) {
            // If there is no entry position, make player current position entry
            Position entry = dungeon.getPlayer().getCurr_position();
            // playerEntryPos = entry;
            dungeon.setPlayerEntryPos(entry);
            }
    }

    public static void getInventory(Dungeon dungeon, JsonObject convertedObject) {
        try {
            final JsonArray inventoryItems = convertedObject.getAsJsonArray("inventory");      //get json array so we can iterate 
            loadPlayerInventory(inventoryItems, dungeon);
        } catch (NullPointerException e) {
            return;
        }
    }

    public static void saveJSONfile(Dungeon curr_dungeon, String name) {
        // ========== Creating a JSONObject object ==========
        JSONObject jsonObject = new JSONObject();

        // ========== Retrieve all entity information ==========
        List<JSONObject> savedEntities = new ArrayList<>();

        for(Entity each: curr_dungeon.getEntities()) {
            JSONObject newJsonObject = new JSONObject();
            newJsonObject.put("x",each.getCurr_position().getX());
            newJsonObject.put("y",each.getCurr_position().getY());
            newJsonObject.put("type",each.getFilteredEntityType());

            if (each.getFilteredEntityType().equals("door")) {
                newJsonObject.put("key", ((Door) each).getKeyNo());
            }
            else if (each.getFilteredEntityType().contains("key")) {
                newJsonObject.put("key", ((Key) each).getKeyNo());
            }
            else if (each.getFilteredEntityType().contains("portal")) {
                newJsonObject.put("colour", ((Portal) each).getColour());
            }

            if (each instanceof Spider) {
                SpiderMoveBehavior spiderMovement = (SpiderMoveBehavior) ((Spider) each).getMovingBehavior();
                newJsonObject.put("path", spiderMovement.getPathIndex());
                newJsonObject.put("clockwise", spiderMovement.isClockwise());
                newJsonObject.put("spawnPositionX", spiderMovement.getSpawnPosition().getX());
                newJsonObject.put("spawnPositionY", spiderMovement.getSpawnPosition().getY());
            }

            if (each instanceof MovingEntity) {
                newJsonObject.put("health", ((MovingEntity) each).getHealth());
                newJsonObject.put("attack", ((MovingEntity) each).getAttackDamage());
            }
            if (each instanceof ZombieToastSpawner) {
                newJsonObject.put("zombieSpawn", ((ZombieToastSpawner) each).getTickCounter());
            }
            if (each instanceof Bomb) placedBombs(newJsonObject, (Bomb) each);

            // Set logic variables
            if (each instanceof LogicChecker) saveLogic(newJsonObject, (LogicChecker) each);
            
            savedEntities.add(newJsonObject);
        }
        jsonObject.put("entities", savedEntities);

        Player player_entity = (Player) curr_dungeon.getEntity("player");
        Inventory player_bag = player_entity.getInventory();

        List<JSONObject> savedInventory = new ArrayList<>();
        for(StaticEntity each: player_bag.getStaticEntities()) {
            JSONObject newJsonObject = new JSONObject();
            newJsonObject.put("id",each.getId());
            newJsonObject.put("type",each.getFilteredEntityType());
            // ========== Durability ==========
            if (each instanceof DurableEquipment) {
                newJsonObject.put("durability", ((DurableEquipment) each).getDurability());
            }
            if (each.getFilteredEntityType() == "key") {
                newJsonObject.put("key", ((Key) each).getKeyNo());
            }
            savedInventory.add(newJsonObject);
        }
        jsonObject.put("inventory", savedInventory);

        // ========== Entry Position ==========
        JSONObject position = new JSONObject();
        Position playerEntryPos = curr_dungeon.getPlayerEntryPos();
        position.put("x", playerEntryPos.getX());
        position.put("y", playerEntryPos.getX());
        jsonObject.put("entry", position);
        
        // ========== Other variables ==========
        String gameMode = curr_dungeon.getGameMode();
        gameMode = gameMode.replaceAll("\"", "");
        JSONObject obj = new JSONObject(goals.toString());
        
        jsonObject.put("goal-condition", obj);
        jsonObject.put("width", curr_dungeon.getWidth());
        jsonObject.put("height", curr_dungeon.getHeight());
        jsonObject.put("gameMode", gameMode);
        jsonObject.put("dungeonName", curr_dungeon.getDungeonName());


        // ========== Spawns ==========
        JSONObject spawnCounter = new JSONObject();
        Spawner spawner = curr_dungeon.getSpawner();
        spawnCounter.put("spiderSpawn", spawner.getSpiderSpawn());
        spawnCounter.put("mercenarySpawn", spawner.getMercenarySpawn());
        jsonObject.put("spawners", spawnCounter);


        // ========== Create absolute file path ==========
        String filePath = new File("").getAbsolutePath();
        String fileSeparator = FileSystems.getDefault().getSeparator();
        filePath = filePath.concat(fileSeparator + "src" + fileSeparator + "main" + fileSeparator + "resources" + fileSeparator + "savedDungeons" + fileSeparator + name + ".json");

        // ========== Create a new json file ==========
        try {
            File myObj = new File(filePath);
            if (myObj.createNewFile()) {
            // Write the JSON object on the file
            FileWriter file;
            try {
                file = new FileWriter(filePath);
                file.write(jsonObject.toString());
                file.flush();
                file.close();
            } catch (IOException e) {
                System.out.println("An error occurred when writing to save file.");
            }
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred when creating save file.");
        }
    }

    // Return player's inventory from saved game
    public static void loadPlayerInventory(JsonArray inventoryItems, Dungeon dungeon) {
        Player player_entity = (Player) dungeon.getEntity("player");
        Inventory player_bag = player_entity.getInventory();
        
        for(JsonElement element : inventoryItems) {
            String itemType = element.getAsJsonObject().get("type").toString().substring(1, element.getAsJsonObject().get("type").toString().length() -1);
            String itemID = element.getAsJsonObject().get("id").toString();

            itemType = convertStringToEntityName(itemType);

            EntityCreator entityCreator = new EntityCreator();
            Entity entity = entityCreator.createEntity(Integer.valueOf(itemID), itemType, "-1", "-1", 0);  // Create entity, Position(-1,-1) for inventory; does not get pass onto entityResponse
            intializeExtraVar(entity, element);

            player_bag.storeToInventory((StaticEntity) entity);
        }

        BuildableEntity.createable(dungeon, player_bag);
    }

    // Convert string from JSON file to readable entity names
    public static String convertStringToEntityName(String entityType) {
        entityType = entityType.replaceAll("\"", "");
		// If there is an underscore with number, chop it off
        entityType = entityType.replaceAll("[0-9]","");
		if (entityType.endsWith("_")) entityType = entityType.substring(0, entityType.length() -1);

        return entityType;
    }

    // Retrieve extra variables
    // Such as door and key; key variable, portal; colour
    public static void intializeExtraVar(Entity entity, JsonElement element) {
        try {
            if (entity instanceof Door) {
                int keyNo = Integer.parseInt(element.getAsJsonObject().get("key").toString());
                ((Door) entity).setKeyNo(keyNo);
            } 
            else if (entity instanceof Key) {
                int keyNo = Integer.parseInt(element.getAsJsonObject().get("key").toString());
                ((Key) entity).setKeyNo(keyNo);
            }  
            else if (entity instanceof Portal) {
                String colour = element.getAsJsonObject().get("colour").toString();
                ((Portal) entity).setColour(colour);
            }
        } catch (NullPointerException e) {
            return;
        }
    }

    public static int getMovementFactor(JsonElement element, String entityType) {
        int movement_factor = 0;
        if(entityType.contains("swamp")) {
            String moveFactor = element.getAsJsonObject().get("movement_factor").toString();
            movement_factor = Integer.parseInt(moveFactor);
        }
        return movement_factor;
    }

    public static String createMazeJSONFile(MazeGenerator mg) {
        JSONObject dungeonJSON = new JSONObject();

        // ========== Width & Height ==========
        dungeonJSON.put("width", mg.getWidth());
        dungeonJSON.put("height", mg.getHeight());

        // ========== Goal ==========
        JSONObject goalJSON = new JSONObject();
        goalJSON.put("goal", "exit");
        dungeonJSON.put("goal-condition", goalJSON);

        // ========== Entry & Exit ==========
        dungeonJSON.append("entities", createEntityJSON(mg.getStart().getX(), mg.getStart().getY(), "player"));
        dungeonJSON.append("entities", createEntityJSON(mg.getEnd().getX(), mg.getEnd().getY(), "exit"));
    
        // ========== Walls (maze) ==========
        boolean[][] maze = mg.getMaze();

        for (int h = 0; h < mg.getHeight(); h++) {
            for (int w = 0; w < mg.getWidth(); w++) {
                // if position in maze is false, add a wall entity to the JSON object
                if (!maze[h][w]) {
                    dungeonJSON.append("entities", createEntityJSON(w, h, "wall"));
                }
            }
        }

        // write dungeon json object into file
        // ========== Create absolute file path ==========
        String fileName = "mazeLastGenerated";
        String filePath = new File("").getAbsolutePath();
        String fileSeparator = FileSystems.getDefault().getSeparator();
        filePath = filePath.concat(fileSeparator + "src" + fileSeparator + "main" + fileSeparator + "resources" + fileSeparator + "dungeons" + fileSeparator + "mazeLastGenerated" + ".json");

        // ========== Overwrite existing mazeLastGenerated.json ==========

        FileWriter file;
        try {
            file = new FileWriter(filePath, false);
            file.write(dungeonJSON.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.println("An error occurred when writing new maze dungeon.");
            e.printStackTrace();
        }

        return fileName;
    }

    public static JSONObject createEntityJSON(int x, int y, String type) {
        JSONObject entityJSON = new JSONObject();
        entityJSON.put("x", x);
        entityJSON.put("y", y);
        entityJSON.put("type", type);
        return entityJSON;
    }

    public static void setStats(JsonElement element, MovingEntity entity) {
        try {
            int health = Integer.valueOf(element.getAsJsonObject().get("health").toString());
            int attack = Integer.valueOf(element.getAsJsonObject().get("attack").toString());

            entity.setHealth(health);
            entity.setAttackDamage(attack);
        } catch (NullPointerException e) {
            return;
        }
    }

    public static void dungeonSpawns(Dungeon dungeon, JsonObject convertedObject) {
        Spawner spawner = dungeon.getSpawner();
        try {
            JsonElement spawners = convertedObject.getAsJsonObject().get("spawners");
            int spiderSpawn = Integer.valueOf(spawners.getAsJsonObject().get("spiderSpawn").toString());
            int mercenarySpawn = Integer.valueOf(spawners.getAsJsonObject().get("mercenarySpawn").toString());
            spawner.setSpiderSpawn(spiderSpawn);
            spawner.setMercenarySpawn(mercenarySpawn);
        } catch (NullPointerException e) {
            return;
        }
    }

    public static void zombieSpawns(ZombieToastSpawner entity, JsonElement element) {
        try {
            int zombieSpawn = Integer.valueOf(element.getAsJsonObject().get("zombieSpawn").toString());
            entity.setTickCounter(zombieSpawn);
        } catch (NullPointerException e) {
            return;
        }
    }

    public static void spiderPath(Spider entity, JsonElement element) {
        try {
            int path = Integer.valueOf(element.getAsJsonObject().get("path").toString());
            ((SpiderMoveBehavior) entity.getMovingBehavior()).setPathIndex(path);
            boolean clockwise = Boolean.parseBoolean(element.getAsJsonObject().get("clockwise").toString());
            int spawnPositionX = Integer.valueOf(element.getAsJsonObject().get("spawnPositionX").toString());
            int spawnPositionY = Integer.valueOf(element.getAsJsonObject().get("spawnPositionY").toString());
            Position spawnPosition = new Position(spawnPositionX, spawnPositionY);
            ((SpiderMoveBehavior) entity.getMovingBehavior()).setClockwise(clockwise);
            ((SpiderMoveBehavior) entity.getMovingBehavior()).setSpawnPosition(spawnPosition);
        } catch (NullPointerException e) {
            return;
        }
    }

    public static void setDurability(JsonElement element, DurableEquipment equipment) {
        try {
            int durability = Integer.valueOf(element.getAsJsonObject().get("durability").toString());
            equipment.setDurability(durability);
        } catch (NullPointerException e) {
            return;
        }
    }

    public static void setLogic(JsonElement element, LogicChecker entity) {
        try {
            String logic = element.getAsJsonObject().get("logic").toString();
            logic = logic.replaceAll("\"", "");
            entity.setLogic(logic);
        } catch (NullPointerException e) {
            return;
        }
    }

    public static void saveLogic(JSONObject newJsonObject, LogicChecker entity) {
        try {
            Logic logic = entity.getLogic();
            newJsonObject.put("logic", logic.getLogicString());
        } catch (NullPointerException e) {
            return;
        }
    }

    public static void placedBombs(JSONObject newJsonObject, Bomb entity) {
        try {
            Boolean bombSet = entity.isBombSet();
            newJsonObject.put("bombSet", bombSet);
        } catch (NullPointerException e) {
            return;
        }
    }

    public static void isBombsPlaced(JsonElement element, Bomb entity) {
        try {
            boolean bombSet = Boolean.parseBoolean(element.getAsJsonObject().get("bombSet").toString());
            if (bombSet) entity.setBomb();
        } catch (NullPointerException e) {
            return;
        }
    }

    //get width and height
    public static void setDungeonSize(Dungeon dungeon, JsonObject convertedObject) {
        int width = 50;
        int height = 50;
        try {
            width = convertedObject.getAsJsonObject().get("width").getAsInt();
            height = convertedObject.getAsJsonObject().get("height").getAsInt();
            dungeon.setWidth(width);
            dungeon.setHeight(height);
        }
        catch(NullPointerException e) {
            dungeon.setWidth(width);
            dungeon.setHeight(height);
            return;
        }
    }
}
