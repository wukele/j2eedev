package com.googlecode.doce.utils;

import com.googlecode.doce.service.PathException;

/**
 * 
 * @author binsongl
 *
 */
public class PathUtils {
	
	public static String checkPath(String path) throws PathException {
		if( !path.startsWith("/") || "/".equals(path) || path.contains("//") ) {
			throw new PathException("path is not correct: " + path);
		} else {
			if( path.endsWith("/") ) {
				path = path.substring(0, (path.length() - 1));
			}
		}
		
		return path;
	}
	
	public static String getParentPath(String path) throws PathException {
		path = checkPath(path);
		
		int index = path.lastIndexOf("/");
		if(index != 0) {
			path = path.substring(0, index);
		
			return path;
		} else {
			return null;
		}
	}
	
	public static String getNodeName(String path) throws PathException {
		path = checkPath(path);
		int index = path.lastIndexOf("/");
		
		return path.substring(index + 1);
	}
	
	public static String[] getNodeNames(String path) throws PathException {
		path = checkPath(path);
		path = path.substring(1);
		
		return path.split("/");
	}
	
	public static void main(String[] args) throws PathException {
		String path = "/test/sdf/";
		System.out.println(PathUtils.getNodeNames(path));
		//System.out.println(PathUtils.getNodeName(path));
	}
}
