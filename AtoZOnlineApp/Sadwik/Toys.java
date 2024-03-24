package AtoZOnlineApp.Sadwik;

import java.util.HashMap;
import java.util.Map;

public class Toys {
    public static Map<String, Double> getItems() {
        Map<String, Double> toyItems = new HashMap<>();
        // Sample toy items
        toyItems.put("Action Figure", 10.0);
        toyItems.put("Doll", 8.5);
        toyItems.put("Blocks", 5.0);
        return toyItems;
    }
}
