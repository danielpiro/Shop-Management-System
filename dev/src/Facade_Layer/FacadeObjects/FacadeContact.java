package Facade_Layer.FacadeObjects;

import Bussiness_Layer.Supplier.Contact;

public class FacadeContact {

    public final String name;
    public String phoneNumber;
    public String email;

    public FacadeContact(String name, String phoneNumber, String email){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public FacadeContact(Contact businessContact){
        name = businessContact.getName();
        phoneNumber = businessContact.getPhoneNumber();
        email = businessContact.getEmail();
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name:'" + name + '\n' +
                ", phoneNumber:'" + phoneNumber + '\n' +
                ", email:'" + email + '\n' +
                '}';
    }
}
