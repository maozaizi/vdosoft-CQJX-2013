package com.u2a.util.tag;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import com.brick.data.IMap;
import com.u2a.framework.commons.CodeTreeHelper;

public class BaseOrganizationTree extends TagSupport {

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
		createTree(this.getList(),this.getParentId());
		return super.doStartTag();
	}
	
	public void createTree(List<IMap> list,String id){
		for(IMap item : list){
			String baseOrganizationId = item.get("baseOrganizationId").toString();
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
			if(baseOrganizationId.length() == 4){
				String node = "tree.N[\""+id+"_"+baseOrganizationId+"\"] = \"T:<span title="+item.get("baseOrganizationName")+">"+item.get("baseOrganizationName")+"</span>;C:goOperator('"+item.get("baseOrganizationId")+"','"+item.get("parentId")+"','"+item.get("baseOrganizationName")+"','"+item.get("iLevel")+"','"+item.get("id")+"');icon:"+icon+"\";";
				printTree(node);
			}else{
				String node = "tree.N[\""+CodeTreeHelper.getParentId(baseOrganizationId)+"_"+item.get("baseOrganizationId")+"\"] = \"T:<span title="+item.get("baseOrganizationName")+">"+item.get("baseOrganizationName")+"</span>;C:goOperator('"+item.get("baseOrganizationId")+"','"+item.get("parentId")+"','"+item.get("baseOrganizationName")+"','"+item.get("iLevel")+"','"+item.get("id")+"');icon:"+icon+"\";";
				printTree(node);
			}
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
