/* 
 * Copyright 2014 Institute fml (TU Munich) and Institute FLW (TU Dortmund).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package graphXML;

/**
 *
 * @author Moritz Roidl, Orthodoxos Kipouridis
 */
import com.ctc.wstx.stax.WstxOutputFactory;

import it.uniroma1.dis.wsngroup.gexf4j.core.Gexf;
import it.uniroma1.dis.wsngroup.gexf4j.core.GexfWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.writer.GexfEntityWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * IndentingStaxGraphWriter class is an implementation of the GexfWriter interface that indents entries
 *
 */
public class IndentingStaxGraphWriter implements GexfWriter {

    @Override
    public void writeToStream(Gexf gexf, Writer out, String encoding) throws IOException {
        
        try {
            XMLOutputFactory outputFactory1 = WstxOutputFactory.newInstance();
            XMLStreamWriter streamWriter = outputFactory1.createXMLStreamWriter(out);
            streamWriter = new IndentingXMLStreamWriter(streamWriter);
            streamWriter.writeStartDocument(encoding, "1.0");

            new GexfEntityWriter(streamWriter, gexf);

            streamWriter.writeEndDocument();

            streamWriter.flush();
            streamWriter.close();

        } catch (XMLStreamException e) {
            throw new IOException("XML Exception: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void writeToStream(Gexf gexf, OutputStream out, String encoding) throws IOException {
        writeToStream(gexf, new OutputStreamWriter(out, encoding), encoding);
    }
}
