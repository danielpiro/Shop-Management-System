package DataLayer.DalObjects;


import Bussiness_Layer.Location.Location;

import java.sql.*;

public class LocationHandler extends Handler<Location> {
    private final String FLocType = "location_type";
    private final String FAisle = "aisle_number";
    private final String FShelf = "shelf_number";
    private final String FLocAmount = "amount";
    private final String FBranchID = "branch_id";

    public LocationHandler(int branchID)
    {
        super();
        Tname = "items_location";
        Fid = "item_id";
        this.branchID = branchID;
    }

    public void createLocationTable() throws SQLException {
        Connection conn = getConnection();
        try{
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                Statement stat = conn.createStatement();
                String ITEM_TABLE = "CREATE TABLE if not exists "+Tname+" (\n" +
                    "\t"+Fid+" INTEGER NOT NULL,\n" +
                        "\t"+FLocType+" TEXT NOT NULL,\n" +
                        "\t"+FAisle+" INTEGER NOT NULL,\n" +
                        "\t"+FShelf+" INTEGER NOT NULL,\n" +
                        "\t"+FLocAmount+" INTEGER NOT NULL,\n" +
                        "\t"+FBranchID+" INTEGER NOT NULL,\n" +
                        "\tPRIMARY KEY("+FLocType+","+FAisle+","+FShelf+","+FBranchID+")\n" +
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
    public void insert(Location toInsert) {

    }

    public void insert(Location loc, int itemID)
    {
        String query = "INSERT INTO "+Tname+" ("+Fid+","+FLocType+","+FAisle+","+FShelf+","+FLocAmount+ ","+FBranchID+") "+
                "VALUES (?,?,?,?,?,?);";
        Connection conn = getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1, itemID);
                pstmt.setString(2, loc.getLocType());
                pstmt.setInt(3, loc.getAisleNum());
                pstmt.setInt(4, loc.getShelfNum());
                pstmt.setInt(5, loc.getAmount());
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
    public void delete(Location toDelete) {
        String query = "DELETE FROM "+Tname+" WHERE "+FLocType+" = ? AND "+FAisle+ " = ? AND "+FShelf+"= ? AND "+FBranchID+" = ?;";
        Connection conn = getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setString(1, toDelete.getLocType());
                pstmt.setInt(2, toDelete.getAisleNum());
                pstmt.setInt(3, toDelete.getShelfNum());
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
    public Location get(int key) {
        return null;
    }//not using???

    //updates amount in a location
    //assume toUpdate contains updated amount
    @Override
    public void update(Location toUpdate) {
        String query = "UPDATE "+Tname+" SET "+FLocAmount+" = ?\n"+
                " WHERE "+FLocType+" = ? AND "+FAisle+ " = ? AND "+FShelf+"= ? AND "+FBranchID+" = ?;";
        Connection conn = getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1, toUpdate.getAmount());
                pstmt.setString(2, toUpdate.getLocType());
                pstmt.setInt(3, toUpdate.getAisleNum());
                pstmt.setInt(4, toUpdate.getShelfNum());
                pstmt.setInt(5,branchID);

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

    public void update(Location oldLoc, Location toUpdate) {
        String query = "UPDATE "+Tname+" SET "+FLocType+"= ?, "+FAisle+"= ?, "+FShelf+" = ?\n"+
                "WHERE "+FLocType+" = ? AND "+FAisle+ " = ? AND "+FShelf+"= ? AND "+FBranchID+" =?;";
        Connection conn = getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setString(1, toUpdate.getLocType());
                pstmt.setInt(2, toUpdate.getAisleNum());
                pstmt.setInt(3, toUpdate.getShelfNum());
                pstmt.setString(4, oldLoc.getLocType());
                pstmt.setInt(5, oldLoc.getAisleNum());
                pstmt.setInt(6, oldLoc.getShelfNum());
                pstmt.setInt(7,branchID);

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

}
