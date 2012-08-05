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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebConsole;
import com.gargoylesoftware.htmlunit.WebConsole.Logger;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Tests for {@link Console}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ConsoleTest extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    public void log() throws Exception {
        final WebConsole console = getWebClient().getWebConsole();
        final List<String> messages = new ArrayList<String>();
        console.setLogger(new Logger() {

            @Override
            public void warn(final Object message) {
            }

            @Override
            public void trace(final Object message) {
            }

            @Override
            public void info(final Object message) {
                messages.add("info: " + message);
            }

            @Override
            public void error(final Object message) {
            }

            @Override
            public void debug(final Object message) {
            }
        });

        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  if (window.console) {\n"
            + "    var arr = ['one', 'two', 'three', document.body.children];\n"
            + "    window.console.log(arr);\n"
            + "    window.dump('hello');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPage(html);
        assertEquals("info: [\"one\", \"two\", \"three\", ({})]", messages.get(0));
        assertEquals("info: hello", messages.get(1));
    }
}
