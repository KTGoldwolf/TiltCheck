package service;

/**
 * Created by KTGoldwolf on 10/1/2016.
 */
public class NoResultsException extends Exception {
    public NoResultsException() { super(); }
    public NoResultsException(String message) {
        super(message);
    }
}
