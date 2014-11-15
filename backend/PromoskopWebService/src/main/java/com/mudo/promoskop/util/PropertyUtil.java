package com.mudo.promoskop.util;

import java.util.HashMap;
import java.util.ResourceBundle;

public class PropertyUtil {

	private static final String APP_VERSION = "app.version";
	private static final String FLAG_SHOW_HIGHEST_GAP_PRODUCT = "show.highestGapProduct.flag";

	public static HashMap<String, Object> getSystemConfigProperties() {
		HashMap<String, Object> configProperties = new HashMap<>();
		ResourceBundle configBundle = ResourceBundle.getBundle("config");
		String appVersion = configBundle.getString(APP_VERSION);
		Boolean flag = Boolean.parseBoolean(configBundle.getObject(
				FLAG_SHOW_HIGHEST_GAP_PRODUCT).toString());
		configProperties.put("app_version", appVersion);
		configProperties.put("flag", flag);
		return configProperties;
	}
}
