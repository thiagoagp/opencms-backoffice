package com.mscg.emule.util;

import org.htmlcleaner.CleanerProperties;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.Intent;

import com.mscg.emule.Login;

public class Util {

	private Util() {

	}

	public static boolean isEmpty(String value) {
		return value == null || value.length() == 0;
	}

	public static boolean isEmptyOrWhiteSpaceOnly(String value) {
		return isEmpty(value) || value.trim().length() == 0;
	}

	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

	public static boolean isNotEmptyOrWhiteSpaceOnly(String value) {
		return !isEmptyOrWhiteSpaceOnly(value);
	}

	public static CleanerProperties getHtmlCleanerStandardProperties() {
		CleanerProperties props = new CleanerProperties();
        // set some properties to non-default values
        props.setTranslateSpecialEntities(true);
        props.setTransResCharsToNCR(true);
        props.setOmitComments(true);
        return props;
	}

	public static Intent getHomeIntent(Context context) {
		Intent homeIntent = new Intent(context, Login.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return homeIntent;
	}

	public static String getNodesText(NodeList nodes) {
		StringBuffer ret = new StringBuffer();
		for(int i = 0, l = nodes.getLength(); i < l; i++) {
			ret.append(nodes.item(i).getTextContent());
		}
		return ret.toString();
	}

}
