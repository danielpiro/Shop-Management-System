package Bussiness_Layer.TransportationPackage;

import DataLayer.DalObjects.DalSupply;

public class Supplies {
    private int id;
    private String name;
    private int quantity;

    public Supplies(int id,String name , int quantity){
        this.id=id;
        this.name=name;
        this.quantity =quantity;
    }
    public Supplies(DalSupply dalSupply){
        this.id = dalSupply.getId();
        this.name = dalSupply.getName();
        this.quantity = dalSupply.getQuantity();
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

    public String toString(){
        return "id: "+ id + '\t'+"name: " + name ;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
