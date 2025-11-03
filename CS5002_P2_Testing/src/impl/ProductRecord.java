package impl;

import exceptions.ProductUnavailableException;
import interfaces.IVendingMachineProduct;
import interfaces.IProductRecord;

/**
 * This class represents a ProductRecord, recording information relating to a product sold in a vending machine.
 */
public class ProductRecord implements IProductRecord {

    private final IVendingMachineProduct product;
    private int numberOfSales;
    private int numberAvailable;

    /**
     * Constructs a new ProductRecord for a given product.
     * The record starts with zero items available and zero sales.
     *
     * @param product the product associated with this record
     * @throws IllegalArgumentException if the product is null
     */
    public ProductRecord(IVendingMachineProduct product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.product = product;
        this.numberOfSales = 0;
        this.numberAvailable = 0;
    }

    @Override
    public IVendingMachineProduct getProduct() {
        return product;
    }

    @Override
    public int getNumberOfSales() {
        return numberOfSales;
    }

    @Override
    public int getNumberAvailable() {
        return numberAvailable;
    }

    @Override
    public void addItem() {
        numberAvailable++;
    }

    @Override
    public void buyItem() throws ProductUnavailableException {
        if (numberAvailable <= 0) {
            throw new ProductUnavailableException("Product unavailable: " + product.getDescription());
        }
        numberAvailable--;
        numberOfSales++;
    }
}
