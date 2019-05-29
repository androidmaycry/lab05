package com.mad.mylibrary;

import java.util.HashMap;

public final class OrderItem {
    public String key, addrCustomer, totPrice;
    public HashMap<String, Integer> dishes; //key = dish name, value = quantity
    public Long time;
    Integer status;

    public OrderItem(){

    }

    public OrderItem(String key, String addrCustomer, String totPrice, Integer status, HashMap<String, Integer> dishes, Long time) {
        this.key = key;
        this.addrCustomer = addrCustomer;
        this.totPrice = totPrice;
        this.status = status;
        this.dishes = dishes;
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public String getAddrCustomer() {
        return addrCustomer;
    }

    public String getTotPrice() {
        return totPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public HashMap<String, Integer> getDishes() {
        return dishes;
    }

    public Long getTime() {
        return time;
    }
}
