package seers.appcore.xml;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.*;

public class XMLHelper {

    private static final String DEFAULT_ENCODING = "UTF-8";

    @SuppressWarnings("unchecked")
    public static <T> T readXMLFromString(Class<?> class1, String content)
            throws JAXBException, IOException, SAXException, ParserConfigurationException {
        try (Reader reader = new StringReader(content)) {
            return readXMLFromReader(class1, reader);
        }
    }

    public static <T> T readXML(Class<?> class1, String filepath)
            throws JAXBException, IOException, SAXException, ParserConfigurationException {
        return readXML(class1, new File(filepath));
    }

    @Deprecated
    public static void writeXML(Class<?> class1, Object obj, String outputFile)
            throws JAXBException {
        writeXML(class1, obj, new File(outputFile));
    }

    @Deprecated
    public static void writeXML(Class<?> class1, Object obj, File outputFile)
            throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(class1);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(obj, outputFile);
    }

    public static void writeXML(Object obj, String outputFile) throws JAXBException {
        writeXML(obj, new File(outputFile));
    }

    public static void writeXML(Object obj, File outputFile) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, DEFAULT_ENCODING);
        m.marshal(obj, outputFile);
    }

    @SuppressWarnings("unchecked")
    public static <T> T readXML(Class<?> class1, File file)
            throws JAXBException, IOException, SAXException, ParserConfigurationException {
        try (Reader reader = new InputStreamReader(new FileInputStream(file), DEFAULT_ENCODING);) {
            return readXMLFromReader(class1, reader);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readXMLContent(Class<?> class1, String content)
            throws JAXBException, IOException, SAXException, ParserConfigurationException {
        try (Reader reader = new StringReader(content)) {
            return readXMLFromReader(class1, reader);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readXMLFromReader(Class<?> class1, Reader reader)
            throws JAXBException, SAXException, ParserConfigurationException {
        JAXBContext context = JAXBContext.newInstance(class1);

        Unmarshaller um = context.createUnmarshaller();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        spf.setFeature("http://xml.org/sax/features/validation", false);

        XMLReader xr = (XMLReader) spf.newSAXParser().getXMLReader();
        SAXSource source = new SAXSource(xr, new InputSource(reader));

        T obj = (T) um.unmarshal(source);
        return obj;
    }

}
