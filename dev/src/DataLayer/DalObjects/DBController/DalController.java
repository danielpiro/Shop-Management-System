package DataLayer.DalObjects.DBController;

import Bussiness_Layer.BussinessDataAccess;
import Bussiness_Layer.Job;
import Bussiness_Layer.ShiftPackage.ShiftType;
import Bussiness_Layer.Tuple;
import Bussiness_Layer.Utility;
import DataLayer.DalObjects.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class DalController {
    List<Job> jobListnitay = Arrays.asList(new Job[]{Job.Cashier, Job.Usher, Job.Guard});
    List<Job> jobListomry = Arrays.asList(new Job[]{Job.Cashier, Job.Usher, Job.Guard});
    List<Job> jobLisyben = Arrays.asList(new Job[]{Job.Cashier, Job.Usher, Job.Guard});
    List<Job> jobListElad = Arrays.asList(new Job[]{Job.Cashier, Job.Usher, Job.Guard});
    List<Job> jobListOlivia = Arrays.asList(new Job[]{Job.Cashier, Job.Usher, Job.Guard});
    List<Job> StorageKeeperjobList = Arrays.asList(new Job[]{Job.StoreKeeper});
    List<Job> jobListDriverA = Arrays.asList(new Job[]{Job.DriverLicenseA});
    List<Job> jobListDriverB = Arrays.asList(new Job[]{Job.DriverLicenseB});
    List<Job> jobListDriverC = Arrays.asList(new Job[]{Job.DriverLicenseC});
    List<Job> jobListDriverD = Arrays.asList(new Job[]{Job.DriverLicenseD});
    List<Job> jobListStorage = Arrays.asList(new Job[]{Job.StoreKeeper});
    private String r = "r";
    private String t = "t";
    private String h = "h";
    private String d = "d";
    private String m = "m";
    private String k = "k";
    private static BussinessDataAccess BDA;
    private static DalController singleOne;
    private Connection conn;
    private boolean flag = false;

    private DalController() {
    }

    public static DalController getDalController() throws SQLException {
        if (singleOne == null) {
            singleOne = new DalController();
            BDA = BussinessDataAccess.getBTD();
        }
        return singleOne;
    }

    public void openConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            String current = new java.io.File(".").getCanonicalPath();
            String url = "jdbc:sqlite:" + current + "\\/SuperLee.db";
            conn = DriverManager.getConnection(url);
            if (!flag) {
                flag = true;
                initialize();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private boolean check_if_database_empty() {
        boolean return_value = true;
        openConnection();
        String sql;
        sql = "SELECT * FROM Locations";
        ResultSet resultSet = null;
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();
            if (resultSet.next())
                return_value = false;
        } catch (Exception ignored) {
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                conn.close();
            } catch (SQLException ignored) {
            }
        }
        return return_value;
    }


    public void initialize() {
        createTables();
        if (check_if_database_empty()) {
            initLocations();
            initSections();
            initTrucks();
            initWorkers();
            initSupply();
            initWork();
        }
    }


    private void initSupply() {
        List<String> sqls = new LinkedList<>();
        sqls.add("INSERT INTO Supply (id, name) VALUES (1, \"corn\");");
        sqls.add("INSERT INTO Supply (id, name) VALUES (2, \"pizza\");");
        sqls.add("INSERT INTO Supply (id, name) VALUES (3, \"falafel\");");
        sqls.add("INSERT INTO Supply (id, name) VALUES (4, \"hamburger\");");
        openConnection();
        for (String sqlCommand : sqls) {
            try (PreparedStatement statement = conn.prepareStatement(sqlCommand)) {
                statement.execute();
            } catch (Exception exception) {
                return;
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initWork() {
        boolean[][] trueCons = {{true, true}, {true, true}, {true, true}, {true, true}, {true, true}, {true, true}, {true, true}};
        DalRegularWorker reg = new DalRegularWorker("Nitay", "Vitkin", 123456789, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, trueCons, true, jobListnitay, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Nitay", "Ornon", 1256789, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, trueCons, true, jobListnitay, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Rami", "Shevy", 125846, 5000, 2.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555555, 111, trueCons, true, jobListomry, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Tonik", "Karminsky", 1212, 3000, 2.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555555, 111, trueCons, true, jobListnitay, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Lamalo", "Funjoya", 1313, 3500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555555, 111, trueCons, true, jobListnitay, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Ira", "Turing", 1414, 3600, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, true, jobListnitay, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Chico", "La-moral", 1111, 10000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 222, trueCons, true, jobListStorage, null);
        addDalRegWorker(reg, k);
        reg = new DalRegularWorker("Chico", "La-moral", 1515, 10000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, true, jobListStorage, null);
        addDalRegWorker(reg, k);
        reg = new DalRegularWorker("Rama", "Dan", 2021, 2500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, true, jobListElad, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Fifo", "Tuama", 1717, 4000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, true, jobListomry, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Rak", "Hani", 1818, 4000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, true, jobListOlivia, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Haia", "Meta", 1919, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, true, jobListOlivia, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Moni", "Moshonov", 2121, 35000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, true, jobListomry, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Carmela", "Gross-Wagner", 2323, 20000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, true, jobListomry, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Hadal", "Ahbek", 3131, 1500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverA, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Maggie", "Roko", 2424, 20000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, true, jobListomry, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Likas", "Tenki", 2525, 20000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, true, jobListomry, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Adma", "Tai", 2626, 20000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 222, trueCons, true, jobListomry, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Shaloshve", "Dai", 2727, 20000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 222, trueCons, true, jobListomry, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Mimi", "Pikeru", 2828, 20000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 222, trueCons, true, jobListomry, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Termina", "Fand", 2929, 20000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 222, trueCons, true, jobListomry, null);
        addDalRegWorker(reg, r);
        reg = new DalRegularWorker("Polo", "Kantor", 6767, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverD, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Tenishi", "Megra", 6868, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverD, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Tulum", "Kisam", 6969, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverD, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Bucha", "Piros", 60000, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverD, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Nepentes", "Kimos", 60001, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverD, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Kobi", "Zeev", 2221, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 222, trueCons, false, jobListStorage, null);
        addDalRegWorker(reg, k);
        reg = new DalRegularWorker("Ron", "Mani", 2223, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 222, trueCons, false, jobListStorage, null);
        addDalRegWorker(reg, k);
        reg = new DalRegularWorker("ken", "bolo", 2224, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 222, trueCons, false, jobListStorage, null);
        addDalRegWorker(reg, k);
        reg = new DalRegularWorker("nikita", "darbos", 2225, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 222, trueCons, false, jobListStorage, null);
        addDalRegWorker(reg, k);
        reg = new DalRegularWorker("Tanya", "Pishotto", 3232, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverA, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Miko", "Melamed", 3434, 1500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverA, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Jerry", "Levy", 3535, 1500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverA, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Giome", "Kerpanaro", 3636, 1500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverA, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Taltalim", "Sharif", 4141, 2500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverB, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Tzinok", "Matok", 4242, 2500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverB, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Trabelsi", "Hehamor", 4343, 2500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverB, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Moriya", "Hahiti", 4545, 2500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverB, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Bongos", "Tulum", 4646, 2500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverB, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Oingo", "Boingo", 5151, 28000, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverC, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Meni", "Jonjo", 5252, 3500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverC, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Terra", "Bitya", 5353, 3500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverC, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Tenten", "Sherrif", 5454, 3500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverC, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Tash", "Olam", 5656, 3500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverC, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Rikki", "Kioto", 6161, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverD, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Senya", "Ohelba", 6262, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverD, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Mido", "Fid", 6363, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverD, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Adlo", "Yada", 6464, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverD, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Nigiri", "Besoya", 6565, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, -1, trueCons, false, jobListDriverD, null);
        addDalRegWorker(reg, d);
        reg = new DalRegularWorker("Meni", "Tenzer", 11111, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, false, jobListStorage, null);
        addDalRegWorker(reg, k);
        reg = new DalRegularWorker("Ran", "Shomer", 11112, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, false, jobListStorage, null);
        addDalRegWorker(reg, k);
        reg = new DalRegularWorker("Kobi", "Zeev", 11113, 4500, 1.5, 1.5, 123456, "Mizrahi Tfahot", 987654, 555, 111, trueCons, false, jobListStorage, null);
        addDalRegWorker(reg, k);
        DalEmployee manager = new DalEmployee("Will", "Yanie", 8888, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111);
        addDalTPM_HRM(manager, t);
        manager = new DalEmployee("Nitzan", "Gal", 7171, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 222);
        addDalTPM_HRM(manager, t);
        manager = new DalEmployee("Fruku", "Kukku", 7272, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 333);
        addDalTPM_HRM(manager, t);
        manager = new DalEmployee("Chikon", "Remi", 9111, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111);
        addDalTPM_HRM(manager, h);
        manager = new DalEmployee("Daniel", "Requiel", 1000000, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111);
        addDalTPM_HRM(manager, m);
        manager = new DalEmployee("Momo", "Tizan", 9222, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 222);
        addDalTPM_HRM(manager, h);
        manager = new DalEmployee("dani", "ronen", 9333, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 333);
        addDalTPM_HRM(manager, h);
        manager = new DalEmployee("michal", "tores", 9444, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 444);
        addDalTPM_HRM(manager, h);
        manager = new DalEmployee("kim", "chan", 9555, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 555);
        addDalTPM_HRM(manager, h);
        manager = new DalEmployee("francine", "nagar", 9666, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 666);
        addDalTPM_HRM(manager, h);
        manager = new DalEmployee("moti", "kiman", 9777, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 777);
        addDalTPM_HRM(manager, h);
        manager = new DalEmployee("adaya", "sade", 9888, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 888);
        addDalTPM_HRM(manager, h);
        manager = new DalEmployee("shiran", "gadya", 9999, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 999);
        addDalTPM_HRM(manager, h);
    }


    private void initWorkers() {
        List<String> sqls = new LinkedList<>();
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Cashier\",1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Guard\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"StoreKeeper\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Usher\", 1,111);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Cashier\",1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Guard\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"StoreKeeper\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Usher\", 1,222);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Cashier\",1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Guard\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"StoreKeeper\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Usher\", 1,333);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Cashier\",1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Guard\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"StoreKeeper\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Usher\", 1,444);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Cashier\",1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Guard\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"StoreKeeper\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Usher\", 1,555);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Cashier\",1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Guard\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"StoreKeeper\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Usher\", 1,666);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Cashier\",1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Guard\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"StoreKeeper\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Usher\", 1,777);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Cashier\",1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Guard\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"StoreKeeper\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Usher\", 1,888);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Cashier\",1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Guard\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"StoreKeeper\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"Usher\", 1,999);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Day\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (0, \"Night\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Day\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (1, \"Night\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Day\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (2, \"Night\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Day\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (3, \"Night\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Day\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (4, \"Night\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Day\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (5, \"Night\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Day\", \"DriverLicenseD\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"DriverLicenseA\",1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"DriverLicenseB\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"DriverLicenseC\", 1,-1);");
        sqls.add("INSERT INTO ShiftRecommendedLineUp (shiftDay, shiftType, job, numberOfEmployee,branchID) VALUES (6, \"Night\", \"DriverLicenseD\", 1,-1);");


        openConnection();
        for (String sqlCommand : sqls) {
            try (PreparedStatement statement = conn.prepareStatement(sqlCommand)) {
                statement.execute();
            } catch (Exception exception) {
                return;
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initLocations() {
        List<String> sqls = new LinkedList<>();
        sqls.add("INSERT INTO Location (id, address, phone, contactName) VALUES (111, \"Super Lee beer sheva\", \"05212342\", \"amos\");");
        sqls.add("INSERT INTO Location (id, address, phone, contactName) VALUES (222, \"Super Lee tel aviv\", \"052234342\", \"bar\");");
        sqls.add("INSERT INTO Location (id, address, phone, contactName) VALUES (333, \"Super Lee eilat\", \"07434523\", \"nitay\");");
        sqls.add("INSERT INTO Location (id, address, phone, contactName) VALUES (444, \"Super Lee haifa\", \"09842173\", \"omri\");");
        sqls.add("INSERT INTO Location (id, address, phone, contactName) VALUES (555, \"Super Lee dimona\", \"02324323\", \"omer\");");
        sqls.add("INSERT INTO Location (id, address, phone, contactName) VALUES (666, \"Super Lee gedera\", \"01212441\", \"daniel\");");
        sqls.add("INSERT INTO Location (id, address, phone, contactName) VALUES (777, \"Super Lee jerusalam\", \"0453221\", \"david\");");
        sqls.add("INSERT INTO Location (id, address, phone, contactName) VALUES (888, \"Super Lee katzrin\", \"05293581\", \"shimon\");");
        sqls.add("INSERT INTO Location (id, address, phone, contactName) VALUES (999, \"Super Lee ashdod\", \"0412481248\", \"gershon\");");
        openConnection();
        for (String sqlCommand : sqls) {
            try (PreparedStatement statement = conn.prepareStatement(sqlCommand)) {
                statement.execute();
            } catch (Exception exception) {
                return;
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initSections() {
        List<String> sqls = new LinkedList<>();
        sqls.add("INSERT INTO Section (areaName, location) VALUES (\"South\", \"Super Lee beer sheva\");");
        sqls.add("INSERT INTO Section (areaName, location) VALUES (\"Middle\", \"Super Lee tel aviv\");");
        sqls.add("INSERT INTO Section (areaName, location) VALUES (\"South\", \"Super Lee eilat\");");
        sqls.add("INSERT INTO Section (areaName, location) VALUES (\"North\", \"Super Lee haifa\");");
        sqls.add("INSERT INTO Section (areaName, location) VALUES (\"South\", \"Super Lee dimona\");");
        sqls.add("INSERT INTO Section (areaName, location) VALUES (\"Middle\", \"Super Lee gedera\");");
        sqls.add("INSERT INTO Section (areaName, location) VALUES (\"Middle\", \"Super Lee jerusalam\");");
        sqls.add("INSERT INTO Section (areaName, location) VALUES (\"North\", \"Super Lee katzrin\");");
        sqls.add("INSERT INTO Section (areaName, location) VALUES (\"Middle\", \"Super Lee ashdod\");");
        openConnection();
        for (String sqlCommand : sqls) {
            try (PreparedStatement statement = conn.prepareStatement(sqlCommand)) {
                statement.execute();
            } catch (Exception exception) {
                return;
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initTrucks() {
        List<String> sqls = new LinkedList<>();
        sqls.add("INSERT INTO Trucks (licenseNum, model, maxWeight, netoWeight) VALUES (1,\"BMW\", 19,4);");
        sqls.add("INSERT INTO Trucks (licenseNum, model, maxWeight, netoWeight) VALUES (2,\"BMW\", 16,7);");
        sqls.add("INSERT INTO Trucks (licenseNum, model, maxWeight, netoWeight) VALUES (3,\"Mercdez\", 18,2);");
        sqls.add("INSERT INTO Trucks (licenseNum, model, maxWeight, netoWeight) VALUES (4,\"Volvo\", 14,6);");
        sqls.add("INSERT INTO Trucks (licenseNum, model, maxWeight, netoWeight) VALUES (5,\"Subaru\", 17,4);");
        openConnection();
        for (String sqlCommand : sqls) {
            try (PreparedStatement statement = conn.prepareStatement(sqlCommand)) {
                statement.execute();
            } catch (Exception exception) {
                return;
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        List<String> sqls = new LinkedList<>();
        sqls.add("CREATE TABLE IF NOT EXISTS \"Deliveries\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"departureDay\"\tDate,\n" +
                "\t\"shift\"\tINTEGER,\n" +
                "\t\"truckNum\"\tINTEGER,\n" +
                "\t\"driverName\"\tTEXT,\n" +
                "\t\"driverID\"\tINTEGER,\n" +
                "\t\"source\"\tTEXT,\n" +
                "\t\"truckWeight\"\tDOUBLE,\n" +
                "\t\"approved\"\tINTEGER,\n" +
                "\tPRIMARY KEY(\"id\"),\n" +
                "\tFOREIGN KEY(\"truckNum\") REFERENCES \"Trucks\"(\"id\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS \"DeliveryDestination\" (\n" +
                "\t\"deliveryID\"\tINTEGER NOT NULL,\n" +
                "\t\"address\"\tText NOT NULL,\n" +
                "\tPRIMARY KEY(\"deliveryID\",\"address\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS \"DeliveryDocs\" (\n" +
                "\t\"deliveryID\"\tINTEGER NOT NULL,\n" +
                "\t\"docID\"\tINTEGER NOT NULL,\n" +
                "\t\"dest\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"docID\"),\n" +
                "\tFOREIGN KEY(\"deliveryID\") REFERENCES \"Delivery\"(\"id\"),\n" +
                "\tFOREIGN KEY(\"dest\") REFERENCES \"Locations\"(\"address\")\n" +
                ");");   //DalDelivery Document
        sqls.add("CREATE TABLE IF NOT EXISTS \"Supplies\" (\n" +
                "\t\"deliveryDocID\"\tINTEGER NOT NULL,\n" +
                "\t\"supplyID\"\tINTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"supplyID\"),\n" +
                "\tFOREIGN KEY(\"supplyID\") REFERENCES \"Supply\"(\"id\"),\n" +
                "\tFOREIGN KEY(\"deliveryDocID\") REFERENCES \"DeliveryDocs\"(\"docID\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS \"Location\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL,\n" +
                "\t\"address\"\tTEXT NOT NULL,\n" +
                "\t\"phone\"\tTEXT,\n" +
                "\t\"contactName\"\tText,\n" +
                "\tPRIMARY KEY(\"address\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS\"Section\" (\n" +
                "\t\"areaName\"\tTEXT NOT NULL,\n" +
                "\t\"location\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"areaName\"),\n" +
                "\tFOREIGN KEY(\"location\") REFERENCES \"Locations\"(\"address\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS \"Supply\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"name\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"id\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS \"SupplyInDoc\" (\n" +
                "\t\"idSupply\"\tINTEGER,\n" +
                "\t\"idDoc\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"idSupply\",\"idDoc\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS\"Trucks\" (\n" +
                "\t\"licenseNum\"\tINTEGER NOT NULL,\n" +
                "\t\"model\"\tTEXT,\n" +
                "\t\"maxWeight\"\tDOUBLE,\n" +
                "\t\"netoWeight\"\tDOUBLE,\n" +
                "\tPRIMARY KEY(\"licenseNum\")\n" +
                ");");

        sqls.add("CREATE TABLE IF NOT EXISTS\"Employee\" (\n" +
                "\t\"f_name\"\tTEXT NOT NULL,\n" +
                "\t\"l_name\"\tTEXT NOT NULL,\n" +
                "\t\"id\"\tINTEGER NOT NULL,\n" +
                "\t\"wage\"\tINTEGER NOT NULL,\n" +
                "\t\"monthlyDayOffs\"\tDOUBLE,\n" +
                "\t\"monthlySickDays\"\tDOUBLE,\n" +
                "\t\"Bankid\"\tINTEGER,\n" +
                "\t\"BankBranch\"\tTEXT,\n" +
                "\t\"BankAccount\"\tINTEGER,\n" +
                "\t\"advanceStudyFunds\"\tINTEGER NOT NULL,\n" +
                "\t\"branch\"\tINTEGER NOT NULL,\n" +
                "\t\"type\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"id\")\n" +
                ");");

        sqls.add("CREATE TABLE IF NOT EXISTS\"RegularWorker\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL,\n" +
                "\t\"shiftManager\"\tBLOB NOT NULL,\n" +
                "\tPRIMARY KEY(\"id\"),\n" +
                "\tFOREIGN KEY(\"id\") REFERENCES \"Employee\"(\"id\")\n" +
                ");");

        sqls.add("CREATE TABLE IF NOT EXISTS\"JobWorker\" (\n" +
                "\t\"employeeID\"\tINTEGER NOT NULL,\n" +
                "\t\"job\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"employeeID\",\"job\"),\n" +
                "\tFOREIGN KEY(\"employeeID\") REFERENCES \"RegularWorker\"(\"id\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS\"Shift\" (\n" +
                "\t\"type\"\tTEXT NOT NULL,\n" +
                "\t\"day\"\tINTEGER NOT NULL,\n" +
                "\t\"shiftManagerID\"\tINTEGER NOT NULL,\n" +
                "\t\"date\"\tDATE NOT NULL,\n" +
                "\t\"branchID\"\tINTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"type\",\"date\",\"branchID\"),\n" +
                "\tFOREIGN KEY(\"shiftManagerID\") REFERENCES \"RegularWorker\"(\"id\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS\"ShiftAssignedWorker\" (\n" +
                "\t\"shiftDate\"\tDATE NOT NULL,\n" +
                "\t\"shiftType\"\tTEXT NOT NULL,\n" +
                "\t\"employeeID\"\tINTEGER NOT NULL,\n" +
                "\t\"job\"\tTEXT NOT NULL,\n" +
                "\t\"branchID\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"shiftDate\",\"shiftType\",\"job\",\"branchID\"),\n" +
                "\tFOREIGN KEY(\"shiftDate\") REFERENCES \"Shift\"(\"date\"),\n" +
                "\tFOREIGN KEY(\"shiftType\") REFERENCES \"Shift\"(\"type\"),\n" +
                "\tFOREIGN KEY(\"employeeID\") REFERENCES \"RegularWorker\"(\"id\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS\"ShiftDriver\" (\n" +
                "\t\"shiftDate\"\tDATE NOT NULL,\n" +
                "\t\"shiftType\"\tTEXT NOT NULL,\n" +
                "\t\"employeeID\"\tINTEGER NOT NULL,\n" +
                "\t\"job\"\tTEXT NOT NULL,\n" +
                "\tPRIMARY KEY(\"shiftDate\",\"shiftType\",\"employeeID\"),\n" +
                "\tFOREIGN KEY(\"shiftDate\") REFERENCES \"Shift\"(\"date\"),\n" +
                "\tFOREIGN KEY(\"shiftType\") REFERENCES \"Shift\"(\"type\"),\n" +
                "\tFOREIGN KEY(\"employeeID\") REFERENCES \"RegularWorker\"(\"id\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS\"ShiftRecommendedLineUp\" (\n" +
                "\t\"shiftDay\"\tINTEGER NOT NULL,\n" +
                "\t\"shiftType\"\tTEXT NOT NULL,\n" +
                "\t\"job\"\tTEXT NOT NULL,\n" +
                "\t\"numberOfEmployee\"\tINTEGER NOT NULL,\n" +
                "\t\"branchID\"\tINTEGER NOT NULL,\n" +
                "\tPRIMARY KEY(\"shiftDay\",\"shiftType\",\"job\",\"branchID\"),\n" +
                "\tFOREIGN KEY(\"branchID\") REFERENCES \"Location\"(\"id\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS\"PersonalConstraints\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL,\n" +
                "\t\"SUNDAY day\"\tBLOB NOT NULL,\n" +
                "\t\"SUNDAY night\"\tBLOB NOT NULL,\n" +
                "\t\"MONDAY day\"\tBLOB NOT NULL,\n" +
                "\t\"MONDAY night\"\tBLOB NOT NULL,\n" +
                "\t\"TUESDAY day\"\tBLOB NOT NULL,\n" +
                "\t\"TUESDAY night\"\tBLOB NOT NULL,\n" +
                "\t\"WEDNESDAY day\"\tBLOB NOT NULL,\n" +
                "\t\"WEDNESDAY night\"\tBLOB NOT NULL,\n" +
                "\t\"THURSDAY day\"\tBLOB NOT NULL,\n" +
                "\t\"THURSDAY night\"\tBLOB NOT NULL,\n" +
                "\t\"FRIDAY day\"\tBLOB NOT NULL,\n" +
                "\t\"FRIDAY night\"\tBLOB NOT NULL,\n" +
                "\t\"SATURDAY day\"\tBLOB NOT NULL,\n" +
                "\t\"SATURDAY night\"\tBLOB NOT NULL,\n" +
                "\tPRIMARY KEY(\"id\"),\n" +
                "\tFOREIGN KEY(\"id\") REFERENCES \"RegularWorker\"(\"id\")\n" +
                ");");
        sqls.add("CREATE TABLE IF NOT EXISTS\"CanceledRequests\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL,\n" +
                "\t\"HRPremission\"\tINTEGER,\n" +
                "\t\"LGPremission\"\tINTEGER,\n" +
                "\t\"INPremission\"\tINTEGER,\n" +
                "\tPRIMARY KEY(\"id\"),\n" +
                "\tFOREIGN KEY(\"id\") REFERENCES \"Deliveries\"(\"id\")\n" +
                ");");
        for (String sqlCommand : sqls) {
            try (PreparedStatement statement = conn.prepareStatement(sqlCommand)) {
                statement.execute();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Location
//---------------------------------------------------------------------------------------------------

    public void MakeBetterModuleQuerry() {
        String querry = "BEGIN TRANSACTION;\n" +
                "INSERT OR IGNORE INTO \"Items_Main\" (\"item_id\",\"item_name\",\"minimum_amount\",\"manufacture_name\",\"current_amount\",\"number_of_damaged_items\",\"next_supply_date\",\"current_price\",\"weight\",\"category\",\"branch_id\") VALUES (1,'milk',100,'tnuva',0,0,NULL,6.0,8,'milk',111),\n" +
                " (1,'milk',100,'tnuva',0,0,NULL,6.0,8,'milk',222),\n" +
                " (1,'milk',100,'tnuva',0,0,NULL,6.0,8,'milk',333),\n" +
                " (1,'milk',100,'tnuva',0,0,NULL,6.0,8,'milk',444),\n" +
                " (1,'milk',100,'tnuva',0,0,NULL,6.0,8,'milk',555),\n" +
                " (1,'milk',100,'tnuva',0,0,NULL,6.0,8,'milk',666),\n" +
                " (1,'milk',100,'tnuva',0,0,NULL,6.0,8,'milk',777),\n" +
                " (1,'milk',100,'tnuva',0,0,NULL,6.0,8,'milk',888),\n" +
                " (1,'milk',100,'tnuva',0,0,NULL,6.0,8,'milk',999),\n" +
                " (3,'butter',50,'tnuva',0,0,'24-06-2021',4.0,5,'cheese',111),\n" +
                " (3,'butter',50,'tnuva',0,0,NULL,4.0,5,'cheese',222),\n" +
                " (3,'butter',50,'tnuva',0,0,NULL,4.0,5,'cheese',333),\n" +
                " (3,'butter',50,'tnuva',0,0,NULL,4.0,5,'cheese',444),\n" +
                " (3,'butter',50,'tnuva',0,0,NULL,4.0,5,'cheese',555),\n" +
                " (3,'butter',50,'tnuva',0,0,NULL,4.0,5,'cheese',666),\n" +
                " (3,'butter',50,'tnuva',0,0,NULL,4.0,5,'cheese',777),\n" +
                " (3,'butter',50,'tnuva',0,0,NULL,4.0,5,'cheese',888),\n" +
                " (3,'butter',50,'tnuva',0,0,NULL,4.0,5,'cheese',999),\n" +
                " (4,'cheder',700,'tnuva',0,0,'24-06-2021',10.0,5,'cheese',111),\n" +
                " (4,'cheder',700,'tnuva',0,0,NULL,10.0,5,'cheese',222),\n" +
                " (4,'cheder',700,'tnuva',0,0,NULL,10.0,5,'cheese',333),\n" +
                " (4,'cheder',700,'tnuva',0,0,NULL,10.0,5,'cheese',444),\n" +
                " (4,'cheder',700,'tnuva',0,0,NULL,10.0,5,'cheese',555),\n" +
                " (4,'cheder',700,'tnuva',0,0,NULL,10.0,5,'cheese',666),\n" +
                " (4,'cheder',700,'tnuva',0,0,NULL,10.0,5,'cheese',777),\n" +
                " (4,'cheder',700,'tnuva',0,0,NULL,10.0,5,'cheese',888),\n" +
                " (4,'cheder',700,'tnuva',0,0,NULL,10.0,5,'cheese',999),\n" +
                " (6,'cottage',500,'tnuva',0,0,'24-06-2021',5.5,5,'cheese',111),\n" +
                " (6,'cottage',500,'tnuva',0,0,NULL,5.5,5,'cheese',222),\n" +
                " (6,'cottage',500,'tnuva',0,0,NULL,5.5,5,'cheese',333),\n" +
                " (6,'cottage',500,'tnuva',0,0,NULL,5.5,5,'cheese',444),\n" +
                " (6,'cottage',500,'tnuva',0,0,NULL,5.5,5,'cheese',555),\n" +
                " (6,'cottage',500,'tnuva',0,0,NULL,5.5,5,'cheese',666),\n" +
                " (6,'cottage',500,'tnuva',0,0,NULL,5.5,5,'cheese',777),\n" +
                " (6,'cottage',500,'tnuva',0,0,NULL,5.5,5,'cheese',888),\n" +
                " (6,'cottage',500,'tnuva',0,0,NULL,5.5,5,'cheese',999),\n" +
                " (7,'white bread',203,'Berman',0,0,'28-06-2021',6.9,10,'breads',111),\n" +
                " (7,'white bread',203,'Berman',0,0,NULL,6.9,10,'breads',222),\n" +
                " (7,'white bread',203,'Berman',0,0,NULL,6.9,10,'breads',333),\n" +
                " (7,'white bread',203,'Berman',0,0,NULL,6.9,10,'breads',444),\n" +
                " (7,'white bread',203,'Berman',0,0,NULL,6.9,10,'breads',555),\n" +
                " (7,'white bread',203,'Berman',0,0,NULL,6.9,10,'breads',666),\n" +
                " (7,'white bread',203,'Berman',0,0,NULL,6.9,10,'breads',777),\n" +
                " (7,'white bread',203,'Berman',0,0,NULL,6.9,10,'breads',888),\n" +
                " (7,'white bread',203,'Berman',0,0,NULL,6.9,10,'breads',999),\n" +
                " (8,'baguette',250,'Berman',0,0,'28-06-2021',4.5,3,'breads',111),\n" +
                " (8,'baguette',250,'Berman',0,0,NULL,4.5,3,'breads',222),\n" +
                " (8,'baguette',250,'Berman',0,0,NULL,4.5,3,'breads',333),\n" +
                " (8,'baguette',250,'Berman',0,0,NULL,4.5,3,'breads',444),\n" +
                " (8,'baguette',250,'Berman',0,0,NULL,4.5,3,'breads',555),\n" +
                " (8,'baguette',250,'Berman',0,0,NULL,4.5,3,'breads',666),\n" +
                " (8,'baguette',250,'Berman',0,0,NULL,4.5,3,'breads',777),\n" +
                " (8,'baguette',250,'Berman',0,0,NULL,4.5,3,'breads',888),\n" +
                " (8,'baguette',250,'Berman',0,0,NULL,4.5,3,'breads',999),\n" +
                " (9,'black bread',150,'Berman',0,0,'28-06-2021',13.9,10,'breads',111),\n" +
                " (10,'bagel',750,'Berman',0,0,'28-06-2021',3.25,7,'breads',111),\n" +
                " (11,'pita',500,'Berman',0,0,'28-06-2021',1.5,3,'breads',111),\n" +
                " (12,'white sugar',100,'sugat',0,0,'20-06-2021',4.5,6,'sugar',111),\n" +
                " (13,'flour',1000,'sugat',0,0,'20-06-2021',5.5,5,'flour',111),\n" +
                " (14,'eggs',500,'sugat',0,0,'20-06-2021',11.5,8,'eggs',111),\n" +
                " (9,'black bread',150,'Berman',0,0,NULL,13.9,10,'breads',777),\n" +
                " (10,'bagel',750,'Berman',0,0,NULL,3.25,7,'breads',777),\n" +
                " (11,'pita',500,'Berman',0,0,NULL,1.5,3,'breads',777),\n" +
                " (12,'white sugar',100,'sugat',0,0,NULL,4.5,6,'sugar',777),\n" +
                " (13,'flour',1000,'sugat',0,0,NULL,5.5,5,'flour',777),\n" +
                " (14,'eggs',500,'sugat',0,0,NULL,11.5,8,'eggs',777),\n" +
                " (9,'black bread',150,'Berman',0,0,NULL,13.9,10,'breads',888),\n" +
                " (10,'bagel',750,'Berman',0,0,NULL,3.25,7,'breads',888),\n" +
                " (11,'pita',500,'Berman',0,0,NULL,1.5,3,'breads',888),\n" +
                " (12,'white sugar',100,'sugat',0,0,NULL,4.5,6,'sugar',888),\n" +
                " (13,'flour',1000,'sugat',0,0,NULL,5.5,5,'flour',888),\n" +
                " (14,'eggs',500,'sugat',0,0,NULL,11.5,8,'eggs',888),\n" +
                " (9,'black bread',150,'Berman',0,0,NULL,13.9,10,'breads',222),\n" +
                " (10,'bagel',750,'Berman',0,0,NULL,3.25,7,'breads',222),\n" +
                " (11,'pita',500,'Berman',0,0,NULL,1.5,3,'breads',222),\n" +
                " (12,'white sugar',100,'sugat',0,0,NULL,4.5,6,'sugar',222),\n" +
                " (13,'flour',1000,'sugat',0,0,NULL,5.5,5,'flour',222),\n" +
                " (14,'eggs',500,'sugat',0,0,NULL,11.5,8,'eggs',222),\n" +
                " (9,'black bread',150,'Berman',0,0,NULL,13.9,10,'breads',333),\n" +
                " (10,'bagel',750,'Berman',0,0,NULL,3.25,7,'breads',333),\n" +
                " (11,'pita',500,'Berman',0,0,NULL,1.5,3,'breads',333),\n" +
                " (12,'white sugar',100,'sugat',0,0,NULL,4.5,6,'sugar',333),\n" +
                " (13,'flour',1000,'sugat',0,0,NULL,5.5,5,'flour',333),\n" +
                " (14,'eggs',500,'sugat',0,0,NULL,11.5,8,'eggs',333),\n" +
                " (9,'black bread',150,'Berman',0,0,NULL,13.9,10,'breads',444),\n" +
                " (10,'bagel',750,'Berman',0,0,NULL,3.25,7,'breads',444),\n" +
                " (11,'pita',500,'Berman',0,0,NULL,1.5,3,'breads',444),\n" +
                " (12,'white sugar',100,'sugat',0,0,NULL,4.5,6,'sugar',444),\n" +
                " (13,'flour',1000,'sugat',0,0,NULL,5.5,5,'flour',444),\n" +
                " (14,'eggs',500,'sugat',0,0,NULL,11.5,8,'eggs',444),\n" +
                " (9,'black bread',150,'Berman',0,0,NULL,13.9,10,'breads',555),\n" +
                " (10,'bagel',750,'Berman',0,0,NULL,3.25,7,'breads',555),\n" +
                " (11,'pita',500,'Berman',0,0,NULL,1.5,3,'breads',555),\n" +
                " (12,'white sugar',100,'sugat',0,0,NULL,4.5,6,'sugar',555),\n" +
                " (13,'flour',1000,'sugat',0,0,NULL,5.5,5,'flour',555),\n" +
                " (14,'eggs',500,'sugat',0,0,NULL,11.5,8,'eggs',555),\n" +
                " (9,'black bread',150,'Berman',0,0,NULL,13.9,10,'breads',999),\n" +
                " (10,'bagel',750,'Berman',0,0,NULL,3.25,7,'breads',999),\n" +
                " (11,'pita',500,'Berman',0,0,NULL,1.5,3,'breads',999),\n" +
                " (12,'white sugar',100,'sugat',0,0,NULL,4.5,6,'sugar',999),\n" +
                " (13,'flour',1000,'sugat',0,0,NULL,5.5,5,'flour',999),\n" +
                " (14,'eggs',500,'sugat',0,0,NULL,11.5,8,'eggs',999),\n" +
                " (9,'black bread',150,'Berman',0,0,NULL,13.9,10,'breads',666),\n" +
                " (10,'bagel',750,'Berman',0,0,NULL,3.25,7,'breads',666),\n" +
                " (11,'pita',500,'Berman',0,0,NULL,1.5,3,'breads',666),\n" +
                " (12,'white sugar',100,'sugat',0,0,NULL,4.5,6,'sugar',666),\n" +
                " (13,'flour',1000,'sugat',0,0,NULL,5.5,5,'flour',666),\n" +
                " (14,'eggs',500,'sugat',0,0,NULL,11.5,8,'eggs',666),\n" +
                " (15,'canola oil',350,'sugat',0,0,'20-06-2021',7.5,10,'oil',111),\n" +
                " (15,'canola oil',350,'sugat',0,0,NULL,7.5,10,'oil',222),\n" +
                " (15,'canola oil',350,'sugat',0,0,NULL,7.5,10,'oil',333),\n" +
                " (15,'canola oil',350,'sugat',0,0,NULL,7.5,10,'oil',444),\n" +
                " (15,'canola oil',350,'sugat',0,0,NULL,7.5,10,'oil',555),\n" +
                " (15,'canola oil',350,'sugat',0,0,NULL,7.5,10,'oil',666),\n" +
                " (15,'canola oil',350,'sugat',0,0,NULL,7.5,10,'oil',777),\n" +
                " (15,'canola oil',350,'sugat',0,0,NULL,7.5,10,'oil',888),\n" +
                " (15,'canola oil',350,'sugat',0,0,NULL,7.5,10,'oil',999);\n" +
                "INSERT OR IGNORE INTO \"categories\" (\"category_name\",\"father_category_name\") VALUES ('food',NULL),\n" +
                " ('diary','food'),\n" +
                " ('cheese','diary'),\n" +
                " ('milk','dairy'),\n" +
                " ('general food','food'),\n" +
                " ('oil','general food'),\n" +
                " ('eggs','general food'),\n" +
                " ('flour','general food'),\n" +
                " ('sugar','general food'),\n" +
                " ('breads','general food');\n" +
                "INSERT OR IGNORE INTO \"Suppliers\" (\"supplierBN\",\"name\",\"address\",\"bankAccount\",\"termOfPayment\",\"needDelivery\") VALUES (1,'Tnuva','eli sinai 2 netivot',430123,'EOM90',0),\n" +
                " (2,'Berman','katif 1 netivot',456908,'EOM60',0),\n" +
                " (3,'Willi-food','katif 2 netivot',674039,'EOM90',0),\n" +
                " (4,'the Farmer','katif 3 netivot',652375,'EOM30',0),\n" +
                " (5,'itzik','katif 4 netivot',12311,'EOM30',1);\n" +
                "INSERT OR IGNORE INTO \"SuppliersTermsOfSupply\" (\"supplierBN\",\"day\") VALUES (1,'SUNDAY'),\n" +
                " (1,'TUESDAY'),\n" +
                " (1,'THURSDAY'),\n" +
                " (2,'MONDAY'),\n" +
                " (3,'SUNDAY'),\n" +
                " (4,'TUESDAY'),\n" +
                " (5,'TUESDAY');\n" +
                "INSERT OR IGNORE INTO \"Orders\" (\"orderID\",\"supplierBN\",\"issueDate\",\"supplyDate\",\"totalOrderPrice\",\"isClosed\",\"branchID\") VALUES (1,1,'2021-06-20','2021-06-24',16600.0,0,111),\n" +
                " (2,2,'2021-06-20','2021-06-28',13071.45,0,111),\n" +
                " (3,3,'2021-06-16','2021-06-20',16675.0,0,111);\n" +
                "INSERT OR IGNORE INTO \"Contacts\" (\"name\",\"supplierBN\",\"phoneNumber\",\"email\") VALUES ('Israel Israeli',1,'0503578638','isreal@gmail.com'),\n" +
                " ('Moshe Cohen',2,'0524560987','moshe@gmail.com'),\n" +
                " ('Adam Shalom',3,'0545678123','adam@gmail.com'),\n" +
                " ('Rinat Sharon',4,'0536847384','rinat@gmail.com'),\n" +
                " ('itzik',5,'0536843334','ri22@gmail.com');\n" +
                "INSERT OR IGNORE INTO \"SuppliersIncludedItems\" (\"supplierBN\",\"catalogNumber\",\"serialNumber\",\"price\") VALUES (1,1,1,6.0),\n" +
                " (1,2,2,2.9),\n" +
                " (1,3,3,4.0),\n" +
                " (1,4,4,10.0),\n" +
                " (1,5,5,8.9),\n" +
                " (1,6,6,5.5),\n" +
                " (2,7,7,6.9),\n" +
                " (2,8,8,4.5),\n" +
                " (2,9,9,13.9),\n" +
                " (2,10,10,3.25),\n" +
                " (2,11,11,1.5),\n" +
                " (3,12,12,4.5),\n" +
                " (3,13,13,5.5),\n" +
                " (3,14,14,11.5),\n" +
                " (3,15,15,7.5),\n" +
                " (4,16,16,5.9),\n" +
                " (4,17,17,5.9),\n" +
                " (4,18,18,8.9),\n" +
                " (4,19,19,4.9),\n" +
                " (4,20,20,6.9),\n" +
                " (5,1,1,0.5);\n" +
                "INSERT OR IGNORE INTO \"SuppliersOrdersDiscounts\" (\"supplierBN\",\"amount\",\"discount\") VALUES (1,2000,0.02),\n" +
                " (1,5000,0.04),\n" +
                " (1,10000,0.05),\n" +
                " (2,2000,0.05),\n" +
                " (2,6000,0.08),\n" +
                " (3,15000,0.04),\n" +
                " (3,20000,0.07),\n" +
                " (4,15000,0.05),\n" +
                " (4,20000,0.07);\n" +
                "INSERT OR IGNORE INTO \"SuppliersItemsDiscounts\" (\"supplierBN\",\"catalogNumber\",\"amount\",\"discount\") VALUES (1,1,5000,0.02),\n" +
                " (1,1,10000,0.05),\n" +
                " (1,2,3000,0.05),\n" +
                " (1,3,6000,0.02),\n" +
                " (1,4,1000,0.03),\n" +
                " (1,5,1000,0.03),\n" +
                " (1,6,4000,0.03),\n" +
                " (2,7,2000,0.03),\n" +
                " (2,8,500,0.005),\n" +
                " (2,9,1500,0.03),\n" +
                " (2,10,700,0.02),\n" +
                " (2,11,2000,0.06),\n" +
                " (3,12,2000,0.03),\n" +
                " (3,13,3000,0.03),\n" +
                " (3,14,4000,0.04),\n" +
                " (3,15,4000,0.03),\n" +
                " (4,16,10000,0.01),\n" +
                " (4,16,15000,0.03),\n" +
                " (4,17,10000,0.01),\n" +
                " (4,17,15000,0.03),\n" +
                " (4,18,8000,0.02),\n" +
                " (4,19,8000,0.01),\n" +
                " (4,19,10000,0.03),\n" +
                " (4,20,5000,0.01),\n" +
                " (4,20,9000,0.03),\n" +
                " (5,1,100,0.1);\n" +
                "INSERT OR IGNORE INTO \"OrderItems\" (\"orderID\",\"catalogNumber\",\"name\",\"amount\",\"cost\",\"discountCost\",\"totalCost\") VALUES (1,3,'butter',250,4.0,4.0,1000.0),\n" +
                " (1,4,'cheder',900,10.0,10.0,9000.0),\n" +
                " (1,6,'cottage',200,5.5,5.5,6600.0),\n" +
                " (2,7,'white bread',200,6.9,6.9,2780.7),\n" +
                " (2,8,'baguette',200,4.5,4.5,1350.0),\n" +
                " (2,9,'black bread',200,13.9,13.9,4865.0),\n" +
                " (2,10,'bagel',150,3.25,3.185,3025.75),\n" +
                " (2,11,'pita',150,1.5,1.5,1050.0),\n" +
                " (3,12,'white sugar',300,4.5,4.5,4500.0),\n" +
                " (3,14,'eggs',100,11.5,11.5,8050.0),\n" +
                " (3,15,'canola oil',120,7.5,7.5,4125.0);\n" +
                "COMMIT;\n";
        openConnection();
        try (Statement statement = conn.createStatement()) {
            statement.execute(querry);
        } catch (SQLException e) {
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }


    }

    public List<DalLocation> getDalLocations(int id) {
        List<DalLocation> locations = new LinkedList<>();
        openConnection();
        String sql = "SELECT * FROM DeliveryDestination WHERE deliveryID=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DalLocation dalLocation = new DalLocation();
                dalLocation.setId(rs.getInt("deliveryID"));
                dalLocation.setAddress(rs.getString("address"));
                locations.add(getDalLocation(dalLocation.getAddress()));
            }
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return locations;
    }


    public DalLocation getDalLocation(String address) {
        DalLocation dalLocation = null;
        openConnection();
        String sql = "SELECT * FROM Location WHERE address=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, address);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dalLocation = new DalLocation();
                dalLocation.setId(rs.getInt("id"));
                dalLocation.setAddress(rs.getString("address"));
                dalLocation.setPhone(rs.getString("phone"));
                dalLocation.setContactName(rs.getString("contactName"));
            }
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return dalLocation;
    }

    public DalLocation getDalLocationByID(int id) {
        DalLocation dalLocation = null;
        openConnection();
        String sql = "SELECT * FROM Location WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dalLocation = new DalLocation();
                dalLocation.setId(rs.getInt("id"));
                dalLocation.setAddress(rs.getString("address"));
                dalLocation.setPhone(rs.getString("phone"));
                dalLocation.setContactName(rs.getString("contactName"));
            }
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return dalLocation;
    }


    public boolean addDalLocation(DalLocation location) {
        boolean check = false;
        openConnection();
        String sql = "INSERT INTO Location(id,address,phone,contactName) VALUES(?,?,?,?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, location.getId());
            statement.setString(2, location.getAddress());
            statement.setString(3, location.getPhone());
            statement.setString(4, location.getContactName());
            statement.executeUpdate();
            check = true;
        } catch (SQLException e) {
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
        return check;
    }

    public boolean removeDalLocation(int locationID) {
        boolean check = false;
        openConnection();
        String sql = "DELETE FROM Location WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, locationID);
            pstmt.executeUpdate();
            conn.close();
            check = true;
        } catch (SQLException e) {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return check;
    }

    public boolean updateDalLocation(DalLocation location) {
        boolean check = false;
        openConnection();
        String sql = "UPDATE Location SET id=?,address=?,phone=?,contactName=? WHERE id=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, location.getId());
            statement.setString(2, location.getAddress());
            statement.setString(3, location.getPhone());
            statement.setString(4, location.getContactName());
            statement.setInt(5, location.getId());
            statement.executeUpdate();
            check = true;
        } catch (SQLException e) {
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
        return check;
    }


    //Trucks
//------------------------------------------------------------------------------------


    public DalTruck getDalTruck(int id) {
        DalTruck dalTruck = null;
        openConnection();
        String sql = "SELECT * FROM Trucks WHERE licenseNum=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                dalTruck = new DalTruck();
                dalTruck.setLicenseNum(rs.getInt("licenseNum"));
                dalTruck.setMaxWeight(rs.getDouble("maxWeight"));
                dalTruck.setNetoWeight(rs.getDouble("netoWeight"));
                dalTruck.setModel(rs.getString("model"));
            }
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return dalTruck;
    }

    public boolean addDalTruck(DalTruck truck) {
        boolean check = false;
        openConnection();
        String sql = "INSERT INTO Trucks(licenseNum,model,maxWeight,netoWeight) VALUES(?,?,?,?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, truck.getLicenseNum());
            statement.setString(2, truck.getModel());
            statement.setDouble(3, truck.getMaxWeight());
            statement.setDouble(4, truck.getNetoWeight());
            statement.executeUpdate();
            check = true;
        } catch (SQLException e) {
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
        return check;
    }

    public boolean updateDalTruck(DalTruck truck) {
        boolean check = false;
        openConnection();
        String sql = "UPDATE Trucks SET licenseNum=?,model=?,maxWeight=?,netoWeight=? Where licenseNum=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, truck.getLicenseNum());
            statement.setString(2, truck.getModel());
            statement.setDouble(3, truck.getMaxWeight());
            statement.setDouble(4, truck.getNetoWeight());
            statement.setInt(5, truck.getLicenseNum());
            statement.executeUpdate();
            check = true;
        } catch (SQLException e) {
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
        return check;
    }

    public boolean removeDalTruck(int licenseNum) {
        boolean check = false;
        openConnection();
        String sql = "DELETE FROM Trucks WHERE licenseNum=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, licenseNum);
            pstmt.executeUpdate();
            conn.close();
            check = true;
        } catch (SQLException e) {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return check;
        }
        return check;
    }


    public List<DalTruck> getTrucks() {
        List<DalTruck> allTrucks = new LinkedList<>();
        openConnection();
        DalTruck dalTruck = null;
        String sql = "SELECT * FROM Trucks";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                dalTruck = new DalTruck();
                dalTruck.setLicenseNum(rs.getInt("licenseNum"));
                dalTruck.setModel(rs.getString("model"));
                dalTruck.setMaxWeight(rs.getDouble("maxWeight"));
                dalTruck.setNetoWeight(rs.getDouble("netoWeight"));
                allTrucks.add(dalTruck);
            }
            conn.close();
        } catch (SQLException e) {
            return null;
        }
        return allTrucks;
    }

    //Deliveries
//---------------------------------------------------------------------------------------
    public int getNextDeliveryIdx() throws SQLException {
        openConnection();
        String sql = "SELECT max(id) FROM CanceledRequests";
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(sql);
            if (rs.next())
                return rs.getInt("max(id)") + 1;
            return 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return 0;
        }finally {
            conn.close();

        }
    }

        public DalDelivery getDalDelivery ( int id){
            DalDelivery dalDelivery = null;
            openConnection();
            String sql = "SELECT * FROM Deliveries WHERE id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    dalDelivery = new DalDelivery();
                    dalDelivery.setId(rs.getInt("id"));
                    dalDelivery.setDepartureDay((rs.getDate("departureDay")).toLocalDate());
                    dalDelivery.setShift(rs.getInt("shift"));
                    dalDelivery.setTruckNum(rs.getInt("truckNum"));
                    dalDelivery.setDriverName(rs.getString("driverName"));
                    dalDelivery.setDriverID(rs.getInt("driverID"));
                    dalDelivery.setSource(rs.getString("source"));
                    dalDelivery.setTruckWeight(rs.getDouble("truckWeight"));
                    List<DalLocation> destination = getDalLocations(dalDelivery.getId());
                    dalDelivery.setDestination(destination);
                    List<DalDeliveryDoc> documents = getDeliveryDocByDeliveryID(dalDelivery.getId());
                    dalDelivery.setDocs(documents);
                    dalDelivery.setApproved(rs.getInt("approved"));
                }
                conn.close();

            } catch (SQLException e) {
                return null;
            }
            return dalDelivery;
        }

        public Date MakeDateFromString (String s){
            List<String> list = new ArrayList<>(Arrays.asList(s.split("-")));
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

        private int getIntFromString (String s){
            return Integer.parseInt(s);

        }

        private Date parseDate (String date){
            try {
                return new SimpleDateFormat("dd-MM-yyyy").parse(date);
            } catch (ParseException e) {
                return null;
            }
        }

        public DalDelivery getRequestDalDelivery ( int id, int approved){
            DalDelivery dalDelivery = null;
            openConnection();
            String sql = "SELECT * FROM Deliveries WHERE id=? AND approved=? ";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                pstmt.setInt(2, approved);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    dalDelivery = new DalDelivery();
                    dalDelivery.setId(rs.getInt("id"));
                    dalDelivery.setDepartureDay((rs.getDate("departureDay")).toLocalDate());
                    dalDelivery.setShift(rs.getInt("shift"));
                    dalDelivery.setTruckNum(rs.getInt("truckNum"));
                    dalDelivery.setDriverName(rs.getString("driverName"));
                    dalDelivery.setDriverID(rs.getInt("driverID"));
                    dalDelivery.setSource(rs.getString("source"));
                    dalDelivery.setTruckWeight(rs.getDouble("truckWeight"));
                    List<DalLocation> destination = getDalLocations(dalDelivery.getId());
                    dalDelivery.setDestination(destination);
                    List<DalDeliveryDoc> documents = getDeliveryDocByDeliveryID(dalDelivery.getId());
                    dalDelivery.setDocs(documents);
                    dalDelivery.setApproved(rs.getInt("approved"));
                }
                conn.close();

            } catch (SQLException e) {
                return null;
            }
            return dalDelivery;
        }

        public List<DalDelivery> getDalDeliveryRequest () {//brings all requsetList!
            List<DalDelivery> requestList = null;
            openConnection();
            DalDelivery dalDelivery;
            String sql = "SELECT * FROM Deliveries WHERE driverID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, -1);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    dalDelivery = new DalDelivery();
                    dalDelivery.setId(rs.getInt("id"));
                    dalDelivery.setDepartureDay((rs.getDate("departureDay")).toLocalDate());
                    dalDelivery.setShift(rs.getInt("shift"));
                    dalDelivery.setTruckNum(rs.getInt("truckNum"));
                    dalDelivery.setDriverName(rs.getString("driverName"));
                    dalDelivery.setDriverID(rs.getInt("driverID"));
                    dalDelivery.setSource(rs.getString("source"));
                    dalDelivery.setTruckWeight(rs.getDouble("truckWeight"));
                    List<DalLocation> destination = getDalLocations(dalDelivery.getId());
                    dalDelivery.setDestination(destination);
                    List<DalDeliveryDoc> documents = getDeliveryDocByDeliveryID(dalDelivery.getId());
                    dalDelivery.setDestination(destination);
                    dalDelivery.setApproved(rs.getInt("approved"));
                    assert false;
                    requestList.add(dalDelivery);
                }
                conn.close();

            } catch (SQLException e) {
                return null;
            }
            return requestList;
        }

        public void removeDalDelivery ( int delID){
            openConnection();
            String sql = "DELETE FROM Deliveries WHERE id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, delID);
                pstmt.executeUpdate();
                conn.close();
            } catch (SQLException e) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println(e.getMessage());
                }
            }
        }

        public void removeDalDest ( int delID){
            openConnection();
            String sql = "DELETE FROM DeliveryDestination WHERE id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, delID);
                pstmt.executeUpdate();
                conn.close();
            } catch (SQLException e) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("NO");
                    ex.printStackTrace();
                }
            }
        }

        public boolean updateDalDelivery (DalDelivery delivery){
            boolean check = false;
            openConnection();
            String sql = "UPDATE Deliveries SET id=?,departureDay=?,shift=?,truckNum=?, driverName=?, driverID=? ,source=? ,truckWeight=? ,approved=? WHERE id=?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, delivery.getId());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String time = df.format(Utility.localToDate(delivery.getDepartureDay()));
                statement.setString(2, time);
                statement.setInt(3, delivery.getShift());
                statement.setInt(4, delivery.getTruckNum());
                statement.setString(5, delivery.getDriverName());
                statement.setInt(6, delivery.getDriverID());
                statement.setString(7, delivery.getSource());
                statement.setDouble(8, delivery.getTruckWeight());
                statement.setInt(9, delivery.getApproved());
                statement.setInt(10, delivery.getId());
                statement.executeUpdate();
                check = true;
            } catch (SQLException e) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }

        // sqls.add("CREATE TABLE IF NOT EXISTS\"CanceledRequests\" (\n" +
        //                "\t\"id\"\tINTEGER NOT NULL,\n" +
        //                "\t\"HRPremission\"\tINTEGER,\n" +
        //                "\t\"LGPremission\"\tINTEGER,\n" +
        //                "\t\"INPremission\"\tINTEGER,\n" +
        //                "\tPRIMARY KEY(\"id\"),\n" +
        //                "\tFOREIGN KEY(\"id\") REFERENCES \"Deliveries\"(\"id\")\n" +
        //                ");");

        public boolean addDalDelivery (DalDelivery delivery){
            boolean check = false;
            openConnection();
            String sql = "INSERT INTO Deliveries(id,departureDay,shift,truckNum,driverName,driverID,source,truckWeight,approved) VALUES(?,?,?,?,?,?,?,?,?)";
            String sql2 = "INSERT INTO CanceledRequests(id,HRPremission,LGPremission,INPremission) VALUES(?,?,?,?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, delivery.getId());
                java.sql.Date sqlDate = java.sql.Date.valueOf(delivery.getDepartureDay());
                statement.setDate(2, sqlDate);
                statement.setInt(3, delivery.getShift());
                statement.setInt(4, delivery.getTruckNum());
                statement.setString(5, delivery.getDriverName());
                statement.setInt(6, delivery.getDriverID());
                statement.setString(7, delivery.getSource());
                statement.setDouble(8, delivery.getTruckWeight());
                statement.setInt(9, delivery.getApproved());
                statement.executeUpdate();
                check = true;
            } catch (SQLException e) {
            }
            try (PreparedStatement statement = conn.prepareStatement(sql2)) {
                statement.setInt(1, delivery.getId());
                statement.setInt(2, 0);
                statement.setInt(3, 0);
                statement.setInt(4, 0);
                statement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }

        public int previousRequestID () {
            String query = "SELECT max(id) " +
                    "FROM CanceledRequests;";
            openConnection();
            try {
                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(query);
                if (result.next())
                    return result.getInt("max(id)");
                return 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return 0;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }

        }


        public boolean addDestination ( int deliveryID, String address){
            boolean check = false;
            openConnection();
            String sql = "INSERT INTO DeliveryDestination(id,departureDay) VALUES(?,?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, deliveryID);
                statement.setString(2, address);
                check = true;
            } catch (SQLException e) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }


        //DeliveryDoc
