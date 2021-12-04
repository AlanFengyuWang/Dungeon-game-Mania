package dungeonmania.entity.blockingStates;

public class UnblockState implements BlockingStates{
    BlockingStates blockingState;
    
    public UnblockState(BlockingStates blockingState) {
        this.blockingState = blockingState;
    }

    @Override
    public void doBlock() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doUnblock() {
        // TODO Auto-generated method stub
        
    }
    
}
