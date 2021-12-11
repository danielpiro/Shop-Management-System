package Bussiness_Layer.EmployeePackage;

import Bussiness_Layer.*;
import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.ShiftPackage.ShiftController;
import Bussiness_Layer.ShiftPackage.ShiftType;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.stream.*;

import Bussiness_Layer.TransportationPackage.Location;
import DataLayer.DalObjects.DalEmployee;
import Facade_Layer.FacadeObjects.*;


public class EmployeeController {
    private static EmployeeController single_instance = null;
    private Employee activeEmployee;
    private Map<Integer, Employee> employeeList;
    private boolean loaded;
    //private ShiftController shiftController = ShiftController.getInstance();


    private EmployeeController() {
        activeEmployee = null;
        employeeList = new HashMap<>();
        loaded = false;
    }

    public String getEmployeeList() {
        int id = activeEmployee.location;
        String output = "";
        for (Map.Entry<Integer, Employee> entry : employeeList.entrySet()) {
            if (entry.getValue().location == id) {
                output += entry.getValue().toString() + '\n';
            }
        }
        return output;
    }

    private EmployeeController(Employee employee) {
        activeEmployee = employee;
        employeeList = new HashMap<>();
    }

    public String getDriverList() {
        int id = -1;
        String output = "";

        for (Map.Entry<Integer, Employee> entry : employeeList.entrySet()) {
            if (entry.getValue().location == id) {
                output += entry.getValue().toString() + '\n';
            }
        }
        return output;
    }

    public boolean changeConstraints(boolean[][] processedConstraints) {
        return ((RegularWorker) activeEmployee).option1(processedConstraints);
    }

    public String showConstraints() {
        return ((RegularWorker) activeEmployee).showConstraints();
    }

    public boolean changeConstraints(int dayOfWeek, int shiftType, boolean constraint) {
        return ((RegularWorker) activeEmployee).option2(dayOfWeek, shiftType, constraint);
    }

    public void FacadeEmployee(FacadeEmployee subject) {
        int id = subject.getId();
        Employee employee = employeeList.get(id);
        employee.setF_Name(subject.getF_Name());
        employee.setL_Name(subject.getL_Name());
        employee.setBankInfo(subject.getBankInfo());
        employee.setWage(subject.getWage());
        employee.setAdvancedStudyFunds(subject.getAdvancedStudyFunds());
        employee.setMonthlySickDays(subject.getMonthlySickDays());
        employee.setMonthlyDayOffs(subject.getMonthlyDayOffs());
        BussinessDataAccess.getBTD().updateEmployee(employee);
    }

    public void FireEmployee(int id) throws Exception {
        employeeList.remove(id);
    }

    public int getActiveEmployeeBrach() {
        return activeEmployee.location;
    }

    public Employee getDriver(DriverFacade driverFacade) {
        return employeeList.get(driverFacade.getId());
    }

    public void registerTPManager(FacadeTPM facadeTPM) {
        TransportationManager transportationManager = new TransportationManager(facadeTPM);
        employeeList.putIfAbsent(transportationManager.id, transportationManager);
    }

    public void update(FacadeRegularWorker regularWorker, FacadeShift facadeShift, Date date) throws Exception {
        RegularWorker regularWorker1 = (RegularWorker) getEmployeeById(regularWorker.getId());
        Tuple<Date, ShiftType> typeTuple = new Tuple<>(date, facadeShift.getType());
        regularWorker1.insertShiftRecord(date, getShift(typeTuple, getActiveEmployeeBrach()));
    }

    private Shift getShift(Tuple<Date, ShiftType> typeTuple, int activeEmployeeBrach) throws SQLException {
        ShiftController shiftController = ShiftController.getInstance();
        return shiftController.getShift(typeTuple, activeEmployeeBrach);
    }


    public boolean isSManager() {
        return activeEmployee instanceof StoreManager;
    }

    private static class InstanceHolder {
        private static final EmployeeController instance = new EmployeeController();
    }

    public static EmployeeController getInstance() {
        if (single_instance == null)
            single_instance = new EmployeeController();
        return single_instance;
    }


    public boolean Login(int id) throws Exception {
        if (activeEmployee != null) {
            throw new IllegalAccessException("cannot login if a user is already connected! Error");
        }
        if(!loaded){
            List<Employee> employees = BussinessDataAccess.getBTD().loadEmployees();
            for(Employee emp : employees){
                employeeList.putIfAbsent(emp.getId(), emp);
            }
            loaded = true;
        }
        activeEmployee = getEmployeeById(id);
        return true;
    }

