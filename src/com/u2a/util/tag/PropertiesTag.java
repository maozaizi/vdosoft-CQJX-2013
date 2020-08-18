package com.u2a.util.tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

public class PropertiesTag extends TagSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 默认config配置文件 config*/
	private String source;
	private String key;
	private String other;
	/** 请求默认值 */
	private HttpServletRequest request;
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	@SuppressWarnings("static-access")
	@Override
	public int doStartTag() throws JspException {
		if(source==null || "".equals(source)) source = "config";		
		ServletContext context = this.pageContext.getServletContext();
		String contextClasses = context.getRealPath("")+"/WEB-INF/classes/";
		String propertiesPath = "com/hopsun/resources/"+this.source+".properties";
		if(other!=null){
			propertiesPath = other.replace(".", "/")+".properties";
		}
		File file = new File(contextClasses+propertiesPath);
		String name = null;
		if(file.exists()){
			try {
				FileInputStream input = new FileInputStream(file);
				Properties prop = new Properties();
				prop.load(input);
				name = prop.getProperty(key);
				TagUtils.getInstance().write(this.pageContext, name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return super.SKIP_BODY;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}