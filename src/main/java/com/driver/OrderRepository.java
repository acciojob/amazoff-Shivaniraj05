package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Repository
public class OrderRepository {
    private HashMap<String, Order> orderdb;
    private HashMap<String, DeliveryPartner> deliveryPartnerdb;

    private HashMap<String, List<String>> orderToDeliveryPartnerdb;
    private HashMap<String, List<String>> partnerIdTimedb;

    public OrderRepository() {
    }

    public OrderRepository(HashMap<String, Order> orderdb, HashMap<String, DeliveryPartner> deliveryPartnerdb, HashMap<String, List<String>> orderToDeliveryPartner, HashMap<String, List<String>> partnerIdTimedb) {
        this.orderdb = orderdb;
        this.deliveryPartnerdb = deliveryPartnerdb;
        this.orderToDeliveryPartnerdb = orderToDeliveryPartner;
        this.partnerIdTimedb = partnerIdTimedb;
    }
    public void addOrder(Order order)
    {
        orderdb.put(order.getId(),order);
    }

    public void addPartner(String partnerId)
    {
        DeliveryPartner deliverypartner = new DeliveryPartner();
        deliverypartner.setId(partnerId);
        deliverypartner.setNumberOfOrders(0);
        deliveryPartnerdb.put(partnerId,deliverypartner);
    }

    public void addOrderPartnerId(String orderId, String partnerId)
    {
        List<String> orderIdList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();

        if(orderdb.containsKey(orderId) && deliveryPartnerdb.containsKey(partnerId)) {
            if (orderToDeliveryPartnerdb.containsKey(partnerId)) {
                orderIdList = orderToDeliveryPartnerdb.get(partnerId);
            }

            orderIdList.add(orderId);

            orderToDeliveryPartnerdb.put(partnerId, orderIdList);

            Order order= orderdb.get(orderId);
            String time= String.valueOf(order.getDeliveryTime());

            if(partnerIdTimedb.containsKey(partnerId)){
                timeList=partnerIdTimedb.get(partnerId);
            }

            timeList.add(time);

            partnerIdTimedb.put(time, timeList);
        }
    }

    public Order getOrderById(String orderId)
    {
        return orderdb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId)
    {
        return deliveryPartnerdb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId)
    {
        List<String> orderList = new ArrayList<>();
        int count=0;
        if(orderToDeliveryPartnerdb.containsKey(partnerId)){
            orderList = orderToDeliveryPartnerdb.get(partnerId);
            count =  orderList.size();
        }
        return count;

    }

    public List<String> getOrdersByPartnerId(String partnerId)
    {
        List<String> orderList = new ArrayList<>();

        if(orderToDeliveryPartnerdb.containsKey(partnerId)){
            orderList = orderToDeliveryPartnerdb.get(partnerId);
        }
        return orderList;
    }

    public List<String> getAllOrders()
    {
        return new ArrayList<>(orderdb.keySet());
    }

    public Integer getCountOfUnassignedOrders()
    {
        int totalOrders = orderdb.size();

        for(String orders: orderdb.keySet()){
            for(String partner: orderToDeliveryPartnerdb.keySet()){
                List<String> orderList = new ArrayList<>();
                orderList = orderToDeliveryPartnerdb.get(partner);
                for(String order: orderList){
                    if(orders.equals(order)){
                        totalOrders--;
                        break;
                    }
                }
            }
        }
        return totalOrders;
    }

    public Integer getOrdersleftAfterGivenTimeByPartnerId(String time, String partnerId)
    {
        Order order = new Order();

        int convertedTime = order.convertTimeToInteger(time);
        int count = 0;
        if(partnerIdTimedb.containsKey(partnerId)){
            List<String> timeList = new ArrayList<>();
            timeList = partnerIdTimedb.get(partnerId);
            for(String times: timeList){
                int newTime  = order.convertTimeToInteger(times);
                if(newTime>convertedTime){
                    count++;
                }
            }
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId)
    {
        int maximumTime = 0;
        String time ="";
        Order order = new Order();
        List<String> list = new ArrayList<>();
        if(partnerIdTimedb.containsKey(partnerId)){
            list=  partnerIdTimedb.get(partnerId);
            for(String times: list){
                int newTime = order.convertTimeToInteger(times);
                if(newTime>maximumTime){
                    maximumTime = newTime;
                }
            }
        }
        time=Integer.toString(maximumTime);
        return time;
    }

    public void deletePartnerId(String partnerId)
    {
        if(deliveryPartnerdb.containsKey(partnerId)){
            deliveryPartnerdb.remove(partnerId);
        }
        if(orderToDeliveryPartnerdb.containsKey(partnerId)){
            orderToDeliveryPartnerdb.remove(partnerId);
        }
        if(partnerIdTimedb.containsKey(partnerId)){
            partnerIdTimedb.remove(partnerId);
        }
    }

    public void deleteOrderById(String orderId)
    {
        if(orderdb.containsKey(orderId)){
            orderdb.remove(orderId);
        }
        List<String> orderList = new ArrayList<>();
        for(String partnerId: orderToDeliveryPartnerdb.keySet()){
            orderList = orderToDeliveryPartnerdb.get(partnerId);
            for(String orders: orderList){
                if(orders.equals(orderId)){
                    orderList.remove(orders);
                }
            }
            orderToDeliveryPartnerdb.put(partnerId,orderList);
        }
        Order order = new Order();
        String time= String.valueOf(orderdb.get(orderId));

        List<String> timeList = new ArrayList<>();
        for(String partner: partnerIdTimedb.keySet()){
            timeList=partnerIdTimedb.get(partner);
            for(String times: timeList){
                if(times.equals(time)){
                    timeList.remove(times);
                }
            }
            partnerIdTimedb.put(partner,timeList);
        }
    }
}
