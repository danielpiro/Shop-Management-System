package Facade_Layer;

import Bussiness_Layer.Order.Order;
import Bussiness_Layer.Order.OrderController;
import Bussiness_Layer.Order.OrderItem;
import Bussiness_Layer.Supplier.BillsOfQuantities;
import Bussiness_Layer.Supplier.Contact;
import Bussiness_Layer.Supplier.Supplier;
import Bussiness_Layer.Supplier.SupplierController;
import Facade_Layer.FacadeObjects.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SuppliersFacade {

    private final OrderController orderController;
    private final SupplierController supplierController;
    private final InventoryFacade inventoryFacade;


    public SuppliersFacade(InventoryFacade inventoryFacade) throws SQLException {
        orderController = new OrderController();
        supplierController = new SupplierController();
        this.inventoryFacade = inventoryFacade;
    }

    public ResponseT<FacadeSupplier> getSupplier(int supplierBN) {
        try {
            FacadeSupplier dSupplier = new FacadeSupplier(supplierController.getSupplier(supplierBN));
            return new ResponseT<>(dSupplier);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response setSupplier(int supplierBN, FacadeSupplier dSupplier) {
        try {
            Supplier businessSupplier = supplierController.getSupplier(supplierBN);
            businessSupplier.setName(dSupplier.name);
            businessSupplier.setBusinessNumber(dSupplier.businessNumber);
            businessSupplier.setBankAccount(dSupplier.bankAccount);
            businessSupplier.setPaymentTerms(dSupplier.paymentTerms);
            businessSupplier.setTermsOfSupply(dSupplier.termsOfSupply);
            businessSupplier.setBillsOfQuantities(dSupplier.getBusinessBillsOfQuantities());
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }

    }

    public ResponseT<FacadeSupplier> addSupplier(FacadeSupplier dSupplier) {
        try {
            List<Contact> contacts = dSupplier.contactList.stream().
                    map(c->new Contact(c.name,c.phoneNumber,c.email)).collect(Collectors.toList());
            BillsOfQuantities bills = dSupplier.getBusinessBillsOfQuantities();
            Supplier supplier = supplierController.addSupplier(dSupplier.name, dSupplier.businessNumber, dSupplier.address, dSupplier.bankAccount,
                    dSupplier.paymentTerms, dSupplier.termsOfSupply,
                    bills, contacts, dSupplier.needDelivery);
            return new ResponseT<>(new FacadeSupplier(supplier));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }

    }

    public Response removeSupplier(int supplierBN) {
        try {
            supplierController.removeSupplier(supplierBN);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }


    }

    public Response updateContact(int supplierBN, String contactName, String newPhoneNumber, String newEmail) {
        try {
            supplierController.getSupplier(supplierBN).updateContact(contactName, newPhoneNumber, newEmail);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<FacadeContact> getContact(int supplierBN, String contactName) {
        try {
            FacadeContact c = new FacadeContact(supplierController.getSupplier(supplierBN).getContact(contactName));
            return new ResponseT<>(c);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());

        }
    }

    public Response addContact(int supplierBN,String contactName, String phoneNumber, String email){
        try {
            supplierController.getSupplier(supplierBN).addContact(contactName,phoneNumber,email);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response removeContact(int supplierBN, String contactName) {
        try {
            supplierController.getSupplier(supplierBN).removeContact(contactName);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }


    public List<ResponseT<FacadeSupplierItem>> getAllItems(int supplierBN){
        List<ResponseT<FacadeSupplierItem>> supplierItems = new LinkedList<>();
        try {
            Map <Integer,Integer> items = supplierController.getSupplier(supplierBN).getBillsOfQuantities().getCatalogToSerial();
            Map<Integer,Double> prices = supplierController.getSupplier(supplierBN).getBillsOfQuantities().getIncludedItemsAndPricing();
            for (Integer catalogNumber: items.keySet()) {
                int serial = items.get(catalogNumber);
                String name = inventoryFacade.getName(serial);
                if(name==null)
                    throw new IllegalArgumentException("supplier contains non-exist item");
                FacadeSupplierItem item = new FacadeSupplierItem(name,catalogNumber,serial,prices.get(catalogNumber));
                supplierItems.add(new ResponseT<>(item));
            }
        }catch (Exception e){
            supplierItems.add(new ResponseT<>(e.getMessage()));
        }
        return supplierItems;
    }

    public List<ResponseT<FacadeOrder>> getAllSupplierOrders(int supplierBN){
        List<ResponseT<FacadeOrder>> supplierOrders = new ArrayList<>();
        try {
            List<FacadeOrder> orders = orderController.getAllSupplierOrders(supplierBN).stream().map(
                    (o)-> {
                        String name = supplierController.getSupplier(o.getSupplierBN()).getName();
                        String phoneNumber = supplierController.getSupplier(o.getSupplierBN()).getContactList().get(0).getPhoneNumber();
                        return new FacadeOrder(o,name,phoneNumber);
                    }
            ).collect(Collectors.toList());
            for (FacadeOrder o : orders) {
                supplierOrders.add(new ResponseT<>(o));
            }
        }catch (Exception e){
            supplierOrders.add(new ResponseT<>(e.getMessage()));
        }
        return supplierOrders;
    }



    public Response removeItem(int serial) {
        try {
            supplierController.updateSupplierItems(serial);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeItemFromSupplier(int catalogNumber, int supplierBN) {
        try {
            supplierController.getSupplier(supplierBN).getBillsOfQuantities().removeItem(supplierBN,catalogNumber);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response placeOrder(int supplierBN, Map<Integer, Integer> itemsAndQuantities, int branch_id ){
        try {
            Supplier s = supplierController.getSupplier(supplierBN);
            BillsOfQuantities bills =s.getBillsOfQuantities();
            LocalDate supplyDate = s.getNextShipmentDay();
            Map<Integer,String> itemsNames = getItemsNames(bills.getCatalogToSerial());
            orderController.addOrder(itemsAndQuantities,itemsNames, supplierBN,supplyDate,bills, branch_id);
            if(s.isDeliveryNeeded()){
                List<Integer> dest = new ArrayList<>(1);
                dest.add(branch_id);
                List<Integer> itemsToDeliver = new ArrayList<>(itemsAndQuantities.keySet());
                for(Integer id: itemsToDeliver)
                {
                    int serialNumber = bills.getSerial(id);
                    inventoryFacade.updateNextSupDateByID(serialNumber,supplyDate);
                }
                int totalWeight = itemsToDeliver.stream().map(x->inventoryFacade.get_weight(bills.getSerial(x)*itemsAndQuantities.get(x))).mapToInt(Integer::intValue).sum();
                Contact c = s.getContactList().get(0);
                LocationFacade src_supplier = new LocationFacade(s.getBusinessNumber(), s.getAddress(),c.getPhoneNumber(), c.getName());
                TransportationFacade.getInstance().requestDelivery(supplyDate,src_supplier,dest,totalWeight, itemsToDeliver);
            }
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<FacadeOrder> getOrder(int orderID) {
        try {
            Order bOrder = orderController.getOrder(orderID);
            String name = supplierController.getSupplier(bOrder.getSupplierBN()).getName();
            String phoneNumber = supplierController.getSupplier(bOrder.getSupplierBN()).getContactList().get(0).getPhoneNumber();
            FacadeOrder dOrder = new FacadeOrder(bOrder,name,phoneNumber);
            return new ResponseT<>(dOrder);
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    public List<ResponseT<FacadeOrder>> getOrders(int supplierBN, String searchedDate) {
        List<ResponseT<FacadeOrder>> dOrders = new ArrayList<>();
        try {
            List<Order> businessOrders = orderController.getOrders(supplierBN, searchedDate);
            for (Order o : businessOrders) {
                String name = supplierController.getSupplier(o.getSupplierBN()).getName();
                String phoneNumber = supplierController.getSupplier(o.getSupplierBN()).getContactList().get(0).getPhoneNumber();
                dOrders.add(new ResponseT<>(new FacadeOrder(o,name,phoneNumber)));
            }
        } catch (Exception e) {
            dOrders.add(new ResponseT<>(e.getMessage()));
        }
        return dOrders;
    }

    public Response removeOrder(int id, int branch_id) {
        try {
            orderController.removeOrder(id, branch_id);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addItemToOrder(int id, int supplierBN, int itemToAdd, int amount) {
        try {
            BillsOfQuantities bills = supplierController.getSupplier(supplierBN).getBillsOfQuantities();
            String name = inventoryFacade.getName(bills.getSerial(itemToAdd));
            orderController.getOrder(id).addItemToOrder(itemToAdd,supplierBN,amount,name, bills);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response updateQuantities(int id, int supplierBN, int newAmount, int itemToUpdate) {
        try {
            BillsOfQuantities bills = supplierController.getSupplier(supplierBN).getBillsOfQuantities();
            orderController.getOrder(id).updateQuantities(itemToUpdate, newAmount, bills);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response changeDiscountPerItem(int supplierBN, int catalogNumber, int amount, double newDiscount) {
        try {
            supplierController.getSupplier(supplierBN).getBillsOfQuantities().changeDiscountPerItem(supplierBN,catalogNumber, amount, newDiscount);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response changeDiscountPerOrder(int supplierBN, int amount, double newDiscount) {
        try {
            supplierController.getSupplier(supplierBN).getBillsOfQuantities().changeDiscountPerOrder(supplierBN,amount, newDiscount);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeDiscountPerItem(int supplierBN,int catalogNumber, int amount){
        try{
            supplierController.getSupplier(supplierBN).getBillsOfQuantities().removeDiscountPerItem(supplierBN,catalogNumber, amount);
            return new Response();
        }catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeDiscountPerOrder(int supplierBN, int amount){
        try{
            supplierController.getSupplier(supplierBN).getBillsOfQuantities().removeDiscountPerOrder(supplierBN,amount);
            return new Response();
        }catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response addIncludedItem(int supplierBN, int serialNumber, int catalogNumber, double cost) {
        try {
            supplierController.getSupplier(supplierBN).getBillsOfQuantities().addIncludedItem(supplierBN,catalogNumber,serialNumber, cost);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addDiscountPerItem(int supplierBN, int catalogNumber, int amount, double discount) {
        try {
            supplierController.getSupplier(supplierBN).getBillsOfQuantities().addDiscountPerItem(supplierBN,catalogNumber, amount, discount);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addDiscountPerOrder(int supplierBN, int amount, double discount) {
        try {
            supplierController.getSupplier(supplierBN).getBillsOfQuantities().addDiscountPerOrder(supplierBN,amount, discount);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    private Map<Integer,String> getItemsNames(Map<Integer,Integer> catalogToSerial){
        Map<Integer,String> itemsNames = new HashMap<>();
        for (Integer catalog: catalogToSerial.keySet()) {
            int serial = catalogToSerial.get(catalog);
            String name = inventoryFacade.getName(serial);
            itemsNames.put(catalog,name);
        }
        return itemsNames;
    }

    public Response createShortageOrder(Map<Integer, Integer> itemAndQuantities, int branch_id, Map<Integer,Integer> weights) {
        try {
            for (int serialNumber : itemAndQuantities.keySet()) {
                int[] lowestSupplierAndCatalog = orderController.getLowestSupplier(serialNumber);
                int supplier = lowestSupplierAndCatalog[0], catalogNumber = lowestSupplierAndCatalog[1], amount = itemAndQuantities.get(serialNumber);
                int orderID = orderController.existOpenOrder(lowestSupplierAndCatalog[0], branch_id);
                BillsOfQuantities bills = supplierController.getBills(supplier);
                if (orderID != -1) {
                    Order order = orderController.getOrder(orderID);
                    if(order.containItem(catalogNumber)) {
                        order.updateQuantities(catalogNumber, amount, bills);
                    }
                    else {
                        String name = inventoryFacade.getName(serialNumber);
                        order.addItemToOrder(catalogNumber, supplier, amount, name, bills);
                    }
                    inventoryFacade.updateNextSupDateByID(serialNumber,order.getSupplyDate());
                } else {
                    Map<Integer, Integer> item = new HashMap<>(1);
                    item.put(catalogNumber, amount);
                    LocalDate supplyDate = supplierController.getSupplier(supplier).getNextShipmentDay();
                    Map<Integer, String> itemsNames = getItemsNames(bills.getCatalogToSerial());
                    Supplier s = supplierController.getSupplier(supplier);
                    inventoryFacade.updateNextSupDateByID(serialNumber,supplyDate);
                    orderController.addOrder(item, itemsNames, supplier, supplyDate, bills, branch_id);
                    if (s.isDeliveryNeeded()) {
                        List<Integer> dest = new ArrayList<>(1);
                        dest.add(branch_id);
                        Contact c = s.getContactList().get(0);
                        LocationFacade src_supplier = new LocationFacade(s.getBusinessNumber(), s.getAddress(),c.getPhoneNumber(), c.getName());
                        TransportationFacade.getInstance().addLocation(src_supplier.id,src_supplier.address,src_supplier.phone,src_supplier.contactName,111);
                        TransportationFacade.getInstance().requestDelivery(supplyDate,src_supplier,dest,weights.get(serialNumber), new ArrayList<>(item.keySet()));
                    }
                }

            }
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public List<ResponseT<Integer>> closeOrder(int orderNumber, int branch_id){
        List<ResponseT<Integer>> items = new ArrayList<>();
        try {
            Order order = orderController.getOrder(orderNumber);
            order.closeOrder();
            Supplier supplier = supplierController.getSupplier(order.getSupplierBN());
            if (supplier.isRegularSupplier()) {
                LocalDate supplyDate = orderController.CreateCopyOrder(order, supplier.getBillsOfQuantities());
                if(supplier.isDeliveryNeeded()){
                    List<Integer> dest = new ArrayList<>(1);
                    dest.add(branch_id);
                    List<Integer> itemsToDeliver =order.getOrderedItems().stream().map(OrderItem::getId).collect(Collectors.toList());
                    int totalWeight = order.getOrderedItems().stream().map(x->inventoryFacade.get_weight(supplier.getBillsOfQuantities().getSerial(x.getId()))*x.getAmount()).mapToInt(Integer::intValue).sum();
                    Contact c = supplier.getContactList().get(0);
                    LocationFacade src_supplier = new LocationFacade(supplier.getBusinessNumber(), supplier.getAddress(),c.getPhoneNumber(), c.getName());
                    TransportationFacade.getInstance().requestDelivery(supplyDate,src_supplier,dest,totalWeight, itemsToDeliver);
                }

            }
            BillsOfQuantities b = supplier.getBillsOfQuantities();
            List<Integer> catalogs = order.getOrderedItems().stream().map(OrderItem::getId).collect(Collectors.toList());
            for (Integer catalog: catalogs) {
                 items.add(new ResponseT<>(b.getSerial(catalog)));
            }


        }catch (Exception e){
           items.add(new ResponseT<>(e.getMessage()));
        }
        return items;
    }
}

