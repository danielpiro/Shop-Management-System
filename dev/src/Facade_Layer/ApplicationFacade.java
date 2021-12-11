package Facade_Layer;

import Bussiness_Layer.EmployeePackage.*;
import Bussiness_Layer.Job;
import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.ShiftPackage.ShiftController;
import Bussiness_Layer.ShiftPackage.ShiftType;
import Bussiness_Layer.TransportationPackage.Delivery;
import Bussiness_Layer.TransportationPackage.Location;
import Bussiness_Layer.Tuple;
import Bussiness_Layer.Utility;
import DataLayer.DalObjects.DBController.DalController;
import Facade_Layer.FacadeObjects.*;

import java.io.BufferedReader;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationFacade {
    private final int driverBranch = -1;
    private static ApplicationFacade applicationFacade = null;
    private EmployeeController employeeController;
    private ShiftController shiftController;

    public ApplicationFacade() throws SQLException {
        employeeController = EmployeeController.getInstance();
        shiftController = ShiftController.getInstance();
    }

    public static ApplicationFacade getInstance() throws SQLException {
        if (applicationFacade == null)
            applicationFacade = new ApplicationFacade();
        return applicationFacade;
    }

    public boolean registerSManager(String f_Name, String l_Name, int id,
                                    int wage, double monthlyDayOff, double monthlySickDays, int bankId, String branch, int accountNumber, int advancedStudyFunds) throws SQLException {
        FacadeStoreManager facadeHumanResourceManager = new FacadeStoreManager(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, new BankInfo(bankId, branch, accountNumber), advancedStudyFunds, employeeController.getActiveEmployeeBrach());
        employeeController.registerSManager(facadeHumanResourceManager);
        return true;
    }

    public void mainMenu() {
    }

    public boolean createShift() {
        try {
            if (employeeController.isHRManager())
                return shiftController.createMultiShift(employeeController.getActiveEmployeeBrach());
            else return false;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public String getCurrShift() {
        Shift output = null;
        if (output != null)
            return output.toString();
        return "No such Shift found";
    }

    public Response getShift(Date date, ShiftType shiftType) throws SQLException {
        Tuple<Date, ShiftType> shiftTypeTuple = new Tuple<>(date, shiftType);
        FacadeShift facadeShift = new FacadeShift(shiftController.getShift(shiftTypeTuple, employeeController.getActiveEmployeeBrach()));
        if (facadeShift == null)
            return new Response("No such Shift found\n");
        return new Response();
    }

    public Response getEmployeesForJob(String input) {
        return new Response(employeeController.getEmployeesJob(Utility.parseJob(input)));
    }

    public Response getEmployeesForJob(Job input, Date date, ShiftType shiftType) {
        return new Response(employeeController.getEmployeesJob(input, date, shiftType));
    }

    public void requestEmpForDelivery(Job license, List<Location> location, int day, int type) {
        ShiftType sType = (type == 0) ? ShiftType.Day : ShiftType.Night;
        shiftController.requestForDelivery(license, location, day, sType);
    }

    private ResponseT<List<Driver>> getDriversAvailable(LocalDate day, int shift) {
        try {
            return new ResponseT<>(shiftController.getDriver(day, shift));
        }
        catch (Exception e){
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response upDateShift(Date date, ShiftType shiftType, int upDateShiftOp) throws SQLException {
        Tuple<Date, ShiftType> shiftTypeTuple = new Tuple<>(date, shiftType);
        FacadeShift facadeShift = new FacadeShift(shiftController.getShift(shiftTypeTuple, employeeController.getActiveEmployeeBrach()));
        return new ResponseT(facadeShift.getRegularWorkerList());
    }

    private int upDateShiftOp(BufferedReader scanner) {
        System.out.println("What Do you want to change? \n 1) remove worker \n 2) add worker\n 3) change shiftManager");
        try {
            int input = Integer.parseInt(scanner.readLine());
            while (input < 1 | input > 3) {
                System.out.println("out of bounds.\nWhat Do you want to change? \n 1) remove worker \n 2) add worker\n 3) change shiftManager");
                input = Integer.parseInt(scanner.readLine());
            }
            return input;
        } catch (Exception e) {
            System.out.println("Invalid input, please try again");
            return upDateShiftOp(scanner);
        }
    }

    public boolean registerRegWorker(String f_Name, String l_Name, int id,
                                     int wage, double monthlyDayOff, double monthlySickDays, int bankId, String branch, int accountNumber, int advancedStudyFunds, boolean shiftManager,
                                     List<Job> certifiedWorks) throws SQLException {
        FacadeRegularWorker facadeRegularWorker = new FacadeRegularWorker(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, new BankInfo(bankId, branch, accountNumber), advancedStudyFunds, employeeController.getActiveEmployeeBrach(), shiftManager, certifiedWorks);
        employeeController.registerRegularWorker(facadeRegularWorker);
        return true;
    }

    public boolean registerHRManager(String f_Name, String l_Name, int id,
                                     int wage, double monthlyDayOff, double monthlySickDays, int bankId, String branch, int accountNumber, int advancedStudyFunds, int location) throws SQLException {
        FacadeHumanResourceManager facadeHumanResourceManager = new FacadeHumanResourceManager(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, new BankInfo(bankId, branch, accountNumber), advancedStudyFunds, location);
        employeeController.registerHRManager(facadeHumanResourceManager);
        return true;
    }


    public void change_in_the_number_of_employees_on_shift(int dayOfWeek, ShiftType shiftType, Job job, int number) {
        shiftController.change_in_the_number_of_employees_on_shift(dayOfWeek, shiftType, job, number, employeeController.getActiveEmployeeBrach());
    }

    public int get_number_of_employee_for_job_on_shift(int i, ShiftType shiftType, Job job) {
        return shiftController.get_number_of_employee_for_job_on_shift(i, shiftType, job, employeeController.getActiveEmployeeBrach());
    }

    public String get_number_of_employees_on_shift(int i, ShiftType shiftType) {
        return shiftController.get_number_of_employees_on_shift(i, shiftType, employeeController.getActiveEmployeeBrach());
    }


    ////Employee package
    public Response Login(int id) {
        try {
            employeeController.Login(id);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response isActive() {
        if (employeeController.isActive())
            return new Response();
        else return new Response("You Are Not LogIn!");
    }


    public boolean isHRManager() {
        return employeeController.isHRManager();
    }

    public boolean isTPManager() {
        return employeeController.isTPManager();
    }

    public String getFName() {
        return employeeController.getCurrFName();
    }

    public String empExists(int id) {
        return employeeController.empExists(id);
    }

    public String thisWeekShifts() throws Exception {
        return employeeController.thisWeekShifts();
    }

    public String nextWeekShifts() throws Exception {
        return employeeController.readWeekShifts();
    }

    public String nextWeekShifts(int id) throws Exception {
        return employeeController.readWeekShifts(id);
    }

    public Response getSpecificShift(Date date) throws Exception {
        if (employeeController.getSpecificShift(date) != null)
            return new ResponseT(new FacadeShift(employeeController.getSpecificShift(date)));
        else return new Response();
    }

    public Response getSpecificShift(Date date, ShiftType shiftType) throws Exception {
        if (employeeController.getSpecificShift(date, shiftType) != null)
            return new ResponseT(new FacadeShift(employeeController.getSpecificShift(date, shiftType)));
        else return new Response();
    }

    public void logout() {
        try {
            employeeController.Logout();
        } catch (IllegalAccessException e) {
            //Logger.log("logout function called without a user being logged in. critical." + e.getMessage())
        }
    }

    public Response changeConstraints(boolean[][] processedConstraints) {
        if (employeeController.changeConstraints(processedConstraints))
            return new Response();
        else return new Response("error occurred.");
    }

    public String getDriverList() {
        return employeeController.getDriverList();
    }

    public String showConstraints() {
        return employeeController.showConstraints();
    }

    public String getEmployeelist() {
        return employeeController.getEmployeeList();
    }

    public Response changeConstraints(int dayOfWeek, int shiftType, boolean constraint) {
        if (employeeController.changeConstraints(dayOfWeek, shiftType, constraint)) {
            return new Response();
        } else return new Response("error occurred.");
    }

    public FacadeEmployee getEmployeeById(int id) throws Exception { //todo: check if we could avoid line 215
        FacadeRegularWorker facadeRegularWorker;
        FacadeHumanResourceManager facadeHumanResourceManager;
        Employee e = employeeController.getEmployeeById(id);
        if (e instanceof RegularWorker) {
            facadeRegularWorker = new FacadeRegularWorker((RegularWorker) e);
            return facadeRegularWorker;
        } else {
            facadeHumanResourceManager = new FacadeHumanResourceManager((HumanResourceManager) e);
            return facadeHumanResourceManager;
        }
    }

    public void changeEmployeeF_Name(String singleInputDecipher, FacadeEmployee subject) {
        subject.setF_Name(singleInputDecipher);
        employeeController.FacadeEmployee(subject);
    }

    public void changeEmployeeL_Name(String singleInputDecipher, FacadeEmployee subject) {
        subject.setL_Name(singleInputDecipher);
        employeeController.FacadeEmployee(subject);
    }

    public void changeEmployeeWage(int wage, FacadeEmployee subject) {
        subject.setWage(wage);
        employeeController.FacadeEmployee(subject);
    }

    public void changeMonthlyDayOffs(Double monthlyDaysOff, FacadeEmployee subject) {
        subject.setMonthlyDayOffs(monthlyDaysOff);
        employeeController.FacadeEmployee(subject);
    }

    public void changeMonthlySickDays(Double monthlySickDays, FacadeEmployee subject) {
        subject.setMonthlySickDays(monthlySickDays);
        employeeController.FacadeEmployee(subject);
    }

    public void changeBankInfo(BankInfo bankInfo, FacadeEmployee subject) {
        subject.setBankInfo(bankInfo);
        employeeController.FacadeEmployee(subject);
    }

    public void changeAdvancedStudyFunds(int newFundsAmount, FacadeEmployee subject) {
        subject.setAdvancedStudyFunds(newFundsAmount);
        employeeController.FacadeEmployee(subject);
    }

    public Response remove(Date date, ShiftType shiftType, int upDateShiftOp) throws SQLException {
        Tuple<Date, ShiftType> shiftTypeTuple = new Tuple<>(date, shiftType);
        FacadeShift shift = new FacadeShift(shiftController.getShift(shiftTypeTuple, employeeController.getActiveEmployeeBrach()));
        try {
            shiftController.remove(shiftTypeTuple, upDateShiftOp);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response add(Date date, ShiftType shiftType, int add, Job input) throws SQLException {
        Tuple<Date, ShiftType> shiftTypeTuple = new Tuple<>(date, shiftType);
        FacadeShift shift = new FacadeShift(shiftController.getShift(shiftTypeTuple, employeeController.getActiveEmployeeBrach()));
        try {
            FacadeRegularWorker regularWorker = new FacadeRegularWorker((RegularWorker) employeeController.getEmployeeById(add));
            if (regularWorker.checkAvailability(date.getDay(), shiftType)) {
                if (shift.Registration_for_shift(regularWorker, input)) {
                    shiftController.upDateShift(shiftTypeTuple, regularWorker.getId(), input);
                    regularWorker.insertShiftRecord(date, shift);
                    employeeController.update(regularWorker, shift, date); //todo: update Employee
                    return new Response();
                } else return new Response("Can not remove shiftManager. Try to replace instead.");
            } else return new Response("The employee you selected is not available for the shift you are editing");
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response getShiftManager(Date date, ShiftType shiftType) throws SQLException {
        Tuple<Date, ShiftType> shiftTypeTuple = new Tuple<>(date, shiftType);
        FacadeShift shift = new FacadeShift(shiftController.getShift(shiftTypeTuple, employeeController.getActiveEmployeeBrach()));
        Job job = shift.getJob(shift.getOtherShiftManager());
        return new Response(employeeController.getShiftManagerJob(job));
    }

    public Response replce(Date date, ShiftType shiftType, int id) throws SQLException {
        Tuple<Date, ShiftType> shiftTypeTuple = new Tuple<>(date, shiftType);
        FacadeShift shift = new FacadeShift(shiftController.getShift(shiftTypeTuple, employeeController.getActiveEmployeeBrach()));
        try {
            FacadeRegularWorker regularWorker = new FacadeRegularWorker((RegularWorker) employeeController.getEmployeeById(id));
            if (regularWorker.checkAvailability(date.getDay(), shiftType)) {
                shift.addManager(regularWorker, shift.getJob(shift.getOtherShiftManager()));
                regularWorker.insertShiftRecord(date, shift);
                return new Response();
            } else return new Response("The employee you selected is not available for the shift you are editing");
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response FireEmployee(int id) throws Exception {
        if (nextWeekShifts(id).equals("Shifts haven't been made yet, please wait until Thursday.")) {
            employeeController.FireEmployee((id));
            return new Response();
        }
        return new Response("Cannot fire employee. register to up coming shifts");
    }

    public boolean checkthisweekShift(int id) {
        try {
            if (employeeController.checkthisWeekShifts(id).equals("No shifts this week."))
                return true;
            else return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checknextweekShift(int id) {
        try {
            if (employeeController.checkreadWeekShifts(id).equals("No shifts this week."))
                return true;
            else return false;
        } catch (Exception e) {
            return false;
        }
    }

    public Response checkLocation(LocationFacade locationFacade) {
        if (shiftController.checkLocation(locationFacade)) {
            return new Response();
        } else return new Response("The given branch does not exist");
    }

    public boolean registerDriver(String fName, String lName, int id, int wage, Double monthlyDaysOff, Double monthlySickDays, int bankId, String branch, int accNumb, int advStudyFunds, boolean strIsMNG,List<Job> job) {
        DriverFacade driverFacade = new DriverFacade(fName, lName, id, wage, monthlyDaysOff, monthlySickDays, new BankInfo(bankId, branch, accNumb), advStudyFunds, driverBranch , strIsMNG, job);
        employeeController.registerDriver(driverFacade);
        return true;
    }

    public void registerTPManager(String f_Name, String l_Name, int id,
                                  int wage, double monthlyDayOff, double monthlySickDays, int bankId, String branch, int accountNumber, int advancedStudyFunds) {
        FacadeTPM facadeTPM = new FacadeTPM(f_Name, l_Name, id, wage, monthlyDayOff, monthlySickDays, new BankInfo(bankId, branch, accountNumber), advancedStudyFunds, employeeController.getActiveEmployeeBrach());
        employeeController.registerTPManager(facadeTPM);
    }

    public boolean registerStorageKeeper(String f_Name, String l_Name, int id, int wage, Double monthlyDaysOff, Double monthlySickDays, int bankId, String branch, int accNumb, int advStudyFunds, boolean shiftManager,List<Job> job) {
        FacadeRegularWorker facadeRegularWorker = new FacadeRegularWorker(f_Name, l_Name, id, wage, monthlyDaysOff, monthlySickDays, new BankInfo(bankId, branch, accNumb), advStudyFunds, employeeController.getActiveEmployeeBrach(), shiftManager, job);
        employeeController.registerStorageKeeper(facadeRegularWorker);
        return true;
    }


    public List<Driver> getDriversInShift(LocalDate run, int i) {
        try {
            List<Driver> toRet = shiftController.getDriver(run, i);
            if (toRet == null) {
                return new ArrayList<>();
            }
            return toRet;
        }
        catch (Exception e){
            return null;
        }
    }

    public boolean hasStoreKeeper(Tuple<Date, ShiftType> shiftTuple, int branchId){
        return shiftController.hasStoreKeeper(shiftTuple, branchId);
    }

    public boolean isStorageKeeper() {
        return employeeController.isStorageKeeper();
    }

    public boolean isSManager() { return employeeController.isSManager();}

    public int getLocation() {
        return employeeController.getActiveEmployeeBrach();
    }

    public void cancelRequest(int delID) throws SQLException {
        DalController.getDalController().setDeletePermissionHR(delID);
    }
}
