package Bussiness_Layer.Order;

import Bussiness_Layer.Supplier.BillsOfQuantities;
import DataLayer.DalObjects.OrderHandler;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrderController {
    private final Map<Integer, Order> orders;
    private int orderIdGenerator;

    public OrderController() {
        orders = new HashMap<>();
        orderIdGenerator = OrderHandler.getInstance().previousOrderID() + 1;
    }

    public void addOrder(Map<Integer, Integer> orderedItems, Map<Integer,String> itemsNames,int supplierBN, LocalDate supplyDate, BillsOfQuantities billsOfItems, int branch_id) {
        Order newOrder = new Order(orderIdGenerator, supplierBN,orderedItems,itemsNames ,supplyDate, billsOfItems, branch_id);
        OrderHandler.getInstance().insert(newOrder);
        orders.put(orderIdGenerator, newOrder);
        orderIdGenerator++;
    }

    public void removeOrder(int id, int branch_id) {
        Order o = OrderHandler.getInstance().get(id);
        if(o.getBranchId()!= branch_id)
            throw new IllegalArgumentException("the current user has no authorization to delete orders from other branch");
        OrderHandler.getInstance().delete(id);
        if(orders.containsKey(id))
            orders.remove(id);
    }

    public Order getOrder(int id) {
        if(orders.containsKey(id))
            return orders.get(id);
        Order order = OrderHandler.getInstance().get(id);
        orders.put(id, order);
        return order;
    }

    public List<Order> getOrders(int supplierBN, String searchedDate) {
        return orders.values().stream().filter(order -> order.getSupplierBN() == supplierBN && order.getIssueDate().toString().equals(searchedDate)).collect(Collectors.toList());
    }

    public List<Order> getAllSupplierOrders(int supplierBN){
        return OrderHandler.getInstance().getAllSupplierOrder(supplierBN);
    }

    public int[] getLowestSupplier(int serialNumber){
        return OrderHandler.getInstance().GetMinCostSupplier(serialNumber);
    }

    public int existOpenOrder(int supplierBN, int branchID) throws SQLException {
        return OrderHandler.getInstance().existOpenOrder(supplierBN, branchID);
    }

    public LocalDate CreateCopyOrder(Order order,BillsOfQuantities billsOfQuantities){
        Map<Integer, Integer> itemsANdQuantities = new HashMap<>(order.getOrderedItems().size());
        order.getOrderedItems().forEach(item -> itemsANdQuantities.put(item.getId(), item.getAmount()));
        LocalDate supplyDate = order.getSupplyDate().plusDays(7);
        Map<Integer,String> itemsNames = new HashMap<>();
        order.getOrderedItems().forEach(item-> itemsNames.put(item.getId(),item.getName()));
        Order newOrder = new Order(orderIdGenerator,order.getSupplierBN(),itemsANdQuantities,itemsNames,supplyDate,billsOfQuantities, order.getBranchId());
        OrderHandler.getInstance().insert(newOrder);
        orders.put(orderIdGenerator, newOrder);
        orderIdGenerator++;
        return supplyDate;
    }
}
