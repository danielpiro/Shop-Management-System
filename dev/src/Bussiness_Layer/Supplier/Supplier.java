package Bussiness_Layer.Supplier;

import Bussiness_Layer.Utility;
import DataLayer.DalObjects.SupplierHandler;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.time.DayOfWeek;
import java.util.Set;


public class Supplier {

    public enum PaymentTerms{
        EOM30, EOM60, EOM90, cash
    }

    private String name;
    private int businessNumber;
    private String address;
    private int bankAccount;
    private PaymentTerms paymentTerms;
    private Set<DayOfWeek> termsOfSupply;
    private BillsOfQuantities billsOfQuantities;
    private final List<Contact> contactList;


    private final boolean isDeliveryNeeded;

    public Supplier(String name, int businessNumber, String address, int bankAccount, PaymentTerms paymentTerms, Set<DayOfWeek> termsOfSupply, BillsOfQuantities bills, List<Contact> contactList ,boolean isDeliveryNeeded){
        this.name = name;
        this.businessNumber = businessNumber;
        this.bankAccount = bankAccount;
        this.paymentTerms = paymentTerms;
        this.termsOfSupply = termsOfSupply;
        this.billsOfQuantities = bills;
        this.contactList = contactList;
        this.isDeliveryNeeded = isDeliveryNeeded;
        this.address = address;
    }

    public int getBusinessNumber() {
        return businessNumber;
    }

    public String getName() {
        return name;
    }

    public int getBankAccount() {
        return bankAccount;
    }

    public PaymentTerms getPaymentTerms() {
        return paymentTerms;
    }

    public Set<DayOfWeek> getTermsOfSupply() {
        return termsOfSupply;
    }

    public BillsOfQuantities getBillsOfQuantities() {
        return billsOfQuantities;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setName(String name) {
        SupplierHandler.getInstance().updateName(businessNumber,name);
        this.name = name;
    }

    public void setBusinessNumber(int businessNumber) {
        this.businessNumber = businessNumber;
    }

    public void setBillsOfQuantities(BillsOfQuantities billsOfQuantities) {
        this.billsOfQuantities = billsOfQuantities;
    }

    public void setBankAccount(int bankAccount) {
        SupplierHandler.getInstance().updateBankAccount(businessNumber,bankAccount);
        this.bankAccount = bankAccount;
    }

    public void setPaymentTerms(PaymentTerms paymentTerms) {
        SupplierHandler.getInstance().updatePayment(businessNumber,paymentTerms);
        this.paymentTerms = paymentTerms;
    }

    //TODO: add remove day, add day
    public void setTermsOfSupply(Set<DayOfWeek> termsOfSupply) {
        if(isDeliveryNeeded)
            throw new IllegalArgumentException("on demand supplier can't have regular shipment day");

        this.termsOfSupply = termsOfSupply;
    }

    public void addContact(String contactName, String phoneNumber, String email) throws SQLException {
        Contact c = new Contact(contactName,phoneNumber,email);
        SupplierHandler.getInstance().insertContacts(businessNumber,c,null);

        contactList.add(c);
    }

    public void removeContact(String contactName) {
        SupplierHandler.getInstance().deleteContact(businessNumber,contactName);
        contactList.removeIf(c -> c.getName().equals(contactName));
    }


    public void updateContact(String contactName, String newPhoneNumber, String newEmail) {
        SupplierHandler.getInstance().updateContact(businessNumber,contactName,newPhoneNumber,newEmail);
        for (Contact c : contactList
        ) {
            if (c.getName().equals(contactName)) {
                c.setPhoneNumber(newPhoneNumber);
                c.setEmail(newEmail);
            }
        }
    }

    public Contact getContact(String contactName) {
        Contact toReturn = null;
        for (Contact c : contactList
        ) {
            if (c.getName().equals(contactName))
                toReturn = c;
        }
        return toReturn;
    }

    public boolean isRegularSupplier(){
        return !termsOfSupply.isEmpty();
    }

    public boolean isDeliveryNeeded() {
        return isDeliveryNeeded;
    }

    public LocalDate getNextShipmentDay(){
        if(termsOfSupply.isEmpty())
            return LocalDate.now().plusDays(7);
        else{
            LocalDate today = LocalDate.now();
            LocalDate supplyDate = termsOfSupply.stream().map((x)-> today.
                    with(TemporalAdjusters.next(x))).sorted().iterator().next();
            LocalDate next_sunday = Utility.getUpcomingDate(DayOfWeek.SUNDAY);// function that brings back the date of the next date of sunday
            if(supplyDate.isAfter(next_sunday))
                return supplyDate;
            return supplyDate.plusDays(7);
        }
    }

    public String getAddress() {
        return address;
    }
}
