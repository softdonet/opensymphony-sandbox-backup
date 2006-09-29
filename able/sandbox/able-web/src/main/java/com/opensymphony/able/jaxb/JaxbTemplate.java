/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.able.jaxb;

import com.opensymphony.able.entity.EntityInfo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import java.io.OutputStream;

/**
 * 
 * version $Revision: 1 $
 */
public class JaxbTemplate {

	private Class[] types;

	public JaxbTemplate(Class[] types) {
		this.types = types;
	}

	public JaxbTemplate(Class entityType) {
		this( new Class[] {entityType });
	}

	public void write(OutputStream out, Object value) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(types);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(value, out);
	}

    public void writeElement(OutputStream out, Object value) throws JAXBException {
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }
        Class type = value.getClass();

        // lets deal with entities which have no root element annotation
        if (type.getAnnotation(XmlRootElement.class) == null) {
            QName qname = EntityInfo.newInstance(type).getQName();
            value = new JAXBElement(qname, type, value);
        }
        JAXBContext context = JAXBContext.newInstance(types);
		Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(value, out);
    }
}
