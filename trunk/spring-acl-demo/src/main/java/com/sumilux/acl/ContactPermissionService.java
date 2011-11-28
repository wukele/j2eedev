package com.sumilux.acl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class ContactPermissionService {
	private static Logger logger = LoggerFactory.getLogger(ContactPermissionService.class);
	
	private final PermissionFactory permissionFactory = new DefaultPermissionFactory();
	
	@Autowired
	private MutableAclService mutableAclService;
	
	public void addPermission(String recipient, Integer mask, Contact contact) {
		
		PrincipalSid sid = new PrincipalSid(recipient);
        Permission permission = permissionFactory.buildFromMask(mask);
        
        MutableAcl acl;
        ObjectIdentity oid = new ObjectIdentityImpl(Contact.class, contact.getId());

        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }

        acl.insertAce(acl.getEntries().size(), permission, sid, true);
        mutableAclService.updateAcl(acl);

        logger.debug("Added permission " + permission + " for Sid " + recipient + " contact " + contact);
	}
	
	public void deletePermission(Contact contact, String recipient, int mask) {
        Sid sidObject = new PrincipalSid(recipient);
        Permission permission = permissionFactory.buildFromMask(mask);

        ObjectIdentity oid = new ObjectIdentityImpl(Contact.class, contact.getId());
        MutableAcl acl = (MutableAcl) mutableAclService.readAclById(oid);

        List<AccessControlEntry> entries = acl.getEntries();

        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getSid().equals(sidObject) && entries.get(i).getPermission().equals(permission)) {
                acl.deleteAce(i);
            }
        }

        mutableAclService.updateAcl(acl);

        if (logger.isDebugEnabled()) {
            logger.debug("Deleted contact " + contact + " ACL permissions for recipient " + sidObject);
        }
    }
	
	public void deleteAcl(Contact contact, boolean deleteChildren) {
		ObjectIdentity oid = new ObjectIdentityImpl(Contact.class, contact.getId());
		mutableAclService.deleteAcl(oid, true);
	}

	
	public void create(Contact contact) {
        addPermission(getUsername(), BasePermission.ADMINISTRATION.getMask(), contact);

        if (logger.isDebugEnabled()) {
            logger.debug("Created contact " + contact + " and granted admin permission to recipient " + getUsername());
        }
    }
	
	public void delete(Contact contact) {
        ObjectIdentity oid = new ObjectIdentityImpl(Contact.class, contact.getId());
        mutableAclService.deleteAcl(oid, false);

        if (logger.isDebugEnabled()) {
            logger.debug("Deleted contact " + contact + " including ACL permissions");
        }
    }
	
	public String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        } else {
            return auth.getPrincipal().toString();
        }
    }
}
