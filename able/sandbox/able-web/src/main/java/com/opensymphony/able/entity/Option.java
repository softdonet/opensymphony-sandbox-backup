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
package com.opensymphony.able.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A little helper class for creating multi select menus from collections of
 * entities
 * 
 * version $Revision: 1 $
 */
public class Option {

	private Object entity;
	private boolean selected;
	private Object value;
	private Object name;

	public static List<Option> createOptions(Collection allEntities, Collection currentChoices, EntityInfo info) {
		List<Option> answer = new ArrayList<Option>(allEntities.size());
		for (Object entity : allEntities) {
			Option option = new Option();
			option.setEntity(entity);
			option.setValue(info.getIdValue(entity));
			List<PropertyInfo> viewFieldProperties = info.getNameProperties();
			Object name = null;
			if (!viewFieldProperties.isEmpty()) {
				name = viewFieldProperties.get(0).getValue(entity);
			}
			if (name == null) {
				name = entity.toString();
			}
			option.setName(name);

			if (currentChoices.contains(entity)) {
				option.setSelected(true);
			}
			answer.add(option);
		}
		return answer;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
