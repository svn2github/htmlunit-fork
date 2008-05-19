/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlOption}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlOptionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSelect() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' id='select1'>\n"
            + "<option value='option1' id='option1'>Option1</option>\n"
            + "<option value='option2' id='option2' selected='selected'>Option2</option>\n"
            + "<option value='option3' id='option3'>Option3</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = (HtmlOption) page.getHtmlElementById("option1");
        final HtmlOption option2 = (HtmlOption) page.getHtmlElementById("option2");
        final HtmlOption option3 = (HtmlOption) page.getHtmlElementById("option3");

        assertFalse(option1.isSelected());
        assertTrue(option2.isSelected());
        assertFalse(option3.isSelected());

        option3.setSelected(true);

        assertFalse(option1.isSelected());
        assertFalse(option2.isSelected());
        assertTrue(option3.isSelected());

        option3.setSelected(false);

        assertFalse(option1.isSelected());
        assertFalse(option2.isSelected());
        assertFalse(option3.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetValue() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'><select name='select1' id='select1'>\n"
            + "<option value='option1' id='option1'>Option1</option>\n"
            + "<option id='option2' selected>Number Two</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = (HtmlOption) page.getHtmlElementById("option1");
        final HtmlOption option2 = (HtmlOption) page.getHtmlElementById("option2");

        assertEquals("option1", option1.getValueAttribute());
        assertEquals("Number Two", option2.getValueAttribute());
    }
    
    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetValue_ContentsIsValue() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<select name='select1' id='select1'>\n"
            + "     <option id='option1'>Option1</option>\n"
            + "     <option id='option2' selected>Number Two</option>\n"
            + "     <option id='option3'>\n  Number 3 with blanks </option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = (HtmlOption) page.getHtmlElementById("option1");
        assertEquals("Option1", option1.getValueAttribute());

        final HtmlOption option2 = (HtmlOption) page.getHtmlElementById("option2");
        assertEquals("Number Two", option2.getValueAttribute());

        final HtmlOption option3 = (HtmlOption) page.getHtmlElementById("option3");
        assertEquals("Number 3 with blanks", option3.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick() throws Exception {
        final String htmlContent
            = "<html><body>\n"
            + "<form id='form1'>\n"
            + "<select name='select1' id='select1'>\n"
            + "     <option id='option1'>Option1</option>\n"
            + "     <option id='option2' selected>Number Two</option>\n"
            + "</select>\n"
            + "<input type='submit' name='button' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = (HtmlOption) page.getHtmlElementById("option1");
        assertFalse(option1.isSelected());
        option1.click();
        assertTrue(option1.isSelected());
        option1.click();
        assertTrue(option1.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testAsText() throws Exception {
        final String htmlContent = "<html><head><title>foo</title></head><body>\n"
            + "<form><select>\n"
            + "<option id='option1'>option1</option>\n"
            + "<option id='option2' label='Number Two'/>\n"
            + "<option id='option3' label='overridden'>Number Three</option>\n"
            + "<option id='option4'>Number&nbsp;4</option>\n"
            + "</select>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = (HtmlOption) page.getHtmlElementById("option1");
        final HtmlOption option2 = (HtmlOption) page.getHtmlElementById("option2");
        final HtmlOption option3 = (HtmlOption) page.getHtmlElementById("option3");
        final HtmlOption option4 = (HtmlOption) page.getHtmlElementById("option4");

        assertEquals("option1", option1.asText());
        assertEquals("Number Two", option2.asText());
        assertEquals("overridden", option3.asText());
        assertEquals("Number 4", option4.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSimpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<select>\n"
            + "  <option id='myId'>test1</option>\n"
            + "  <option id='myId2'>test2</option>\n"
            + "</select>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"[object HTMLOptionElement]"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertTrue(HtmlOption.class.isInstance(page.getHtmlElementById("myId")));
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
