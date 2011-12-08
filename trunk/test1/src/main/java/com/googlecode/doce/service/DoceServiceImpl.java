package com.googlecode.doce.service;

import static com.googlecode.doce.DoceConstants.NODE_STATUS_DELETE;
import static com.googlecode.doce.DoceConstants.NODE_STATUS_ENABLED;
import static com.googlecode.doce.DoceConstants.NODE_STATUS_RELA_DELETE;
import static com.googlecode.doce.DoceConstants.NODE_STATUS_SHARED;
import static com.googlecode.doce.DoceConstants.NODE_TYPE_FILE;
import static com.googlecode.doce.DoceConstants.NODE_TYPE_FOLDER;
import static com.googlecode.doce.DoceConstants.OWNER_ROOT_NODE_ID;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.util.Assert;

import com.googlecode.doce.DoceConstants;
import com.googlecode.doce.DoceException;
import com.googlecode.doce.data.DataIdentifier;
import com.googlecode.doce.data.DataRecord;
import com.googlecode.doce.data.DataStore;
import com.googlecode.doce.events.AddDocumentEvent;
import com.googlecode.doce.events.DeleteDocumentEvent;
import com.googlecode.doce.model.Node;
import com.googlecode.doce.persistence.DocePersistenceManager;
import com.googlecode.doce.utils.EntityIdHelper;
import com.googlecode.doce.utils.PathUtils;
       
/**
 * 
 * @author binsongl
 *
 */
public class DoceServiceImpl implements DoceService {
	protected static final Logger LOGGER = LoggerFactory.getLogger(DoceServiceImpl.class);
    
	@Autowired
    private DocePersistenceManager persistenceManager;
	
	@Autowired
    private DataStore dataStore;
    
	@Autowired
    private ApplicationContext applicationContext;
    
    /* (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#createFolder(java.lang.String, java.lang.String, java.lang.Long)
     */
    public String createFolder(String owner, String name, String parentDirId) throws DoceException {
    	this.checkNodeExist(parentDirId);
    	
    	if(this.existNodeName(parentDirId, name, NODE_TYPE_FOLDER))
            throw new SameNodeNameException("There is the same node name: " + name);
    	
        Node node = new Node();
        node.setNodeId(EntityIdHelper.getEntityId());
        node.setOwner(owner);
        node.setNodeName(name);
        node.setParentDirId(parentDirId);
        node.setNodeType(NODE_TYPE_FOLDER);
        node.setCurrentStatus(NODE_STATUS_ENABLED);
        node.setModified(new Date());
        
        return persistenceManager.save(node);
    }
    
    /* (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#upload(java.lang.String, java.lang.String, java.lang.String, java.lang.Long, java.io.InputStream)
     */
    public String upload(String owner, String filename, String mimeType, String parentDirId, InputStream input) throws DoceException {
    	this.checkNodeExist(parentDirId);
    	
    	if(this.existNodeName(parentDirId, filename, NODE_TYPE_FILE))
            throw new SameNodeNameException("There is the same node name: " + filename);
    	
        DataRecord dataRecord = dataStore.saveRecord(input, filename);
        
        Node node = new Node();
        node.setNodeId(EntityIdHelper.getEntityId());
        node.setOwner(owner);
        node.setNodeName(filename);
        node.setMimeType(mimeType);
        node.setFileCode(dataRecord.getIdentifier().toString());
        node.setFileSize(dataRecord.getLength());
        node.setParentDirId(parentDirId);
        node.setNodeType(NODE_TYPE_FILE);
        node.setCurrentStatus(NODE_STATUS_ENABLED);
        node.setModified(new Date());
     
        persistenceManager.save(node);
        if(!dataRecord.getExistFile()) {
        	applicationContext.publishEvent(new AddDocumentEvent(node, dataRecord.getIdentifier().toString()));
        }
        return node.getNodeId();
    }
    
//	public String innerSaveFile(Node node) {
//		return persistenceManager.create(node);
//	}
    
