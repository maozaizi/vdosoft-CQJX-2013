package com.u2a.util.tag;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import com.brick.data.IMap;

public class OrganizationDetaliTree extends TagSupport {

	/**
	 * 
	 */
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
		HttpServletRequest request = (HttpServletRequest) pageContext
		.getRequest();
		createTree(this.getList(),this.getParentId(),request);
		return super.doStartTag();
	}
	
	public void createTree(List<IMap> list,String id,HttpServletRequest request){
		for(IMap item : list){

			BigDecimal iLevel = (BigDecimal)item.get("iLevel");
			String icon = "";
			switch (iLevel.intValue()) {
			case 1:
				icon = "bing";
				break;
			case 2:
				icon = "shi";
				break;
			case 3:
				icon = "ken";
				break;
			case 4:
				icon = "tuan";
				break;
			}
			String node = "tree.N[\""+id+"_"+item.get("organizationDetailId")+"\"] = \"T:<span title="+item.get("organizationDetailName")+">"+item.get("organizationDetailName")+"</span>;target:app_rightFrame;url:"+request.getContextPath()+"/api/organizationdetali/getFirePreventionCommitteeInfo?organizationDetailId="+item.get("organizationDetailId")+";C:goOperator('"+id+"','"+item.get("organizationDetailId")+"','"+item.get("organizationDetailName")+"','"+item.get("baseOrganizationId")+"','"+item.get("type")+"');icon:"+icon+"\";";
			printTree(node);
			List<IMap> subItem = (List<IMap>)(item.get("childList"));
			List<IMap> waitPrintNodeList = new ArrayList<IMap>();
			if(subItem != null && subItem.size() != 0){
				for(IMap subNode:subItem){
					waitPrintNodeList.add(subNode);
				}
			}
			createTree(waitPrintNodeList,item.get("organizationDetailId").toString(),request);
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
