package com.googlecode.doce.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

/**
 * 
 * @author binsongl
 *
 */
@Component
public class FileDataStore implements DataStore {
    private static final String TMP = "tmp";
    
    private static final String DIGEST = "SHA-1";
    
    private String path;
    
    private String homeDir = "./doce-reps";
    
    private File directory;

    @PostConstruct
    public void initialize() {
        if (path == null) {
            path = homeDir + "/datastore";
        }
        directory = new File(path);
        directory.mkdirs();
    }
    
    public DataRecord queryRecord(DataIdentifier identifier) {
        File file = getFile(identifier);
        synchronized (this) {
            if (!file.exists()) {
                return null;
            }
            
            return new FileDataRecord(identifier, file);
        }
    }

    public DataRecord saveRecord(InputStream input, String fileName) throws DataStoreException {
        File temporary = null;
        try {
            temporary = newTemporaryFile();

            long length = 0;
            MessageDigest digest = MessageDigest.getInstance(DIGEST);
            OutputStream output = new DigestOutputStream(
                    new FileOutputStream(temporary), digest);
            try {
                length = IOUtils.copyLarge(input, output);
            } finally {
            	input.close();
                output.close();
            }
            DataIdentifier identifier = new DataIdentifier(digest.digest());
            File file;

            file = getFile(identifier);
            boolean existFile = file.exists();
            synchronized (this) {
                
                if (!existFile) {
                    File parent = file.getParentFile();
                    parent.mkdirs();
                    if (temporary.renameTo(file)) {
                        // no longer need to delete the temporary file
                        temporary = null;
                    } else {
                        throw new IOException(
                                "Can not rename " + temporary.getAbsolutePath()
                                + " to " + file.getAbsolutePath()
                                + " (media read only?)");
                    }
                }
                
                if (file.length() != length) {
                    if (!file.isFile()) {
                        throw new IOException("Not a file: " + file);
                    }
                    throw new IOException(DIGEST + " collision: " + file);
                }
            }

            FileDataRecord dataRecord = new FileDataRecord(identifier, file);
            dataRecord.setExistFile(existFile);
            return dataRecord;
        } catch (NoSuchAlgorithmException e) {
            throw new DataStoreException(DIGEST + " not available", e);
        } catch (IOException e) {
            throw new DataStoreException("Could not add record", e);
        } finally {
            if (temporary != null) {
                temporary.delete();
            }
        }
    }

    private File newTemporaryFile() throws IOException {
        return File.createTempFile(TMP, null, directory);
    }
    
    private File getFile(DataIdentifier identifier) {
        String string = identifier.toString();
        File file = directory;
        file = new File(file, string.substring(0, 2));
        file = new File(file, string.substring(2, 4));
        file = new File(file, string.substring(4, 6));
        return new File(file, string);
    }

    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }
}