    public boolean Logout() throws IllegalAccessException {
        if (activeEmployee == null) {
            throw new IllegalAccessException("cannot logout if no user is connected! Error");
        }
        activeEmployee = null;
        return true;
    }

    public String empExists(int id) {
        try {
            getEmployeeById(id);
        } catch (Exception e) {
            return "Incorrect user details";
        }
        return "Exists";
    }

    public void registerRegularWorker(FacadeRegularWorker facadeRegularWorker) {
        RegularWorker newWorker = new RegularWorker(facadeRegularWorker);
        employeeList.putIfAbsent(newWorker.id, newWorker);
        BussinessDataAccess.getBTD().addRegularWorker(newWorker);
    }

    public void registerDriver(DriverFacade driverFacade) {
        Driver newWorker = new Driver(driverFacade);
        employeeList.putIfAbsent(newWorker.id, newWorker);
        BussinessDataAccess.getBTD().addDriver(newWorker);
    }

    public void registerStorageKeeper(FacadeRegularWorker facadeRegularWorker) {
        StorageKeeper newWorker = new StorageKeeper(facadeRegularWorker);
        employeeList.putIfAbsent(newWorker.id, newWorker);
        BussinessDataAccess.getBTD().addRegularWorker(newWorker);
    }

    public void testRegisterRegularWorker(String f_Name, String l_Name, int id,
                                          int wage, double monthlyDayOff, double monthlySickDays, int bankId, String branch, int accountNumber, int advancedStudyFunds, int workplace, boolean shiftManager,
                                          List<Job> certifiedWorks, boolean[][] personalConstraints, Object isLegitimate) throws Exception {

        RegularWorker newWorker = new RegularWorker(f_Name.toLowerCase(), l_Name.toLowerCase(), id, wage, monthlyDayOff, monthlySickDays, new BankInfo(bankId, branch, accountNumber), advancedStudyFunds, workplace, shiftManager, certifiedWorks, personalConstraints, isLegitimate);
        Employee checkIfHumanErr = employeeList.putIfAbsent(id, newWorker);
        if (checkIfHumanErr != null) {
            throw new Exception("Error, this Employee is already registered to the system");
        }
    }

    public void registerHRManager(FacadeHumanResourceManager facadeHumanResourceManager) throws SQLException {
        HumanResourceManager newManager = new HumanResourceManager(facadeHumanResourceManager);
        employeeList.putIfAbsent(newManager.id, newManager);
        BussinessDataAccess.getBTD().addTMP_HRM(newManager);
    }

    public boolean testregisterHRManager(String f_Name, String l_Name, int id,
                                         int wage, double monthlyDayOff, double monthlySickDays, int bankId, String branch, int accountNumber, int advancedStudyFunds, int workplace) {
        HumanResourceManager newManager = new HumanResourceManager(f_Name.toLowerCase(), l_Name.toLowerCase(), id, wage, monthlyDayOff, monthlySickDays, new BankInfo(bankId, branch, accountNumber), advancedStudyFunds, workplace);
        Employee checkIfHumanErr = employeeList.putIfAbsent(id, newManager);
        if (checkIfHumanErr != null) {
            System.out.println("Error, this Employee is already registered to the system");
            return false;
        }
        return true;
    }

    public Employee getEmployeeById(int id) throws Exception {
        Employee tryGetEmp = employeeList.get(id);
        if (tryGetEmp == null) {
            tryGetEmp = BussinessDataAccess.getBTD().getEmployee(id);
            if (tryGetEmp == null)
                throw new Exception("User is Not Found, Please try again");
            else {
               // RegularWorker regularWorker = (RegularWorker) tryGetEmp;
                employeeList.putIfAbsent(tryGetEmp.getId(), tryGetEmp);
            }
        }
        return tryGetEmp;
    }

    public boolean isHRManager() {
        return activeEmployee instanceof HumanResourceManager;
    }

    public boolean isTPManager() {
        return activeEmployee instanceof TransportationManager;
    }
    public boolean isStorageKeeper() {
        return activeEmployee instanceof StorageKeeper;
    }

    public List<Employee> getEmployeesForJob(Job job, int location) {
        return availableEmpStream(job, location).collect(Collectors.toList());
    }

    public String getEmployeesJob(Job job) {
        String output = "";
        for (Map.Entry<Integer, Employee> run : employeeList.entrySet()) {
            if (run.getValue() instanceof RegularWorker) {
                RegularWorker regularWorker = (RegularWorker) run.getValue();
                if (regularWorker.getCertifiedWorks().contains(job))
                    output += "Name: " + run.getValue().getF_Name() + " " + run.getValue().getL_Name() + " id: " + run.getValue().getId() + '\n';
            }
        }
        return output;
    }

