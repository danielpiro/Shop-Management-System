package Tests.Transportation;


import Facade_Layer.TransportationFacade;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TransportationFacadeTest {

    @Test
    void addTruck() {
        try {
            TransportationFacade.getInstance().addTruck(11121, "audi", 12.3, 15);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @Test
    void getTruck() {
        try {
            assertEquals(TransportationFacade.getInstance().getTruck(11121).getValue().model, "audi");
            assertEquals(TransportationFacade.getInstance().getTruck(11121).getValue().maxWeight, 15);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @Test
    void removeTruck() {
        try {
            TransportationFacade.getInstance().removeTruck(1112);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    void addLocation() {
        try {
            TransportationFacade.getInstance().addLocation(1122, "haifa1", "051515151", "itzik", 2);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @Test
    void getLocation() {
        try {
            assertEquals(TransportationFacade.getInstance().getLocation(1122).getValue().contactName, "itzik");
            assertEquals(TransportationFacade.getInstance().getLocation(1122).getValue().phone, "051515151");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @Test
    void removeLocation() {
        try {
            TransportationFacade.getInstance().removeLocation(1122);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}