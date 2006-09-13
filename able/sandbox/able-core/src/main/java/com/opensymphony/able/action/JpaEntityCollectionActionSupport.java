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
import java.util.Iterator;
import java.util.List;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.able.entity.EntityInfo;
import com.opensymphony.able.entity.Option;
import com.opensymphony.able.jaxb.JaxbResolution;
import com.opensymphony.able.jaxb.JaxbTemplate;

/**
 * A base {@link ActionBean} class for viewing and editing an embedded
 * collection of a CRUD based JPA entity
 * 
 * @version $Revision: 46 $
 */
public abstract class JpaEntityCollectionActionSupport<O, E> extends JpaActionSupport {
	private static final Log log = LogFactory.getLog(JpaEntityCollectionActionSupport.class);

	private String propertyName;
	private Object ownerId;
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

	/*
	 * public Resolution edit() { return new
	 * ForwardResolution(entityInfo.getEditUri()); }
	 */

	@DontValidate
	public Resolution delete() {
		List<E> list = getEntities();
		Iterator<E> iter = list.iterator();
		while (iter.hasNext()) {
			E e = iter.next();
			Object idValue = entityInfo.getIdValue(e);
			if (idValue != null) {
				iter.remove();
				shouldCommit();
			}
		}
		return new ForwardResolution(getHomeUri());
		// return new RedirectResolution(getHomeUri());
	}

	public Resolution save() {
		if (getContext().getValidationErrors().isEmpty()) {
			shouldCommit();
		}

		// TODO
		// getContext().addMsg( "saved " + getEntityName(); );

		String uri = entityInfo.getHomeUri();
		return new ForwardResolution(uri);
		// return new RedirectResolution(uri);
	}

	@DontValidate
	public Resolution cancel() {
		shouldRollback();

		// TODO
		// getContext().addMsg( Messages.cancelled( "Manufacturer" ) );

		// return new RedirectResolution(entityInfo.getHomeUri());
		return new ForwardResolution(getHomeUri());
	}

	protected String getHomeUri() {
		return ownerInfo.getHomeUriForCollection(propertyName);
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
	 * Returns a collection of {@link Option} objects to make it easy to render multi select menus
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
		String[] idValues = uriStrategy.getEntityPrimaryKeyValues(this);
		if (idValues != null && idValues.length > 0) {
			ownerId = ownerInfo.convertToPrimaryKeyValkue(idValues[0]);
		}
	}

	/**
	 * Looks up the owner by primary key
	 */
	protected O findOwnerByPrimaryKey() {
		log.info("Loading primaryKey Value: " + ownerId + " of type: " + ownerId.getClass());
		return getJpaTemplate().find(ownerClass, ownerId);
	}

	/**
	 * Allows the query to be overloaded for returning all of the items
	 */
	protected String getAllElementsQuery() {
		return entityInfo.getFindAllQuery();
	}
}
