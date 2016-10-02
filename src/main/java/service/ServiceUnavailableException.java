package service;

/**
 * Created by KTGoldwolf on 10/1/2016.
 */
public class ServiceUnavailableException extends Exception {
    public ServiceUnavailableException() { super(); }
    public ServiceUnavailableException(String message) {
        super(message);
    }
}
