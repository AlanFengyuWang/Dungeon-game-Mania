package dungeonmania.staticEntity;

import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.entity.Entity;
import dungeonmania.movingEntity.Mercenary;
import dungeonmania.movingEntity.Player;
import dungeonmania.util.Position;

public class Portal extends StaticEntity {
    private String colour = "";
    private Position landing = null;

    /**
     * Constructor for Portal
     * @param posix_entityType is the string that is after the entityType "Portal_1"
     */

    public Portal(int id, Position curr_position) {
        super(id, "portal", curr_position);

    }

    public Portal(int id, String posix_entityType, Position curr_position) {
        super(id, "portal_" + posix_entityType, curr_position);

    }

    public String getColour() {
        return this.colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Position linkPortal(Dungeon dungeon) {
        // Find the other portal with same colour 
        for (Entity teleport: dungeon.getEntities().stream().filter(e -> e.getEntityType().contains("portal")).collect(Collectors.toList())) {
            if (((Portal) teleport).getColour().equals(this.getColour()) && teleport.getCurr_position() != this.getCurr_position()) {
                return teleport.getCurr_position();
            }
        }
        // If another portal not found (return invalid position)
        return new Position(-1, -1);
    }

    @Override
    public void doInteract(Entity entity, Dungeon dungeon) {
        // ========== Skin Management ==========
        // For skins
        // To make player skin show over door skin
        dungeon.skinsOrdering(entity, this);

        // ========== Link with 2nd Portal ==========
        if (this.landing == null) {
            // Portal not linked
            landing = linkPortal(dungeon);
        }
        if (this.landing.equals(new Position(-1,-1))) {
            // Only 1 portal; Do not teleport
            // All entities can tread on portal
            entity.setCurr_position(this.getCurr_position());
            dungeon.updateEntityResponse(entity);
            return;
        }

        // ========== Pre conditions ==========
        // Only Player, Mercanaries and Boulders can tp
        if (
            !(entity instanceof Player ||
            entity instanceof Mercenary ||
            entity instanceof Boulder)
        ) return;


        // if (!(dungeon.getEntities(this.getCurr_position()).size() == 2)) {
        // Get direction as input as well
        int horizontal_move = this.getCurr_position().getX() - entity.getCurr_position().getX();
        int vertical_move = this.getCurr_position().getY() - entity.getCurr_position().getY();
        Position newPos;

        if (horizontal_move != 0 && vertical_move == 0) {
            // Moving horizontally
            newPos = new Position(landing.getX() + horizontal_move, landing.getY());
        } else if (vertical_move != 0 && horizontal_move == 0) {
            // Moving vertically
            newPos = new Position(landing.getX(), landing.getY() + vertical_move);
        } else {
            // Nothing happens; Player remains in original position
            return;
        }

        // Precondition: Interact with all other entities (on same tile) before portal
        if (dungeon.getEntities(this.getCurr_position()).size() > 1) {
            for (Entity portalLast: dungeon.getEntities(this.getCurr_position())) {
                if (portalLast != this) {
                    portalLast.doInteract((Player) entity, dungeon);
                }    
            }
            // Blocked by entity hence, no movement
            if (this.getCurr_position() != entity.getCurr_position()) return;
        }

        // If the tile is not an obstruction
        if (
            dungeon.getEntities(newPos).isEmpty() ||
            (dungeon.getEntities(newPos).size() == 1 && 
            (dungeon.getEntities(newPos).get(0) instanceof FloorSwitch ||
            dungeon.getEntities(newPos).get(0) instanceof Exit ||
            (dungeon.getEntities(newPos).get(0) instanceof Door && ((Door) dungeon.getEntities(newPos).get(0)).getUnlocked())))
        ) {
            if (entity instanceof Player) {
                Player player_entity = (Player) entity;
                player_entity.setPrev_position(getCurr_position());
            }
            entity.setCurr_position(newPos);
            return;
        }
        // If landing tile is a boulder
        // Check if it's pushable
        else if (
            dungeon.getEntities(newPos).stream().filter(e -> e instanceof Boulder).findAny().orElse(null) != null &&
            entity instanceof Player
        ) {
            int index_old_er = dungeon.getEntities(newPos).indexOf(
                dungeon.getEntities(newPos).stream().filter(e -> e.getEntityType().contains("boulder"))
                .findAny().orElse(null)
            );
            Player player_entity = (Player) entity;
            player_entity.setCurr_position(landing);
        
            ((Boulder) dungeon.getEntities(newPos).get(index_old_er)).doInteract(player_entity, dungeon);

            // Player does not tp to intended place, tp back
            if (player_entity.getCurr_position() == landing) player_entity.setCurr_position(this.getCurr_position());
            else player_entity.setPrev_position(getCurr_position());

            return;
        }
        // Do not tp when there is an obstruction
        else {
            if (entity instanceof Player) {
                Player player_entity = (Player) entity;
                player_entity.setPrev_position(getCurr_position());
            }
            entity.setCurr_position(this.getCurr_position());
        }
        return;
    }
}