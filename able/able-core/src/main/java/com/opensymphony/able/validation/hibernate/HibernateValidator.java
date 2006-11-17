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

import com.opensymphony.able.action.Validator;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

/**
 * A helper class for validating the object
 * 
 * @version $Revision$
 */
public class HibernateValidator implements Validator {

    public void validate(ActionBeanContext actionBeanContext, String propertyPrefix, Object entity) {
        ClassValidator validator = ClassValidatorCache.getInstance().getValidator(entity.getClass());
        ValidationErrors validationErrors = actionBeanContext.getValidationErrors();
        InvalidValue[] invalidValues = validator.getInvalidValues(entity);
        for (InvalidValue value : invalidValues) {
            String fieldName = propertyPrefix + value.getPropertyPath();
            ValidationError error = new SimpleError(value.getMessage(), value.getValue());
            validationErrors.add(fieldName, error);
        }
    }
}
