package com.u2a.util.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.json.JSONObject;

import org.apache.struts.taglib.TagUtils;

import com.brick.data.DataMap;
import com.brick.data.IMap;

public class GridTree extends TagSupport{
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
					HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
					createTree(this.getList(),this.getParentId(),request);
					return super.doStartTag();
	}
	public void createTree(List<IMap> list,String id,HttpServletRequest request ){
			for(IMap item : list){
				String node = "";
				String checkReason =(String) (item.get("checkReason")==null?"":item.get("checkReason"));
				String checkStandard = (String) (item.get("checkStandard")==null?"":item.get("checkStandard"));
				String checkImportance = (String) (item.get("checkImportance")==null?"":item.get("checkImportance"));
				String checkResult = (String) (item.get("checkResult")==null?"2":item.get("checkResult"));
				String checkDetail = (String) item.get("checkDetail");
				String checkItemId = (String) (item.get("checkItemId")==null?"0": item.get("checkItemId"));
				String checkRemark =(String) (item.get("checkRemark")==null?"":item.get("checkRemark"));
				String tempid = (String) item.get("checkResultId");
				IMap<String, Object> result = new DataMap<String, Object>();
				result.put("checkReason", checkReason);
				result.put("checkStandard", checkStandard);
				result.put("checkResult", checkResult);
				//JSONObject jsonObject = JSONObject.fromObject(result);
				
				String d="['"+checkStandard+"','"+checkResult+"','"+checkReason+"','"+checkDetail+"','"+tempid+"','"+checkRemark+"','"+checkImportance+"']";
				node = "tree.N[\""+item.get("parentId")+"_"+checkItemId+"\"] = \"T:<span title="+item.get("checkDetail")+">"+item.get("checkDetail")+"</span>;mydata:"+d+"\";";
				printTree(node);
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
