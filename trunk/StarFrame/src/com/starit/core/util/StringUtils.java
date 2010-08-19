package com.starit.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @datetime 2010-8-19 下午07:17:36
 * @author libinsong1204@gmail.com
 */
public class StringUtils {

	//~ Instance fields ================================================================================================

	//~ Methods ========================================================================================================
	/**
	 * 逗号相连的数字字符串，使用逗号分隔开，转为List存放到List容器中
	 * 
	 * @param
	 * @return list
	 */
	public static List<Long> convertStrToLongList(String str) {
		List<Long> list = new ArrayList<Long>();
		String[] arrs = org.apache.commons.lang.StringUtils.split(str, ",");
		for(String value : arrs) {
			if(NumberUtils.isDigits(value))
				list.add(Long.valueOf(value));
		}
		return list;
	}
}
