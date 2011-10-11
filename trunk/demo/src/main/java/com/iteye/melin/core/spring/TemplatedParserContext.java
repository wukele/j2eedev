package com.iteye.melin.core.spring;

import org.springframework.expression.ParserContext;

/**
 *
 * @datetime 2010-8-25 上午11:23:09
 * @author libinsong1204@gmail.com
 */
public class TemplatedParserContext implements ParserContext {

	public String getExpressionPrefix() {
		return "${";
	}

	public String getExpressionSuffix() {
		return "}";
	}

	public boolean isTemplate() {
		return true;
	}
}
