package DataLayer.DalObjects;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateHandler extends Handler<ItemDate> {

    private final String FAmount = "amount";
    private final String FDate = "expiration_date";
    private  final String FBranchID = "branch_id";

    public DateHandler(int branchID)
    {
        super();
        Tname = "Items_Expiration_Date";
        Fid = "item_id";
        this.branchID = branchID;
    }


    public void createDateTable() throws SQLException {
        Connection conn = getConnection();
        try{
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                Statement stat = conn.createStatement();
                String ITEM_TABLE = "CREATE TABLE if not exists "+Tname+" (\n" +
                        "\t"+Fid+" INTEGER NOT NULL,\n" +
                        "\t"+FAmount+" INTEGER NOT NULL,\n" +
                        "\t"+FDate+" TEXT NOT NULL,\n" +
                        "\t"+FBranchID+" INTEGER NOT NULL,\n" +
                        "\tPRIMARY KEY("+Fid+","+FDate+","+FBranchID+")\n" +
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
    @Override
    public void insert(ItemDate toInsert) {
        String query = "INSERT INTO " + Tname + " ("+Fid+","+FAmount+","+FDate+","+FBranchID+") " +
                "VALUES (?,?,?,?);";
        Connection conn = getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1, toInsert.getItemID());
                pstmt.setInt(2, toInsert.getAmount());
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String time = df.format(toInsert.getDate());
                pstmt.setString(3, time);
                pstmt.setInt(4,branchID);

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
    public void delete(ItemDate toDelete) {
        String query = "DELETE FROM "+Tname+" WHERE "+Fid+" = ? AND "+FDate+ " = ? AND "+FBranchID+" =?;";
        Connection conn = getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1, toDelete.getItemID());
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String time = df.format(toDelete.getDate());
                pstmt.setString(2, time);
                pstmt.setInt(3, branchID);

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
    public ItemDate get(int key) {
        return null;
    }

    public List<ItemDate> getExpDateByID(int itemID) {
        List<ItemDate> lst = new ArrayList<ItemDate>();
        Connection conn = getConnection();
        String query = "SELECT *\n" +
                "FROM " +Tname + "\n" +
                "WHERE "+Fid+" =? AND "+FBranchID+" =?;";

        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1,itemID);
                pstmt.setInt(2,branchID);
                ResultSet result = pstmt.executeQuery();
                while (result.next()){
                    int id = result.getInt(Fid);
                    int amount = result.getInt(FAmount);
                    String dateString = result.getString(FDate);
                    lst.add(new ItemDate(MakeDateFromString(dateString), id, amount));
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
        return lst;
    }

    public Map<Integer,Integer> getIDByExpDate() {
        List<ItemDate> lst = new ArrayList<ItemDate>();
        Connection conn = getConnection();
        String query = "SELECT *\n" +
                "FROM " +Tname + "\n" +
                "WHERE date("+FDate+") < date('now') AND "+FBranchID+"= ?;";

        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1,branchID);
                ResultSet result = pstmt.executeQuery();
                while (result.next()){
                    int amount = result.getInt(FAmount);
                    int id = result.getInt(Fid);
                    String date = result.getString(FDate);
                    lst.add(new ItemDate(MakeDateFromString(date), id, amount));
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

        Map<Integer,Integer> myMap = new HashMap<Integer,Integer>();
        for(int i=0; i<lst.size();i++)
            myMap.putIfAbsent(lst.get(i).getItemID(),lst.get(i).getAmount());
        return myMap;
    }

    @Override
    public void update(ItemDate toUpdate) {
        Connection conn = getConnection();
        String query  = "UPDATE " + Tname + " SET "+FAmount+" = ?\n" +
                "WHERE "+Fid+" = ? AND "+FDate + " =? AND "+FBranchID+" =?;";
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1,toUpdate.getAmount());
                pstmt.setInt(2,toUpdate.getItemID());
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String time = df.format(toUpdate.getDate());
                pstmt.setString(3,time);
                pstmt.setInt(4,branchID);

                pstmt.executeUpdate();
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
    }



}

