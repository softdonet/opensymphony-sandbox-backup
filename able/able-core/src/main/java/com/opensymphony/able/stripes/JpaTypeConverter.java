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
package com.opensymphony.able.stripes;

import com.opensymphony.able.introspect.EntityInfo;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.Collection;
import java.util.Locale;

public class JpaTypeConverter<T> implements TypeConverter<T> {
    private static final transient Log log = LogFactory.getLog(JpaTypeConverter.class);

    @SpringBean
    private JpaTemplate jpaTemplate;
    private EntityInfo entityInfo;

    public JpaTypeConverter(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    public void setLocale(Locale locale) {
    }

    public T convert(String input, Class<? extends T> targetType, Collection<ValidationError> errors) {
        Object primaryKey = null;
        if (input != null && input.length() > 0) {
            try {
                primaryKey = entityInfo.convertToPrimaryKeyValue(input);
            }
            catch (NumberFormatException e) {
                errors.add(new ScopedLocalizableError("converter.primaryKey", "invalidPrimaryKey"));
                return null;
            }
        }
        if (primaryKey == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("Looking up entity: " + entityInfo + " with primary key: " + primaryKey);
        }
        return (T) jpaTemplate.find(entityInfo.getEntityClass(), primaryKey);
    }
}
