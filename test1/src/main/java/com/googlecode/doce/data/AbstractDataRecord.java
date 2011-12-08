package com.googlecode.doce.data;

/**
 * 
 * @author binsongl
 *
 */
public abstract class AbstractDataRecord implements DataRecord {
	private boolean existFile;

    /**
     * The binary identifier;
     */
    private final DataIdentifier identifier;

    /**
     * Creates a data record with the given identifier.
     *
     * @param identifier data identifier
     */
    public AbstractDataRecord(DataIdentifier identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the data identifier.
     *
     * @return data identifier
     */
    public DataIdentifier getIdentifier() {
        return identifier;
    }

    /**
     * Returns the string representation of the data identifier.
     *
     * @return string representation
     */
    public String toString() {
        return identifier.toString();
    }

    /**
     * Checks if the given object is a data record with the same identifier
     * as this one.
     *
     * @param object other object
     * @return <code>true</code> if the other object is a data record and has
     *         the same identifier as this one, <code>false</code> otherwise
     */
    public boolean equals(Object object) {
        return (object instanceof DataRecord)
            && identifier.equals(((DataRecord) object).getIdentifier());
    }

    /**
     * Returns the hash code of the data identifier.
     *
     * @return hash code
     */
    public int hashCode() {
        return identifier.hashCode();
    }

    public boolean getExistFile() {
    	return existFile;
    }
    
    public void setExistFile(boolean existFile) {
    	this.existFile = existFile;
    }
}