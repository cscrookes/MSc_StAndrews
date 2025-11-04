package exceptions;

/**
 * This exception should be used to indicate that a product is not available.
 */
public class ProductUnavailableException extends Exception {
    /**
     * Constructs a new ProductUnavailableException with the specified detail message.
     *
     * @param message the detail message
     */
    public ProductUnavailableException(String message) {
        super(message);
    }
}
