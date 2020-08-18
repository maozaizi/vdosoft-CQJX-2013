package com.u2a.framework.service.workflow.tree;


import java.util.List;

import com.brick.data.IMap;
import com.u2a.util.tag.ITreeTagCallBack;

/**
 * 流程管理树 系统名称：新疆生产建设兵团消防网络化信息管理系统 类名称：ProcessManageTree 类描述： 创建人：gaoy 创建时间：Mar
 * 26, 2012 10:01:26 AM
 */
public class ProcessTreeTagCallBack implements ITreeTagCallBack {
	
	public List getChildList(IMap item) {
		return (List) item.get("childList");
	}

	public String getChildListName() {
		return "childList";
	}

	public String getId(IMap item) {
		return (String) item.get("processId");
	}

	public String getIdName() {
		return "processId";
	}

	public String nodeParser(String rootPath, String parentId, IMap item) {
		String node = "tree.N[\"" + parentId + "_" + item.get("processId")
				+ "\"] = \"T:<span title=" + item.get("strname") + ">"
				+ item.get("strname") + "</span>;target:rightProcess;url:"
				+ rootPath
				+ "/api/workflow/getProcessInfo?processId="
				+ item.get("processId") + "&tempPat=0;C:goOperator('"
				+ item.get("processId") + "','" + item.get("parentId") + "',"
				+ item.get("flag") + ")\";";
		return node;
	}
}
