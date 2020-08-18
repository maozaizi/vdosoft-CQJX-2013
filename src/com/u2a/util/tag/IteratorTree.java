package com.u2a.util.tag;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.TagUtils;

import com.brick.data.IMap;
/**
 * 迭代树
 * @author Administrator
 * @date
 */
public class IteratorTree extends TagSupport{
	
	private List<IMap> list ;
	
	private String initId;
	
	public String getInitId() {
		return initId;
	}

	public void setInitId(String initId) {
		this.initId = initId;
	}

	public List<IMap> getList() {
		return list;
	}

	public void setList(List<IMap> list) {
		this.list = list;
	}

	@Override
	public int doStartTag() throws JspException {
		iteratorTree(list,initId);
		return super.doStartTag();
	}
	private void iteratorTree(List<IMap> oneList,String id){
		for(IMap one : oneList){			
				String nodeOne = "tree.N[\""+id+"_"+one.get("id")+"\"] = \"T:<span title="+one.get("name")+">"+one.get("name")+"</span>;C:goOperator('"+one.get("type")+"','"+one.get("id")+"','"+one.get("fid")+"','"+one.get("choose")+"')\";";
				print(nodeOne);
				//sSystem.out.println(nodeOne);
			List<IMap> typeThree = (List<IMap>) one.get("typeThree");
			List<IMap> typeOther = (List<IMap>) one.get("typeOther");
			for(IMap three : typeThree){
					String nodeThree = "tree.N[\""+one.get("id")+"_"+three.get("id")+"\"] = \"T:<span title="+three.get("name")+">"+three.get("name")+"</span>;C:goOperator('"+three.get("type")+"','"+three.get("id")+"','"+one.get("fid")+"','"+one.get("choose")+"')\";";
					print(nodeThree);
			}
			if(typeOther!=null && !typeOther.isEmpty()){
				iteratorTree(typeOther,(String)one.get("id"));
			}
		}
	}
	private void print(String text){
		try {
			TagUtils.getInstance().write(this.pageContext, text);
		} catch (JspException e) {
			e.printStackTrace();
		}
	}
	
}
