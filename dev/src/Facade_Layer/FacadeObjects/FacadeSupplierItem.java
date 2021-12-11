package Facade_Layer.FacadeObjects;

public class FacadeSupplierItem {
    public final String name;
    public final int serialNUmber;
    public final int catalogNumber;
    public final double price;

    public FacadeSupplierItem(String name, int catalogNumber, int serialNUmber, double price){
        this.name = name;
        this.catalogNumber = catalogNumber;
        this.serialNUmber = serialNUmber;
        this.price = price;
    }
}
