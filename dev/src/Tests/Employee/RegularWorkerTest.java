package Tests.Employee;

import Bussiness_Layer.EmployeePackage.BankInfo;
import Bussiness_Layer.EmployeePackage.RegularWorker;
import Bussiness_Layer.Job;
import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.ShiftPackage.ShiftType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class RegularWorkerTest {

    private RegularWorker regularWorker;
    private BankInfo bankInfo;
    private List<Job> jobs;
    private Shift shift;
    private Date date;

    @BeforeEach
    void setRegularWorker() {
        date = new Date(1 / 5 / 20);
        shift = new Shift(ShiftType.Day, 2, regularWorker.getBranch());
        bankInfo = new BankInfo(666, "Hapoalim", 19887);
        jobs = new LinkedList<>();
        jobs.add(Job.Guard);
        regularWorker = new RegularWorker("nitay", "vitkin", 123456789, 5000, 2, 3, bankInfo, 987654, 0, true, jobs);
    }

    @Test
    void checkAvailability() {
        regularWorker.option2(0, 0, false);
        assertFalse(regularWorker.checkAvailability(DayOfWeek.SUNDAY.getValue(), ShiftType.Day));
        regularWorker.option2(0, 0, true);
        assertTrue(regularWorker.checkAvailability(DayOfWeek.SUNDAY.getValue(), ShiftType.Day));
    }

    @Test
    void isShiftManager() {
        assertTrue(regularWorker.isShiftManager());
    }

    @Test
    void insertShiftRecord() {
        regularWorker.insertShiftRecord(date, shift);
        assertTrue(regularWorker.checkIfOccupiedInDate(date));
    }

    @Test
    void getCertifiedWorks() {
        assertEquals(jobs, regularWorker.getCertifiedWorks());
    }
}
