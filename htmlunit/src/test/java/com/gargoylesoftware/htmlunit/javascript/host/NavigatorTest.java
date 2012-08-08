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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Navigator}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class NavigatorTest extends WebDriverTestCase {

    /**
     * Tests the "appCodeName" property.
     * @throws Exception on test failure
     */
    @Test
    public void appCodeName() throws Exception {
        attribute("appCodeName", getBrowserVersion().getApplicationCodeName());
    }

    /**
     * Tests the "appMinorVersion" property.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = "0", DEFAULT = "undefined")
    public void appMinorVersion() throws Exception {
        attribute("appMinorVersion", getExpectedAlerts()[0]);
    }

    /**
     * Tests the "appName" property.
     * @throws Exception on test failure
     */
    @Test
    public void appName() throws Exception {
        attribute("appName", getBrowserVersion().getApplicationName());
    }

    /**
     * Tests the "appVersion" property.
     * @throws Exception on test failure
     */
    @Test
    public void appVersion() throws Exception {
        attribute("appVersion", getBrowserVersion().getApplicationVersion());
    }

    /**
     * Tests the "browserLanguage" property.
     * @throws Exception on test failure
     */
    @Test
    @Browsers(Browser.IE)
    public void browserLanguage_IE() throws Exception {
        attribute("browserLanguage", getBrowserVersion().getBrowserLanguage());
    }

    /**
     * Tests the "browserLanguage" property.
     * @throws Exception on test failure
     */
    @Test
    @Browsers(Browser.FF)
    public void browserLanguage_FF() throws Exception {
        attribute("browserLanguage", "undefined");
    }

    /**
     * Tests the "productSub" property.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "string", "true" }, IE = { "undefined", "false" })
    public void productSub() throws Exception {
        final String html = "<html><head><script>\n"
            + "alert(typeof(navigator.productSub));\n"
            + "alert(parseInt(navigator.productSub) > 20000101);\n"
            + "</script>\n"
            + "</head><body></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Tests the "cookieEnabled" property.
     * @throws Exception on test failure
     */
    @Test
    public void cookieEnabled() throws Exception {
        cookieEnabled(true);
        cookieEnabled(false);
    }

    private void cookieEnabled(final boolean cookieEnabled) throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(navigator.cookieEnabled);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body>\n"
            + "</html>";

        setExpectedAlerts(Boolean.toString(cookieEnabled));
        if (!cookieEnabled) {
            getWebClient().getCookieManager().setCookiesEnabled(cookieEnabled);
        }

        loadPageWithAlerts2(html);
    }

    /**
     * Tests the "cpuClass" property.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = "x86", DEFAULT = "undefined")
    public void cpuClass() throws Exception {
        attribute("cpuClass", getExpectedAlerts()[0]);
    }

    /**
     * Tests the "onLine" property.
     * @throws Exception on test failure
     */
    @Test
    public void onLine() throws Exception {
        attribute("onLine", String.valueOf(getBrowserVersion().isOnLine()));
    }

    /**
     * Tests the "platform" property.
     * @throws Exception on test failure
     */
    @Test
    public void platform() throws Exception {
        attribute("platform", getBrowserVersion().getPlatform());
    }

    /**
     * Tests the "systemLanguage" property.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = "en-us", DEFAULT = "undefined")
    public void systemLanguage() throws Exception {
        attribute("systemLanguage", getExpectedAlerts()[0]);
    }

    /**
     * Tests the "userAgent" property.
     * @throws Exception on test failure
     */
    @Test
    public void userAgent() throws Exception {
        attribute("userAgent", getBrowserVersion().getUserAgent());
    }

    /**
     * Tests the "userLanguage" property.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = "en-us", DEFAULT = "undefined")
    public void userLanguage() throws Exception {
        attribute("userLanguage", getExpectedAlerts()[0]);
    }

    /**
     * Tests the "plugins" property.
     * @throws Exception on test failure
     */
    @Test
    public void plugins() throws Exception {
        attribute("plugins.length", String.valueOf(getBrowserVersion().getPlugins().size()));
    }

    /**
     * Tests the "javaEnabled" method.
     * @throws Exception on test failure
     */
    @Test
    public void javaEnabled() throws Exception {
        attribute("javaEnabled()", "false");
        final WebClient webClient = getWebClient();
        webClient.getOptions().setAppletEnabled(true);
        attribute(webClient, "javaEnabled()", "true");
    }

    /**
     * Tests the "taintEnabled" property.
     * @throws Exception on test failure
     */
    @Test
    public void taintEnabled() throws Exception {
        attribute("taintEnabled()", "false");
    }

    /**
     * Generic method for testing the value of a specific navigator attribute.
     * @param name the name of the attribute to test
     * @param value the expected value for the named attribute
     * @throws Exception on test failure
     */
    private void attribute(final String name, final String value) throws Exception {
        attribute(getWebClient(), name, value);
    }

    /**
     * Generic method for testing the value of a specific navigator attribute.
     * @param webClient the web client to use to load the page
     * @param name the name of the attribute to test
     * @param value the expected value for the named attribute
     * @throws Exception on test failure
     */
    private void attribute(final WebClient webClient, final String name, final String value) throws Exception {
        final String html = "<html>\n"
                + "<head>\n"
                + "    <title>test</title>\n"
                + "    <script>\n"
                + "    function doTest(){\n"
                + "       alert('" + name + " = ' + window.navigator." + name + ");\n"
                + "    }\n"
                + "    </script>\n"
                + "</head>\n"
                + "<body onload=\'doTest()\'>\n"
                + "</body>\n"
                + "</html>";

        setExpectedAlerts(name + " = " + value);
        loadPageWithAlerts2(html);
    }

    /**
     * Test language property (only for Mozilla).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "en-us", IE = "undefined", CHROME = "undefined")
    public void language() throws Exception {
        final String html
            = "<html><head><title>First</title></head>\n"
            + "<body onload='alert(window.navigator.language)'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test some Mozilla properties (minimal tests are support is not completed).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "number", "number" })
    public void mozilla() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(typeof window.navigator.mimeTypes.length);\n"
            + "  alert(typeof window.navigator.plugins.length);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test some Mozilla properties (minimal tests are support is not completed).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "Gecko", IE = "undefined", CHROME = "Gecko")
    public void product() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(navigator.product);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object GeoGeolocation]", IE = "undefined", CHROME = "[object Geolocation]")
    //IE9 = "[object Geolocation]"
    public void geolocation() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(navigator.geolocation);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF10 = "20120713134347", FF3_6 = "20120306064154", DEFAULT = "undefined")
    public void buildID() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(navigator.buildID);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "", "" }, IE = { "undefined", "undefined" }, CHROME = { "Google Inc.", "" })
    public void vendor() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(navigator.vendor);\n"
            + "  alert(navigator.vendorSub);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void oscpu() throws Exception {
        final String html
            = "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(navigator.oscpu);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'></body>\n"
            + "</html>";

        String expectation;
        if (getBrowserVersion().getNickname().startsWith("FF")) {
            final String os = System.getProperty("os.name").toLowerCase();
            if ("windows 7".equals(os)) {
                expectation = "Windows NT 6.1";
            }
            else if ("windows vista".equals(os)) {
                expectation = "Windows NT 6.0";
            }
            else if ("windows 2003".equals(os)) {
                expectation = "Windows NT 5.2";
            }
            else if ("windows xp".equals(os)) {
                expectation = "Windows NT 5.1";
            }
            else if ("windows 2000".equals(os)) {
                expectation = "Windows NT 5.0";
            }
            else if ("mac os x".equals(os)) {
                expectation = "Intel Mac OS X " + System.getProperty("os.version");
            }
            else if ("linux".equals(os)) {
                expectation = "Linux i686";
            }
            else {
                expectation = "Windows NT 6.1";
            }
        }
        else {
            expectation = "undefined";
        }
        setExpectedAlerts(expectation);
        loadPageWithAlerts2(html);
    }
}
