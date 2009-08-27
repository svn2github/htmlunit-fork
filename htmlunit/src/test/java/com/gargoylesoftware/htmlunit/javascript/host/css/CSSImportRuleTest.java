/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link CSSImportRule}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class CSSImportRuleTest extends WebTestCase {

    /**
     * Regression test for bug 2658249.
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void testGetImportFromCssRulesCollection() throws Exception {
        final String html
            = "<html><body>\n"
            + "<style>@import url('" + URL_SECOND + "');</style><div id='d'>foo</div>\n"
            + "<script>\n"
            + "var r = document.styleSheets.item(0).cssRules[0];\n"
            + "alert(r);\n"
            + "alert(r.href);\n"
            + "alert(r.media);\n"
            + "alert(r.media.length);\n"
            + "alert(r.styleSheet);\n"
            + "</script>\n"
            + "</body></html>";
        final String css = "#d { color: green }";

        getMockWebConnection().setResponse(URL_SECOND, css, "text/css");

        setExpectedAlerts("[object CSSImportRule]", URL_SECOND.toString(),
            "[object MediaList]", "0", "[object CSSStyleSheet]");
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void testImportedStylesheetsLoaded() throws Exception {
        final String html
            = "<html><body>\n"
            + "<style>@import url('" + URL_SECOND + "');</style>\n"
            + "<div id='d'>foo</div>\n"
            + "<script>\n"
            + "var d = document.getElementById('d');\n"
            + "var s = window.getComputedStyle ? window.getComputedStyle(d,null) : d.currentStyle;\n"
            + "alert(s.color.indexOf('128') > 0);\n"
            + "</script>\n"
            + "</body></html>";
        final String css = "#d { color: rgb(0, 128, 0); }";

        getMockWebConnection().setResponse(URL_SECOND, css, "text/css");

        loadPageWithAlerts(html);
    }

}
