package DataLayer.DalObjects;


import Bussiness_Layer.Order.Order;
import Bussiness_Layer.Order.OrderItem;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderHandler extends Handler<Order> {

    private static final OrderHandler instance = new OrderHandler();

    private OrderHandler(){
        super();
        createTables();
    }


    public static OrderHandler getInstance() {
        return instance;
    }

    @Override
    public void insert(Order toInsert) {
        Connection con = getConnection();
        String query = "INSERT INTO Orders (orderID, issueDate, supplyDate, supplierBN, totalOrderPrice, isClosed, branchID) VALUES (?,?,?,?,?,?,?);";
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, toInsert.getId());
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            ps.setString(2, toInsert.getIssueDate().toString());
            ps.setString(3, toInsert.getSupplyDate().toString());
            ps.setInt(4, toInsert.getSupplierBN());
            ps.setDouble(5, toInsert.getTotalPriceAfterDiscount());
            ps.setInt(6,toInsert.isClosed()? 1: 0 );
            ps.setInt(7,toInsert.getBranchId() );

            ps.executeUpdate();

            insertOrderedItems(toInsert.getId(),toInsert.getSupplierBN(),toInsert.getOrderedItems(),con);

            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(con != null)
                try {
                    con.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void delete(Order toDelete) {

    }

    private void insertOrderedItems(int orderID, int supplierBN, List<OrderItem> items, Connection con) throws SQLException {
        for (OrderItem item : items){
            insertOrderedItem(orderID, supplierBN,item,con);
        }
    }

    public void delete(int key) {
        Connection con = getConnection();
        String query = "DELETE FROM Orders WHERE orderID = ?;";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, key);
            ps.executeUpdate();
            identityMap.remove(key);
            query = "DELETE FROM OrderItems WHERE orderID = ?;";
            ps = con.prepareStatement(query);
            ps.setInt(1,key);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try{
                con.close();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Order get(int key) {
        if(identityMap.containsKey(key))
            return identityMap.get(key);
        Order orderToReturn = null;
        String query = "SELECT * FROM Orders WHERE orderID = ?;";
        try (Connection con = getConnection()) {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,key);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                orderToReturn = parseResult(result);
            }
            result.close();
            return orderToReturn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Order parseResult(ResultSet result) throws SQLException {
        int orderID = result.getInt("orderID");
        return new Order(orderID,result.getInt("supplierBN"), getOrderedItems(orderID),
                LocalDate.parse(result.getString("issueDate")), LocalDate.parse(result.getString("supplyDate")),
                result.getDouble("totalOrderPrice"), result.getInt("isClosed")==0? false: true,result.getInt(("branchID")));
    }

    @Override
    public void update(Order toUpdate) {
        String query = "UPDATE Orders Set issueDate = ?, supplyDate = ?, " +
                                     "supplierBN = ?, totalOrderPrice = ?, isClosed = ?" +
                "       WHERE orderID = ?;";
        try(Connection con = getConnection()){
            // give us the control to set all update including updating the orderedItem list as one transaction
            con.setAutoCommit(false);

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, toUpdate.getIssueDate().toString());
            ps.setString(2, toUpdate.getSupplyDate().toString());
            ps.setInt(3, toUpdate.getSupplierBN());
            ps.setDouble(4, toUpdate.getTotalPriceAfterDiscount());
            ps.setInt(5, toUpdate.isClosed()? 1: 0);
            ps.setInt(6, toUpdate.getId());

            ps.executeUpdate();

            // updating the included items of this order
            updatedOrderedItems(toUpdate.getId(),toUpdate.getOrderedItems(), con);

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<OrderItem> getOrderedItems(int orderID){
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT * FROM OrderItems WHERE orderID = ?;";
        try (Connection con = getConnection()) {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,orderID);
            ResultSet result = statement.executeQuery();
            while (result.next()){
                OrderItem item = new OrderItem(result.getInt("catalogNumber"),result.getString("name"),result.getInt("amount"),
                        result.getDouble("cost"),result.getDouble("discountCost"),result.getDouble("totalCost"));
                orderItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    private void updatedOrderedItems(int orderID, List<OrderItem> orderItems, Connection con) throws SQLException {
        String query = "UPDATE OrderItems Set name = ?, amount = ?, cost = ?, discountCost = ?, totalCost = ?" +
                        "WHERE orderID = ? AND catalogNumber = ?;";
        for (OrderItem item : orderItems){
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, item.getName());
            ps.setInt(2, item.getAmount());
            ps.setDouble(3,item.getCost());
            ps.setDouble(4,item.getDiscountCost());
            ps.setDouble(5,item.getTotalPrice());
            ps.setInt(6,orderID);
            ps.setInt(7,item.getId());

            ps.executeUpdate();
        }
    }

    public List<Order> getAllSupplierOrder(int supplierBN){
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE supplierBN = ? ORDER BY issueDate;";
        try(Connection con = getConnection()){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, supplierBN);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                Order order = parseResult(result);
                orders.add(order);
                identityMap.put(order.getId(),order);
            }
            result.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void insertOrderedItem(int orderID,int supplierBN, OrderItem item ,Connection con) throws SQLException{
        boolean toCloseConnection = false;
        if(con == null) {
            con = getConnection();
            toCloseConnection = true;
        }
        String query = "INSERT INTO OrderItems (orderId ,catalogNumber, name, amount, cost, discountCost, totalCost) VALUES (?,?,?,?,?,?,?);";
        PreparedStatement ps;
        ps = con.prepareStatement(query);
        ps.setInt(1,orderID);
        ps.setInt(2, item.getId());
        ps.setString(3, item.getName());
        ps.setInt(4, item.getAmount());
        ps.setDouble(5, item.getCost());
        ps.setDouble(6, item.getDiscountCost());
        ps.setDouble(7, item.getTotalPrice());
        ps.executeUpdate();
        if(toCloseConnection)
            con.close();
    }

    public int[] GetMinCostSupplier(int serialNumber){
        String query = "SELECT supplierBN, catalogNumber "+
        "FROM SuppliersIncludedItems WHERE serialNumber = ? AND price  = (SELECT min(price)"+
        "FROM SuppliersIncludedItems WHERE serialNumber = ?);";
        int[] result = {-1, -1};
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, serialNumber);
            statement.setInt(2,serialNumber);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                result[0] = resultSet.getInt("supplierBN");
                result[1] = resultSet.getInt("catalogNumber");
                return result;
            }
            return result;
        }
        catch (SQLException e){
            e.printStackTrace();
            return result;
        }
    }

    public int existOpenOrder(int supplierBN, int branchID) throws SQLException {
        String query = "SELECT orderID " +
                " FROM Orders" +
                " WHERE supplierBN = ? AND isClosed = 0 AND supplyDate > date('now','+1 day') AND branchID = ?" +
                " ORDER BY supplyDate;";
        try(Connection con = getConnection()){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, supplierBN);
            statement.setInt(2, branchID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                return resultSet.getInt("orderID");
            return -1;
        }
    }

    public int previousOrderID(){
        String query = "SELECT max(orderID) " +
                "FROM Orders;";
        try(Connection con = getConnection()){
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            if(result.next())
                return result.getInt("max(orderID)");
            return 0;
        }
        catch (SQLException e){
            e.printStackTrace();
            return 0;
        }

    }
}
