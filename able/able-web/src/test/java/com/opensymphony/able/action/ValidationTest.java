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

import com.opensymphony.able.demo.action.PersonActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.mock.MockHttpServletRequest;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @version $Revision$
 */
public class ValidationTest {

    @Test(enabled = false)
    public void testValidationWithoutContainer() throws Throwable {
        PersonActionBean action = new PersonActionBean();

        Locale locale = Locale.getDefault();

        /*
        Configuration configuration = StripesFilter.getConfiguration();
        assertNotNull(configuration);
        LocalizationBundleFactory bundleFactory = configuration.getLocalizationBundleFactory();
        assertNotNull(bundleFactory);
        ResourceBundle bundle = bundleFactory.getFormFieldBundle(locale);
          */
        
        ActionBeanContext context = new ActionBeanContext() {
            @Override
            public Resolution getSourcePageResolution() {
                return new ForwardResolution("/sourcePage");
            }
        };
        MockHttpServletRequest request = new MockHttpServletRequest("/", "/person");
        request.getParameterMap().put("id", new String[]{"1234"});
        context.setRequest(request);

        action.setContext(context);

        Resolution resolution = action.save();

        ValidationErrors validationErrors = context.getValidationErrors();

        assertTrue(validationErrors.size() > 0, "Validation errors are: " + validationErrors);

        System.out.println("Errors: " + validationErrors);

        Set<Map.Entry<String, List<ValidationError>>> entries = validationErrors.entrySet();
        for (Map.Entry<String, List<ValidationError>> entry : entries) {
            List<ValidationError> list = entry.getValue();
            for (ValidationError validationError : list) {
                String message = validationError.getMessage(locale);

                System.out.println("Field: " + entry.getKey() + " message: " + message);
            }
        }
    }
}