    public String getEmployeesJob(Job job, Date date, ShiftType shiftType) {
        String output = "";
        for (Map.Entry<Integer, Employee> run : employeeList.entrySet()) {
            if (run.getValue() instanceof RegularWorker) {
                RegularWorker regularWorker = (RegularWorker) run.getValue();
                if (regularWorker.getBranch() == activeEmployee.location)
                    if (regularWorker.getCertifiedWorks().contains(job))
                        if (regularWorker.getSpecificShift(date, -1) == null)
                            if (regularWorker.checkAvailability(date.getDay(), shiftType))
                                output += "Name: " + run.getValue().getF_Name() + " " + run.getValue().getL_Name() + " id: " + run.getValue().getId() + '\n';
            }
        }
        return output;
    }

    public String getDriversWithLicense(Job job) {
        String output = "";
        for (Map.Entry<Integer, Employee> run : employeeList.entrySet()) {
            if (run.getValue() instanceof RegularWorker) {
                RegularWorker regularWorker = (RegularWorker) run.getValue();
                if (Utility.isDriver(regularWorker)) {
                    if (regularWorker.getCertifiedWorks().contains(job))
                        output += "Name: " + run.getValue().getF_Name() + " " + run.getValue().getL_Name() + " id: " + run.getValue().getId() + '\n';
                }
            }
        }
        return output;
    }

    public String getShiftManagerJob(Job job) {
        String output = "";
        for (Map.Entry<Integer, Employee> run : employeeList.entrySet()) {
            if (run.getValue() instanceof RegularWorker) {
                RegularWorker regularWorker = (RegularWorker) run.getValue();
                if (regularWorker.getCertifiedWorks().contains(job))
                    output += "Name: " + run.getValue().getF_Name() + " " + run.getValue().getL_Name() + " id: " + run.getValue().getId() + '\n';
            }
        }
        return output;
    }

    public List<Employee> jobCertifiedAvailableWorkers(Job job, DayOfWeek day, ShiftType shift, int branchId) {
        List<Employee> output = new LinkedList<>();
        for (Employee employee : getEmployeesForJob(job, activeEmployee.getLocation())) {
            if (employee instanceof RegularWorker) {
                RegularWorker regularWorker = (RegularWorker) employee;
                if (regularWorker.getBranch() == branchId) {
                    if (regularWorker.checkAvailability(Utility.checkDay(day), shift)) {
                        output.add(employee);
                    }
                }
            }
        }
        return output;
    }


    public List<Employee> jobCertifiedAvailableDrivers(Job license, DayOfWeek day, ShiftType shift) {
        List<Employee> output = new LinkedList<>();
        List<Job> licenses = new ArrayList<>();
        licenses.add(license);
        licenses = Utility.allPossibleLicense(licenses);
        for (Job lic : licenses) {
            List<Employee> driversList = getEmployeesForJob(lic, -1);
            for (Employee employee : driversList) {
                RegularWorker regularWorker = (RegularWorker) employee;
                if (regularWorker.checkAvailability(Utility.checkDay(day), shift)) {
                    output.add(employee);
                }
            }
        }
        return output;
    }

    private Stream<Employee> availableEmpStream(Job job, int location) {
        List<Employee> empList = new ArrayList<Employee>(employeeList.values());
        /*List<Employee> temp = empList.stream().filter(emp -> emp instanceof RegularWorker).collect(Collectors.toList());
        temp = temp.stream().filter(emp -> (((RegularWorker) emp).getCertifiedWorks().contains(job))).collect(Collectors.toList());
        temp = temp.stream().filter(emp -> emp.getLocation() == location).collect(Collectors.toList());
        return temp.stream();*/
        return empList.stream().filter(emp -> emp instanceof RegularWorker).filter(
                emp -> (((RegularWorker) emp).getCertifiedWorks().contains(job) && emp.getLocation() == location));
    }

    public boolean isActive() {
        return activeEmployee != null;
    }


    private String singleInputDecipher(String toProc) {
        String[] tokens = toProc.split(" ");
        return tokens[0];
    }

    public boolean testregisterTPM(String f_Name, String l_Name, int id,
                                   int wage, double monthlyDayOff, double monthlySickDays, int bankId, String branch, int accountNumber, int advancedStudyFunds, int workplace) {
        TransportationManager newManager = new TransportationManager(f_Name.toLowerCase(), l_Name.toLowerCase(), id, wage, monthlyDayOff, monthlySickDays, new BankInfo(bankId, branch, accountNumber), advancedStudyFunds, workplace);
        Employee checkIfHumanErr = employeeList.putIfAbsent(id, newManager);
        if (checkIfHumanErr != null) {
            System.out.println("Error, this Employee is already registered to the system");
            return false;
        }
        return true;
    }

