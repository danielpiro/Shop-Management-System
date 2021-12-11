package Bussiness_Layer.TransportationPackage;

import Bussiness_Layer.BussinessDataAccess;
import DataLayer.DalObjects.DalDeliveryDoc;
import DataLayer.DalObjects.DalSupply;
import Facade_Layer.FacadeObjects.SuppliesFacade;

import java.util.List;

public class DeliveryDoc {
    private int id;
    private String dest;
    private List<Supplies> supplies;


    public DeliveryDoc(int id, String dest, List<Supplies> supplies) {
        this.id = id;
        this.dest = dest;
        this.supplies = supplies;
    }

    public DeliveryDoc(DalDeliveryDoc dalDeliveryDoc) {
        this.id = dalDeliveryDoc.getId();
        this.dest = dalDeliveryDoc.getDest();
        for (DalSupply dalSupply : dalDeliveryDoc.getSupplies()) {
            supplies.add(new Supplies(dalSupply));
        }
    }

    public void addNewSupply(Supplies supply) throws Exception {
        supplies.add(supply);
        throw new Exception("Problem to add new supply to system, please try again!");
    }

    public void removeSupplies(int id) throws Exception {
        for (Supplies supply : supplies) {
            if (supply.getId() == id) {
                supplies.remove(supply);
                break;
            }
        }
        BussinessDataAccess bussinessDataAccess = BussinessDataAccess.getBTD();
        if (!bussinessDataAccess.removeSup(this.id, id))
            throw new Exception("The id :" + id + " not found!");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public List<Supplies> getSupplies() {
        return supplies;
    }

    public void setSupplies(List<Supplies> supplies) {
        this.supplies = supplies;
    }

    public String toString() {
        return "DeliveryDocFacade{" +
                "id='" + id + '\'' +
                ", dest=" + dest +
                ", departureTime=" + printSupplies() +
                '}';
    }

    private String printSupplies() {
        StringBuilder out = new StringBuilder();
        for (Supplies contact : getSupplies()) {
            out.append(contact.toString()).append('\n');
        }
        return out.toString();
    }
}
