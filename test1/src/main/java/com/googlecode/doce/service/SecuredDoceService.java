package com.googlecode.doce.service;


/**
 * 
 * @author binsongl
 *
 */
public interface SecuredDoceService extends DoceService {
	public void addPermission(String recipient, String nodeId, Integer mask);
	
	public void deletePermission(String recipient, String nodeId, int mask);
}
