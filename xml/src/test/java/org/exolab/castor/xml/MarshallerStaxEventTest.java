/*
 * Copyright 2011 Jakub Narloch
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.exolab.castor.xml;

import java.io.StringWriter;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;

/**
 * Tests the {@link Marshaller} class when marshalling the output into
 * {@link javax.xml.stream.XMLEventWriter}.
 *
 * @author <a herf="mailto:jmnarloch AT gmail DOT com">Jakub Narloch</a>
 * @version 1.3.3
 * @since 1.3.3
 */
public class MarshallerStaxEventTest extends BaseMarshallerTest {

  @Override
  protected String marshal(Marshaller marshaller, Object object) throws Exception {

    StringWriter writer = new StringWriter();
    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
    XMLEventWriter xmlEventWriter = outputFactory.createXMLEventWriter(writer);

    marshaller.setXmlEventWriter(xmlEventWriter);
    marshaller.marshal(object);

    return writer.toString();
  }
}
