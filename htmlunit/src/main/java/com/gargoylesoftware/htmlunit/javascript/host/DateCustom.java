/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DATE_LOCATE_TIME_24;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * Contains some missing features of Rhino NativeDate.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class DateCustom {

    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    private DateCustom() { }

    private static Field DATE_FIELD_;

    /**
     * Converts a date to a string, returning the "date" portion using the operating system's locale's conventions.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return converted string
     */
    public static String toLocaleDateString(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        final SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM dd, yyyy", getLocale(thisObj));
        return format.format(new Date(getDateValue(thisObj)));
    }

    /**
     * Converts a date to a string, returning the "time" portion using the current locale's conventions.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return converted string
     */
    public static String toLocaleTimeString(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        final String formatString;
        if (((Window) thisObj.getParentScope()).getWebWindow().getWebClient().getBrowserVersion()
                .hasFeature(JS_DATE_LOCATE_TIME_24)) {
            formatString = "HH:mm:ss";
        }
        else {
            formatString = "hh:mm:ss a";
        }
        final DateFormat format =  new SimpleDateFormat(formatString, getLocale(thisObj));
        return format.format(new Date(getDateValue(thisObj)));
    }

    /**
     * Converts a date to a UTC string. Special version for IE
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return converted string
     */
    public static String toUTCString(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        final Date date = new Date(getDateValue(thisObj));
        return DateFormatUtils.format(date, "EEE, d MMM yyyy HH:mm:ss z", UTC_TIME_ZONE, Locale.ENGLISH);
    }

    private static long getDateValue(final Scriptable thisObj) {
        try {
            if (DATE_FIELD_ == null) {
                DATE_FIELD_ = thisObj.getClass().getDeclaredField("date");
                DATE_FIELD_.setAccessible(true);
            }
            return ((Double) DATE_FIELD_.get(thisObj)).longValue();
        }
        catch (final Exception e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    private static Locale getLocale(final Scriptable thisObj) {
        final BrowserVersion broserVersion = ((Window) thisObj.getParentScope())
                .getWebWindow().getWebClient().getBrowserVersion();
        return new Locale(broserVersion.getSystemLanguage());
    }
}