    public void testRegisterDriver(String f_Name, String l_Name, int id,
                                   int wage, double monthlyDayOff, double monthlySickDays, int bankId, String branch, int accountNumber, int advancedStudyFunds, int workplace, boolean shiftManager,
                                   List<Job> certifiedWorks, boolean[][] personalConstraints, Object isLegitimate) throws Exception {

        Driver newWorker = new Driver(f_Name.toLowerCase(), l_Name.toLowerCase(), id, wage, monthlyDayOff, monthlySickDays, new BankInfo(bankId, branch, accountNumber), advancedStudyFunds, workplace, shiftManager, certifiedWorks, personalConstraints, isLegitimate);
        Employee checkIfHumanErr = employeeList.putIfAbsent(id, newWorker);
        if (checkIfHumanErr != null) {
            throw new Exception("Error, this Employee is already registered to the system");
        }
    }


    public void changeEmployeeF_Name(String name, Employee toBeChanged) throws Exception {
        if (isHRManager()) {
            toBeChanged.setF_Name(name);
        } else
            throw new Exception("You are not the manager! you cannot change personal Info. please contact the HR manager for assistance");
    }

    public void changeEmployeeL_Name(String name, Employee toBeChanged) throws Exception {
        if (isHRManager()) {
            toBeChanged.setL_Name(name);
        } else
            throw new Exception("You are not the manager! you cannot change personal Info. please contact the HR manager for assistance");
    }

    public void changeEmployeeWage(int wage, Employee toBeChanged) throws Exception {
        if (isHRManager()) {
            toBeChanged.setWage(wage);
        } else
            throw new Exception("You are not the manager! you cannot change personal Info. please contact the HR manager for assistance");
    }

    public void changeMonthlyDayOffs(double monthlyDayOffs, Employee toBeChanged) throws Exception {
        if (isHRManager()) {
            toBeChanged.setMonthlyDayOffs(monthlyDayOffs);
        } else
            throw new Exception("You are not the manager! you cannot change personal Info. please contact the HR manager for assistance");
    }

    public void changeMonthlySickDays(double monthlySickDays, Employee toBeChanged) throws Exception {
        if (isHRManager()) {
            toBeChanged.setMonthlySickDays(monthlySickDays);
        } else
            throw new Exception("You are not the manager! you cannot change personal Info. please contact the HR manager for assistance");
    }

    public void changeBankInfo(BankInfo bankInfo, Employee toBeChanged) throws Exception {
        if (isHRManager()) {
            toBeChanged.setBankInfo(bankInfo);
        } else
            throw new Exception("You are not the manager! you cannot change personal Info. please contact the HR manager for assistance");
    }

    public void changeAdvancedStudyFunds(int advancedStudyFunds, Employee toBeChanged) throws Exception {
        if (isHRManager()) {
            toBeChanged.setAdvancedStudyFunds(advancedStudyFunds);
        } else
            throw new Exception("You are not the manager! you cannot change personal Info. please contact the HR manager for assistance");
    }

    public String getCurrFName() {
        return activeEmployee.getF_Name();
    }

    public String thisWeekShifts() throws Exception {
        return activeEmployee.thisWeekShifts();
    }

    public String readWeekShifts() throws Exception {
        return activeEmployee.nextWeekShifts();
    }

    public String readWeekShifts(int id) throws Exception {
        Employee e = getEmployeeById(id);
        return e.nextWeekShifts();
    }

    public String checkthisWeekShifts(int id) throws Exception {
        Employee employee = getEmployeeById(id);
        return employee.thisWeekShifts();
    }

    public String checkreadWeekShifts(int id) throws Exception {
        Employee employee = getEmployeeById(id);
        return employee.nextWeekShifts();
    }

    public Shift getSpecificShift(Date date) throws Exception {
        return activeEmployee.getSpecificShift(date, -1);
    }

    public Shift getSpecificShift(Date date, ShiftType i) throws Exception {
        return activeEmployee.getSpecificShift(date, Utility.checkShift(i));
    }

    public void setEmployeeList(Map<Integer, Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public void registerSManager(FacadeStoreManager facadeStoreManager) {
        StoreManager newManager = new StoreManager(facadeStoreManager);
        employeeList.putIfAbsent(newManager.id, newManager);
        BussinessDataAccess.getBTD().addTMP_HRM(newManager);
    }

}
