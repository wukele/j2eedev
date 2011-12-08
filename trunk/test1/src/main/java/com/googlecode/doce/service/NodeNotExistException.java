package com.googlecode.doce.service;

import com.googlecode.doce.DoceException;

/**
 * 
 * @author binsongl
 *
 */
@SuppressWarnings("serial")
public class NodeNotExistException extends DoceException {
	
	/**
     * Constructs a new instance of this class with the specified nodeId
     *
     * @param message the detailed message.
     */
    public NodeNotExistException(Long nodeId) {
        super("node not exist: " + nodeId);
    }

    /**
     * Constructs a new instance of this class with the specified detail
     * message.
     *
     * @param message the detailed message.
     */
    public NodeNotExistException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of this class with the specified detail
     * message and root cause.
     *
     * @param message the detailed message.
     * @param cause root failure cause
     */
    public NodeNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of this class with the specified root cause.
     *
     * @param rootCause root failure cause
     */
    public NodeNotExistException(Throwable rootCause) {
        super(rootCause);
    }

}
