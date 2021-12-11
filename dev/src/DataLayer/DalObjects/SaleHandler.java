package DataLayer.DalObjects;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SaleHandler extends Handler<SaleInfo>{

    private final String FBought = "price_bought";
    private final String FSold = "price_sold";
    private final String Famount = "amount";
    private final String Fdate = "date";
    private final String Fbranch = "branch_id";


    public SaleHandler(int branchID)
    {
        super();
        Tname = "Sales";
        Fid = "item_id";
        this.branchID = branchID;
    }
    public void createSaleTable() throws SQLException {
        Connection conn = getConnection();
        try{
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                Statement stat = conn.createStatement();
                String ITEM_TABLE = "CREATE TABLE if not exists "+Tname+" (\n" +
                        "\t"+Fid+" INTEGER,\n" +
                        "\t"+FBought+" REAL NOT NULL,\n" +
                        "\t"+FSold+" REAL NOT NULL,\n" +
                        "\t"+Famount+" INTEGER NOT NULL,\n" +
                        "\t"+Fdate+" TEXT NOT NULL,\n" +
                        "\t"+Fbranch+" INTEGER NOT NULL,\n" +
                        "\tPRIMARY KEY("+Fid+","+Fdate+","+Fbranch+")\n" +
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
    public void delete(SaleInfo toDelete) {
        //never use
    }

    @Override
    public void insert(SaleInfo pair) {
        String query = "INSERT INTO "+Tname+" ("+Fid+","+FBought+","+FSold+","+Famount+","+Fdate+","+Fbranch+") " +
                "VALUES (?,?,?,?,?,?);";
        Connection conn = getConnection();
        try {

            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1, pair.getSaleID());
                pstmt.setDouble(2,pair.get_priceBought());
                pstmt.setDouble(3, pair.get_priceSold());
                pstmt.setInt(4, pair.get_amount());
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String time = df.format(pair.getDate());
                pstmt.setString(5, time);
                pstmt.setInt(6,branchID);

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
    public SaleInfo get(int key) {
        return null;
    }

    public List<SaleInfo> getItemsSales(int itemID) {
        Connection conn = getConnection();
        List<SaleInfo> salesList = new ArrayList<SaleInfo>();
        String query = "SELECT *\n"+
                "FROM "+Tname+"\n"+
                "WHERE "+Fid+" = ? AND "+Fbranch+" =?;";
        try {

            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1, itemID);
                pstmt.setInt(2,branchID);

                ResultSet result = pstmt.executeQuery();
                while(result.next())
                {
                    int id = result.getInt(Fid);
                    double pBought = result.getDouble(FBought);
                    double pSold = result.getDouble(FSold);
                    int amount = result.getInt(Famount);
                    String date = result.getString(Fdate);
                    salesList.add(new SaleInfo(id, amount, pBought, pSold,MakeDateFromString(date)));
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


        return salesList;
    }

    @Override
    public void update(SaleInfo toUpdate)
    {

    }

}
