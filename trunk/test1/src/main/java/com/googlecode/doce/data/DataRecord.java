package com.googlecode.doce.data;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * 
 * @author binsongl
 *
 */
public interface DataRecord {

    /**
     * Returns the identifier of this record.
     *
     * @return data identifier
     */
    DataIdentifier getIdentifier();

    /**
     * Returns the length of the binary stream in this record.
     *
     * @return length of the binary stream
     * @throws DataStoreException if the record could not be accessed
     */
    long getLength() throws DataStoreException;

    /**
     * Returns the the binary stream in this record.
     *
     * @return binary stream
     * @throws DataStoreException if the record could not be accessed
     */
    InputStream getStream() throws DataStoreException;
    
    InputStream getPageImageStream(int index) throws DataStoreException;
    
    /**
     * Returns the the byte array in this record.
     *
     * @return binary stream
     * @throws DataStoreException if the record could not be accessed
     */
    ByteBuffer getByteBuffer() throws DataStoreException;
    
    String getFilePath();
    
    File getFile();
    
    boolean getExistFile();
    
    void setExistFile(boolean existFile);
}

