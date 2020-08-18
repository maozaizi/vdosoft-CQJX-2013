package com.u2a.framework.util.freemarker;

import java.util.ArrayList;
import java.util.List;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class WordWrap implements TemplateMethodModel {

	public Object exec(List arg0) throws TemplateModelException {
		// 需要折行的数据
		String str = (String) arg0.get(0);
		// 开始折行的字符数
		String wrapCountStr = (String) arg0.get(1);
		
		if (str == null || str.trim().equals("")) {
			return "";
		}
		if (wrapCountStr == null || wrapCountStr.trim().equals("")) {
			return str;
		}
		int wrapCount=Integer.valueOf(wrapCountStr);
		if (wrapCount==0) {
			return str;
		}
		int length = str.length();
		StringBuffer sb = new StringBuffer();
		int index = 0;
		for (int i = 0; i < length; i++) {
			sb.append(str.substring(i, i+1));
			index=index+1;
			if (index>=wrapCount){
				sb.append("<BR />");
				index=0;
			}
		}
		return sb.toString();

	}

	public static void main(String[] args) throws TemplateModelException {
		WordWrap w=new WordWrap();
		List list=new ArrayList();
		list.add("天府家园45号A座");
		list.add(2);
		String s= (String) w.exec(list);
		System.out.println(s);
	}
}
