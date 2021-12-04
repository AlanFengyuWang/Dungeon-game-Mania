package dungeonmania.movingEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.equipAndCollectablemodel.DurableEquipment;
import dungeonmania.staticEntity.StaticEntity;
import dungeonmania.staticEntity.collectableEntity.Armour;
import dungeonmania.staticEntity.collectableEntity.Arrows;
import dungeonmania.staticEntity.collectableEntity.Sword;
import dungeonmania.staticEntity.collectableEntity.buildableEntity.Bow;
import dungeonmania.staticEntity.collectableEntity.buildableEntity.Shield;

public class BattleBehavior {
    Dungeon dungeon;
    MovingEntity attacker;
    MovingEntity target;
    Armour usingArmour = null;
    Sword usingSword = null;
    Shield usingShield = null;
    Bow usingBow = null;
    Arrows usingArrow = null;

    public BattleBehavior(Dungeon dungeon, MovingEntity attacker, MovingEntity target) {
        this.dungeon = dungeon;
        this.attacker = attacker;
        this.target = target;
    }

    public void updateBattleResult(MovingEntity target, Dungeon dungeon) {
        //remove the dead player
        // System.out.println("BattleBehavior.updateBattleResult --> target berfore= " + target);
        // System.out.println("BattleBehavior.updateBattleResult --> attacker before= " + attacker);
        battleMethology(target, dungeon);

        // System.out.println("attacker health = " + attacker.getHealth() + ", target health = " + target.getHealth());

        //remove the dead player
        // System.out.println("BattleBehavior.updateBattleResult --> target after= " + target);
        // System.out.println("BattleBehavior.updateBattleResult --> attacker after= " + attacker);
        //below player can be "this" or "target"
        Player player = attacker.findPlayer(target);
        MovingEntity nonPlayer = attacker.findOtherEntity(target);

        //if player is the players
        if(player != null) {
            // System.out.println("-->battleBehavior.updateBattleResult --> char died");
            if(player.getHealth() <= 0) {
                if (player.getOneRing() != null) {
                    player.getOneRing().doUse(player);
                } else {
                    nonPlayer.removePlayerIfDie((Player)player, dungeon);
                }
            }
            else {
                player.removeOtherEntityIfDie(nonPlayer, dungeon);
            }
        }
        else {
            if(target.getHealth() <= 0) {
                // System.out.println("-->battleBehavior.updateBattleResult --> other entity died");
                attacker.removeOtherEntityIfDie(target, dungeon);
            }
            else {
                target.removeOtherEntityIfDie(attacker, dungeon);
            }
        }

        //remove other entity
        // attacker.removeOtherEntityIfDie(nonplayer, dungeon);
    }

    //========================
    //========Battle==========
    //========================
    /**
     * Default battle methology, each moving entities might have different ways of battle
     */
    public void battleMethology(MovingEntity target, Dungeon dungeon) {
        Player player = (Player) attacker;
        
        while(attacker.getHealth() > 0 && target.getHealth() > 0) {
            //update the health
            // updateHealth(target);
            // System.out.println("enemy damage -> " + target.getAttackDamage());
            updateHealth(target);
            
            Random random = new Random(System.currentTimeMillis());
            while(attacker.getHealth() > 0 && target.getHealth() > 0) {
                if (attacker instanceof Hydra) {
                    //update the health
                    if (player.getInventory().getEquippedWeapon("anduril") != null) {
                        updateHealth(target);
                    } else {
                        if (random.nextInt(2) == 1) {
                            // add the health it as supposed to lose
                            updateHealthHydraAdd(target);
                        } else {
                            updateHealth(target);
                        }
                    }
                }
                else {
                    //update the health
                    updateHealth(target);
                }  
            }
            // update player's item durability
            List <StaticEntity> tempInventory = new ArrayList<>();
            for (StaticEntity se : player.getInventory().getStaticEntities()) {
                tempInventory.add(se);
            }
            for (StaticEntity se : tempInventory)  {
                if (se instanceof DurableEquipment) {
                    DurableEquipment temp = (DurableEquipment) se;
                    temp.updatePlayerItemDurability(player, dungeon);
                } 
            } 
        }
    }
    

    /**
     * @pre defender and MovingEntity can be any moving entities
     */
    public void updateHealth(MovingEntity defender) {
        //update the player's health
        /**
         * Assumption for Player
         * --health: 100
         * --attack damage: 30
         * --damageDivisor: 10
         * =======================
         * Assumption for mercenary
         * --health: 20
         * --attack damage: 30
         * --damagedivisor: 5
         * =======================
         * Algorithm:
         * Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
         * Enemy Health = Character Health - ((character Health * character Attack Damage) / 5)
         */
        //fight till one party is dead, update quickly

        int bufferedAttackerDamage = attacker.getAttackDamage();
        int bufferedDefenderDamage = defender.getAttackDamage();

        int newAttackerHealth = attacker.getHealth() - (defender.getHealth() * bufferedDefenderDamage) / attacker.getDamage_divisor();
        int newDefenderHealth = defender.getHealth() - (attacker.getHealth() * bufferedAttackerDamage) / defender.getDamage_divisor();

        // Attack the defense stats first before attacking health
        if (attacker.getDefense() > 0) {
            attacker.setDefense(attacker.getDefense()-defender.getAttackDamage());
        } else {
            attacker.setHealth(newAttackerHealth);
        }
        
        // Attack the defense stats first before attacking health
        if (defender.getDefense() > 0) {
            defender.setDefense(defender.getDefense()-attacker.getAttackDamage());
        } else {
            defender.setHealth(newDefenderHealth);
        }
    }
    
    /**
     * Algorism:
     * Character Health = Character Health - ((Enemy Health * Enemy Attack Damage) / 10)
     * Hydra Health = Character Health + ((character Health * character Attack Damage) / 5)
     */
    public void updateHealthHydraAdd(MovingEntity defender) {
        int bufferedAttackerDamage = attacker.getAttackDamage() - defender.getDefense();
        int bufferedDefenderDamage = defender.getAttackDamage() - attacker.getDefense();
        if(bufferedAttackerDamage < 1) {
            bufferedAttackerDamage = 0;
        }
        if(bufferedDefenderDamage < 1) {
            bufferedDefenderDamage = 0;
        }
        int newAttackerHealth = attacker.getHealth() + (defender.getHealth() * defender.getAttackDamage()) / attacker.getDamage_divisor();
        int newDefenderHealth = defender.getHealth() - (attacker.getHealth() * attacker.getAttackDamage()) / defender.getDamage_divisor();

        attacker.setHealth(newAttackerHealth);
        defender.setHealth(newDefenderHealth);
    }

    public void attack(MovingEntity attacker) {
        MovingEntity defender = attacker;
        int bufferedAttackerDamage = attacker.getAttackDamage() - defender.getDefense();
        if(bufferedAttackerDamage < 0) bufferedAttackerDamage = 0;
        defender.setHealth(defender.getHealth() - (attacker.getHealth() * bufferedAttackerDamage) / defender.getDamage_divisor());
    }
}
