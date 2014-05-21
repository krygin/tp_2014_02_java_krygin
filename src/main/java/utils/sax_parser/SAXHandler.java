package utils.sax_parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import utils.ReflectionHelper;

/**
 * Created by Ivan on 18.05.2014 in 23:07.
 */
public class SAXHandler extends DefaultHandler {
    private static String CLASSNAME = "class";

    private String element = null;
    private Object object = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (!qName.equals(CLASSNAME)) {
            element = qName;
        } else {
            String className = attributes.getValue(0);
            object = ReflectionHelper.createInstance(className);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        element = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        if (element != null) {
            String value = new String(ch, start, length);
            ReflectionHelper.setFieldValue(object, element, value);
        }
    }

    public Object getObject() {
        return object;
    }
}
