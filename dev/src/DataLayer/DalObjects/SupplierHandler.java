package DataLayer.DalObjects;

import Bussiness_Layer.Supplier.BillsOfQuantities;
import Bussiness_Layer.Supplier.Contact;
import Bussiness_Layer.Supplier.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.*;

public class SupplierHandler extends Handler<Supplier> {

    private final static SupplierHandler instance = new SupplierHandler();

    private SupplierHandler() {
        super();
        createTables();
    }

    public static SupplierHandler getInstance() {
        return instance;
    }


    @Override
    public void insert(Supplier toInsert) {
        try (Connection con = getConnection()) {
            con.setAutoCommit(false);
            String query = "INSERT INTO Suppliers (supplierBN, name,address, bankAccount, termOfPayment, needDelivery) " +
                    "VALUES (?,?,?,?,?,?);";


            int bn = toInsert.getBusinessNumber();

            PreparedStatement s = con.prepareStatement(query);
            s.setInt(1, bn);
            s.setString(2, toInsert.getName());
            s.setString(3,toInsert.getAddress());
            s.setInt(4, toInsert.getBankAccount());
            s.setString(5, toInsert.getPaymentTerms().toString());
            s.setBoolean(6, toInsert.isDeliveryNeeded());
            s.executeUpdate();

            for (Contact c : toInsert.getContactList()
            ) {
                insertContacts(bn, c, con);
            }

            insertBills(bn, toInsert.getBillsOfQuantities(), con);

            for (DayOfWeek d : toInsert.getTermsOfSupply()) {
                insertTermsOfSupply(bn, d, con);
            }

            con.commit();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

    }

    @Override
    public void delete(Supplier toDelete) {

    }

    private void insertBills(int bn, BillsOfQuantities bills, Connection con) throws SQLException {
        for (Integer catalog : bills.getIncludedItemsAndPricing().keySet()) {
            int serial = bills.getCatalogToSerial().get(catalog);
            double price = bills.getIncludedItemsAndPricing().get(catalog);
            insertIncludedItems(bn, catalog, serial, price, con);
        }
        for (Integer amount : bills.getDiscountPerOrder().keySet()) {
            double discount = bills.getDiscountPerOrder().get(amount);
            insertDiscountsPerOrder(bn, amount, discount, con);
        }
        for (Integer catalog : bills.getDiscountPerItem().keySet()) {
            Map<Integer, Double> discounts = bills.getDiscountPerItem().get(catalog);
            for (Integer amount : discounts.keySet()) {
                double discount = discounts.get(amount);
                insertDiscountsPerItems(bn, catalog, amount, discount, con);
            }
        }

    }

    private void insertTermsOfSupply(int supplierBN, DayOfWeek day, Connection con) {
        PreparedStatement s;
        String query = "INSERT INTO SuppliersTermsOfSupply (supplierBN, day) VALUES (?,?)";

        try {
            s = con.prepareStatement(query);
            s.setInt(1, supplierBN);
            s.setString(2, day.name());
            s.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void insertContacts(int supplierBN, Contact c, Connection con) throws SQLException {
        boolean toClose = false;
        if (con == null) {
            toClose = true;
            con = getConnection();
        }
        PreparedStatement s;
        String query = "INSERT INTO Contacts (name, supplierBN, phoneNumber, email ) VALUES (?,?,?,?);";

        try {
            s = con.prepareStatement(query);
            s.setString(1, c.getName());
            s.setInt(2, supplierBN);
            s.setString(3, c.getPhoneNumber());
            s.setString(4, c.getEmail());
            s.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        if (toClose)
            con.close();
    }

    public void insertIncludedItems(int supplierBN, int catalog, int serialNumber, double price, Connection con) throws SQLException {
        boolean toClose = false;
        if (con == null) {
            toClose = true;
            con = getConnection();
        }
        PreparedStatement s;
        String query = "INSERT INTO SuppliersIncludedItems (supplierBN, catalogNumber,serialNumber,price) VALUES (?,?,?,?);";
        try {
            s = con.prepareStatement(query);
            s.setInt(1, supplierBN);
            s.setInt(2, catalog);
            s.setInt(3, serialNumber);
            s.setDouble(4, price);
            s.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        if (toClose)
            con.close();
    }

    public void insertDiscountsPerOrder(int supplierBN, int amount, double discount, Connection con) throws SQLException {
        boolean toClose = false;
        if (con == null) {
            toClose = true;
            con = getConnection();

            PreparedStatement s;
            String query = "INSERT INTO SuppliersOrdersDiscounts (supplierBN, amount, discount) VALUES(?,?,?);";
            try {
                s = con.prepareStatement(query);
                s.setInt(1, supplierBN);
                s.setInt(2, amount);
                s.setDouble(3, discount);
                s.executeUpdate();

            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
            if (toClose)
                con.close();
        }
    }

    public void insertDiscountsPerItems(int supplierBN, int catalog, int amount, double discount, Connection con) throws SQLException {
        PreparedStatement s;
        String query = "INSERT INTO SuppliersItemsDiscounts (supplierBN, catalogNumber, amount, discount) VALUES (?,?,?,?);";

        boolean toClose = false;
        if (con == null) {
            toClose = true;
            con = getConnection();
        }
        try {
            s = con.prepareStatement(query);
            s.setInt(1, supplierBN);
            s.setInt(2, catalog);
            s.setInt(3, amount);
            s.setDouble(4, discount);
            s.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        if (toClose)
            con.close();
    }


    public void delete(int key) {
        try (Connection con = getConnection()) {
            String query = "DELETE FROM Suppliers WHERE supplierBN = ?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setInt(1, key);
            s.executeUpdate();

            identityMap.remove(key);

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public Supplier get(int key) {
        int bankAccount = 0;
        String name = null;
        String address = null;
        Supplier.PaymentTerms termOfPayment = null;
        boolean needDelivery = false;

        try (Connection con = getConnection()) {
            String query = "SELECT * FROM Suppliers WHERE supplierBN =?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setInt(1, key);
            ResultSet record = s.executeQuery();

            while (record.next()) {
                bankAccount = record.getInt("bankAccount");
                name = record.getString("name");
                address = record.getString("address");
                termOfPayment = Supplier.PaymentTerms.valueOf(record.getString("termOfPayment"));
                needDelivery = record.getBoolean("needDelivery");
            }

            List<Contact> contacts = getContactList(key, con);
            BillsOfQuantities bills = getBills(key, con);
            Set<DayOfWeek> supplyTerms = getTermsOfSupply(key, con);

            Supplier supplier = new Supplier(name, key,address, bankAccount, termOfPayment, supplyTerms, bills, contacts, needDelivery);
            identityMap.put(supplier.getBusinessNumber(), supplier);
            return supplier;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

    }

    @Override
    public void update(Supplier toUpdate) {

    }

    public void updateName(int supplierBN, String newName) {
        try (Connection con = getConnection()) {
            String query = "UPDATE Suppliers SET name=? WHERE supplierBN =?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setString(1, newName);
            s.setInt(2, supplierBN);

            s.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void updateBankAccount(int supplierBN, int bankAccount) {
        try (Connection con = getConnection()) {
            String query = "UPDATE Suppliers SET bankAccount=? WHERE supplierBN =?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setInt(1, bankAccount);
            s.setInt(2, supplierBN);

            s.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void updatePayment(int supplierBN, Supplier.PaymentTerms paymentTerms) {
        try (Connection con = getConnection()) {
            String query = "UPDATE Suppliers SET paymentTerms=? WHERE supplierBN =?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setString(1, paymentTerms.name());
            s.setInt(2, supplierBN);

            s.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    public void deleteItemFromAgreement(int supplierBN, int catalogNumber) {
        PreparedStatement s;
        String query = "DELETE FROM SuppliersIncludedItems WHERE supplierBN=? AND catalogNumber=?;";

        try (Connection con = getConnection()) {
            s = con.prepareStatement(query);
            s.setInt(1, supplierBN);
            s.setInt(2, catalogNumber);
            s.executeUpdate();

            query = "DELETE FROM SuppliersItemsDiscounts WHERE supplierBN=? AND catalogNumber=?;";
            s = con.prepareStatement(query);
            s.setInt(1, supplierBN);
            s.setInt(2, catalogNumber);
            s.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void deleteContact(int supplierBN, String contactName) {
        try (Connection con = getConnection()) {
            String query = "DELETE FROM Contacts WHERE supplierBN = ? AND contactName=?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setInt(1, supplierBN);
            s.setString(2, contactName);
            s.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void updateContact(int supplierBN, String name, String newPhoneNumber, String newEmail) {
        try (Connection con = getConnection()) {
            String query = "UPDATE Contacts SET email=? AND phoneNumber=? WHERE supplierBN =? AND contactName=?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setString(1, newEmail);
            s.setString(2, newPhoneNumber);
            s.setInt(3, supplierBN);
            s.setString(4, name);
            s.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void updateDiscountPerItem(int supplierBN, int catalogNumber, int amount, double newDiscount) {
        try (Connection con = getConnection()) {
            String query = "UPDATE SuppliersItemsDiscounts SET discount=? WHERE supplierBN = ? AND catalogNumber=? AND amount=?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setDouble(1, newDiscount);
            s.setInt(2, supplierBN);
            s.setInt(3, catalogNumber);
            s.setInt(4, amount);
            s.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void updateDiscountPerOrder(int supplierBN, int amount, double newDiscount) {
        try (Connection con = getConnection()) {
            String query = "UPDATE SuppliersOrdersDiscounts SET discount=? WHERE supplierBN =? AND amount=?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setDouble(1, newDiscount);
            s.setInt(2, supplierBN);
            s.setInt(3, amount);
            s.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void deleteDiscountPerItem(int supplierBN, int catalogNumber, int amount) {
        try (Connection con = getConnection()) {
            String query = "DELETE FROM SuppliersItemsDiscounts WHERE supplierBN =? AND catalogNumber=? AND amount=?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setInt(1, supplierBN);
            s.setInt(2, catalogNumber);
            s.setInt(3, amount);
            s.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void deleteDiscountPerOrder(int supplierBN, int amount) {
        try (Connection con = getConnection()) {
            String query = "DELETE FROM SuppliersOrdersDiscounts WHERE supplierBN =? AND amount=?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setInt(1, supplierBN);
            s.setInt(2, amount);
            s.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


    private Set<DayOfWeek> getTermsOfSupply(int key, Connection con) {
        Set<DayOfWeek> days = new HashSet<>();
        try {
            String query = "SELECT * FROM SuppliersTermsOfSupply WHERE supplierBN=?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setInt(1, key);
            ResultSet records = s.executeQuery();

            while (records.next()) {
                DayOfWeek day = DayOfWeek.valueOf(records.getString("day"));
                days.add(day);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return days;
    }


    private List<Contact> getContactList(int supplierBN, Connection con) {
        List<Contact> contacts = new ArrayList<>();
        try {
            String query = "SELECT * FROM Contacts WHERE supplierBN =?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setInt(1, supplierBN);
            ResultSet records = s.executeQuery();

            while (records.next()) {
                String name = records.getString("name");
                String phone = records.getString("phoneNumber");
                String email = records.getString("email");

                contacts.add(new Contact(name, phone, email));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contacts;
    }

    public BillsOfQuantities getBills(int supplierBN, Connection con) {

        Map<Integer, Double> itemsAndPrice = new HashMap<>();
        Map<Integer, Integer> catalogToSerial = new HashMap<>();
        boolean toClose = con == null;
        if (toClose)
            con = getConnection();

        try {
            String query = "SELECT * FROM SuppliersIncludedItems WHERE supplierBN=?;";
            PreparedStatement s = con.prepareStatement(query);
            s.setInt(1, supplierBN);
            ResultSet records = s.executeQuery();

            while (records.next()) {
                int catalogNumber = records.getInt("catalogNumber");
                int serialNumber = records.getInt("serialNumber");
                double price = records.getDouble("price");

                itemsAndPrice.put(catalogNumber, price);
                catalogToSerial.put(catalogNumber, serialNumber);
            }

            Map<Integer, Map<Integer, Double>> itemsDiscounts = getItemsDiscounts(supplierBN, itemsAndPrice, con);
            Map<Integer, Double> discountsPerOrders = getOrdersDiscounts(supplierBN, con);

            if (toClose)
                con.close();

            return new BillsOfQuantities(itemsAndPrice, catalogToSerial, discountsPerOrders, itemsDiscounts);

        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }


    }

    private Map<Integer, Map<Integer, Double>> getItemsDiscounts(int supplierBN, Map<
            Integer, Double> itemsAndPrice, Connection con) throws SQLException {
        PreparedStatement s;
        String query = "SELECT * FROM SuppliersItemsDiscounts WHERE supplierBN=? AND catalogNumber=?;";
        Map<Integer, Map<Integer, Double>> itemDiscounts = new HashMap<>();

        try {
            for (Integer item : itemsAndPrice.keySet()) {
                Map<Integer, Double> discounts = new HashMap<>();

                s = con.prepareStatement(query);
                s.setInt(1, supplierBN);
                s.setInt(2, item);
                ResultSet records = s.executeQuery();

                while (records.next()) {
                    int amount = records.getInt("amount");
                    double discount = records.getDouble("discount");
                    discounts.put(amount, discount);
                }
                itemDiscounts.put(item, discounts);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return itemDiscounts;
    }

    private Map<Integer, Double> getOrdersDiscounts(int supplierBN, Connection con) throws SQLException {
        Map<Integer, Double> ordersDiscounts = new HashMap<>();
        PreparedStatement s;
        String query = "SELECT * FROM SuppliersOrdersDiscounts WHERE supplierBN=?;";

        try {
            s = con.prepareStatement(query);
            s.setInt(1, supplierBN);

            ResultSet records = s.executeQuery();
            while (records.next()) {
                int amount = records.getInt("amount");
                double discount = records.getDouble("discount");
                ordersDiscounts.put(amount, discount);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ordersDiscounts;
    }


}
