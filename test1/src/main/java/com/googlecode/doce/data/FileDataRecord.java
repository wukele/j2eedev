package com.googlecode.doce.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * @author binsongl
 *
 */
public class FileDataRecord extends AbstractDataRecord {

    /**
     * The file that contains the binary stream.
     */
    private final File file;

    /**
     * Creates a data record based on the given identifier and file.
     *
     * @param identifier data identifier
     * @param file file that contains the binary stream
     */
    public FileDataRecord(DataIdentifier identifier, File file) {
        super(identifier);
        assert file.isFile();
        this.file = file;
    }

    /**
     * {@inheritDoc}
     */
    public long getLength() {
        return file.length();
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getStream() throws DataStoreException {
        try {
            return new LazyFileInputStream(file);
        } catch (IOException e) {
            throw new DataStoreException("Error opening input stream of " + file.getAbsolutePath(), e);
        }
    }
    
    public InputStream getPageImageStream(int index) throws DataStoreException {
        try {
        	String path = file.getPath() + "-png/" + this.toString() + "-" + index+".png";
        	System.out.println(path);
            return new LazyFileInputStream(new File(path));
        } catch (IOException e) {
            throw new DataStoreException("Error opening input stream of " + file.getAbsolutePath(), e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public ByteBuffer getByteBuffer() throws DataStoreException {
    	try {
	    	RandomAccessFile raf = new RandomAccessFile(file, "r");
	
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			
			return buf;
    	} catch (FileNotFoundException e) {
			throw new DataStoreException(e);
		} catch (IOException e) {
			throw new DataStoreException(e);
		}
    }

	public String getFilePath() {
		return file.getPath();
	}
	
	public File getFile() {
		return file;
	}
}