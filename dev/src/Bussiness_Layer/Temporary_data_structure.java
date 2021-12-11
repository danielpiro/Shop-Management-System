package Bussiness_Layer;

import Bussiness_Layer.EmployeePackage.*;
import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.ShiftPackage.ShiftController;
import Bussiness_Layer.ShiftPackage.ShiftType;
import Bussiness_Layer.TransportationPackage.Location;
import Facade_Layer.FacadeObjects.SuppliesFacade;
import Facade_Layer.TransportationFacade;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Temporary_data_structure {
    private EmployeeController employeeController = EmployeeController.getInstance();
    private ShiftController shiftController = ShiftController.getInstance();

    public Temporary_data_structure() throws Exception {
        List<Job> jobListnitay = Arrays.asList(new Job[]{Job.Cashier, Job.Usher, Job.Guard, Job.StoreKeeper});
        List<Job> jobListomry = Arrays.asList(new Job[]{Job.Cashier, Job.Usher, Job.Guard, Job.StoreKeeper});
        List<Job> jobLisyben = Arrays.asList(new Job[]{Job.Cashier, Job.Usher, Job.Guard, Job.StoreKeeper});
        List<Job> jobListElad = Arrays.asList(new Job[]{Job.Cashier, Job.Usher, Job.Guard, Job.StoreKeeper});
        List<Job> jobListOlivia = Arrays.asList(new Job[]{Job.Cashier, Job.Usher, Job.Guard, Job.StoreKeeper});

        ////EXAMPLE
        boolean[][] nitayCons = {{true, true}, {true, true}, {false, false}, {false, true}, {true, true}, {false, true}, {false, true}};
        employeeController.testRegisterRegularWorker("Nitay", "Vitkin", 123456789, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 1, true, jobListnitay, randomConstraints(), this);



        Shift shift = new Shift(ShiftType.Day, 2, 0);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.of(2021, 4, 16);
        LocalDate nextweek = LocalDate.of(2021, 4, 23);
        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        Date date1 = Date.from(nextweek.atStartOfDay(defaultZoneId).toInstant());
        employeeController.testRegisterRegularWorker("Nitay", "Ornon", 1256789, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, true, jobListnitay, randomConstraints(), this);
        employeeController.testRegisterRegularWorker("BerNald", "Virkin", 98765, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, true, jobListnitay, randomConstraints(), this);
        employeeController.testRegisterRegularWorker("Omry", "Arviv", 987654321, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, true, jobListomry, randomConstraints(), this);
        employeeController.testRegisterRegularWorker("Kobe", "Arviv", 795427, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, true, jobListomry, randomConstraints(), this);
        employeeController.testregisterHRManager("Ofer", "Yanie", 999999999, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111);
        employeeController.testRegisterRegularWorker("Ben", "Arviv", 9654321, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, true, jobLisyben, randomConstraints(), this);
        employeeController.testRegisterRegularWorker("LeBron", "James", 23232323, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, true, jobLisyben, randomConstraints(), this);
        employeeController.testregisterHRManager("Amir", "Dor", 943273298, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111);
        employeeController.testregisterHRManager("Amir", "BEN", 9876543, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111);
        employeeController.testRegisterRegularWorker("Elad", "Dor", 9876321, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, true, jobListElad, randomConstraints(), this);
        employeeController.testRegisterRegularWorker("Elad", "OPL", 5555555, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, true, jobListElad, randomConstraints(), this);
        employeeController.testRegisterRegularWorker("Olivia", "Mia", 9874321, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, true, jobListOlivia, randomConstraints(), this);
        employeeController.testRegisterRegularWorker("Niv", "Mia", 66666, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111, false, jobListOlivia, randomConstraints(), this);
        employeeController.testregisterTPM("will", "Yanie", 8888, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 111);
        employeeController.testRegisterDriver("daniel", "piro", 123456, 12, 1, 1, 123, "beer sheva", 111, 12312, 111, false, new ArrayList<>(Collections.singletonList(Job.DriverLicenseD)), randomConstraints(), this);
        employeeController.testRegisterDriver("david", "loreal", 555, 12, 1, 1, 55, "beer sheva", 111, 12312, 111, false, new ArrayList<>(Collections.singletonList(Job.DriverLicenseB)), randomConstraints(), this);
        employeeController.testRegisterDriver("david", "temp", 556, 12, 1, 1, 55, "beer sheva", 111, 12312, 111, false, new ArrayList<>(Collections.singletonList(Job.DriverLicenseD)), randomConstraints(), this);
        //shiftController.createMultiShift();
        Employee employee = employeeController.getEmployeeById(9876321);
        RegularWorker regularWorker = (RegularWorker) employee;
        regularWorker.insertShiftRecord(date, shift);
        shift.Registration_for_shift(regularWorker, Job.StoreKeeper);
        employee = employeeController.getEmployeeById(123456789);
        regularWorker = (RegularWorker) employee;
        shift.addManager(regularWorker, Job.Usher);
        Tuple<Date, ShiftType> typeTuple = new Tuple<>(date, ShiftType.Day);
        Tuple<Date, ShiftType> typeTuple1 = new Tuple<>(date1, ShiftType.Day);
        regularWorker.insertShiftRecord(date, shift);
        regularWorker.insertShiftRecord(date1, shift);
        shiftController.addShift(typeTuple, shift);
        shiftController.addShift(typeTuple1, shift);

        //transportation fake data
        TransportationFacade.getInstance().addLocation(1, "haifa", "05034523623", "beni", 3);
        TransportationFacade.getInstance().addLocation(2, "eilat", "05037675443", "beni", 2);
        TransportationFacade.getInstance().addLocation(3, "beer sheva", "0525221124", "beni", 1);
        TransportationFacade.getInstance().addLocation(4, "dimona", "052532232", "beni", 1);
        TransportationFacade.getInstance().addLocation(5, "tel aviv", "0511511", "beni", 2);
        TransportationFacade.getInstance().addTruck(1, "bmv", 1.2, 3);
        TransportationFacade.getInstance().addTruck(2, "bmv", 2.2, 19);
        TransportationFacade.getInstance().addTruck(3, "bmv", 1.2, 8);
        List<SuppliesFacade> a = new ArrayList<>();
        List<SuppliesFacade> b = new ArrayList<>();
        a.add(new SuppliesFacade(1, "pita" , 2));
        a.add(new SuppliesFacade(2, "chocolate",3));
        a.add(new SuppliesFacade(3, "cornflakes",3));
        a.add(new SuppliesFacade(4, "pickles",3));
        a.add(new SuppliesFacade(5, "bread",3));
        b.add(new SuppliesFacade(6, "pasta",3));
        b.add(new SuppliesFacade(7, "Cigarettes",3));
        b.add(new SuppliesFacade(4, "pickles",3));
        TransportationFacade.getInstance().addDocument(1, 1, a, 1);
        TransportationFacade.getInstance().addDocument(2, 2, a, 2);
        TransportationFacade.getInstance().addDocument(3, 3, b, 3);
    }

    private boolean[][] randomConstraints() {
        boolean[][] constraints = new boolean[7][2];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                Random random = new Random();
                //constraints[i][j] = random.nextBoolean();
                constraints[i][j] = true;
            }
        }
        return constraints;
    }
}

