package Bussiness_Layer.ShiftPackage;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.lang.*;

import java.time.LocalDate;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.next;

import java.util.Map;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import Bussiness_Layer.BussinessDataAccess;
import Bussiness_Layer.EmployeePackage.Driver;
import Bussiness_Layer.EmployeePackage.Employee;
import Bussiness_Layer.EmployeePackage.EmployeeController;
import Bussiness_Layer.EmployeePackage.RegularWorker;
import Bussiness_Layer.Job;
import Bussiness_Layer.TransportationPackage.Location;
import Bussiness_Layer.Tuple;
import Bussiness_Layer.Utility;
import Facade_Layer.FacadeObjects.DriverFacade;
import Facade_Layer.FacadeObjects.LocationFacade;


public class ShiftController {
    private final int zero = 0;
    private final int one = 1;
    private LocalTime night = LocalTime.of(14, 0);
    private static ShiftController single_instance = null;
    //private Map<Tuple<Date, ShiftType>, Shift> shiftHistory;
    private Map<Tuple<Date, ShiftType>, List<Integer>> transport;
    private Map<Location, Map<Tuple<Date, ShiftType>, Shift>> shiftHistory;
    private EmployeeController employeeController = EmployeeController.getInstance();
    private RecommendedLineUp recommendedLineUp;
    private List<Location> branches; //TODO: database?

    private ShiftController() throws SQLException {
        initBranchs();
        recommendedLineUp = Objects.requireNonNull(BussinessDataAccess.getBTD()).getRecommendedLineUp();
        initShiftHistory();
        transport = new HashMap<>();
    }

    private void initBranchs() {
        branches = new LinkedList<>();
        for (int j = 1; j < 10; j++) {
            branches.add(new Location(j * 111, "", "", ""));
        }
    }

    private void initShiftHistory() {
        shiftHistory = new HashMap<>();
        for (Location run : branches) {
            Map<Tuple<Date, ShiftType>, Shift> loc = new HashMap<>();
            shiftHistory.putIfAbsent(run, loc);
        }
    }

    public static ShiftController getInstance() throws SQLException {
        if (single_instance == null)
            single_instance = new ShiftController();
        return single_instance;
    }

    public Shift getShift(Tuple<Date, ShiftType> shiftTuple, int beanchId) throws SQLException {
        Shift output = null;
        for (Location run : branches) {
            if (run.getId() == beanchId) {
                Map<Tuple<Date, ShiftType>, Shift> loc = getBranchShiftHistory(run);
                if (loc != null) {
                    for (Map.Entry<Tuple<Date, ShiftType>, Shift> entry : loc.entrySet()) {
                        Tuple<Date, ShiftType> temp = entry.getKey();
                        if (temp.x.equals(shiftTuple.x) & temp.y.equals(shiftTuple.y)) {
                            output = loc.get(temp);
                            break;
                        }
                    }
                }
            } else break;
        }
        if (output == null) {
            output = Objects.requireNonNull(BussinessDataAccess.getBTD()).getShift(shiftTuple.y, shiftTuple.x, beanchId);
            if (output != null) {
                Map<Tuple<Date, ShiftType>, Shift> loc = shiftHistory.get(getLocation(beanchId));
                loc.put(shiftTuple, output);
            }
        }
        return output;
    }

    private Map<Tuple<Date, ShiftType>, Shift> getBranchShiftHistory(Location run) {
        Map<Tuple<Date, ShiftType>, Shift> output = null;
        for (Map.Entry<Location, Map<Tuple<Date, ShiftType>, Shift>> entry : shiftHistory.entrySet()) {
            if (entry.getKey().getId() == run.getId()) {
                output = shiftHistory.get(entry.getKey());
                break;
            }
        }
        return output;
    }

    public Tuple<Date, ShiftType> getShiftTuple(Tuple<Date, ShiftType> shiftTuple) {
        Tuple<Date, ShiftType> output = null;
        for (Map.Entry<Tuple<Date, ShiftType>, List<Integer>> entry : transport.entrySet()) {
            Tuple<Date, ShiftType> temp = entry.getKey();
            if (temp.x.toString().equals(shiftTuple.x.toString()) & temp.y.toString().equals(shiftTuple.y.toString())) {
                output = temp;
                break;
            }
        }
        return output;
    }


