package org.wuerthner.sport;

import org.junit.Test;
import org.wuerthner.sample.SampleBuilder;
import org.wuerthner.sample.SampleFactory;
import org.wuerthner.sample.School;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.core.Model;
import org.wuerthner.sport.core.XMLReader;
import org.wuerthner.sport.core.XMLWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class XMLTest {

    @Test
    public void testXMLReadWrite() {
        ModelElement school = new SampleBuilder("MHS", "Midland High School", 37, true)
                .addCourse("PH", "physics", 100)
                .addParticipant("JW", "Jan")
                .addParticipant("HX", "Hugo")
                .done()
                .addCourse("MA", "math", 120)
                .addParticipant("JW", "Jan")
                .addParticipant("EG", "Emil")
                .build();

        XMLWriter w = new XMLWriter();
        OutputStream os = new ByteArrayOutputStream();
        w.run(school, os);

        String xml = os.toString();

        XMLReader r = new XMLReader(new SampleFactory(), School.TYPE);
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        ModelElement root = r.run(is);

        System.out.println(Model.makeString(root));
    }
}
