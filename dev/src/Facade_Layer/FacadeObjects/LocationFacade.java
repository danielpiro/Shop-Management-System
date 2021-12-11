package Facade_Layer.FacadeObjects;


import Bussiness_Layer.TransportationPackage.Location;

public class LocationFacade {
    public int id;
    public String address;
    public String phone;
    public String contactName;

    public LocationFacade() {
    }

    public LocationFacade(int id, String name, String phone, String contactName) {
        this.id = id;
        this.address = name;
        this.phone = phone;
        this.contactName = contactName;
    }

    public LocationFacade(Location location) {
        this.id = location.getId();
        this.address = location.getAddress();
        this.phone = location.getPhone();
        this.contactName = location.getContactName();
    }

    @Override
    public String toString() {
        return "id: " + id + '\t' + "address: " + address + '\t' + "contact name: " + contactName + '\t' + "phone: " + phone + '\n';
    }
}
