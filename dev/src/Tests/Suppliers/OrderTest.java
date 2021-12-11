package Tests.Suppliers;


import Bussiness_Layer.Order.Order;
import Bussiness_Layer.Order.OrderItem;
import Bussiness_Layer.Supplier.BillsOfQuantities;
import DataLayer.DalObjects.OrderHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    Order order;
    BillsOfQuantities bills;

    @BeforeEach
    void setUp(){
        OrderHandler.getInstance().createTables();
        Map<Integer,Double> itemAndPricing = new HashMap<>(4);
        itemAndPricing.put(1,10.0);
        itemAndPricing.put(2,5.0);
        itemAndPricing.put(3,12.0);
        itemAndPricing.put(4,20.0);
        itemAndPricing.put(5,2.0);
        Map<Integer,Double> discountPerOrder = new HashMap<>(1);
        discountPerOrder.put(1000,0.5);
        Map<Integer,Map<Integer,Double>> discountPerItem = new HashMap<>(4);
        Map<Integer,Double> discount = new HashMap<>(1);
        Map<Integer,Integer> catalogToSerial = new HashMap<>();
        catalogToSerial.put(1,1);
        catalogToSerial.put(2,2);
        catalogToSerial.put(3,3);
        catalogToSerial.put(4,4);
        catalogToSerial.put(5,5);
        Map<Integer,String> names = new HashMap<>();
        names.put(1,"milk");
        names.put(2,"sugar");
        names.put(3,"honey");
        names.put(4,"bread");

        Map<Integer,Integer> items = new HashMap<>();
        items.put(1,10);
        items.put(2,10);
        items.put(3,10);
        items.put(4,10);

        discount.put(1000,0.1);
        discount.put(5000,0.15);
        discountPerItem.put(1,discount);
        discountPerItem.put(2,discount);
        bills = new BillsOfQuantities(itemAndPricing,catalogToSerial,discountPerOrder,discountPerItem);
        order = new Order(10,1,items,names, LocalDate.now().plusDays(4),bills,111);
        OrderHandler.getInstance().insert(order);
    }
    @AfterEach
    void tearDown(){
        OrderHandler.getInstance().delete(10);
    }

    @Test
    void updateQuantities() throws SQLException {
        List<OrderItem> toCompare = new ArrayList<>();
        toCompare.add(new OrderItem(1,"milk",10,10.0, 10.0, 100));
        toCompare.add(new OrderItem(2,"sugar",10,5.0, 5.0, 50));
        toCompare.add(new OrderItem(3,"honey",10,12.0, 12.0, 120));
        toCompare.add(new OrderItem(4,"bread",10,20.0, 20.0, 200));
        List<OrderItem> orderItems = order.getOrderedItems();
        for(int i=0; i< toCompare.size(); i++){
            if(!toCompare.get(i).equals(orderItems.get(i))){
                fail();
            }
        }
        order.addItemToOrder(5,1,10,"bazooka",bills);
        toCompare.add(new OrderItem(5,"bazooka",10,2.0, 2.0, 20.0));
        orderItems = order.getOrderedItems();
        for(int i=0; i< toCompare.size(); i++){
            if(!toCompare.get(i).equals(orderItems.get(i))){
                fail();
            }

        }
    }

    @Test
    void getTotalPriceAfterDiscount(){
        assertEquals(470,order.getTotalPriceAfterDiscount());
        order.updateQuantities(1,5,bills);
        assertEquals(420,order.getTotalPriceAfterDiscount());
        order.updateQuantities(1,10,bills);
        assertEquals(470,order.getTotalPriceAfterDiscount());
        order.updateQuantities(2,20,bills);
        assertEquals(520,order.getTotalPriceAfterDiscount());
        assertThrows(IllegalArgumentException.class,()->order.updateQuantities(5,100,bills));
    }

}