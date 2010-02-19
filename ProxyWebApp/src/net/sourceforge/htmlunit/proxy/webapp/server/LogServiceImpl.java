/*
 * Copyright (c) 2010 HtmlUnit team.
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
package net.sourceforge.htmlunit.proxy.webapp.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.htmlunit.proxy.webapp.client.LogService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 *
 * @author Ahmed Ashour
 * @version $Revision$
 */
public class LogServiceImpl extends RemoteServiceServlet implements LogService {

    private static final long serialVersionUID = 3951561955277264689L;

    private static final List<String> logs_ = new ArrayList<String>();

    /**
     * Adds a log entry.
     * @param log the log
     */
    static void addLog(final String log) {
        synchronized (logs_) {
            logs_.add(new Date() + ":" + log);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String[] getLog(final int index) {
        synchronized (logs_) {
            if (index < logs_.size()) {
                final String[] list = new String[logs_.size() - index];
                for (int i = 0; i < list.length; i++) {
                    list[i] = logs_.get(index + i);
                }
                return list;
            }
            return new String[0];
        }
    }
}
