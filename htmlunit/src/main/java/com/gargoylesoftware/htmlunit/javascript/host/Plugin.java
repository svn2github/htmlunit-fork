/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;


/**
 * A JavaScript object for a document.navigator.plugins.
 * @version $Revision$
 * @author Marc Guillemot
 *
 * @see <a href="http://www.xulplanet.com/references/objref/MimeTypeArray.html">XUL Planet</a>
 */
public class Plugin extends SimpleArray {
    private static final long serialVersionUID = -6829501824595761156L;
    private String description_;
    private String filename_;
    private String name_;

    /**
     * Create an instance. JavaScript objects must have a default constructor.
     */
    public Plugin() {
        // nothing
    }

    /**
     * C'tor initializing fields.
     * @param name the plugin name.
     * @param description the plugin description.
     * @param filename the plugin filename.
     */
    public Plugin(final String name, final String description, final String filename) {
        name_ = name;
        description_ = description;
        filename_ = filename;
    }

    /**
     * Get the name of the mime type
     * @param element a {@link MimeType}.
     * @return the name
     */
    @Override
    protected String getItemName(final Object element) {
        return ((MimeType) element).jsxGet_type();
    }

    /**
     * Gets the plugin's description
     * @return the description.
     */
    public String jsxGet_description() {
        return description_;
    }

    /**
     * Gets the plugin's file name
     * @return the file name.
     */
    public String jsxGet_filename() {
        return filename_;
    }

    /**
     * Gets the plugin's name
     * @return the name.
     */
    public String jsxGet_name() {
        return name_;
    }
}
