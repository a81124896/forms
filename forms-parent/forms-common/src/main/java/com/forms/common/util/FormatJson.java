package com.forms.common.util;

import com.alibaba.fastjson.JSON;

public class FormatJson {

	public static void main(String[] args) {
		String jsonStr = "{\"id\":\"1\",\"name\":\"a1\",\"obj\":{\"id\":11,\"name\":\"a11\",\"array\":[{\"id\":111,\"name\":\"a111\"},{\"id\":112,\"name\":\"a112\"}]}}";
		String fotmatStr = FormatJson.format(jsonStr);
		// fotmatStr = fotmatStr.replaceAll("\n", "<br/>");
		// fotmatStr = fotmatStr.replaceAll("\t", "    ");
		System.out.println(fotmatStr);
	}

	public static String format(Object o){
		return format(JSON.toJSONStringWithDateFormat(o, "yyyy-MM-dd HH:mm:ss")); 
	}
	/**
	 * 得到格式化json数据 退格用\t 换行用\r
	 */
	public static String format(String jsonStr) {
		int level = 0;
		StringBuffer jsonForMatStr = new StringBuffer();
		for (int i = 0; i < jsonStr.length(); i++) {
			char c = jsonStr.charAt(i);
			if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
				jsonForMatStr.append(getLevelStr(level));
			}
			switch (c) {
			case '{':
			case '[':
				jsonForMatStr.append(c + "\n");
				level++;
				break;
			case ',':
				jsonForMatStr.append(c + "\n");
				break;
			case '}':
			case ']':
				jsonForMatStr.append("\n");
				level--;
				jsonForMatStr.append(getLevelStr(level));
				jsonForMatStr.append(c);
				break;
			default:
				jsonForMatStr.append(c);
				break;
			}
		}

		return jsonForMatStr.toString();

	}

	private static String getLevelStr(int level) {
		StringBuffer levelStr = new StringBuffer();
		for (int levelI = 0; levelI < level; levelI++) {
//			levelStr.append("\t");
			levelStr.append("    ");
		}
		return levelStr.toString();
	}

}
