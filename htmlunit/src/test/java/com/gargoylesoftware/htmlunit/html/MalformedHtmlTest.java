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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Set of tests for ill formed HTML code.
 * @version $Revision$
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class MalformedHtmlTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "in test", "BODY" })
    public void testBodyAttributeWhenOpeningBodyGenerated() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function test(){\n"
            + "    alert('in test');\n"
            + "    alert(document.getElementById('span1').parentNode.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "<span id='span1'>hello</span>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3", "text3", "text3", "null" })
    public void testLostFormChildren() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "function test(){\n"
            + "    alert(document.forms[0].childNodes.length);\n"
            + "    alert(document.forms[0].elements.length);\n"
            + "    alert(document.forms[0].elements[2].name);\n"
            + "    alert(document.forms[0].text3.name);\n"
            + "    alert(document.getElementById('text4').form);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<div>\n"
            + "<form action='foo'>"
            + "<input type='text' name='text1'/>"
            + "<input type='text' name='text2'/>"
            + "</div>\n"
            + "<input type='text' name='text3'/>\n"
            + "</form>\n"
            + "<input type='text' name='text4' id='text4'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Test document")
    public void testTitleAfterInsertedBody() throws Exception {
        final String content = "<html><head>\n"
            + "<noscript><link href='other.css' rel='stylesheet' type='text/css'></noscript>\n"
            + "<title>Test document</title>\n"
            + "</head><body onload='alert(document.title)'>\n"
            + "foo"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Test document")
    public void testTitleTwice() throws Exception {
        final String content = "<html><head>\n"
            + "<title>Test document</title>\n"
            + "<title>2nd title</title>\n"
            + "</head><body onload='alert(document.title)'>\n"
            + "foo"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * Test for <a href="http://sourceforge.net/support/tracker.php?aid=2767865">Bug 2767865</a>.
     * In fact this is not fully correct because IE (6 at least) does something very strange
     * and keeps the DIV in TABLE but wraps it in a node without name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "DIV", "TABLE" })
    public void div_between_table_and_tr() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test(){\n"
            + "  var c1 = document.body.firstChild;\n"
            + "  alert(c1.tagName);\n"
            + "  alert(c1.nextSibling.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>"
            + "<table><div>hello</div>\n"
            + "<tr><td>world</td></tr></table>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello")
    public void script_between_head_and_body() throws Exception {
        final String content = "<html><head><title>foo</title></head><script>\n"
            + "alert('hello');\n"
            + "</script>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
    }

    /**
     * Tests that wrong formed HTML code is parsed like browsers do.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12345")
    public void testWrongHtml_TagBeforeHtml() throws Exception {
        final String html = "<div>\n"
            + "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "var toto = 12345;\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='alert(toto)'>\n"
            + "blabla"
            + "</body>\n"
            + "</html>";

        final WebDriver webdriver = loadPageWithAlerts2(html);
        assertEquals("foo", webdriver.getTitle());
    }

    /**
    * Regression test for bug 2838901.
    * @throws Exception if an error occurs
    */
    @Test
    @Alerts(IE8 = "0")
    @NotYetImplemented(IE8)
    public void missingSingleQuote() throws Exception {
        final String html = "<html><body>"
            + "Go to <a href='http://blah.com>blah</a> now."
            + "<script>alert(document.links.length)</script>"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
    * Regression test for bug 2838901.
    * @throws Exception if an error occurs
    */
    @Test
    @Alerts(IE8 = "0")
    @NotYetImplemented(IE8)
    public void missingDoubleQuote() throws Exception {
        final String html = "<html><body>"
            + "Go to <a href=\"http://blah.com>blah</a> now."
            + "<script>alert(document.links.length)</script>"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "inFirst", "inSecond" })
    public void nestedForms() throws Exception {
        final String html = "<html><body>\n"
            + "<form name='TransSearch'>\n"
            + "<input type='submit' id='button'>\n"
            + "<table>\n"
            + "<tr><td><input name='FromDate' value='inFirst'></form></td></tr>\n"
            + "</table>\n"
            + "<table>\n"
            + "<tr><td><form name='ImageSearch'></td></tr>\n"
            + "<tr><td><input name='FromDate' value='inSecond'></form></td></tr>\n"
            + "</table>\n"
            + "<script>\n"
            + "alert(document.forms[0].elements['FromDate'].value);\n"
            + "alert(document.forms[1].elements['FromDate'].value);\n"
            + "</script>\n"
            + "</body></html>";
        final WebDriver driver = loadPageWithAlerts2(html);
        driver.findElement(By.id("button")).click();

        assertEquals(getDefaultUrl() + "?FromDate=inFirst", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void li_div_li() throws Exception {
        final String html = "<html><body>\n"
            + "<ul id='it'><li>item 1<div>in div</li><li>item2</li></ul>"
            + "<script>\n"
            + "alert(document.getElementById('it').childNodes.length);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
    * Regression test for bug 1564.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "\uFFFD", "65533" })
    public void entityWithInvalidUTF16Code() throws Exception {
        final String html = "<html><head><title>&#x1b3d6e;</title></head><body><script>"
            + "alert(document.title.length);\n"
            + "alert(document.title);\n"
            + "alert(document.title.charCodeAt(0));\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
