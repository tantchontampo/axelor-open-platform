package com.axelor.web.internal;

import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.axelor.app.AppSettings;
import com.axelor.auth.AuthUtils;
import com.axelor.auth.db.Group;
import com.axelor.auth.db.User;
import com.axelor.common.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public final class AppInfo {

	private static final String APPLICATION_JS = "js/application.js";
	private static final String APPLICATION_JS_MIN = "js/application.min.js";
	
	private static final String APPLICATION_CSS = "css/application.css";
	private static final String APPLICATION_CSS_MIN = "css/application.min.css";

	private static final String APPLICATION_LANG_JS = "js/i18n/%s.js";
	
	private static final Locale DEFAULT_LOCALE = new Locale("en");
	
	private static final AppSettings settings = AppSettings.get();

	public static String asJson() {

		final Map<String, Object> map = Maps.newHashMap();
		try {
			User user = AuthUtils.getUser();
			Group group = user.getGroup();

			map.put("user.name", user.getName());
			map.put("user.login", user.getCode());

			if (group != null) {
				map.put("user.navigator", group.getNavigation());
			}
			map.put("user.lang", user.getLanguage());
			map.put("user.action", user.getAction().getName());
		} catch (Exception e){
		}

		map.put("app.toolbar.titles", settings.getBoolean("application.toolbar.titles", false));
		map.put("app.menubar.location", settings.get("application.menubar.location"));

		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(map);
		} catch (Exception e) {
		}
		return "{}";
	}

	private static String getUserLanguage() {
		final User user = AuthUtils.getUser();
		if (user == null) {
			return null;
		}
		return user.getLanguage();
	}

	public static String getLangJS(HttpServletRequest request, ServletContext context){

		Locale locale = null;
		String language = getUserLanguage();
		
		if (!StringUtils.isBlank(language)) {
			locale = toLocale(language);
		}
		if (locale == null) {
			locale = request.getLocale();
		}
		if (locale == null) {
			locale = toLocale(settings.get("application.locale", DEFAULT_LOCALE.getLanguage()));
		}

		for(String lang : Lists.newArrayList(toLanguage(locale, false), toLanguage(locale, true))) {
			if (checkResources(context, "/js/i18n/" + lang + ".js")) {
				language = lang;
				break;
			}
		}
		
		if (language == null) {
			language = DEFAULT_LOCALE.getLanguage();
		}
		
		return String.format(APPLICATION_LANG_JS, language);
	}

	public static String getAppJS(ServletContext context) {
		if (settings.isProduction() && checkResources(context, "/" + APPLICATION_JS_MIN)) {
			return APPLICATION_JS_MIN;
		}
		return APPLICATION_JS;
	}
	
	public static String getAppCSS(ServletContext context) {
		if (settings.isProduction() && checkResources(context, APPLICATION_CSS_MIN)) {
			return APPLICATION_CSS_MIN;
		}
		return APPLICATION_CSS;
	}

	private static String toLanguage(Locale locale, boolean minimize) {
		final String lang = locale.getLanguage().toLowerCase();
		if (minimize || StringUtils.isBlank(locale.getCountry())) {
			return lang;
		}
		return lang + "_" + locale.getCountry().toUpperCase();
	}

	private static Locale toLocale(String language) {
	    final String parts[] = language.split("_", -1);
	    if (parts.length == 1) {
	    	return new Locale(parts[0].toLowerCase());
	    }
	    return new Locale(parts[0].toLowerCase(), parts[1].toUpperCase());
	}

	private static boolean checkResources(ServletContext context, String resourcesPath) {
		try {
			return context.getResource(resourcesPath) != null;
		} catch (MalformedURLException e) {
			return false;
		}
	}
}
