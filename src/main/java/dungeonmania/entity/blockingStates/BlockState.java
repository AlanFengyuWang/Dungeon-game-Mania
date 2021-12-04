package dungeonmania.entity.blockingStates;

public class BlockState implements BlockingStates{
    BlockingStates blockingState;
    
    public BlockState(BlockingStates blockingState) {
        this.blockingState = blockingState;
    }

    @Override
    public void doBlock() {
        
    }

    @Override
    public void doUnblock() {
        
    }
    
}
