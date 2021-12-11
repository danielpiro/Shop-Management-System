package Tests.Suppliers;

import Bussiness_Layer.Supplier.Contact;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    private Contact contact;

    @BeforeEach
    void setUp() {
        contact = new Contact("Shay","0507851225","shay@test.com");
    }

    @Test
    void getContact() {
        assertEquals("Name: Shay, Phone Number: 0507851225, Email: shay@test.com",contact.getContact());
    }

    @Test
    void setPhoneNumber(){
        contact.setPhoneNumber("0507851229");
        assertEquals("0507851229", contact.getPhoneNumber());
        assertThrows(IllegalArgumentException.class,() -> contact.setPhoneNumber(""));
        assertThrows(IllegalArgumentException.class,() -> contact.setPhoneNumber("055265984"));
        assertThrows(IllegalArgumentException.class,() -> contact.setPhoneNumber("0507851a29"));
        assertThrows(IllegalArgumentException.class,() -> contact.setPhoneNumber("0807851229"));
    }

    @Test
    void setEmail(){
        contact.setEmail("shay1@test.com");
        assertEquals("shay1@test.com",contact.getEmail());
        assertThrows(IllegalArgumentException.class,()-> contact.setEmail(""));
        assertThrows(IllegalArgumentException.class,()-> contact.setEmail("sdfsdf.com"));
        assertThrows(IllegalArgumentException.class,()-> contact.setEmail("sdf@sdfs"));
        assertThrows(IllegalArgumentException.class,()-> contact.setEmail("asdasd@.com"));
    }
}