    public boolean requestForDelivery(Job license, List<Location> location, int i, ShiftType shiftType) {
        Tuple<Integer, ShiftType> typeTuple = new Tuple<>(new Integer(i), shiftType);
        if (recommendedLineUp == null)
            recommendedLineUp = BussinessDataAccess.getBTD().getRecommendedLineUp();
        recommendedLineUp.setUpDriver(i, shiftType, license);
        int j = Utility.checkShift(shiftType);
        for (Location loc : location) {
            //Todo:request a store keeper in the recommended lineup in the listed day
            if (branches.contains(loc)) {
                if (recommendedLineUp.getNumofEmpl(Job.StoreKeeper, i, j, loc).intValue() == 0) {
                    recommendedLineUp.setNewNumber(i, j, Job.StoreKeeper, one, loc);
                }
            }
        }
        //Todo:request a driver in the recommended lineup in the listed day
        return true;
    }

    public boolean createMultiShift(int branchId) throws Exception {
        LocalDate now = LocalDate.now();
        if (Utility.checkDay(now.getDayOfWeek()) >= 0) { //create the next week shifts on THURSDAY
            LocalDate shiftDate = LocalDate.now().with(next(SUNDAY));
            boolean create = true;
            int i;
            Map<RegularWorker, AtomicInteger> workerIntegerMAP = new HashMap();
            for (i = 0; i < 7; i++) {
                if (!createShift(shiftDate, ShiftType.Day, i, workerIntegerMAP, branchId)) {
                    create = false;
                    System.out.println("Shift: " + shiftDate + "  " + ShiftType.Day + " dont created, miss shift Manager.");
                    break;
                }

                if (!createShift(shiftDate, ShiftType.Night, i, workerIntegerMAP, branchId)) {
                    create = false;
                    System.out.println("Shift: " + shiftDate + "  " + ShiftType.Night + " dont created, miss shift Manager.");
                    break;
                }
                createDriverList(shiftDate);
                driverAssignment(shiftDate, Job.DriverLicenseA);
                driverAssignment(shiftDate, Job.DriverLicenseB);
                driverAssignment(shiftDate, Job.DriverLicenseC);
                driverAssignment(shiftDate, Job.DriverLicenseD);
                shiftDate = shiftDate.plusDays(1);
            }
            return create;
        } else {
            System.out.println("Today its not THURSDAY, you cannot create new shift..");
            return false;
        }
    }

    private void createDriverList(LocalDate shiftDate) {
        Tuple<Date, ShiftType> typeTupleDay = new Tuple<>(Utility.localToDate(shiftDate), ShiftType.Day);
        transport.putIfAbsent(typeTupleDay, new LinkedList<>());
        Tuple<Date, ShiftType> typeTupleNight = new Tuple<>(Utility.localToDate(shiftDate), ShiftType.Night);
        transport.putIfAbsent(typeTupleNight, new LinkedList<>());
    }


    private void driverAssignment(LocalDate shiftDate, Job job) {
        int driverNumber = 0;
        List<Employee> drivers = new LinkedList<>();
        drivers = employeeController.jobCertifiedAvailableDrivers(job, shiftDate.getDayOfWeek(), ShiftType.Day);
        driverNumber = recommendedLineUp.getDriverNumber(Utility.checkDay(shiftDate.getDayOfWeek()), 0, job);
        Collections.shuffle(drivers);
        for (Employee employee : drivers) {
            if (driverNumber > 0) {
                Driver driver = (Driver) employee;
                //if(driver.checkAvailability(Utility.checkDay(shiftDate.getDayOfWeek()),ShiftType.Day)){
                Tuple<Date, ShiftType> typeTuple = new Tuple<>(Utility.localToDate(shiftDate), ShiftType.Day);
                if (!working(typeTuple, employee)) {
                    transport.get(getShiftTuple(typeTuple)).add(driver.getId());
                    driver.insertShiftRecord(Utility.localToDate(shiftDate), null);
                    driverNumber = driverNumber - 1;
                }
                //}
            } else break;
        }
        if (driverNumber > 0)//todo
            System.out.println("Miss " + driverNumber + " employees who can work at: " + job + '\n');
        drivers = employeeController.jobCertifiedAvailableDrivers(job, shiftDate.getDayOfWeek(), ShiftType.Night);
        driverNumber = recommendedLineUp.getDriverNumber(Utility.checkDay(shiftDate.getDayOfWeek()), 1, Job.DriverLicenseA);
        Collections.shuffle(drivers);
        for (Employee employee : drivers) {
            if (driverNumber > 0) {
                Driver driver = (Driver) employee;
                //if(driver.checkAvailability(Utility.checkDay(shiftDate.getDayOfWeek()),ShiftType.Night)){
                Tuple<Date, ShiftType> typeTuple = new Tuple<>(Utility.localToDate(shiftDate), ShiftType.Night);
                if (!working(typeTuple, employee)) {
                    transport.get(getShiftTuple(typeTuple)).add(driver.getId());
                    driver.insertShiftRecord(Utility.localToDate(shiftDate), null);
                    BussinessDataAccess.getBTD().addDriverShift(driver.getId(), driver.getJob(), typeTuple);
                    driverNumber = driverNumber - 1;
                }
                //}
            } else break;
        }
        if (driverNumber > 0)//todo
            System.out.println("Miss " + driverNumber + " employees who can work at: " + job + '\n');
    }

