package AtoZOnlineApp.Sadwik;

import java.util.HashMap;
import java.util.Map;

public class ItemRecord {
    private static Map<String, Map<String, Double>> itemRecords = new HashMap<>();

    public static void addItem(String category, String itemName, double itemPrice) {
        Map<String, Double> categoryItems = itemRecords.getOrDefault(category, new HashMap<>());
        categoryItems.put(itemName, itemPrice);
        itemRecords.put(category, categoryItems);
    }

    public static void removeItem(String category, String itemName) {
        Map<String, Double> categoryItems = itemRecords.get(category);
        if (categoryItems != null) {
            categoryItems.remove(itemName);
        }
    }

    public static void updateItemPrice(String category, String itemName, double newPrice) {
        Map<String, Double> categoryItems = itemRecords.get(category);
        if (categoryItems != null && categoryItems.containsKey(itemName)) {
            categoryItems.put(itemName, newPrice);
        }
    }

    public static Map<String, Map<String, Double>> getItemRecords() {
        return itemRecords;
    }
}
