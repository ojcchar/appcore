package seers.appcore.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XMLHelper {

	private static final String DEFAULT_ENCODING = "UTF-8";

	@SuppressWarnings("unchecked")
	public static <T> T readXMLFromString(Class<?> class1, String content)
			throws JAXBException, FileNotFoundException, SAXException, ParserConfigurationException {
		JAXBContext context = JAXBContext.newInstance(class1);

		Unmarshaller um = context.createUnmarshaller();

		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		spf.setFeature("http://xml.org/sax/features/validation", false);

		XMLReader xr = (XMLReader) spf.newSAXParser().getXMLReader();
		try (StringReader reader = new StringReader(content)) {
			SAXSource source = new SAXSource(xr, new InputSource(reader));

			T obj = (T) um.unmarshal(source);
			return obj;
		}
	}

	public static <T> T readXML(Class<?> class1, String filepath)
			throws JAXBException, IOException, SAXException, ParserConfigurationException {
		return readXML(class1, new File(filepath));
	}

	@Deprecated
	public static void writeXML(Class<?> class1, Object obj, String outputFile)
			throws FileNotFoundException, JAXBException {
		writeXML(class1, obj, new File(outputFile));
	}

	@Deprecated
	public static void writeXML(Class<?> class1, Object obj, File outputFile)
			throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(class1);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(obj, outputFile);
	}

	public static void writeXML(Object obj, String outputFile) throws FileNotFoundException, JAXBException {
		writeXML(obj, new File(outputFile));
	}

	public static void writeXML(Object obj, File outputFile) throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_ENCODING, DEFAULT_ENCODING);
		m.marshal(obj, outputFile);
	}

	@SuppressWarnings("unchecked")
	public static <T> T readXML(Class<?> class1, File file)
			throws JAXBException, IOException, SAXException, ParserConfigurationException {
		JAXBContext context = JAXBContext.newInstance(class1);

		Unmarshaller um = context.createUnmarshaller();

		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		spf.setFeature("http://xml.org/sax/features/validation", false);

		XMLReader xr = (XMLReader) spf.newSAXParser().getXMLReader();
		try (Reader reader = new InputStreamReader(new FileInputStream(file), DEFAULT_ENCODING);) {
			SAXSource source = new SAXSource(xr, new InputSource(reader));

			T obj = (T) um.unmarshal(source);
			return obj;
		}
	}

}
