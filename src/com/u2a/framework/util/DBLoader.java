package com.u2a.framework.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;


import freemarker.cache.TemplateLoader;

public class DBLoader implements TemplateLoader {

	public void closeTemplateSource(Object arg0) throws IOException {

	}


	public long getLastModified(Object arg0) {
		Date d= new Date();
		return d.getTime();
	}

	public Reader getReader(Object arg0, String arg1) throws IOException {
		return new StringReader((String)arg0);
	}


	public Object findTemplateSource(String arg0) throws IOException {
		return null;
		
				//TODO自动生成方法体文档 return null;
			
	}

}
