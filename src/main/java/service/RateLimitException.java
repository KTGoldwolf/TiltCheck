package service;

/**
 * Created by KTGoldwolf on 10/1/2016.
 */
public class RateLimitException extends Exception {

    public RateLimitException() {
        super();
    }

    public RateLimitException(String message) {
        super(message);
    }
}
