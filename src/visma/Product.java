package visma;

import java.time.LocalDate;
import java.util.Objects;

public class Product implements Comparable<Product> {

    private final String name;
    private final long code;
    private int qty;
    private LocalDate expirationDate;

    public Product(String name, long code, int qty, LocalDate expDate) {
        this.name = name;
        this.code = code;
        this.qty = qty;
        this.expirationDate = expDate;
    }

    @Override
    public String toString() {
        return "Name: " + name
                + ", Code: " + code
                + ", Expiration Date: " + expirationDate
                + " Quantity " + qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        if (product.hashCode() == this.hashCode()) {
            return true;
        }

        return name.equalsIgnoreCase(product.getName())
                && code == product.getCode()
                && HelperMethods.equalsLocalDate(this.expirationDate, product.getExpDate());
    }

    @Override
    public int compareTo(Product product) {
        if (this == product) {
            return 0;
        }
        //compare by name
        int comparison = this.name.compareTo(product.getName());
        if (comparison == 0) {
            //compare by dates
            comparison = HelperMethods.compareToLocalDate(this.expirationDate, product.getExpDate());
            if (comparison == 0) {
                //comparison by product code
                if (this.code > product.getCode()) {
                    return 1;
                } else if (this.code < product.getCode()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
        return comparison;

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + (int) (this.code ^ (this.code >>> 32));
        hash = 89 * hash + Objects.hashCode(this.expirationDate);
        return hash;
    }

    public String getName() {
        return name;
    }

    public long getCode() {
        return code;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public LocalDate getExpDate() {
        return expirationDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expirationDate = expDate;
    }

}
