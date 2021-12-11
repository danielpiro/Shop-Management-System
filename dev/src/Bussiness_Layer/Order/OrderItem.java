package Bussiness_Layer.Order;

import java.util.Objects;

public class OrderItem {

    private final int id;
    private final String name;
    private int amount;
    private final double cost;
    private double discountCost;
    private double totalPrice;

    public OrderItem(int id, String name, int amount, double cost, double discountCost, double totalPrice){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.cost = cost;
        this.discountCost =discountCost;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public double getCost() {
        return cost;
    }

    public double getDiscountCost() {
        return discountCost;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setAmount(int amount) {
        if(amount <= 0)
            throw new IllegalArgumentException("amount cannot be negative");
        this.amount = amount;
    }

    public void setDiscountCost(double discountCost){
        this.discountCost = discountCost;
        totalPrice = discountCost*amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id == orderItem.id && amount == orderItem.amount && Double.compare(orderItem.cost, cost) == 0 && Double.compare(orderItem.discountCost, discountCost) == 0 && Double.compare(orderItem.totalPrice, totalPrice) == 0 && name.equals(orderItem.name);
    }


}
