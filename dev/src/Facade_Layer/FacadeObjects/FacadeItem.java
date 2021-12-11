package Facade_Layer.FacadeObjects;

import Bussiness_Layer.Item.Item;

public class FacadeItem {
    public final int serialNumber;
    public final String name;

    public FacadeItem(int serialNumber, String name){
        this.serialNumber = serialNumber;
        this.name = name;
    }

    public FacadeItem(Item businessItem){
        this.serialNumber = businessItem.getId();
        this.name = businessItem.getItemName();
    }

    @Override
    public String toString() {
        return  name + '\t' + serialNumber +'\n';
    }

    public void print(){
        String leftAlignFormat = "| %-15s | %-15d |%n";

        System.out.format("+-----------------+-----------------+%n");
        System.out.format("| Item Name       | Serial Number   |%n");
        System.out.format("+-----------------+-----------------+%n");
        System.out.format(leftAlignFormat, name, serialNumber);
        System.out.format("+-----------------+-----------------+%n");
        System.out.flush();
    }
}
