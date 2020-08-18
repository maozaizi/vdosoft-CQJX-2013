package com.u2a.util.tag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.struts.taglib.TagUtils;

import com.brick.data.IMap;

public class TreeHelper {
	public  static void createTreeTag(List<IMap> list,String id,PageContext pageContext,ITreeTagCallBack cb){
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		for(IMap item : list){
			String node = cb.nodeParser(request.getContextPath(),id,item);
			//String node = "tree.N[\""+id+"_"+item.get("articleId")+"\"] = \"T:<span title="+item.get("name")+">"+item.get("name")+"</span>;target:articleInfoRightFrame;url:"+request.getContextPath()+"/api/articleInfo/get?articleId="+item.get("articleId")+"&isShua=0&type="+item.get("type")+";C:goOperator('"+item.get("articleId")+"','"+item.get("parentId")+"','"+item.get("type")+"')\";";
			try {
				TagUtils.getInstance().write(pageContext, node);
			} catch (JspException e) {
				e.printStackTrace();
			}
			List<IMap> subItem = (List<IMap>)cb.getChildList(item);//(item.get("childList"));
			List<IMap> waitPrintNodeList = new ArrayList<IMap>();
			if(subItem != null && subItem.size() != 0){
				for(IMap subNode:subItem){
					waitPrintNodeList.add(subNode);
				}
			}
			//createTreeTag(waitPrintNodeList,item.get("articleId").toString(),pageContext);
			createTreeTag(waitPrintNodeList,cb.getId(item).toString(),pageContext,cb);
		}
	}
	/*
	 * 测试，该方法仅做使用参考 
	 */			
	public void test(){
		List<IMap> list=new ArrayList<IMap>();
		String id="001";
		PageContext pageContext=null;
		ITreeTagCallBack cb=new TreeTagCallBack() {
			public List getChildList(IMap item) {
				return (List) item.get("childList");
			}

			public String getChildListName() {
				return "childList";
			}

			public String getId(IMap item) {
				return (String) item.get("articleId");
			}

			public String getIdName() {
				return "articleId";
			}

			public String nodeParser(String rootPath,String parentId, IMap item) {
			   String node = "tree.N[\""+parentId+"_"+item.get("articleId")+"\"] = \"T:<span title="+item.get("name")+">"+item.get("name")+"</span>;target:articleInfoRightFrame;url:"+rootPath+"/api/articleInfo/get?articleId="+item.get("articleId")+"&isShua=0&type="+item.get("type")+";C:goOperator('"+item.get("articleId")+"','"+item.get("parentId")+"','"+item.get("type")+"')\";";
				return node;
			}
		      };

		createTreeTag(list,id,pageContext,cb);
	}
}
