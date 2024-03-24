package AtoZOnlineApp.Sadwik;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AtoZOnlineApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser;
    private static final String ADMIN_PASSWORD = "supersecret";
    private static final Logger logger = Logger.getLogger(AtoZOnlineApp.class.getName());
    private static Map<String, User> registeredUsers = new HashMap<>();
    private static Map<String, Map<String, Double>> hotelItems = new HashMap<>(); 

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\nWelcome to AtoZOn");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Explore Hotel Details");
            System.out.println("4. Billing");
            System.out.println("5. Admin Menu (Secret)");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    exploreHotelDetails();
                    break;
                case 4:
                    doBilling();
                    break;
                case 5:
                    System.out.print("Enter the secret password to access Admin Menu: ");
                    String password = scanner.nextLine();
                    if (password.equals(ADMIN_PASSWORD)) {
                        adminMenu();
                    } else {
                        System.out.println("Incorrect password. Access denied.");
                    }
                    break;
                case 6:
                    System.out.println("Thank you for using AtoZOn. Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void register() {
        System.out.println("Registration");
        System.out.print("Enter your name (not exceeding 20 alphabets): ");
        String name = scanner.nextLine();
    
        if (name.length() > 20 || !name.matches("[a-zA-Z ]+")) {
            System.out.println("Invalid name. Name should not exceed 20 alphabets and contain only alphabets.");
            return;
        }
    
        System.out.print("Enter your mobile number (10 digits): ");
        String mobileNumber = scanner.nextLine();
    
        if (!mobileNumber.matches("\\d{10}")) {
            System.out.println("Invalid mobile number. Mobile number should be a 10-digit number.");
            return;
        }
    
        System.out.print("Enter your address with pin number: ");
        String address = scanner.nextLine();
    
        if (address.length() < 6) {
            System.out.println("Invalid address. Please enter a valid address with at least 6 characters.");
            return;
        }
    
        String pinCode = address.substring(address.length() - 6);
        if (!isValidPinCode(pinCode)) {
            System.out.println("Invalid address. Please enter a valid pin code.");
            return;
        }
    
        System.out.print("Enter your email ID (optional): ");
        String email = scanner.nextLine();
    
        if (!email.isEmpty() && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            System.out.println("Invalid email format.");
            return;
        }
    
        currentUser = new User(name, mobileNumber, address, email);
        registeredUsers.put(currentUser.getMobileNumber(), currentUser);
        System.out.println("You have been successfully registered. Welcome to the AtoZ online Application.");
    
        logRegistration(name, mobileNumber, address, email);
    }
    
    private static void login() {
        System.out.print("Enter your mobile number: ");
        String mobileNumber = scanner.nextLine();

        if (registeredUsers.containsKey(mobileNumber)) {
            currentUser = registeredUsers.get(mobileNumber);
            System.out.println("Login successful. Welcome, " + currentUser.getName() + "!");
        } else {
            System.out.println("Invalid mobile number. Please register first.");
        }
    }

    private static void exploreHotelDetails() {
        Map<String, Map<String, Double>> hotelItems = new HashMap<>();
        hotelItems.put("Grocery", Grocery.getItems());
        hotelItems.put("Toys", Toys.getItems());

        System.out.println("Hotel Details");
        System.out.println("Categories available:");
        for (String category : hotelItems.keySet()) {
            System.out.println("- " + category);
        }

        System.out.print("Enter the category you want to explore: ");
        String selectedCategory = scanner.nextLine();

        if (hotelItems.containsKey(selectedCategory)) {
            System.out.println("Items available in " + selectedCategory + ":");
            Map<String, Double> items = hotelItems.get(selectedCategory);
            for (String itemName : items.keySet()) {
                System.out.println("- " + itemName + " : $" + items.get(itemName));
            }
        } else {
            System.out.println("Invalid category selected.");
        }
    }

    private static void doBilling() {
        if (currentUser == null) {
            System.out.println("Please register or login first.");
            return;
        }

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.putAll(Grocery.getItems());
        itemPrices.putAll(Toys.getItems());

        Order order = new Order(itemPrices);

        System.out.println("Available items:");
        for (String item : itemPrices.keySet()) {
            System.out.println("- " + item + " ($" + itemPrices.get(item) + ")");
        }

        boolean addMoreItems = true;
        while (addMoreItems) {
            System.out.print("Enter item name (or 'done' to finish): ");
            String itemName = scanner.nextLine();

            if (itemName.equalsIgnoreCase("done")) {
                addMoreItems = false;
            } else if (itemPrices.containsKey(itemName)) {
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                order.addItem(itemName, quantity);
            } else {
                System.out.println("Invalid item name.");
            }
        }

        order.displayOrder();

        System.out.println("Choose payment method:");
        System.out.println("1. Cash on delivery");
        System.out.println("2. Paytm");
        System.out.println("3. Gpay");
        System.out.print("Enter your choice: ");
        int paymentMethod = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (paymentMethod) {
            case 1:
                System.out.println("You have chosen Cash on delivery.");
                break;
            case 2:
                System.out.println("You have chosen Paytm.");
                break;
            case 3:
                System.out.println("You have chosen Gpay.");
                break;
            default:
                System.out.println("Invalid choice.");
        }

        logPayment(currentUser.getName(), order.calculateTotalCost(), paymentMethod);
    }

    private static void adminMenu() {
        System.out.println("Admin Menu");
        System.out.println("1. Add Item");
        System.out.println("2. Remove Item");
        System.out.println("3. Update Item Price");
        System.out.println("4. Add new Category");
        System.out.println("5. Exit Admin Menu");
        System.out.print("Enter your choice: ");

        int adminChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (adminChoice) {
            case 1:
                addItem();
                break;
            case 2:
                removeItem();
                break;
            case 3:
                updateItemPrice();
                break;
            case 4: 
            addCategory();
            break;
                
            case 5:System.out.println("Exiting Admin Menu.");
            break;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void addItem() {
        System.out.print("Enter category name (Grocery or Toys): ");
        String category = scanner.nextLine();

        if (category.equalsIgnoreCase("Grocery") || category.equalsIgnoreCase("Toys")) {
            System.out.print("Enter item name: ");
            String itemName = scanner.nextLine();

            System.out.print("Enter item price: ");
            double itemPrice = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            ItemRecord.addItem(category, itemName, itemPrice);
            System.out.println("Item added successfully.");
        } else {
            System.out.println("Invalid category name.");
        }
    }

    private static void addCategory() {
        System.out.print("Enter the name of the new category: ");
        String newCategory = scanner.nextLine();
    
        if (hotelItems.containsKey(newCategory)) {
            System.out.println("Category already exists.");
        } else {
            hotelItems.put(newCategory, new HashMap<>());
            System.out.println("New category '" + newCategory + "' created successfully.");
        }
    }
    

    private static void removeItem() {
        System.out.print("Enter category name (Grocery or Toys): ");
        String category = scanner.nextLine();

        if (category.equalsIgnoreCase("Grocery") || category.equalsIgnoreCase("Toys")) {
            Map<String, Double> items = ItemRecord.getItemRecords().get(category);

            System.out.println("Available items in " + category + ":");
            for (String item : items.keySet()) {
                System.out.println("- " + item + " ($" + items.get(item) + ")");
            }

            System.out.print("Enter item name to remove: ");
            String itemName = scanner.nextLine();

            if (items.containsKey(itemName)) {
                ItemRecord.removeItem(category, itemName);
                System.out.println("Item removed successfully.");
            } else {
                System.out.println("Item not found.");
            }
        } else {
            System.out.println("Invalid category name.");
        }
    }

    private static void updateItemPrice() {
        System.out.print("Enter category name (Grocery or Toys): ");
        String category = scanner.nextLine();

        if (category.equalsIgnoreCase("Grocery") || category.equalsIgnoreCase("Toys")) {
            Map<String, Double> items = ItemRecord.getItemRecords().get(category);

            System.out.println("Available items in " + category + ":");
            for (String item : items.keySet()) {
                System.out.println("- " + item + " ($" + items.get(item) + ")");
            }

            System.out.print("Enter item name to update price: ");
            String itemName = scanner.nextLine();

            if (items.containsKey(itemName)) {
                System.out.print("Enter new price: ");
                double newPrice = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                ItemRecord.updateItemPrice(category, itemName, newPrice);
                System.out.println("Item price updated successfully.");
            } else {
                System.out.println("Item not found.");
            }
        } else {
            System.out.println("Invalid category name.");
        }
    }

    private static boolean isValidPinCode(String pinCode) {
        // Implement your logic to check if the pinCode is valid
        // For example, you can maintain a list of valid pin codes
        // or use a regular expression to validate the format
        return true; // Replace this with your actual validation logic
    }

    private static void logRegistration(String name, String mobileNumber, String address, String email) {
        try (FileWriter writer = new FileWriter("registration_log.txt", true);
             PrintWriter printWriter = new PrintWriter(writer)) {
            printWriter.println("Name: " + name + ", Mobile Number: " + mobileNumber + ", Address: " + address + ", Email: " + email);
            logger.log(Level.INFO, "Registration logged for: " + name);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to registration log file: " + e.getMessage());
        }
    }

    private static void logPayment(String userName, double amount, int paymentMethod) {
        String paymentMethodStr;
        switch (paymentMethod) {
            case 1:
                paymentMethodStr = "Cash on Delivery";
                break;
            case 2:
                paymentMethodStr = "Paytm";
                break;
            case 3:
                paymentMethodStr = "Gpay";
                break;
            default:
                paymentMethodStr = "Unknown";
        }

        try (FileWriter writer = new FileWriter("payment_log.txt", true);
             PrintWriter printWriter = new PrintWriter(writer)) {
            printWriter.println("User: " + userName + ", Amount: $" + amount + ", Payment Method: " + paymentMethodStr);
            logger.log(Level.INFO, "Payment logged for: " + userName);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing to payment log file: " + e.getMessage());
        }
    }
}
