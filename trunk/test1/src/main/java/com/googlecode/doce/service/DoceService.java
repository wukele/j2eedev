package com.googlecode.doce.service;

import java.io.InputStream;
import java.util.List;

import com.googlecode.doce.DoceException;
import com.googlecode.doce.model.Node;

public interface DoceService {
	
    /**
     * create folder
     * 
     * @param owner 
     * @param name  
     * @param parentDirId
     * @throws DoceException
     */
    public String createFolder(String owner, String name, String parentDirId) throws DoceException;

    /**
     * upload file
     * 
     * @param owner
     * @param filename
     * @param parentDirId
     * @param input
     * @throws DoceException
     */
    public String upload(String owner, String filename, String mimeType, String parentDirId, InputStream input) throws DoceException;

    /**
     * 
     * @param owner
     * @param nodeId
     * @return
     * @throws DoceException
     */
    public InputStream download(String nodeId) throws DoceException;
    
    /**
     * 
     * @param owner
     * @param nodeId
     * @param index
     * @return
     * @throws DoceException
     */
    public InputStream getPageImageForFile(String nodeId, int index) throws DoceException;

    /**
     * 
     * @param owner
     * @param nodeId
     * @return
     */
    public Node queryNode(String nodeId) throws DoceException;
    
    /**
     * 
     * @param owner
     * @param path
     * @return
     * @throws DoceException
     */
    public Node queryNodeByPath(String path) throws DoceException;
    
    /**
     * 
     * @param owner
     * @param parentDirId
     * @return
     */
    public List<Node> queryChildNodes(String parentDirId) throws DoceException;
    
    /**
     * the folder/file is deleted into the recycle bin, recoverable
     * 
     * @param owner
     * @param nodeId
     */
    public void deleteNode(String nodeId) throws DoceException;
    
    public void bulkDeleteNodes(List<String> nodeIds) throws DoceException;
    
    /**
     * 
     * @param owner
     * @return
     * @throws DoceException
     */
    public List<Node> queryDeletedNodes(String owner) throws DoceException;
    
    /**
     * revocver deleted the folder/file
     * 
     * @param owner
     * @param nodeId
     */
    public void unDeleteNode(String nodeId) throws DoceException;
    
    public void bulkUnDeleteNodes(List<String> nodeIds) throws DoceException;
    
    /**
     * After the folder/file is deleted into the recycle bin, 
     * permanetly delete the folder/file information in the database table data, unrecoverable
     * 
     * @param owner
     * @param nodeId
     */
    public void permanentDeleteNode(String nodeId) throws DoceException;
    
    public void bulkPermanentDeleteNodes(List<String> nodeIds) throws DoceException;
    
    /**
     * move folder/file to new location
     * 
     * @param owner
     * @param nodeId
     * @param parentDirId
     */
    public void moveNode(String nodeId, String parentDirId) throws DoceException;
    
    /**
     * copy folder/file to new location
     * 
     * @param owner
     * @param nodeId
     * @param parentDirId
     */
    public void copyNode(String nodeId, String parentDirId) throws DoceException;
    
    /**
     * Check the directory name is the same folder / file
     * 
     * @param owner
     * @param parentDirId
     * @param name
     * @return
     */
    public boolean existNodeName(String parentDirId, String name, int nodeType) throws DoceException;
    
    /**
     * Rename the folder/file name. if the diectory has the same name folder/file, 
     * throw an exception SameNodeNameException
     * 
     * @param owner
     * @param parentDirId
     * @param nodeId
     * @param name
     * @throws DoceException
     */
    public void renameNode(String nodeId, String name) throws DoceException;
    
    /**
     * Shared folder/file, 
     * 
     * @param owner
     * @param receivers
     * @param nodeId
     * @throws DoceException
     */
    public List<String> sharedNode(String nodeId, List<String> receivers) throws DoceException;
    
    /**
     * Shared folder/file, 
     * 
     * @param owner
     * @param receivers
     * @param nodeId
     * @throws DoceException
     */
    public List<String> sharedNode(String nodeId, List<String> receivers, String parentDirId) throws DoceException;

    /**
     * unshared folder/file, 
     * 
     * @param owner
     * @param receivers
     * @param nodeId
     * @throws DoceException
     */
    public List<String> unSharedNode(String nodeId, List<String> receivers) throws DoceException;
    
    /**
     * 
     * @param sharder
     * @param nodeId
     * @throws DoceException
     */
    public List<Node> getMySharedNodes(String owner) throws DoceException;
    
    /**
     * 
     * @param sharder
     * @param receiver
     * @param nodeId
     * @throws DoceException
     */
    public List<Node> getNodesSharedWithMe(String owner) throws DoceException;
    
}