package com.starit.cache;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * 
 * @author  libinsong1204@gmail.com
 * @date    2011-1-18 上午11:03:34
 * @version 
 */
public class JsonTranscoder {
	private final ObjectMapper mapper;
	
	public JsonTranscoder() {
		this.mapper = new ObjectMapper();
	}

	public String serialize(Object o) throws Exception{
		return mapper.writeValueAsString(o);
	}
	
	@SuppressWarnings("unchecked")
	public Object deserialize(String in, Class clazz) throws Exception {
		return mapper.readValue(in, clazz);
	}
}
