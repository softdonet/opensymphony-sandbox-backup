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
package com.opensymphony.able.action;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.TypeConverter;

import com.opensymphony.able.entity.EntityInfo;

/**
 * Base class for any CRUD based JPA {@link ActionBean} to view or edit an
 * entity by ID
 * 
 * @version $Revision$
 */
public abstract class JpaCrudActionSupport<E> extends JpaActionSupport {
	private static final Log log = LogFactory
			.getLog(JpaCrudActionSupport.class);

	private List idList = new ArrayList();
	private List<E> entities;
	private List<E> bulkEditEntities;
	private E entity;
	private Class<E> entityClass;
	private EntityInfo entityInfo;
	private UriStrategy uriStrategy = new UriStrategy();
	private TypeConverter typeConverter = new BeanWrapperImpl();
	private int bulkEditCount = 5;

	public JpaCrudActionSupport() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		Type[] typeArguments = genericSuperclass.getActualTypeArguments();
		this.entityClass = (Class<E>) typeArguments[0];
		this.entityInfo = new EntityInfo(entityClass);
	}

	// Actions
	// -------------------------------------------------------------------------
	@DontValidate
	@DefaultHandler
	public Resolution view() {
		return new ForwardResolution(entityInfo.getViewUri());
	}

	public Resolution edit() {
		return new ForwardResolution(entityInfo.getEditUri());
	}

	public Resolution bulkEdit() {
		return new ForwardResolution(entityInfo.getBulkEditUri());
	}

	@DontValidate
	public Resolution delete() {
		List<E> list = getEntities();
		for (E e : list) {
			Object idValue = entityInfo.getIdValue(e);
			if (idValue != null) {
				getJpaTemplate().remove(e);
				shouldCommit();
			}
		}
		return new ForwardResolution(entityInfo.getHomeUri());
	}

	public Resolution save() {
		if (getContext().getValidationErrors().isEmpty()) {
			if (bulkEditEntities != null) {
				for (E e : bulkEditEntities) {
					save(e);
				}
			} else {
				save(getEntity());
			}
			shouldCommit();
		}

		// TODO
		// getContext().addMsg( "saved " + getEntityName(); );

		String uri = entityInfo.getHomeUri();
		return new RedirectResolution(uri);
	}

	protected void save(E e) {
		Object idValue = entityInfo.getIdValue(e);
		if (idValue == null) {
			getJpaTemplate().persist(e);
			idValue = entityInfo.getIdValue(e);
		}
	}

	@DontValidate
	public Resolution cancel() {
		shouldRollback();

		// TODO
		// getContext().addMsg( Messages.cancelled( "Manufacturer" ) );

		return new RedirectResolution(entityInfo.getHomeUri());
	}

	// Properties
	// -------------------------------------------------------------------------
	public E getEntity() {
		if (entity == null) {
			List<E> list = getEntities();
			if (list.isEmpty()) {
				entity = newInstance();
				list.add(entity);
			} else {
				// TODO if size > 1 should we warn?
				entity = list.get(0);
			}
		}
		return entity;
	}

	public List<E> getEntities() {
		if (entities == null) {
			int size = idList.size();
			entities = new ArrayList<E>(size);

			// TODO we could do a more efficient query?
			for (Object id : idList) {
				E entity = findByPrimaryKey(id);
				if (entity != null) {
					entities.add(entity);
				}
			}
		}
		return entities;
	}

	public List<E> getBulkEditEntities() {
		if (bulkEditEntities == null) {
			List<E> list = getEntities();
			int size = list.size();
			if (size == bulkEditCount) {
				bulkEditEntities = list;
			} else if (size > bulkEditCount) {
				bulkEditEntities = list.subList(0, bulkEditCount - 1);
			} else {
				bulkEditEntities = new ArrayList<E>(bulkEditCount);
				bulkEditEntities.addAll(list);
				while (bulkEditEntities.size() < bulkEditCount) {
					bulkEditEntities.add(newInstance());
				}
			}
		}
		return bulkEditEntities;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public Class getIdClass() {
		return entityInfo.getIdClass();
	}

	@SuppressWarnings("unchecked")
	public List<E> getAllEntities() {
		return query("from " + entityInfo.getEntityName());
	}

	public EntityInfo getEntityInfo() {
		return entityInfo;
	}

	public int getBulkEditCount() {
		return bulkEditCount;
	}

	public void setBulkEditCount(int bulkEditCount) {
		this.bulkEditCount = bulkEditCount;
	}

	// Implementation methods
	// -------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	protected void preBind() {
		String[] idValues = uriStrategy.getEntityPrimaryKeyValues(this);
		idList.clear();
		if (idValues != null) {
			for (String value : idValues) {
				Object id = convertToPrimaryKeyVale(value);
				if (id != null) {
					idList.add(id);
				}
			}
		}
	}

	protected Object convertToPrimaryKeyVale(String value) {
		if (value != null && value.length() > 0) {
			Class idClass = entityInfo.getIdClass();
			return typeConverter.convertIfNecessary(value, idClass);
		}
		return null;
	}

	/**
	 * Looks up the entity by primary key
	 */
	protected E findByPrimaryKey(Object id) {
		log.info("Loading primaryKey Value: " + id + " of type: "
				+ id.getClass());
		return getJpaTemplate().find(entityClass, id);
	}

	/**
	 * Creates a new instance of the entity class
	 */
	protected E newInstance() {
		try {
			return entityClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to instantiate class: "
					+ entityClass.getName() + ". Reason: " + e, e);
		}
	}
}
