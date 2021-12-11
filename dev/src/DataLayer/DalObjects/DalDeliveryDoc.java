package DataLayer.DalObjects;

import Bussiness_Layer.TransportationPackage.DeliveryDoc;
import Bussiness_Layer.TransportationPackage.Supplies;

import java.util.LinkedList;
import java.util.List;

public class DalDeliveryDoc {
    private int deliveryID;
    private int id;
    private String dest;
    private List<DalSupply> supplies;
    private List<Supplies> supplies2;

//    public DalDeliveryDoc(int id, String dest, List<DalSupply> supplies, int deliveryID) {
//        this.deliveryID = deliveryID;
//        this.id = id;
//        this.dest = dest;
//        this.supplies = supplies;
//    }
    public DalDeliveryDoc(int id, String dest, List<Supplies> supplies, int deliveryID) {
        this.deliveryID = deliveryID;
        this.id = id;
        this.dest = dest;
        this.supplies2 = supplies;
    }

    public DalDeliveryDoc() {
    }

    public DalDeliveryDoc(DeliveryDoc deliveryDoc, int deliveryID) {
        this.deliveryID = deliveryID;
        this.dest = deliveryDoc.getDest();
        this.id = deliveryDoc.getId();
        this.supplies = new LinkedList<>();
        for (Supplies supply : deliveryDoc.getSupplies()) {
            this.supplies.add(new DalSupply(supply));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeliveryID() {
        return id;
    }

    public void setDeliveryID(int deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public List<DalSupply> getSupplies() {
        return supplies;
    }

    public void setSupplies(List<DalSupply> supplies) {
        this.supplies = supplies;
    }


}
