package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {
        this.id=id;
        this.deliveryTime=convertTimeToInteger(deliveryTime);

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
    }

    public Order() {
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public int convertTimeToInteger(String time){
        int h1=Integer.parseInt(String.valueOf(time.charAt(0)));
        int h2=Integer.parseInt(String.valueOf(time.charAt(1)));
        int m1=Integer.parseInt(String.valueOf(time.charAt(3)));
        int m2=Integer.parseInt(String.valueOf(time.charAt(4)));
        int h=h1*10+h2;
        int m=m1*10+h2;
        return (h*60)+m;
    }
}
