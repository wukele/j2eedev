package com.iteye.melin.core.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 *
 * @author   libinsong1204@gmail.com
 * @Date	 2011-6-14 下午01:41:33
 */
public class CustomSQL {
	private static Logger logger = LoggerFactory.getLogger(CustomSQL.class);

	private Map<String, SQLBean> _sqlPool;

	private static final String GROUP_BY_CLAUSE = " GROUP BY ";
	private static final String ORDER_BY_CLAUSE = " ORDER BY ";
	private static final String STRING_SPACE = " ";
	
	private final SAXReader saxReader = new SAXReader();
	private Configuration configuration = null;
	private StringTemplateLoader stringTemplateLoader = null;

	public CustomSQL() throws SQLException {

		_sqlPool = new HashMap<String, SQLBean>();

		try {
			ClassLoader classLoader = getClass().getClassLoader();
			configuration = new Configuration();  
			stringTemplateLoader = new StringTemplateLoader();
	        configuration.setDefaultEncoding("UTF-8");  

			String[] configs = getConfigs();
			for (String _config : configs) {
				read(classLoader, _config);
			}
			
	        configuration.setTemplateLoader(stringTemplateLoader);  
		}
		catch (Exception e) {
			logger.error("", e);
		}
		
	}
	
	protected String[] getConfigs() {
		return new String[] {"custom-sql/default.xml"};
	}

	public String appendCriteria(String sql, String criteria) {
		if (StringUtils.isBlank(criteria)) {
			return sql;
		}

		if (!criteria.startsWith(STRING_SPACE)) {
			criteria = STRING_SPACE.concat(criteria);
		}

		if (!criteria.endsWith(STRING_SPACE)) {
			criteria = criteria.concat(STRING_SPACE);
		}

		int pos = sql.indexOf(GROUP_BY_CLAUSE);

		if (pos != -1) {
			return sql.substring(0, pos + 1).concat(criteria).concat(
				sql.substring(pos + 1));
		}

		pos = sql.indexOf(ORDER_BY_CLAUSE);

		if (pos != -1) {
			return sql.substring(0, pos + 1).concat(criteria).concat(
				sql.substring(pos + 1));
		}

		return sql.concat(criteria);
	}

	public String get(String id) {
		SQLBean bean = _sqlPool.get(id);
		if("simple".equals(bean.getTempateType())) {
			return _sqlPool.get(id).getContent();
		} else
			throw new RuntimeException("SQL 模板类型不正确，只可以是simple类型");
	}
	
	public String get(String id, Map<String, Object> model) {
		try {
			Template template = configuration.getTemplate(id);      
			StringWriter writer = new StringWriter();      
			template.process(model, writer); 
			return writer.toString();
        } catch (TemplateException e) {  
            throw new RuntimeException("Parse sql failed", e);  
        } catch (IOException e) {  
            throw new RuntimeException("Parse sql failed", e);  
        }  
	}

	protected void read(ClassLoader classLoader, String source)
		throws Exception {

		InputStream is = classLoader.getResourceAsStream(source);

		if (is == null) return;

		logger.info("Loading " + source);

		Document document = saxReader.read(is);
		Element rootElement = document.getRootElement();

		for (Object sqlObj : rootElement.elements("sql")) {
			Element sqlElement = (Element)sqlObj;
			String file = sqlElement.attributeValue("file");

			if (StringUtils.isNotBlank(file)) {
				read(classLoader, file);
			} else {
				String id = sqlElement.attributeValue("id");
				String sqlType = sqlElement.attributeValue("sqlType");
				String tempateType = sqlElement.attributeValue("tempateType");
				
				if("simple".equals(tempateType) || "freeMarker".equals(tempateType)) {
					String content = transform(sqlElement.getText());
					
					SQLBean bean = new SQLBean();
					bean.setTempateType(tempateType);
					bean.setSqlType(sqlType);
					bean.setContent(content);
					
					if("freeMarker".equals(tempateType))
						stringTemplateLoader.putTemplate(id, content);
					
					_sqlPool.put(id, bean);
				} else {
					logger.warn("{} 对应 tempateType 值 {} 不正确，可选值为：simple和freeMarker", id, sqlType);
				}
			}
		}
	}

	protected String transform(String sql) {
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader bufferedReader =
				new BufferedReader(new StringReader(sql));

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line.trim());
				sb.append(STRING_SPACE);
			}

			bufferedReader.close();
		}
		catch (IOException ioe) {
			return sql;
		}

		return sb.toString();
	}
	
	public static class SQLBean {
		/**
		 * 两种可选类型：simple和freeMarker
		 */
		private String tempateType = "simple";
		/**
		 * 两种可选类型：SQL和HQL
		 */
		private String sqlType = "SQL";
		private String content = "";
		
		public String getTempateType() {
			return tempateType;
		}
		public void setTempateType(String tempateType) {
			this.tempateType = tempateType;
		}
		public String getSqlType() {
			return sqlType;
		}
		public void setSqlType(String sqlType) {
			this.sqlType = sqlType;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		
	}

}