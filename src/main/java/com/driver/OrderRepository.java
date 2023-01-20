package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderDb;
    private HashMap<String, DeliveryPartner> deliveryPartnerDb;
    private HashMap<String, List<String>> orderToDeliveryPartnerDb;
    private HashMap<String, List<String>> partnerIdTimeDb;

    public OrderRepository() {
    }

    public OrderRepository(HashMap<String, Order> orderDb, HashMap<String, DeliveryPartner> deliveryPartnerDb, HashMap<String, List<String>> orderToDeliveryPartnerDb, HashMap<String, List<String>> partnerIdTimeDb) {
        this.orderDb = orderDb;
        this.deliveryPartnerDb = deliveryPartnerDb;
        this.orderToDeliveryPartnerDb = orderToDeliveryPartnerDb;
        this.partnerIdTimeDb = partnerIdTimeDb;
    }

    public void addOrder(Order order){
        orderDb.put(order.getId(), order);
    }
    public void addPartner(String partnerId){
        DeliveryPartner deliveryPartner = new DeliveryPartner();
        deliveryPartner.setId(partnerId);
        deliveryPartner.setNumberOfOrders(0);
        deliveryPartnerDb.put(partnerId, deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        List<String> orderIdList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();

        if(orderDb.containsKey(orderId) && deliveryPartnerDb.containsKey(partnerId)){
            if(orderToDeliveryPartnerDb.containsKey(partnerId)){
                orderIdList = orderToDeliveryPartnerDb.get(partnerId);
            }
            orderIdList.add(orderId);
            orderToDeliveryPartnerDb.put(partnerId, orderIdList);
            Order order = orderDb.get(orderId);
            String time = String.valueOf(order.getDeliveryTime());

            if(partnerIdTimeDb.containsKey(partnerId)){
                timeList = partnerIdTimeDb.get(partnerId);
            }
            timeList.add(time);
            partnerIdTimeDb.put(time, timeList);
        }
    }

    public Order getOrderById(String  orderId){
        return orderDb.get(orderId);
    }
    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerDb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId){
        List<String> orderList = new ArrayList<>();
        int count=0;
        if(orderToDeliveryPartnerDb.containsKey(partnerId)){
            orderList = orderToDeliveryPartnerDb.get(partnerId);
            count = orderList.size();
        }
        return count;
    }
    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> orderList = new ArrayList<>();
        if(orderToDeliveryPartnerDb.containsKey(partnerId)){
            orderList = orderToDeliveryPartnerDb.get(partnerId);
        }
        return orderList;
    }
    public List<String> getAllOrders(){
        return new ArrayList<>(orderDb.keySet());
    }
    public Integer getCountOfUnassignedOrders(){
        int totalOrders = orderDb.size();
        for(String orders: orderDb.keySet()){
            for(String partner: orderToDeliveryPartnerDb.keySet()){
                List<String> orderList = new ArrayList<>();
                orderList = orderToDeliveryPartnerDb.get(partner);
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
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        Order order = new Order();
        int convertedTime = order.convertTimeToInteger(time);
        int count=0;
        if(partnerIdTimeDb.containsKey(partnerId)){
            List<String> timeList = new ArrayList<>();
            timeList = partnerIdTimeDb.get(partnerId);
            for(String times: timeList){
                int newTime = order.convertTimeToInteger(times);
                if(newTime > convertedTime){
                    count++;
                }
            }
        }
        return count;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int maximumTime = 0;
        String time ="";
        Order order = new Order();
        List<String> list = new ArrayList<>();
        if(partnerIdTimeDb.containsKey(partnerId)){
            list = partnerIdTimeDb.get(partnerId);
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
    public void deletePartnerById(String partnerId){
        if(deliveryPartnerDb.containsKey(partnerId)){
            deliveryPartnerDb.remove(partnerId);
        }
        if(orderToDeliveryPartnerDb.containsKey(partnerId)){
            orderToDeliveryPartnerDb.remove(partnerId);
        }
        if(partnerIdTimeDb.containsKey(partnerId)){
            partnerIdTimeDb.remove(partnerId);
        }
    }
    public void deleteOrderById(String orderId)
    {
        if(orderDb.containsKey(orderId)){
            orderDb.remove(orderId);
        }
        List<String> orderList = new ArrayList<>();
        for(String partnerId: orderToDeliveryPartnerDb.keySet()){
            orderList = orderToDeliveryPartnerDb.get(partnerId);
            for(String orders: orderList){
                if(orders.equals(orderId)){
                    orderList.remove(orders);
                }
            }
            orderToDeliveryPartnerDb.put(partnerId,orderList);
        }
        Order order = new Order();
        String time= String.valueOf(orderDb.get(orderId));

        List<String> timeList = new ArrayList<>();
        for(String partner: partnerIdTimeDb.keySet()){
            timeList=partnerIdTimeDb.get(partner);
            for(String times: timeList){
                if(times.equals(time)){
                    timeList.remove(times);
                }
            }
            partnerIdTimeDb.put(partner,timeList);
        }
    }


}
