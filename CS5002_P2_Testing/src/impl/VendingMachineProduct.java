package impl;

import interfaces.IVendingMachineProduct;

/**
 * This class represents products that can be stocked and sold in a vending machine in a specific lane.
 */
public class VendingMachineProduct implements IVendingMachineProduct {

    private final String laneCode;
    private final String description;

    /**
     * Constructs a new VendingMachineProduct with the given lane code and description.
     *
     * @param laneCode the lane code (e.g. A1, B2)
     * @param description the description of the product (e.g. "Cola", "Crisps")
     * @throws IllegalArgumentException if laneCode or description is null or blank
     */
    public VendingMachineProduct(String laneCode, String description) {
        if (laneCode == null || laneCode.isBlank()) {
            throw new IllegalArgumentException("Lane code cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        this.laneCode = laneCode;
        this.description = description;
    }

    @Override
    public String getLaneCode() {
        return laneCode;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return laneCode + " - " + description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof VendingMachineProduct)) return false;
        VendingMachineProduct other = (VendingMachineProduct) obj;
        return laneCode.equals(other.laneCode);
    }

    @Override
    public int hashCode() {
        return laneCode.hashCode();
    }
}
