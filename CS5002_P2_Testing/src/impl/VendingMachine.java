package impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.regex.Pattern;

import exceptions.LaneCodeAlreadyInUseException;
import exceptions.LaneCodeNotRegisteredException;
import exceptions.ProductUnavailableException;
import interfaces.IVendingMachineProduct;
import interfaces.IVendingMachine;
import interfaces.IProductRecord;

/**
 * This class represents a simple vending machine which can stock and sell
 * products.
 */
public class VendingMachine implements IVendingMachine {

    // Each lane code maps to its ProductRecord
    private final Map<String, IProductRecord> lanes = new HashMap<>();

    // Regex pattern: must contain one letter and one digit in any order (e.g. A1 or 3B)
    private static final Pattern VALID_LANE_CODE = Pattern.compile("^(?:[A-Za-z]\\d|\\d[A-Za-z])$");

    /**
     * Validates that the lane code follows the correct format (one letter and one number).
     */
    private void validateLaneCodeFormat(String laneCode) {
        if (laneCode == null || !VALID_LANE_CODE.matcher(laneCode).matches()) {
            throw new IllegalArgumentException(
                "Invalid lane code format: " + laneCode + 
                ". Lane codes must contain exactly one letter and one number (e.g. A1 or 3D)."
            );
        }
    }

    @Override
    public void registerProduct(IVendingMachineProduct vendingMachineProduct)
            throws LaneCodeAlreadyInUseException {

        String laneCode = vendingMachineProduct.getLaneCode();
        validateLaneCodeFormat(laneCode);

        if (lanes.containsKey(laneCode)) {
            throw new LaneCodeAlreadyInUseException("Lane code already registered: " + laneCode);
        }

        IProductRecord record = new ProductRecord(vendingMachineProduct);
        lanes.put(laneCode, record);
    }

    @Override
    public void unregisterProduct(IVendingMachineProduct vendingMachineProduct)
            throws LaneCodeNotRegisteredException {

        String laneCode = vendingMachineProduct.getLaneCode();
        validateLaneCodeFormat(laneCode);

        if (!lanes.containsKey(laneCode)) {
            throw new LaneCodeNotRegisteredException("Lane code not registered: " + laneCode);
        }

        lanes.remove(laneCode);
    }

    @Override
    public void addItem(String laneCode) throws LaneCodeNotRegisteredException {
        validateLaneCodeFormat(laneCode);

        IProductRecord record = lanes.get(laneCode);
        if (record == null) {
            throw new LaneCodeNotRegisteredException("Lane code not registered: " + laneCode);
        }
        record.addItem();
    }

    @Override
    public void buyItem(String laneCode)
            throws ProductUnavailableException, LaneCodeNotRegisteredException {
        validateLaneCodeFormat(laneCode);

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
        validateLaneCodeFormat(laneCode);

        IProductRecord record = lanes.get(laneCode);
        if (record == null) {
            throw new LaneCodeNotRegisteredException("Lane code not registered: " + laneCode);
        }
        return record.getNumberAvailable();
    }

    @Override
    public int getNumberOfSales(String laneCode) throws LaneCodeNotRegisteredException {
        validateLaneCodeFormat(laneCode);

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

        return lanes.values().stream()
                .max(Comparator.comparingInt(IProductRecord::getNumberOfSales))
                .get()
                .getProduct();
    }
}
