package com.company;

public class OrderDetails_arrayList {
    private String  productName, madeBy;
    private int orderId, productId, quantity;
    private Float unitPrice;
    public OrderDetails_arrayList(int orderId, int productId, String productName, String madeBy, int quantity, Float unitPrice)
    {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.madeBy = madeBy;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getProductName() {
        return productName;
    }

    public String getMadeBy() {
        return madeBy;
    }

    public int getProductId() {
        return productId;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