//-----------------------------------------------------------------------------------------------------------
        public void removeDeliveryDocs ( int docID){
            openConnection();
            String sql = "DELETE FROM DeliveryDocs WHERE docID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, docID);
                pstmt.executeUpdate();
                conn.close();
            } catch (SQLException e) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        public boolean removeDeliveryDoc ( int docID, int deliveryID){
            boolean check = false;
            openConnection();
            String sql = "DELETE FROM DeliveryDocs WHERE docID=? AND deliveryID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, docID);
                pstmt.setInt(2, deliveryID);
                pstmt.executeUpdate();
                conn.close();
                check = true;
            } catch (SQLException e) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return check;
        }


        public DalDeliveryDoc getDeliveryDoc ( int docID){
            DalDeliveryDoc doc = null;
            openConnection();
            String sql = "SELECT * FROM DeliveryDocs WHERE docID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, docID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    doc = new DalDeliveryDoc();
                    doc.setId(rs.getInt("docID"));
                    doc.setDest(rs.getString("destination"));
                }
                conn.close();
                if (doc != null) {
                    List<DalSupply> supplies = getDalSupplies(doc.getId());
                    doc.setSupplies(supplies);
                } else
                    return null;
            } catch (SQLException e) {
                return null;
            }
            return doc;
        }

        public List<DalDeliveryDoc> getDeliveryDocByDeliveryID ( int deliveryDocID){
            List<DalDeliveryDoc> dalDeliveryDoc = new LinkedList<>();
            openConnection();
            String sql = "SELECT * FROM DeliveryDocs WHERE deliveryID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, deliveryDocID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    DalDeliveryDoc dalDeliveryDoc1 = new DalDeliveryDoc();
                    dalDeliveryDoc1 = getDeliveryDoc(rs.getInt("docID"));
                    if (dalDeliveryDoc1 != null) {
                        List<DalSupply> supplies = getDalSupplies(dalDeliveryDoc1.getId());
                        dalDeliveryDoc1.setSupplies(supplies);
                    } else {
                        return null;
                    }
                }
                conn.close();
            } catch (SQLException e) {
                return null;
            }
            return dalDeliveryDoc;
        }

        public boolean addDalDeliveryDoc (DalDeliveryDoc deliveryDoc){
            boolean check = false;
            openConnection();
            String sql = "INSERT INTO DeliveryDocs(deliveryID,docID,dest) VALUES(?,?,?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, deliveryDoc.getDeliveryID());
                statement.setInt(2, deliveryDoc.getId());
                statement.setString(3, deliveryDoc.getDest());
                statement.executeUpdate();
                check = true;
            } catch (SQLException e) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }


        //Supplies
