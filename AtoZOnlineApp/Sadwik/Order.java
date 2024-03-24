package AtoZOnlineApp.Sadwik;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private Map<String, Integer> items;
    private Map<String, Double> itemPrices;

    public Order(Map<String, Double> itemPrices) {
        this.items = new HashMap<>();
        this.itemPrices = itemPrices;
    }

    public void addItem(String itemName, int quantity) {
        items.put(itemName, items.getOrDefault(itemName, 0) + quantity);
    }

    public void displayOrder() {
        if (items.isEmpty()) {
            System.out.println("Your order is empty.");
            return;
        }

        System.out.println("Your Order:");
        for (String itemName : items.keySet()) {
            System.out.println("- " + itemName + ": Quantity - " + items.get(itemName));
        }
    }

    public double calculateTotalCost() {
        double totalCost = 0;
        for (String itemName : items.keySet()) {
            double price = itemPrices.getOrDefault(itemName, 0.0);
            totalCost += price * items.get(itemName);
        }
        return totalCost;
    }
    
}