    private boolean working(Tuple<Date, ShiftType> typeTuple, Employee employee) {
        boolean output = false;
        List<Integer> driverList = transport.get(getShiftTuple(typeTuple));
        if (driverList != null)
            if (driverList.contains(employee))
                output = true;
            else {//todo
                driverList = BussinessDataAccess.getBTD().getDriverShift(typeTuple);
                if (driverList != null)
                    if (driverList.contains(employee))
                        output = true;
            }
        if (typeTuple.y == ShiftType.Day)
            typeTuple = new Tuple<>(typeTuple.x, ShiftType.Night);
        else typeTuple = new Tuple<>(typeTuple.x, ShiftType.Day);
        driverList = transport.get(getShiftTuple(typeTuple));
        if (driverList != null)
            if (driverList.contains(employee))
                output = true;
        return output;
    }

    private boolean createShift(LocalDate localDate, ShiftType shiftType, int day, Map<RegularWorker, AtomicInteger> workerIntegerMAP, int branchId) throws Exception {
        int sh;
        String miss = "";
        DayOfWeek dayOfWeek = Utility.dayOfWeek(day);
        boolean manager = false;
        sh = Utility.checkShift(shiftType);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Shift shift = new Shift(shiftType, day, branchId);
        Tuple<Date, ShiftType> shiftTypeTuple = new Tuple<>(date, shiftType);
        Shift temp = getShift(shiftTypeTuple, branchId);
        if (recommendedLineUp == null) {
            recommendedLineUp = Objects.requireNonNull(BussinessDataAccess.getBTD()).getRecommendedLineUp();
        }
        Map<Job, AtomicInteger>[][] temple = recommendedLineUp.getRecommendedLineUp(branchId);
        if (temp == null) {
            for (Map.Entry<Job, AtomicInteger> run : temple[day][sh].entrySet()) {
                Job job = run.getKey();
                int sum = run.getValue().intValue();
                List<Employee> regularWorkers = employeeController.jobCertifiedAvailableWorkers(job, dayOfWeek, shiftType, branchId);
                Collections.shuffle(regularWorkers);
                for (Employee employee : regularWorkers) {
                    if (sum > 0) {
                        RegularWorker regularWorker = (RegularWorker) employee;
                        AtomicInteger zero = new AtomicInteger(0);
                        workerIntegerMAP.putIfAbsent(regularWorker, zero);
                        boolean canwork = regularWorker.checkIfOccupiedInDate(date);
                        AtomicInteger integer = workerIntegerMAP.get(regularWorker);
                        if (!manager & regularWorker.isShiftManager()) {
                            if (integer.intValue() < 5 && canwork) {
                                shift.addManager(regularWorker, job);
                                manager = true;
                                sum--;
                                integer.set(integer.intValue() + 1);
                                regularWorker.insertShiftRecord(date, shift);
                            }
                        } else {
                            if (integer.intValue() < 5 && canwork) {
                                if (shift.Registration_for_shift(regularWorker, job)) {
                                    sum--;
                                    integer.set(integer.intValue() + 1);
                                    regularWorker.insertShiftRecord(date, shift);
                                }
                            }
                        }
                    } else break;
                }
                if (sum != 0) {
                    miss += "Miss " + sum + " employees who can work at: " + localDate + " shift type: " + shiftType + " position :" + job + '\n';
                }
            }
            if (!manager)
                return false;
            Map<Tuple<Date, ShiftType>, Shift> loc = shiftHistory.get(getLocation(branchId));
            loc.put(shiftTypeTuple, shift);
            BussinessDataAccess.getBTD().addShift(shift, shiftTypeTuple.x);
            System.out.println("Shift: " + localDate + "  " + shiftType + " created.");
            System.out.println(miss);
            return true;
        }
        System.out.println("Shift: " + localDate + "  " + shiftType + " already created.");
        return true;
    }

