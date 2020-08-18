package com.u2a.log.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.brick.exception.BrickException;
import com.brick.util.Util;
/**
 * 事件加载类
 * @author 王冲
 * @date 2010-9-26 14:25:31
 */
@SuppressWarnings("unchecked")
public class LoadEventManager {
	static Logger logger = Logger.getLogger(LoadEventManager.class.getName());
	private List<EventBean> events = new ArrayList<EventBean>();
	private static LoadEventManager LoadEventManager = new LoadEventManager();
	private LoadEventManager(){}
	public static LoadEventManager getLoadEventManager(){
		return LoadEventManager;
	}
	/**
	 * 多events加载events xml
	 * @param xmlPath
	 */
	
	public void loadEvents(String xmlPath){
		SAXBuilder sax = new SAXBuilder();
		try {
			Document document =  sax.build(Util.fileExist(xmlPath));
			Element root = document.getRootElement();
			List<Element> beanList = root.getChildren("event");
			//加载
			for(Element bean : beanList){
				String location = bean.getAttributeValue("location");
				System.out.println("加载event: "+location);
				loadBeanConfig(location);
			}
		} catch (Exception e) {
			logger.error("loadEvents错误！ ", e);
			throw new BrickException("loadEvents错误！ ",e);
		}
	}
	/**
	 * 销毁方法
	 */
	public void destory(){
		List<EventBean> keys  = new ArrayList<EventBean>();
		for(EventBean event : events){
			keys.add(event);
		}
		/**
		 * 防报异常：java.util.ConcurrentModificationException
		 * 
		 */
		for(EventBean key : keys){
			events.remove(key);
		}
	}
	/**
	 * 加载event xml
	 * @param xmlPath
	 */
	protected void loadBeanConfig(String xmlPath){
		SAXBuilder sax = new SAXBuilder();
		try {
			Document document = sax.build(Util.fileExist(xmlPath));
			//解析xml
			parseEventXML(document);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(xmlPath+" 文件解析错误", e);
			throw new BrickException(xmlPath+" 文件解析错误!"+e.getMessage());
		}
	}
	/**
	 * 解析事件Event配置XML
	 * @param document
	 */
	protected void parseEventXML(Document document){
		Element root = document.getRootElement();
		List<Element> events = root.getChildren("event");
		for(Element event : events){
			EventBean eventBean = new EventBean();
			String id = event.getAttributeValue("id");
			String method = event.getAttributeValue("method");
			String delay = event.getAttributeValue("delay");
			eventBean.setId(id);
			eventBean.setMethodName(method);
			eventBean.setDelay(delay);
			/*List<Element> properties = event.getChildren("property");
			for(Element property : properties){
				String name = property.getAttributeValue("name");
				eventBean.addProperty(name);
			}*/
			this.events.add(eventBean);
		}
	}
	public void fillProperty(){
		
	}
	/**
	 * 根据ID获得事件event
	 * @param id
	 * @return
	 */
	public List<EventBean> getEvent(String id){
		if(id==null || "".equals(id)) return new ArrayList<EventBean>();
		List<EventBean> beans = new ArrayList<EventBean>();
		for(EventBean bean : this.events){
			if(id.equals(bean.getId())){
				beans.add(bean);
			}
		}
		return beans;
	}
}
