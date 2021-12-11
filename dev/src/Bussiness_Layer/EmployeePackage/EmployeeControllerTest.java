package Bussiness_Layer.EmployeePackage;

import Bussiness_Layer.Job;
import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.ShiftPackage.ShiftType;
/*import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeControllerTest {
    private EmployeeController employeeController = EmployeeController.getInstance();
    private List<Job> jobListnitay = Arrays.asList(new Job[]{Job.Cashier, Job.Usher});
    private List<Job> jobListomry = Arrays.asList(new Job[]{Job.Cashier, Job.StoreKeeper});
    private List<Job> jobLisyben = Arrays.asList(new Job[]{Job.StoreKeeper, Job.Guard});
    private List<Job> jobListElad = Arrays.asList(new Job[]{Job.Cashier, Job.Guard});
    private List<Job> jobListOlivia = Arrays.asList(new Job[]{Job.StoreKeeper, Job.Guard, Job.Usher});
    private BankInfo nitayBankinfo = new BankInfo(123456, "Hapoalim", 987654);

    private boolean[][] randomConstraints() {
        boolean[][] constraints = new boolean[7][2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                Random random = new Random();
                constraints[i][j] = random.nextBoolean();
            }
        }
        return constraints;
    }

    @BeforeEach
    void setEmployeeController() throws Exception {
        //employeeController.clear();
        Shift shift = new Shift(ShiftType.Day, 2, 1);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.of(2021, 4, 6);
        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        employeeController.testRegisterRegularWorker("Nitay", "Vitkin", 123456789, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 1, true, jobListnitay, randomConstraints(), this);
        employeeController.testRegisterRegularWorker("Omry", "Arviv", 987654321, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 1, true, jobListomry, randomConstraints(), this);
        employeeController.testregisterHRManager("Ofer", "Vitkin", 999999999, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 1);
        employeeController.testRegisterRegularWorker("Ben", "Arviv", 98765431, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 1, true, jobLisyben, randomConstraints(), this);
        employeeController.testregisterHRManager("Amir", "Dor", 943273298, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 1);
        employeeController.testRegisterRegularWorker("Elad", "Dor", 984321, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 1, true, jobListElad, randomConstraints(), this);
        employeeController.testRegisterRegularWorker("Olivia", "Mia", 87654321, 5000, 2.5, 1.5, 123456, "Hapoalim", 987654, 555555, 1, true, jobListOlivia, randomConstraints(), this);
        Employee employee = employeeController.getEmployeeById(87654321);
        RegularWorker regularWorker = (RegularWorker) employee;
        regularWorker.insertShiftRecord(date, shift);
        shift.Registration_for_shift(regularWorker, Job.StoreKeeper);
        employee = employeeController.getEmployeeById(123456789);
        regularWorker = (RegularWorker) employee;
        shift.addManager(regularWorker, Job.Usher);
    }

    @Test
    void Login() throws Exception {
        assertTrue(employeeController.Login(123456789));
    }

    @Test
    void Logout() throws Exception {
        employeeController.Login(123456789);
        assertTrue(employeeController.Logout());
    }

    @Test
    void DoubleLogin() {
        try {
            employeeController.Login(123456789);
            employeeController.Login(123456789);
        } catch (Exception e) {
            assertEquals("cannot login if a user is already connected! Error", e.getMessage());
        }
    }

    @Test
    void getEmployeeById() {
        try {
            Employee employee = employeeController.getEmployeeById(984321);
            assertEquals("elad", employee.f_Name);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void isHRManager1() {
        try {
            employeeController.Login(984321);
            assertTrue(employeeController.isHRManager());
        } catch (Exception e) {

        }
    }

    @Test
    void isHRManager2() {
        try {
            employeeController.Login(984321);
            assertFalse(employeeController.isHRManager());
        } catch (Exception e) {

        }
    }

    @Test
    void getEmployeesForJob() {
        List<Employee> employeeList = employeeController.getEmployeesForJob(Job.Guard, 111);
        assertTrue(employeeList.size() == 3);
    }

    @Test
    void changeEmployeeF_Name1() {
        try {
            employeeController.Login(984321);
            employeeController.changeEmployeeF_Name("BIBI", employeeController.getEmployeeById(984321));
        } catch (Exception e) {
            assertEquals("You are not the manager! you cannot change personal Info. please contact the HR manager for assistance", e.getMessage());
        }
    }

    @Test
    void changeEmployeeF_Name2() {
        try {
            employeeController.Login(999999999);
            employeeController.changeEmployeeF_Name("BIBI", employeeController.getEmployeeById(984321));
            assertEquals("BIBI", employeeController.getEmployeeById(984321).f_Name);
        } catch (Exception e) {
            //assertEquals("You are not the manager! you cannot change personal Info. please contact the HR manager for assistance",e.getMessage());
        }
    }

    @Test
    void thisWeekShifts() {
        try {
            employeeController.Login(87654321);
            String str = employeeController.thisWeekShifts();
            System.out.println(str);
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void jobCertifiedAvailableWorkers() {
        List<Employee> regularWorkers = employeeController.jobCertifiedAvailableWorkers(Job.Cashier, DayOfWeek.SUNDAY, ShiftType.Day, employeeController.getActiveEmployeeBrach());
        assertTrue(regularWorkers.size() > 0);
    }
}*/
