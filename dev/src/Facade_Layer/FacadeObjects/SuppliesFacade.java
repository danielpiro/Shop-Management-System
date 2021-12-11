package Facade_Layer.FacadeObjects;


import Bussiness_Layer.TransportationPackage.Supplies;

public class SuppliesFacade {
    public int id;
    public String name;
    public int quantity;

    public SuppliesFacade(int id,String name , int quantity){
        this.id=id;
        this.name=name;
        this.quantity = quantity;
    }

    public SuppliesFacade(Supplies supplies){
        this.id=supplies.getId();
        this.name=supplies.getName();
        this.quantity = supplies.getQuantity();
    }

    @Override
    public String toString(){
        return "id: "+ id + '\t'+"name: " + name ;
    }
}
