package com.u2a.log.service;

import java.util.ArrayList;
import java.util.List;

import com.brick.exception.BusinessException;

/**
 * 事件配置Bean
 * @author 王冲
 * @date 2010-9-25 14:58:00
 */
public class EventBean {
	//事件ID
	private String id;
	//事件类名称
	private String clazz;
	//方法名
	private String methodName;
	
	private Long delay;
	//事件参数
	private List<String> properties = new ArrayList<String>();
	public String getId() {
		return id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final EventBean other = (EventBean) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public void setId(String id) {
		if(id==null || "".equals(id)){
			throw new BusinessException("event id not null");
		}
		this.id = id;
	}
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		if(clazz==null || "".equals(clazz)){
			throw new BusinessException("event class not null");
		}
		this.clazz = clazz;
	}
	public List<String> getProperties() {
		return properties;
	}
	public void addProperty(String property){
		properties.add(property);
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		if(methodName==null || "".equals(methodName)){
			throw new BusinessException("event methodName not null");
		}
		this.methodName = methodName;
	}
	public Long getDelay() {
		return delay;
	}
	public void setDelay(String delay) {
		if(delay!=null && !"".equals(delay)){
			this.delay = Long.parseLong(delay);
		}
	}
}
