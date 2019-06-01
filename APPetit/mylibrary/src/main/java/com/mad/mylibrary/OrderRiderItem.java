package com.mad.mylibrary;

public class OrderRiderItem {
    private String keyRestaurant;
    private String keyCustomer;
    private String addrCustomer;
    private String addrRestaurant;
    private String time;
    private Float totPrice;

    public OrderRiderItem(String keyRestaurant, String keyCustomer, String addrCustomer, String addrRestaurant, String time, Float totPrice) {
        this.keyRestaurant = keyRestaurant;
        this.keyCustomer = keyCustomer;
        this.addrCustomer = addrCustomer;
        this.addrRestaurant = addrRestaurant;
        this.time = time;
        this.totPrice = totPrice;
    }

    public String getKeyRestaurant() {
        return keyRestaurant;
    }

    public String getKeyCustomer() {
        return keyCustomer;
    }

    public String getAddrCustomer() {
        return addrCustomer;
    }

    public String getAddrRestaurant() {
        return addrRestaurant;
    }

    public String getTime() {
        return time;
    }

    public Float getTotPrice() {
        return totPrice;
    }
}
