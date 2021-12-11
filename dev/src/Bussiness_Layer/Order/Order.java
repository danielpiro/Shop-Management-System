package Bussiness_Layer.Order;

import Bussiness_Layer.Supplier.BillsOfQuantities;
import DataLayer.DalObjects.OrderHandler;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Order {
    private final int id;
    private final List<OrderItem> orderedItems;
    private final LocalDate issueDate;
    private LocalDate supplyDate;
    private final int supplierBN;
    private double totalPriceAfterDiscount;
    private boolean isClosed;
    private int branchId;

    /**
     * constructor, receive the id, ordered items and supplierBN. date initialized to today's date.
     *
     * @param id           of the order
     * @param orderedItems - included items. the keys are catalog numbers of the supplier and the values are the quantities
     * @param supplierBN   - BN of supplier
     * @param billsOfItems - the bill of quantities of the supplier
     */
    public Order(int id, int supplierBN, Map<Integer, Integer> orderedItems,Map<Integer,String> itemsNames,LocalDate supplyDate, BillsOfQuantities billsOfItems, int branchId) {
        this.id = id;
        this.orderedItems = createList(orderedItems,itemsNames,billsOfItems);
        this.supplierBN = supplierBN;
        issueDate = LocalDate.now();
        this.supplyDate = supplyDate;
        calculateTotalPriceAfterDiscount();
        isClosed = false;
        this.branchId = branchId;
    }

    public Order(int id, int supplierBN, List<OrderItem> orderItems, LocalDate issueDate, LocalDate supplyDate, double totalPriceAfterDiscount, boolean isClosed, int branchId){
        this.id = id;
        this.supplierBN = supplierBN;
        this.orderedItems = orderItems;
        this.issueDate = issueDate;
        this.supplyDate = supplyDate;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
        this.isClosed = isClosed;
        this.branchId = branchId;
    }

    private List<OrderItem> createList(Map<Integer,Integer> idsAndAmounts,Map<Integer,String> itemsNames, BillsOfQuantities bills){
        List<OrderItem> orderItems = new ArrayList<>(idsAndAmounts.size());
        for(int id : idsAndAmounts.keySet()){
            int amount = idsAndAmounts.get(id);
            double price = bills.getPrice(id);
            double discount = bills.getDiscountPriceForItem(id,amount);
            String name = itemsNames.get(id);
            orderItems.add(new OrderItem(id,name,amount, price,discount,amount*discount ));
        }
        return orderItems;
    }

    private void calculateTotalPriceAfterDiscount() {
        totalPriceAfterDiscount = 0;
        orderedItems.forEach(item -> totalPriceAfterDiscount += item.getTotalPrice());
    }

    public void addItemToOrder(int catalog_number, int supplierBN, int amount, String name, BillsOfQuantities billsOfItems) throws SQLException {
        if(canEdit()) {
            if (orderedItems.stream().anyMatch(item -> item.getId() == catalog_number))
                throw new IllegalArgumentException("item " + catalog_number + "already exists in order " + id);
            double price = billsOfItems.getPrice(catalog_number);
            double discountPrice = billsOfItems.getDiscountPriceForItem(catalog_number, amount);
            OrderItem item = new OrderItem(catalog_number, name, amount, price, discountPrice, amount * discountPrice);
            OrderHandler.getInstance().insertOrderedItem(id, supplierBN, item, null);
            orderedItems.add(item);
            totalPriceAfterDiscount += item.getTotalPrice();
        }
        else
            throw new IllegalArgumentException("The order " + id + " is locked for editing 24 hours before shipment");
    }

    public void updateQuantities(int itemToUpdate, int newAmount, BillsOfQuantities billsOfItems) {
        if(canEdit()) {
            for (OrderItem item : orderedItems) {
                if (item.getId() == itemToUpdate) {
                    item.setAmount(newAmount);
                    item.setDiscountCost(billsOfItems.getDiscountPriceForItem(itemToUpdate, newAmount));
                    calculateTotalPriceAfterDiscount();
                    OrderHandler.getInstance().update(this);
                    return;
                }
            }

            // not found the item with the id of itemToUpdate
            throw new IllegalArgumentException("item does not exist in the current order");
        }
        else
            throw new IllegalArgumentException("The order " + id + " is locked for editing 24 hours before shipment");
    }

    private boolean canEdit(){
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return supplyDate.isAfter(tomorrow);
    }

    public int getId() {
        return id;
    }

    public int getSupplierBN() {
        return supplierBN;
    }

    public double getTotalPriceAfterDiscount() {
        return totalPriceAfterDiscount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public List<OrderItem> getOrderedItems() {
        return orderedItems;
    }

    public LocalDate getSupplyDate() {
        return supplyDate;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void closeOrder(){
        if(!isClosed)
            isClosed = true;
        OrderHandler.getInstance().update(this);
    }

    public boolean containItem(int catalogNumber){
        for(OrderItem i : orderedItems){
            if(i.getId() == catalogNumber)
                return true;
        }
        return false;
    }

    public int getBranchId() {
        return branchId;
    }
}
