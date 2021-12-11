package Facade_Layer.FacadeObjects;


import Bussiness_Layer.Supplier.BillsOfQuantities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FacadeBillsOfQuantities {

    public final Map<Integer, Double> includedItemsAndPricing;
    public final Map<Integer, Double> discountPerOrder;
    public final Map<Integer, Map<Integer, Double>> discountPerItem;
    public final Map<Integer, Integer> catalogToSerial;

    public FacadeBillsOfQuantities(Map<Integer, Double> includedItemsAndPricing, Map<Integer, Double> discountPerOrder, Map<Integer, Map<Integer, Double>> discountPerItem) {
        this.includedItemsAndPricing = includedItemsAndPricing;
        this.discountPerOrder = discountPerOrder;
        this.discountPerItem = discountPerItem;
        catalogToSerial = new HashMap<>();
    }

    public FacadeBillsOfQuantities(Map<Integer, Double> includedItemsAndPricing, Map<Integer, Double> discountPerOrder, Map<Integer, Map<Integer, Double>> discountPerItem, Map<Integer, Integer> catalogToSerial) {
        this.includedItemsAndPricing = includedItemsAndPricing;
        this.discountPerOrder = discountPerOrder;
        this.discountPerItem = discountPerItem;
        this.catalogToSerial = catalogToSerial;
    }


    public FacadeBillsOfQuantities(BillsOfQuantities billsOfQuantities) {
        includedItemsAndPricing = Collections.unmodifiableMap(billsOfQuantities.getIncludedItemsAndPricing());
        discountPerOrder = Collections.unmodifiableMap(billsOfQuantities.getDiscountPerOrder());
        discountPerItem = Collections.unmodifiableMap(billsOfQuantities.getDiscountPerItem());
        catalogToSerial = new HashMap<>();

    }

    public void printBills() {

        String leftAlignFormat_int = "| %-15s | %-15.2f |%n";
        System.out.println("Included Items:");
        System.out.format("+-----------------+-----------------+%n");
        System.out.format("| Item            | Price           |%n");
        System.out.format("+-----------------+-----------------+%n");
        includedItemsAndPricing.keySet().forEach((x) -> System.out.format(leftAlignFormat_int, x, includedItemsAndPricing.get(x)));
        System.out.format("+-----------------+-----------------+%n%n");

        String leftAlignFormat_double = "| %-15d | %-15.2f |%n";
        System.out.println("Discounts Per Order:");
        System.out.format("+-----------------+-----------------+%n");
        System.out.format("| Amount          | Discount        |%n");
        System.out.format("+-----------------+-----------------+%n");
        discountPerOrder.keySet().forEach((x) -> System.out.format(leftAlignFormat_double, x, discountPerOrder.get(x)));
        System.out.format("+-----------------+-----------------+%n%n");

        System.out.println("Discounts Per Item:");
        for (Integer item : discountPerItem.keySet()) {
            System.out.printf("Item: %d%n", item);
            System.out.format("+-----------------+-----------------+%n");
            System.out.format("| Amount          | Discount        |%n");
            System.out.format("+-----------------+-----------------+%n");
            discountPerItem.get(item).keySet().forEach((x) -> System.out.format(leftAlignFormat_double, x, discountPerItem.get(item).get(x)));
            System.out.format("+-----------------+-----------------+%n%n");

        }

    }
}
