package DataLayer.DalObjects;


import Bussiness_Layer.Category.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoryHandler extends Handler<Category> {

    private final String FCatName = "category_name";
    private final String FFatherCat = "father_category_name";

    public void createCategoryTable() throws SQLException {
        Connection conn = getConnection();
        try{
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                Statement stat = conn.createStatement();
                String ITEM_TABLE = "CREATE TABLE if not exists " + Tname + " (\n" +
                        "\t"+FCatName+" TEXT PRIMARY KEY,\n" +
                        "\t"+FFatherCat+" TEXT\n" + ");";
                stat.execute(ITEM_TABLE);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
    public CategoryHandler()
    {
        super();
        Tname = "categories";
        Fid = "-1";
    }

    @Override
    public void insert(Category toInsert) {
        String query = "INSERT INTO " + Tname + " ("+FCatName+","+FFatherCat+") " +
                "VALUES (?,?);";
        Connection conn = getConnection();
        try {

            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setString(1, toInsert.getCatName());
                pstmt.setString(2, toInsert.getFatherName());
                pstmt.executeUpdate();
               // conn.commit();
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
    public void delete(Category toDelete) {
        String query = "DELETE FROM "+Tname+" WHERE "+FCatName+" = ?";
        Connection conn = getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setString(1, toDelete.getCatName());
                pstmt.executeUpdate();
            }
            UpdateDeletedCat(toDelete,conn);

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
    public Category get(int key) {
        return null; //not uesed.
    }

    public List<Category> loadCategory(){ //return list of all father categories linked to sons
        List<Category> catList = new ArrayList<Category>();
        Connection conn = getConnection();
        String query = "SELECT "+FCatName+"\n" +
                "FROM " +Tname + "\n" +
                "WHERE "+FFatherCat+" IS NULL;";

        try {
            DatabaseMetaData meta = conn.getMetaData();
            Statement pstmt = conn.createStatement();{
            ResultSet result = pstmt.executeQuery(query);
            while (result.next()){
                catList.add(new Category(result.getString("category_name")));
            }
            }
            for(int i = 0; i<catList.size(); i++)
                makeTreeCategory(catList.get(i),conn);

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
        return catList;
    }

    private void makeTreeCategory(Category category, Connection conn) throws SQLException {
        String Cname = category.getCatName();
        String query = "SELECT category_name\n" +
                "FROM " +Tname + "\n" +
                "WHERE "+FFatherCat+" = ?;";
        DatabaseMetaData meta = conn.getMetaData();
        PreparedStatement pstmt2 = conn.prepareStatement(query);{
            pstmt2.setString(1,Cname);
            ResultSet result = pstmt2.executeQuery();
            while (result.next()){
                String temp = result.getString(FCatName);
                category.AddNewSonCategory(temp);
                makeTreeCategory(category.findSon(temp),conn);
            }
        }
    }

    @Override
    public void update(Category toUpdate) {
//not using??
    }//not using???

    public void updateFull(String catToChange, Category toUpdate) {
        Connection conn = getConnection();
        String query  = "UPDATE " + Tname + " SET "+FCatName+" = ?\n"+
                "WHERE "+FCatName+" =?;";
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setString(1, toUpdate.getCatName());
                pstmt.setString(2, catToChange);
                pstmt.executeUpdate();

            }
            String query2 = "UPDATE " + Tname + " SET "+FFatherCat+" = ?\n"+
                    "WHERE "+FFatherCat+" =?;";
            PreparedStatement pstmt2 = conn.prepareStatement(query2);{
                pstmt2.setString(1, toUpdate.getCatName());
                pstmt2.setString(2, catToChange);
                pstmt2.executeUpdate();
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

    private void UpdateDeletedCat(Category toDelete,Connection conn) throws SQLException {
        String query  = "UPDATE " + Tname + " SET "+FFatherCat+" = ?\n" +
                "WHERE "+FFatherCat+" = ?;";
        DatabaseMetaData meta = conn.getMetaData();
        PreparedStatement pstmt = conn.prepareStatement(query);
        {
            pstmt.setString(1, toDelete.getFatherName());
            pstmt.setString(2, toDelete.getCatName());

            pstmt.executeUpdate();
        }


    }
}