    /* (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#download(java.lang.String)
     */
    public InputStream download(String nodeId) throws DoceException {
    	Node node;
    	try {
    		node = persistenceManager.queryNode(nodeId);
    	} catch (EmptyResultDataAccessException e) {
			throw new NodeNotExistException(nodeId);
		}
    	
        DataIdentifier identifier = new DataIdentifier(node.getFileCode());
        
        return dataStore.queryRecord(identifier).getStream();
    }
     
    public InputStream getPageImageForFile(String nodeId, int index) throws DoceException {
    	Node node;
    	try {
    		node = persistenceManager.queryNode(nodeId);
    	} catch (EmptyResultDataAccessException e) {
			throw new NodeNotExistException(nodeId);
		}
    	
        DataIdentifier identifier = new DataIdentifier(node.getFileCode());
     	return dataStore.queryRecord(identifier).getPageImageStream(index);
    }
    
    /* (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#queryNode(java.lang.Long)
     */
    public Node queryNode(String nodeId) throws DoceException {
    	try {
    		return persistenceManager.queryNode(nodeId);
    	} catch (EmptyResultDataAccessException e) {
			throw new NodeNotExistException(nodeId);
		}
    }
    
    /*
     * (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#queryNode(java.lang.String, java.lang.String)
     */
    public Node queryNodeByPath(String path) throws DoceException {
    	Assert.hasText(path);
    	
    	String[] names = PathUtils.getNodeNames(path);
    	
    	Node node = null;
    	try {
    		for(int i=0, len = names.length; i<len; i++) {
        		if(node == null)
        			node = persistenceManager.queryNodeByName(OWNER_ROOT_NODE_ID, names[i]);
        		else
        			node = persistenceManager.queryNodeByName(node.getNodeId(), names[i]);
        	}
    	} catch (EmptyResultDataAccessException e) {
			throw new PathException("path not exist: " + path);
		}
    	
    	return node;
    }
    
    private void checkNodeExist(String nodeId) throws DoceException {
    	if(!nodeId.equals(OWNER_ROOT_NODE_ID))
    		this.queryNode(nodeId);
    }
    
    /* (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#queryChildNodes(java.lang.Long, java.lang.String)
     */
    public List<Node> queryChildNodes(String parentDirId) throws DoceException {
        List<Node> list = persistenceManager.queryChildNodes(parentDirId);
        
        return list;
    }
    
    /*
     * (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#deleteNode(java.lang.String, java.lang.Long)
     */
    public void deleteNode(String nodeId) throws DoceException {
        this.deleteNode(nodeId, NODE_STATUS_DELETE);
    }
    
    private void deleteNode(String nodeId, int status) throws DoceException {
    	this._updateNodeStatus(nodeId, status);
        List<Node> list = persistenceManager.queryChildNodes(nodeId);
        
        for(Node node : list) {
            if(node.getNodeType() == 0) { //folder
                this.deleteNode(node.getNodeId(), NODE_STATUS_RELA_DELETE);
            } else {
            	this._updateNodeStatus(node.getNodeId(), NODE_STATUS_RELA_DELETE);
            }
        }
    }
    
