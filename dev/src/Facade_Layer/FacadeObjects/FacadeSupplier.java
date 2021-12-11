package Facade_Layer.FacadeObjects;

import Bussiness_Layer.Supplier.BillsOfQuantities;
import Bussiness_Layer.Supplier.Supplier;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FacadeSupplier {
    public String name;
    public final int businessNumber;
    public final String address;
    public int bankAccount;
    public Supplier.PaymentTerms paymentTerms;
    public Set<DayOfWeek> termsOfSupply;
    public final FacadeBillsOfQuantities billsOfQuantities;
    public List<FacadeContact> contactList;
    public boolean needDelivery;

    public FacadeSupplier(String name, int businessNumber,String address, int bankAccount, Supplier.PaymentTerms paymentTerms, Set<DayOfWeek> termsOfSupply,
                          FacadeBillsOfQuantities billsOfQuantities, List<FacadeContact> contactList,boolean needDelivery){
        this.name = name;
        this.businessNumber = businessNumber;
        this. bankAccount = bankAccount;
        this.paymentTerms = paymentTerms;
        this.termsOfSupply = termsOfSupply;
        this.billsOfQuantities = billsOfQuantities;
        this.contactList = contactList;
        this.needDelivery = needDelivery;
        this.address = address;

    }

    public FacadeSupplier(Supplier businessSupplier){
        name = businessSupplier.getName();
        businessNumber = businessSupplier.getBusinessNumber();
        bankAccount = businessSupplier.getBankAccount();
        paymentTerms = businessSupplier.getPaymentTerms();
        termsOfSupply = businessSupplier.getTermsOfSupply();
        billsOfQuantities = new FacadeBillsOfQuantities(businessSupplier.getBillsOfQuantities());
        contactList = businessSupplier.getContactList().stream().map(FacadeContact::new).collect(Collectors.toList());
        address = businessSupplier.getAddress();
    }

    public FacadeBillsOfQuantities getBillsOfQuantities() {
        return billsOfQuantities;
    }

    public BillsOfQuantities getBusinessBillsOfQuantities(){

        return new BillsOfQuantities(billsOfQuantities.includedItemsAndPricing, billsOfQuantities.catalogToSerial, billsOfQuantities.discountPerOrder, billsOfQuantities.discountPerItem);
    }

    public void printContacts(){
        String leftAlignFormat_int = "| %-19s | %-18s | %-28s |%n";
        System.out.println("Contacts:");
        System.out.format("+---------------------+--------------------+------------------------------+%n");
        System.out.format("| name                | phone number       | email                        |%n");
        System.out.format("+---------------------+---------------------------------------------------+%n");
        contactList.forEach((x) -> System.out.format(leftAlignFormat_int, x.name,x.phoneNumber,x.email));
        System.out.format("+---------------------+--------------------+------------------------------+%n%n");
    }

    public void printSupplier() {
        System.out.printf("Name: %s%nBN: %d%nAddress: %s%nBank account: %d%nPayment Terms: %s%nTerms of supply: %s%n%n",name,businessNumber,address,bankAccount,paymentTerms,termsOfSupply);
    }


}
