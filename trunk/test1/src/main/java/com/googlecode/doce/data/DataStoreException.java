package com.googlecode.doce.data;

import com.googlecode.doce.DoceException;

/**
 * 
 * @author binsongl
 *
 */
@SuppressWarnings("serial")
public class DataStoreException extends DoceException {

    /**
     * Constructs a new instance of this class with the specified detail
     * message.
     *
     * @param message the detailed message.
     */
    public DataStoreException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of this class with the specified detail
     * message and root cause.
     *
     * @param message the detailed message.
     * @param cause root failure cause
     */
    public DataStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of this class with the specified root cause.
     *
     * @param rootCause root failure cause
     */
    public DataStoreException(Throwable rootCause) {
        super(rootCause);
    }

}
