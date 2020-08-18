package com.u2a.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.brick.manager.ContextUtil;

/**
 * 属性文件讲读取类 可绕过context容器 王冲
 */
public class PropUtils<T> {
	private Properties p = new Properties();
	private File file;

	public Properties getP() {
		return p;
	}

	public void setP(Properties p) {
		this.p = p;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * 初始化
	 * 
	 * @param request
	 *            得到绝对路径
	 * @param source
	 *            引用的那一个properties文件
	 */
	public PropUtils(HttpServletRequest request, String source) {
		String filePath = request.getSession().getServletContext().getRealPath(
				"")
				+ "/WEB-INF/config/properties/" + source + ".properties";
		loadFile(filePath);
	}

	/**
	 * 读取文件
	 * @param filePath
	 */
	private void loadFile(String filePath) {
		file = new File(filePath);
		if (file.exists()) {
			FileInputStream input = null;
			//Reader fileread = null;
			try {
				input = new FileInputStream(file);
				//fileread = new InputStreamReader(input, "UTF-8");
				p.load(input);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 重载
	 * 
	 * @param realpath
	 *            项目路径
	 * @param source
	 *            引用的那一个properties文件
	 */
	public PropUtils(String realpath, String source) {
		String filePath = realpath + "/WEB-INF/config/properties/" + source
				+ ".properties";
		loadFile(filePath);
	}

	/**
	 * 重新
	 * 
	 * @param source
	 *            引用的那一个properties文件
	 */
	public PropUtils(String source) {
		String realpath = ContextUtil.servletContext.getRealPath("");
		String filePath = realpath + "/WEB-INF/config/properties/" + source
				+ ".properties";
		loadFile(filePath);
	}

	/**
	 * 读值
	 * 
	 * @param key
	 *            键值
	 * @return
	 */
	public String getProperty(String key) {
		if (p == null)
			return null;
		return p.getProperty(key);
	}

	/**
	 * 得到所有的KEYs
	 * 
	 * @return
	 */
	public Set<Entry<Object, Object>> getKeys() {
//		Set<Entry<Object, Object>> set=p.entrySet();
//        for(Entry<Object, Object> en:set){
//			en.
//		}
		return p.entrySet();
	}

}
