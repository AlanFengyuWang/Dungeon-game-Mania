package dungeonmania.movingEntity;

import dungeonmania.Dungeon;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.util.Position;

public class Assassin extends Mercenary{

    public Assassin(int id, Position curr_position) {
        super(id, "assassin", curr_position);
        this.setAttackDamage(40);
    }

    public Assassin(int id, String entityType, Position curr_position) {
        super(id, entityType, curr_position);
        this.setAttackDamage(40);
    }

    @Override
    public void doBribe(Player player, Dungeon dungeon) throws InvalidActionException {
        //if this is enemy, it is bribed 
        if(getState().equals(enemyState)) {
            
            //reduce the treasure if the player has treasure
            if(player.getInventory().hasItem("treasure") && player.getInventory().hasItem("one_ring")) {
                //use the treasure
                player.getInventory().useInventory("treasure");
                player.getInventory().useInventory("one_ring");
                //change the state
                setState(allyState);
            }
            else {
                throw new InvalidActionException("missing gold or the one ring");
            }
        }
        //if this is enemy, it is bribed 
        if(getState().equals(enemyState)) {
            // If you have sceptre, mind control first over bribing
            // Unlimited durability
            if (player.getInventory().hasItem("sceptre")) {
                //change the state
                setState(allyState);
                setMindControlled(10);
                setIsControlled(true);
            }
            // use the treasure and one ring if the player has both
            else if(player.getInventory().hasItem("treasure") && player.getInventory().hasItem("one_ring")) {
                //use the treasure
                player.getInventory().useInventory("treasure");
                player.getInventory().useInventory("one_ring");
                //change the state
                setState(allyState);
            }
            // use the sun stone and one ring if the player has both
            else if(player.getInventory().hasItem("sun_stone") && player.getInventory().hasItem("one_ring")) {
                //use the treasure
                player.getInventory().useInventory("sun_stone");
                player.getInventory().useInventory("one_ring");
                //change the state
                setState(allyState);
            }
            else {
                throw new InvalidActionException("no gold available");
            }
        }
    }

    
}
