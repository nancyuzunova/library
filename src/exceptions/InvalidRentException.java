package exceptions;

public class InvalidRentException extends Exception {

    public InvalidRentException() {
        super("Sorry! You cannot rent this item!");
    }
}
