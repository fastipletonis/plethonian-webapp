/*
 *     plethonian-webapp - Plethonian calendar Web Application
 *
 *     Copyright (C) 2020 Marco Confalonieri <marco at marcoconfalonieri.it>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;

/**
 * Language switcher.
 */
@RequestScoped
@Named("lang")
public class PlethonianCalendarLang {
    public static final String LANG_COOKIE = "lang";
    /**
     * Current selected locale.
     */
    private Locale locale;

    /**
     * Writes the language cookie.
     * 
     * @param lang the language
     */
    protected void writeLangCookie(String lang) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("maxAge", 31536000);
        properties.put("secure", false); 
        properties.put("path","/");
        FacesContext.getCurrentInstance().getExternalContext()
                .addResponseCookie(LANG_COOKIE, lang, properties);
    }
    
    /**
     * Reads the language cookie.
     * 
     * @return the language cookie value or null
     */
    protected String readLangCookie() {
        Object obj = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestCookieMap().get(LANG_COOKIE);
        if (obj == null || !(obj instanceof Cookie)) {
            return null;
        }
        Cookie cookie = (Cookie) obj;
        return cookie.getValue();
    }
    
    /**
     * Initializes the language.
     */
    @PostConstruct
    public void initialize() {
        String lang = readLangCookie();    
        if (lang != null) {
            locale = new Locale(lang);
            FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        } else {
            locale = FacesContext.getCurrentInstance().getViewRoot()
                    .getLocale();
        }     
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
    public void changeTo(String lang) {
        locale = new Locale(lang);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        writeLangCookie(lang);
    }
}
