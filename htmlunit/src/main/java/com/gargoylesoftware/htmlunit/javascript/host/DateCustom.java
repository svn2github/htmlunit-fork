/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Contains some missing features of Rhino NativeDate.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class DateCustom {

    private DateCustom() { }

    private static DateFormat LOCAL_DATE_FORMAT_;
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
        if (LOCAL_DATE_FORMAT_ == null) {
            LOCAL_DATE_FORMAT_ = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);
        }
        initDateField(thisObj);
        try {
            final double value = (Double) DATE_FIELD_.get(thisObj);
            return LOCAL_DATE_FORMAT_.format(new Date((long) value));
        }
        catch (final Exception e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }

    private static void initDateField(final Scriptable thisObj) {
        try {
            DATE_FIELD_ = thisObj.getClass().getDeclaredField("date");
            DATE_FIELD_.setAccessible(true);
        }
        catch (final Exception e) {
            throw Context.throwAsScriptRuntimeEx(e);
        }
    }
}
