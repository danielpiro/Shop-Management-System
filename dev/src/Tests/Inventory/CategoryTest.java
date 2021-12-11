package Tests.Inventory;

import Bussiness_Layer.Category.Category;
import Bussiness_Layer.Supplier.BillsOfQuantities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category father1;
    private Category father2;
    private Category father3;

    private Category f1_sub;
    private Category f2_sub;
    private Category f3_sub;

    @BeforeEach
    void setUp() {
        father1 = new Category("Foods");
        father2 = new Category("Home Products");
        father3 = new Category("Electricity Products");

        f1_sub = new Category("Fruits");
        f2_sub = new Category("Garden Products");
        f3_sub = new Category("TV");
    }

    @Test
    void get_fatherCat() {
        assertEquals(null,father1.get_fatherCat());
        assertEquals(null,father2.get_fatherCat());
        assertEquals(null,father3.get_fatherCat());
    }

    @Test
    void set_fatherCat() {
        assertEquals(null,f1_sub.get_fatherCat());
        f1_sub.set_fatherCat(father1);
        assertEquals(father1,f1_sub.get_fatherCat());

        assertEquals(null,f2_sub.get_fatherCat());
        f2_sub.set_fatherCat(father2);
        assertEquals(father2,f2_sub.get_fatherCat());

        assertEquals(null,f3_sub.get_fatherCat());
        f3_sub.set_fatherCat(father3);
        assertEquals(father3,f3_sub.get_fatherCat());

    }

    @Test
    void get_sonCat() {
        assertEquals(0,father1.get_sonCat().size());
        assertEquals(0,father2.get_sonCat().size());
        assertEquals(0,father2.get_sonCat().size());

        father1.AddNewSonCategory(f1_sub);
        father1.AddNewSonCategory("Sweets");
        father2.AddNewSonCategory(f2_sub);
        assertEquals(2,father1.get_sonCat().size());
        assertEquals(1,father2.get_sonCat().size());

    }

    @Test
    void compare() {
        assertFalse(father1.Compare(father2));
        Category test = new Category("Foods");
        assertTrue(father1.Compare(test));
        father2.AddNewSonCategory(test);
        assertTrue(father1.Compare(test));
    }

    @Test
    void getCatName() {
        assertEquals("Foods",father1.getCatName());
        assertEquals("Home Products",father2.getCatName());
        assertEquals("Electricity Products",father3.getCatName());
        assertEquals("Fruits",f1_sub.getCatName());
        assertEquals("Garden Products",f2_sub.getCatName());
        assertEquals("TV",f3_sub.getCatName());

    }

    @Test
    void updateCategoryName() {
        father1.UpdateCategoryName("Bill");
        assertEquals("Bills",father1.getCatName());
    }


    @Test
    void findSon() {
        assertEquals(null,father1.findSon("Fruits"));
        father1.AddNewSonCategory(f1_sub);
        assertEquals(f1_sub,father1.findSon("Fruits"));
    }


    @Test
    void getFatherName() {
        assertEquals(null,f1_sub.getFatherName());
        father1.AddNewSonCategory(f1_sub);
        assertEquals("Foods",f1_sub.getFatherName());
    }

}