package DataLayer.DalObjects;

import Bussiness_Layer.Item.Item;
import Bussiness_Layer.Item.ItemInfo;
import Bussiness_Layer.Location.Location;
import Bussiness_Layer.Location.StorageLocation;
import Bussiness_Layer.Location.StoreLocation;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemHandler extends Handler<Item>  {

    private final String Fname = "item_name";
    private final String FManName ="manufacture_name";
    private final String FMinAmount ="minimum_amount";
    private final String FCategory= "category";
    private final String FWeight= "weight";
    private final String FCurrentAmount = "current_amount";
    private final String FCurrentPrice= "current_price";
    private final String Fdmg= "number_of_damaged_items";
    private final String FSupDate = "next_supply_date";
    private final String FBranchID = "branch_id";

    private final String LocTable = "items_location";
    private final String FLocType = "location_type";
    private final String FAisle = "aisle_number";
    private final String FShelf = "shelf_number";
    private final String FLocAmount = "amount";

    public ItemHandler(int branchID)
    {
        super();
        Tname = "Items_Main";
        Fid = "item_id";
        this.branchID = branchID;
    }
    public void createItemsTable() throws SQLException {
        Connection conn = getConnection();
        try{
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                Statement stat = conn.createStatement();
                String ITEM_TABLE = "CREATE TABLE if not exists "+Tname+" (\n" +
                    "\t"+Fid+" INTEGER NOT NULL,\n" +
                    "\t"+Fname+" TEXT NOT NULL,\n" +
                    "\t"+FMinAmount+" INTEGER NOT NULL,\n" +
                    "\t"+FManName+" TEXT NOT NULL,\n" +
                    "\t"+FCurrentAmount+" INTEGER NOT NULL,\n" +
                    "\t"+Fdmg+" INTEGER NOT NULL,\n" +
                    "\t"+FSupDate+" TEXT,\n" +
                    "\t"+FCurrentPrice+" REAL NOT NULL,\n" +
                    "\t"+FWeight+" INTEGER NOT NULL,\n" +
                    "\t"+FCategory+" TEXT NOT NULL,\n" +
                    "\t"+FBranchID+" INTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY("+Fid+","+FBranchID+")\n" +
                    ");";
                stat.execute(ITEM_TABLE);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if(conn != null)
                try {
                    conn.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
        }
    }
    public Map<Integer,Item> loadItems()
    {
        Map<Integer,Item> finalMap = new HashMap<Integer,Item>();
        List<Item> itemList = new ArrayList<Item>();
        Connection conn = getConnection();
        String query = "SELECT *\n" +
                "FROM " +Tname + "\n"+
                "WHERE "+ FBranchID + " = ?;";

        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1,branchID);
                ResultSet result = pstmt.executeQuery();
                while (result.next()){
                    List<String> infoBuild = new ArrayList<String>();
                    infoBuild.add(result.getString(Fname));
                    infoBuild.add(String.valueOf(result.getInt(Fid)));
                    infoBuild.add(result.getString(FManName));
                    infoBuild.add(String.valueOf(result.getInt(FMinAmount)));
                    infoBuild.add(String.valueOf(result.getInt(FWeight)));
                    infoBuild.add(result.getString(FCategory));

                    ItemInfo itemInfo = new ItemInfo(infoBuild);
                    Item item = new Item(itemInfo);
                    String time = result.getString(FSupDate);
                    if (time !=null)
                        item.updateNextSupplement(MakeDateFromString(time));//TODO check if translates from TEXT to DATE TYPE
                    item.updateCurrentQuantity(result.getInt(FCurrentAmount));
                    item.update_numOfDmgItems(result.getInt(Fdmg));
                    item.setCurrentPrice(result.getDouble(FCurrentPrice));
                    itemList.add(item);
                }
            }
            for (int i = 0; i<itemList.size();i++)
            {
                String subQuery = "SELECT "+FLocType+","+FAisle+","+FShelf+","+FLocAmount+
                        "\nFROM "+LocTable+"\n"+
                        "WHERE "+Fid + " = ? AND "+FBranchID +"=?;";
                PreparedStatement pstmt2 = conn.prepareStatement(subQuery);{
                    pstmt2.setInt(1,itemList.get(i).getId());
                    pstmt2.setInt(2,branchID);
                    ResultSet res = pstmt2.executeQuery();
                    while(res.next())
                    {
                        Location loc;
                        String type = res.getString(FLocType);
                        int aisle = res.getInt(FAisle);
                        int shelf = res.getInt(FShelf);
                        int amount = res.getInt(FLocAmount);
                        if(type.equals("store")) {
                            loc = new StoreLocation(aisle, shelf);
                            loc.setAmount(amount);
                        }
                        else if(type.equals("storage"))
                        {
                            loc = new StorageLocation(aisle,shelf);
                            loc.setAmount(amount);
                        }
                        else
                            loc = null;

                        itemList.get(i).addNewLoc(loc);
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        for(int i=0; i<itemList.size();i++)
            finalMap.putIfAbsent(itemList.get(i).getId(),itemList.get(i));
        return finalMap;
    }

    @Override
    public void delete(Item keys) {
        String query = "DELETE FROM "+Tname+" WHERE "+Fid+" = ? AND "+FBranchID + " = ?;";
        Connection conn = getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
            pstmt.setInt(1, keys.getId());
            pstmt.setInt(2,branchID);
            pstmt.executeUpdate();
            conn.commit();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            if(conn != null)
                try {
                    conn.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
        }

    }

    @Override
    public Item get(int key) {
        return null;
    }

    @Override
    public void update(Item toUpdate) {
        String query = "UPDATE "+Tname+" SET "+Fname+" =?, "+FMinAmount+" =?, "+FManName+" =?, "+
                FCurrentAmount+" =?, "+Fdmg+" =?, "+FSupDate+" =?, "+FCurrentPrice+" =?, "+FWeight+" =?, "+
                FCategory+" =?\n"+
                "WHERE "+Fid+" = ? AND "+FBranchID+" = ?;";
        Connection conn = getConnection();
        try {

            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setString(1, toUpdate.getItemName());
                pstmt.setInt(2, toUpdate.getMinimumQuantity());
                pstmt.setString(3, toUpdate.get_manufacturerName());
                pstmt.setInt(4, toUpdate.get_currentQuantity());
                pstmt.setInt(5, toUpdate.get_numOfDmgItems());

                if(toUpdate.get_nextSupplement()!=null) {
                    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                    String time = df.format(toUpdate.get_nextSupplement());
                    pstmt.setString(6, time);
                }
                else
                    pstmt.setDate(6, null);
                pstmt.setDouble(7, toUpdate.get_current_price());
                pstmt.setInt(8, toUpdate.getWeight());
                pstmt.setString(9, toUpdate.getCategoryName());
                pstmt.setInt(10, toUpdate.getId());
                pstmt.setInt(11,branchID);
                pstmt.executeUpdate();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            if(conn != null)
                try {
                    conn.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
        }


    }


    @Override
    public void insert(Item item) {
        String query = "INSERT INTO "+Tname+" ("+Fid+","+Fname+","+FMinAmount+","+FManName+","+FCurrentAmount+
                ","+Fdmg+","+FSupDate+","+FCurrentPrice+","+FWeight+","+FCategory+","+FBranchID+") "+
                "VALUES (?,?,?,?,?,?,?,?,?,?,?);";
        Connection conn = getConnection();
        try {

            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
            pstmt.setInt(1, item.getId());
            pstmt.setString(2, item.getItemName());
            pstmt.setInt(3, item.getMinimumQuantity());
            pstmt.setString(4, item.get_manufacturerName());
            pstmt.setInt(5, item.get_currentQuantity());
            pstmt.setInt(6, item.get_numOfDmgItems());
            pstmt.setDate(7, null);
            pstmt.setDouble(8, item.get_current_price());
            pstmt.setInt(9, item.getWeight());
            pstmt.setString(10, item.getCategoryName());
            pstmt.setInt(11,branchID);
            pstmt.executeUpdate();
        }

    } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            if(conn != null)
                try {
                    conn.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
        }

    }

    //<id,amount>
    public Map<Integer, Integer> getShortageItems() {
        String query = "SELECT "+Fid+","+FCurrentAmount+"\n" +
                "FROM " +Tname + "\n"+
                "WHERE "+FBranchID+" = ? AND "+FCurrentAmount+" < "+FMinAmount+";";

        return get_items_by_query(query);
    }

    public Map<Integer, Integer> get_dmgItems() {
        String query = "SELECT "+Fid+","+FCurrentAmount+"\n" +
                "FROM " +Tname + "\n"+
                "WHERE "+FBranchID+" = ? AND "+Fdmg+" >  0 ;";
        return get_items_by_query(query);

    }

    private Map<Integer, Integer> get_items_by_query(String query) {
        Map<Integer,Integer> db_map = new HashMap<Integer,Integer>();
        Connection conn = getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1,branchID);
                ResultSet result =  pstmt.executeQuery();
                while(result.next()){
                    int id = result.getInt(Fid);
                    int amount = result.getInt((FCurrentAmount));
                    db_map.put(id,amount);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            if(conn != null)
                try {
                    conn.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
        }
        return db_map;
    }

}
