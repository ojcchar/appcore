package seers.appcore.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

	public static void writeXML(Class<?> class1, Object obj, File outputFile)
			throws JAXBException, FileNotFoundException {
		JAXBContext context = JAXBContext.newInstance(class1);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
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
		try (FileReader reader = new FileReader(file)) {
			SAXSource source = new SAXSource(xr, new InputSource(reader));

			T obj = (T) um.unmarshal(source);
			return obj;
		}
	}

}
