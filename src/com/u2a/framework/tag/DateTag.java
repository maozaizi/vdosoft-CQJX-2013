package com.u2a.framework.tag;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class DateTag extends TagSupport {

	protected PageContext	pageContext;

	private String			value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	public PageContext getPageContext() {
		return pageContext;
	}

	public int doStartTag() {
		JspWriter out = this.getPageContext().getOut();
		if(value!=null&&!"".equals(value))
			value=value.substring(0,10);
		try {
			out.print(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
}
