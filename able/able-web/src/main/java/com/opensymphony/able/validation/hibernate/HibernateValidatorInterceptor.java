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
package com.opensymphony.able.validation.hibernate;

import com.opensymphony.able.action.CrudActionBean;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

/**
 * Adds support for Hibernate Annotations for validation
 * 
 * @version $Revision$
 */
public class HibernateValidatorInterceptor implements Interceptor {

    public Resolution intercept(ExecutionContext context) throws Exception {
        ActionBean actionBean = context.getActionBean();
        if (actionBean instanceof CrudActionBean) {
            CrudActionBean crudAction = (CrudActionBean) actionBean;
            Class entityClass = crudAction.getEntityClass();
            ClassValidator validator = ClassValidatorCache.getInstance().getValidator(entityClass);
            validate(context, validator, crudAction);
        }
        return context.proceed();
    }

    protected void validate(ExecutionContext context, ClassValidator validator, CrudActionBean crudAction) {
        ValidationErrors validationErrors = context.getActionBeanContext().getValidationErrors();
        Object entity = null;
        InvalidValue[] invalidValues = validator.getInvalidValues(entity);
        for (InvalidValue value : invalidValues) {
            String fieldName = "entity." + value.getPropertyPath();
            ValidationError error = new SimpleError(value.getMessage(), value.getValue());
            validationErrors.add(fieldName, error);
        }
    }

}
