package net.sourceforge.htmlunit.htmlparser;

import java.io.IOException;
import java.io.Reader;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class HTMLScanner {

    private TagBalancer tagBalancer_ = new TagBalancer();
    private Reader reader_;

    /** Null means empty, -1 means we reached end of stream. */
    private Integer nextChar_;
    

    public void setContentHandler(ContentHandler handler) {
        tagBalancer_.setContentHandler(handler);
    }

    public ContentHandler getContentHandler() {
        return tagBalancer_.getContentHandler();
    }

    public void parse(final InputSource source) throws IOException, SAXException {
        reader_ = source.getCharacterStream();
        scanDocument();
    }

    protected int peek() throws IOException {
        if (nextChar_ == null) {
            nextChar_ = reader_.read();
        }
        return nextChar_;
    }

    /**
     * Consumes all characters, until reaching any of the specified list.
     * @param isSapceWhite if space (' ') is specified, {@link Character#isWhitespace(char)} will be used.
     * @param chars the list of characters to stop (note they are not considered), only {@link #peek()} is used.
     * @return the string
     */
    protected String consumeUntil(boolean isSapceWhite, char... chars) throws IOException {
        StringBuilder builder = new StringBuilder();
outer:  while (true) {
            int read = peek();
            if (read == -1) {
                break;
            }
            for (char c : chars) {
                if (read == c) {
                    break outer;
                }
                if (c == ' ' && isSapceWhite && Character.isWhitespace(read)) {
                    break outer;
                }
            }
            builder.append((char) next());
        }
        return builder.toString();
    }

    protected void startDocument() throws SAXException {
        tagBalancer_.startDocument();
    }

    protected void characters(char[] ch, int start, int length) throws SAXException {
        tagBalancer_.characters(ch, start, length);
    }

    protected void endDocument() throws SAXException {
        tagBalancer_.endDocument();
    }

    protected void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        tagBalancer_.startElement(uri, localName, qName, atts);
    }

    protected void endElement(String uri, String localName, String qName) throws SAXException {
        qName = qName.toUpperCase();
        tagBalancer_.endElement(uri, localName, qName);
    }

    protected int next() throws IOException {
        if (nextChar_ == null) {
            int ch = reader_.read();
            if (ch == -1) {
                nextChar_ = -1;
            }
            return ch;
        }
        else if (nextChar_ == -1) {
            return -1;
        }
        int ch = nextChar_;
        nextChar_ = null;
        return ch;
    }

    protected void scanDocument() throws IOException, SAXException {
        startDocument();
        int ch;
        while ((ch = peek()) != -1) {
            if (ch == '<') {
                next();
                ch = peek();
                if (ch == '?') {
                    //processing instruction
                }
                if (ch == '/') {
                    next();
                    parseEndElement();
                }
                else {
                    parseStartElement();
                }
            }
            else if (Character.isWhitespace(ch)) {
                next();
            }
            else {
                String character = consumeUntil(true, '<');
                characters(character.toCharArray(), 0, character.length());
            }
        }
        endDocument();
    }

    protected void parseStartElement() throws IOException, SAXException {
        String tagName = consumeUntil(true, ' ', '>');
        tagName = tagName.toUpperCase();
        int ch = peek();
        AttributesImpl attributes = new AttributesImpl();
        if (ch != '>') {
            next();
            while (peek() != '>') {
                parseAttribute(attributes);
            }
        }
        next();
        startElement("", "", tagName, attributes);
    }

    protected void parseAttribute(AttributesImpl attributes) throws IOException, SAXException {
        String attributeName = consumeUntil(true, ' ', '=', '>');
        String value = null;
        int ch = peek();
        if (ch == '=') {
            next();
            ch = peek();
            if (ch == '"' || ch == '\'') {
                final int surroundingChar = next();
                value = consumeUntil(true, (char) surroundingChar);
            }
            else {
                value = consumeUntil(true, ' ', '<');
            }
        }
        next();
        attributes.addAttribute("", "", attributeName, "sometype", value);
    }

    protected void parseEndElement() throws IOException, SAXException {
        String name = consumeUntil(true, '>');
        next();
        endElement("", "", name);
    }
}
