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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * 
 * version $Revision: 1 $
 */
public class JaxbTemplate {

	private final Class entityType;

	public JaxbTemplate(Class entityType) {
		this.entityType = entityType;
	}

	public void write(OutputStream out, List objects) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(new Class[] {
				ObjectCollection.class, entityType });
		Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		ObjectCollection collection = new ObjectCollection();
		collection.setObjects(objects);
		
		System.out.println("#### writing object: " + collection);
		marshaller.marshal(collection, out);
	}

}