package com.u2a.framework.service.workflow.tree;

import java.util.List;

import com.brick.data.IMap;
import com.u2a.util.tag.ITreeTagCallBack;

public class AuthorizedTreeTagCallBack implements ITreeTagCallBack {

	public List getChildList(IMap item) {
		return (List) item.get("childList");
	}

	public String getChildListName() {
		return "childList";
	}

	public String getId(IMap item) {
		return (String) item.get("organizationDetailId");
	}

	public String getIdName() {
		return "organizationDetailId";
	}

	public String nodeParser(String rootPath, String parentId, IMap item) {
		String node = "tree.N[\"" + parentId + "_"
				+ item.get("organizationDetailId") + "\"] = \"T:<span title="
				+ item.get("organizationDetailName") + ">"
				+ item.get("organizationDetailName")
				+ "</span>;target:app_rightFrame;url:" + rootPath
				+ "/api/workflow/getPostAndUserInfo?organizationDetailId="
				+ item.get("organizationDetailId") + "&processStepId="
				+ item.get("processStepId")+"&authorizedType="
				+ item.get("authorizedType")+"&processId="
				+ item.get("processId")+";C:goOperator('"
				+ parentId + "','" + item.get("organizationDetailId") + "','"
				+ item.get("organizationDetailName") + "','"
				+ item.get("baseOrganizationId") + "','" + item.get("type")
				+ "')\";";
		return node;
	}
}
