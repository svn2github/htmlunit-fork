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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The JavaScript object that represents a Storage.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass
public class Storage extends SimpleScriptable {

    static enum Type { GLOBAL_STORAGE, LOCAL_STORAGE, SESSION_STORAGE }

    private static List<String> RESERVED_NAMES_ = Arrays.asList("clear", "key", "getItem", "length", "removeItem",
        "setItem");

    private Type type_;

    void setType(final Type type) {
        type_ = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (type_ == null || RESERVED_NAMES_.contains(name)) {
            super.put(name, start, value);
            return;
        }
        setItem(name, Context.toString(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if (type_ == null || RESERVED_NAMES_.contains(name)) {
            return super.get(name, start);
        }
        return getItem(name);
    }

    /**
     * Returns the length property.
     * @return the length property
     */
    @JsxGetter
    public int getLength() {
        return getMap().size();
    }

    /**
     * Removes the specified key.
     * @param key the item key
     */
    @JsxFunction
    public void removeItem(final String key) {
        getMap().remove(key);
    }

    /**
     * Returns the key of the specified index.
     * @param index the index
     * @return the key
     */
    @JsxFunction
    public String key(final int index) {
        int counter = 0;
        for (final String key : getMap().keySet()) {
            if (counter++ == index) {
                return key;
            }
        }
        return null;
    }

    private Map<String, String> getMap() {
        return StorageImpl.getInstance().getMap(type_, (HtmlPage) getWindow().getWebWindow().getEnclosedPage());
    }

    /**
     * Returns the value of the specified key.
     * @param key the item key
     * @return the value
     */
    @JsxFunction
    public Object getItem(final String key) {
        return getMap().get(key);
    }

    /**
     * Sets the item value.
     * @param key the item key
     * @param data the value
     */
    @JsxFunction
    public void setItem(final String key, final String data) {
        getMap().put(key, data);
    }

    /**
     * Clears all items.
     */
    @JsxFunction({ @WebBrowser(value = IE, minVersion = 8), @WebBrowser(FF) })
    public void clear() {
        StorageImpl.getInstance().clear(type_, (HtmlPage) getWindow().getWebWindow().getEnclosedPage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        if (getWindow().getWebWindow() != null) {
            if (getBrowserVersion().hasFeature(BrowserVersionFeatures.STORAGE_OBSOLETE)) {
                return "StorageObsolete";
            }
        }
        return super.getClassName();
    }
}
