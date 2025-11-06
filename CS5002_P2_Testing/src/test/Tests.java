package test;
import org.junit.jupiter.api.Test;
import exceptions.ProductUnavailableException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import impl.Factory;
import impl.ProductRecord;
import interfaces.IVendingMachineProduct;
import interfaces.IVendingMachine;
import interfaces.IProductRecord;

/**
 * This is a JUnit test class for the Vending Machine.
 */
public class Tests {

    /**
     * This checks that the factory was able to call a constructor to get a non-null instance of IVendingMachineProduct.
     */
   @Test
    public void vendingMachineProductNotNull() {
        IVendingMachineProduct vendingMachineProduct = Factory.getInstance().makeVendingMachineProduct("A1", "Haggis Crisps");
        assertNotNull(vendingMachineProduct);
    }

    // FACTORY TESTS
    @Test
    public void factoryCreatesVendingMachine() {
        IVendingMachine machine = Factory.getInstance().makeVendingMachine();
        assertNotNull(machine);
    }

    @Test
    public void factoryCreatesVendingMachineProduct() {
        IVendingMachineProduct product = Factory.getInstance().makeVendingMachineProduct("G1", "Lays Chips");
        assertNotNull(product);
    }

    // VENDING MACHINE TESTS
    @Test
    public void vendingMachineNotNull() {
        IVendingMachine machine = Factory.getInstance().makeVendingMachine();
        assertNotNull(machine);
    }

    @Test
    public void vendingMachineInitialProductCountIsZero() {
        IVendingMachine machine = Factory.getInstance().makeVendingMachine();
        assertEquals(0, machine.getNumberOfProducts());
    }

    // PRODUCT RECORD CREATION TESTS
    @Test
    public void productRecordInitialValues() {
        IVendingMachineProduct product = Factory.getInstance().makeVendingMachineProduct("B1", "Coca Cola");
        IProductRecord record = Factory.getInstance().makeProductRecord(product);

        assertNotNull(record);
        assertEquals(product, record.getProduct());
        assertEquals(0, record.getNumberAvailable());
        assertEquals(0, record.getNumberOfSales());
    }

    // PRODUCT RECORD UPDATE TESTS
    @Test
    public void addAndBuyItemUpdatesCounts() throws Exception {
        IVendingMachineProduct product = Factory.getInstance().makeVendingMachineProduct("C1", "Sprite");
        IProductRecord record = Factory.getInstance().makeProductRecord(product);

        record.addItem();
        assertEquals(1, record.getNumberAvailable());

        record.buyItem();
        assertEquals(0, record.getNumberAvailable());
        assertEquals(1, record.getNumberOfSales());
    }

    @Test
    public void multipleAddItemsIncreasesCountCorrectly() throws Exception {
        IVendingMachineProduct product = Factory.getInstance().makeVendingMachineProduct("E1", "Mars Bar");
        IProductRecord record = Factory.getInstance().makeProductRecord(product);

        record.addItem();
        record.addItem();
        record.addItem();

        assertEquals(3, record.getNumberAvailable());
        assertEquals(0, record.getNumberOfSales());
    }

    @Test
    public void multiplePurchasesUpdateCountsProperly() throws Exception {
        IVendingMachineProduct product = Factory.getInstance().makeVendingMachineProduct("F1", "Pepsi");
        IProductRecord record = Factory.getInstance().makeProductRecord(product);

        for (int i = 0; i < 5; i++) record.addItem();
        for (int i = 0; i < 3; i++) record.buyItem();

        assertEquals(2, record.getNumberAvailable());
        assertEquals(3, record.getNumberOfSales());
    }

    // ERROR HANDLING TESTS
    @Test
    public void buyItemThrowsWhenUnavailable() {
        IVendingMachineProduct product = Factory.getInstance().makeVendingMachineProduct("D1", "Doritos");
        IProductRecord record = Factory.getInstance().makeProductRecord(product);

        assertThrows(Exception.class, record::buyItem); // ProductUnavailableException expected
    }

    //OVERALL FUNCTIONALITY TESTS
    @Test
    public void overallFunctionality() throws Exception {
        ProductRecord.getAvailableProducts().clear(); 

        // Create a vending machine
        IVendingMachine machine = Factory.getInstance().makeVendingMachine();
        assertNotNull(machine);

        // Create several products
        IVendingMachineProduct cola = Factory.getInstance().makeVendingMachineProduct("A1", "Coca Cola");
        IVendingMachineProduct crisps = Factory.getInstance().makeVendingMachineProduct("A2", "Haggis Crisps");
        IVendingMachineProduct mars = Factory.getInstance().makeVendingMachineProduct("A3", "Bero non-Alcoholic Beer");

        // Create product records and add them to the machine
        IProductRecord colaRecord = Factory.getInstance().makeProductRecord(cola);
        IProductRecord crispsRecord = Factory.getInstance().makeProductRecord(crisps);
        IProductRecord marsRecord = Factory.getInstance().makeProductRecord(mars);

        // Register products in the machine
        machine.registerProduct(cola);
        machine.registerProduct(crisps);
        machine.registerProduct(mars);

        // Check that 3 products are registered
        assertEquals(3, machine.getNumberOfProducts());

        // Check that 3 product names were added to the ProductRecord list
        ProductRecord.getAvailableProducts().forEach(System.out::println);
        assertEquals(3, ProductRecord.getAvailableProducts().size());
        assertTrue(ProductRecord.getAvailableProducts().containsAll(
            List.of("Coca Cola", "Haggis Crisps", "Bero non-Alcoholic Beer")
        ));

        // Stock items using a loop
        for (int i = 0; i < 5; i++) colaRecord.addItem();
        for (int i = 0; i < 3; i++) crispsRecord.addItem();
        for (int i = 0; i < 2; i++) marsRecord.addItem();

        assertEquals(5, colaRecord.getNumberAvailable());
        assertEquals(3, crispsRecord.getNumberAvailable());
        assertEquals(2, marsRecord.getNumberAvailable());

        // sales
        colaRecord.buyItem(); // 4 left
        colaRecord.buyItem(); // 3 left
        crispsRecord.buyItem(); // 2 left
        marsRecord.buyItem(); // 1 left

        assertEquals(3, colaRecord.getNumberAvailable());
        assertEquals(2, crispsRecord.getNumberAvailable());
        assertEquals(1, marsRecord.getNumberAvailable());

        assertEquals(2, colaRecord.getNumberOfSales());
        assertEquals(1, crispsRecord.getNumberOfSales());
        assertEquals(1, marsRecord.getNumberOfSales());

        // Ensure total product count and consistency
        assertEquals(3, machine.getNumberOfProducts());

        // Attempting to over-purchase should throw
        // Buy the last valid item
        marsRecord.buyItem(); // 0 left

        // The next buy should throw
        assertThrows(ProductUnavailableException.class, marsRecord::buyItem);
        assertThrows(Exception.class, marsRecord::buyItem);

        // Final checks after all operations
        assertEquals(0, marsRecord.getNumberAvailable());
        assertEquals(2, marsRecord.getNumberOfSales());

    }

}
