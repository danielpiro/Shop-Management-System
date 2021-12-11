package Bussiness_Layer.Supplier;

import DataLayer.DalObjects.SupplierHandler;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class BillsOfQuantities {
    private final Map<Integer, Double> includedItemsAndPricing;
    private final Map<Integer, Integer> catalogToSerial;
    private final Map<Integer, Double> discountPerOrder;
    private final Map<Integer, Map<Integer, Double>> discountPerItem;

    public BillsOfQuantities(Map<Integer, Double> includedItemsAndPricing, Map<Integer, Integer> catalogToSerial, Map<Integer, Double> discountPerOrder,
                             Map<Integer, Map<Integer, Double>> discountPerItem) {
        this.includedItemsAndPricing = includedItemsAndPricing;
        this.catalogToSerial = catalogToSerial;
        this.discountPerItem = discountPerItem;
        this.discountPerOrder = discountPerOrder;
    }


    public void addIncludedItem(int supplierBN,int catalogNumber, int serialNumber, Double cost) throws SQLException {
        if (includedItemsAndPricing.containsKey(catalogNumber) || catalogToSerial.containsKey(catalogNumber))
            throw new IllegalArgumentException("item " + catalogNumber + "already exists in bills of quantities");
        SupplierHandler.getInstance().insertIncludedItems(supplierBN,catalogNumber,serialNumber,cost,null);
        includedItemsAndPricing.put(catalogNumber, cost);
        catalogToSerial.put(catalogNumber, serialNumber);
    }

    public void removeItem(int supplierBN,int catalogNumber) {
        validateItem(catalogNumber);
        SupplierHandler.getInstance().deleteItemFromAgreement(supplierBN,catalogNumber);
        includedItemsAndPricing.remove(catalogNumber);
        discountPerItem.remove(catalogNumber);
        catalogToSerial.remove(catalogNumber);
    }



    public void addDiscountPerOrder(int supplierBN,int amount, double discount) throws SQLException {
        if (discountPerOrder.containsKey(amount)) {
            throw new IllegalArgumentException("discount per order with the given amount already exists");
        }
        SupplierHandler.getInstance().insertDiscountsPerOrder(supplierBN,amount,discount,null);
        discountPerOrder.put(amount, discount);
    }


    public void addDiscountPerItem(int supplierBN,int catalogNumber, int amount, double discount) throws SQLException {
        validateItem(catalogNumber);
        if (discountPerItem.get(catalogNumber).containsKey(amount))
            throw new IllegalArgumentException("discount per item with the given amount already exists");
        SupplierHandler.getInstance().insertDiscountsPerItems(supplierBN,catalogNumber,amount,discount,null);
        discountPerItem.get(catalogNumber).put(amount, discount);
    }

    public void changeDiscountPerOrder(int supplierBN,int amount, double newDiscount) {
        if (!discountPerOrder.containsKey(amount)) {
            throw new IllegalArgumentException("discount per order with the given amount not exists");
        }
        SupplierHandler.getInstance().updateDiscountPerOrder(supplierBN,amount,newDiscount);
        discountPerOrder.replace(amount, newDiscount);
    }

    public void changeDiscountPerItem(int supplierBN,int catalogNumber, int amount, double newDiscount) {
        validateItem(catalogNumber);
        if (discountPerItem.get(catalogNumber).containsKey(amount)) {
            SupplierHandler.getInstance().updateDiscountPerItem(supplierBN,catalogNumber,amount,newDiscount);
            discountPerItem.get(catalogNumber).replace(amount, newDiscount);
        } else
            throw new IllegalArgumentException(("no such combination of item and amount exists"));
    }


    private void validateItem(int catalogNumber) {
        if (!includedItemsAndPricing.containsKey(catalogNumber))
            throw new IllegalArgumentException("ItemMenu is not included in bills of quantities");
    }


    public Map<Integer, Double> getIncludedItemsAndPricing() {
        return includedItemsAndPricing;
    }


    public Double getPrice(int catalogNumber) {
        validateItem(catalogNumber);
        return includedItemsAndPricing.get(catalogNumber);
    }

    public double getDiscountPriceForItem(int catalogNumber, int itemAmount) {
        if (!includedItemsAndPricing.containsKey(catalogNumber))
            throw new IllegalArgumentException("The item is not belong to this supplier");
        if (itemAmount < 0)
            throw new IllegalArgumentException("amount must be non negative number");
        validateItem(catalogNumber);
        Map<Integer, Double> discounts = discountPerItem.get(catalogNumber);
        double discount = 1, originalPrice = includedItemsAndPricing.get(catalogNumber);
        if (discounts == null)
            return originalPrice;
        for (Integer amount : discounts.keySet().stream().sorted().collect(Collectors.toList())) {
            if (itemAmount >= amount)
                discount = discounts.get(amount);
        }

        return discount == 1 ? originalPrice : originalPrice * (1 - discount);
    }


    public void removeDiscountPerItem(int supplierBN,int catalogNumber, int amount) {
        validateItem(catalogNumber);
        if (!discountPerItem.get(catalogNumber).containsKey(amount))
            throw new IllegalArgumentException("there is no discount for the given item and amount");
        SupplierHandler.getInstance().deleteDiscountPerItem(supplierBN,catalogNumber,amount);
        discountPerItem.get(catalogNumber).remove(amount);
    }

    public void removeDiscountPerOrder(int supplierBN,int amount) {
        if (!discountPerOrder.containsKey(amount))
            throw new IllegalArgumentException("there is no discount on order for the given amount");
        SupplierHandler.getInstance().deleteDiscountPerOrder(supplierBN,amount);
        discountPerOrder.remove(amount);
    }

    public Map<Integer, Double> getDiscountPerOrder() {
        return discountPerOrder;
    }

    public Map<Integer, Map<Integer, Double>> getDiscountPerItem() {
        return discountPerItem;
    }

    public int getSerial(int catalogNumber) {
        validateItem(catalogNumber);
        return catalogToSerial.get(catalogNumber);
    }

    public Map<Integer, Integer> getCatalogToSerial() {
        return catalogToSerial;
    }
}
