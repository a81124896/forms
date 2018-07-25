package com.forms.common.util;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class MLog {
	private static final Logger logger = Logger.getLogger(MLog.class);

	public static void debug(Object o) {
		StringBuffer sb = new StringBuffer();
		if (o instanceof String) {
			sb.append(o);
			sb.append(getSessionId());
			logger.debug(sb.toString());
		} else if (o instanceof Exception) {
			sb.append(((Exception) o).getMessage());
			sb.append(getSessionId());
			logger.debug(sb.toString(), (Exception) o);
		} else {
			sb.append(FormatJson.format(o));
			sb.append(getSessionId());
			logger.debug("\n---------------info-------------------->:\n\n" + sb.toString() + "\n\n<------------info--------------");
		}
	}

	public static void info(Object o) {
		StringBuffer sb = new StringBuffer();
		if (o instanceof String) {
			sb.append(o);
			sb.append(getSessionId());
			logger.info(sb.toString());
		} else if (o instanceof Exception) {
			sb.append(((Exception) o).getMessage());
			sb.append(getSessionId());
			logger.info(sb.toString(), (Exception) o);
		} else {
			sb.append(JSON.toJSONStringWithDateFormat(o, "yyyy-MM-dd HH:mm:ss"));
			sb.append(getSessionId());
			logger.info("\n---------------info-------------------->:\n\n" + FormatJson.format(sb.toString()) + "\n\n<------------info--------------");
		}
	}

	public static void result(Object o) {
		// StringBuffer sb = new StringBuffer("Respone Info ===============\n");
		StringBuffer sb = new StringBuffer();
		if (o instanceof String) {
			sb.append(o);
			sb.append("MID:");
			sb.append(getSessionId());
			sb.append("\n");
			logger.info(sb.toString());
		} else if (o instanceof Exception) {
			sb.append(((Exception) o).getMessage());
			sb.append("MID:");
			sb.append(getSessionId()); 
			logger.info(sb.toString() + "\n=============== Respone Info ===============", (Exception) o);
		} else {
			sb.append("■ <=== (JSON) ");
			sb.append(FormatJson.format(JSON.toJSONStringWithDateFormat(o, "yyyy-MM-dd HH:mm:ss")));
			sb.append("MID:");
			sb.append(getSessionId());
			
			logger.info(sb.toString());
			// logger.info(sb.toString() + "\n=============== Respone Info ===============");
		}
	}

	public static void info(String message, Exception t) {
		logger.info(message, t);
	}

	public static void warn(Object o) {
		logger.warn("\nMyDeBug---warnSTART-->:\n" + FormatJson.format(o) + "\n<---warnEND--MyDeBug");
	}

	public static void debug(Object o, Exception ex) {
		logger.debug("\nMyDeBug---debugSTART-->:\n" + FormatJson.format(o) + "\n<---debugEND--MyDeBug");
	}

	public static void error(Object o) {
		logger.error("\nMyDeBug---errorSTART-->:\n" + FormatJson.format(o) + "\n<---errorEND--MyDeBug");
	}

	public static String getSessionId() {
		StringBuffer sb = new StringBuffer("[");
		String id = "0";
//		try {
////			Object obj = MRuntime.getContext("cgiId");
////			if (obj != null) {
////				id = obj.toString();
////			}
//		} catch (Exception e) {
//		}
		sb.append(id).append("] ");
		return sb.toString();
	}
}