    public void bulkDeleteNodes(List<String> nodeIds) throws DoceException {
    	for(String nodeId : nodeIds) {
    		this.deleteNode(nodeId);
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#queryDeletedNods(java.lang.String)
     */
    public List<Node> queryDeletedNodes(String owner) throws DoceException {
        List<Node> list = persistenceManager.findByNamedParam(new String[]{"currentStatus", "owner"}, new Object[]{NODE_STATUS_DELETE, owner});
        
        return list;
    }
    
    /*
     * (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#unDeleteNode(java.lang.String, java.lang.Long)
     */
    public void unDeleteNode(String nodeId) throws DoceException {
        this.unDeleteNode(nodeId, NODE_STATUS_ENABLED);
    }
    
    private void unDeleteNode(String nodeId, int status) throws DoceException {
    	this._updateNodeStatus(nodeId, status);
        List<Node> list = persistenceManager.queryChildNodes(nodeId);
        
        for(Node node : list) {
            if(node.getNodeType() == NODE_TYPE_FOLDER) { //folder
                this.deleteNode(node.getNodeId(), status);
            } else {
            	this._updateNodeStatus(node.getNodeId(), status);
            }
        }
    }
    
    public void bulkUnDeleteNodes(List<String> nodeIds) throws DoceException {
    	for(String nodeId : nodeIds) {
    		this.unDeleteNode(nodeId);
    	}
    }
    
    /*
     * (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#permanentDeleteNode(java.lang.String, java.lang.Long)
     */
    public void permanentDeleteNode(String nodeId) throws DoceException {
        Node delNode = this._deleteNode(nodeId);
        
        if(delNode.getNodeType() == NODE_TYPE_FILE)
        	applicationContext.publishEvent(new DeleteDocumentEvent(nodeId, delNode.getFileCode()));
        else {
	        List<Node> list = persistenceManager.queryChildNodes(nodeId);
	        
	        for(Node node : list) {
	            if(node.getNodeType() == NODE_TYPE_FOLDER) { //folder
	                this.permanentDeleteNode(node.getNodeId());
	            } else {
	            	this._deleteNode(node.getNodeId());
	                
	                applicationContext.publishEvent(new DeleteDocumentEvent(node.getNodeId(), node.getFileCode()));
	            }
	        }
        }
    }
    
    private Node _deleteNode(String nodeId) {
		List<Node> nodes = persistenceManager.findByNamedParam("nodeId", nodeId);
		Node node = nodes.get(0);
		persistenceManager.delete(node);
		return node;
    }
    
    public void bulkPermanentDeleteNodes(List<String> nodeIds) throws DoceException {
    	for(String nodeId : nodeIds)
    		this.permanentDeleteNode(nodeId);
    }

    /*
     * (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#moveNode(java.lang.String, java.lang.String, java.lang.String)
     */
    public void moveNode(String nodeId, String parentDirId) throws DoceException {
    	this.checkNodeExist(parentDirId);
    	
    	Node node;
    	try {
    		node = persistenceManager.queryNode(nodeId);
    	} catch (EmptyResultDataAccessException e) {
			throw new NodeNotExistException(nodeId);
		}
    	
    	if(this.existNodeName(parentDirId, node.getNodeName(), node.getNodeType()))
            throw new SameNodeNameException("There is the same node name: " + node.getNodeName());
    	
        persistenceManager.moveNode(nodeId, parentDirId);
    }
    
    /*
     * (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#copyNode(java.lang.String, java.lang.Long, java.lang.Long)
     */
    public void copyNode(String nodeId, String parentDirId) throws DoceException {
    	this.checkNodeExist(parentDirId);
    	
    	Node node;
    	try {
    		node = persistenceManager.queryNode(nodeId);
    	} catch (EmptyResultDataAccessException e) {
			throw new NodeNotExistException(nodeId);
		}
    	
    	if(this.existNodeName(parentDirId, node.getNodeName(), node.getNodeType()))
            throw new SameNodeNameException("There is the same node name: " + node.getNodeName());
    	
    	if(node.getNodeType() == DoceConstants.NODE_TYPE_FILE) {
    		node.setParentDirId(parentDirId);
    		persistenceManager.save(node);
    	} else
    		this.createFolder(node.getOwner(), node.getNodeName(), parentDirId);
    }
    
    /*
     * (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#existNodeName(java.lang.String, java.lang.Long, java.lang.String)
     */
    public boolean existNodeName(String parentDirId, String name, int nodeType) {
        return persistenceManager.existNodeName(parentDirId, name, nodeType);
    }

    /*
     * (non-Javadoc)
     * @see com.sumilux.doce.service.DoceService#renameNode(java.lang.String, java.lang.Long, java.lang.String)
     */
    public void renameNode(String nodeId, String name) throws DoceException {
    	Node node;
    	try {
    		node = persistenceManager.queryNode(nodeId);
    	} catch (EmptyResultDataAccessException e) {
			throw new NodeNotExistException(nodeId);
		}
    	
    	if(name.equals(node.getNodeName())) 
    		return;
    		
        if(this.existNodeName(node.getParentDirId(), name, node.getNodeType()))
            throw new SameNodeNameException("There is the same node name: " + name);
        persistenceManager.renameNode(nodeId, name);
    }
    
    public List<String> sharedNode(String nodeId, List<String> receivers) throws DoceException {
    	return this.sharedNode(nodeId, receivers, OWNER_ROOT_NODE_ID);
    }
    
    public List<String> sharedNode(String nodeId, List<String> receivers, String parentDirId) throws DoceException {
    	Node node = null;
    	if(receivers.size() > 0) {
    		node = this._updateNodeStatus(nodeId, NODE_STATUS_SHARED);
    	}
    	
    	List<String> nodeIds = new ArrayList<String>();
    	for(String receiver : receivers) {
    		Node newNode = new Node();
    		String _nodeId = EntityIdHelper.getEntityId();
    		newNode.setNodeId(_nodeId);
    		newNode.setNodeName(node.getNodeName());
    		newNode.setNodeType(node.getNodeType());
    		newNode.setFileCode(node.getFileCode());
    		newNode.setFilePages(node.getFilePages());
    		newNode.setMimeType(node.getMimeType());
    		newNode.setModified(node.getModified());
    		newNode.setFileSize(node.getFileSize());
    		
    		newNode.setSharedNodeId(nodeId);
    		newNode.setSharer(node.getOwner());
    		newNode.setOwner(receiver);
    		newNode.setParentDirId(parentDirId);
    		newNode.setCurrentStatus(NODE_STATUS_SHARED);
    		persistenceManager.save(newNode);
    		
    		nodeIds.add(_nodeId);
    	}
    	return nodeIds;
    }

    public List<String> unSharedNode(String nodeId, List<String> receivers) throws DoceException {
    	Node node = this.queryNode(nodeId);
    	
    	List<String> nodeIds = new ArrayList<String>();
    	for(String receiver : receivers) {
    		List<Node> nodes = persistenceManager.findByNamedParam(new String[]{"owner", "sharer", "sharedNodeId"}, 
        			new Object[]{receiver, node.getOwner(), nodeId});
    		persistenceManager.deleteAll(nodes);
    		
    		if(nodes.size() > 0)
    			nodeIds.add(nodes.get(0).getNodeId());
    	}
    	
    	List<Node> nodes = persistenceManager.findByNamedParam(new String[]{"sharer", "sharedNodeId"}, 
    			new Object[]{node.getOwner(), nodeId});
    	
    	if(nodes.size() == 0)
    		this._updateNodeStatus(nodeId, NODE_STATUS_ENABLED);
    	
    	return nodeIds;
    }
    
    public List<Node> getMySharedNodes(String owner) throws DoceException {
    	List<Node> list = persistenceManager.findByNamedParam(new String[]{"owner", "currentStatus", "sharedNodeId"}, 
    			new Object[]{owner, NODE_STATUS_SHARED, Restrictions.isNull("sharedNodeId")});
    	
    	return list;
    }
    
    public List<Node> getNodesSharedWithMe(String owner) throws DoceException {
    	List<Node> list = persistenceManager.findByNamedParam(new String[]{"owner", "currentStatus", "sharedNodeId"}, 
    			new Object[]{owner, NODE_STATUS_SHARED, Restrictions.isNotNull("sharedNodeId")});
    	
    	return list;
    }
	
	private Node _updateNodeStatus(String nodeId, int status) {
		List<Node> nodes = persistenceManager.findByNamedParam("nodeId", nodeId);
		Node node = nodes.get(0);
		node.setCurrentStatus(status);
		persistenceManager.update(node);
		
		return node;
	}
    
    public DataStore getDataStore() {
        return dataStore;
    }

    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }
}
