package Tests.Inventory;

import Bussiness_Layer.Location.StorageLocation;
import Bussiness_Layer.Location.StoreLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    private StoreLocation store1;
    private StoreLocation store2;
    private StoreLocation store3;
    private StoreLocation store4;
    private StoreLocation store5;

    private StorageLocation storage1;
    private StorageLocation storage2;
    private StorageLocation storage3;
    private StorageLocation storage4;
    private StorageLocation storage5;




    @BeforeEach
    void setUp()
    {
        store1 = new StoreLocation(1,1);
        store2 = new StoreLocation(2,2);
        store3 = new StoreLocation(1,2);
        store4 = new StoreLocation(2,1);
        store5 = new StoreLocation(1,1);

        store1.setAmount(100);
        store2.setAmount(200);
        store3.setAmount(300);
        store4.setAmount(25);
        store5.setAmount(88);

        storage1 = new StorageLocation(1,1);
        storage2 = new StorageLocation(2,2);
        storage3 = new StorageLocation(1,2);
        storage4 = new StorageLocation(2,1);
        storage5 = new StorageLocation(2,2);

        storage1.setAmount(1);
        storage2.setAmount(24);
        storage3.setAmount(44);
        storage4.setAmount(50);
        storage5.setAmount(0);


    }

    @Test
    void getLocType() {
        assertEquals("store",store1.getLocType());
        assertEquals("storage",storage3.getLocType());
    }

    @Test
    void addAmount() {
        store1.AddAmount(50);
        assertEquals(150,store1.getAmount());
        storage3.AddAmount(6);
        assertEquals(50,storage3.getAmount());
    }


    @Test
    void getAmount() {
        assertEquals(100,store1.getAmount());
        assertEquals(200,store2.getAmount());
        assertEquals(300,store3.getAmount());
        assertEquals(25,store4.getAmount());
        assertEquals(88,store5.getAmount());
        assertEquals(1,storage1.getAmount());
        assertEquals(24,storage2.getAmount());
        assertEquals(44,storage3.getAmount());
        assertEquals(50,storage4.getAmount());
        assertEquals(0,storage5.getAmount());

    }

    @Test
    void isEqual() {
        assertTrue(store1.isEqual(store5));
        assertTrue(storage2.isEqual(storage5));

        assertFalse(storage1.isEqual(store1));
        assertFalse(store2.isEqual(storage2));

        assertFalse(store1.isEqual(store2));
        assertFalse(store1.isEqual(store3));
        assertFalse(store1.isEqual(store4));

        assertFalse(storage2.isEqual(storage1));
        assertFalse(storage2.isEqual(storage3));
        assertFalse(storage2.isEqual(storage4));

    }


    @Test
    void getAisleNum() {
        assertEquals(1,store1.getAisleNum());
        assertEquals(2,store2.getAisleNum());
        assertEquals(1,store3.getAisleNum());
        assertEquals(2,store4.getAisleNum());
        assertEquals(1,store5.getAisleNum());
        assertEquals(1,storage1.getAisleNum());
        assertEquals(2,storage2.getAisleNum());
        assertEquals(1,storage3.getAisleNum());
        assertEquals(2,storage4.getAisleNum());
        assertEquals(2,storage5.getAisleNum());

    }

    @Test
    void getShelfNum() {
        assertEquals(1,store1.getShelfNum());
        assertEquals(2,store2.getShelfNum());
        assertEquals(2,store3.getShelfNum());
        assertEquals(1,store4.getShelfNum());
        assertEquals(1,store5.getShelfNum());

        assertEquals(1,storage1.getShelfNum());
        assertEquals(2,storage2.getShelfNum());
        assertEquals(2,storage3.getShelfNum());
        assertEquals(1,storage4.getShelfNum());
        assertEquals(2,storage5.getShelfNum());

    }
}