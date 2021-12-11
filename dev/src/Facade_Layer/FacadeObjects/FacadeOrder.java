package Facade_Layer.FacadeObjects;

import Bussiness_Layer.Order.Order;
import Bussiness_Layer.Order.OrderItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;


public class FacadeOrder {
    public final int id;
    public final List<OrderItem> orderedItems;
    public final LocalDate issueDate;
    public final String supplierName;
    public final int supplierBN;
    public final LocalDate supplyDate;
    public final String contactNumber;
    public final int branch_id;
    public final double totalPriceAfterDiscount;


    public FacadeOrder(Order businessOrder, String supplierName, String contactNumber){
        this.id = businessOrder.getId();
        this.orderedItems = Collections.unmodifiableList(businessOrder.getOrderedItems());
        this.issueDate = businessOrder.getIssueDate();
        this.supplierBN = businessOrder.getSupplierBN();
        this.supplierName = supplierName;
        this.contactNumber = contactNumber;
        this.supplyDate = businessOrder.getSupplyDate();
        this.totalPriceAfterDiscount = businessOrder.getTotalPriceAfterDiscount();
        this.branch_id = businessOrder.getBranchId();
    }


    public void print(){
        String leftAlignFormat = "| %-15d | %-13s | %-11d | %-15.2f | %-16.2f | %-14.2f |  %n";
        System.out.printf("Order ID: %d%nSupplier BN: %d%nSupplier Name:%s%nSupplier's contact number:%s%n",id,supplierBN,supplierName,contactNumber);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        System.out.println("Issue Date: " + formatter.format(issueDate));
        System.out.println("Supply Date: " + formatter.format(supplyDate));
        System.out.format("+-----------------+---------------+-------------+-----------------+------------------+----------------+%n");
        System.out.format("| Item            | Name          | Quantities  | Catalog price   | Discounted Price | Total Price    |%n");
        System.out.format("+-----------------+---------------+-------------+-----------------+------------------+----------------+%n");
        orderedItems.forEach( (x)-> System.out.format(leftAlignFormat, x.getId(), x.getName(),x.getAmount(), x.getCost(),x.getDiscountCost(),x.getTotalPrice()));
        System.out.format("+-----------------+---------------+-------------+-----------------+------------------+----------------+%n");
        System.out.format("Total price: %.2f%n",totalPriceAfterDiscount);
    }
}
