package Bussiness_Layer.Supplier;


import DataLayer.DalObjects.SupplierHandler;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SupplierController {
    private final Map<Integer, Supplier> suppliers;

    public SupplierController() {
        suppliers = new HashMap<>();
    }

    public Supplier getSupplier(int supplierBN) {
        if (suppliers.containsKey(supplierBN))
            return suppliers.get(supplierBN);
        else {
            Supplier s = SupplierHandler.getInstance().get(supplierBN);
            if (s != null) {
                suppliers.put(s.getBusinessNumber(), s);
                return s;
            } else
                throw new IllegalArgumentException("supplier does not exist");
        }

    }

    public Supplier addSupplier(String name, int BN, String address, int bankAccount, Supplier.PaymentTerms paymentTerms, Set<DayOfWeek> termsOfSupply,
                                BillsOfQuantities bills, List<Contact> contacts, boolean needDelivery) {
        if (suppliers.containsKey(BN))
            throw new IllegalArgumentException("supplier already exists");
        Supplier newSupplier = new Supplier(name, BN,address, bankAccount, paymentTerms, termsOfSupply, bills, contacts, needDelivery);
        SupplierHandler.getInstance().insert(newSupplier);
        suppliers.put(BN, newSupplier);
        return newSupplier;
    }

    public void removeSupplier(int supplierBN) {
        SupplierHandler.getInstance().delete(supplierBN);
        suppliers.remove(supplierBN);
    }

    public BillsOfQuantities getBills(int supplierBN) throws SQLException {
        if(suppliers.containsKey(supplierBN)){
            return suppliers.get(supplierBN).getBillsOfQuantities();
        }
        return SupplierHandler.getInstance().getBills(supplierBN,null);
    }

    public void updateSupplierItems(int serialNumber){
        for(Supplier s:suppliers.values()){
            s.getBillsOfQuantities().removeItem(s.getBusinessNumber(),serialNumber);
        }
    }

}
