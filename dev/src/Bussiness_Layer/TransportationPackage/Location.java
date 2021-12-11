package Bussiness_Layer.TransportationPackage;

import DataLayer.DalObjects.DalLocation;
import Facade_Layer.FacadeObjects.LocationFacade;

public class Location {
    private int id;
    private String address;
    private String phone;
    private String contactName;

    public Location(int id,String name, String phone, String contactName) {
        this.id=id;
        this.address=name;
        this.phone=phone;
        this.contactName=contactName;
    }

    public Location(LocationFacade locationFacade) {
        this.id = locationFacade.id;
        this.address = locationFacade.address;
        this.contactName = locationFacade.contactName;
        this.phone = locationFacade.phone;

    }
    public Location(DalLocation dalLocation){
        this.id = dalLocation.getId();
        this.address = dalLocation.getAddress();
        this.phone = dalLocation.getPhone();
        this.contactName = dalLocation.getContactName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String toString(){
        return "id: "+ id + '\t'+"address: " + address +'\t'+"contact name: "+contactName+'\t'+"phone: "+phone+'\n';
    }
}