//--------------------------------------------------------------------------------------------------------------

        //get Supply list to deliveryDOC

        public List<DalSupply> getDalSupplies ( int deliveryID){
            List<DalSupply> dalSupplies = new LinkedList<>();
            openConnection();
            String sql = "SELECT * FROM SupplyInDoc WHERE deliveryID=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, deliveryID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    DalSupply dalSupply = getDalSupply(rs.getInt("docID"));
                    dalSupplies.add(dalSupply);
                }
                conn.close();
            } catch (SQLException e) {
                return null;
            }
            return dalSupplies;
        }

        //get supply byID

        public DalSupply getDalSupply ( int supplyID){
            DalSupply dalSupply = new DalSupply();
            openConnection();
            String sql = "SELECT * FROM Supply WHERE id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, supplyID);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    dalSupply.setId(rs.getInt("id"));
                    dalSupply.setName(rs.getString("name"));
                }
                conn.close();
            } catch (SQLException e) {
                return null;
            }
            return dalSupply;
        }

        public boolean addDalSuppliesToDoc ( int docID, int supply){
            boolean check = false;
            openConnection();
            String sql = "INSERT INTO SupplyInDoc(idSupply,idDoc) VALUES(?,?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, supply);
                statement.setInt(2, docID);
                statement.executeUpdate();
                check = true;
            } catch (SQLException e) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }


        public boolean removeSup ( int docID, int supply){
            boolean check = false;
            openConnection();
            String sql = "DELETE FROM SupplyInDoc WHERE idSupply=? AND idDoc=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, supply);
                pstmt.setInt(2, docID);
                pstmt.executeUpdate();
                check = true;
                conn.close();
            } catch (SQLException e) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return check;
        }

        //Sections
