package github.tornaco.android.thanos.widget.html;

import android.text.Editable;
import android.text.Html;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class WrapperContentHandler implements ContentHandler, Html.TagHandler {
    private ContentHandler mContentHandler;
    private WrapperTagHandler mTagHandler;
    private Editable mSpannableStringBuilder;

    public WrapperContentHandler(WrapperTagHandler tagHandler) {
        this.mTagHandler = tagHandler;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (mContentHandler == null) {
            mSpannableStringBuilder = output;
            mContentHandler = xmlReader.getContentHandler();
            xmlReader.setContentHandler(this);
        }
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        mContentHandler.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        mContentHandler.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        mContentHandler.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        mContentHandler.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        mContentHandler.endPrefixMapping(prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (!mTagHandler.handleTag(true, localName, mSpannableStringBuilder, attributes)) {
            mContentHandler.startElement(uri, localName, qName, attributes);
        }
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (!mTagHandler.handleTag(false, localName, mSpannableStringBuilder, null)) {
            mContentHandler.endElement(uri, localName, qName);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        mContentHandler.characters(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        mContentHandler.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        mContentHandler.processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        mContentHandler.skippedEntity(name);
    }

}
