package com.googlecode.doce.service;

import static com.googlecode.doce.DoceConstants.OWNER_ROOT_NODE_ID;

import java.io.InputStream;
import java.util.List;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.doce.DoceConstants;
import com.googlecode.doce.DoceException;
import com.googlecode.doce.model.Node;

/**
 * 
 * @author binsongl
 *
 */
@Service
@Transactional
public class SecuredDoceServiceImpl extends DoceServiceImpl implements SecuredDoceService {
	private final PermissionFactory permissionFactory = new DefaultPermissionFactory();
	
	@Autowired
	private MutableAclService mutableAclService;
	
	public String createFolder(String owner, String name, String parentDirId) throws DoceException {
		if(!DoceConstants.OWNER_ROOT_NODE_ID.equals(parentDirId))
			this.checkParentDirPermission(owner, parentDirId);
		
		String nodeId = super.createFolder(owner, name, parentDirId);
		this.addPermission(owner, nodeId, BasePermission.WRITE.getMask());
		this.addPermission(owner, nodeId, BasePermission.READ.getMask());
		return nodeId;
	}
	
	public String upload(String owner, String filename, String mimeType, String parentDirId, InputStream input) throws DoceException {
		if(!DoceConstants.OWNER_ROOT_NODE_ID.equals(parentDirId))
			this.checkParentDirPermission(owner, parentDirId);
		
		String nodeId = super.upload(owner, filename, mimeType, parentDirId, input);
		this.addPermission(owner, nodeId, BasePermission.WRITE.getMask());
		this.addPermission(owner, nodeId, BasePermission.READ.getMask());
		
		return nodeId;
	}
	
	@PreAuthorize("hasPermission(#nodeId, 'com.googlecode.doce.model.Node', 'READ')")
	public InputStream download(String nodeId) throws DoceException {
		return super.download(nodeId);
	}
	
	@PreAuthorize("hasPermission(#nodeId, 'com.googlecode.doce.model.Node', 'READ')")
	public Node queryNode(String nodeId) throws DoceException {
		return super.queryNode(nodeId);
	}
	
	@PostAuthorize("hasPermission(returnObject, 'READ')")
	public Node queryNodeByPath(String path) throws DoceException {
		return super.queryNodeByPath(path);
	}
	
	@PostFilter("hasPermission(filterObject, 'READ')")
	public List<Node> queryChildNodes(String parentDirId) throws DoceException {
		return super.queryChildNodes(parentDirId);
	}
	
	@PreAuthorize("hasPermission(#nodeId, 'com.googlecode.doce.model.Node', 'WRITE')")
	public void deleteNode(String nodeId) throws DoceException {
		super.deleteNode(nodeId);
	}
	
	public void bulkDeleteNodes(List<String> nodeIds) throws DoceException {
    	for(String nodeId : nodeIds) {
    		((SecuredDoceServiceImpl)(AopContext.currentProxy())).deleteNode(nodeId);
    	}
    }
	
	@PreAuthorize("hasPermission(#nodeId, 'com.googlecode.doce.model.Node', 'WRITE')")
	public void unDeleteNode(String nodeId) throws DoceException {
        super.unDeleteNode(nodeId);
    }
	
	public void bulkUnDeleteNodes(List<String> nodeIds) throws DoceException {
		for(String nodeId : nodeIds) {
    		((SecuredDoceServiceImpl)(AopContext.currentProxy())).unDeleteNode(nodeId);
    	}
	}
	
	@PostFilter("hasPermission(filterObject, 'READ')")
	public List<Node> queryDeletedNodes(String owner) throws DoceException {
		return super.queryDeletedNodes(owner);
	}
	
	@PreAuthorize("hasPermission(#nodeId, 'com.googlecode.doce.model.Node', 'WRITE')")
	public void permanentDeleteNode(String nodeId) throws DoceException {
		super.permanentDeleteNode(nodeId);
		
		deleteAcl(nodeId, true);
	}
	
	public void bulkPermanentDeleteNodes(List<String> nodeIds) throws DoceException {
    	for(String nodeId : nodeIds)
    		((SecuredDoceServiceImpl)(AopContext.currentProxy())).permanentDeleteNode(nodeId);
    }
	
