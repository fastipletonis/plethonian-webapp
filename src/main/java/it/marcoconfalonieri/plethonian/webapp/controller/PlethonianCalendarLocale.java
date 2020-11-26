/*
 *     plethonian-webapp - Plethonian calendar Web Application
 *
 *     Copyright (C) 2020 Marco Confalonieri <marco at marcoconfalonieri.it>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.marcoconfalonieri.plethonian.webapp.controller;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;

/**
 * Language switcher.
 */
@RequestScoped
@Named("locale")
public class PlethonianCalendarLocale {
    /**
     * Default timezone.
     */
    public static final String TZ_DEFAULT = "UTC";
    /**
     * Language cookie name.
     */
    public static final String LANG_COOKIE = "lang";
    /**
     * TimeZone cookie name.
     */
    public static final String TZ_COOKIE = "tz";
    /**
     * Current selected locale.
     */
    private Locale locale;
    /**
     * Current selected timezone.
     */
    private ZoneId zoneId;
    /**
     * PlethonianCalendarApp instance.
     */
    private PlethonianCalendarApp appInstance;
    
    /**
     * Check input language.
     * 
     * @param lang language
     * 
     * @return the language if supported or "la"
     */
    private String checkLang(String lang) {
        if (lang == null) { return null; }
        return appInstance.getAvailableLocales().containsKey(lang)?
                lang : "la";
    }
    
    /**
     * Check time zone.
     * 
     * @param tz timezone
     * 
     * @return the timezone if supported or the default timezone.
     */
    private String checkZoneId(String tz) {
        if (tz == null) { return null; }
        return appInstance.getAvailableTimeZones().contains(tz)?
                tz : TZ_DEFAULT;
    }
    
    /**
     * Writes a preference cookie.
     * 
     * @param name the cookie name
     * @param value the cookie value
     */
    protected void writeCookie(String name, String value) {
        final String MAX_AGE = "maxAge";
        final String SECURE = "secure";
        final String PATH = "path";
        
        Map<String, Object> properties = new HashMap<>();
        properties.put(MAX_AGE, 31536000);
        properties.put(SECURE, false); 
        properties.put(PATH, "/");
        
        FacesContext.getCurrentInstance().getExternalContext()
                .addResponseCookie(name, value, properties);
    }

    /**
     * Reads a cookie.
     * 
     * @param name the cookie name
     * 
     * @return the cookie value or null
     */
    protected String readCookie(String name) {
        Object obj = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestCookieMap().get(name);
        if (obj == null || !(obj instanceof Cookie)) {
            return null;
        }
        Cookie cookie = (Cookie) obj;
        return cookie.getValue();
    }
    
    /**
     * Initializes the language.
     */
    protected void initLang() {
        String lang = readCookie(LANG_COOKIE);
        if (lang != null) {
            locale = new Locale(checkLang(lang));
            FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        } else {
            locale = FacesContext.getCurrentInstance().getViewRoot()
                    .getLocale();
        }
    }
    
    /**
     * Initializes the time zone.
     */
    protected void initZoneId() {
        String tz = readCookie(TZ_COOKIE);
        zoneId = ZoneId.of((tz == null)? TZ_DEFAULT : checkZoneId(tz));
    }
    
    /**
     * Initializes the object.
     */
    @PostConstruct
    public void initialize() {
        initLang();
        initZoneId();
    }

    /**
     * Getter for the locale.
     * 
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }
    
    /**
     * Changes the language to the specified one.
     * 
     * @param lang the language
     */
    public void setLang(String lang) {
        if (lang == null) { return; }
        locale = new Locale(checkLang(lang));
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        writeCookie(LANG_COOKIE, lang);
    }
    
    /**
     * Returns the language.
     * 
     * @return the language
     */
    public String getLang() {
        return locale.getLanguage();
    }

    /**
     * Getter for the timezone ID.
     * 
     * @return the timezone as a <code>ZoneId</code> object
     */
    public ZoneId getZoneId() {
        return zoneId;
    }
        
    /**
     * Getter for the timezone ID as string.
     * 
     * @return the timezone as a string
     */
    public String getTimeZone() {
        return zoneId.getId();
    }
    
    /**
     * Changes the timezone to the specified one.
     * 
     * @param tz the timezone
     */
    public void setTimeZone(String tz) {
        if (tz == null) { return; }
        zoneId = ZoneId.of(checkZoneId(tz));
        writeCookie(TZ_COOKIE, tz);
    }

    /**
     * Sets the PlethonianCalendarApp instance.
     * 
     * @param appInstance the appInstance to set
     */
    @Inject
    public void setAppInstance(PlethonianCalendarApp appInstance) {
        this.appInstance = appInstance;
    }
    
}
