package com.googlecode.doce.persistence;

import static com.googlecode.doce.DoceConstants.NODE_STATUS_ENABLED;
import static com.googlecode.doce.DoceConstants.NODE_STATUS_SHARED;

import java.util.Date;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.googlecode.doce.model.Node;
import com.googlecode.hibernatedao.HibernateBaseDaoImpl;

@Repository
@SuppressWarnings("unchecked")
public class DocePersistenceManagerImpl extends HibernateBaseDaoImpl<Node, String> implements DocePersistenceManager {
	
	public Node queryNode(String nodeId) {
		String hql = "from Node where (currentStatus = ? or currentStatus = ?) AND nodeId = ?";
		
		List<Node> list = this.findByHQL(hql, NODE_STATUS_ENABLED, NODE_STATUS_SHARED, nodeId);
		
		if(list.size() == 0)
			throw new EmptyResultDataAccessException(1);
		else
			return list.get(0);
		
	}

	public Node queryNodeByName(String parentDirId, String name) {
		String hql = "from Node where (currentStatus = ? or currentStatus = ?) AND nodeName = ? AND parentDirId = ?";
        
        List<Node> list = this.findByHQL(hql, NODE_STATUS_ENABLED, NODE_STATUS_SHARED, name, parentDirId);
		
		if(list.size() == 0)
			throw new EmptyResultDataAccessException(1);
		else
			return list.get(0);
	}

	public List<Node> queryChildNodes(String parentDirId) {
		String hql = "from Node where (currentStatus = ? or currentStatus = ?) AND parentDirId = ? ";
        
        return this.findByHQL(hql, NODE_STATUS_ENABLED, NODE_STATUS_SHARED, parentDirId);
	}

	public void moveNode(String nodeId, String parentDirId) {
		String hql = "from Node where nodeId = ?";
		
		List<Node> list = this.findByHQL(hql, nodeId);
		Node node = list.get(0);
		node.setParentDirId(parentDirId);
		node.setModified(new Date());
		this.getHibernateTemplate().update(node);
	}

	public boolean existNodeName(String parentDirId, String name, int nodeType) {
		String hql = "from Node where (currentStatus = ? OR currentStatus = ?) AND nodeName = ? " +
				"AND parentDirId = ? and nodeType = ?";
		
		int count = this.findByHQL(hql, NODE_STATUS_ENABLED, NODE_STATUS_SHARED, name, parentDirId, nodeType).size();
		
		if(count > 0)
		    return true;
		else
		    return false;
	}

	public void renameNode(String nodeId, String name) {
		String hql = "from Node where nodeId = ?";
		
		List<Node> list = this.findByHQL(hql, nodeId);
		Node node = list.get(0);
		node.setNodeName(name);
		node.setModified(new Date());
		this.getHibernateTemplate().update(node);
	}

	public int querySharedNodeCount(String sharder, String nodeId) {
		throw new IllegalAccessError("no use");
	}

	public void unSharedNode(String sharder, String receiver, String nodeId) {
		String hql = "delete from SharedNode where nodeId = ? AND receiver = ? AND sharder = ?";
        
		this.bulkUpdate(hql, nodeId, receiver, sharder);
	}

	public List<Node> querySharedNodes(String sharder, String nodeId) {
		String hql = "select distinct a from Node a, SharedNode b where a.nodeId = b.nodeId " +
		"AND b.nodeId = ? AND b.sharder = ?";

		return this.findByHQL(hql, nodeId, sharder);
	}

	public List<Node> queryReceivedSharedNodes(String sharder, String receiver, String nodeId) {
		String hql = "select distinct a from Node a, SharedNode b where a.nodeId = b.nodeId " +
		"AND b.nodeId = ? AND b.receiver = ? and sharder = ?";

		return this.findByHQL(hql, nodeId, receiver, sharder);
	}

}