//------------------------------------------------------------------------------------------------------------------


        public DalSection getSections () {
            DalSection sections = null;
            openConnection();
            String sql = "SELECT * FROM Section";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                sections = new DalSection();
                while (rs.next()) {
                    String area = rs.getString("area");
                    String location = rs.getString("location");
                    sections.addLocationToSection(area, location);
                }
                conn.close();
            } catch (SQLException e) {
                return null;
            }
            return sections;
        }


        public boolean addLocationToSection (String areaName, String location){
            boolean check = false;
            openConnection();
            String sql = "INSERT INTO Section(areaName,location) VALUES(?,?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, areaName);
                statement.setString(2, location);
                check = true;
            } catch (SQLException e) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }

        //Employee
//-----------------------------------------------------------------------------------------------------------------
        public DalEmployee getTPM_HRM ( int id){
            DalEmployee dalEmployee = null;
            openConnection();
            String sql = "SELECT * FROM Employee Where id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String f_name = rs.getString("f_name");
                    String l_name = rs.getString("l_name");
                    int Eid = rs.getInt("id");
                    int wage = rs.getInt("wage");
                    double monthlyDayOffs = rs.getDouble("monthlyDayOffs");
                    double monthlySickDays = rs.getDouble("monthlySickDays");
                    int bankid = rs.getInt("Bankid");
                    String BankBranch = rs.getString("BankBranch");
                    int BankAccount = rs.getInt("BankAccount");
                    int advanceStudyFunds = rs.getInt("advanceStudyFunds");
                    int branch = rs.getInt("branch");
                    dalEmployee = new DalEmployee(f_name, l_name, Eid, wage, monthlyDayOffs, monthlySickDays, bankid, BankBranch, BankAccount, advanceStudyFunds, branch);
                }
            } catch (SQLException e) {
                return null;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return dalEmployee;
        }
        //"Items_Main" ("item_id","item_name","minimum_amount","manufacture_name","current_amount","number_of_damaged_items","next_supply_date","current_price","weight","category","branch_id")
        // VALUES (1,'milk',100,'tnuva',0,0,'24-06-2021',6.0,0,'milk',111),

        public DalSupply getItems ( int id){
            DalSupply dalSupply = null;
            openConnection();
            String sql = "SELECT * FROM Items_Main WHERE item_id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    dalSupply = new DalSupply();
                    dalSupply.setId(rs.getInt("item_id"));
                    dalSupply.setName(rs.getString("item_name"));
                    dalSupply.setQuantity(rs.getInt("current_amount"));
                }
                conn.close();
            } catch (SQLException e) {
                return null;
            }
            return dalSupply;
        }

        public String getEmployeeType ( int id){
            String output = "";
            openConnection();
            String sql = "SELECT type FROM Employee Where id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    output = rs.getString("type");
                }
            } catch (SQLException e) {
                return "";
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return output;
        }

        public boolean addDalTPM_HRM (DalEmployee dalEmployee, String type){
            boolean check = false;
            openConnection();
            String sql = "INSERT INTO Employee VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, dalEmployee.getF_Name());
                statement.setString(2, dalEmployee.getL_Name());
                statement.setInt(3, dalEmployee.getId());
                statement.setInt(4, dalEmployee.getWage());
                statement.setDouble(5, dalEmployee.getMonthlyDayOffs());
                statement.setDouble(6, dalEmployee.getMonthlySickDays());
                statement.setInt(7, dalEmployee.getBankID());
                statement.setString(8, dalEmployee.getBranch());
                statement.setInt(9, dalEmployee.getAccountNumber());
                statement.setInt(10, dalEmployee.getAdvancedStudyFunds());
                statement.setInt(11, dalEmployee.getLocation());
                statement.setString(12, type);
                statement.executeUpdate();
                check = true;
            } catch (SQLException e) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }

        public boolean updateDalTPM_HRM (DalEmployee dalEmployee){
            boolean check = false;
            openConnection();
            String sql = "UPDATE Employee SET f_name=?,l_name=?,wage = ?,monthlyDayOffs=?,monthlySickDays=?,Bankid=?,BankBranch=?,BankAccount=?,advanceStudyFunds=? WHERE id=?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, dalEmployee.getF_Name());
                statement.setString(2, dalEmployee.getL_Name());
                statement.setInt(3, dalEmployee.getWage());
                statement.setDouble(4, dalEmployee.getMonthlyDayOffs());
                statement.setDouble(5, dalEmployee.getMonthlySickDays());
                statement.setInt(6, dalEmployee.getBankID());
                statement.setString(7, dalEmployee.getBranch());
                statement.setInt(8, dalEmployee.getAccountNumber());
                statement.setInt(9, dalEmployee.getAdvancedStudyFunds());
                statement.setInt(10, dalEmployee.getId());
                statement.executeUpdate();
                check = true;
            } catch (SQLException e) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }
        //RegularWorker
