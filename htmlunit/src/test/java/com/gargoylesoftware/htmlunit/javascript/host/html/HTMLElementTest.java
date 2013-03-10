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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF17;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests for {@link HTMLElement}.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Hans Donner
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ethan Glasser-Camp
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(FF)
    @Alerts(IE = { "all node for body: DIV A IMG DIV ", "all node for testDiv: A IMG ",
            "all node for testA: IMG ", "all node for testImg: ", "all node for testDiv2: " })
    public void all_IndexByInt() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  dumpAll('body');\n"
            + "  dumpAll('testDiv');\n"
            + "  dumpAll('testA');\n"
            + "  dumpAll('testImg');\n"
            + "  dumpAll('testDiv2');\n"
            + "}\n"
            + "function dumpAll(_id) {\n"
            + "  var oNode = document.getElementById(_id);\n"
            + "  var col = oNode.all;\n"
            + "  var str = 'all node for ' + _id + ': ';\n"
            + "  for (var i=0; i<col.length; i++)\n"
            + "  {\n"
            + "    str += col[i].tagName + ' ';\n"
            + "  }\n"
            + "  alert(str);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()' id='body'>\n"
            + "<div id='testDiv'>foo<a href='foo.html' id='testA'><img src='foo.png' id='testImg'></a></div>\n"
            + "<div id='testDiv2'>foo</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "a", "a", "undefined", "null" })
    public void getAttribute() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "       var myNode = document.getElementById('myNode');\n"
                + "       alert(myNode.title);\n"
                + "       alert(myNode.getAttribute('title'));\n"
                + "       alert(myNode.Title);\n"
                + "       alert(myNode.getAttribute('class'));\n"
                + "   }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<p id='myNode' title='a'>\n"
                + "</p>\n"
                + "</body>\n"
                + "</html>";

        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals("test", webDriver.getTitle());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "color: green;", IE = "")
    public void getAttribute_styleAttributeWithFlag() throws Exception {
        final String html =
              "<html><body onload='test()'><div id='div' style='color: green;'>abc</div>\n"
            + "<script>\n"
            + "  function test(){\n"
            + "    var div = document.getElementById('div');\n"
            + "    alert(div.getAttribute('style', 2));\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Some libraries like MochiKit looks after the number of attributes of a freshly created node.
     * When this is fixed for IE, all {@link com.gargoylesoftware.htmlunit.libraries.MochiKitTest}
     * working for FF will work for IE too.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "0 attribute", IE = "at least 1 attribute")
    public void attributes() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "       var myNode = document.body.firstChild;\n"
                + "       if (myNode.attributes.length == 0)\n"
                + "         alert('0 attribute');\n"
                + "       else\n"
                + "         alert('at least 1 attribute');\n"
                + "   }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>" // no \n here!
                + "<span>test span</span>\n"
                + "</body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = "exception",
            FF17 = { "null", "bla", "true" },
            DEFAULT = { "", "bla", "true" })
    @NotYetImplemented(FF17)
    public void getSetAttributeNS() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "try {\n"
                + "       var myNode = document.getElementById('myNode');\n"
                + "       alert(myNode.getAttributeNS('myNamespaceURI', 'my:foo'));\n"
                + "       myNode.setAttributeNS('myNamespaceURI', 'my:foo', 'bla');\n"
                + "       alert(myNode.getAttributeNS('myNamespaceURI', 'foo'));\n"
                + "       alert(myNode.getAttributeNodeNS('myNamespaceURI', 'foo').specified);\n"
                + "} catch (e) { alert('exception'); }\n"
                + "   }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<p id='myNode' title='a'>\n"
                + "</p>\n"
                + "</body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "a", "b", "undefined", "foo" })
    public void setAttribute() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       alert(myNode.title);\n"
            + "       myNode.setAttribute('title', 'b');\n"
            + "       alert(myNode.title);\n"
            + "       alert(myNode.Title);\n"
            + "       myNode.Title = 'foo';\n"
            + "       alert(myNode.Title);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode' title='a'>\n"
            + "</p>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals("test", webDriver.getTitle());
    }

    /**
     * Caution: with IE if you get a node with some lowercase letters, the node will be retrieved
     * and will get as name the value passed as attribute to getAttributeNode.
     * The consequence for IE: x.getAttributeNode("Foo").nodeName != x.getAttributeNode("foo").nodeName
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = {
            "null",
            "expando=true",
            "firstChild=null",
            "lastChild=null",
            "name=custom_attribute",
            "nextSibling=null",
            "nodeName=custom_attribute",
            "nodeType=2",
            "nodeValue=bleh",
            "(ownerDocument==document)=true",
            "parentNode=null",
            "previousSibling=null",
            "specified=true",
            "value=bleh" },
            FF17 = {
            "null",
            "expando=undefined",
            "firstChild=null",
            "lastChild=null",
            "name=custom_attribute",
            "nextSibling=null",
            "nodeName=custom_attribute",
            "nodeType=2",
            "nodeValue=bleh",
            "(ownerDocument==document)=true",
            "parentNode=null",
            "previousSibling=null",
            "specified=true",
            "value=bleh"
            },
            DEFAULT = {
            "null",
            "expando=undefined",
            "firstChild=[object Text]",
            "lastChild=[object Text]",
            "name=custom_attribute",
            "nextSibling=null",
            "nodeName=custom_attribute",
            "nodeType=2",
            "nodeValue=bleh",
            "(ownerDocument==document)=true",
            "parentNode=null",
            "previousSibling=null",
            "specified=true",
            "value=bleh"
            })
    @NotYetImplemented(FF17)
    public void getAttributeNode() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var div = document.getElementById('div2');\n"
            + "      alert(div.getAttributeNode('notExisting'));\n"
            + "      var customAtt = div.getAttributeNode('custom_attribute');\n"
            + "      alertAttributeProperties(customAtt);\n"
            + "    }\n"
            + "    function alertAttributeProperties(att) {\n"
            + "      alert('expando=' + att.expando);\n"
            + "      alert('firstChild=' + att.firstChild);\n"
            + "      alert('lastChild=' + att.lastChild);\n"
            + "      alert('name=' + att.name);\n"
            + "      alert('nextSibling=' + att.nextSibling);\n"
            + "      alert('nodeName=' + att.nodeName);\n"
            + "      alert('nodeType=' + att.nodeType);\n"
            + "      alert('nodeValue=' + att.nodeValue);\n"
            + "      alert('(ownerDocument==document)=' + (att.ownerDocument==document));\n"
            + "      alert('parentNode=' + att.parentNode);\n"
            + "      alert('previousSibling=' + att.previousSibling);\n"
            + "      alert('specified=' + att.specified);\n"
            + "      alert('value=' + att.value);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1'></div>\n"
            + "  <div id='div2' name='blah' custom_attribute='bleh'></div>\n"
            + "  <div id='div3'></div>\n"
            + "</body>\n"
            + "</html>";

        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals("test", webDriver.getTitle());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "left", "left", "right", "right" })
    public void setAttributeNode() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "  <title>test</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      // Get the old alignment.\n"
            + "      var div1 = document.getElementById('div1');\n"
            + "      var a1 = div1.getAttributeNode('align');\n"
            + "      alert(a1.value);\n"
            + "      // Set the new alignment.\n"
            + "      var a2 = document.createAttribute('align');\n"
            + "      a2.value = 'right';\n"
            + "      a1 = div1.setAttributeNode(a2);\n"
            + "      alert(a1.value);\n"
            + "      alert(div1.getAttributeNode('align').value);\n"
            + "      alert(div1.getAttribute('align'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='div1' align='left'></div>\n"
            + "</body>\n"
            + "</html>";
        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals("test", webDriver.getTitle());
    }

    /**
     * Test for <tt>getElementsByTagName</tt>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "all = 4", "row = 2", "by wrong name: 0" })
    public void getElementsByTagName() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "  var a1 = document.getElementsByTagName('td');\n"
            + "  alert('all = ' + a1.length);\n"
            + "  var firstRow = document.getElementById('r1');\n"
            + "  var rowOnly = firstRow.getElementsByTagName('td');\n"
            + "  alert('row = ' + rowOnly.length);\n"
            + "  alert('by wrong name: ' + firstRow.getElementsByTagName('>').length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<table>\n"
            + "<tr id='r1'><td>1</td><td>2</td></tr>\n"
            + "<tr id='r2'><td>3</td><td>4</td></tr>\n"
            + "</table>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "div1", "div2" })
    public void getElementsByTagName2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    for (var f = 0; (formnode = document.getElementsByTagName('form').item(f)); f++)\n"
            + "      for (var i = 0; (node = formnode.getElementsByTagName('div').item(i)); i++)\n"
            + "        alert(node.id);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form>\n"
            + "    <div id='div1'/>\n"
            + "    <div id='div2'/>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that {@link HTMLElement#getElementsByTagName} returns an associative array.
     * Test for bug 1369514.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "first", "second", "third" })
    public void getElementsByTagNameCollection() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var form1 = document.getElementById('form1');\n"
            + "  var elements = form1.getElementsByTagName('input');\n"
            + "  alert(elements['one'].name);\n"
            + "  alert(elements['two'].name);\n"
            + "  alert(elements['three'].name);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<form id='form1'>\n"
            + "<input id='one' name='first' type='text'>\n"
            + "<input id='two' name='second' type='text'>\n"
            + "<input id='three' name='third' type='text'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests that getElementsByTagName('*') returns all child elements, both
     * at the document level and at the element level.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "8", "3" })
    public void getElementsByTagNameAsterisk() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      alert(document.getElementsByTagName('*').length);\n"
            + "      alert(document.getElementById('div').getElementsByTagName('*').length);\n"
            + "   }\n"
            + "</script>\n"
            + "<div id='div'><p>a</p><p>b</p><p>c</p></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "true", "true", "false", "false" })
    public void getElementsByTagNameEquality() throws Exception {
        final String html =
              "<html><body><div id='d'><script>\n"
            + "var div = document.getElementById('d');\n"
            + "alert(document.getElementsByTagName('*') == document.getElementsByTagName('*'));\n"
            + "alert(document.getElementsByTagName('script') == document.getElementsByTagName('script'));\n"
            + "alert(document.getElementsByTagName('foo') == document.getElementsByTagName('foo'));\n"
            + "alert(document.getElementsByTagName('script') == document.getElementsByTagName('body'));\n"
            + "alert(document.getElementsByTagName('script') == div.getElementsByTagName('script'));\n"
            + "</script></div></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test getting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("the class is x")
    public void getClassName() throws Exception {
        final String html
            = "<html><head><style>.x {  font: 8pt Arial bold;  }</style>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var ele = document.getElementById('pid');\n"
            + "    var aClass = ele.className;\n"
            + "    alert('the class is ' + aClass);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test setting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("the class is z")
    public void setClassName() throws Exception {
        final String html
            = "<html><head><style>.x {  font: 8pt Arial bold;  }</style>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var ele = document.getElementById('pid');\n"
            + "    ele.className = 'z';\n"
            + "    var aClass = ele.className;\n"
            + "    alert('the class is ' + aClass);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "if (1 > 2 & 3 < 2) willNotHappen('yo');",
            IE = "\r\nif (1 > 2 & 3 < 2) willNotHappen('yo');")
    public void getInnerHTML() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script id='theScript'>"
            + "if (1 > 2 & 3 < 2) willNotHappen('yo');"
            + "</script>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('theScript');\n"
            + "       alert(myNode.innerHTML);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<form id='myNode'></form>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "<DIV id=i foo=\"\" name=\"\"></DIV>", DEFAULT = "<div id=\"i\" foo=\"\" name=\"\"></div>")
    public void getInnerHTML_EmptyAttributes() throws Exception {
        final String html = "<body onload='alert(document.body.innerHTML)'><div id='i' foo='' name=''></div></body>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <B>Old innerHTML</B>", "New = New cell value" },
            DEFAULT = { "Old = <b>Old innerHTML</b>", "New = New  cell value" })
    public void getSetInnerHTMLSimple_FF() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       alert('Old = ' + myNode.innerHTML);\n"
            + "       myNode.innerHTML = 'New  cell value';\n"
            + "       alert('New = ' + myNode.innerHTML);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode'><b>Old innerHTML</b></p>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test the use of innerHTML to set a new input.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getSetInnerHTMLNewInput() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       myNode.innerHTML = '<input type=\"checkbox\" name=\"myCb\" checked>';\n"
            + "       alert(myNode.myCb.checked);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<form id='myNode'></form>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {
            "Old = <B>Old innerHTML</B>",
            "New = New cell value &amp; \u0110 \u0110" },
            DEFAULT = {
            "Old = <b>Old innerHTML</b>",
            "New = New  cell value &amp; \u0110 \u0110" })
    public void getSetInnerHTMLChar_FF() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       alert('Old = ' + myNode.innerHTML);\n"
            + "       myNode.innerHTML = 'New  cell value &amp; \\u0110 &#272;';\n"
            + "       alert('New = ' + myNode.innerHTML);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<p id='myNode'><b>Old innerHTML</b></p>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that empty tags are not abbreviated into their &lt;tag/&gt; form.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "<DIV id=div><UL></UL></DIV>", "<UL></UL>", "" },
            CHROME = { "<div id=\"div\"><ul></ul></div>", "<ul></ul>", "" },
            FF3_6 = { "undefined", "<ul></ul>", "undefined" },
            FF10 = { "undefined", "<ul></ul>", "undefined" },
            FF17 = { "<div id=\"div\"><ul></ul></div>", "<ul></ul>", "undefined" })
    public void getSetInnerHtmlEmptyTag_FF() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      var div = document.getElementById('div');\n"
            + "      alert(div.outerHTML);\n"
            + "      alert(div.innerHTML);\n"
            + "      alert(div.innerText);\n"
            + "   }\n"
            + "</script>\n"
            + "<div id='div'><ul/></div>"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that attributes containing whitespace are always quoted.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "<DIV id=div><SPAN class=\"a b\"></SPAN></DIV>", "<SPAN class=\"a b\"></SPAN>", "" },
            CHROME = { "<div id=\"div\"><span class=\"a b\"></span></div>", "<span class=\"a b\"></span>", "" },
            FF3_6 = { "undefined", "<span class=\"a b\"></span>", "undefined" },
            FF10 = { "undefined", "<span class=\"a b\"></span>", "undefined" },
            FF17 = { "<div id=\"div\"><span class=\"a b\"></span></div>", "<span class=\"a b\"></span>", "undefined" })
    public void getSetInnerHtmlAttributeWithWhitespace_FF() throws Exception {
        final String html = "<html><body onload='test()'><script>\n"
            + "   function test() {\n"
            + "      var div = document.getElementById('div');\n"
            + "      alert(div.outerHTML);\n"
            + "      alert(div.innerHTML);\n"
            + "      alert(div.innerText);\n"
            + "   }\n"
            + "</script>\n"
            + "<div id='div'><span class='a b'></span></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting innerHTML to empty string.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Empty ChildrenLength: 0")
    public void setInnerHTMLEmpty() throws Exception {
        final String html = "<html><head></head><body>\n"
                + "<div id='testDiv'>foo</div>\n"
                + "<script language='javascript'>\n"
                + "    var node = document.getElementById('testDiv');\n"
                + "    node.innerHTML = '';\n"
                + "    alert('Empty ChildrenLength: ' + node.childNodes.length);\n"
                + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting innerHTML to null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "Null ChildrenLength: 1", DEFAULT = "Null ChildrenLength: 0")
    public void setInnerHTMLNull() throws Exception {
        final String html = "<html><head></head><body>\n"
                + "<div id='testDiv'>foo</div>\n"
                + "<script language='javascript'>\n"
                + "    var node = document.getElementById('testDiv');\n"
                + "    node.innerHTML = null;\n"
                + "    alert('Null ChildrenLength: ' + node.childNodes.length);\n"
                + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test getting <code>outerHTML</code> of a <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Outer = <DIV id=myNode>New cell value</DIV>" },
            DEFAULT = { "Outer = <div id=\"myNode\">New  cell value</div>" },
            FF3_6 = { "Outer = undefined" },
            FF10 = { "Outer = undefined" })
    public void getOuterHTMLFromBlock() throws Exception {
        final String html = createPageForGetOuterHTML("div", "New  cell value", false);
        loadPageWithAlerts2(html);
    }

    /**
     * Test getting <code>outerHTML</code> of a <code>span</code> (inline).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Outer = <SPAN id=myNode>New cell value</SPAN>" },
            DEFAULT = { "Outer = <span id=\"myNode\">New  cell value</span>" },
            FF3_6 = { "Outer = undefined" },
            FF10 = { "Outer = undefined" })
    public void getOuterHTMLFromInline() throws Exception {
        final String html = createPageForGetOuterHTML("span", "New  cell value", false);
        loadPageWithAlerts2(html);
    }

    /**
     * Test getting <code>outerHTML</code> of a <code>br</code> (empty).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Outer = <BR id=myNode>" },
            DEFAULT = { "Outer = <br id=\"myNode\">" },
            FF3_6 = { "Outer = undefined" },
            FF10 = { "Outer = undefined" })
    public void getOuterHTMLFromEmpty() throws Exception {
        final String html = createPageForGetOuterHTML("br", "", true);
        loadPageWithAlerts2(html);
    }

    /**
     * Test getting <code>outerHTML</code> of an unclosed <code>p</code>.<br>
     * Closing <code>p</code> is optional.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Outer = <P id=myNode>New cell value\n</P>" },
            DEFAULT = { "Outer = <p id=\"myNode\">New  cell value\n</p>" },
            FF3_6 = { "Outer = undefined" },
            FF10 = { "Outer = undefined" },
            FF17 = { "Outer = <p id=\"myNode\">New  cell value\n\n</p>" })
    @NotYetImplemented(FF17)
    public void getOuterHTMLFromUnclosedParagraph() throws Exception {
        final String html = createPageForGetOuterHTML("p", "New  cell value", true);
        loadPageWithAlerts2(html);
    }

    private String createPageForGetOuterHTML(final String nodeTag, final String value, final boolean unclosed) {
        return "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "       var myNode = document.getElementById('myNode');\n"
                + "       try {\n"
                + "           alert('Outer = ' + myNode.outerHTML);\n"
                + "       } catch(e) {alert('exception'); }\n"
                + "    }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "    <" + nodeTag + " id='myNode'>" + value + (unclosed ? "" : "</" + nodeTag + ">") + "\n"
                + "</body>\n"
                + "</html>";
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>div</code> (block) to a text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = New cell value" },
            DEFAULT = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = New  cell value" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddTextToBlock() throws Exception {
        final String html = createPageForSetOuterHTML("div", "New  cell value");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>span</code> (inline) to a text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = New cell value" },
            DEFAULT = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = New  cell value" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddTextToInline() throws Exception {
        final String html = createPageForSetOuterHTML("span", "New  cell value");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>div</code> (block) to a <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = <DIV>test</DIV>" },
            DEFAULT = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <div>test</div>" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddBlockToBlock() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<div>test</div>");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>span</code> (inline) to a <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = <DIV>test</DIV>" },
            DEFAULT = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <div>test</div>" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddBlockToInline() throws Exception {
        final String html = createPageForSetOuterHTML("span", "<div>test</div>");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>span</code> (inline) to a <code>span</code> (inline).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = <SPAN>test</SPAN>" },
            DEFAULT = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <span>test</span>" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddInlineToInline() throws Exception {
        final String html = createPageForSetOuterHTML("span", "<span>test</span>");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>div</code> (block) to a <code>span</code> (inline).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = <SPAN>test</SPAN>" },
            DEFAULT = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <span>test</span>" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddInlineToBlock() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<span>test</span>");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to a <code>br</code> (empty).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = <BR>" },
            DEFAULT = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <br>" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddEmpty() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<br>");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to <code>tr</code> (read-only).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11" },
            FF17 = { "-0", "1", "2", "3", "-4", "5", "6", "7", "8", "9", "10", "11" },
            DEFAULT = { "-0", "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9", "-10", "-11" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLToReadOnly() throws Exception {
        final String html =  "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "      var nodeTypes = ['body', 'caption', 'col', 'colgroup', 'head', 'html',\n"
            + "                       'tbody', 'td', 'tfoot', 'th', 'thead', 'tr'];\n"
            + "      for (var i = 0; i < nodeTypes.length; i++) {\n"
            + "        var nodeType = nodeTypes[i];\n"
            + "        var myNode = document.getElementsByTagName(nodeType)[0];\n"
            + "        try {\n"
            + "          myNode.outerHTML = 'test';\n"
            + "          alert('-' + i);\n"
            + "        } catch(e) {alert(i); }\n"
            + "      }\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "    <table>\n"
            + "      <caption></caption>\n"
            + "      <colgroup><col></colgroup>\n"
            + "      <thead><tr><td></td><th></th></tr></thead>\n"
            + "      <tbody></tbody>\n"
            + "      <tfoot></tfoot>\n"
            + "    </table>\n"
            + "    </table>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>p</code> to a <code>div</code> (block).<br>
     * <code>p</code> allows no block elements inside.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "exception" },
            CHROME = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF17 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <div>test</div>" },
            FF = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddBlockToParagraph() throws Exception {
        final String html = createPageForSetOuterHTML("p", "<div>test</div>");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of a <code>p</code> to a <code>p</code>.<br>
     * A following <code>p</code> closes the one before.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "exception" },
            CHROME = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                        "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF17 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <p>test</p>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddParagraphToParagraph() throws Exception {
        final String html = createPageForSetOuterHTML("p", "<p>test</p>");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to an unclosed <code>p</code>.<br>
     * Closing <code>p</code> is optional.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = <P>test</P>" },
            DEFAULT = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <p>test</p>" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddUnclosedParagraph() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<p>test");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> of an <code>a</code> to an <code>a</code>.<br>
     * <code>a</code> allows no <code>a</code> inside.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "exception" },
            CHROME = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                        "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF17 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <a>test</a>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddAnchorToAnchor() throws Exception {
        final String html = createPageForSetOuterHTML("a", "<a>test</a>");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to an XHTML self-closing <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = <DIV></DIV>" },
            DEFAULT = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <div></div>" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddSelfClosingBlock() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<div/>");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to two XHTML self-closing <code>div</code> (block).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = <DIV></DIV><DIV></DIV>" },
            CHROME = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <div></div><div></div>" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF17 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <div><div></div></div>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddMultipleSelfClosingBlock() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<div/><div>");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to an XHTML self-closing <code>span</code> (inline).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = <SPAN></SPAN>" },
            DEFAULT = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <span></span>" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddSelfClosingInline() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<span/>");
        loadPageWithAlerts2(html);
    }

    /**
     * Test setting <code>outerHTML</code> to an XHTML self-closing <code>br</code> (empty).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = <SPAN id=innerNode>Old outerHTML</SPAN>", "New = <BR>" },
            DEFAULT = { "Old = <span id=\"innerNode\">Old outerHTML</span>", "New = <br>" },
            FF3_6 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" },
            FF10 = { "Old = <span id=\"innerNode\">Old outerHTML</span>",
                    "New = <span id=\"innerNode\">Old outerHTML</span>" })
    @NotYetImplemented(FF17)
    public void setOuterHTMLAddSelfClosingEmpty() throws Exception {
        final String html = createPageForSetOuterHTML("div", "<br/>");
        loadPageWithAlerts2(html);
    }

    private String createPageForSetOuterHTML(final String nodeTag, final String newValue) {
        return "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       var innerNode = document.getElementById('innerNode');\n"
            + "       alert('Old = ' + myNode.innerHTML);\n"
            + "       try {\n"
            + "           innerNode.outerHTML = '" + newValue + "';\n"
            + "           alert('New = ' + myNode.innerHTML);\n"
            + "       } catch(e) {alert('exception'); }\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "    <" + nodeTag + " id='myNode'><span id='innerNode'>Old outerHTML</span></" + nodeTag + ">\n"
            + "</body>\n"
            + "</html>";
    }

    /**
     * Test the <tt>#default#clientCaps</tt> default IE behavior.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "body.cpuClass = undefined", "exception" },
            IE = {
            "body.cpuClass = undefined",
            "body.cpuClass = x86",
            "body.cpuClass = undefined" })
    public void addBehaviorDefaultClientCaps() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var body = document.body;\n"
            + "  alert('body.cpuClass = ' + body.cpuClass);\n"
            + "  var id = body.addBehavior('#default#clientCaps');\n"
            + "  alert('body.cpuClass = ' + body.cpuClass);\n"
            + "  var id2 = body.addBehavior('#default#clientCaps');\n"
            + "  body.removeBehavior(id);\n"
            + "  alert('body.cpuClass = ' + body.cpuClass);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

   /**
     * Test the removal of behaviors.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "body.isHomePage = undefined", "exception" },
            IE = { "body.isHomePage = undefined", "body.isHomePage = false", "body.isHomePage = undefined" })
    public void removeBehavior() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var body = document.body;\n"
            + "  alert('body.isHomePage = ' + body.isHomePage);\n"
            + "  var id = body.addBehavior('#default#homePage');\n"
            + "  alert('body.isHomePage = ' + body.isHomePage('not the home page'));\n"
            + "  body.removeBehavior(id);\n"
            + "  alert('body.isHomePage = ' + body.isHomePage);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception", IE = { "BR", "DIV", "2", "3" })
    @NotYetImplemented(FF)
    public void children() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='myDiv'><br/><div><span>test</span></div></div>\n"
            + "<script>\n"
            + "try {\n"
            + "  var oDiv = document.getElementById('myDiv');\n"
            + "  for (var i=0; i<oDiv.children.length; i++)\n"
            + "    alert(oDiv.children(i).tagName);\n"
            + "  var oCol = oDiv.children;\n"
            + "  alert(oCol.length);\n"
            + "  oDiv.insertAdjacentHTML('beforeEnd', '<br>');\n"
            + "  alert(oCol.length);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "Old = Old \r\ninnerText", "New = New cell value" },
            CHROME = { "Old = Old\ninnerText", "New = New cell value" },
            FF = { "Old = undefined", "New = New cell value" })
    public void getSetInnerTextSimple() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>test</title>\n"
            + "    <script>\n"
            + "    function doTest(){\n"
            + "       var myNode = document.getElementById('myNode');\n"
            + "       alert('Old = ' + myNode.innerText);\n"
            + "       myNode.innerText = 'New cell value';\n"
            + "       alert('New = ' + myNode.innerText);\n"
            + "   }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "<div id='myNode'><b>Old <p>innerText</p></b></div>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test the removal of attributes from HTMLElements.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "removeMe", "null" })
    public void removeAttribute() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "       var myDiv = document.getElementById('aDiv');\n"
            + "       alert(myDiv.getAttribute('name'));\n"
            + "       myDiv.removeAttribute('name');\n"
            + "       alert(myDiv.getAttribute('name'));\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'><div id='aDiv' name='removeMe'>\n"
            + "</div></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * IE doesn't really make a distinction between property and attribute...
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "hello", "null", "hello" }, IE = { "hello", "hello", "undefined" })
    public void removeAttribute_property() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function doTest() {\n"
            + "       var myDiv = document.getElementById('aDiv');\n"
            + "       myDiv.foo = 'hello';\n"
            + "       alert(myDiv.foo);\n"
            + "       alert(myDiv.getAttribute('foo'));\n"
            + "       myDiv.removeAttribute('foo');\n"
            + "       alert(myDiv.foo);\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'><div id='aDiv' name='removeMe'>\n"
            + "</div></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test scrolls (real values don't matter currently).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "number", "number", "number", "number", "number", "number", "number", "number" })
    public void scrolls() throws Exception {
        final String html = "<html>\n"
              + "<head>\n"
              + "  <title>Test</title>\n"
              + "</head>\n"
              + "<body>\n"
              + "</div></body>\n"
              + "<div id='div1'>foo</div>\n"
              + "<script>\n"
              + "function alertScrolls(_oElt) {\n"
              + "  alert(typeof _oElt.scrollHeight);\n"
              + "  alert(typeof _oElt.scrollWidth);\n"
              + "  alert(typeof _oElt.scrollLeft);\n"
              + "  _oElt.scrollLeft = 123;\n"
              + "  alert(typeof _oElt.scrollTop);\n"
              + "  _oElt.scrollTop = 123;\n"
              + "}\n"
              + "alertScrolls(document.body);\n"
              + "alertScrolls(document.getElementById('div1'));\n"
              + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "0", "0", "0", "0", "0", "17", "0", "0" })
    public void scrollLeft_overflowScroll() throws Exception {
        scrollLeft("scroll");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "0", "0", "0", "0", "0", "17", "0", "0" })
    public void scrollLeft_overflowAuto() throws Exception {
        scrollLeft("auto");
    }

    /**
     * NOTE: When running this test with Firefox (3.6, at least), it's important to reload the page with Ctrl+F5
     * in order to completely clear the cache; otherwise, Firefox appears to incorrectly cache some style attributes.
     * @throws Exception if an error occurs
     */
    private void scrollLeft(final String overflow) throws Exception {
        final String html
            = "<html><body onload='test()'>\n"
            + "<div id='d1' style='width:100px;height:100px;background-color:green;'>\n"
            + "  <div id='d2' style='width:50px;height:50px;background-color:blue;'></div>\n"
            + "</div>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var d1 = document.getElementById('d1'), d2 = document.getElementById('d2');\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.scrollLeft = -1;\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.scrollLeft = 5;\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d2.style.width = '200px';\n"
            + "  d2.style.height = '200px';\n"
            + "  d1.scrollLeft = 7;\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.style.overflow = '" + overflow + "';\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.scrollLeft = 17;\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.style.overflow = 'visible';\n"
            + "  alert(d1.scrollLeft);\n"
            + "  d1.scrollLeft = 19;\n"
            + "  alert(d1.scrollLeft);\n"
            + "}\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "0", "0", "0", "0", "0", "17", "0", "0" })
    public void scrollTop_overflowScroll() throws Exception {
        scrollTop("scroll");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "0", "0", "0", "0", "0", "17", "0", "0" })
    public void scrollTop_overflowAuto() throws Exception {
        scrollTop("auto");
    }

    /**
     * NOTE: When running this test with Firefox (3.6, at least), it's important to reload the page with Ctrl+F5
     * in order to completely clear the cache; otherwise, Firefox appears to incorrectly cache some style attributes.
     * @throws Exception if an error occurs
     */
    private void scrollTop(final String overflow) throws Exception {
        final String html
            = "<html><body onload='test()'>\n"
            + "<div id='d1' style='width:100px;height:100px;background-color:green;'>\n"
            + "  <div id='d2' style='width:50px;height:50px;background-color:blue;'></div>\n"
            + "</div>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var d1 = document.getElementById('d1'), d2 = document.getElementById('d2');\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.scrollTop = -1;\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.scrollTop = 5;\n"
            + "  alert(d1.scrollTop);\n"
            + "  d2.style.width = '200px';\n"
            + "  d2.style.height = '200px';\n"
            + "  d1.scrollTop = 7;\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.style.overflow = '" + overflow + "';\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.scrollTop = 17;\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.style.overflow = 'visible';\n"
            + "  alert(d1.scrollTop);\n"
            + "  d1.scrollTop = 19;\n"
            + "  alert(d1.scrollTop);\n"
            + "}\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests that JavaScript scrollIntoView() function doesn't fail.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ok")
    public void scrollIntoView() throws Exception {
        final String html = "<html>\n"
              + "<body>\n"
              + "<script id='me'>document.getElementById('me').scrollIntoView(); alert('ok');</script>\n"
              + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests the offsetParent property.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({
            "element: span3 offsetParent: td2", "element: td2 offsetParent: table2",
            "element: tr2 offsetParent: table2", "element: table2 offsetParent: td1",
            "element: td1 offsetParent: table1", "element: tr1 offsetParent: table1",
            "element: table1 offsetParent: body1", "element: span2 offsetParent: body1",
            "element: span1 offsetParent: body1", "element: div1 offsetParent: body1",
            "element: body1 offsetParent: null" })
    public void offsetParent_Basic() throws Exception {
        final String html = "<html><head>\n"
            + "<script type='text/javascript'>\n"
            + "function alertOffsetParent(id) {\n"
            + "  var element = document.getElementById(id);\n"
            + "  var offsetParent = element.offsetParent;\n"
            + "  var alertMessage = 'element: ' + element.id + ' offsetParent: ';\n"
            + "  if (offsetParent) {\n"
            + "    alertMessage += offsetParent.id;\n"
            + "  }\n"
            + "  else {\n"
            + "    alertMessage += offsetParent;\n"
            + "  }\n"
            + "  alert(alertMessage);\n"
            + "}\n"
            + "function test() {\n"
            + "  alertOffsetParent('span3');\n"
            + "  alertOffsetParent('td2');\n"
            + "  alertOffsetParent('tr2');\n"
            + "  alertOffsetParent('table2');\n"
            + "  alertOffsetParent('td1');\n"
            + "  alertOffsetParent('tr1');\n"
            + "  alertOffsetParent('table1');\n"
            + "  alertOffsetParent('span2');\n"
            + "  alertOffsetParent('span1');\n"
            + "  alertOffsetParent('div1');\n"
            + "  alertOffsetParent('body1');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body id='body1' onload='test()'>\n"
            + "<div id='div1'>\n"
            + "  <span id='span1'>\n"
            + "    <span id='span2'>\n"
            + "      <table id='table1'>\n"
            + "        <tr id='tr1'>\n"
            + "          <td id='td1'>\n"
            + "            <table id='table2'>\n"
            + "              <tr id='tr2'>\n"
            + "                <td id='td2'>\n"
            + "                  <span id='span3'>some text</span>\n"
            + "                </td>\n"
            + "              </tr>\n"
            + "            </table>\n"
            + "          </td>\n"
            + "        </tr>\n"
            + "      </table>\n"
            + "    </span>\n"
            + "  </span>\n"
            + "</div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests the offsetParent property.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "null", "null" }, IE = { "exception", "null" })
    public void offsetParent_newElement() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  var oNew = document.createElement('span');\n"
            + "  alert(oNew.offsetParent);\n"
            + "} catch(e) { alert('exception') }\n"
            + "var fragment = document.createDocumentFragment();\n"
            + "fragment.appendChild(oNew);\n"
            + "alert(oNew.offsetParent);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests the offsetParent property, including the effects of CSS "position" attributes.
     * Based on <a href="http://dump.testsuite.org/2006/dom/style/offset/spec#offsetparent">this work</a>.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "null", "body", "body", "body", "body", "body",
            "body", "body", "h1", "i1", "td", "td", "td", "body", "body" },
            FF = { "null", "body", "body", "body", "body", "body",
            "f1", "body", "h1", "i1", "td", "body", "td", "body", "body" },
            CHROME = { "null", "body", "exception" })
    public void offsetParent_WithCSS() throws Exception {
        final String html = "<html>\n"
            + "  <body id='body' onload='test()'>\n"
            + "    <div id='a1'><div id='a2'>x</div></div>\n"
            + "    <div id='b1'><div id='b2' style='position:fixed'>x</div></div>\n"
            + "    <div id='c1'><div id='c2' style='position:static'>x</div></div>\n"
            + "    <div id='d1'><div id='d2' style='position:absolute'>x</div></div>\n"
            + "    <div id='e1'><div id='e2' style='position:relative'>x</div></div>\n"
            + "    <div id='f1' style='position:fixed'><div id='f2'>x</div></div>\n"
            + "    <div id='g1' style='position:static'><div id='g2'>x</div></div>\n"
            + "    <div id='h1' style='position:absolute'><div id='h2'>x</div></div>\n"
            + "    <div id='i1' style='position:relative'><div id='i2'>x</div></div>\n"
            + "    <table id='table'>\n"
            + "      <tr id='tr'>\n"
            + "        <td id='td'>\n"
            + "          <div id='j1'><div id='j2'>x</div></div>\n"
            + "          <div id='k1'><div id='k2' style='position:fixed'>x</div></div>\n"
            + "          <div id='l1'><div id='l2' style='position:static'>x</div></div>\n"
            + "          <div id='m1'><div id='m2' style='position:absolute'>x</div></div>\n"
            + "          <div id='n1'><div id='n2' style='position:relative'>x</div></div>\n"
            + "        </td>\n"
            + "      </tr>\n"
            + "    </table>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "      try {\n"
            + "                                                              // FF   IE   \n"
            + "        alert(document.getElementById('body').offsetParent);  // null null \n"
            + "        alert(document.getElementById('a2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('b2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('c2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('d2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('e2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('f2').offsetParent.id); // f1   body \n"
            + "        alert(document.getElementById('g2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('h2').offsetParent.id); // h1   h1   \n"
            + "        alert(document.getElementById('i2').offsetParent.id); // i1   i1   \n"
            + "        alert(document.getElementById('j2').offsetParent.id); // td   td   \n"
            + "        alert(document.getElementById('k2').offsetParent.id); // body td   \n"
            + "        alert(document.getElementById('l2').offsetParent.id); // td   td   \n"
            + "        alert(document.getElementById('m2').offsetParent.id); // body body \n"
            + "        alert(document.getElementById('n2').offsetParent.id); // body body \n"
            + "      } catch (e) { alert('exception'); }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test for bug https://sourceforge.net/tracker/?func=detail&atid=448266&aid=1960512&group_id=47038.
     * @throws Exception if the test fails
     */
    @Test
    public void offsetParent_withSelectors() throws Exception {
        final String html = "<html><head><style>\n"
            + "div ul > li {\n"
            + "  font-size: xx-small;\n"
            + "}\n"
            + "</style><script>\n"
            + "function test() {\n"
            + "  var divThing = document.getElementById('outer');\n"
            + "  while (divThing) {\n"
            + "    divThing = divThing.offsetParent;\n"
            + "  }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<div id='outer'></div>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "undefined", "undefined", "undefined",
            "undefined", "123", "from myFunction", "123", "from myFunction" },
            IE = { "undefined", "undefined", "undefined", "undefined", "exception" })
    public void prototype() throws Exception {
        final String html = "<html><head><title>Prototype test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "    var d = document.getElementById('foo');\n"
            + "    alert(d.foo);\n"
            + "    alert(d.myFunction);\n"
            + "    var link = document.getElementById('testLink');\n"
            + "    alert(link.foo);\n"
            + "    alert(link.myFunction);\n"
            + "    HTMLElement.prototype.foo = 123;\n"
            + "    alert(HTMLElement.foo);\n"
            + "    HTMLElement.prototype.myFunction = function() { return 'from myFunction'; };\n"
            + "    alert(d.foo);\n"
            + "    alert(d.myFunction());\n"
            + "    alert(link.foo);\n"
            + "    alert(link.myFunction());\n"
            + "} catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script></head><body onload='test()''>\n"
            + "<div id='foo'>bla</div>\n"
            + "<a id='testLink' href='foo'>bla</a>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * 'Element' and 'HTMLElement' prototypes are synonyms.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "in selectNodes", IE = "exception")
    public void prototype_Element() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "try {\n"
            + "    Element.prototype.selectNodes = function(sExpr){\n"
            + "      alert('in selectNodes');\n"
            + "    }\n"
            + "    document.getElementById('myDiv').selectNodes();\n"
            + "} catch (e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true" }, IE = "exception")
    public void instanceOf() throws Exception {
        final String html = "<html><head><title>instanceof test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "    var d = document.getElementById('foo');\n"
            + "    alert(d instanceof HTMLDivElement);\n"
            + "    var link = document.getElementById('testLink');\n"
            + "    alert(link instanceof HTMLAnchorElement);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script></head><body onload='test()''>\n"
            + "<div id='foo'>bla</div>\n"
            + "<a id='testLink' href='foo'>bla</a>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "null", "[object]" },
            FF3_6 = { "undefined", "undefined" },
            DEFAULT = { "null", "[object HTMLBodyElement]" })
    public void parentElement() throws Exception {
        final String html
            = "<html id='htmlID'>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div id='divID'/>\n"
            + "<script language=\"javascript\">\n"
            + "    alert(document.getElementById('htmlID').parentElement);\n"
            + "    alert(document.getElementById('divID' ).parentElement);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(0, 0, 0)")
    @Browsers(IE)
    public void currentStyle() throws Exception {
        style("currentStyle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    @NotYetImplemented(FF)
    public void runtimeStyle() throws Exception {
        style("runtimeStyle");
    }

    private void style(final String styleProperty) throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myDiv')." + styleProperty + ".color);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<div id='myDiv'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0" })
    public void clientLeftTop() throws Exception {
        final String html = "<html><body>"
            + "<div id='div1'>hello</div><script>\n"
            + "  var d1 = document.getElementById('div1');\n"
            + "  alert(d1.clientLeft);\n"
            + "  alert(d1.clientTop);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Another nice feature of the IE.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", "0" }, IE = { "2", "2" })
    public void clientLeftTop_documentElement() throws Exception {
        final String html =
              "<!DOCTYPE HTML "
            +      "PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
            + "<html>\n"
            + "<body>"
            + "<div id='div1'>hello</div><script>\n"
            + "  var d1 = document.documentElement;\n"
            + "  alert(d1.clientLeft);\n"
            + "  alert(d1.clientTop);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "4", "4" }, IE = { "0", "0" })
    public void clientLeftTopWithBorder() throws Exception {
        final String html = "<html><body>"
            + "<div id='div1' style='border: 4px solid black;'>hello</div><script>\n"
            + "  var d1 = document.getElementById('div1');\n"
            + "  alert(d1.clientLeft);\n"
            + "  alert(d1.clientTop);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object ClientRect]", IE = "[object]")
    public void getBoundingClientRect() throws Exception {
        final String html = "<html><body><div id='div1'>hello</div><script>\n"
            + "try {\n"
            + "  var d1 = document.getElementById('div1');\n"
            + "  var pos = d1.getBoundingClientRect();\n"
            + "  alert(pos);\n"
            + "} catch (e) { alert('exception');}\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "400", "100" }, IE = { "402", "102" })
    public void getBoundingClientRect2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var pos = d1.getBoundingClientRect();\n"
            + "    alert(pos.left);\n"
            + "    alert(pos.top);\n"
            + "    } catch (e) { alert('exception');}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='outer' style='position: absolute; left: 400px; top: 100px; width: 50px; height: 80px;'>"
            + "<div id='div1'></div></div>"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", "100", "100", "50" }, IE = { "2", "102", "102", "52" })
    public void getBoundingClientRect_Scroll() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var d1 = document.getElementById('outer');\n"
            + "      d1.scrollTop=150;\n"
            + "      var pos = d1.getBoundingClientRect();\n"
            + "      alert(pos.left);\n"
            + "      alert(pos.top);\n"

            + "      d1 = document.getElementById('div1');\n"
            + "      pos = d1.getBoundingClientRect();\n"
            + "      alert(pos.left);\n"
            + "      alert(pos.top);\n"
            + "    } catch (e) { alert('exception');}\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='outer' "
               + "style='position: absolute; height: 500px; width: 500px; top: 100px; left: 0px; overflow:auto'>\n"
            + "  <div id='div1' "
               + "style='position: absolute; height: 100px; width: 100px; top: 100px; left: 100px; z-index:99;'>"
               + "</div>\n"
            + "  <div id='div2' "
              + "style='position: absolute; height: 100px; width: 100px; top: 100px; left: 300px; z-index:99;'></div>\n"
            + "  <div style='position: absolute; top: 1000px;'>way down</div>\n"
            + "</div>"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(FF)
    public void getClientRects() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    d1.getClientRects();\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "null", "#document-fragment" }, DEFAULT = { "null", "null" })
    public void innerHTML_parentNode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div1 = document.createElement('div');\n"
            + "    alert(div1.parentNode);\n"
            + "    div1.innerHTML='<p>hello</p>';\n"
            + "    if(div1.parentNode)\n"
            + "      alert(div1.parentNode.nodeName);\n"
            + "    else\n"
            + "      alert(div1.parentNode);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "null", "#document-fragment" }, DEFAULT = { "null", "null" })
    public void innerText_parentNode() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div1 = document.createElement('div');\n"
            + "    alert(div1.parentNode);\n"
            + "    div1.innerText='<p>hello</p>';\n"
            + "    if(div1.parentNode)\n"
            + "      alert(div1.parentNode.nodeName);\n"
            + "    else\n"
            + "      alert(div1.parentNode);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "false", "true", "false" }, DEFAULT = { "true", "true", "true" })
    public void uniqueID() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     var div2 = document.getElementById('div2');\n"
            + "     alert(div1.uniqueID == undefined);\n"
            + "     alert(div1.uniqueID == div1.uniqueID);\n"
            + "     alert(div1.uniqueID == div2.uniqueID);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "  <div id='div2'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests if element.uniqueID starts with 'ms__id', and is lazily created.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "true", "true" })
    public void uniqueIDFormatIE() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     var div2 = document.getElementById('div2');\n"
            + "     var id2 = div2.uniqueID;\n"
            + "     //as id2 is retrieved before getting id1, id2 should be < id1;\n"
            + "     var id1 = div1.uniqueID;\n"
            + "     alert(id1.substring(0, 6) == 'ms__id');\n"
            + "     var id1Int = parseInt(id1.substring(6, id1.length));\n"
            + "     var id2Int = parseInt(id2.substring(6, id2.length));\n"
            + "     alert(id2Int < id1Int);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "  <div id='div2'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(FF)
    public void setExpression() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     div1.setExpression('title','id');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(FF)
    public void removeExpression() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "     var div1 = document.getElementById('div1');\n"
            + "     div1.setExpression('title','id');\n"
            + "     div1.removeExpression('title');"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "clicked", IE = "exception")
    public void dispatchEvent() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function foo() {\n"
            + "try {\n"
            + "    var e = document.createEvent('MouseEvents');\n"
            + "    e.initMouseEvent('click', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);\n"
            + "    var d = document.getElementById('d');\n"
            + "    var canceled = !d.dispatchEvent(e);\n"
            + "} catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='foo()'><div id='d' onclick='alert(\"clicked\")'>foo</div></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "false" }, IE = "exception")
    public void hasAttribute() throws Exception {
        final String html =
              "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "try {\n"
            + "    var elt = document.body;\n"
            + "    alert(elt.hasAttribute('onload'));\n"
            + "    alert(elt.hasAttribute('onLoad'));\n"
            + "    alert(elt.hasAttribute('foo'));\n"
            + "} catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "undefined", "x86", "0", "undefined" })
    public void getComponentVersion() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  alert(document.body.cpuClass);\n"
            + "  document.body.style.behavior='url(#default#clientCaps)';\n"
            + "  alert(document.body.cpuClass);\n"
            + "  var ver=document.body.getComponentVersion('{E5D12C4E-7B4F-11D3-B5C9-0050045C3C96}','ComponentID');\n"
            + "  alert(ver.length);\n"
            + "  document.body.style.behavior='';\n"
            + "  alert(document.body.cpuClass);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "30", "40" }, DEFAULT = { "36", "46" })
    public void clientWidthAndHeight() throws Exception {
        final String html =
              "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var myDiv = document.getElementById('myDiv');\n"
            + "    alert(myDiv.clientWidth);\n"
            + "    alert(myDiv.clientHeight);\n"
            + "  }\n"
            + "</script>\n"
            + "<style>#myDiv { width:30px; height:40px; padding:3px; border:5px; margin:7px; }</style>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for https://sourceforge.net/tracker/?func=detail&aid=2022578&group_id=47038&atid=448266.
     * @throws Exception if the test fails
     */
    @Test
    public void stackOverflowWithInnerHTML() throws Exception {
        final String html = "<html><head><title>Recursion</title></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "     document.body.innerHTML = unescape(document.body.innerHTML);\n"
            + "</script></body></html>";
        final WebDriver webDriver = loadPageWithAlerts2(html);
        assertEquals("Recursion", webDriver.getTitle());
    }

    /**
     * Test setting the class for the element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "x", "null", "[object Attr]", "null", "x", "byClassname" },
            IE = { "null", "x", "[object]", "null", "null", "byClassname" })
    public void class_className_attribute() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "    var e = document.getElementById('pid');\n"
            + "    alert(e.getAttribute('class'));\n"
            + "    alert(e.getAttribute('className'));\n"
            + "    alert(e.getAttributeNode('class'));\n"
            + "    alert(e.getAttributeNode('className'));\n"
            + "    e.setAttribute('className', 'byClassname');\n"
            + "    alert(e.getAttribute('class'));\n"
            + "    alert(e.getAttribute('className'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "-undefined-x", "null-x-null", "null-[object Attr]-null", "null-[object Attr]-null",
            "x-byClassname", "[object Attr]-[object Attr]", "byClassname-byClassname", "[object Attr]-[object Attr]" },
            IE = { "-undefined-x", "-null-x", "[object]-[object]-null", "[object]-[object]-null", "null-byClassname",
            "[object]-null", "byClassname-byClassname", "[object]-null" })
    public void class_className_attribute2() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function doTest() {\n"
            + "  var e = document.getElementById('pid');\n"
            + "  alert(e['lang'] + '-' + e['class'] + '-' + e['className']);\n"
            + "  alert(e.getAttribute('lang') + '-' + e.getAttribute('class') + '-' + e.getAttribute('className'));\n"
            + "  alert(e.getAttributeNode('lang') + '-' + e.getAttributeNode('class') + '-' + "
            + "e.getAttributeNode('className'));\n"
            + "  alert(e.attributes.getNamedItem('lang') + '-' + e.attributes.getNamedItem('class') + '-' + "
            + "e.attributes.getNamedItem('className'));\n"
            + "  e.setAttribute('className', 'byClassname');\n"
            + "  alert(e.getAttribute('class') + '-' + e.getAttribute('className'));\n"
            + "  alert(e.getAttributeNode('class') + '-' + e.getAttributeNode('className'));\n"
            + "  e.setAttribute('class', 'byClassname');\n"
            + "  alert(e.getAttribute('class') + '-' + e.getAttribute('className'));\n"
            + "  alert(e.getAttributeNode('class') + '-' + e.getAttributeNode('className'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p id='pid' class='x'>text</p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3_6 = "exception",
            DEFAULT = { "true", "true", "true", "false", "false", "false", "false", "true", "true", "false", "false" })
    public void contains() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var div2 = document.getElementById('div2');\n"
            + "  var text = div2.firstChild;\n"
            + "  var div3 = document.getElementById('div3');\n"
            + "  alert(div1.contains(div1));\n"
            + "  alert(div1.contains(div2));\n"
            + "  alert(div1.contains(div3));\n"
            + "  alert(div1.contains(div4));\n"
            + "  alert(div2.contains(div1));\n"
            + "  alert(div3.contains(div1));\n"
            + "  alert(div4.contains(div1));\n"
            + "  alert(div2.contains(div3));\n"
            + "  alert(div2.contains(text));\n"
            + "  alert(div3.contains(text));\n"
            + "  alert(text.contains(div3));\n"
            + "} catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'>\n"
            + "  <div id='div2'>hello\n"
            + "    <div id='div3'>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "</div>\n"
            + "<div id='div4'>\n"
            + "</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void contains_invalid_argument() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  alert(document.body.contains([]));\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "undefined", IE = "defined")
    public void filters() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var defined = typeof(div1.filters) != 'undefined';\n" // "unknown" for IE6!!!
            + "  alert(defined ? 'defined' : 'undefined');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='div1'>\n"
            + "</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "> myClass <", "> myId  <" })
    public void attributes_trimmed() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var div1 = document.body.firstChild;\n"
            + "  alert('>' + div1.className + '<');\n"
            + "  alert('>' + div1.id + '<');\n"
            + "}\n"
            + "</script></head><body onload='test()'>"
            + "<div id=' myId  ' class=' myClass '>\n"
            + "hello"
            + "</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function", "* => body: 0, div1: 0", "foo => body: 3, div1: 1", "foo red => body: 1, div1: 0",
            "red foo => body: 1, div1: 0", "blue foo => body: 0, div1: 0", "null => body: 0, div1: 0" },
            IE = { "undefined", "exception" })
    public void getElementsByClassName() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function test(x) {\n"
            + "    var b = document.body;\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    var s = x + ' => body: ' + b.getElementsByClassName(x).length;\n"
            + "    s += ', div1: ' + div1.getElementsByClassName(x).length;\n"
            + "    alert(s);\n"
            + "}\n"
            + "function doTest() {\n"
            + "    var b = document.body;\n"
            + "    var div1 = document.getElementById('div1');\n"
            + "    alert(typeof document.body.getElementsByClassName);\n"
            + "    try {\n"
            + "      test('*');\n"
            + "      test('foo');\n"
            + "      test('foo red');\n"
            + "      test('red foo');\n"
            + "      test('blue foo');\n"
            + "      test(null);\n"
            + "    }\n"
            + "    catch (e) { alert('exception') }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div class='foo' id='div1'>\n"
            + "  <span class='c2'>hello</span>\n"
            + "  <span class='foo' id='span2'>World!</span>\n"
            + "</div>\n"
            + "<span class='foo red' id='span3'>again</span>\n"
            + "<span class='red' id='span4'>bye</span>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3_6 = { "undefined", "undefined" },
            DEFAULT = { "null", "[object HTMLDivElement]" },
            IE = { "null", "[object]" })
    public void parentElement2() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var fragment = document.createDocumentFragment();\n"
            + "  var div = document.createElement('div');\n"
            + "  var bold = document.createElement('b');\n"
            + "  fragment.appendChild(div);\n"
            + "  div.appendChild(bold);\n"
            + "  alert(div.parentElement);\n"
            + "  alert(bold.parentElement);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * The method doScroll() should throw an exception if document is not yet loaded,
     * have a look into <a href="http://javascript.nwbox.com/IEContentLoaded/">this</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "exception", "exception" }, IE = { "exception", "success" })
    public void doScroll() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    document.documentElement.doScroll('left');\n"
            + "    alert('success');\n"
            + "  } catch (e) {\n"
            + "    alert('exception');\n"
            + "  }\n"
            + "}\n"
            + "test();\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts({ "div2", "null", "div3", "div4", "div6", "div7", "null" })
    public void removeNode() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var div1 = document.getElementById('div1');\n"
            + "  var div2 = document.getElementById('div2');\n"
            + "  alert(div1.firstChild.id);\n"
            + "  alert(div2.removeNode().firstChild);\n"
            + "  alert(div1.firstChild.id);\n"
            + "  alert(div1.firstChild.nextSibling.id);\n"
            + "\n"
            + "  var div5 = document.getElementById('div5');\n"
            + "  var div6 = document.getElementById('div6');\n"
            + "  alert(div5.firstChild.id);\n"
            + "  alert(div6.removeNode(true).firstChild.id);\n"
            + "  alert(div5.firstChild);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1'><div id='div2'><div id='div3'></div><div id='div4'></div></div></div>\n"
            + "  <div id='div5'><div id='div6'><div id='div7'></div><div id='div8'></div></div></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "false", "hello", "true" }, IE = { "undefined", "true", "undefined", "false" })
    public void firefox__proto__() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var div1 = document.createElement('div');\n"
            + "  alert(div1.myProp);\n"
            + "  var p1 = div1['__proto__'];\n"
            + "  alert(p1 == undefined);\n"
            + "  if (p1)\n"
            + "    p1.myProp = 'hello';\n"
            + "  alert(div1.myProp);\n"
            + "  alert(p1 !== document.createElement('form')['__proto__']);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    @Alerts(IE = { "false,false,false,false,false,false,false", "false,false,false,false,false,true,true" })
    public void clearAttributes() throws Exception {
        final String html
            = "<input type='text' id='i' name='i' style='color:red' onclick='alert(1)' custom1='a' /><script>\n"
            + "function u(o) { return typeof o == 'undefined'; }\n"
            + "var i = document.getElementById('i');\n"
            + "i.custom2 = 'b';\n"
            + "alert([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "       u(i.custom1), u(i.custom2)].join(','));\n"
            + "i.clearAttributes();\n"
            + "alert([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "       u(i.custom1), u(i.custom2)].join(','));\n"
            + "</script>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(IE)
    public void mergeAttributes() throws Exception {
        mergeAttributes("i2", "false,false,false,false,false,true,true", "i", "",
            "false,false,false,false,false,false,false", "i", "");
        mergeAttributes("i2, true", "false,false,false,false,false,true,true", "i", "",
            "false,false,false,false,false,false,false", "i", "");
        mergeAttributes("i2, false", "false,false,false,false,false,true,true", "i", "",
            "false,false,false,false,false,false,false", "i2", "i2");
    }

    private void mergeAttributes(final String params, final String... expectedAlerts) throws Exception {
        final String html
            = "<input type='text' id='i' />\n"
            + "<input type='text' id='i2' name='i2' style='color:red' onclick='alert(1)' custom1='a' />\n"
            + "<script>\n"
            + "function u(o) { return typeof o == 'undefined'; }\n"
            + "var i = document.getElementById('i');\n"
            + "var i2 = document.getElementById('i2');\n"
            + "i2.custom2 = 'b';\n"
            + "alert([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "       u(i.custom1), u(i.custom2)].join(','));\n"
            + "alert(i.id);\n"
            + "alert(i.name);\n"
            + "i.mergeAttributes(" + params + ");\n"
            + "alert([u(i.type), u(i.id), u(i.name), u(i.style), u(i.onclick),"
            + "       u(i.custom1), u(i.custom2)].join(','));\n"
            + "alert(i.id);\n"
            + "alert(i.name);\n"
            + "</script>";

        setExpectedAlerts(expectedAlerts);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false", IE = "true")
    public void document() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.body.document === document);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3_6 = { "undefined", "true" }, CHROME = { "undefined", "true" },
            DEFAULT = "exception")
    @NotYetImplemented(FF)
    public void prototype_innerHTML() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  alert(HTMLElement.prototype.innerHTML);\n"
            + "  var myFunc = function() {};\n"
            + "  HTMLElement.prototype.innerHTML = myFunc;\n"
            + "  alert(HTMLElement.prototype.innerHTML == myFunc);\n"
            + "} catch (e) { alert('exception') }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = {"", "#0000aa", "#000000", "#ffebcd", "#ab00e0", "#b00e00" },
            IE9 = {"", "#0000aa", "#0", "blanchedalmond", "#ab00e", "#b00e0" },
            DEFAULT = {"", "#0000aa", "x", "BlanchedAlmond", "aBlue", "bluex" },
            FF3_6 = {"", "#0000aa", "#000000", "#ffebcd", "#ab00e0", "#b00e00" })
    public void setColorAttribute() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(b.vLink);\n"
            + "        document.vlinkColor = '#0000aa';\n"
            + "        alert(b.vLink);\n"
            + "        document.vlinkColor = 'x';\n"
            + "        alert(b.vLink);\n"
            + "        document.vlinkColor = 'BlanchedAlmond';\n"
            + "        alert(b.vLink);\n"
            + "        document.vlinkColor = 'aBlue';\n"
            + "        alert(b.vLink);\n"
            + "        document.vlinkColor = 'bluex';\n"
            + "        alert(b.vLink);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "<SPAN onclick=\"var f = &quot;hello&quot; + 'world'\">test span</SPAN>",
            DEFAULT = "<span onclick=\"var f = &quot;hello&quot; + 'world'\">test span</span>")
    public void innerHTMLwithQuotes() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.getElementById('foo').innerHTML);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='foo'><span onclick=\"var f = &quot;hello&quot; + 'world'\">test span</span></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "button", "getAttributeNS() not supported" },
            FF17 = { "button", "null", "false", "true" },
            DEFAULT = { "button", "", "false", "true" })
    @NotYetImplemented(FF17)
    public void attributeNS() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('foo');\n"
            + "    alert(e.getAttribute('type'));\n"
            + "    try {\n"
            + "      alert(e.getAttributeNS('bar', 'type'));\n"
            + "      alert(e.hasAttributeNS('bar', 'type'));\n"
            + "      e.removeAttributeNS('bar', 'type');\n"
            + "      alert(e.hasAttribute('type'));\n"
            + "    } catch (e) {alert('getAttributeNS() not supported')}\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='foo' type='button' value='someValue'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "undefined", FF3_6 = "undefined", DEFAULT = "[object DOMStringMap]")
    public void dataset() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.body.dataset);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = "t", FF = "")
    public void setAttribute_className() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test(){\n"
            + "    var div = document.createElement('div');\n"
            + "    div.setAttribute('className', 't');\n"
            + "    alert(div.className);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = "", DEFAULT = "t")
    public void setAttribute_class() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test(){\n"
            + "    var div = document.createElement('div');\n"
            + "    div.setAttribute('class', 't');\n"
            + "    alert(div.className);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("")
    public void setAttribute_className_standards() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
            + "<script>\n"
            + "  function test(){\n"
            + "    var div = document.createElement('div');\n"
            + "    div.setAttribute('className', 't');\n"
            + "    alert(div.className);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("t")
    public void setAttribute_class_standards() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head>\n"
            + "<script>\n"
            + "  function test(){\n"
            + "    var div = document.createElement('div');\n"
            + "    div.setAttribute('class', 't');\n"
            + "    alert(div.className);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "null", "", "null", "undefined" }, IE = { "", "", "null", "undefined" })
    public void getAttribute2() throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "       var form = document.getElementById('testForm');\n"
                + "       alert(form.getAttribute('target'));\n"
                + "       alert(form.target);\n"
                + "       alert(form.getAttribute('target222'));\n"
                + "       alert(form.target222);\n"
                + "   }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<form id='testForm' action='#' method='get'>\n"
                + "</form>\n"
                + "</body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "null", "", "null", "undefined" })
    public void getAttribute2_standards() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "       var form = document.getElementById('testForm');\n"
                + "       alert(form.getAttribute('target'));\n"
                + "       alert(form.target);\n"
                + "       alert(form.getAttribute('target222'));\n"
                + "       alert(form.target222);\n"
                + "   }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "<form id='testForm' action='#' method='get'>\n"
                + "</form>\n"
                + "</body>\n"
                + "</html>";

        loadPageWithAlerts2(html);
    }

    private String outerHTML(final String elementName) {
        return "<html><head>\n"
                + "  <script>\n"
                + "    function test(){\n"
                + "      var value = document.createElement('" + elementName + "').cloneNode(true).outerHTML;\n"
                + "      while (value && (value.charAt(0) == '\\r' || value.charAt(0) == '\\n'))\n"
                + "        value = value.substring(1);\n"
                + "      alert(value);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'></body></html>";
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<abbr></abbr>", IE = "<ABBR></ABBR>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_abbr() throws Exception {
        loadPageWithAlerts2(outerHTML("abbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<acronym></acronym>", IE = "<ACRONYM></ACRONYM>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_acronym() throws Exception {
        loadPageWithAlerts2(outerHTML("acronym"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<a></a>", IE = "<A></A>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_a() throws Exception {
        loadPageWithAlerts2(outerHTML("a"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<address></address>", IE = "<ADDRESS></ADDRESS>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_address() throws Exception {
        loadPageWithAlerts2(outerHTML("address"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<applet></applet>", IE = "<APPLET></APPLET>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_applet() throws Exception {
        loadPageWithAlerts2(outerHTML("applet"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<area>", IE = "<AREA>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_area() throws Exception {
        loadPageWithAlerts2(outerHTML("area"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<audio></audio>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_audio() throws Exception {
        loadPageWithAlerts2(outerHTML("audio"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<bgsound>", IE = "<BGSOUND>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_bgsound() throws Exception {
        loadPageWithAlerts2(outerHTML("bgsound"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<base>", IE = "<BASE>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_base() throws Exception {
        loadPageWithAlerts2(outerHTML("base"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<basefont>", IE = "<BASEFONT></BASEFONT>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_basefont() throws Exception {
        loadPageWithAlerts2(outerHTML("basefont"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<bdo></bdo>", IE = "<BDO></BDO>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_bdo() throws Exception {
        loadPageWithAlerts2(outerHTML("bdo"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<big></big>", IE = "<BIG></BIG>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_big() throws Exception {
        loadPageWithAlerts2(outerHTML("big"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<blink></blink>", IE = "<BLINK></BLINK>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_blink() throws Exception {
        loadPageWithAlerts2(outerHTML("blink"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<blockquote></blockquote>", IE = "<BLOCKQUOTE></BLOCKQUOTE>",
        FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_blockquote() throws Exception {
        loadPageWithAlerts2(outerHTML("blockquote"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<body></body>", IE = "<BODY></BODY>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_body() throws Exception {
        loadPageWithAlerts2(outerHTML("body"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<b></b>", IE = "<B></B>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_b() throws Exception {
        loadPageWithAlerts2(outerHTML("b"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<br>", IE = "<BR>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_br() throws Exception {
        loadPageWithAlerts2(outerHTML("br"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<button></button>", IE = "<BUTTON></BUTTON>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_button() throws Exception {
        loadPageWithAlerts2(outerHTML("button"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<canvas></canvas>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_canvas() throws Exception {
        loadPageWithAlerts2(outerHTML("canvas"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<caption></caption>", IE = "<CAPTION></CAPTION>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_caption() throws Exception {
        loadPageWithAlerts2(outerHTML("caption"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<center></center>", IE = "<CENTER></CENTER>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_center() throws Exception {
        loadPageWithAlerts2(outerHTML("center"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<cite></cite>", IE = "<CITE></CITE>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_cite() throws Exception {
        loadPageWithAlerts2(outerHTML("cite"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<code></code>", IE = "<CODE></CODE>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_code() throws Exception {
        loadPageWithAlerts2(outerHTML("code"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<dfn></dfn>", IE = "<DFN></DFN>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_dfn() throws Exception {
        loadPageWithAlerts2(outerHTML("dfn"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<dd></dd>", IE = "<DD></DD>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_dd() throws Exception {
        loadPageWithAlerts2(outerHTML("dd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<del></del>", IE = "<DEL></DEL>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_del() throws Exception {
        loadPageWithAlerts2(outerHTML("del"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<dir></dir>", IE = "<DIR></DIR>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_dir() throws Exception {
        loadPageWithAlerts2(outerHTML("dir"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<div></div>", IE = "<DIV></DIV>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_div() throws Exception {
        loadPageWithAlerts2(outerHTML("div"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<dl></dl>", IE = "<DL></DL>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_dl() throws Exception {
        loadPageWithAlerts2(outerHTML("dl"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<dt></dt>", IE = "<DT></DT>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_dt() throws Exception {
        loadPageWithAlerts2(outerHTML("dt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<embed>", IE = "<EMBED>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_embed() throws Exception {
        loadPageWithAlerts2(outerHTML("embed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<em></em>", IE = "<EM></EM>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_em() throws Exception {
        loadPageWithAlerts2(outerHTML("em"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<fieldset></fieldset>", IE = "<FIELDSET></FIELDSET>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_fieldset() throws Exception {
        loadPageWithAlerts2(outerHTML("fieldset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<font></font>", IE = "<FONT></FONT>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_font() throws Exception {
        loadPageWithAlerts2(outerHTML("font"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<form></form>", IE = "<FORM></FORM>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_form() throws Exception {
        loadPageWithAlerts2(outerHTML("form"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<frame>", IE = "<FRAME>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_frame() throws Exception {
        loadPageWithAlerts2(outerHTML("frame"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<frameset></frameset>", IE = "<FRAMESET></FRAMESET>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_frameset() throws Exception {
        loadPageWithAlerts2(outerHTML("frameset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<h1></h1>", IE = "<H1></H1>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_h1() throws Exception {
        loadPageWithAlerts2(outerHTML("h1"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<h2></h2>", IE = "<H2></H2>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_h2() throws Exception {
        loadPageWithAlerts2(outerHTML("h2"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<h3></h3>", IE = "<H3></H3>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_h3() throws Exception {
        loadPageWithAlerts2(outerHTML("h3"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<h4></h4>", IE = "<H4></H4>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_h4() throws Exception {
        loadPageWithAlerts2(outerHTML("h4"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<h5></h5>", IE = "<H5></H5>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_h5() throws Exception {
        loadPageWithAlerts2(outerHTML("h5"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<h6></h6>", IE = "<H6></H6>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_h6() throws Exception {
        loadPageWithAlerts2(outerHTML("h6"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<head></head>", IE = "<HEAD></HEAD>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_head() throws Exception {
        loadPageWithAlerts2(outerHTML("head"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<hr>", IE = "<HR>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_hr() throws Exception {
        loadPageWithAlerts2(outerHTML("hr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<html></html>", IE = "<HTML></HTML>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_html() throws Exception {
        loadPageWithAlerts2(outerHTML("html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<iframe></iframe>", IE = "<IFRAME></IFRAME>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_iframe() throws Exception {
        loadPageWithAlerts2(outerHTML("iframe"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<q></q>", IE = "<Q></Q>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_q() throws Exception {
        loadPageWithAlerts2(outerHTML("q"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<img>", IE = "<IMG>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_img() throws Exception {
        loadPageWithAlerts2(outerHTML("img"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<ins></ins>", IE = "<INS></INS>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_ins() throws Exception {
        loadPageWithAlerts2(outerHTML("ins"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<isindex></isindex>", IE = "<ISINDEX>", FF3_6 = "undefined", FF10 = "undefined")
    @NotYetImplemented(FF17)
    public void outerHTML_isindex() throws Exception {
        loadPageWithAlerts2(outerHTML("isindex"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<i></i>", IE = "<I></I>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_i() throws Exception {
        loadPageWithAlerts2(outerHTML("i"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<kbd></kbd>", IE = "<KBD></KBD>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_kbd() throws Exception {
        loadPageWithAlerts2(outerHTML("kbd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<label></label>", IE = "<LABEL></LABEL>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_label() throws Exception {
        loadPageWithAlerts2(outerHTML("label"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<legend></legend>", IE = "<LEGEND></LEGEND>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_legend() throws Exception {
        loadPageWithAlerts2(outerHTML("legend"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<listing></listing>", IE = "<LISTING></LISTING>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_listing() throws Exception {
        loadPageWithAlerts2(outerHTML("listing"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<li></li>", IE = "<LI></LI>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_li() throws Exception {
        loadPageWithAlerts2(outerHTML("li"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<link>", IE = "<LINK>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_link() throws Exception {
        loadPageWithAlerts2(outerHTML("link"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<map></map>", IE = "<MAP></MAP>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_map() throws Exception {
        loadPageWithAlerts2(outerHTML("map"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<marquee></marquee>", IE = "<MARQUEE></MARQUEE>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_marquee() throws Exception {
        loadPageWithAlerts2(outerHTML("marquee"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<menu></menu>", IE = "<MENU></MENU>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_menu() throws Exception {
        loadPageWithAlerts2(outerHTML("menu"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<meta>", IE = "<META>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_meta() throws Exception {
        loadPageWithAlerts2(outerHTML("meta"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<meter></meter>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_meter() throws Exception {
        loadPageWithAlerts2(outerHTML("meter"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<multicol></multicol>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_multicol() throws Exception {
        loadPageWithAlerts2(outerHTML("multicol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<nobr></nobr>", IE = "<NOBR></NOBR>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_nobr() throws Exception {
        loadPageWithAlerts2(outerHTML("nobr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<noembed></noembed>", IE = "<NOEMBED></NOEMBED>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_noembed() throws Exception {
        loadPageWithAlerts2(outerHTML("noembed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<noframes></noframes>", IE = "<NOFRAMES></NOFRAMES>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_noframes() throws Exception {
        loadPageWithAlerts2(outerHTML("noframes"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<noscript></noscript>", IE = "<NOSCRIPT></NOSCRIPT>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_noscript() throws Exception {
        loadPageWithAlerts2(outerHTML("noscript"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<object></object>", IE = "<OBJECT></OBJECT>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_object() throws Exception {
        loadPageWithAlerts2(outerHTML("object"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<ol></ol>", IE = "<OL></OL>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_ol() throws Exception {
        loadPageWithAlerts2(outerHTML("ol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<optgroup></optgroup>", IE = "<OPTGROUP></OPTGROUP>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_optgroup() throws Exception {
        loadPageWithAlerts2(outerHTML("optgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<option></option>", IE = "<OPTION></OPTION>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_option() throws Exception {
        loadPageWithAlerts2(outerHTML("option"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<p></p>", IE = "<P></P>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_p() throws Exception {
        loadPageWithAlerts2(outerHTML("p"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<param>", IE = "<PARAM>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_param() throws Exception {
        loadPageWithAlerts2(outerHTML("param"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<plaintext></plaintext>", IE = "<PLAINTEXT></PLAINTEXT>",
        FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_plaintext() throws Exception {
        loadPageWithAlerts2(outerHTML("plaintext"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<pre></pre>", IE = "<PRE></PRE>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_pre() throws Exception {
        loadPageWithAlerts2(outerHTML("pre"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<progress></progress>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_progress() throws Exception {
        loadPageWithAlerts2(outerHTML("progress"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<s></s>", IE = "<S></S>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_s() throws Exception {
        loadPageWithAlerts2(outerHTML("s"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<samp></samp>", IE = "<SAMP></SAMP>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_samp() throws Exception {
        loadPageWithAlerts2(outerHTML("samp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<script></script>", IE = "<SCRIPT></SCRIPT>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_script() throws Exception {
        loadPageWithAlerts2(outerHTML("script"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<select></select>", IE = "<SELECT></SELECT>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_select() throws Exception {
        loadPageWithAlerts2(outerHTML("select"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<small></small>", IE = "<SMALL></SMALL>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_small() throws Exception {
        loadPageWithAlerts2(outerHTML("small"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<source>", IE = "<source></source>", FF3_6 = "undefined", FF10 = "undefined")
    @NotYetImplemented(FF17)
    public void outerHTML_source() throws Exception {
        loadPageWithAlerts2(outerHTML("source"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<spacer></spacer>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_spacer() throws Exception {
        loadPageWithAlerts2(outerHTML("spacer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<span></span>", IE = "<SPAN></SPAN>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_span() throws Exception {
        loadPageWithAlerts2(outerHTML("span"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<strike></strike>", IE = "<STRIKE></STRIKE>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_strike() throws Exception {
        loadPageWithAlerts2(outerHTML("strike"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<strong></strong>", IE = "<STRONG></STRONG>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_strong() throws Exception {
        loadPageWithAlerts2(outerHTML("strong"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<style></style>", IE = "<STYLE></STYLE>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_style() throws Exception {
        loadPageWithAlerts2(outerHTML("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<sub></sub>", IE = "<SUB></SUB>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_sub() throws Exception {
        loadPageWithAlerts2(outerHTML("sub"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<sup></sup>", IE = "<SUP></SUP>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_sup() throws Exception {
        loadPageWithAlerts2(outerHTML("sup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<table></table>", IE = "<TABLE></TABLE>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_table() throws Exception {
        loadPageWithAlerts2(outerHTML("table"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<col>", IE = "<COL>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_col() throws Exception {
        loadPageWithAlerts2(outerHTML("col"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<colgroup></colgroup>", IE = "<COLGROUP></COLGROUP>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_colgroup() throws Exception {
        loadPageWithAlerts2(outerHTML("colgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<tbody></tbody>", IE = "<TBODY></TBODY>", FF3_6 = "undefined", FF10 = "undefined")
    @NotYetImplemented(FF17)
    public void outerHTML_tbody() throws Exception {
        loadPageWithAlerts2(outerHTML("tbody"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<td></td>", IE = "<TD></TD>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_td() throws Exception {
        loadPageWithAlerts2(outerHTML("td"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<th></th>", IE = "<TH></TH>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_th() throws Exception {
        loadPageWithAlerts2(outerHTML("th"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<tr></tr>", IE = "<TR></TR>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_tr() throws Exception {
        loadPageWithAlerts2(outerHTML("tr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<textarea></textarea>", IE = "<TEXTAREA></TEXTAREA>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_textarea() throws Exception {
        loadPageWithAlerts2(outerHTML("textarea"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<tfoot></tfoot>", IE = "<TFOOT></TFOOT>", FF3_6 = "undefined", FF10 = "undefined")
    @NotYetImplemented(FF17)
    public void outerHTML_tfoot() throws Exception {
        loadPageWithAlerts2(outerHTML("tfoot"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<thead></thead>", IE = "<THEAD></THEAD>", FF3_6 = "undefined", FF10 = "undefined")
    @NotYetImplemented(FF17)
    public void outerHTML_thead() throws Exception {
        loadPageWithAlerts2(outerHTML("thead"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<tt></tt>", IE = "<TT></TT>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_tt() throws Exception {
        loadPageWithAlerts2(outerHTML("tt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<title></title>", IE = "<TITLE></TITLE>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_title() throws Exception {
        loadPageWithAlerts2(outerHTML("title"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<u></u>", IE = "<U></U>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_u() throws Exception {
        loadPageWithAlerts2(outerHTML("u"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<ul></ul>", IE = "<UL></UL>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_ul() throws Exception {
        loadPageWithAlerts2(outerHTML("ul"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<var></var>", IE = "<VAR></VAR>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_var() throws Exception {
        loadPageWithAlerts2(outerHTML("var"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<video></video>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_video() throws Exception {
        loadPageWithAlerts2(outerHTML("video"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<wbr>", IE = "<WBR>", FF3_6 = "undefined", FF10 = "undefined")
    @NotYetImplemented(FF17)
    public void outerHTML_wbr() throws Exception {
        loadPageWithAlerts2(outerHTML("wbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<xmp></xmp>", IE = "<XMP></XMP>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_xmp() throws Exception {
        loadPageWithAlerts2(outerHTML("xmp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<abcdefg></abcdefg>", FF3_6 = "undefined", FF10 = "undefined")
    public void outerHTML_arbitrary() throws Exception {
        loadPageWithAlerts2(outerHTML("abcdefg"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "DIV", "section", "<DIV></DIV>", "<section></section>" },
            FF3_6 = { "DIV", "SECTION", "undefined", "undefined" },
            FF10 = { "DIV", "SECTION", "undefined", "undefined" },
            DEFAULT = { "DIV", "SECTION", "<div></div>", "<section></section>" })
    @NotYetImplemented(IE)
    public void nodeNameVsOuterElement() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.createElement('div').tagName);\n"
            + "      alert(document.createElement('section').tagName);\n"
            + "      alert(document.createElement('div').cloneNode( true ).outerHTML);\n"
            + "      alert(document.createElement('section').cloneNode( true ).outerHTML);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "null", "ho" })
    public void getSetAttribute_in_xml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var text='<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\\n';\n"
            + "    text += '<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://myNS\">\\n';\n"
            + "    text += '  <xsl:template match=\"/\">\\n';\n"
            + "    text += \"  <html xmlns='http://www.w3.org/1999/xhtml'>\\n\";\n"
            + "    text += '    <body>\\n';\n"
            + "    text += '    </body>\\n';\n"
            + "    text += '  </html>\\n';\n"
            + "    text += '  </xsl:template>\\n';\n"
            + "    text += '</xsl:stylesheet>';\n"
            + "    if (window.ActiveXObject) {\n"
            + "      var doc=new ActiveXObject('Microsoft.XMLDOM');\n"
            + "      doc.async=false;\n"
            + "      doc.loadXML(text);\n"
            + "    } else {\n"
            + "      var parser=new DOMParser();\n"
            + "      var doc=parser.parseFromString(text,'text/xml');\n"
            + "    }\n"
            + "    try {\n"
            + "      var elem = doc.documentElement.getElementsByTagName('html').item(0);\n"
            + "      alert(elem.getAttribute('hi'));\n"
            + "      elem.setAttribute('hi', 'ho');\n"
            + "      alert(elem.getAttribute('hi'));\n"
            + "    } catch (e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object Text]", "[object Text]" }, IE = { "[object]", "[object]" })
    public void textContentShouldNotDetachNestedNode() throws Exception {
        final String html = "<html><body><div><div id='it'>foo</div></div><script>\n"
            + "try {\n"
            + "  var elt = document.getElementById('it');\n"
            + "  alert(elt.firstChild);\n"
            + "  elt.parentNode.textContent = '';\n"
            + "  alert(elt.firstChild);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }
}
