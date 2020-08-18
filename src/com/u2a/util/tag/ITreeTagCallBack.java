package com.u2a.util.tag;

import java.util.List;

import com.brick.data.IMap;
/**
 * 树tag回调接口
 * @author wumq
 *
 */
public interface ITreeTagCallBack {
	/**
	 * 解析节点
	 * @param parentId 父节点id
	 * @param item 具体项目
	 * @return
	 */
	public String nodeParser(String rootPath,String parentId,IMap item);  
	/**
	 * 获取当前id
	 * @param item 具体项目
	 * @return
	 */
	public String getId(IMap item); 
	/**
	 * 获取id在map中的名称
	 * @return
	 */
	public String getIdName();  
	/**
	 * 获取子节点list
	 * @param item
	 * @return
	 */
	public List getChildList(IMap item);  
	/**
	 * 获取子节点在map中的名称
	 * @return
	 */
	public String getChildListName();  
}
