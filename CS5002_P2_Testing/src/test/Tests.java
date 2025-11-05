package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

}
