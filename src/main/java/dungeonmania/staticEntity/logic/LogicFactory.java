package dungeonmania.staticEntity.logic;

public class LogicFactory {
    public static Logic createLogic(String logic) {
        switch (logic) {
            case "and":
                return new LogicAnd();
            case "or":
                return new LogicOr();
            case "xor":
                return new LogicXor();
            case "not":
                return new LogicNot();
            case "co_and":
                return new LogicCoAnd();
        }

        // Default case is always OR
        // Since, we only need 1 adjacent and activated switch
        return new LogicOr();
    }
}
    