/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visma;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author adoma
 */
public final class HelperMethods {

    private static final List<String> formatStrings = Arrays.asList(
            "dd/MM/yyyy", "dd-MM-yyyy", "dd MM yyyy", "yyyy-MM-dd", "yyyy/MM/dd", "yyyy MM dd");

    private HelperMethods() {
    }

    public static List<Product> loadFromCSV(String path) {
        List<Product> productList = new ArrayList<>();
        int rowCount = 0;

        //try to load from root location
        if (path == null) {
            File f = new File("data.csv");
            path = f.getPath();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String[] splitResult = null;
            String line, productName;
            int quantity = -1;
            Long productCode = null;
            LocalDate expirationDate = null;
            //try to detect columns
            Map<String, Integer> detectedColumns = findColumns(path);

            while ((line = reader.readLine()) != null) {
                splitResult = line.split(",");

                //try to parse date with detected columns
                try {
                    productName = splitResult[detectedColumns.get("name")];
                    quantity = Integer.parseInt(splitResult[detectedColumns.get("quantity")]);
                    productCode = Double.valueOf(detectedColumns.get("codes")).longValue();

                    if (quantity >= 0) {
                        //try parse date
                        expirationDate = dateParser(splitResult[detectedColumns.get("exp")]);
                        if (expirationDate != null) {
                            productList.add(new Product(productName, productCode, quantity, expirationDate));
                            rowCount++;
                        } else {
                            System.out.println("% Problem on expiration dates column on row " + (rowCount + 1));
                        }
                    }
                } catch (Exception e) {
                    // if column prediction fails, try to parse data on default ordering
                    try {
                        productName = splitResult[0];
                        quantity = Integer.parseInt(splitResult[2]);
                        productCode = Double.valueOf(splitResult[1]).longValue();
                        if (quantity >= 0) {
                            expirationDate = dateParser(splitResult[3]);
                            if (expirationDate != null) {
                                productList.add(new Product(productName, productCode, quantity, expirationDate));
                                rowCount++;
                            } else {
                                System.out.println("% Product on row " + (rowCount + 1) + " haven't been added");
                            }
                        }
                    } catch (Exception exception) {
                        System.out.println("% Product on row " + (rowCount + 1) + " haven't been added");
                        rowCount++;
                    }
                }
            }
        } catch (IOException s) {
            System.out.println("File can't be open");
        }
        return productList;

    }

    //method tries to create LocalDate from input string
    public static LocalDate dateParser(String dateStr) {
        DateTimeFormatter formatter = null;
        // iterate through pre-defined static list of date patterns
        // return LocalDate if pattern is matched
        for (String dateFormat : formatStrings) {
            try {
                formatter = DateTimeFormatter.ofPattern(dateFormat);
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException ex) {
            }
        }
        System.out.println("Cannot parse date");
        return null;
    }

    //automatically detect named columns
    public static Map<String, Integer> findColumns(String path) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String[] names = {"name", "exp", "code", "quantity"};
            List<String> splitResult = new ArrayList<>(Arrays.asList(reader.readLine().split(",")));
            for (String string : splitResult) {
                for (String name : names) {
                    if (string.toLowerCase().contains(name)) {
                        map.put(name, splitResult.indexOf(string));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File can't be open");
        }
        return map;
    }

    //used to return the reference to the object from the list
    public static Product findInListByProduct(Set<Product> list, Product p) {
        for (Product product : list) {
            if (product.equals(p)) {
                return p;
            }
        }
        return null;
    }

    public static void printSet(Set<Product> list) {
        for (Product p : list) {
            System.out.println(p);
        }
    }

    // gets an integer input from the user
    public static int getInputInt(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.next();
            System.out.println("Invalid input. Expecting number");
            return -1;
        }
    }

    // Method used to compare LocalDate objects. For Products.equals()
    public static boolean equalsLocalDate(LocalDate ld1, LocalDate ld2) {
        if (ld1.getYear() == ld2.getYear()) {
            if (ld1.getMonthValue() == ld2.getMonthValue()) {
                if (ld1.getDayOfMonth() == ld2.getDayOfMonth()) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method used to compare LocalDate objects
    public static int compareToLocalDate(LocalDate ld1, LocalDate ld2) {

        int comparison = compareInt(ld1.getYear(), ld2.getYear());
        if (comparison == 0) {
            comparison = compareInt(ld1.getMonthValue(), ld2.getMonthValue());
            if (comparison == 0) {
                return compareInt(ld1.getDayOfMonth(), ld2.getDayOfMonth());
            }
        }
        return comparison;
    }

    // Method used for integer comparisons
    public static int compareInt(int i1, int i2) {
        if (i1 < i2) {
            return -1;
        } else if (i1 > i2) {
            return 1;
        } else {
            return 0;
        }
    }

}