//-----------------------------------------------------------------------------------------------------------------

        public DalRegularWorker getRegularWorker ( int id){
            DalRegularWorker dalRegularWorker = null;
            DalEmployee dalEmployee = getTPM_HRM(id);
            if (dalEmployee != null) {
                dalRegularWorker = new DalRegularWorker(dalEmployee);
                String sql2 = "SELECT * FROM RegularWorker Where id=?";
                openConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        dalRegularWorker.setShiftManager(rs.getBoolean("shiftManager"));
                    }
                } catch (SQLException e) {
                    return null;
                }
                String sql3 = "SELECT * FROM PersonalConstraints WHERE id=?";
                openConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql3)) {
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        boolean[][] PersonalConstraints = new boolean[7][2];
                        PersonalConstraints[0][0] = rs.getBoolean("SUNDAY day");
                        PersonalConstraints[0][1] = rs.getBoolean("SUNDAY night");
                        PersonalConstraints[1][0] = rs.getBoolean("MONDAY day");
                        PersonalConstraints[1][1] = rs.getBoolean("MONDAY night");
                        PersonalConstraints[2][0] = rs.getBoolean("TUESDAY day");
                        PersonalConstraints[2][1] = rs.getBoolean("TUESDAY night");
                        PersonalConstraints[3][0] = rs.getBoolean("WEDNESDAY day");
                        PersonalConstraints[3][1] = rs.getBoolean("WEDNESDAY night");
                        PersonalConstraints[4][0] = rs.getBoolean("THURSDAY day");
                        PersonalConstraints[4][1] = rs.getBoolean("THURSDAY night");
                        PersonalConstraints[5][0] = rs.getBoolean("FRIDAY day");
                        PersonalConstraints[5][1] = rs.getBoolean("FRIDAY night");
                        PersonalConstraints[6][0] = rs.getBoolean("SATURDAY day");
                        PersonalConstraints[6][1] = rs.getBoolean("SATURDAY night");
                        dalRegularWorker.setPersonalConstraints(PersonalConstraints);
                    }
                } catch (SQLException e) {
                    return null;
                }
                String sql4 = "SELECT * FROM JobWorker WHERE employeeID=?";
                openConnection();
                try (PreparedStatement pstmt = conn.prepareStatement(sql4)) {
                    pstmt.setInt(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    List<Job> jobs = new LinkedList<>();
                    while (rs.next()) {
                        Job job = Utility.parseAnyJob(rs.getString("Job"));
                        jobs.add(job);
                    }
                    dalRegularWorker.setCertifiedWorks(jobs);
                } catch (SQLException e) {
                    return null;
                }
            }
            return dalRegularWorker;
        }


        public boolean addDalRegWorker (DalRegularWorker dalRegularWorker, String type){
            boolean check = false;
            addDalTPM_HRM(dalRegularWorker, type);
            String sql = "INSERT INTO RegularWorker VALUES (?,?)";
            String sql2 = "INSERT INTO PersonalConstraints VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String sql3 = "INSERT INTO JobWorker VALUES (?,?)";
            openConnection();
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                if (dalRegularWorker.getPersonalConstraints() != null) {
                    //boolean[][] PersonalConstraints = new boolean[7][2];
                    statement.setInt(1, dalRegularWorker.getId());
                    statement.setBoolean(2, dalRegularWorker.isShiftManager());
                    statement.executeUpdate();
                }
                try (PreparedStatement statement2 = conn.prepareStatement(sql2)) {
                    statement2.setInt(1, dalRegularWorker.getId());
                    statement2.setBoolean(2, dalRegularWorker.getPersonalConstraints()[0][0]);
                    statement2.setBoolean(3, dalRegularWorker.getPersonalConstraints()[0][1]);
                    statement2.setBoolean(4, dalRegularWorker.getPersonalConstraints()[1][0]);
                    statement2.setBoolean(5, dalRegularWorker.getPersonalConstraints()[1][1]);
                    statement2.setBoolean(6, dalRegularWorker.getPersonalConstraints()[2][0]);
                    statement2.setBoolean(7, dalRegularWorker.getPersonalConstraints()[2][1]);
                    statement2.setBoolean(8, dalRegularWorker.getPersonalConstraints()[3][0]);
                    statement2.setBoolean(9, dalRegularWorker.getPersonalConstraints()[3][1]);
                    statement2.setBoolean(10, dalRegularWorker.getPersonalConstraints()[4][0]);
                    statement2.setBoolean(11, dalRegularWorker.getPersonalConstraints()[4][1]);
                    statement2.setBoolean(12, dalRegularWorker.getPersonalConstraints()[5][0]);
                    statement2.setBoolean(13, dalRegularWorker.getPersonalConstraints()[5][1]);
                    statement2.setBoolean(14, dalRegularWorker.getPersonalConstraints()[6][0]);
                    statement2.setBoolean(15, dalRegularWorker.getPersonalConstraints()[6][1]);
                    statement2.executeUpdate();
                    openConnection();
                    try (PreparedStatement statement3 = conn.prepareStatement(sql3)) {
                        for (Job job : dalRegularWorker.getCertifiedWorks()) {
                            statement3.setInt(1, dalRegularWorker.getId());
                            statement3.setString(2, String.valueOf(job));
                            statement3.executeUpdate();
                        }
                    }
                }
                check = true;
            } catch (SQLException e) {
                check = false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }

        public List<DalRegularWorker> getDriverforJob (String job,int branch){
            List<DalRegularWorker> output = new LinkedList<>();
            openConnection();
            String sql = "SELECT employeeID FROM JobWorker WHERE job = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, job);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    DalRegularWorker dalRegularWorker = getRegularWorker(rs.getInt("employeeID"));
                    if (dalRegularWorker.getLocation() == branch)
                        output.add(dalRegularWorker);
                }
            } catch (SQLException e) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return output;
        }

        //todo need link
        public boolean updateDalRegWorker (DalRegularWorker dalRegularWorker, String type){
            boolean check = false;
            openConnection();
            addDalTPM_HRM(dalRegularWorker, type);
            String sql = "INSERT INTO RegularWorker VALUES (?)";
            String sql2 = "INSERT INTO PersonalConstraints VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String sql3 = "INSERT INTO JobWorker VALUES (?,?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                if (dalRegularWorker.getPersonalConstraints() != null) {
                    boolean[][] PersonalConstraints = new boolean[7][2];
                    statement.setBoolean(1, dalRegularWorker.isShiftManager());
                    statement.executeUpdate();
                }
                try (PreparedStatement statement2 = conn.prepareStatement(sql2)) {
                    statement2.setInt(1, dalRegularWorker.getId());
                    statement2.setBoolean(2, dalRegularWorker.getPersonalConstraints()[0][0]);
                    statement2.setBoolean(3, dalRegularWorker.getPersonalConstraints()[0][1]);
                    statement2.setBoolean(4, dalRegularWorker.getPersonalConstraints()[1][0]);
                    statement2.setBoolean(5, dalRegularWorker.getPersonalConstraints()[1][1]);
                    statement2.setBoolean(6, dalRegularWorker.getPersonalConstraints()[2][0]);
                    statement2.setBoolean(7, dalRegularWorker.getPersonalConstraints()[2][1]);
                    statement2.setBoolean(8, dalRegularWorker.getPersonalConstraints()[3][0]);
                    statement2.setBoolean(9, dalRegularWorker.getPersonalConstraints()[3][1]);
                    statement2.setBoolean(10, dalRegularWorker.getPersonalConstraints()[4][0]);
                    statement2.setBoolean(11, dalRegularWorker.getPersonalConstraints()[4][1]);
                    statement2.setBoolean(12, dalRegularWorker.getPersonalConstraints()[5][0]);
                    statement2.setBoolean(13, dalRegularWorker.getPersonalConstraints()[5][1]);
                    statement2.setBoolean(14, dalRegularWorker.getPersonalConstraints()[6][0]);
                    statement2.setBoolean(15, dalRegularWorker.getPersonalConstraints()[6][1]);
                    statement2.executeUpdate();
                    try (PreparedStatement statement3 = conn.prepareStatement(sql3)) {
                        for (Job job : dalRegularWorker.getCertifiedWorks()) {
                            statement3.setInt(1, dalRegularWorker.getId());
                            statement3.setString(2, job.toString());
                            statement3.executeUpdate();
                        }
                    }
                }
            } catch (SQLException e) {
                return false;
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }
        //Shift
//----------------------------------------------------------------------------------------------------------------

        public boolean addShift (DalShift dalShift){
            boolean check = false;
            openConnection();
            String sql = "INSERT INTO Shift VALUES (?,?,?,?,?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(Utility.dateToLocal(dalShift.getDate()));
                statement.setInt(1, dalShift.getType());
                statement.setInt(2, dalShift.getDay());
                statement.setInt(3, dalShift.getManagerId());
                statement.setDate(4, sqlDate);
                statement.setInt(5, dalShift.getBarnchId());
                statement.executeUpdate();
            } catch (SQLException e) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            openConnection();
            String sql2 = "INSERT INTO ShiftAssignedWorker VALUES(?,?,?,?,?)";
            try (PreparedStatement statement = conn.prepareStatement(sql2)) {
                Map<Integer, Job> temp = dalShift.getRegularWorkerList();
                for (Map.Entry<Integer, Job> entry : temp.entrySet()) {
                    java.sql.Date sqlDate = java.sql.Date.valueOf(Utility.dateToLocal(dalShift.getDate()));
                    statement.setInt(2, dalShift.getType());
                    statement.setString(4, entry.getValue().toString());
                    statement.setInt(3, entry.getKey());
                    statement.setInt(5, dalShift.getBarnchId());
                    statement.setDate(1, sqlDate);
                    statement.executeUpdate();
                    check = true;
                }
            } catch (SQLException ignored) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }


        public DalShift getShift ( int branchID, Tuple<Date, ShiftType > typeTuple){
            DalShift dalShift;
            openConnection();
            String sql = "SELECT * FROM Shift WHERE date = ? AND type = ? AND branchID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDate(1, java.sql.Date.valueOf(Utility.dateToLocal(typeTuple.x)));
                pstmt.setInt(2, Utility.checkShift(typeTuple.y));
                pstmt.setInt(3, branchID);
                ResultSet rs = pstmt.executeQuery();
                dalShift = new DalShift();
                if (rs.next()) {
                    dalShift.setManagerId(rs.getInt("shiftManagerID"));
                    dalShift.setDay(rs.getInt("day"));
                    dalShift.setType(rs.getInt("type"));
                    dalShift.setBarnchId(rs.getInt("branchID"));
                    dalShift.setDate(Utility.localToDate(rs.getDate("date").toLocalDate()));
                } else return null;
                conn.close();
            } catch (SQLException e) {
                return null;
            }
            String sql2 = "SELECT * FROM ShiftAssignedWorker WHERE shiftDate = ? AND shiftType = ? AND branchID = ?";
            openConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(Utility.dateToLocal(typeTuple.x));
                pstmt.setDate(1, sqlDate);
                pstmt.setInt(2, Utility.checkShift(typeTuple.y));
                pstmt.setInt(3, branchID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    dalShift.addRegularWorker(rs.getInt("employeeID"), rs.getString("job"));
                }
                conn.close();
            } catch (SQLException e) {
                return null;
            }
            return dalShift;
        }

        public boolean addDriverShift (Tuple < Date, ShiftType > typeTuple,int id, Job job){
            boolean check = false;
            openConnection();
            String sql = "INSERT INTO ShiftDriver VALUES (?,?,?,?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                java.sql.Date date = java.sql.Date.valueOf(Utility.dateToLocal(typeTuple.x));
                statement.setDate(1, date);
                statement.setString(2, typeTuple.y.toString());
                statement.setInt(3, id);
                statement.setString(4, job.toString());
                statement.executeUpdate();
                check = true;
            } catch (SQLException e) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }

        //todo
        public List<Integer> getDriverShft (Tuple < Date, ShiftType > typeTuple){
            List<Integer> dalRegularWorkerList = null;
            openConnection();
            String sql = "SELECT * FROM ShiftDriver WHERE date = ? AND type = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDate(1, java.sql.Date.valueOf(Utility.dateToLocal(typeTuple.x)));
                pstmt.setString(2, typeTuple.y.toString());
                ResultSet rs = pstmt.executeQuery();
                dalRegularWorkerList = new LinkedList<>();
                if (!rs.next()) return null;
                while (rs.next()) {
                    dalRegularWorkerList.add(rs.getInt("employeeID"));
                }
                conn.close();
            } catch (SQLException e) {
                return null;
            }
            return dalRegularWorkerList;
        }

        //recommendedLineUp

        //---------------------------------------------------------------------------------------------------------------
        public DalRecommendedLineUp getRecommendedLineUp () throws SQLException {
            DalRecommendedLineUp dalRecommendedLineUp = new DalRecommendedLineUp();
            openConnection();
            String sql = "SELECT * FROM ShiftRecommendedLineUp WHERE NOT branchID = ?"; //RegularWorker
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, -1);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int day = rs.getInt("shiftDay");
                    String type = rs.getString("shiftType");
                    String job = rs.getString("job");
                    int empNumb = rs.getInt("numberOfEmployee");
                    int branchId = rs.getInt("branchID");
                    dalRecommendedLineUp.add(day, type, job, empNumb, branchId);
                }
            } catch (SQLException e) {
                return null;
            }
            String sql2 = "SELECT * FROM ShiftRecommendedLineUp WHERE branchID = ?"; //Driver
            try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
                pstmt.setInt(1, -1);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int day = rs.getInt("shiftDay");
                    String type = rs.getString("shiftType");
                    String job = rs.getString("job");
                    int empNumb = rs.getInt("numberOfEmployee");
                    int branchId = rs.getInt("branchID");
                    dalRecommendedLineUp.add(day, type, job, empNumb);
                }
            } catch (SQLException e) {
                return null;
            } finally {
                conn.close();
            }
            return dalRecommendedLineUp;
        }

        public boolean addRecommendLineup (DalRecommendedLineUp dalRecommendedLineUp) throws SQLException {
            boolean check = false;
            openConnection();
            String sql = "INSERT INTO ShiftRecommendedLineUp VALUES (?,?,?,?,?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                Map<AtomicInteger, Map<Job, AtomicInteger>[][]> temp = dalRecommendedLineUp.getUpdaterecommendedLineUp();
                for (Map.Entry<AtomicInteger, Map<Job, AtomicInteger>[][]> entry : temp.entrySet()) {
                    Map<Job, AtomicInteger>[][] temp2 = entry.getValue();
                    for (int i = 0; i < 7; i++) {
                        for (int j = 0; j < 2; j++) {
                            for (Map.Entry<Job, AtomicInteger> entry2 : temp2[i][j].entrySet()) {
                                statement.setInt(1, i);
                                statement.setInt(2, j);
                                statement.setString(3, entry2.getKey().toString());
                                statement.setInt(4, entry2.getValue().intValue());
                                statement.setInt(5, entry.getKey().intValue());
                                statement.executeUpdate();
                            }
                        }
                    }
                    check = true;
                }
                Map<Job, AtomicInteger>[][] temp3 = dalRecommendedLineUp.getRecommendedLineUpDrivers();
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 2; j++) {
                        for (Map.Entry<Job, AtomicInteger> entry2 : temp3[i][j].entrySet()) {
                            statement.setInt(1, i);
                            statement.setInt(2, j);
                            statement.setString(3, entry2.getKey().toString());
                            statement.setInt(4, entry2.getValue().intValue());
                            statement.setNull(5, Types.BIGINT);
                            statement.executeUpdate();
                        }
                    }
                }
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return check;
        }

        public boolean upDateRecommendLineup ( int i, ShiftType shiftType, Job job,int number, int activeEmployeeBrach) throws
        SQLException {
            boolean output = false;
            openConnection();
            String sql = "UPDATE ShiftRecommendedLineUp SET numberOfEmployee = ? WHERE shiftDay = ? AND shiftType = ? AND job = ? AND branchID = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, number);
                statement.setInt(2, i);
                statement.setString(3, shiftType.toString());
                statement.setString(4, job.toString());
                statement.setInt(5, activeEmployeeBrach);
                statement.executeUpdate();
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return output;
        }

        public List<Integer> loadEmployees () {
            List<Integer> output = new LinkedList<>();
            openConnection();
            String sql = "SELECT id FROM Employee";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                //pstmt.setInt(1, location);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    output.add(rs.getInt("id"));
                }
            } catch (SQLException e) {
            } finally {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
            return output;
        }

        public int getDeletePermission () throws SQLException {
            int ret;
            openConnection();
            String sql = "SELECT id FROM CanceledRequests WHERE HRPremission = ? AND LGPremission = ? AND INPremission = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, 1);
                pstmt.setInt(2, 1);
                pstmt.setInt(3, 1);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    ret = rs.getInt("id");
                } else ret = -1;
                if (ret != -1)
                    deleteFromCancel(ret);
            } catch (SQLException e) {
                return -1;
            } finally {
                conn.close();
            }
            return ret;
        }

        private void deleteFromCancel ( int ret) throws SQLException {
            openConnection();
            String sql = "DELETE FROM CanceledRequests WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, ret);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                conn.close();
            }
        }

        public void setDeletePermissionHR ( int id) throws SQLException {
            openConnection();
            String sql = "UPDATE CanceledRequests SET HRPremission = ? WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, 1);
                statement.setInt(2, id);
                statement.executeUpdate();
            } finally {
                try {
                    int temp = getDeletePermission();
                    if (temp != -1) removeDalDelivery(temp);
                    conn.close();

                } catch (SQLException ignored) {
                }
            }
        }

        public void setDeletePermissionLG ( int id) throws SQLException {
            openConnection();
            String sql = "UPDATE CanceledRequests SET LGPremission = ? WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, 1);
                statement.setInt(2, id);
                statement.executeUpdate();
            } finally {
                try {
                    int temp = getDeletePermission();
                    if (temp != -1) removeDalDelivery(temp);
                    conn.close();

                } catch (SQLException ignored) {
                }
            }
        }

        public void setDeletePermissionIN ( int id) throws SQLException {
            openConnection();
            String sql = "UPDATE CanceledRequests SET INPremission = ? WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, 1);
                statement.setInt(2, id);
                statement.executeUpdate();
            } finally {
                try {
                    int temp = getDeletePermission();
                    if (temp != -1) removeDalDelivery(temp);
                    conn.close();

                } catch (SQLException ignored) {
                }
            }
        }

    }