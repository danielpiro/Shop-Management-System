package Facade_Layer.FacadeObjects;


import Bussiness_Layer.TransportationPackage.DeliveryDoc;
import Bussiness_Layer.TransportationPackage.Supplies;

import java.util.ArrayList;
import java.util.List;

public class DeliveryDocFacade {
    public int id;
    public String dest;
    public List<SuppliesFacade> supplies;
    public int deliveryID;

    public DeliveryDocFacade(int id, String dest, List<SuppliesFacade> s, int deliveryID) {
        this.id = id;
        this.dest = dest;
        this.setSupplies(s);
        this.deliveryID = deliveryID;
    }

    public DeliveryDocFacade(DeliveryDoc deliveryDoc, int deliveryID) {
        this.id = deliveryDoc.getId();
        this.dest = deliveryDoc.getDest();
        this.supplies = new ArrayList<>();
        for (Supplies supplies : deliveryDoc.getSupplies())
            this.supplies.add(new SuppliesFacade(supplies));
        this.deliveryID = deliveryID;

    }

    private String printSupplies() {
        StringBuilder out = new StringBuilder();
        for (SuppliesFacade contact : getSupplies()) {
            out.append(contact.toString()).append('\n');
        }
        return out.toString();
    }

    public List<SuppliesFacade> getSupplies() {
        return supplies;
    }

    public void setSupplies(List<SuppliesFacade> supplies) {
        this.supplies = supplies;
    }


    @Override
    public String toString() {
        return "DeliveryDocFacade{" +
                "id=" + id +
                ", dest='" + dest + '\'' +
                ", supplies=" + printSupplies() +
                ", deliveryID=" + deliveryID +
                '}';
    }
}
