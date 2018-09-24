package com.mis.community.rms.Common;

public class OrderCommon {
    public static int product_id;
    public static String product_name;
    public static String price;
    public static String quantity;

    public static int getProduct_id() {
        return product_id;
    }

    public static void setProduct_id(int product_id) {
        OrderCommon.product_id = product_id;
    }

    public static String getProduct_name() {
        return product_name;
    }

    public static void setProduct_name(String product_name) {
        OrderCommon.product_name = product_name;
    }

    public static String getPrice() {
        return price;
    }

    public static void setPrice(String price) {
        OrderCommon.price = price;
    }

    public static String getQuantity() {
        return quantity;
    }

    public static void setQuantity(String quantity) {
        OrderCommon.quantity = quantity;
    }
}
