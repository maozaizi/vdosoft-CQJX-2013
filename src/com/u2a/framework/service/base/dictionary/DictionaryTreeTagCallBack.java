package com.u2a.framework.service.base.dictionary;

import java.util.List;

import com.brick.data.IMap;
import com.u2a.util.tag.ITreeTagCallBack;

public class DictionaryTreeTagCallBack implements ITreeTagCallBack {

	public List getChildList(IMap item) {
		return (List) item.get("childList");
	}

	public String getChildListName() {
		return "childList";
	}

	public String getId(IMap item) {
		return (String) item.get("dataItemId");
	}

	public String getIdName() {
		return "dataItemId";
	}

	public String nodeParser(String rootPath, String parentId, IMap item) {
//		String node = "tree.N[\"" + item.get("parentId") + "_" + item.get("dataItemId")
//				+ "\"] = \"T:<span title=" + item.get("dataItemValue") + ">"
//				+ item.get("dataItemName")
//				+ "</span>;target:dictionaryRightFrame;url:" + rootPath
//				+ "/api/dictionary/get?dataItemId=" + item.get("dataItemId")
//				+ "&isShua=0;C:goOperator('" + item.get("dataItemId") + "','"
//				+ item.get("dataItemCode") + "','" + item.get("type") + "','"
//				+ item.get("parentId") + "')\";";
//		return node;
		String node ="";
		if(parentId.equals("-1")){
		 node = "tree.N[\"" + item.get("parentId") + "_" + item.get("dataItemId")
				+ "\"] = \"T:<span title=" + item.get("dataItemValue") + ">"
				+ item.get("dataItemName")
				+ "</span>;target:dictionaryRightFrame;url:" + rootPath
				+ "/api/dictionary/get?dataItemId=" + item.get("dataItemId")
				+ "&isShua=0;C:goOperator('"
				+ item.get("dataItemId") + "','" + item.get("dataItemCode") + "','"
				+ item.get("type") + "','"
				+ item.get("parentId") + "')\";";
		}else{
		 node = "tree.N[\"" + item.get("parentId") + "_" + item.get("dataItemId")
				+ "\"] = \"T:<span title=" + item.get("dataItemValue") + ">"
				+ item.get("dataItemName")
				+ "</span>;target:political_rightFrame;C:goOperator('"
				+ item.get("dataItemId") + "','" + item.get("dataItemCode") + "','"
				+ item.get("type") + "','"
				+ item.get("parentId") + "','"
				+ item.get("dataItemName") + "')\";";
		 }
		return node;
	}
}
