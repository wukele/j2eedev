package com.googlecode.doce;

/**
 * 
 * @author binsongl
 *
 */
@SuppressWarnings("serial")
public class DoceException extends Exception {

    /**
     * Constructs a new instance of this class with the specified detail
     * message.
     *
     * @param message the detailed message.
     */
    public DoceException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new instance of this class with the specified detail
     * message and root cause.
     *
     * @param message the detailed message.
     * @param cause root failure cause
     */
    public DoceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of this class with the specified root cause.
     *
     * @param rootCause root failure cause
     */
    public DoceException(Throwable rootCause) {
        super("", rootCause);
    }
}