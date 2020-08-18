package com.u2a.framework.util;

import java.util.Iterator;
import java.util.Map.Entry;

import com.brick.data.IMap;

public class JsonUtil {

	private static org.apache.commons.logging.Log log = 
			org.apache.commons.logging.LogFactory.getLog(JsonUtil.class);

	public static String object2json(IMap map) {
		StringBuilder json = new StringBuilder();
		if (map == null) {
			json.append("\"\"");
		} else {
			json.append(map2json(map));
		}
		return json.toString();
	}

	public static String map2json(IMap map) {
		StringBuilder json = new StringBuilder();
		json.append("{");

		if (map != null && !map.isEmpty()) {
			Iterator iter = map.entrySet().iterator();

			while (iter.hasNext()) {
				Entry entry = (Entry) iter.next();
				if (entry != null) {
					String key = entry.getKey().toString();
					Object value = entry.getValue() == null ? 
										"" : entry.getValue().toString();
					json.append("\"" + key + "\"");
					json.append(":");
					json.append("\"" + value + "\"");
					json.append(",");
				}
			}

			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}

		return json.toString();
	}

	public static String string2json(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
}
