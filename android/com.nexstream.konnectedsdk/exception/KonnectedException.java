package com.nexstream.konnectedsdk.exception;

/**
 * Created by jasonhor on 09/12/15.
 */
public class KonnectedException extends Exception {
    public KonnectedException() {
    }

    public KonnectedException(String message) {
        super(message);
    }

    public KonnectedException(Throwable cause) {
        super(cause);
    }

    public KonnectedException(String message, Throwable cause) {
        super(message, cause);
    }

}
