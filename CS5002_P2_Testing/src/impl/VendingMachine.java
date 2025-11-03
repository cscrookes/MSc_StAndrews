package impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;

import exceptions.LaneCodeAlreadyInUseException;
import exceptions.LaneCodeNotRegisteredException;
import exceptions.ProductUnavailableException;
import interfaces.IVendingMachineProduct;
import interfaces.IVendingMachine;
import interfaces.IProductRecord;

/**
 * This class represents a simple vending machine which can stock and sell products.
 */
public class VendingMachine implements IVendingMachine {

    // Each lane code maps to its ProductRecord
    private final Map<String, IProductRecord> lanes = new HashMap<>();

    @Override
    public void registerProduct(IVendingMachineProduct vendingMachineProduct)
            throws LaneCodeAlreadyInUseException {

        String laneCode = vendingMachineProduct.getLaneCode();
        if (lanes.containsKey(laneCode)) {
            throw new LaneCodeAlreadyInUseException("Lane code already registered: " + laneCode);
        }

        // create a new record for this product
        IProductRecord record = new ProductRecord(vendingMachineProduct);
        lanes.put(laneCode, record);
    }

    @Override
    public void unregisterProduct(IVendingMachineProduct vendingMachineProduct)
            throws LaneCodeNotRegisteredException {

        String laneCode = vendingMachineProduct.getLaneCode();
        if (!lanes.containsKey(laneCode)) {
            throw new LaneCodeNotRegisteredException("Lane code not registered: " + laneCode);
        }

        lanes.remove(laneCode);
    }

    @Override
    public void addItem(String laneCode) throws LaneCodeNotRegisteredException {
        IProductRecord record = lanes.get(laneCode);
        if (record == null) {
            throw new LaneCodeNotRegisteredException("Lane code not registered: " + laneCode);
        }
        record.addItem();
    }

    @Override
    public void buyItem(String laneCode)
            throws ProductUnavailableException, LaneCodeNotRegisteredException {
        IProductRecord record = lanes.get(laneCode);
        if (record == null) {
            throw new LaneCodeNotRegisteredException("Lane code not registered: " + laneCode);
        }
        record.buyItem();
    }

    @Override
    public int getNumberOfProducts() {
        return lanes.size();
    }

    @Override
    public int getTotalNumberOfItems() {
        int total = 0;
        for (IProductRecord record : lanes.values()) {
            total += record.getNumberAvailable();
        }
        return total;
    }

    @Override
    public int getNumberOfItems(String laneCode) throws LaneCodeNotRegisteredException {
        IProductRecord record = lanes.get(laneCode);
        if (record == null) {
            throw new LaneCodeNotRegisteredException("Lane code not registered: " + laneCode);
        }
        return record.getNumberAvailable();
    }

    @Override
    public int getNumberOfSales(String laneCode) throws LaneCodeNotRegisteredException {
        IProductRecord record = lanes.get(laneCode);
        if (record == null) {
            throw new LaneCodeNotRegisteredException("Lane code not registered: " + laneCode);
        }
        return record.getNumberOfSales();
    }

    @Override
    public IVendingMachineProduct getMostPopular() throws LaneCodeNotRegisteredException {
        if (lanes.isEmpty()) {
            throw new LaneCodeNotRegisteredException("No products registered in machine");
        }

        // find record with max sales
        return lanes.values().stream()
                .max(Comparator.comparingInt(IProductRecord::getNumberOfSales))
                .get() // safe because we already checked for empty
                .getProduct();
    }
}
