/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visma;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * @author adoma
 */
public class Main {

    public static Warehouse inventory = null;

    // try to parse from default location when program starts
    static {
        inventory = new Warehouse();
    }

    public static void main(String[] args) {
        boolean quit = false;
        boolean noData = inventory.getProductList().isEmpty();
        int choiceUser;
        String inputPath = null;
        Scanner scanner = new Scanner(System.in);

        // LOAD DATA
        while (noData) {
            System.out.println("\nLoad data first.\nEnter file path:");
            inputPath = scanner.nextLine();
            try {
                File inpFile = new File(inputPath);
                String filePath = inpFile.getPath() + (inpFile.getPath().contains(".")?"":".csv");
                inventory = new Warehouse(filePath) ;
                noData = inventory.getProductList().isEmpty();
            } catch (InvalidPathException e) {
                System.out.println("Provide valid path");
            }

        }

        //Main menu
        while (!quit) {
            printMenu();
            choiceUser = HelperMethods.getInputInt(scanner);

            switch (choiceUser) {
                case 1:
                    System.out.println("Find all products with quantity less (or equals) than: ");
                    int input = HelperMethods.getInputInt(scanner);
                    inventory.findByQty(input);
                    break;
                case 2:
                    System.out.println("Find all products with expiration date less (or equals) than: ");
                    scanner.nextLine();
                    String strInput = scanner.nextLine();
                    LocalDate date = HelperMethods.dateParser(strInput);
                    if (date != null) {
                        inventory.findByExpDate(date);
                    } else {
                        System.out.println("Invalid date provided");
                    }
                    break;
                case 3:
                    quit = true;
                    break;
                default:
                    System.out.println("Provide valid choice");
            }
        }
    }

    public static void printMenu() {
        System.out.println("\nSelect an option:\n"
                + "1. Find products by quantity \n"
                + "2. Find products by expiration date \n"
                + "3. Quit\n");
    }

}
