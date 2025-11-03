package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import impl.Factory;
import interfaces.IVendingMachineProduct;
import interfaces.IVendingMachine;
import interfaces.IProductRecord;

/**
 * This is a JUnit test class for the Vending Machine.
 */
public class Tests {

    /**
     * This checks that the factory was able to call a sensible constructor to get a non-null instance of IVendingMachineProduct.
     */
   @Test
    public void vendingMachineProductNotNull() {
        IVendingMachineProduct vendingMachineProduct = Factory.getInstance().makeVendingMachineProduct("A1", "Haggis Crisps");
        assertNotNull(vendingMachineProduct);
    }

    @Test
    public void productRecordInitialValues() {
        // Create a new product using the factory
        IVendingMachineProduct product = Factory.getInstance().makeVendingMachineProduct("B1", "Coca Cola");
        
        // Create a new product record for the product
        IProductRecord record = Factory.getInstance().makeProductRecord(product);
        
        // Check that the record is not null
        assertNotNull(record);
        
        // Check that the product stored in the record matches the product we created
        assertEquals(product, record.getProduct());
        
        // Check that the initial number of items available is 0
        assertEquals(0, record.getNumberAvailable());
        
        // Check that the initial number of sales is 0
        assertEquals(0, record.getNumberOfSales());
    }

    @Test
    public void addAndBuyItemUpdatesCounts() throws Exception {
        // Create a new product
        IVendingMachineProduct product = Factory.getInstance().makeVendingMachineProduct("C1", "Sprite");
        
        // Create a product record for the product
        IProductRecord record = Factory.getInstance().makeProductRecord(product);
        
        // Add one item to the product record
        record.addItem();
        
        // Check that the number of items available is now 1
        assertEquals(1, record.getNumberAvailable());
        
        // Buy one item
        record.buyItem();
        
        // Check that the number of items available decreased to 0
        assertEquals(0, record.getNumberAvailable());
        
        // CHeck that the number of sales increased to 1
        assertEquals(1, record.getNumberOfSales());
    }

    @Test
    public void buyItemThrowsWhenUnavailable() {
        // Create a new product
        IVendingMachineProduct product = Factory.getInstance().makeVendingMachineProduct("D1", "Doritos");
        
        // Create a product record for the product
        IProductRecord record = Factory.getInstance().makeProductRecord(product);
        
        // Attempt to buy an item when 0 are available
        // Should throw ProductUnavailableException?
        assertThrows(Exception.class, record::buyItem); // ProductUnavailableException
    }

    @Test
    public void itemPermenanceCheck() {
        // Attempt to buy an item already added in a previous test
        // looks like each test is a new application instance? 
        
        // Create a new product and vending machine
        IVendingMachineProduct product = Factory.getInstance().makeVendingMachineProduct("J4", "Oranje Juice");
        IVendingMachine machine = Factory.getInstance().makeVendingMachine();
        
        // Register the product and add items
        machine.registerProduct(product);
        machine.addItem("J1");
        machine.addItem("J1");
        
        // Verify that the number of items is correctly tracked
        assertEquals(2, machine.getNumberOfItems("J1"));
        
        // Buy one item
        machine.buyItem("J1");
        
        // Verify that the remaining items reflect the purchase
        assertEquals(1, machine.getNumberOfItems("J1"));
        
        // Buy another item
        machine.buyItem("J1");
        
        // Now the stock should be 0
        assertEquals(0, machine.getNumberOfItems("J1"));
        
        // Buying again should throw exception
        assertThrows(Exception.class, () -> machine.buyItem("J1"));
    }

}
