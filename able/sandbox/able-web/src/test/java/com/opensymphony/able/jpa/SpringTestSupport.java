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
package com.opensymphony.able.jpa;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.testng.Assert;

import java.net.URL;

/**
 * 
 * @version $Revision$
 */
public class SpringTestSupport {

    protected Object getMandatoryBean(ApplicationContext context, String name) {
        Object bean = context.getBean(name);
        Assert.assertNotNull(bean, "Failed to find bean in context: " + name);
        return bean;
    }

    protected AbstractXmlApplicationContext loadContext(String relativeOrAbsoluteClassPathUri) {
        URL url = getClass().getResource(relativeOrAbsoluteClassPathUri);
        if (url == null) {
            // lets try an absolute classpath URI
            url = getClass().getClassLoader().getResource(relativeOrAbsoluteClassPathUri);
        }
        Assert.assertNotNull(url, "Failed to load relativeClassPathUri: " + relativeOrAbsoluteClassPathUri);
        return new FileSystemXmlApplicationContext(url.toString());

    }
}
