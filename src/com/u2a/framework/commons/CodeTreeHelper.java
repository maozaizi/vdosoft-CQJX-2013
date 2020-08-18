package com.u2a.framework.commons;

public class CodeTreeHelper {
	public static String getParentId(String id){
		String result = "";
		int lastPointIndex = id.lastIndexOf(".");
		result = id.substring(0, lastPointIndex);
		return result;
	}
}
