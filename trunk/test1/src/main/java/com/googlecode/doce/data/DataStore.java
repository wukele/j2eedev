package com.googlecode.doce.data;

import java.io.InputStream;

/**
 * 
 * @author binsongl
 *
 */
public interface DataStore {
    
    public void initialize();
    
    public DataRecord queryRecord(DataIdentifier identifier);
    
    public DataRecord saveRecord(InputStream stream, String fileName) throws DataStoreException;
}