	@PreAuthorize("hasPermission(#nodeId, 'com.googlecode.doce.model.Node', 'WRITE') and hasPermission(#parentDirId, 'com.googlecode.doce.model.Node', 'WRITE')")
	public void moveNode(String nodeId, String parentDirId) throws DoceException {
		super.moveNode(nodeId, parentDirId);
	}
	
	@PreAuthorize("hasPermission(#nodeId, 'com.googlecode.doce.model.Node', 'WRITE') and hasPermission(#parentDirId, 'com.googlecode.doce.model.Node', 'WRITE')")
	public void copyNode(String nodeId, String parentDirId) throws DoceException {
		super.copyNode(nodeId, parentDirId);
	}
	
	@PreAuthorize("hasPermission(#nodeId, 'com.googlecode.doce.model.Node', 'WRITE')")
	public void renameNode(String nodeId, String name) throws DoceException {
		super.renameNode(nodeId, name);
	}
	
	public List<String> sharedNode(String nodeId, List<String> receivers) throws DoceException {
    	return this.sharedNode(nodeId, receivers, OWNER_ROOT_NODE_ID);
    }
	
    public List<String> sharedNode(String nodeId, List<String> receivers, String parentDirId) throws DoceException {
    	List<String> nodeIds = super.sharedNode(nodeId, receivers, parentDirId);
    	
    	int len = nodeIds.size();
    	for(int i = 0; i < len; i++) {
    		this.addPermission(receivers.get(i), nodeIds.get(i), BasePermission.READ.getMask());
    	}
    	
    	return nodeIds;
    }
    
    public List<String> unSharedNode(String nodeId, List<String> receivers) throws DoceException {
    	List<String> nodeIds = super.unSharedNode(nodeId, receivers);
    
    	for(String id : nodeIds) {
    		this.deleteAcl(id, true);
    	}
    	return nodeIds;
	}
	
	public void addPermission(String recipient, String nodeId, Integer mask) {
		PrincipalSid sid = new PrincipalSid(recipient);
        Permission permission = permissionFactory.buildFromMask(mask);
        
        MutableAcl acl;
        ObjectIdentity oid = new ObjectIdentityImpl(Node.class, nodeId);

        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }

        acl.insertAce(acl.getEntries().size(), permission, sid, true);
        mutableAclService.updateAcl(acl);

        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("Added permission " + permission + " for Sid " + recipient + " node: " + nodeId);
        }
	}
	
	public void deletePermission(String recipient, String nodeId, int mask) {
        Sid sidObject = new PrincipalSid(recipient);
        Permission permission = permissionFactory.buildFromMask(mask);

        ObjectIdentity oid = new ObjectIdentityImpl(Node.class, nodeId);
        MutableAcl acl = (MutableAcl) mutableAclService.readAclById(oid);

        List<AccessControlEntry> entries = acl.getEntries();

        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getSid().equals(sidObject) && entries.get(i).getPermission().equals(permission)) {
                acl.deleteAce(i);
            }
        }

        mutableAclService.updateAcl(acl);

        if (LOGGER.isDebugEnabled()) {
        	LOGGER.debug("Deleted node: " + nodeId + " ACL permissions for recipient " + sidObject);
        }
    }
	
	private void deleteAcl(String nodeId, boolean deleteChildren) {
		ObjectIdentity oid = new ObjectIdentityImpl(Node.class, nodeId);
		mutableAclService.deleteAcl(oid, true);
	}
	
	private void checkParentDirPermission(String recipient, String parentDirId) {
		Sid sidObject = new PrincipalSid(recipient);
		ObjectIdentity oid = new ObjectIdentityImpl(Node.class, parentDirId);
        MutableAcl acl = (MutableAcl) mutableAclService.readAclById(oid);

        List<AccessControlEntry> entries = acl.getEntries();
        
        boolean haveWritePermission = false;
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getSid().equals(sidObject) && entries.get(i).getPermission().equals(BasePermission.WRITE)) {
            	haveWritePermission = true;
            	break;
            }
        }
        
        if(!haveWritePermission)
        	throw new AccessDeniedException("No write access to parent directory");
	}
}
