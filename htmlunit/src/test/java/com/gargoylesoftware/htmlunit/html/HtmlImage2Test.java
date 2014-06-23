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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlImage}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlImage2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void loadImageWithoutSource() throws Exception {
        loadImage("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void loadImageEmptySource() throws Exception {
        loadImage("src=''");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "2", IE = "1")
    @NotYetImplemented(IE)
    public void loadImageBlankSource() throws Exception {
        loadImage("src=' '");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void loadImage() throws Exception {
        loadImage("src='img.jpg'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void loadImageUnknown() throws Exception {
        loadImage("src='unknown'");
    }

    private void loadImage(final String src) throws Exception {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img");
        final byte[] directBytes = IOUtils.toByteArray(is);
        is.close();

        final URL urlImage = new URL(URL_FIRST, "img.jpg");
        final List<NameValuePair> emptyList = Collections.emptyList();
        getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);

        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var img = document.getElementById('myImage');\n"
            + "    img.width;\n" // this forces image loading in htmlunit
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <img id='myImage' " + src + " >\n"
            + "</body></html>";

        loadPage2(html);
        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getMockWebConnection().getRequestCount());
    }
}
