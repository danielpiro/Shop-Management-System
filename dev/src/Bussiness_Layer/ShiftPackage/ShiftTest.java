package Bussiness_Layer.ShiftPackage;

import Bussiness_Layer.EmployeePackage.BankInfo;
import Bussiness_Layer.EmployeePackage.RegularWorker;
import Bussiness_Layer.Job;

import Bussiness_Layer.TransportationPackage.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShiftTest {
    private Shift shift;
    private RegularWorker regularWorker;
    private BankInfo bankInfo;
    private List<Job> jobs;

    @BeforeEach
    void setShift() {
        bankInfo = new BankInfo(666, "Hapoalim", 19887);
        jobs = new LinkedList<>();
        jobs.add(Job.Guard);
        regularWorker = new RegularWorker("nitay", "vitkin", 123456789, 5000, 2, 3, bankInfo, 987654, 0, true, jobs);
        shift = new Shift(ShiftType.Day, 0, 1);
    }

    @Test
    void Registration_for_shift() {
        shift.Registration_for_shift(regularWorker, Job.Guard);
        List<Integer> list = shift.getRegularWorkerList();
        assertTrue(list.contains(regularWorker));
    }

    @Test
    void getJob() {
        shift.Registration_for_shift(regularWorker, Job.Guard);
        assertEquals(Job.Guard, shift.getJob(regularWorker));
    }

    @Test
    void remove() {
        shift.Registration_for_shift(regularWorker, Job.Guard);
        List<Integer> list = shift.getRegularWorkerList();
        assertTrue(list.contains(regularWorker));
        shift.removeWorker(regularWorker);
        list = shift.getRegularWorkerList();
        assertFalse(list.contains(regularWorker));
    }

    @Test
    void getShiftManager() {
        shift.addManager(regularWorker, Job.Guard);
        assertEquals(regularWorker, shift.getShiftManager());
    }
}
