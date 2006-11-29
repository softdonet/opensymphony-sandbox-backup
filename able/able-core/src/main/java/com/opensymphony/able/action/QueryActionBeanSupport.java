/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.able.action;

import com.opensymphony.able.annotations.Partial;
import com.opensymphony.able.stripes.DefaultResolution;
import com.opensymphony.able.stripes.GenerateResolution;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.Resolution;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * A useful base {@link ActionBean} for querying of an entity
 *
 * @version $Revision$
 */
public abstract class QueryActionBeanSupport<E> extends EntityActionBeanSupport<E> {

    protected QueryActionBeanSupport() {
    }

    protected QueryActionBeanSupport(Class<E> entityClass) {
        super(entityClass);
    }

    // Actions
    // -------------------------------------------------------------------------

    /**
     * Generates a tabular view
     */
    @DontValidate
    @DefaultHandler
    public Resolution list() {
        return new DefaultResolution(getClass(), getContext(), "/WEB-INF/jsp/generic/list.jsp");
    }

    @Partial
    @DontValidate
    public Resolution generateList() {
        return new GenerateResolution(getClass(), getContext());
    }

    public abstract List<E> getAllEntities();
}
