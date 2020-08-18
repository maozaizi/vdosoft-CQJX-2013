package com.u2a.framework.service.base.baseOrganization;

import java.util.List;

import com.brick.data.IMap;
import com.u2a.util.tag.ITreeTagCallBack;

@SuppressWarnings("unchecked")
public class BaseOrganizationTree implements ITreeTagCallBack {

	public List getChildList(IMap item) {
		return (List) item.get("childList");
	}

	public String getChildListName() {
		return "childList";
	}

	public String getId(IMap item) {
		return (String) item.get("baseOrganizationId");
	}

	public String getIdName() {
		return "baseOrganizationId";
	}

	public String nodeParser(String rootPath, String parentId, IMap item) {

		String baseOrganizationId = item.get("baseOrganizationId").toString();
		parentId = item.get("parentId").toString();
		Integer iLevel = (Integer) item.get("iLevel");
		String icon = "";
		switch (iLevel) {
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
		parentId = parentId.replace("{", "");
		parentId = parentId.replace("}", "");
		baseOrganizationId = baseOrganizationId.replace("{", "");
		baseOrganizationId = baseOrganizationId.replace("}", "");
		String node = "tree.N[\"" + parentId + "_" + baseOrganizationId
				+ "\"] = \"T:<span title=" + item.get("baseOrganizationName")
				+ ">" + item.get("baseOrganizationName")
				+ "</span>;C:goOperator('" + item.get("baseOrganizationId")
				+ "','" + item.get("parentId") + "','"
				+ item.get("baseOrganizationName") + "','" + item.get("iLevel")
				+ "','" + item.get("id") + "');icon:" + icon + "\";";
		return node;
	}

}
