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

import com.opensymphony.able.entity.EntityInfo;
import com.opensymphony.able.entity.Option;
import com.opensymphony.able.jaxb.JaxbResolution;
import com.opensymphony.able.jaxb.JaxbTemplate;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A base {@link ActionBean} class for viewing and editing an embedded
 * collection of a CRUD based JPA entity
 * 
 * @version $Revision: 46 $
 */
public abstract class JpaEntityCollectionActionSupport<O, E> extends JpaActionSupport implements CrudActionBean {
	private static final Log log = LogFactory.getLog(JpaEntityCollectionActionSupport.class);

	private String propertyName;
	private Object ownerId;
	private Set entityIds;
	private O owner;
	private Class<O> ownerClass;
	private List<E> entities;
	private Class<E> entityClass;
	private EntityInfo ownerInfo;
	private EntityInfo entityInfo;
	private UriStrategy uriStrategy = new UriStrategy();

	public JpaEntityCollectionActionSupport(String propertyName) {
		this.propertyName = propertyName;
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		Type[] typeArguments = genericSuperclass.getActualTypeArguments();
		this.ownerClass = (Class<O>) typeArguments[0];
		this.entityClass = (Class<E>) typeArguments[1];
		this.ownerInfo = new EntityInfo(ownerClass);
		this.entityInfo = new EntityInfo(entityClass);
	}

	// Actions
	// -------------------------------------------------------------------------
	@DontValidate
	@DefaultHandler
	public Resolution view() {
		return new ForwardResolution(getHomeUri());
	}

	/**
	 * Lets remove the entities which were selected via the entityIds property
	 */
	@DontValidate
	public Resolution delete() {
		List<E> list = getEntities();
		Iterator<E> iter = list.iterator();
		while (iter.hasNext()) {
			E e = iter.next();
			Object idValue = entityInfo.getIdValue(e);
			if (entityIds.contains(idValue)) {
				iter.remove();
				shouldCommit();
			}
		}
		return new RedirectResolution(getHomeUri());
	}

	public Resolution save() {
		if (getContext().getValidationErrors().isEmpty()) {
			System.out.println("Delete with: Entities: " + getEntities());
			System.out.println("eids: " + entityIds);
			
			Iterator<E> iter = getEntities().iterator();
			while (iter.hasNext()) {
				E entity = iter.next();
				Object primaryKey = entityInfo.getIdValue(entity);
				boolean answer = entityIds.remove(primaryKey);
				if (!answer) {
					// lets remove this entity from the collection
					iter.remove();
				}
			}
			// now any primary keys left in the set need to be added
			for (Object entityId : entityIds) {
				// TODO we could do a more efficient batch query here
				E entity = findEntityByPrimaryKey(entityId);
				getEntities().add(entity);
			}
			shouldCommit();
		}

		// TODO
		// getContext().addMsg( "saved " + getEntityName(); );

		return new RedirectResolution(getHomeUri());
	}

	@DontValidate
	public Resolution cancel() {
		shouldRollback();

		// TODO
		// getContext().addMsg( Messages.cancelled( "Manufacturer" ) );

		return new RedirectResolution(getHomeUri());
	}

	protected String getHomeUri() {
		return ownerInfo.getHomeUri();
	}

	public Resolution xmlView() {
		return new JaxbResolution(new JaxbTemplate(entityClass), getEntities());
	}

	// Properties
	// -------------------------------------------------------------------------
	public O getOwner() {
		if (owner == null) {
			if (ownerId != null) {
				owner = findOwnerByPrimaryKey();
			}
		}
		return owner;
	}

	public List<E> getEntities() {
		if (entities == null) {
			O owner = getOwner();
			if (owner == null) {
				log.warn("No entity for oid: " + ownerId);
				entities = new ArrayList<E>();
			} else {
				entities = getOwnerCollection(owner);
			}
		}
		return entities;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public Class getIdClass() {
		return ownerInfo.getIdClass();
	}

	@SuppressWarnings("unchecked")
	public List<E> getAllEntities() {
		return query(getAllElementsQuery());
	}

	public EntityInfo getEntityInfo() {
		return entityInfo;
	}

	/**
	 * Returns a collection of {@link Option} objects to make it easy to render
	 * multi select menus
	 */
	public List<Option> getOptions() {
		return Option.createOptions(getAllEntities(), getEntities(), entityInfo);
	}

	// Implementation methods
	// -------------------------------------------------------------------------

	/**
	 * Looks up the owners related entities
	 */
	protected abstract List<E> getOwnerCollection(O owningEntity);

	@SuppressWarnings("unchecked")
	protected void preBind() {
		String idValue = uriStrategy.getOwnerPrimaryKeyValue(this);
		if (idValue != null) {
			ownerId = ownerInfo.convertToPrimaryKeyValkue(idValue);
		}
		entityIds = new HashSet();
		String[] values = uriStrategy.getEntityPrimaryKeyValues(this);
		if (values != null) {
			for (String value : values) {
				if (value != null) {
					Object entityId = ownerInfo.convertToPrimaryKeyValkue(value);
					entityIds.add(entityId);
				}
			}
		}
	}

	/**
	 * Looks up the owner by primary key
	 */
	protected O findOwnerByPrimaryKey() {
		return getJpaTemplate().find(ownerClass, ownerId);
	}

	/**
	 * Looks up an entity by primary key
	 */
	protected E findEntityByPrimaryKey(Object entityId) {
		return getJpaTemplate().find(entityClass, entityId);
	}

	/**
	 * Allows the query to be overloaded for returning all of the items
	 */
	protected String getAllElementsQuery() {
		return entityInfo.getFindAllQuery();
	}
}
