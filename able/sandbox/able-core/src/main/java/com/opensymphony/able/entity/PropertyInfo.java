/*
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
package com.opensymphony.able.entity;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Enumeration;

import javax.persistence.Id;
import javax.persistence.Transient;

public class PropertyInfo {

	private final EntityInfo entity;
	private final PropertyDescriptor descriptor;
	private boolean idProperty;

	public PropertyInfo(EntityInfo entity, PropertyDescriptor descriptor) {
		this.entity = entity;
		this.descriptor = descriptor;
		Method readMethod = descriptor.getReadMethod();
		idProperty = readMethod != null && readMethod.getAnnotation(Id.class) != null;
	}

	public Enumeration<String> attributeNames() {
		return descriptor.attributeNames();
	}

	public String getDisplayName() {
		return descriptor.getDisplayName();
	}

	public String getName() {
		return descriptor.getName();
	}

	public Class<?> getPropertyType() {
		return descriptor.getPropertyType();
	}

	public String getShortDescription() {
		return descriptor.getShortDescription();
	}

	public boolean isReadable() {
		return descriptor.getReadMethod() != null;
	}

	/**
	 * Returns true if the property value is writable
	 */
	public boolean isWritable() {
		return descriptor.getWriteMethod() != null;
	}

	public boolean isFormProperty() {
		if (isWritable() && isReadable()) {
			if (descriptor.getReadMethod().getAnnotation(Transient.class) != null) {
				return false;
			}
			// TODO use an annotation to exclude from forms?
			return true;
		}
		return false;
	}

	public PropertyDescriptor getDescriptor() {
		return descriptor;
	}

	public EntityInfo getEntity() {
		return entity;
	}

	public boolean isIdProperty() {
		return idProperty;
	}
}
