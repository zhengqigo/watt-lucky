package org.fuelteam.watt.lucky.text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.text.StringEscapeUtils;

public class EscapeUtil {

	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, Charsets.UTF_8_NAME);
		} catch (UnsupportedEncodingException ignored) { // NOSONAR
			// this exception is only for detecting and handling invalid inputs
			return null;
		}
	}

	public static String urlDecode(String part) {
		try {
			return URLDecoder.decode(part, Charsets.UTF_8_NAME);
		} catch (UnsupportedEncodingException e) { // NOSONAR
			// this exception is only for detecting and handling invalid inputs
			return null;
		}
	}

	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml11(xml);
	}

	public static String unescapeXml(String xml) {
		return StringEscapeUtils.unescapeXml(xml);
	}

	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	public static String unescapeHtml(String html) {
		return StringEscapeUtils.unescapeHtml4(html);
	}
}