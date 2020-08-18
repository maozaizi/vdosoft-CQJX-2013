package com.u2a.util.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import com.brick.data.IMap;
/**
 * 流程管理树
 * 系统名称：新疆生产建设兵团消防网络化信息管理系统   
 * 类名称：ProcessManageTree   
 * 类描述：   
 * 创建人：gaoy 
 * 创建时间：Mar 26, 2012 10:01:26 AM
 */
public class ProcessManageTree extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	
	private List<IMap> list ;
	
	private String parentId;
	
	public List<IMap> getList() {
		return list;
	}

	public void setList(List<IMap> list) {
		this.list = list;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	

	public int doStartTag() throws JspException {
					HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
					createTree(this.getList(),this.getParentId(),request);
					return super.doStartTag();
	}
	
	public void createTree(List<IMap> list,String id,HttpServletRequest request ){
		for(IMap item : list){
  			String node = "tree.N[\""+id+"_"+item.get("processId")+"\"] = \"T:<span title="+item.get("folderName")+">"+item.get("folderName")+"</span>;target:rightProcess;url:"+request.getContextPath()+"/api/workflow/getProcessInfo?processId="+item.get("processId")+"&tempPat=0;C:goOperator('"+item.get("processId")+"','"+item.get("parentId")+"',"+item.get("flag")+")\";";
  			printTree(node);
  			List<IMap> subItem = (List<IMap>)(item.get("childList"));
  			List<IMap> waitPrintNodeList = new ArrayList<IMap>();
  			if(subItem != null && subItem.size() != 0){
  				for(IMap subNode:subItem){
  					waitPrintNodeList.add(subNode);
  				}
  			}
  			createTree(waitPrintNodeList,item.get("processId").toString(),request);
		}
	}
	
	public void printTree(String text){
		try {
			TagUtils.getInstance().write(this.pageContext, text);
		} catch (JspException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