    private Location getLocation(int branchId) {
        for (Location run : branches) {
            if (run.getId() == branchId)
                return run;
        }
        return null;
    }

    public void change_in_the_number_of_employees_on_shift(int i, ShiftType shiftType, Job job, int number, int activeEmployeeBrach) {
        int j = Utility.checkShift(shiftType);
        recommendedLineUp.setNewNumber(i, j, job, number, getLocation(activeEmployeeBrach));
    }

    public int get_number_of_employee_for_job_on_shift(int i, ShiftType shiftType, Job job, int activeEmployeeBrach) {
        int j = Utility.checkShift(shiftType);
        AtomicInteger output = recommendedLineUp.getNumofEmpl(job, i, j, getLocation(activeEmployeeBrach));
        if (output != null)
            return output.intValue();
        return zero;
    }

    public String get_number_of_employees_on_shift(int i, ShiftType shiftType, int branchID) {
        int j = Utility.checkShift(shiftType);
        return recommendedLineUp.get_number_of_employees_on_shift(i, j, getLocation(branchID));
    }

    public void addShift(Tuple<Date, ShiftType> typeTuple, Shift shift) {//TODO: Temporary_data_structure delete?
        //shiftHistory.putIfAbsent(typeTuple,shift);
    }

    public void upDateShift(Tuple<Date, ShiftType> shiftTuple, int addId, Job job) throws Exception {
        Shift originShift = getShift(shiftTuple, employeeController.getActiveEmployeeBrach());
        RegularWorker regularWorker = (RegularWorker) employeeController.getEmployeeById(addId);
        originShift.Registration_for_shift(regularWorker, job);
    }

    public boolean checkLocation(LocationFacade locationFacade) {
        boolean output = false;
        for (Location loc : branches) {
            if (loc.getId() == locationFacade.id) {
                output = true;
                break;
            }
        }
        return output;
    }

    public List<Driver> getDriver(LocalDate run, int i) throws Exception {
        List<Driver> output = new LinkedList<>();
        Tuple<Date, ShiftType> typeTuple = new Tuple(Utility.localToDate(run), i == 0 ? ShiftType.Day : ShiftType.Night);
        for (Map.Entry<Tuple<Date, ShiftType>, List<Integer>> tupleListEntry : transport.entrySet()) {
            if (tupleListEntry.getKey().x.equals(typeTuple.x) && tupleListEntry.getKey().y.equals(typeTuple.y)) {
                for (Integer integer : tupleListEntry.getValue())
                    output.add((Driver) employeeController.getEmployeeById(integer.intValue()));
                break;
            }
        }
        return output;
    }

    public void registerDriverToShift(DriverFacade driverFacade, DayOfWeek run, int i) {
        Tuple<Date, ShiftType> typeTuple = new Tuple(Utility.checkDay(run), i == 0 ? ShiftType.Day : ShiftType.Night);
        Employee employee = employeeController.getDriver(driverFacade);
        Driver driver = (Driver) employee;
        transport.get(typeTuple).add(driver.getId());
    }

    public void remove(Tuple<Date, ShiftType> shiftTypeTuple, int removeId) throws Exception {
        RegularWorker regularWorker = (RegularWorker) employeeController.getEmployeeById(removeId);
        Shift shift = getShift(shiftTypeTuple, employeeController.getActiveEmployeeBrach());
        shift.removeWorker(regularWorker);
    }

    public boolean hasStoreKeeper(Tuple<Date, ShiftType> shiftTuple, int branchId) {
        Shift toCheck;
        try {
            toCheck = getShift(shiftTuple, branchId);
        } catch (SQLException e) {
            return false;
        }
        return toCheck.hasStoreKeeper();
    }
}