package DataLayer.DalObjects;

import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;


public abstract class Handler<T> {
    protected String Tname;
    protected String Fid;
    protected final String fileName = "SuperLee.db";//"inventoryAndSuppliers.db"; //TODO: change to SuperLee.db
    protected Map<Integer, T> identityMap;
    protected final String DB_url = "jdbc:sqlite:" + System.getProperty("user.dir")  + "\\"+fileName;
    protected static final String driver = "org.sqlite.JDBC";
    protected int branchID;

    public Handler() {
        identityMap = new HashMap<>();
        branchID = -1;
    }

    public boolean createTables() {
        Connection con = getConnection();
        if (con == null) {
            return false;
        }
        try {
            String schema = "CREATE TABLE IF NOT EXISTS \"Suppliers\" (\n" +
                    "\t\"supplierBN\"\tINTEGER,\n" +
                    "\t\"name\"\tTEXT NOT NULL,\n" +
                    "\t\"address\"\tTEXT NOT NULL,\n" +
                    "\t\"bankAccount\"\tINTEGER NOT NULL,\n" +
                    "\t\"termOfPayment\"\tTEXT NOT NULL,\n" +
                    "\t\"needDelivery\"\tINTEGER NOT NULL,\n" +
                    "\tPRIMARY KEY(\"supplierBN\")\n" +
                    ");";
            Statement s = con.createStatement();
            s.execute(schema);

            schema = "CREATE TABLE IF NOT EXISTS \"SuppliersTermsOfSupply\" (\n" +
                    "\t\"supplierBN\"\tINTEGER,\n" +
                    "\t\"day\"\tTEXT,\n" +
                    "\tFOREIGN KEY(\"supplierBN\") REFERENCES \"Suppliers\"(\"supplierBN\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                    "\tPRIMARY KEY(\"supplierBN\",\"day\")\n" +
                    ");";
            s = con.createStatement();
            s.execute(schema);

            schema = "CREATE TABLE IF NOT EXISTS \"Orders\" (\n" +
                    "\t\"orderID\"\tINTEGER,\n" +
                    "\t\"supplierBN\"\tINTEGER,\n" +
                    "\t\"issueDate\"\tTEXT NOT NULL,\n" +
                    "\t\"supplyDate\"\tTEXT CHECK(\"supplyDate\" >= \"issueDate\"),\n" +
                    "\t\"totalOrderPrice\"\tREAL NOT NULL CHECK(\"totalOrderPrice\" >= 0),\n" +
                    "\t\"isClosed\"\tINTEGER NOT NULL,\n" +
                    "\t\"branchID\"\tINTEGER NOT NULL,\n" +
                    "\tFOREIGN KEY(\"supplierBN\") REFERENCES \"Suppliers\"(\"supplierBN\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                    "\tPRIMARY KEY(\"orderID\")\n" +
                    ");";
            s = con.createStatement();
            s.execute(schema);

            schema = "CREATE TABLE IF NOT EXISTS \"Contacts\" (\n" +
                    "\t\"name\"\tTEXT,\n" +
                    "\t\"supplierBN\"\tINTEGER,\n" +
                    "\t\"phoneNumber\"\tTEXT NOT NULL,\n" +
                    "\t\"email\"\tTEXT NOT NULL,\n" +
                    "\tFOREIGN KEY(\"supplierBN\") REFERENCES \"Suppliers\"(\"supplierBN\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                    "\tPRIMARY KEY(\"name\",\"supplierBN\")\n" +
                    ");";
            s = con.createStatement();
            s.execute(schema);

            schema = "CREATE TABLE IF NOT EXISTS \"SuppliersIncludedItems\" (\n" +
                    "\t\"supplierBN\"\tINTEGER,\n" +
                    "\t\"catalogNumber\"\tINTEGER,\n" +
                    "\t\"serialNumber\"\tINTEGER NOT NULL,\n" +
                    "\t\"price\"\tREAL,\n" +
                    "\tFOREIGN KEY(\"supplierBN\") REFERENCES \"Suppliers\"(\"supplierBN\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                    "\tPRIMARY KEY(\"supplierBN\",\"catalogNumber\")\n" +
                    ");";
            s = con.createStatement();
            s.execute(schema);

            schema = "CREATE TABLE IF NOT EXISTS \"SuppliersOrdersDiscounts\" (\n" +
                    "\t\"supplierBN\"\tINTEGER,\n" +
                    "\t\"amount\"\tINTEGER CHECK(\"amount\" >= 1),\n" +
                    "\t\"discount\"\tREAL CHECK(\"discount\" >= 0),\n" +
                    "\tFOREIGN KEY(\"supplierBN\") REFERENCES \"Suppliers\"(\"supplierBN\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                    "\tPRIMARY KEY(\"supplierBN\",\"amount\")\n" +
                    ");";
            s = con.createStatement();
            s.execute(schema);

            schema = "CREATE TABLE IF NOT EXISTS \"SuppliersItemsDiscounts\" (\n" +
                    "\t\"supplierBN\"\tINTEGER,\n" +
                    "\t\"catalogNumber\"\tINTEGER,\n" +
                    "\t\"amount\"\tINTEGER CHECK(\"amount\" >= 1),\n" +
                    "\t\"discount\"\tREAL NOT NULL CHECK(\"discount\" >= 0),\n" +
                    "\tFOREIGN KEY(\"supplierBN\") REFERENCES \"Suppliers\"(\"supplierBN\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                    "\tPRIMARY KEY(\"supplierBN\",\"catalogNumber\",\"amount\")\n" +
                    ");";
            s = con.createStatement();
            s.execute(schema);

            schema = "CREATE TABLE IF NOT EXISTS \"OrderItems\" (\n" +
                    "\t\"orderID\"\tINTEGER,\n" +
                    "\t\"catalogNumber\"\tINTEGER,\n" +
                    "\t\"name\"\tTEXT NOT NULL,\n" +
                    "\t\"amount\"\tINTEGER NOT NULL CHECK(\"amount\" >= 1),\n" +
                    "\t\"cost\"\tREAL NOT NULL CHECK(\"cost\" > 0),\n" +
                    "\t\"discountCost\"\tREAL NOT NULL CHECK(\"discountCost\" > 0),\n" +
                    "\t\"totalCost\"\tREAL NOT NULL CHECK(\"totalCost\" > 0),\n" +
                    "\tFOREIGN KEY(\"orderID\") REFERENCES \"Orders\"(\"orderID\") ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                    "\tPRIMARY KEY(\"orderID\",\"catalogNumber\")\n" +
                    ");";
            s = con.createStatement();
            s.execute(schema);

            return true;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            try{
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "\\" + fileName;
        Connection conn;
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(url);

            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }
            return conn;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


/*
    public Connection getConnection() {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(DB_url);
            return con;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createTables() {
        Connection con = getConnection();
        if (con == null) {
            return false;
        }
        try {
            String schema = new String(Files.readAllBytes(Paths.get("supplierDB.db.sql")));
            Statement s = con.createStatement();
            s.execute(schema);
            return true;

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
*/
    public abstract void insert(T toInsert);
    public abstract void delete(T toDelete);
    public abstract T get(int key);
    public abstract void update(T toUpdate);

    public void deleteAllByID(int toDelete) {
        String query = "DELETE FROM "+Tname+" WHERE "+Fid+" = ?;";
        Connection conn = getConnection();
        try {
            DatabaseMetaData meta = conn.getMetaData();
            PreparedStatement pstmt = conn.prepareStatement(query);{
                pstmt.setInt(1, toDelete);

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

    protected Date MakeDateFromString(String s) {
        List<String> list = new ArrayList<String>(Arrays.asList(s.split("-")));
        if (list.size() != 3) {
            throw new ArrayIndexOutOfBoundsException("Bad Date");
        }
        int day = getIntFromString(list.get(0));
        if (day <= 0 || day > 31)
            throw new ArrayIndexOutOfBoundsException("Bad Day in date");
        int month = getIntFromString(list.get(1));
        if (month <= 0 || month > 12)
            throw new ArrayIndexOutOfBoundsException("Bad Month in date");
        int year = getIntFromString(list.get(2));
        return parseDate(s);
    }

    private int getIntFromString(String s) {
        return Integer.parseInt(s);

    }

    private Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

}
