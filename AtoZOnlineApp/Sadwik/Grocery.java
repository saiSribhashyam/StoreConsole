package AtoZOnlineApp.Sadwik;

import java.util.HashMap;
import java.util.Map;

public class Grocery {
    public static Map<String, Double> getItems() {
        Map<String, Double> groceryItems = new HashMap<>();
        // Sample grocery items
        groceryItems.put("Apples", 2.5);
        groceryItems.put("Bananas", 1.8);
        groceryItems.put("Bread", 1.2);
        return groceryItems;
    }
}
