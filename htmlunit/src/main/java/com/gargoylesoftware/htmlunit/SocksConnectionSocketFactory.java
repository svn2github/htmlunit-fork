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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

import org.apache.http.HttpHost;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

/**
 * SOCKS aware {@link org.apache.http.conn.scheme.SchemeSocketFactory}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 */
class SocksConnectionSocketFactory extends PlainConnectionSocketFactory {
    private static final String SOCKS_PROXY = "htmlunit.socksproxy";

    static void setSocksProxy(final HttpContext context, final HttpHost socksProxy) {
        context.setAttribute(SOCKS_PROXY, socksProxy);
    }

    static HttpHost getSocksProxy(final HttpContext context) {
        return (HttpHost) context.getAttribute(SOCKS_PROXY);
    }

    static Socket createSocketWithSocksProxy(final HttpHost socksProxy) {
        final InetSocketAddress address = new InetSocketAddress(socksProxy.getHostName(), socksProxy.getPort());
        final Proxy proxy = new Proxy(Proxy.Type.SOCKS, address);
        return new Socket(proxy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Socket createSocket(final HttpContext context) throws IOException {
        final HttpHost socksProxy = getSocksProxy(context);
        if (socksProxy != null) {
            return createSocketWithSocksProxy(socksProxy);
        }
        return super.createSocket(context);
    }
}
