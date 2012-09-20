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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF3_6;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.NONE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests that when DOM events such as "onclick" have access
 * to an {@link Event} object with context information.
 *
 * @version $Revision$
 * @author <a href="mailto:chriseldredge@comcast.net">Chris Eldredge</a>
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class EventTest extends WebDriverTestCase {

    /**
     * Verify the "this" object refers to the Element being clicked when an
     * event handler is invoked.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clickId")
    public void testThisDefined() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(this.getAttribute('id')); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Verify setting a previously undefined/non-existent property on an Element
     * is accessible from inside an event handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void testSetPropOnThisDefined() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(this.madeUpProperty); }\n"
            + "document.getElementById('clickId').onclick = handler;\n"
            + "document.getElementById('clickId').madeUpProperty = 'foo';\n"
            + "</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Verify that JavaScript snippets have a variable named 'event' available to them.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("defined")
    public void testEventArgDefinedByWrapper() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId' onclick=\"alert(event ? 'defined' : 'undefined')\"/>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Verify that when event handler is invoked an argument is passed in.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("defined")
    @Browsers(FF)
    public void testEventArgDefined() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) { alert(event ? 'defined' : 'undefined'); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("pass")
    @Browsers(FF)
    public void testEventTargetSameAsThis() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "alert(event.target == this ? 'pass' : event.target + '!=' + this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "undefined", "false" }, IE = { "[object]", "true" })
    public void testEventSrcElementSameAsThis() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "event = event ? event : window.event;\n"
            + "alert(event.srcElement);\n"
            + "alert(event.srcElement == this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Verify that <tt>event.currentTarget == this</tt> inside JavaScript event handler.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("pass")
    @Browsers(FF)
    public void testEventCurrentTargetSameAsThis() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<input type='button' id='clickId'/>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "alert(event.currentTarget == this ? 'pass' : event.currentTarget + '!=' + this); }\n"
            + "document.getElementById('clickId').onclick = handler;</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * Property currentTarget needs to be set again depending on the listener invoked.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "[object Window]", "[object HTMLDivElement]" })
    @Browsers(FF)
    public void testCurrentTarget_sameListenerForEltAndWindow() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<div id='clickId'>click me</div>\n"
            + "<script>\n"
            + "function handler(event) {\n"
            + "alert(event.currentTarget); }\n"
            + "document.getElementById('clickId').onmousedown = handler;\n"
            + "window.addEventListener('mousedown', handler, true);</script>\n"
            + "</body></html>";
        onClickPageTest(content);
    }

    /**
     * When adding a null event listener browsers react different.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "true", FF10 = "", FF3_6 = "error")
    public void testAddEventListener_HandlerNull() throws Exception {
        final String content
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  if (window.addEventListener) {\n"
            + "    window.addEventListener('mousedown', null, true);\n"
            + "  }\n"
            + "  if (window.attachEvent) {\n"
            + "    alert(window.attachEvent('mousedown', null));\n"
            + "  }\n"
            + "} catch (err) {\n"
            + "  alert('error');\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(content);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "124a", "1a2a4ab1ab2ab4abc" }, FF = { "123a4a", "1a2a3ab4ab1ab2ab3abc4abc" })
    public void testTyping_input() throws Exception {
        testTyping("<input type='text'", "");
        testTyping("<input type='password'", "");
        testTyping("<textarea", "</textarea>");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = { "124a", "1a2a4ab1ab2ab4abc" }, FF = { "123a4a", "1a2a3ab4ab1ab2ab3abc4abc" })
    public void testTyping_textara() throws Exception {
        testTyping("<textarea", "</textarea>");
    }

    private void testTyping(final String opening, final String closing) throws Exception {
        final String html =
              "<html><body>\n"
            + "<script>var x = '';\n"
            + "function log(s) { x += s; }</script>\n"
            + "<form>\n"
            + opening + " id='t' onkeydown='log(1 + this.value)' "
            + "onkeypress='log(2 + this.value)' "
            + "oninput='log(3 + this.value)'"
            + "onkeyup='log(4 + this.value)'>" + closing
            + "</form>\n"
            + "<div id='d' onclick='alert(x); x=\"\"'>abc</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("t")).sendKeys("a");
        driver.findElement(By.id("d")).click();

        driver.findElement(By.id("t")).sendKeys("bc");
        driver.findElement(By.id("d")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    private void onClickPageTest(final String html) throws Exception {
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("clickId")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * Test that "this" refers to the element on which the event applies.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("frame1")
    public void testEventScope() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "<button name='button1' id='button1' onclick='alert(this.name)'>1</button>\n"
            + "<iframe src='about:blank' name='frame1' id='frame1'></iframe>\n"
            + "<script>\n"
            + "document.getElementById('frame1').onload = document.getElementById('button1').onclick;\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that the event property of the window is available.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "true", "exception" }, IE = { "false", "false" })
    public void testIEWindowEvent() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(window.event == null);\n"
            + "  try {\n"
            + "    alert(event == null);\n"
            + "  } catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that the event handler is correctly parsed even if it contains comments.
     * It seems that it is correctly parsed and stored in non public field
     * org.apache.xerces.util.XMLAttributesImpl#nonNormalizedValue
     * but that getValue(i) returns a normalized value. Furthermore access seems not possible as
     * we just see an org.apache.xerces.parsers.AbstractSAXParser.AttributesProxy
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2" })
    public void testCommentInEventHandlerDeclaration() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body onload='alert(1);\n"
            + "// a comment within the onload declaration\n"
            + "alert(2)'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test value for null event handler: null for IE, while 'undefined' for Firefox.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(FF3_6)
    @Alerts(DEFAULT = "null", FF3_6 = "undefined")
    public void testNullEventHandler() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    alert(div.onclick);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='myDiv'/>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(FF)
    @Alerts(FF = {"object", "false" },
            IE = {"object", "undefined" })
    public void testBubbles() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(typeof e);\n"
            + "        alert(e.bubbles);\n"
            + "    }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"object", "true" },
            IE = {"object", "undefined" })
    public void testCancelable() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(typeof e);\n"
            + "        alert(e.cancelable);\n"
            + "    }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"object", "number" },
            IE = {"object", "undefined" })
    public void testTimeStamp() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "    function test(e) {\n"
            + "        alert(typeof e);\n"
            + "        alert(typeof e.timeStamp);\n"
            + "    }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(NONE)
    public void testSetEventPhaseToInvalidValue() throws Exception {
        boolean thrown = false;
        try {
            new Event().setEventPhase((short) 777);
        }
        catch (final IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(FF)
    @Alerts({ "click", "true", "true", "dblclick", "false", "false" })
    public void testInitEvent() throws Exception {
        final String html =
              "<html><body onload='test()'><script>\n"
            + "  function test() {\n"
            + "    var e = document.createEvent('Event');\n"
            + "    e.initEvent('click', true, true);\n"
            + "    doAlerts(e);\n"
            + "    e.initEvent('dblclick', false, false);\n"
            + "    doAlerts(e);\n"
            + "  }\n"
            + "  function doAlerts(e) {\n"
            + "    alert(e.type);\n"
            + "    alert(e.bubbles);\n"
            + "    alert(e.cancelable);\n"
            + "  }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "true", "I was here" }, IE = { "true", "I was here" })
    public void firedEvent_equals_original_event() throws Exception {
        final String html =
            "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var e = document.getElementById('myDiv');\n"
            + "  \n"
            + "  var myEvent\n"
            + "  var listener = function(x) { \n"
            + "    alert(x == myEvent);\n"
            + "    x.foo = 'I was here'\n"
            + "  }\n"
            + "  \n"
            + "  if (document.createEvent) {\n"
            + "    e.addEventListener('click', listener, false);\n"
            + "    myEvent = document.createEvent('HTMLEvents');\n"
            + "    myEvent.initEvent('click', true, true);\n"
            + "    e.dispatchEvent(myEvent);\n"
            + "  }\n"
            + "  else {\n"
            + "    e.attachEvent('onclick', listener);\n"
            + "    myEvent = document.createEventObject();\n"
            + "    myEvent.eventType = 'onclick';\n"
            + "    e.fireEvent(myEvent.eventType, myEvent);\n"
            + "  }\n"
            + "  alert(myEvent.foo);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<div id='myDiv'>toti</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(FF)
    @Alerts("400000,1,20000000,2000,8000,40,2,80,800,800000,1000,8000000,10000000,100,400,200,80000,1000000,"
            + "8,1,20,10,8,4,2,2000000,10000,4000000,40000,4000,4,20000,100000,200000,")
    public void constants() throws Exception {
        final String html =
              "<html><body>\n"
            + "<script>\n"
            + "    var constants = [Event.ABORT, Event.ALT_MASK, Event.BACK, Event.BLUR, Event.CHANGE, Event.CLICK, "
            + "Event.CONTROL_MASK, Event.DBLCLICK, Event.DRAGDROP, Event.ERROR, Event.FOCUS, Event.FORWARD, "
            + "Event.HELP, Event.KEYDOWN, Event.KEYPRESS, Event.KEYUP, Event.LOAD, Event.LOCATE, Event.META_MASK, "
            + "Event.MOUSEDOWN, Event.MOUSEDRAG, Event.MOUSEMOVE, Event.MOUSEOUT, Event.MOUSEOVER, Event.MOUSEUP, "
            + "Event.MOVE, Event.RESET, Event.RESIZE, Event.SCROLL, Event.SELECT, Event.SHIFT_MASK, Event.SUBMIT, "
            + "Event.UNLOAD, Event.XFER_DONE];\n"
            + "    var str = '';\n"
            + "    for (var x in constants) {\n"
            + "      str += constants[x].toString(16) + ',';\n"
            + "    }\n"
            + "    alert(str);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(FF)
    @Alerts("40000000")
    public void text() throws Exception {
        final String html =
              "<html><body onload='test(event)'><script>\n"
            + "  function test(e) {\n"
            + "    alert(e.TEXT.toString(16));\n"// But Event.TEXT is undefined!!!
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug
     * <a href="http://sourceforge.net/tracker/?func=detail&aid=2851920&group_id=47038&atid=448266">2851920</a>.
     * Name resolution doesn't work the same in inline handlers than in "normal" JS code!
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    @Alerts(FF = { "form1 -> custom", "form2 -> [object HTMLFormElement]",
            "form1: [object HTMLFormElement]", "form2: [object HTMLFormElement]",
            "form1 -> custom", "form2 -> [object HTMLFormElement]" },
            IE = { "form1 -> custom", "form2 -> [object]",
            "form1: [object]", "form2: [object]",
            "form1 -> custom", "form2 -> [object]" })
    public void nameResolution() throws Exception {
        final String html = "<html><head><script>\n"
            + "var form1 = 'custom';\n"
            + "function testFunc() {\n"
            + " alert('form1 -> ' + form1);\n"
            + " alert('form2 -> ' + form2);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='testFunc()'>\n"
            + "<form name='form1'></form>\n"
            + "<form name='form2'></form>\n"
            + "<button onclick=\"alert('form1: ' + form1); alert('form2: ' + form2); testFunc()\">click me</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.tagName("button")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }
}
