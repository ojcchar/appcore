package seers.appcore.xml;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XMLHelperTest {

    @Test
    void readXMLContent() throws Exception {


        XmlObject obj = new XmlObject();
        obj.setId("1");
        obj.setName("obj1");
        String outputFile = "xmlobj.xml";

        XMLHelper.writeXML(obj, outputFile);

        try {
            XmlObject obj2 = XMLHelper.readXML(XmlObject.class, outputFile);

            System.out.println(obj2);

            assertEquals(obj, obj2);
        } finally {
            FileUtils.deleteQuietly(new File(outputFile));
        }
    }

    @XmlRootElement
    static class XmlObject{

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "XmlObject{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            XmlObject xmlObject = (XmlObject) o;
            return Objects.equals(id, xmlObject.id) && Objects.equals(name, xmlObject.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }
}