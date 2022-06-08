package com.company;

import java.util.Date;

public class Orders_arrayList {
    private String  supplierName, orderDate;
    private int orderId;
    private Float total_pr_with_dis, total_pr_without_dis, discount;
    public Orders_arrayList(int orderId, String supplierName, String orderDate, Float total_pr_with_dis, Float total_pr_without_dis, Float discount) {
            this.orderId = orderId;
            this.supplierName = supplierName;
            this.orderDate = orderDate;
            this.total_pr_with_dis = total_pr_with_dis;
            this.total_pr_without_dis = total_pr_without_dis;
            this.discount = discount;

    }


    public int getOrderId() {
        return orderId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public Float getTotal_pr_with_dis() {
        return total_pr_with_dis;
    }

    public Float getTotal_pr_without_dis() {
        return total_pr_without_dis;
    }

    public Float getDiscount() {
        return discount;
    }

}

