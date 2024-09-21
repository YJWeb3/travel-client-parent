package com.zheng.travel.client.utils.ip;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;

public class TmIpUtil {

	/**
	 * 判断ip是否在指定网段中
	 *
	 * @param iparea
	 * @param ip
	 * @return boolean
	 */
	public static boolean ipIsInNet(String iparea, String ip) {
		if (iparea == null) {
			throw new NullPointerException("IP段不能为空！");
		}
		if (ip == null) {
			throw new NullPointerException("IP不能为空！");
		}
		iparea = iparea.trim();
		ip = ip.trim();
		final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
		final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
		if (!iparea.matches(REGX_IPB) || !ip.matches(REGX_IP)) {
			return false;
		}
		int idx = iparea.indexOf('-');
		String[] sips = iparea.substring(0, idx).split("\\.");
		String[] sipe = iparea.substring(idx + 1).split("\\.");
		String[] sipt = ip.split("\\.");
		long ips = 0L, ipe = 0L, ipt = 0L;
		for (int i = 0; i < 4; ++i) {
			ips = ips << 8 | Integer.parseInt(sips[i]);
			ipe = ipe << 8 | Integer.parseInt(sipe[i]);
			ipt = ipt << 8 | Integer.parseInt(sipt[i]);
		}
		if (ips > ipe) {
			long t = ips;
			ips = ipe;
			ipe = t;
		}
		return ips <= ipt && ipt <= ipe;
	}

	// IP判断
	public static boolean ipJudge(String ipArea, String ip) {
		boolean i = false;
		ipArea = TmStringSubUtil.LastSubStr(ipArea, ",");
		String[] arrayOfString = ipArea.split(",");
		for (int j = 0; j < arrayOfString.length; ++j) {
			if (arrayOfString[j].indexOf("*") != -1) {
				if (!(ipJudgment(
						arrayOfString[j].replaceAll("\\*", "0") + "-" + arrayOfString[j].replaceAll("\\*", "255"), ip))) {
					continue;
				}
				i = true;
				break;
			} else if (arrayOfString[j].indexOf(45) != -1) {
				if (!(ipJudgment(arrayOfString[j], ip))) {
					continue;
				}
				i = true;
				break;
			} else {
				if (!(arrayOfString[j].equals(ip))) {
					continue;
				}
				i = true;
				break;
			}
		}
		return i;
	}

	public static boolean ipJudgment(String ipArea, String ip) {
		if (ipArea == null) {
			throw new NullPointerException("IPBound is Null");
		}
		if (ip == null) {
			throw new NullPointerException("IP is Null");
		}
		ipArea = ipArea.trim();
		ip = ip.trim();
		String str1 = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
		String str2 = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\-((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
		if ((!(ipArea.matches(
				"((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\-((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)")))
				|| (!(ip.matches(
						"((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)")))) {
			return false;
		}
		int i = ipArea.indexOf(45);
		String[] arrayOfString1 = ipArea.substring(0, i).split("\\.");
		String[] arrayOfString2 = ipArea.substring(i + 1).split("\\.");
		String[] arrayOfString3 = ip.split("\\.");
		long l1 = 0L;
		long l2 = 0L;
		long l3 = 0L;
		for (int j = 0; j < 4; ++j) {
			l1 = l1 << 8 | Integer.parseInt(arrayOfString1[j]);
			l2 = l2 << 8 | Integer.parseInt(arrayOfString2[j]);
			l3 = l3 << 8 | Integer.parseInt(arrayOfString3[j]);
		}
		if (l1 > l2) {
			long l4 = l1;
			l1 = l2;
			l2 = l4;
		}
		return ((l1 <= l3) && (l3 <= l2));
	}

	public static String ipFormat(String paramString) {
		return paramString.substring(0, paramString.lastIndexOf(".") + 1) + "*";
	}

	// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			return "127.0.0.1";
		}
		return ip;
	}



	public static String ipLocation(String ip) {
		String ipLocation = "";
		TmIPSeeker ipSeeker = new TmIPSeeker("qqwry.dat", "C:\\yykk\\学相伴\\projects\\kuangstudy-web\\src\\main\\resources\\ip");
		ipLocation = ipSeeker.getIPLocation(ip).getCountry() + " " + ipSeeker.getIPLocation(ip).getArea();
		return ipLocation;
	}

	public static String ipprovince(String ip) {
		String ipLocation = "";
		TmIPSeeker ipSeeker = new TmIPSeeker("qqwry.dat", "C:\\yykk\\学相伴\\projects\\kuangstudy-web\\src\\main\\resources\\ip");
		ipLocation = ipSeeker.getIPLocation(ip).getCountry();
		return ipLocation;
	}

	public static String ipcity(String ip) {
		String ipLocation = "";
		TmIPSeeker ipSeeker = new TmIPSeeker("qqwry.dat", "D:/tomcat7/webapps/ROOT/WEB-INF/ip/");
		ipLocation = ipSeeker.getIPLocation(ip).getArea();
		return ipLocation;
	}

	public static String ipLocation(String ip, String path) {
		String ipLocation = "";
		TmIPSeeker ipSeeker = new TmIPSeeker("qqwry.dat", path);
		ipLocation = ipSeeker.getIPLocation(ip).getCountry() + " " + ipSeeker.getIPLocation(ip).getArea();
		return ipLocation;
	}

	/**
	 *
	 * 方法名：ipLocation 创建人：xuchengfei 时间：2015年3月25日-上午1:18:13
	 *
	 * @param request
	 * @return String
	 * @exception @since 1.0.0
	 */
	public static String ipLocation(HttpServletRequest request) {
		if (request == null) {
			return "";
		}
		try {
			String ipLocation = "";
			String ip = getIpAddr(request);
			String path = ResourceUtils.getFile("/www/web/ip").getAbsolutePath();
			if (StringUtils.isNotEmpty(path)) {
				path = conversionSpecialCharacters(path);
				TmIPSeeker ipSeeker = new TmIPSeeker("qqwry.dat", path);
				ipLocation = ipSeeker.getIPLocation(ip).getCountry() + " " + ipSeeker.getIPLocation(ip).getArea();
			}
			return ipLocation;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String conversionSpecialCharacters(String string) {
		return string.replaceAll("\\\\", "/");
	}

	public static void main(String[] args) {
//		System.out.println(ipJudge("192.168.1.1", "192.168.1.1"));
//		System.out.println(ipJudge("192.168.1.*", "192.168.1.255"));
//		System.out.println(ipJudge("192.168.1.1-192.168.1.2", "192.168.1.1"));
//		System.out.println(ipJudge("202.168.1.1-202.168.1.3", "202.168.1.2"));
//		System.out.println(ipJudge("127.0.0.0-127.0.0.10", "127.0.0.0"));
//
//		System.out.println(ipJudge("192.168.1.1", "192.168.1.1"));
//		System.out.println(ipLocation("221.209.44.46"));
//		System.out.println(ipLocation("202.101.108.207"));
		System.out.println(ipLocation("27.38.99.250"));
		System.out.println(ipLocation("183.128.166.9"));
		System.out.println(ipLocation("210.22.70.225"));
		System.out.println(ipLocation("192.168.1.38"));
	}

}
