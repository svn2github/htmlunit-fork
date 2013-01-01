/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Attr;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * A JavaScript object for an Attribute of XMLElement.
 *
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-63764602">W3C DOM Level 2</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms762738.aspx">MSDN documentation</a>
 * @version $Revision$
 * @author Sudhan Moghe
 */
@JsxClass
public class XMLAttr extends Attr {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLAttr() { }

    /**
     * Returns the text of this attribute.
     * @return the value of this attribute
     */
    @JsxGetter(@WebBrowser(IE))
    public String getText() {
        return getValue();
    }

    /**
     * Sets the text of this attribute.
     * @param value the new value of this attribute
     */
    @JsxSetter(@WebBrowser(IE))
    public void setText(final String value) {
        setValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXml() {
        final StringBuilder sb = new StringBuilder(getName());
        sb.append('=').append('"');
        sb.append(StringUtils.escapeXmlAttributeValue(getValue()));
        sb.append('"');
        return sb.toString();
    }
}
