/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package models;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ALEJANDRO
 */
public class CustomersDaoTest {
    
    public CustomersDaoTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of registerCustomerQuery method, of class CustomersDao.
     */
    @Test
    public void testRegisterCustomerQuery() {
        System.out.println("registerCustomerQuery");
        Customers customer = null;
        CustomersDao instance = new CustomersDao();
        boolean expResult = false;
        boolean result = instance.registerCustomerQuery(customer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listCustomerQuery method, of class CustomersDao.
     */
    @Test
    public void testListCustomerQuery() {
        System.out.println("listCustomerQuery");
        String value = "";
        CustomersDao instance = new CustomersDao();
        List expResult = null;
        List result = instance.listCustomerQuery(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateCustomerQuery method, of class CustomersDao.
     */
    @Test
    public void testUpdateCustomerQuery() {
        System.out.println("updateCustomerQuery");
        Customers customer = null;
        CustomersDao instance = new CustomersDao();
        boolean expResult = false;
        boolean result = instance.updateCustomerQuery(customer);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteCustomerQuery method, of class CustomersDao.
     */
    @Test
    public void testDeleteCustomerQuery() {
        System.out.println("deleteCustomerQuery");
        int id = 0;
        CustomersDao instance = new CustomersDao();
        boolean expResult = false;
        boolean result = instance.deleteCustomerQuery(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
