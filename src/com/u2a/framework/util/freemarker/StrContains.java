package com.u2a.framework.util.freemarker;

import java.util.ArrayList;
import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class StrContains implements TemplateMethodModel {

	public Object exec(List arg0) throws TemplateModelException {
		// 源数据
		String str = (String) arg0.get(0);
		// 包含的数据
		String strc = (String) arg0.get(1);
		
		if (str == null || str.trim().equals("")) {
			return false;
		}
		if (strc == null || strc.trim().equals("")) {
			return true;
		}
		boolean bl=str.indexOf(strc)>-1;
		return bl;		
	}

	public static void main(String[] args) throws TemplateModelException {
		StrContains w=new StrContains();
		List list=new ArrayList();
		list.add(null);
		list.add("4");
		boolean s= (Boolean) w.exec(list);
		System.out.println(s);
	}

}
