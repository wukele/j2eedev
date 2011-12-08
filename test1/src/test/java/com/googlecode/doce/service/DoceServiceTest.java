package com.googlecode.doce.service;

import static com.googlecode.doce.DoceConstants.OWNER_ROOT_NODE_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.googlecode.doce.DoceException;
import com.googlecode.doce.model.Node;

/**
 * 
 * @author libinsong1204@gmail.com
 * 
 */
@ContextConfiguration(locations={"classpath:META-INF/doce.xml", "classpath:META-INF/security-acl.xml"})
public class DoceServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private DoceService doceService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	private static boolean auth = false;

	@Before
	public void before() {
		if(!auth) {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					"test", "000000");
			SecurityContextHolder.getContext().setAuthentication(
					authenticationManager.authenticate(authentication));
			
			auth = true;
		}
	}

	@Test
	public void testCreateFolder() throws DoceException {
		String nodeId = doceService.createFolder("test", "new folder",
				OWNER_ROOT_NODE_ID);

		assertNotNull(doceService.queryNode(nodeId));
	}

	@Test
	public void testUpload() throws DoceException, InterruptedException {
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream("./uploadfile.doc");

		doceService.upload("test", "uploadfile.doc", "application/msword",
				OWNER_ROOT_NODE_ID, inputStream);

		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		assertEquals(1, list.size());
	}

	@Test
	public void testDownLoad() throws DoceException {
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream("./uploadfile.txt");

		doceService.upload("test", "uploadfile.text", "text/html",
				OWNER_ROOT_NODE_ID, inputStream);

		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		assertEquals(1, list.size());
	}

	@Test
	public void testDeleteNode() throws DoceException {
		doceService.createFolder("test", "new folder", OWNER_ROOT_NODE_ID);

		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		doceService.deleteNode(list.get(0).getNodeId());

		list = doceService.queryDeletedNodes("test");
		assertEquals(1, list.size());

		list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		assertEquals(0, list.size());
	}

	@Test
	public void testQueryNodeByPath() throws DoceException {
		doceService.createFolder("test", "doce", OWNER_ROOT_NODE_ID);

		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		doceService.createFolder("test", "test", list.get(0).getNodeId());

		Node node = doceService.queryNodeByPath("/doce/test");
		assertEquals("test", node.getNodeName());

		node = doceService.queryNodeByPath("/doce/test/");
		assertEquals("test", node.getNodeName());

		queryNodeByErrorPath("/doce12/test");
		queryNodeByErrorPath("/doce/test12");
		queryNodeByErrorPath("doce12/test");
		queryNodeByErrorPath("/doce12/test/test");
	}

	public void queryNodeByErrorPath(String path) throws DoceException {
		try {
			doceService.queryNodeByPath(path);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testUnDeleteNode() throws DoceException {
		doceService.createFolder("test", "new folder", OWNER_ROOT_NODE_ID);

		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		String nodeId = list.get(0).getNodeId();

		doceService.deleteNode(nodeId);
		list = doceService.queryDeletedNodes("test");
		assertEquals(1, list.size());

		list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		assertEquals(0, list.size());

		doceService.unDeleteNode(nodeId);
		list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		assertEquals(1, list.size());
	}

	@Test
	public void testPermanentDeleteNode() throws DoceException {
		doceService.createFolder("test", "new folder", OWNER_ROOT_NODE_ID);

		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		String nodeId = list.get(0).getNodeId();

		doceService.permanentDeleteNode(nodeId);
		list = doceService.queryDeletedNodes("test");
		assertEquals(0, list.size());

		list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		assertEquals(0, list.size());
	}

	@Test
	public void testMoveNode() throws DoceException {
		doceService.createFolder("test", "new folder", OWNER_ROOT_NODE_ID);
		doceService.createFolder("test", "child", OWNER_ROOT_NODE_ID);
		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);

		String parentNodeId = list.get(0).getNodeId();
		Node node = list.get(1);

		doceService.moveNode(node.getNodeId(), parentNodeId);

		list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		assertEquals(1, list.size());
		list = doceService.queryChildNodes(parentNodeId);
		assertEquals(1, list.size());
		assertEquals(node.getNodeName(), list.get(0).getNodeName());
	}

	@Test
	public void testMoveExistNode() throws DoceException {
		doceService.createFolder("test", "new folder", OWNER_ROOT_NODE_ID);
		doceService.createFolder("test", "child", OWNER_ROOT_NODE_ID);
		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);

		String parentNodeId = list.get(0).getNodeId();
		Node node = list.get(1);

		doceService.createFolder("test", "child", parentNodeId);

		try {
			doceService.moveNode(node.getNodeId(), parentNodeId);
			fail();
		} catch (SameNodeNameException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testCopyNode() throws DoceException {
		doceService.createFolder("test", "new folder", OWNER_ROOT_NODE_ID);
		doceService.createFolder("test", "child", OWNER_ROOT_NODE_ID);
		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);

		String parentNodeId = list.get(0).getNodeId();
		Node node = list.get(1);

		doceService.copyNode(node.getNodeId(), parentNodeId);

		list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		assertEquals(2, list.size());
		list = doceService.queryChildNodes(parentNodeId);
		assertEquals(1, list.size());
		assertEquals(node.getNodeName(), list.get(0).getNodeName());
	}

	@Test
	public void testCopyExistNode() throws DoceException {
		doceService.createFolder("test", "new folder", OWNER_ROOT_NODE_ID);
		doceService.createFolder("test", "child", OWNER_ROOT_NODE_ID);
		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);

		String parentNodeId = list.get(0).getNodeId();
		Node node = list.get(1);

		doceService.createFolder("test", "child", parentNodeId);

		try {
			doceService.copyNode(node.getNodeId(), parentNodeId);
			fail();
		} catch (SameNodeNameException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRenameNode() throws DoceException {
		doceService.createFolder("test", "folder", OWNER_ROOT_NODE_ID);
		doceService.createFolder("test", "folder1", OWNER_ROOT_NODE_ID);

		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		String nodeId = list.get(0).getNodeId();

		doceService.renameNode(nodeId, "folder");

		try {
			doceService.renameNode(nodeId, "folder1");
			fail("exist the same name");
		} catch (SameNodeNameException e) {
			assertTrue(true);
		}

		doceService.renameNode(nodeId, "new folder");
		list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		assertEquals("new folder", list.get(0).getNodeName());
	}

	@Test
	public void testSharedNode() throws DoceException {
		doceService.createFolder("test", "folder", OWNER_ROOT_NODE_ID);

		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		String nodeId = list.get(0).getNodeId();

		List<String> receivers = new ArrayList<String>();
		receivers.add("libinsong");
		receivers.add("binsongl");
		doceService.sharedNode(nodeId, receivers);

		list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);

		assertEquals(1, list.size());
	}

	@Test
	public void testExistSharedNode() throws DoceException {
		doceService.createFolder("test", "folder", OWNER_ROOT_NODE_ID);

		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		String nodeId = list.get(0).getNodeId();

		List<String> receivers = new ArrayList<String>();
		receivers.add("libinsong");
		receivers.add("binsongl");
		doceService.sharedNode(nodeId, receivers);

		assertEquals(true,
				doceService.existNodeName(OWNER_ROOT_NODE_ID, "folder", 1));
	}

	@Test
	public void testUnSharedNode() throws DoceException {
		doceService.createFolder("test", "folder", OWNER_ROOT_NODE_ID);

		List<Node> list = doceService.queryChildNodes(OWNER_ROOT_NODE_ID);
		String nodeId = list.get(0).getNodeId();

		List<String> receivers = new ArrayList<String>();
		receivers.add("libinsong");
		receivers.add("binsongl");
		doceService.sharedNode(nodeId, receivers);

		list = doceService.getMySharedNodes("test");
		assertEquals(1, list.size());

		list = doceService.getNodesSharedWithMe("libinsong");
		assertEquals(1, list.size());

		list = doceService.getNodesSharedWithMe("binsongl");
		assertEquals(1, list.size());

		receivers = new ArrayList<String>();
		receivers.add("libinsong");
		doceService.unSharedNode(nodeId, receivers);

		list = doceService.getNodesSharedWithMe("libinsong");
		assertEquals(0, list.size());

		list = doceService.getNodesSharedWithMe("binsongl");
		assertEquals(1, list.size());
	}

}
