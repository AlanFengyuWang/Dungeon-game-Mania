package dungeonmania.goal;

import com.google.gson.JsonObject;

import dungeonmania.Dungeon;

public class GoalFactory {
    public static Goal createGoal(JsonObject goalJson, Dungeon d) {
        String goalType = goalJson.get("goal").getAsString();

        switch (goalType) {
            case "AND":
                return new GoalCompositeAnd(d, createGoal(goalJson.get("subgoals").getAsJsonArray().get(0).getAsJsonObject(), d), 
                    createGoal(goalJson.get("subgoals").getAsJsonArray().get(1).getAsJsonObject(), d));
            case "OR":
                return new GoalCompositeOr(d, createGoal(goalJson.get("subgoals").getAsJsonArray().get(0).getAsJsonObject(), d), 
                    createGoal(goalJson.get("subgoals").getAsJsonArray().get(1).getAsJsonObject(), d));
            case "boulders":
                return new GoalBoulders(d);
            case "exit":
                return new GoalExit(d);
            case "enemies":
                return new GoalEnemies(d);
            case "treasure":
                return new GoalTreasure(d);
        }

        return null;
    }
}
