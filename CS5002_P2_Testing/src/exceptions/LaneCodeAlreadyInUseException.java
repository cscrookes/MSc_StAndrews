package exceptions;

public class LaneCodeAlreadyInUseException extends Exception {
    /**
     * Constructs a new LaneCodeAlreadyInUseException with the specified detail message.
     *
     * @param message the detail message
     */
    public LaneCodeAlreadyInUseException(String message) {
        super(message);
    }
}
