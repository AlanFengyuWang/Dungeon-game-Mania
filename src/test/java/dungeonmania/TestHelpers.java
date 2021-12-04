package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;

public class TestHelpers {
    public static<T extends Comparable<? super T>> void assertListAreEqualIgnoringOrder(List<T> a, List<T> b) {
        Collections.sort(a);
        Collections.sort(b);
        assertArrayEquals(a.toArray(), b.toArray());
    }

    public static EntityResponse getEntityResponse(DungeonResponse dr, String entityType) {
        return dr.getEntities().stream().filter(e -> e.getType().equals(entityType)).findFirst().orElse(null);
    }

    public static Stream<EntityResponse> getEntityResponseStream(DungeonResponse dr, String entityType) {
        return dr.getEntities().stream().filter(e -> e.getType().equals(entityType));
    }
}
