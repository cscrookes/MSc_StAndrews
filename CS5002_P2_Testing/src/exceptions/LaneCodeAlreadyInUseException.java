package exceptions;

public class LaneCodeAlreadyInUseException extends Exception {
    package exceptions;
    /**
     * Constructs a new LaneCodeAlreadyInUseException with no detail message.
     */
    public LaneCodeAlreadyInUseException() {
        super();
    }

    /**
     * Constructs a new LaneCodeAlreadyInUseException with the specified detail message.
     *
     * @param message the detail message
     */
    public LaneCodeAlreadyInUseException(String message) {
        super(message);
    }
}
