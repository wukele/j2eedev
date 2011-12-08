package com.googlecode.doce.data;

import java.io.Serializable;

/**
 * 
 * @author binsongl
 *
 */
public final class DataIdentifier implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -9197191401131100016L;

    /**
     * Array of hexadecimal digits.
     */
    private static final char[] HEX = "0123456789abcdef".toCharArray();

    /**
     * Data identifier.
     */
    private final String identifier;

    /**
     * Creates a data identifier from the given string.
     *
     * @param identifier data identifier
     */
    public DataIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Creates a data identifier from the hexadecimal string
     * representation of the given bytes.
     *
     * @param identifier data identifier
     */
    public DataIdentifier(byte[] identifier) {
        char[] buffer = new char[identifier.length * 2];
        for (int i = 0; i < identifier.length; i++) {
            buffer[2 * i] = HEX[(identifier[i] >> 4) & 0x0f];
            buffer[2 * i + 1] = HEX[identifier[i] & 0x0f];
        }
        this.identifier = new String(buffer);
    }

    //-------------------------------------------------------------< Object >

    /**
     * Returns the identifier string.
     *
     * @return identifier string
     */
    public String toString() {
        return identifier;
    }

    /**
     * Checks if the given object is a data identifier and has the same
     * string representation as this one.
     *
     * @param object other object
     * @return <code>true</code> if the given object is the same identifier,
     *         <code>false</code> otherwise
     */
    public boolean equals(Object object) {
        return (object instanceof DataIdentifier)
            && identifier.equals(object.toString());
    }

    /**
     * Returns the hash code of the identifier string.
     *
     * @return hash code
     */
    public int hashCode() {
        return identifier.hashCode();
    }

}
