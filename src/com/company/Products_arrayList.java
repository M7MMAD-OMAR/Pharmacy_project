package com.company;

public class Products_arrayList {
    private int productID, quantity;
    private String productName, madeBy;
    public Products_arrayList(int productID, String productName, String madeBy, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.madeBy = madeBy;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getMadeBy() {
        return madeBy;
    }

    public int getQuantity() {
        return quantity;
    }
}
