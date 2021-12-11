package DataLayer.DalObjects;

import Bussiness_Layer.TransportationPackage.Location;

public class DalLocation {
    private int id;
    private String address;
    private String phone;
    private String contactName;

    public DalLocation(int id, String address, String phone, String contactName) {
        this.setId(id);
        this.setAddress(address);
        this.setPhone(phone);
        this.setContactName(contactName);
    }

    public DalLocation(){}

    public DalLocation(Location location){
        this.setAddress(location.getAddress());
        this.setContactName(location.getContactName());
        this.setId(location.getId());
        this.setPhone(location.getPhone());
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
}
