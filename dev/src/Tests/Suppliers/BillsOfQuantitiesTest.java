package Tests.Suppliers;

import Bussiness_Layer.Supplier.BillsOfQuantities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BillsOfQuantitiesTest {

    BillsOfQuantities bills;

    @BeforeEach
    void setUp() {
        Map<Integer,Double> itemsAndPricing = new HashMap<>(3);
        itemsAndPricing.put(1,10.0);
        itemsAndPricing.put(2,10.0);
        Map<Integer,Double> discountPerOrder = new HashMap<>(1);
        discountPerOrder.put(500,0.1);
        Map<Integer,Double> discountPerItem = new HashMap<>(1);
        discountPerItem.put(100,0.1);
        discountPerItem.put(150,0.5);
        Map<Integer,Map<Integer,Double>> discountForItems = new HashMap<>();
        discountForItems.put(1,discountPerItem);
        Map<Integer,Integer> catalogToSerial = new HashMap<>(2);
        catalogToSerial.put(1,1);
        catalogToSerial.put(2,2);
        bills = new BillsOfQuantities(itemsAndPricing,catalogToSerial,discountPerOrder,discountForItems);
    }

    @Test
    void getDiscountPriceForItem() {

        assertEquals(10,bills.getDiscountPriceForItem(1,0));
        assertEquals(10,bills.getDiscountPriceForItem(1,10));
        assertEquals(9,bills.getDiscountPriceForItem(1,100));
        assertEquals(5,bills.getDiscountPriceForItem(1,150));
        assertEquals(10,bills.getDiscountPriceForItem(2,0));
        assertEquals(10,bills.getDiscountPriceForItem(2,10));
        assertEquals(10,bills.getDiscountPriceForItem(2,100));
        assertEquals(10,bills.getDiscountPriceForItem(2,150));
        assertThrows(IllegalArgumentException.class,()-> bills.getDiscountPriceForItem(5,1));
        assertThrows(IllegalArgumentException.class,()-> bills.getDiscountPriceForItem(1,-1));
    }

    @Test
    void getSerial() {

        assertEquals(1, bills.getSerial(1));
        assertEquals(2, bills.getSerial(2));
    }
}