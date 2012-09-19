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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlObject;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObjectImpl;
import com.gargoylesoftware.htmlunit.javascript.host.FormChild;

/**
 * The JavaScript object "HTMLObjectElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(htmlClass = HtmlObject.class, extend = "FormChild")
public class HTMLObjectElement extends FormChild {

    private SimpleScriptable wrappedActiveX_;

    /**
     * Creates an instance.
     */
    public HTMLObjectElement() {
        // Empty.
    }

    /**
     * Returns the value of the "alt" property.
     * @return the value of the "alt" property
     */
    @JsxGetter(@WebBrowser(IE))
    public String jsxGet_alt() {
        final String alt = getDomNodeOrDie().getAttribute("alt");
        return alt;
    }

    /**
     * Returns the value of the "alt" property.
     * @param alt the value
     */
    @JsxSetter(@WebBrowser(IE))
    public void jsxSet_alt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    @JsxGetter
    public String jsxGet_border() {
        final String border = getDomNodeOrDie().getAttribute("border");
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    @JsxSetter
    public void jsxSet_border(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Gets the "classid" attribute.
     * @return the "classid" attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String jsxGet_classid() {
        final String classid = getDomNodeOrDie().getAttribute("classid");
        return classid;
    }

    /**
     * Sets the "classid" attribute.
     * @param classid the "classid" attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void jsxSet_classid(final String classid) {
        getDomNodeOrDie().setAttribute("classid", classid);
        if (classid.indexOf(':') != -1 && getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_86)
                && getWindow().getWebWindow().getWebClient().getOptions().isActiveXNative()
                && System.getProperty("os.name").contains("Windows")) {
            try {
                wrappedActiveX_ = new ActiveXObjectImpl(classid);
                wrappedActiveX_.setParentScope(getParentScope());
            }
            catch (final Exception e) {
                Context.throwAsScriptRuntimeEx(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if (wrappedActiveX_ != null) {
            return wrappedActiveX_.get(name, start);
        }
        return super.get(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (wrappedActiveX_ != null) {
            wrappedActiveX_.put(name, start, value);
        }
        else {
            super.put(name, start, value);
        }
    }
}
