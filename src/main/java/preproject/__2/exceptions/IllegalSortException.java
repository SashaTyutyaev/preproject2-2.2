package preproject.__2.exceptions;

public class IllegalSortException extends RuntimeException {

    private String messsage;

    public IllegalSortException(String messsage) {
        super(messsage);
    }
}
