package Tests.Employee;

import Bussiness_Layer.EmployeePackage.Employee;
import Bussiness_Layer.EmployeePackage.EmployeeController;
import Bussiness_Layer.EmployeePackage.RegularWorker;
import Bussiness_Layer.Job;

import static org.junit.jupiter.api.Assertions.*;

import Bussiness_Layer.ShiftPackage.Shift;
import Bussiness_Layer.ShiftPackage.ShiftController;
import Bussiness_Layer.ShiftPackage.ShiftType;
import Bussiness_Layer.Temporary_data_structure;
import Bussiness_Layer.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ShiftControllerTest {
    private EmployeeController employeeController = EmployeeController.getInstance();
    private ShiftController shiftController = ShiftController.getInstance();
   // private Temporary_data_structure temporary_data_structure = new Temporary_data_structure();

    public ShiftControllerTest() throws Exception {
    }


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
    void setShiftController() throws Exception {
    }

    @Test
    void change_in_the_number_of_employees_on_shift() {
        int branchId = employeeController.getActiveEmployeeBrach();
        int num = shiftController.get_number_of_employee_for_job_on_shift(0, ShiftType.Day, Job.Guard, branchId);
        shiftController.change_in_the_number_of_employees_on_shift(0, ShiftType.Day, Job.Guard, num - 1, branchId);
        assertEquals(num - 1, shiftController.get_number_of_employee_for_job_on_shift(0, ShiftType.Day, Job.Guard, employeeController.getActiveEmployeeBrach()));
    }

    @Test
    void createMultiShift() {
        try {
            shiftController.createMultiShift(1);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getShift() throws ParseException, SQLException {
        String str = "13/05/2021";
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(str);
        Tuple<Date, ShiftType> typeTuple = new Tuple<>(date, ShiftType.Day);
        assertTrue(shiftController.getShift(typeTuple, 1) != null);
    }

    @Test
    void removeShift() throws Exception {
        Employee employee = employeeController.getEmployeeById(123456789);
        RegularWorker regularWorker = (RegularWorker) employee;
        String str = "13/05/2021";
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(str);
        Tuple<Date, ShiftType> typeTuple = new Tuple<>(date, ShiftType.Day);
        Shift shift = shiftController.getShift(typeTuple, employee.getBranch());
        regularWorker.removeShift(shift);
    }
}
