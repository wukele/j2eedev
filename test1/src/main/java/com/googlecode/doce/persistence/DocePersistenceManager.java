package com.googlecode.doce.persistence;

import java.util.List;

import com.googlecode.doce.model.Node;
import com.googlecode.hibernatedao.HibernateBaseDao;

/**
 * 
 * @author binsongl
 *
 */
public interface DocePersistenceManager extends HibernateBaseDao<Node, String> {
    
    public Node queryNode(String nodeId);
    
    public Node queryNodeByName(String parentDirId, String name);
    
    public List<Node> queryChildNodes(String parentDirId);
    
    public void moveNode(String nodeId, String parentDirId);
    
    public boolean existNodeName(String parentDirId, String name, int nodeType);
    
    public void renameNode(String nodeId, String name);
    
    public int querySharedNodeCount(String sharder, String nodeId);

    public void unSharedNode(String sharder, String receiver, String nodeId);
}
