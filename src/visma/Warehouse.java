/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visma;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Adomas
 */
public class Warehouse {

    private List<Product> productList;

    public Warehouse() {
        productList = HelperMethods.loadFromCSV(null);
    }

    public Warehouse(String path) {
        productList = HelperMethods.loadFromCSV(path);
    }

    public List<Product> getProductList() {
        return new ArrayList<Product>(productList);
    }

    public void findByQty(int quantity) {
        Set<Product> result = new TreeSet<>();
        for (Product product : productList) {

            if (product.getQty() <= quantity) {
                if (result.contains(product)) {
                    Product p = HelperMethods.findInListByProduct(result, product);
                    p.setQty(p.getQty() + product.getQty());
                } else {
                    result.add(product);
                }
            }
        }
        HelperMethods.printSet(result);
    }

    public void findByExpDate(LocalDate date) {
        Set<Product> result = new TreeSet<>();
        for (Product product : productList) {
            if (product.getExpDate().compareTo(date) >= 0) {
                if (result.contains(product)) {
                    Product p = HelperMethods.findInListByProduct(result, product);
                    p.setQty(p.getQty() + product.getQty());
                } else {
                    result.add(product);
                }
            }
        }
        HelperMethods.printSet(result);
    }

}
