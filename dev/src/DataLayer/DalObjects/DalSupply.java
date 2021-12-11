package DataLayer.DalObjects;

import Bussiness_Layer.TransportationPackage.Supplies;

public class DalSupply {
    private int id;
    private String name;
    private int quantity;
    public DalSupply(int id, String name , int quantity) {
        this.setId(id);
        this.setName(name);
        this.setQuantity(quantity);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DalSupply(){}
    public DalSupply(Supplies supplies){
        this.setId(supplies.getId());
        this.setName(supplies.getName());
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }
}
