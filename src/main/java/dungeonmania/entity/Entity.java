package dungeonmania.entity;

import dungeonmania.Dungeon;
import dungeonmania.entity.blockingStates.BlockState;
import dungeonmania.entity.blockingStates.BlockingStates;
import dungeonmania.entity.blockingStates.UnblockState;
import dungeonmania.movingEntity.Mercenary;
import dungeonmania.staticEntity.ZombieToastSpawner;
import dungeonmania.util.Position;

public abstract class Entity{
    /**
     * Author: Alan
     * Date: 28/10/2021
     */
    private Position curr_position;
    private int id;
    private String entityType;
	private int interactableRange = 1;

	//blocking states
	protected BlockState blockingState;
	protected UnblockState unblockingState;
	protected BlockingStates currBlockingState;

	public Entity(int id, String entityType) {
        this.id = id;
        this.entityType = entityType;

		//change to unblock by default
		blockingState = new BlockState(currBlockingState);
		unblockingState = new UnblockState(currBlockingState);
		currBlockingState = unblockingState;

    }

    public Entity(int id, String entityType, Position curr_position) {
        this.id = id;
        this.entityType = entityType;
        this.curr_position = curr_position;

		//change to unblock by default
		blockingState = new BlockState(currBlockingState);
		unblockingState = new UnblockState(currBlockingState);
		currBlockingState = unblockingState;
    }

	//abstract method
	// public abstract void doInteract(Entity entity);
	public abstract void doInteract(Entity entity, Dungeon dungeon);

	//methods
	public boolean IsInteractable() {
		if(this instanceof Mercenary || this instanceof ZombieToastSpawner) return true;
		return false;
	}

    //getter and setters
	public Position getCurr_position() {
		return this.curr_position;
	}

	public void setCurr_position(Position curr_position) {
		this.curr_position = curr_position;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEntityType() {
		return this.entityType;
	}

	public String getFilteredEntityType() {
		// return JSONfile.convertStringToEntityName(entityType);

		String entityTypeOutput = this.entityType;
		// If there is an underscore with number, chop it off
        entityTypeOutput = entityTypeOutput.replaceAll("[0-9]","");
		if (entityTypeOutput.endsWith("_")) entityTypeOutput = entityTypeOutput.substring(0, entityTypeOutput.length() - 1);

		return entityTypeOutput;
	}

	public String strimString(String string) {
		//delete the front double quote
		if(string.startsWith("\""))
            string = string.substring(1,string.length());

		if(string.endsWith("\""))
            string = string.substring(0,string.length()-1);
		return string;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public int getInteractableRange() {
		return this.interactableRange;
	}

	public void setInteractableRange(int interactableRange) {
		this.interactableRange = interactableRange;
	}

	public BlockingStates getcurrBlockingState() {
		// System.out.print("Entity/getcurrBlockingState --> currBlockingState = " + currBlockingState);
		return this.currBlockingState;
	}

	public BlockState getBlockState() {
		return this.blockingState;
	}

	public UnblockState getUnblockState() {
		// System.out.print("Entity/getUnblockState --> unblockingState = " + unblockingState);
		return this.unblockingState;
	}

	public void setCurrBlockingState(BlockingStates currBlockingState) {
		this.currBlockingState = currBlockingState;
	}
	
}
