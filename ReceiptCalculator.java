import java.io.*;
import java.util.*;

class Item 
{
    String name;
    double mrp;
    double price;
    int quantity;

    public Item(String name, double mrp, double price, int quantity) 
    {
        this.name = name;
        this.mrp = mrp;
        this.price = price;
        this.quantity = quantity;
    }

    public double getTotalMRP() 
    {
        return mrp * quantity;
    }

    public double getTotalCost() 
    {
        return price * quantity;
    }

    public double getSavings() 
    {
        return getTotalMRP() - getTotalCost();
    }

    @Override
    public String toString() 
    {
        return String.format("%-20s %-10.2f %-10.2f %-10d %-10.2f %-10.2f", name, mrp, price, quantity, getTotalMRP(), getTotalCost());
    }
}

public class ReceiptCalculator
{
    private static final double TAX_RATE = 0.12;
    private static int billNumberCounter = 1;     // Static counter for unique bill numbers
    private int billnumber;
    private ArrayList<Item> items = new ArrayList<>();

    public ReceiptCalculator() 
    {
        this.billnumber = billNumberCounter++;     // Assign unique bill number and increment counter
    }

    public void addItem(String name, double mrp, double price, int quantity) 
    {
        items.add(new Item(name, mrp, price, quantity));
    }

    public double calculateSubtotal() 
    {
        double subtotal = 0;
        for (Item item : items) 
        {
            subtotal += item.getTotalCost();
        }
        return subtotal;
    }

    public double calculateTotalMRP() 
    {
        double totalMRP = 0;
        for (Item item : items) 
        {
            totalMRP += item.getTotalMRP();
        }
        return totalMRP;
    }

    public double calculateSavings() 
    {
        double savings = 0;
        for (Item item : items) 
        {
            savings += item.getSavings();
        }
        return savings;
    }

    public double calculateTax(double subtotal) 
    {
        return subtotal * TAX_RATE;
    }

    public double calculateFinalTotal(double subtotal, double tax) 
    {
        return subtotal + tax;
    }

    public void generateReceipt(boolean roundOff) 
    {
        System.out.println("\n========== RECEIPT ==========");
        System.out.printf("Bill Number: %d\n", billnumber);
        System.out.printf("%-20s %-10s %-10s %-10s %-10s %-10s\n", "Item Name", "MRP", "Price", "Quantity", "Total MRP", "Total Payable");
        System.out.println("--------------------------------------------------------------------------");

        for (Item item : items) 
        {
            System.out.println(item.toString());
        }

        double subtotal = calculateSubtotal();
        double totalMRP = calculateTotalMRP();
        double savings = calculateSavings();
        double tax = calculateTax(subtotal);
        double finalTotal = calculateFinalTotal(subtotal, tax);
        
        if (roundOff) 
        {
            finalTotal = Math.round(finalTotal); // Round the final total to the nearest integer
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-30s: %.2f\n", "Total MRP", totalMRP);
        System.out.printf("%-30s: %.2f\n", "Subtotal (Total Payable)", subtotal);
        System.out.printf("%-30s: %.2f\n", "Tax (12%)", tax);
        System.out.printf("%-30s: %.2f\n", "Final Total", finalTotal);
        System.out.printf("%-30s: %.2f\n", "Total Savings", savings);
        System.out.println("=============================");

        saveReceiptToFile(totalMRP, subtotal, tax, finalTotal, savings);
    }

    public void saveReceiptToFile(double totalMRP, double subtotal, double tax, double finalTotal, double savings) 
    {
        try (FileWriter writer = new FileWriter("receipt_" + billnumber + ".txt")) 
        {
            writer.write("\n========== RECEIPT ==========\n");
            writer.write("Bill Number: " + billnumber + "\n");
            writer.write(String.format("%-20s %-10s %-10s %-10s %-10s %-10s\n", "Item Name", "MRP", "Price", "Quantity", "Total MRP", "Total Payable"));
            writer.write("--------------------------------------------------------------------------\n");

            for (Item item : items) 
            {
                writer.write(item.toString() + "\n");
            }

            writer.write("--------------------------------------------------------------------------\n");
            writer.write(String.format("%-30s: %.2f\n", "Total MRP", totalMRP));
            writer.write(String.format("%-30s: %.2f\n", "Subtotal (Total Payable)", subtotal));
            writer.write(String.format("%-30s: %.2f\n", "Tax (12%)", tax));
            writer.write(String.format("%-30s: %.2f\n", "Final Total", finalTotal));
            writer.write(String.format("%-30s: %.2f\n", "Total Savings", savings));
            writer.write("=============================\n");

            System.out.println("Receipt saved to receipt_" + billnumber + ".txt");
        } 
        catch (IOException e) 
        {
            System.out.println("An error occurred while saving the receipt.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        boolean continueBilling = true;

        while (continueBilling) 
        {
            ReceiptCalculator calculator = new ReceiptCalculator();

            while (true) 
            {
                System.out.print("\nEnter item name (or 'done' to finish): ");
                String name = sc.nextLine();
                if (name.equalsIgnoreCase("done")) break;

                double mrp = 0;
                double price = 0;
                int quantity = 0;
                boolean validInput = false;

                // Handle invalid input for MRP
                while (!validInput) 
                {
                    try 
                    {
                        System.out.print("Enter item MRP: ");
                        mrp = sc.nextDouble();
                        validInput = true;
                    } 
                    catch (InputMismatchException e) 
                    {
                        System.out.println("Invalid MRP. Please enter a valid number.");
                        sc.next(); // clear the invalid input
                    }
                }

                validInput = false;

                // Handle invalid input for price
                while (!validInput) 
                {
                    try 
                    {
                        System.out.print("Enter item price: ");
                        price = sc.nextDouble();
                        validInput = true;
                    } 
                    catch (InputMismatchException e) 
                    {
                        System.out.println("Invalid price. Please enter a valid number.");
                        sc.next(); // clear the invalid input
                    }
                }

                validInput = false;

                // Handle invalid input for quantity
                while (!validInput) 
                {
                    try 
                    {
                        System.out.print("Enter item quantity: ");
                        quantity = sc.nextInt();
                        validInput = true;
                    } 
                    catch (InputMismatchException e) 
                    {
                        System.out.println("Invalid quantity. Please enter a valid number.");
                        sc.next(); // clear the invalid input
                    }
                }

                sc.nextLine();  // consume newline

                calculator.addItem(name, mrp, price, quantity);
            }

            if (calculator.items.size() > 0) 
            {
                System.out.print("Would you like to round off the final total? (yes/no): ");
                String roundOffChoice = sc.nextLine();
                boolean roundOff = roundOffChoice.equalsIgnoreCase("yes");

                calculator.generateReceipt(roundOff);
            } 
            else 
            {
                System.out.println("No items added. Exiting.");
            }

            System.out.print("\nDo you want to generate another receipt? (yes/no): ");
            continueBilling = sc.nextLine().equalsIgnoreCase("yes");
        }

        System.out.println("Thank you for using the receipt generator.");
    }
}