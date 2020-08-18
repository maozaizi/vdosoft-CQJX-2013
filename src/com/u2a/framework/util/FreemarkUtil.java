package com.u2a.framework.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkUtil {
	private static final Log log = LogFactory.getLog(FreemarkUtil.class);
	private static Configuration getConfiguration(String filePath){
		Configuration cfg=new Configuration();
		cfg.setNumberFormat("0.##");
		//cfg.setTemplateLoader(new DBLoader());
		FileTemplateLoader templateLoader;
		try {
			templateLoader = new FileTemplateLoader(new File(filePath));
			cfg.setTemplateLoader(templateLoader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cfg.setOutputEncoding("UTF-8");
		cfg.setClassicCompatible(true);
		cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
		return cfg;
	}

	public static String transform(String filePath,String filename, Map<String, Object> args) {
		
		Template template = null;
		StringWriter stringWriter = null;
		try {
			 template = getConfiguration(filePath).getTemplate(filename,"UTF-8");
			 stringWriter = new StringWriter();
			 template.process(args, stringWriter);
			 String body = stringWriter.toString();

			if (log.isDebugEnabled()) {
				log.debug("\n生成HTML片段:\n" + body);
			}
			return body;
		} catch (IOException e) {
			log.error(e);
			throw new RuntimeException("消息模板文件找不到!", e);
		} catch (TemplateException e) {
			log.error(e);
			throw new RuntimeException("消息模板转换时出错!", e);
		} finally {
			if (stringWriter != null) {
				try {
					stringWriter.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}

	}
	public static void main(String[] args) throws IOException {
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("applyUnitName", "a");
		maps.put("buildingName", "a");
		File file=new File("c:\\agreedAccept.ftl");
		System.out.println();
		System.out.println(file.getPath().substring(0, file.getPath().lastIndexOf(file.getName())));
		System.out.println();
		//System.out.println(FreemarkUtil.transform(file,"agreedAccept.ftl", maps));
	}
}
