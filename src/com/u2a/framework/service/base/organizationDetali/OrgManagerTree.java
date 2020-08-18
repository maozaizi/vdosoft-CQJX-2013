package com.u2a.framework.service.base.organizationDetali;

import java.util.List;

import com.brick.data.IMap;
import com.u2a.util.tag.ITreeTagCallBack;
@SuppressWarnings("unchecked")
public class OrgManagerTree implements ITreeTagCallBack {
	
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
		String iLevelStr = (String)item.get("baseOrganizationId");
		iLevelStr = iLevelStr.replace(".", ",");
		String[] iLevelArray = iLevelStr.split(",");
		String icon = "";
		//判断是否为组织，只改变组织的图标
		if(item.get("type").equals("0")){
			switch (iLevelArray.length) {
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
		}
		String node = "tree.N[\""
				+ parentId
				+ "_"
				+ item.get("organizationDetailId")
				+ "\"] = \"T:<span title="
				+ item.get("organizationDetailName")
				+ ">"
				+ item.get("organizationDetailName")
				+ "</span>;target:app_rightFrame;url:"
				+ rootPath
				+ "/api/org_xfj/getInfo?organizationDetailId="
				+ item.get("organizationDetailId") + ";C:goOperator('"
				+ parentId + "','" + item.get("organizationDetailId")
				+ "','" + item.get("organizationDetailName") + "','"
				+ item.get("baseOrganizationId") + "','"
				+ item.get("type") + "');icon:"+icon+"\";";
		return node;
	}
}
